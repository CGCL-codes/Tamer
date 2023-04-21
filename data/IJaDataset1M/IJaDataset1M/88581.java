package org.mhpbox.si;

import java.util.Vector;
import javax.tv.service.selection.InsufficientResourcesException;
import javax.tv.service.selection.ServiceContext;
import javax.tv.service.selection.ServiceContextDestroyedEvent;
import javax.tv.service.selection.ServiceContextEvent;
import javax.tv.service.selection.ServiceContextException;
import javax.tv.service.selection.ServiceContextFactory;
import javax.tv.service.selection.ServiceContextListener;
import javax.tv.service.selection.ServiceContextPermission;

/**
 * This class serves as a factory for the creation of
 * <code>ServiceContext</code> objects.
 */
public class ServiceContextFactoryImpl extends ServiceContextFactory implements ServiceContextListener {

    Vector contexts = new Vector();

    /**
	 * Creates a <code>ServiceContextFactory</code>.
	 */
    public ServiceContextFactoryImpl() {
    }

    /**
	 * Creates a <code>ServiceContext</code> object. The new
	 * <code>ServiceContext</code> is created in the <em>not
	 * presenting</em>
	 * state. Note that implementations may limit the number of
	 * <code>ServiceContext</code> objects to a very small number, perhaps
	 * even one.
	 * @return A new <code>ServiceContext</code> object.
	 * @throws InsufficientResourcesException If the receiver lacks the
	 *             resources to create this <code>ServiceContext</code>.
	 * @throws SecurityException if the caller doesn't have
	 *             ServiceContextPermission("create")
	 */
    public ServiceContext createServiceContext() throws InsufficientResourcesException, SecurityException {
        ServiceContextPermission permission = new ServiceContextPermission("create", "own");
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(permission);
        }
        if (contexts.size() >= 1) {
            System.out.println("ServiceContext limit = " + contexts.size());
            throw new InsufficientResourcesException("number of contexts exceeds limits");
        }
        ServiceContext serviceContext = null;
        try {
            serviceContext = new ServiceContextImpl();
            contexts.addElement(serviceContext);
            serviceContext.addListener(this);
        } catch (Exception e) {
            throw new InsufficientResourcesException(e.toString());
        }
        return serviceContext;
    }

    /**
	 * Provides the <code>ServiceContext</code> instances to which the caller
	 * of the method is permitted access. If the caller has
	 * <code>ServiceContextPermission("access","*")</code>, then all current
	 * (i.e., undestroyed) <code>ServiceContext</code> instances are returned.
	 * If the application making this call is running in a
	 * <code>ServiceContext</code> and has
	 * <code>ServiceContextPermission("access","own")</code>, its own
	 * <code>ServiceContext</code> will be included in the returned array. If
	 * no <code>ServiceContext</code> instances are accessible to the caller,
	 * a zero-length array is returned. No <code>ServiceContext</code>
	 * instances in the <em>destroyed</em> state are returned by this method.
	 * @return An array of accessible <code>ServiceContext</code> objects.
	 * @see ServiceContextPermission
	 */
    public ServiceContext[] getServiceContexts() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                sm.checkPermission(new ServiceContextPermission("access", "*"));
            } catch (SecurityException e) {
                try {
                    sm.checkPermission(new ServiceContextPermission("access", "own"));
                } catch (SecurityException se) {
                    return new ServiceContext[] {};
                }
            }
        }
        int counter = 0;
        for (int i = 0; i < contexts.size(); i++) {
            ServiceContextImpl context = (ServiceContextImpl) contexts.elementAt(i);
            if (context.isDestroyed() == true) continue;
            counter++;
        }
        ServiceContext array[] = new ServiceContext[counter];
        counter = 0;
        for (int i = 0; i < contexts.size(); i++) {
            ServiceContextImpl context = (ServiceContextImpl) contexts.elementAt(i);
            if (context.isDestroyed() == true) continue;
            array[counter] = context;
            counter++;
        }
        return array;
    }

    /**
	 * Reports the <code>ServiceContext</code> in which the <code>Xlet</code>
	 * corresponding to the specified <code>XletContext</code> is running. The
	 * returned <code>ServiceContext</code> is the one from which the
	 * <code>Service</code> carrying the <code>Xlet</code> was selected.
	 * @param ctx The <code>XletContext</code> of the <code>Xlet</code> of
	 *            interest.
	 * @return The <code>ServiceContext</code> in which the <code>Xlet</code>
	 *         corresponding to <code>ctx</code> is running.
	 * @throws SecurityException If the <code>Xlet</code> corresponding to
	 *             <code>ctx</code> does not have
	 *             <code>ServiceContextPermission("access", "own")</code>.
	 * @throws ServiceContextException If the <code>Xlet</code> corresponding
	 *             to <code>ctx</code> is not running within a
	 *             <code>ServiceContext</code>.
	 */
    public ServiceContext getServiceContext(javax.tv.xlet.XletContext ctx) throws ServiceContextException {
        if (ctx == null) {
            throw new NullPointerException("XletContext null");
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new ServiceContextPermission("access", "own"));
        }
        ServiceContext sc = (ServiceContext) ctx.getXletProperty("javax.tv.xlet.service_context");
        if (sc == null) {
            throw new ServiceContextException("xlet not running in a ServiceContext");
        }
        if (sc instanceof ServiceContextImpl) {
            ServiceContextImpl impl = (ServiceContextImpl) sc;
            if (impl.isDestroyed() == true) {
                throw new ServiceContextException("ServiceContext is destroyed");
            }
        }
        return sc;
    }

    /**
	 * Removes a <code>ServiceContext</code> object. The old
	 * <code>ServiceContext</code> is removed from the factories list of
	 * services contexts. Removed service contexts are generally those which
	 * have been destroyed.
	 * @note this method is not part of the standard ServiceContextFactory API.
	 */
    public void receiveServiceContextEvent(ServiceContextEvent sce) {
        if (!(sce instanceof ServiceContextDestroyedEvent)) {
            return;
        }
        try {
            ServiceContext context = (ServiceContext) sce.getSource();
            if (context == null) {
                throw new NullPointerException("context == null");
            }
            for (int i = contexts.size() - 1; i >= 0; i--) {
                ServiceContext current = (ServiceContext) contexts.elementAt(i);
                if (current == context) {
                    contexts.removeElementAt(i);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("ReceiveServiceContextDestroyEvent implementation error.");
        }
    }
}
