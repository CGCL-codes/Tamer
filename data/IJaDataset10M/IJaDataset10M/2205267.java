package org.apptools.data;

/**Describes a single plugin object*/
public class PlugInDescriptor extends AbstractDataDescriptor {

    public PlugInDescriptor(String name, String description, String propertyName, boolean required) {
        super(name, description, propertyName, required);
    }

    public Class getDataType() {
        return Object.class;
    }
}
