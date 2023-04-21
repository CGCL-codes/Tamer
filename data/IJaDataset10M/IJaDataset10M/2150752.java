package de.schlund.pfixcore.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import net.sf.cglib.proxy.Enhancer;
import org.apache.log4j.Logger;
import org.pustefixframework.config.contextxmlservice.StateConfig;
import org.w3c.dom.Element;
import de.schlund.pfixcore.beans.InsertStatus;
import de.schlund.pfixcore.exception.PustefixApplicationException;
import de.schlund.pfixcore.workflow.Context;
import de.schlund.pfixcore.workflow.PageRequestStatus;
import de.schlund.pfixcore.workflow.State;
import de.schlund.pfixxml.PfixServletRequest;
import de.schlund.pfixxml.RequestParam;
import de.schlund.pfixxml.ResultDocument;
import de.schlund.pfixxml.SPDocument;

/**
 * 
 * @author Benjamin Reitzammer <benjamin@schlund.de>
 */
public class StateUtil {

    private static final Logger LOG = Logger.getLogger(StateUtil.class);

    private static final String MIMETYPE = "mimetype";

    private static final String HEADER = "responseheader";

    private static final String DEFAULTMIME = "text/html";

    public static ResultDocument createDefaultResultDocument(Context context, StateConfig config) throws Exception {
        ResultDocument resdoc = new ResultDocument();
        renderContextResources(context, resdoc, config);
        addResponseHeadersAndType(context, resdoc, config);
        return resdoc;
    }

    public static void renderContextResources(Context context, ResultDocument resdoc, StateConfig config) throws Exception {
        Map<String, ?> contextResources = config.getContextResources();
        for (String nodename : contextResources.keySet()) {
            Object cr = contextResources.get(nodename);
            renderContextResource(cr, resdoc, nodename);
        }
    }

    @SuppressWarnings("deprecation")
    private static void renderContextResource(Object cr, ResultDocument resdoc, String nodename) throws Exception {
        String classname = cr.getClass().getName();
        Class<?> clazz = cr.getClass();
        if (Enhancer.isEnhanced(clazz)) {
            clazz = clazz.getSuperclass();
            classname = clazz.getName();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("*** Auto appending status for " + classname + " at node " + nodename);
        }
        if (cr instanceof de.schlund.pfixcore.workflow.ContextResource) {
            LOG.debug("***** Resource implements ContextResource => calling insertStatus(...) of " + clazz.getName());
            ((de.schlund.pfixcore.workflow.ContextResource) cr).insertStatus(resdoc, resdoc.createNode(nodename));
        } else {
            boolean found_annotation = false;
            for (Method m : clazz.getMethods()) {
                if (m.isAnnotationPresent(InsertStatus.class)) {
                    Class<?>[] params = m.getParameterTypes();
                    Class<?> rettype = m.getReturnType();
                    if (params.length == 0 && rettype != null) {
                        LOG.debug("***** Found @InsertStatus for Object:" + m.getName() + "() of " + clazz.getName());
                        ResultDocument.addObject(resdoc.createNode(nodename), m.invoke(cr, new Object[] {}));
                    } else if (params.length == 1 && params[0].isAssignableFrom(Element.class)) {
                        LOG.debug("***** Found @InsertStatus for " + m.getName() + "(Element) of " + clazz.getName());
                        m.invoke(cr, resdoc.createNode(nodename));
                    } else if (params.length == 2 && params[0].isAssignableFrom(ResultDocument.class) && params[1].isAssignableFrom(Element.class)) {
                        LOG.debug("***** Found @InsertStatus for " + m.getName() + "(ResultDocument, Element) of " + clazz.getName());
                        m.invoke(cr, resdoc, resdoc.createNode(nodename));
                    } else {
                        throw new PustefixApplicationException("Exception when trying to call annotated method '@InsertStatus' " + "of " + clazz.getName() + ": Need either a signature of either " + "method(Element) or method(ResultDocument, Element)");
                    }
                    found_annotation = true;
                    break;
                }
            }
            if (!found_annotation) {
                LOG.debug("***** Serializing the complete resource " + clazz.getName());
                ResultDocument.addObject(resdoc.createNode(nodename), cr);
            }
        }
    }

    public static void addResponseHeadersAndType(Context context, ResultDocument resdoc, StateConfig config) {
        boolean have_config = true;
        Properties contextprops = context.getProperties();
        SPDocument doc = resdoc.getSPDocument();
        if (config == null) {
            have_config = false;
        }
        Properties props = null;
        String mime = null;
        if (have_config) {
            props = context.getPropertiesForCurrentPageRequest();
            mime = props.getProperty(MIMETYPE);
        }
        if (mime != null) {
            doc.setResponseContentType(mime);
        } else {
            doc.setResponseContentType(DEFAULTMIME);
        }
        HashMap<String, String> headers = PropertiesUtils.selectProperties(contextprops, HEADER);
        if (headers != null && !headers.isEmpty()) {
            for (Iterator<String> iter = headers.keySet().iterator(); iter.hasNext(); ) {
                String key = iter.next();
                String val = headers.get(key);
                LOG.debug("* Adding response header: " + key + " => " + val);
                doc.addResponseHeader(key, val);
            }
        }
        if (have_config) {
            headers = PropertiesUtils.selectProperties(props, HEADER);
            if (headers != null && !headers.isEmpty()) {
                for (Iterator<String> iter = headers.keySet().iterator(); iter.hasNext(); ) {
                    String key = iter.next();
                    String val = headers.get(key);
                    LOG.debug("* Adding response header: " + key + " => " + val);
                    doc.addResponseHeader(key, val);
                }
            }
        }
    }

    public static boolean isDirectTrigger(Context context, PfixServletRequest preq) {
        RequestParam sdreq = preq.getRequestParam(State.SENDDATA);
        return (!isPageFlowRunning(context) && (context.getCurrentStatus() == PageRequestStatus.JUMP || sdreq == null || !sdreq.isTrue()));
    }

    public static boolean isPageFlowRunning(Context context) {
        return (context.getCurrentStatus() == PageRequestStatus.WORKFLOW);
    }

    public static boolean isSubmitTrigger(Context context, PfixServletRequest preq) {
        return isSubmitTriggerHelper(context, preq.getRequestParam(State.SENDDATA));
    }

    public static boolean isSubmitAuthTrigger(Context context, PfixServletRequest preq) {
        return isSubmitTriggerHelper(context, preq.getRequestParam(State.SENDAUTHDATA));
    }

    private static boolean isSubmitTriggerHelper(Context context, RequestParam sdreq) {
        return (!isPageFlowRunning(context) && !(context.getCurrentStatus() == PageRequestStatus.JUMP) && sdreq != null && sdreq.isTrue());
    }
}
