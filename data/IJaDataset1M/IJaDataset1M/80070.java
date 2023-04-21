package oasis.names.tc.SAML.v2_0.assertion.descriptors;

import oasis.names.tc.SAML.v2_0.assertion.AuthnContextTypeChoiceSequenceChoice;

/**
 * Class AuthnContextTypeChoiceSequenceChoiceDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class AuthnContextTypeChoiceSequenceChoiceDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;

    public AuthnContextTypeChoiceSequenceChoiceDescriptor() {
        super();
        _nsURI = "urn:oasis:names:tc:SAML:2.0:assertion";
        _elementDefinition = false;
        setCompositorAsChoice();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Object.class, "_authnContextDecl", "AuthnContextDecl", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AuthnContextTypeChoiceSequenceChoice target = (AuthnContextTypeChoiceSequenceChoice) object;
                return target.getAuthnContextDecl();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AuthnContextTypeChoiceSequenceChoice target = (AuthnContextTypeChoiceSequenceChoice) object;
                    target.setAuthnContextDecl((java.lang.Object) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new java.lang.Object();
            }
        };
        desc.setSchemaType("java.lang.Object");
        desc.setHandler(handler);
        desc.setNameSpaceURI("urn:oasis:names:tc:SAML:2.0:assertion");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_authnContextDeclRef", "AuthnContextDeclRef", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AuthnContextTypeChoiceSequenceChoice target = (AuthnContextTypeChoiceSequenceChoice) object;
                return target.getAuthnContextDeclRef();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AuthnContextTypeChoiceSequenceChoice target = (AuthnContextTypeChoiceSequenceChoice) object;
                    target.setAuthnContextDeclRef((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new java.lang.String();
            }
        };
        desc.setSchemaType("anyURI");
        desc.setHandler(handler);
        desc.setNameSpaceURI("urn:oasis:names:tc:SAML:2.0:assertion");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);
    }

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    @Override()
    public java.lang.Class getJavaClass() {
        return oasis.names.tc.SAML.v2_0.assertion.AuthnContextTypeChoiceSequenceChoice.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpacePrefix() {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpaceURI() {
        return _nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    @Override()
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    @Override()
    public java.lang.String getXMLName() {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition() {
        return _elementDefinition;
    }
}
