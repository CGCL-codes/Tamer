package uk.ac.essex.ia.action.xml;

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
 * @version $Revision: 1.2 $ $Date: 2002/12/22 20:28:16 $
**/
public class SeperatorDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public SeperatorDescriptor() {
        super();
        xmlName = "seperator";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        FieldValidator fieldValidator = null;
        desc = new XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_index", "index", NodeType.Attribute);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Seperator target = (Seperator) object;
                if (!target.hasIndex()) return null;
                return new Integer(target.getIndex());
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Seperator target = (Seperator) object;
                    if (value == null) {
                        target.deleteIndex();
                        return;
                    }
                    target.setIndex(((Integer) value).intValue());
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
            IntegerValidator iv = new IntegerValidator();
            fieldValidator.setValidator(iv);
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
        return uk.ac.essex.ia.action.xml.Seperator.class;
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
