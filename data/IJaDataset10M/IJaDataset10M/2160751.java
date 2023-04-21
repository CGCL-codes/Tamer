package nl.alterra.openmi.sdk.spring;

import org.openmi.standard.ILinkableComponent;

/**
 * Interface for component groups to be used as bean by the Spring framework.
 */
public interface IComponentGroup {

    /**
     * Sets the link-able components in the group to the specified list of
     * components. For each ILinkableComponent in the list the addComponent
     * method of the group will be called.
     *
     * @param components ILinkableComponents to add
     */
    public void setLinkableComponents(ILinkableComponent[] components);

    /**
     * Sets the links in the group according to the specified list of link
     * descriptors. For each LinkDescriptor in the list the createLink method
     * of the group will be called. The link-able components must be added to
     * the group first, before setting the links.
     *
     * @param descriptors ILinkDescriptors to create links from
     */
    public void setLinkDescriptors(ILinkDescriptor[] descriptors);
}
