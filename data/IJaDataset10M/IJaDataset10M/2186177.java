package uk.ac.ncl.neresc.dynasoar.messages;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class AddServiceRequestDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class AddServiceRequestDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public AddServiceRequestDescriptor() {
        super();
        nsURI = "http://ncl.ac.uk/2006/dynasoar/messages";
        xmlName = "AddServiceRequest";
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_serviceID", "serviceID", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AddServiceRequest target = (AddServiceRequest) object;
                return target.getServiceID();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AddServiceRequest target = (AddServiceRequest) object;
                    target.setServiceID((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/messages");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_serviceURI", "serviceURI", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AddServiceRequest target = (AddServiceRequest) object;
                return target.getServiceURI();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AddServiceRequest target = (AddServiceRequest) object;
                    target.setServiceURI((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/messages");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_wsdlLocation", "wsdlLocation", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AddServiceRequest target = (AddServiceRequest) object;
                return target.getWsdlLocation();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AddServiceRequest target = (AddServiceRequest) object;
                    target.setWsdlLocation((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://ncl.ac.uk/2006/dynasoar/messages");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
    }

    /**
     * Method getAccessMode
     * 
     * 
     * 
     * @return AccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
     * Method getExtends
     * 
     * 
     * 
     * @return ClassDescriptor
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    }

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return FieldDescriptor
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    }

    /**
     * Method getJavaClass
     * 
     * 
     * 
     * @return Class
     */
    public java.lang.Class getJavaClass() {
        return uk.ac.ncl.neresc.dynasoar.messages.AddServiceRequest.class;
    }

    /**
     * Method getNameSpacePrefix
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    }

    /**
     * Method getNameSpaceURI
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    }

    /**
     * Method getValidator
     * 
     * 
     * 
     * @return TypeValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    /**
     * Method getXMLName
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getXMLName() {
        return xmlName;
    }
}
