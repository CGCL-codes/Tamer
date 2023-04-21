package cn.vlabs.duckling.vwb.services.rpc.json;

import java.lang.reflect.Method;
import java.security.Permission;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import cn.vlabs.duckling.vwb.VWBContext;
import cn.vlabs.duckling.vwb.VWBSecurityException;
import cn.vlabs.duckling.vwb.VWBSession;
import cn.vlabs.duckling.vwb.VWBSite;
import cn.vlabs.duckling.vwb.services.auth.permissions.VWBPermission;
import cn.vlabs.duckling.vwb.services.rpc.RPCCallable;
import cn.vlabs.duckling.vwb.services.rpc.RPCService;
import com.metaparadigm.jsonrpc.InvocationCallback;
import com.metaparadigm.jsonrpc.JSONRPCBridge;

/**
 * 
 * @date Mar 13, 2010
 * @author xiejj@cnic.cn
 */
public final class JSONRPCService extends RPCService {

    private static final String JSONRPCBRIDGE = "JSONRPCBridge";

    private static HashMap c_globalObjects = new HashMap();

    /** Prevent instantiation */
    private JSONRPCService() {
        super();
    }

    /**
     *  Emits JavaScript to do a JSON RPC Call.  You would use this method e.g.
     *  in your plugin generation code to embed an AJAX call to your object.
     *  
     *  @param context The Wiki Context
     *  @param c An RPCCallable object
     *  @param function Name of the method to call
     *  @param params Parameters to pass to the method
     *  @return generated JavasSript code snippet that calls the method
     */
    public static String emitJSONCall(VWBContext context, RPCCallable c, String function, String params) {
        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("var result = jsonrpc." + getId(c) + "." + function + "(" + params + ");\r\n");
        sb.append("document.write(result);\r\n");
        sb.append("</script>");
        return sb.toString();
    }

    /**
     *  Finds this user's personal RPC Bridge.  If it does not exist, will
     *  create one and put it in the context.  If there is no HTTP Request included,
     *  returns the global bridge.
     *  
     *  @param context WikiContext to find the bridge in
     *  @return A JSON RPC Bridge
     */
    private static JSONRPCBridge getBridge(VWBContext context) {
        JSONRPCBridge bridge = null;
        HttpServletRequest req = context.getHttpRequest();
        if (req != null) {
            HttpSession hs = req.getSession();
            if (hs != null) {
                bridge = (JSONRPCBridge) hs.getAttribute(JSONRPCBRIDGE);
                if (bridge == null) {
                    bridge = new JSONRPCBridge();
                    hs.setAttribute(JSONRPCBRIDGE, bridge);
                }
            }
        }
        if (bridge == null) bridge = JSONRPCBridge.getGlobalBridge();
        bridge.setDebug(false);
        return bridge;
    }

    /**
     *  Registers a callable to JSON global bridge and requests JSON libraries to be added
     *  to the page.  
     *  
     *  @param context The WikiContext.
     *  @param c The RPCCallable to register
     *  @return the ID of the registered callable object
     */
    public static String registerJSONObject(VWBContext context, RPCCallable c) {
        String id = getId(c);
        getBridge(context).registerObject(id, c);
        requestJSON(context);
        return id;
    }

    /**
     *  Requests the JSON Javascript and object to be generated in the HTML.
     *  @param context The WikiContext.
     */
    public static void requestJSON(VWBContext context) {
        getBridge(context).registerCallback(new WikiJSONAccessor(), HttpServletRequest.class);
    }

    /**
     *  Provides access control to the JSON calls.  Rather private.
     *  Unfortunately we have to check the permission every single time, because
     *  the user can log in and we would need to reset the permissions at that time.
     *  Note that this is an obvious optimization piece if this becomes
     *  a bottleneck.
     *  
     *  @author Janne Jalkanen
     */
    static class WikiJSONAccessor implements InvocationCallback {

        private static final long serialVersionUID = 1L;

        private static final Logger log = Logger.getLogger(WikiJSONAccessor.class);

        /**
         *  Create an accessor.
         */
        public WikiJSONAccessor() {
        }

        /**
         *  Does not do anything.
         * 
         *  {@inheritDoc}
         */
        public void postInvoke(Object context, Object instance, Method method, Object result) throws Exception {
        }

        /**
         *  Checks access against the permission given.
         *  
         *  {@inheritDoc}
         */
        public void preInvoke(Object context, Object instance, Method method, Object[] arguments) throws Exception {
            if (context instanceof HttpServletRequest) {
                boolean canDo = false;
                HttpServletRequest req = (HttpServletRequest) context;
                VWBSession vwbsession = VWBSession.findSession(req);
                VWBSite site = VWBSite.findSite(req);
                for (Iterator i = c_globalObjects.values().iterator(); i.hasNext(); ) {
                    CallbackContainer cc = (CallbackContainer) i.next();
                    if (cc.m_object == instance) {
                        canDo = site.checkPermission(vwbsession, cc.m_permission);
                        break;
                    }
                }
                if (canDo) {
                    return;
                }
            }
            log.debug("Failed JSON permission check: " + instance);
            throw new VWBSecurityException("No permission to access this AJAX method!");
        }
    }

    /**
     *  Registers a global object (i.e. something which can be called by any
     *  JSP page).  Typical examples is e.g. "search".  By default, the RPCCallable
     *  shall need a "view" permission to access.
     *  
     *  @param id     The name under which this shall be registered (e.g. "search")
     *  @param object The RPCCallable which shall be associated to this id.
     */
    public static void registerGlobalObject(String id, RPCCallable object) {
        registerGlobalObject(id, object, VWBPermission.SEARCH);
    }

    /**
     *  Registers a global object (i.e. something which can be called by any
     *  JSP page) with a specific permission.  
     *  
     *  @param id     The name under which this shall be registered (e.g. "search")
     *  @param object The RPCCallable which shall be associated to this id.
     *  @param perm   The permission which is required to access this object.
     */
    public static void registerGlobalObject(String id, RPCCallable object, Permission perm) {
        CallbackContainer cc = new CallbackContainer();
        cc.m_permission = perm;
        cc.m_id = id;
        cc.m_object = object;
        c_globalObjects.put(id, cc);
    }

    /**
     *  Is called whenever a session is created.  This method creates a new JSONRPCBridge
     *  and adds it to the user session.  This is done because the global JSONRPCBridge
     *  InvocationCallbacks are not called; only session locals.  This may be a bug
     *  in JSON-RPC, or it may be a design feature...
     *  <p>
     *  The JSONRPCBridge object will go away once the session expires.
     *  
     *  @param session The HttpSession which was created.
     */
    public static void sessionCreated(HttpSession session) {
        JSONRPCBridge bridge = (JSONRPCBridge) session.getAttribute(JSONRPCBRIDGE);
        if (bridge == null) {
            bridge = new JSONRPCBridge();
            session.setAttribute(JSONRPCBRIDGE, bridge);
        }
        WikiJSONAccessor acc = new WikiJSONAccessor();
        bridge.registerCallback(acc, HttpServletRequest.class);
        for (Iterator i = c_globalObjects.values().iterator(); i.hasNext(); ) {
            CallbackContainer cc = (CallbackContainer) i.next();
            bridge.registerObject(cc.m_id, cc.m_object);
        }
    }

    /**
     *  Just stores the registered global method.
     *  
     *  @author Janne Jalkanen
     *
     */
    private static class CallbackContainer {

        String m_id;

        RPCCallable m_object;

        Permission m_permission;
    }
}
