package org.mars_sim.msp.config.model.people.descriptors;

import org.mars_sim.msp.config.model.people.Mbti;

/**
 * Class MbtiDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class MbtiDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public MbtiDescriptor() {
        super();
        _nsURI = "http://mars-sim.sourceforge.net/people";
        _xmlName = "mbti";
        _elementDefinition = true;
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_type", "type", org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Mbti target = (Mbti) object;
                return target.getType();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Mbti target = (Mbti) object;
                    target.setType((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Double.TYPE, "_percentage", "percentage", org.exolab.castor.xml.NodeType.Attribute);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Mbti target = (Mbti) object;
                if (!target.hasPercentage()) {
                    return null;
                }
                return new java.lang.Double(target.getPercentage());
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Mbti target = (Mbti) object;
                    if (value == null) {
                        target.deletePercentage();
                        return;
                    }
                    target.setPercentage(((java.lang.Double) value).doubleValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("double");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            org.exolab.castor.xml.validators.DoubleValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.DoubleValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setMinInclusive(-1.7976931348623157E308);
            typeValidator.setMaxInclusive(1.7976931348623157E308);
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
        return org.mars_sim.msp.config.model.people.Mbti.class;
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
