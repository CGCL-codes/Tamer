package org.gems.designer.model.props;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public interface CustomPropertyProvider {

    public static final String EXTENSION_POINT = "org.gems.designer.dsml.propertyprovider";

    public String[] getProvidedTypes();

    public CustomProperty getProperty(String dat);

    public IPropertyDescriptor[] getParameterizationProperties(String dat);
}
