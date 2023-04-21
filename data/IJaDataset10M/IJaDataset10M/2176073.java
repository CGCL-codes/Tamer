package org.smartcc;

import java.beans.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import org.smartcc.metadata.*;

/**
 * Invocation handler for the home interface proxy of a smart ref.
 *
 * @version $Revision: 1.2 $
 * @author <a href="mailto:hengels@innovidata.com">Holger Engels</a>
 */
public class SmartRefHome implements InvocationHandler {

    SmartRef smartRef;

    Delegate delegate = new Delegate();

    Map methods = new HashMap();

    public SmartRefHome(SmartRef smartRef) {
        this.smartRef = smartRef;
        try {
            methods.put(EJBHome.class.getMethod("getEJBMetaData", null), Delegate.class.getMethod("getEJBMetaData", null));
            methods.put(EJBHome.class.getMethod("remove", new Class[] { Object.class }), Delegate.class.getMethod("remove", new Class[] { Object.class }));
            methods.put(EJBHome.class.getMethod("remove", new Class[] { Handle.class }), Delegate.class.getMethod("remove", new Class[] { Handle.class }));
            methods.put(EJBHome.class.getMethod("getHomeHandle", null), Delegate.class.getMethod("getHomeHandle", null));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Forward invocations of create methods.
     * Barf, if remove is called, for remove isn't defined for session beans. 
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("create")) {
            Method delegateMethod = getMethod(method);
            if (delegateMethod != null) {
                Invocation invocation = new Invocation(null, delegateMethod, args);
                invocation.putValue(Invocation.SMARTREF, smartRef);
                try {
                    smartRef.setContextClassLoader();
                    RefHandle handle = new RefHandle((Handle) smartRef.getInterceptor().invoke(invocation));
                    return smartRef.create(args, handle);
                } finally {
                    smartRef.unsetContextClassLoader();
                }
            } else throw new EJBException("no such method: " + method);
        }
        Method delegateMethod = (Method) methods.get(method);
        if (delegateMethod != null) return delegateMethod.invoke(delegate, args);
        throw new EJBException("no such method: " + method);
    }

    Method getMethod(Method method) throws NoSuchMethodException {
        return smartRef.getHomeInterfaceClass().getMethod(method.getName(), method.getParameterTypes());
    }

    class Delegate implements EJBHome {

        public EJBMetaData getEJBMetaData() throws EJBException {
            return null;
        }

        public void remove(Object pk) throws RemoveException, EJBException {
            throw new EJBException("not allowed on session beans");
        }

        public void remove(Handle handle) throws RemoveException, EJBException {
            throw new EJBException("not allowed on session beans");
        }

        public HomeHandle getHomeHandle() throws EJBException {
            return null;
        }
    }
}
