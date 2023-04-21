package javax.faces.context;

import javax.faces.FacesWrapper;

/**
 * 
 * @since 2.0
 * @author Leonardo Uribe (latest modification by $Author: slessard $)
 * @version $Revision: 761981 $ $Date: 2009-04-04 13:44:14 -0500 (Sat, 04 Apr 2009) $
 */
public abstract class ExceptionHandlerFactory implements FacesWrapper<ExceptionHandlerFactory> {

    public abstract ExceptionHandler getExceptionHandler();

    /**
     * If this factory has been decorated, the implementation doing the decorating may override this method to provide
     * access to the implementation being wrapped. A default implementation is provided that returns <code>null</code>.
     * 
     * @return the decorated <code>DiscoveryHandlerFactory</code> if this factory decorates another, or
     *         <code>null</code> otherwise
     * 
     * @since 2.0
     */
    public ExceptionHandlerFactory getWrapped() {
        return null;
    }
}
