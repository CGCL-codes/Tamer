package org.xaware.common;

import java.util.ArrayList;
import java.util.Properties;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.xaware.common.exception.XAwareAdvisorException;

/**
 * This Class read the platforms extension registry for advisor extensions. Also validates the advisor.
 * 
 * @author venkatarama.satish
 * 
 */
public class AdvisorExtensionHandler {

    /** advisor extension point id */
    private static final String ADVISOR_EXTENSION_POINT_ID = "org.xaware.common.advisor";

    /** advisor attribute name */
    private static final String ADVISOR_ATTRIBUTE_NAME = "advisor";

    /**
     * Returns the advisor for the given component name; If the advisor is invalid or not found when required then
     * XAwareAdvisorException is thrown.
     * 
     * @param componentName
     *            name of the component.
     * @return Returns the advisor for the given component name;
     * @throws XAwareAdvisorException
     */
    @SuppressWarnings("serial")
    public static ArrayList<IAdvisor> getAdvisor(String componentName) throws XAwareAdvisorException {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] advisorConfigurationElements = registry.getConfigurationElementsFor(ADVISOR_EXTENSION_POINT_ID);
        Properties advisorProperties = new Properties();
        try {
            advisorProperties.load(AdvisorExtensionHandler.class.getResourceAsStream(IAdvisorFactory.ADVISOR_PROPERTIES_FILE));
        } catch (Exception e) {
            throw new XAwareAdvisorException("Unable to find " + IAdvisorFactory.ADVISOR_PROPERTIES_FILE + e.getMessage(), e);
        }
        String advisorContributorName = advisorProperties.getProperty(IAdvisorFactory.ADVISOR_BUNDLE_PROPERTY_NAME);
        if (advisorContributorName == null) {
            return new ArrayList<IAdvisor>();
        }
        if (advisorContributorName != null && (advisorConfigurationElements.length == 0)) {
            throw new XAwareAdvisorException("No advisor extensions are present for the specified 'advisorContributor' :" + advisorContributorName);
        }
        if (advisorConfigurationElements.length != 0) {
            if (advisorConfigurationElements.length > 1) {
                throw new XAwareAdvisorException("Only one advisor is allowed. advisors found :" + advisorConfigurationElements);
            }
            final IConfigurationElement advisorConfigurationElement = advisorConfigurationElements[0];
            if (!advisorConfigurationElement.isValid()) {
                throw new XAwareAdvisorException("Advisor extension '" + advisorConfigurationElement.getName() + "' is not valid ");
            }
            if (!advisorContributorName.equals(advisorConfigurationElement.getContributor().getName())) {
                throw new XAwareAdvisorException("Invalid advisor : " + advisorConfigurationElement.getContributor().getName());
            }
            try {
                final IAdvisor advisor = (IAdvisor) advisorConfigurationElement.createExecutableExtension(ADVISOR_ATTRIBUTE_NAME);
                advisor.setComponentName(componentName);
                advisor.initialize();
                return new ArrayList<IAdvisor>() {

                    {
                        add(advisor);
                    }
                };
            } catch (CoreException e1) {
                throw new XAwareAdvisorException("Exception while creating executable extension for :" + advisorConfigurationElement);
            }
        }
        return new ArrayList<IAdvisor>();
    }
}
