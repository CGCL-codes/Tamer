package org.ws4d.java.xml.schema;

import org.ws4d.java.service.ParameterType;

/**
 * Objects of this class represent XMLSchema elements.
 *
 */
public class XMLSchemaElement {

    private String name = null;

    private String namespace = null;

    private ParameterType type = null;

    public XMLSchemaElement(String name, String namespace, ParameterType type) {
        this.name = name;
        this.namespace = namespace;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public ParameterType getType() {
        return type;
    }

    public String toString() {
        return namespace + ":" + name;
    }
}
