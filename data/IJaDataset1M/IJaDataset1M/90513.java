package com.ail.core.configure;

import com.ail.core.Type;

/**
 * This class holds XML databinding information for a configuration. It holds
 * the mapping description - which is generally a string of XML, and transient
 * instances of a marshaller and unmarshaller object.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/XMLMapping.java,v $
 */
public class XMLMapping extends Type {

    static final long serialVersionUID = 5193477041052835669L;

    private String definition = null;

    private transient Object marshaller = null;

    private transient Object unmarshaller = null;

    /**
     * Default constructor.
     */
    public XMLMapping() {
    }

    /**
     * Get the XML mapping defnition. The actual format of the information
     * returned depends upon the data binding mechenism being used, but it
     * is generally an XML string that describes how XML nodes are mapped into
     * object instances.
     * @return String describing the mapping.
     */
    public String getDefinition() {
        return definition;
    }

    public String getDefinitionCDATA() {
        return "<![CDATA[" + definition + "]]>";
    }

    /**
     * Set the XML mapping string
     * @see #getDefinition
     * @param definition The mapping string.
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * Get the XML marshaller. The XML databinding handlers may use this
     * property to cache an instance of the marshaller. The configuration
     * handler will ensure that this is handled in a version safe fashion.
     * @return An instance of a marshaller.
     */
    public Object getMarshaller() {
        return marshaller;
    }

    /**
     * Set the marshaller for this mapping.
     * @see #getMarshaller
     * @param marshaller The marshaller instance
     */
    public void setMarshaller(Object marshaller) {
        this.marshaller = marshaller;
    }

    /**
     * Get the unmarshaller.
     * @see #getMarshaller
     * @return An instance of the marshaller.
     */
    public Object getUnmarshaller() {
        return unmarshaller;
    }

    /**
     * Set the unmarshaller.
     * @see #getMarshaller
     * @param unmarshaller The XML Unmarshaller.
     */
    public void setUnmarshaller(Object unmarshaller) {
        this.unmarshaller = unmarshaller;
    }
}
