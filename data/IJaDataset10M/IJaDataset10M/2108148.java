package org.nexopenframework.ide.eclipse.jst.datamodel.web.struts2;

import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Config class in order to easily add the values which develoepr has selected from wizard</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class Struts2FacetInstallConfig {

    private String actionExtension = "action";

    private String version = "2.0.9";

    public Struts2FacetInstallConfig() {
        super();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getActionExtension() {
        return actionExtension;
    }

    public void setActionExtension(final String urlPattern) {
        this.actionExtension = urlPattern;
    }

    public static final class Factory implements IActionConfigFactory {

        public Object create() {
            return new Struts2FacetInstallConfig();
        }
    }
}
