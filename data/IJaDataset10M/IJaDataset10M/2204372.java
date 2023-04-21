package org.jboss.snmp.mib.generator;

import java.util.ArrayList;
import org.jboss.snmp.mib.generator.metrics.Mapping;
import org.jboss.snmp.mib.generator.metrics.VarBind;
import org.jboss.snmp.mib.generator.metrics.VarBindList;
import org.jboss.xb.binding.GenericObjectModelFactory;
import org.jboss.xb.binding.UnmarshallingContext;
import org.xml.sax.Attributes;

public class ParserNotificationBindings implements GenericObjectModelFactory {

    public Object completeRoot(Object root, UnmarshallingContext ctx, String uri, String name) {
        return root;
    }

    public Object newRoot(Object root, UnmarshallingContext navigator, String namespaceURI, String localName, Attributes attrs) {
        ArrayList notifList;
        if (root == null) {
            root = notifList = new ArrayList();
        } else {
            notifList = (ArrayList) root;
        }
        return root;
    }

    public Object newChild(Object parent, UnmarshallingContext navigator, String namespaceURI, String localName, Attributes attrs) {
        Object child = null;
        if ("mapping".equals(localName)) {
            Mapping m = new Mapping();
            String notificationType = attrs.getValue("notification-type");
            String generic = attrs.getValue("generic");
            String specific = attrs.getValue("specific");
            String enterprise = attrs.getValue("enterprise");
            String inform = attrs.getValue("inform");
            String securityName = attrs.getValue("security-name");
            String name = attrs.getValue("name");
            String oidDef = attrs.getValue("definition-name");
            String desc = attrs.getValue("description");
            m.setName(name);
            m.setOidDef(oidDef);
            m.setNotificationType(notificationType);
            m.setGeneric(Integer.parseInt(generic));
            m.setSpecific(Integer.parseInt(specific));
            m.setEnterprise(enterprise);
            m.setInform(Boolean.parseBoolean(inform));
            m.setSecurityName(securityName);
            m.setDesc(desc);
            child = m;
        } else if ("var-bind-list".equals(localName)) {
            VarBindList vblist = new VarBindList();
            child = vblist;
            if (attrs.getLength() > 0) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    if ("wrapper-class".equals(attrs.getLocalName(i))) {
                        vblist.setWrapperClass(attrs.getValue(i));
                    }
                }
            }
            if (vblist.getWrapperClass() == null) {
                throw new RuntimeException("'wrapper-class' must be set at 'var-bind-list' element");
            }
        } else if ("var-bind".equals(localName)) {
            VarBind vb = new VarBind();
            String oid = attrs.getValue("oid");
            String tag = attrs.getValue("tag");
            String type = attrs.getValue("type");
            vb.setOid(oid);
            vb.setTag(tag);
            vb.setType(type);
            child = vb;
        }
        return child;
    }

    public void addChild(Object parent, Object child, UnmarshallingContext navigator, String namespaceURI, String localName) {
        if (parent instanceof ArrayList) {
            ArrayList notifList = (ArrayList) parent;
            if (child instanceof Mapping) {
                notifList.add(child);
            }
        } else if (parent instanceof Mapping) {
            Mapping m = (Mapping) parent;
            if (child instanceof VarBindList) {
                m.setVarBindList((VarBindList) child);
            }
        } else if (parent instanceof VarBindList) {
            VarBindList vblist = (VarBindList) parent;
            if (child instanceof VarBind) {
                vblist.addVarBind((VarBind) child);
            }
        }
    }

    public void setValue(Object o, UnmarshallingContext navigator, String namespaceURI, String localName, String value) {
        if (o instanceof Mapping) {
            Mapping m = (Mapping) o;
            if ("notification-type".equals(localName)) {
                m.setNotificationType(value);
            } else if ("generic".equals(localName)) {
                m.setGeneric(Integer.parseInt(value));
            } else if ("specific".equals(localName)) {
                m.setSpecific(Integer.parseInt(value));
            } else if ("enterprise".equals(localName)) {
                m.setEnterprise(value);
            } else if ("inform".equals(localName)) {
                m.setInform(Boolean.parseBoolean(value));
            } else if ("security-name".equals(localName)) {
                m.setSecurityName(value);
            } else if ("name".equals(localName)) {
                m.setName(value);
            } else if ("definition-name".equals(localName)) {
                m.setOidDef(value);
            }
        } else if (o instanceof VarBind) {
            VarBind vb = (VarBind) o;
            if ("tag".equals(localName)) {
                vb.setTag(value);
            } else if ("oid".equals(localName)) {
                vb.setOid(value);
            }
        }
    }

    public Object completedRoot(Object root, UnmarshallingContext navigator, String namespaceURI, String localName) {
        return root;
    }
}
