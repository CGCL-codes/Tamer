package org.extwind.osgi.tapestry.internal.support;

import org.w3c.dom.Document;

/**
 * @author Donf Yang
 * 
 */
public interface ComponentDefinitionResolver {

    public static final String COMPONENT_NAMESPACE_URI = "http://www.extwind.org/schema/component";

    public static final String ELEMENT_COMPONENT = "component";

    public static final String ELEMENT_PACKAGE = "package";

    public static final String ELEMENT_MODULE = "module";

    public static final String ELEMENT_EXTENSION_POINTS = "extension-points";

    public static final String ELEMENT_EXTENSIONS = "extensions";

    public ComponentDefinition resolve(Document document) throws Exception;
}
