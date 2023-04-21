package oasis.names.tc.SAML.v2_0.assertion.descriptors;

import oasis.names.tc.SAML.v2_0.assertion.AdviceType;

/**
 * Class AdviceTypeDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class AdviceTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public AdviceTypeDescriptor() {
        super();
        _xmlName = "AdviceType";
        _elementDefinition = false;
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(oasis.names.tc.SAML.v2_0.assertion.AdviceTypeItem.class, "_items", (java.lang.String) null, org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AdviceType target = (AdviceType) object;
                return target.getAdviceTypeItem();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AdviceType target = (AdviceType) object;
                    target.addAdviceTypeItem((oasis.names.tc.SAML.v2_0.assertion.AdviceTypeItem) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    AdviceType target = (AdviceType) object;
                    target.removeAllAdviceTypeItem();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new oasis.names.tc.SAML.v2_0.assertion.AdviceTypeItem();
            }
        };
        desc.setSchemaType("list");
        desc.setComponentType("oasis.names.tc.SAML.v2_0.assertion.AdviceTypeItem");
        desc.setHandler(handler);
        desc.setContainer(true);
        desc.setClassDescriptor(new oasis.names.tc.SAML.v2_0.assertion.descriptors.AdviceTypeItemDescriptor());
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
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
        return oasis.names.tc.SAML.v2_0.assertion.AdviceType.class;
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
