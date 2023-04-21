package net.sourceforge.c4jplugin.internal.ui.viewers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchGroup;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.activities.WorkbenchActivityHelper;

public class LaunchGroupFilter extends ViewerFilter {

    private ILaunchGroup fGroup;

    /**
	 * Constructor for ExternalToolsLaunchConfigurationFilter.
	 */
    public LaunchGroupFilter(ILaunchGroup groupExtension) {
        super();
        fGroup = groupExtension;
    }

    /**
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        ILaunchConfigurationType type = null;
        ILaunchConfiguration config = null;
        if (parentElement instanceof LaunchConfigurationTypeTreeElement) {
            type = ((LaunchConfigurationTypeTreeElement) parentElement).getLaunchConfigurationType();
        }
        if (element instanceof LaunchConfigurationTypeTreeElement) {
            type = ((LaunchConfigurationTypeTreeElement) element).getLaunchConfigurationType();
        }
        if (element instanceof LaunchConfigurationTreeElement) {
            config = ((LaunchConfigurationTreeElement) element).getLaunchConfiguration();
            try {
                type = config.getType();
            } catch (CoreException e) {
            }
        }
        boolean priv = false;
        if (config != null) {
            try {
                priv = config.getAttribute(IDebugUIConstants.ATTR_PRIVATE, false);
            } catch (CoreException e) {
            }
        }
        if (type != null) {
            return !priv && type.supportsMode(fGroup.getMode()) && equalCategories(type.getCategory(), fGroup.getCategory()) && !WorkbenchActivityHelper.filterItem(new LaunchConfigurationTypeContribution(type));
        }
        return false;
    }

    /**
	 * Returns whether the given categories are equal.
	 * 
	 * @param c1 category identifier or <code>null</code>
	 * @param c2 category identifier or <code>null</code>
	 * @return boolean
	 */
    private boolean equalCategories(String c1, String c2) {
        if (c1 == null || c2 == null) {
            return c1 == c2;
        }
        return c1.equals(c2);
    }
}
