package org.openejb.alt.config.sys;

import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * 
 * @version $Revision: 1.4 $ $Date: 2002/12/28 19:20:39 $
**/
public class ServicesJarDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public ServicesJarDescriptor() {
        super();
        nsURI = "http://www.openejb.org/Service/Configuration";
        xmlName = "ServicesJar";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        FieldValidator fieldValidator = null;
        setCompositorAsSequence();
        desc = new XMLFieldDescriptorImpl(ServiceProvider.class, "_serviceProviderList", "ServiceProvider", NodeType.Element);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                ServicesJar target = (ServicesJar) object;
                return target.getServiceProvider();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    ServicesJar target = (ServicesJar) object;
                    target.addServiceProvider((ServiceProvider) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new ServiceProvider();
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.openejb.org/Service/Configuration");
        desc.setRequired(true);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
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
        return org.openejb.alt.config.sys.ServicesJar.class;
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
