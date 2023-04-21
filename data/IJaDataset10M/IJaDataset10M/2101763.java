package com.calipso.reportgenerator.reportdefinitions.types;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class ParameterValueFilterParameterTypeDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class ParameterValueFilterParameterTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public ParameterValueFilterParameterTypeDescriptor() {
        super();
        xmlName = "ParameterValueFilterParameterType";
    }

    /**
     * Method getAccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
     * Method getExtends
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    }

    /**
     * Method getIdentity
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    }

    /**
     * Method getJavaClass
     */
    public java.lang.Class getJavaClass() {
        return com.calipso.reportgenerator.reportdefinitions.types.ParameterValueFilterParameterType.class;
    }

    /**
     * Method getNameSpacePrefix
     */
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    }

    /**
     * Method getNameSpaceURI
     */
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    }

    /**
     * Method getValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    /**
     * Method getXMLName
     */
    public java.lang.String getXMLName() {
        return xmlName;
    }
}
