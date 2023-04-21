package org.openejb.alt.config.ejb11;

import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.StringValidator;

/**
 * 
 * @version $Revision: 1.2 $ $Date: 2002/12/28 19:20:32 $
**/
public class QueryDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    public QueryDescriptor() {
        super();
        nsURI = "http://www.openejb.org/openejb-jar/1.1";
        xmlName = "query";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        FieldValidator fieldValidator = null;
        setCompositorAsSequence();
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Query target = (Query) object;
                return target.getDescription();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Query target = (Query) object;
                    target.setDescription((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.openejb.org/openejb-jar/1.1");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);
        desc = new XMLFieldDescriptorImpl(QueryMethod.class, "_queryMethod", "query-method", NodeType.Element);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Query target = (Query) object;
                return target.getQueryMethod();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Query target = (Query) object;
                    target.setQueryMethod((QueryMethod) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new QueryMethod();
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.openejb.org/openejb-jar/1.1");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        desc.setValidator(fieldValidator);
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_objectQl", "object-ql", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                Query target = (Query) object;
                return target.getObjectQl();
            }

            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Query target = (Query) object;
                    target.setObjectQl((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.openejb.org/openejb-jar/1.1");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
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
        return org.openejb.alt.config.ejb11.Query.class;
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
