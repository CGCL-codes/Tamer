package com.gele.tools.dbedit.castor;

import org.exolab.castor.xml.validators.StringValidator;

/**
 * Class DbGUIDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class DbGUIDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public DbGUIDescriptor() {
        super();
        xmlName = "dbGUI";
        elementDefinition = true;
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_showSQL", "showSQL", org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUI target = (DbGUI) object;
                return target.getShowSQL();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUI target = (DbGUI) object;
                    target.setShowSQL((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_title", "title", org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUI target = (DbGUI) object;
                return target.getTitle();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUI target = (DbGUI) object;
                    target.setTitle((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_preferredSize", "preferredSize", org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUI target = (DbGUI) object;
                return target.getPreferredSize();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUI target = (DbGUI) object;
                    target.setPreferredSize((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        {
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.gele.tools.dbedit.castor.MainTable.class, "_mainTable", "mainTable", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUI target = (DbGUI) object;
                return target.getMainTable();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUI target = (DbGUI) object;
                    target.setMainTable((com.gele.tools.dbedit.castor.MainTable) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.gele.tools.dbedit.castor.MainTable();
            }
        };
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.gele.tools.dbedit.castor.FindBy.class, "_findBy", "findBy", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUI target = (DbGUI) object;
                return target.getFindBy();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUI target = (DbGUI) object;
                    target.setFindBy((com.gele.tools.dbedit.castor.FindBy) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.gele.tools.dbedit.castor.FindBy();
            }
        };
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.gele.tools.dbedit.castor.DbGUISequence.class, "_dbGUISequenceList", "-error-if-this-is-used-", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUI target = (DbGUI) object;
                return target.getDbGUISequence();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUI target = (DbGUI) object;
                    target.addDbGUISequence((com.gele.tools.dbedit.castor.DbGUISequence) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.gele.tools.dbedit.castor.DbGUISequence();
            }
        };
        desc.setHandler(handler);
        desc.setContainer(true);
        desc.setClassDescriptor(new com.gele.tools.dbedit.castor.DbGUISequenceDescriptor());
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        {
        }
        desc.setValidator(fieldValidator);
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.gele.tools.dbedit.castor.About.class, "_about", "about", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                DbGUI target = (DbGUI) object;
                return target.getAbout();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    DbGUI target = (DbGUI) object;
                    target.setAbout((com.gele.tools.dbedit.castor.About) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.gele.tools.dbedit.castor.About();
            }
        };
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
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
        return com.gele.tools.dbedit.castor.DbGUI.class;
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
