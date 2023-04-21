package com.iver.andami.config.generate;

/**
 * Class AndamiOptionsDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class AndamiOptionsDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public AndamiOptionsDescriptor() {
        super();
        xmlName = "andami-options";
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.iver.andami.config.generate.Option.class, "_optionList", "option", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AndamiOptions target = (AndamiOptions) object;
                return target.getOption();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AndamiOptions target = (AndamiOptions) object;
                    target.addOption((com.iver.andami.config.generate.Option) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.iver.andami.config.generate.Option();
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.iver.andami.config.generate.IconTheme.class, "_iconTheme", "icon-theme", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AndamiOptions target = (AndamiOptions) object;
                return target.getIconTheme();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    AndamiOptions target = (AndamiOptions) object;
                    target.setIconTheme((com.iver.andami.config.generate.IconTheme) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.iver.andami.config.generate.IconTheme();
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);
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
        return com.iver.andami.config.generate.AndamiOptions.class;
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
