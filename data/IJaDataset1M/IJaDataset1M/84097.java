package uk.ac.essex.common.lang.xml.castor;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.handlers.*;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.*;

/**
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/26 16:53:38 $
**/
public class LocaleDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public LocaleDescriptor() {
        super();
        xmlName = "locale";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        FieldValidator fieldValidator = null;
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_language", "language", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Locale target = (Locale) object;
                return target.getLanguage();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Locale target = (Locale) object;
                    target.setLanguage((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_country", "country", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Locale target = (Locale) object;
                return target.getCountry();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Locale target = (Locale) object;
                    target.setCountry((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);
    }

    /**
    **/
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
    **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    }

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    }

    /**
    **/
    public java.lang.Class getJavaClass() {
        return uk.ac.essex.common.lang.xml.castor.Locale.class;
    }

    /**
    **/
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    }

    /**
    **/
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    }

    /**
    **/
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    /**
    **/
    public java.lang.String getXMLName() {
        return xmlName;
    }
}
