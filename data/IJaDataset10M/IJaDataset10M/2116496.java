package org.nexopenframework.faces.context;

import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.nexopenframework.context.framework.Context;
import org.nexopenframework.context.framework.ContextAdaptor;
import org.nexopenframework.context.framework.Contexts;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class ApplicationFacesContext extends ContextAdaptor implements Context {

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.context.framework.ContextAdaptor#getScope()
	 */
    public int getScope() {
        return Contexts.APPLICATION;
    }

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.context.framework.ContextAdaptor#getAttribute(java.lang.String)
	 */
    public Object getAttribute(String name) {
        FacesContext current = FacesContext.getCurrentInstance();
        if (current == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No FacesContext available, just skipping.");
            }
            return null;
        }
        ExternalContext context = current.getExternalContext();
        Map sessionMap = context.getApplicationMap();
        Object value = sessionMap.get(name);
        return value;
    }
}
