package javax.faces.context;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;

/**
 * @author Simon Lessard (latest modification by $Author: slessard $)
 * @version $Revision: 696523 $ $Date: 2009-03-14 17:35:31 -0400 (mer., 17 sept. 2008) $
 * 
 * @since 2.0
 */
public abstract class ExternalContextFactory implements FacesWrapper<ExternalContextFactory> {

    public abstract ExternalContext getExternalContext(Object context, Object request, Object response) throws FacesException;

    public ExternalContextFactory getWrapped() {
        return null;
    }
}
