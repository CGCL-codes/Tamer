package com.gele.tools.dbedit.castor;

/**
 * Class DbGUISequenceItemDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class DbGUISequenceItemDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    /**
   * Field elementDefinition
   */
    private boolean elementDefinition;

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

    public DbGUISequenceItemDescriptor() {
        super();
        elementDefinition = false;
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Object.class, "_dummy", "dummy", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUISequenceItem target = (DbGUISequenceItem) object;
                return target.getDummy();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUISequenceItem target = (DbGUISequenceItem) object;
                    target.setDummy((java.lang.Object) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new java.lang.Object();
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.gele.tools.dbedit.castor.DbColumn.class, "_dbColumn", "dbColumn", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUISequenceItem target = (DbGUISequenceItem) object;
                return target.getDbColumn();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUISequenceItem target = (DbGUISequenceItem) object;
                    target.setDbColumn((com.gele.tools.dbedit.castor.DbColumn) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.gele.tools.dbedit.castor.DbColumn();
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.gele.tools.dbedit.castor.ExternalRef.class, "_externalRef", "externalRef", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUISequenceItem target = (DbGUISequenceItem) object;
                return target.getExternalRef();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUISequenceItem target = (DbGUISequenceItem) object;
                    target.setExternalRef((com.gele.tools.dbedit.castor.ExternalRef) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.gele.tools.dbedit.castor.ExternalRef();
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.gele.tools.dbedit.castor.One2Many.class, "_one2Many", "one2Many", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUISequenceItem target = (DbGUISequenceItem) object;
                return target.getOne2Many();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUISequenceItem target = (DbGUISequenceItem) object;
                    target.setOne2Many((com.gele.tools.dbedit.castor.One2Many) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.gele.tools.dbedit.castor.One2Many();
            }
        };
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
        return com.gele.tools.dbedit.castor.DbGUISequenceItem.class;
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

    /**
   * Method isElementDefinition
   * 
   * 
   * 
   * @return boolean
   */
    public boolean isElementDefinition() {
        return elementDefinition;
    }
}
