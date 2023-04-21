package javax.faces.lifecycle;

import java.util.Iterator;
import javax.faces.FacesWrapper;

/**
 * see Javadoc of <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/index.html">JSF Specification</a>
 *
 * @author Manfred Geiler (latest modification by $Author: lu4242 $)
 * @version $Revision: 723197 $ $Date: 2008-12-03 21:56:39 -0500 (Wed, 03 Dec 2008) $
 */
public abstract class LifecycleFactory implements FacesWrapper<LifecycleFactory> {

    public static final java.lang.String DEFAULT_LIFECYCLE = "DEFAULT";

    public abstract void addLifecycle(String lifecycleId, Lifecycle lifecycle);

    public abstract Lifecycle getLifecycle(String lifecycleId);

    public abstract Iterator<String> getLifecycleIds();

    /**
     * If this factory has been decorated, the implementation doing the decorating may override this method to 
     * provide access to the implementation being wrapped. A default implementation is provided that returns 
     * <code>null</code>.
     * 
     * @return the decorated <code>LifecycleFactory</code> if this factory decorates another, 
     *         or <code>null</code> otherwise
     * 
     * @since 2.0
     */
    public LifecycleFactory getWrapped() {
        return null;
    }
}
