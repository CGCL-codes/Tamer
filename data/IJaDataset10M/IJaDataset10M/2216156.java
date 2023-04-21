package org.openedc.web.util;

import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.openedc.service.MessageResourceResolver;

/**
 *
 * @author peter
 */
public class FacesMessageResourceResolver implements MessageResourceResolver {

    private String bundleName;

    @Override
    public ResourceBundle getBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, bundleName);
        return bundle;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }
}
