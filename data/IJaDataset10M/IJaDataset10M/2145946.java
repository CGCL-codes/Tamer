package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;
import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for ComponentID. 
 * 
 * @author M. Ranganathan
 * @author martins
 * 
 */
public class ComponentIDPropertyEditor extends TextPropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {
        try {
            String componentString = text;
            String componentType = componentString.substring(0, componentString.indexOf('['));
            componentString = componentString.substring(componentType.length() + 1, componentString.length() - 1);
            String[] componentStringParts = componentString.split(",");
            String componentName = componentStringParts[0].substring("name=".length());
            String componentVendor = componentStringParts[1].substring("vendor=".length());
            String componentVersion = componentStringParts[2].substring("version=".length());
            if (componentType.equalsIgnoreCase("LibraryID")) {
                this.setValue(new LibraryID(componentName, componentVendor, componentVersion));
            } else if (componentType.equalsIgnoreCase("EventTypeID")) {
                this.setValue(new EventTypeID(componentName, componentVendor, componentVersion));
            } else if (componentType.equalsIgnoreCase("ProfileSpecificationID")) {
                this.setValue(new ProfileSpecificationID(componentName, componentVendor, componentVersion));
            } else if (componentType.equalsIgnoreCase("ResourceAdaptorTypeID")) {
                this.setValue(new ResourceAdaptorTypeID(componentName, componentVendor, componentVersion));
            } else if (componentType.equalsIgnoreCase("ResourceAdaptorID")) {
                this.setValue(new ResourceAdaptorID(componentName, componentVendor, componentVersion));
            } else if (componentType.equalsIgnoreCase("SbbID")) {
                this.setValue(new SbbID(componentName, componentVendor, componentVersion));
            } else if (componentType.equalsIgnoreCase("ServiceID")) {
                this.setValue(new ServiceID(componentName, componentVendor, componentVersion));
            } else throw new IllegalArgumentException("bad component type! " + componentType);
        } catch (Throwable ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
