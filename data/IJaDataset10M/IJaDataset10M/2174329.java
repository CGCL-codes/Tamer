package org.openejb.alt.config.sys;

import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.StringValidator;

/**
 *
 * @version $Revision: 1.6 $ $Date: 2002/12/28 19:20:39 $
**/
public class ProxyFactoryDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public ProxyFactoryDescriptor() {
        super();
        nsURI = "http://www.openejb.org/System/Configuration";
        xmlName = "ProxyFactory";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        FieldValidator fieldValidator = null;
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_content", "PCDATA", NodeType.Text);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                ProxyFactory target = (ProxyFactory) object;
                return target.getContent();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    ProxyFactory target = (ProxyFactory) object;
                    target.setContent((java.lang.String) value);
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
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_id", "id", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                ProxyFactory target = (ProxyFactory) object;
                return target.getId();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    ProxyFactory target = (ProxyFactory) object;
                    target.setId((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.openejb.org/System/Configuration");
        desc.setRequired(true);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_provider", "provider", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                ProxyFactory target = (ProxyFactory) object;
                return target.getProvider();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    ProxyFactory target = (ProxyFactory) object;
                    target.setProvider((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.openejb.org/System/Configuration");
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_jar", "jar", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                ProxyFactory target = (ProxyFactory) object;
                return target.getJar();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    ProxyFactory target = (ProxyFactory) object;
                    target.setJar((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.openejb.org/System/Configuration");
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            sv.setPattern(".*\\.jar$");
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
        return org.openejb.alt.config.sys.ProxyFactory.class;
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
