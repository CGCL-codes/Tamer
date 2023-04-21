package org.hip.vif.internal;

import org.eclipse.core.runtime.IConfigurationElement;
import org.hip.vif.ApplicationConstants;
import org.hip.vif.menu.TaskConfiguration;
import org.osgi.framework.Bundle;

/**
 * Base class for helpers to register pluggable elements.
 *
 * @author Luthiger
 * Created: 14.07.2009
 */
public abstract class AbstractPluggableHelper {

    protected static final String ATTRIBUTE_ORDER = "order";

    protected static final String ATTRIBUTE_TITLE = "title";

    protected static final String ATTRIBUTE_MENU_PERMISSION = "menuPermission";

    protected static final String NL = System.getProperty("line.separator");

    private boolean isAdmin = false;

    private String namespaceID;

    public AbstractPluggableHelper(boolean inIsAdmin, IConfigurationElement inConfiguration, Bundle inBundle) {
        isAdmin = inIsAdmin;
        namespaceID = inBundle != null ? inBundle.getSymbolicName() : (inConfiguration != null ? inConfiguration.getNamespaceIdentifier() : "");
    }

    protected AbstractPluggableHelper(boolean inIsAdmin, String inNamespaceID) {
        isAdmin = inIsAdmin;
        namespaceID = inNamespaceID;
    }

    protected void registerAdditionalTasks(IConfigurationElement inAdditionalTasks, Bundle inBundle) {
        for (IConfigurationElement lChild : inAdditionalTasks.getChildren()) {
            if (ApplicationConstants.PARTLET_TASK_ID.equals(lChild.getName())) {
                new TaskConfiguration(lChild, isAdmin, inBundle, null);
            }
        }
    }

    /**
	 * Returns the specified attribute value from the <code>IConfigurationElement</code> or an empty string. 
	 * 
	 * @param inConfiguration IConfigurationElement
	 * @param inKey String
	 * @return String
	 */
    protected String getChecked(IConfigurationElement inConfiguration, String inKey) {
        String outValue = inConfiguration.getAttribute(inKey);
        return outValue == null ? "" : outValue.trim();
    }

    /**
	 * @return boolean <code>true</code> if this partlet contributes to the administration part of the forum.
	 */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
	 * Returns the namespace identifier of the plug-in implementing the <code>org.hip.vifapp.partlet</code> extension point.
	 * 
	 * @return String
	 */
    public String getNamespaceID() {
        return namespaceID;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + getNamespaceID();
    }
}
