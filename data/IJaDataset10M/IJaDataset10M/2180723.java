package org.goet.datamodel.impl;

import java.util.*;
import org.goet.datamodel.*;
import java.io.*;
import org.bdgp.util.StringUtil;

public class NodeImpl implements Node {

    protected HashMap properties;

    protected Set referencedPropertyList;

    protected ClassSignature signature;

    protected Set propertyList;

    protected URI uri;

    protected DataCollection dc;

    private static transient HashSet termProps;

    protected NodeImpl() {
    }

    public NodeImpl(URI uri, NodeClass nodeClass, DataCollection dc) {
        this(uri, Collections.singleton(nodeClass), dc);
    }

    public NodeImpl(URI uri, DataCollection dc) {
        this.uri = uri;
        this.properties = new HashMap();
        this.referencedPropertyList = new HashSet();
        this.dc = dc;
    }

    public NodeImpl(URI uri, Set classes, DataCollection dc) {
        this(uri, NodeUtil.getSignature(dc, classes), dc);
    }

    public NodeImpl(URI uri, ClassSignature sig, DataCollection dc) {
        this.uri = uri;
        this.properties = new HashMap();
        this.referencedPropertyList = new HashSet();
        setClassSig(sig);
        this.dc = dc;
    }

    public void setDataCollection(DataCollection dc) {
        this.dc = dc;
    }

    public URI getURI() {
        return uri;
    }

    public void setClassSig(ClassSignature sig) {
        signature = sig;
        properties.clear();
        propertyList = sig.getProperties();
        Iterator it = propertyList.iterator();
        while (it.hasNext()) {
            properties.put((Property) it.next(), new HashSet());
        }
    }

    public void setClasses(Set classes) {
        setClassSig(dc.getSignature(classes));
    }

    public void structureChanged(Property changed, boolean isDel) {
        if (signature == null) return;
        if (isDel) properties.remove(changed); else properties.put(changed, new HashSet());
    }

    public void setURI(URI uri) {
        URI old = this.uri;
        this.uri = uri;
        dc.uriChanged(this, old, uri);
    }

    public boolean isEditable() {
        return true;
    }

    public ClassSignature getSignature() {
        return signature;
    }

    public Set getPropertyValues(Property property) {
        if (!propertyList.contains(property)) throw new IllegalArgumentException("Property " + property + " is not valid for node " + this);
        Set v = (Set) properties.get(property);
        if (v == null) return Collections.EMPTY_SET; else return Collections.unmodifiableSet(v);
    }

    public Value getFirstValue(Property property) {
        if (!propertyList.contains(property)) throw new IllegalArgumentException("Property " + property + " is not valid for node " + this);
        Set v = (Set) properties.get(property);
        if (v == null || v.size() == 0) return null;
        return (Value) v.iterator().next();
    }

    public Set getReferencedByProperties() {
        return referencedPropertyList;
    }

    public void addReferencedByProperty(NodeProperty nodeProperty) {
        referencedPropertyList.add(nodeProperty);
    }

    public void removeReferencedByProperty(NodeProperty nodeProperty) {
        referencedPropertyList.remove(nodeProperty);
    }

    public void addPropertyValue(Property property, Value val) {
        if (!propertyList.contains(property)) throw new IllegalArgumentException("Property " + property + " is not valid for node " + this);
        if (val == null) throw new IllegalArgumentException("Cannot add null value to " + "property " + property + " of " + this);
        Set v = (Set) properties.get(property);
        if (v == null) {
            System.err.println("node " + uri + " gave a null response for property " + property);
            System.err.println("node signature = " + getSignature());
            v = new HashSet();
            properties.put(property, v);
        }
        v.add(val);
        if (val instanceof Node) {
            ((Node) val).addReferencedByProperty(new NodeProperty(this, property));
        }
    }

    public void removePropertyValue(Property property, Value val) {
        Set v = (Set) properties.get(property);
        v.remove(val);
        if (val instanceof Node) {
            ((Node) val).removeReferencedByProperty(new NodeProperty(this, property));
        }
    }

    public Set getProperties() {
        return propertyList;
    }

    public boolean equals(Object o) {
        if (o instanceof Node) {
            return uri.equals(((Node) o).getURI());
        } else return false;
    }

    public int hashCode() {
        return uri.hashCode();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeObject(uri);
        s.defaultWriteObject();
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        uri = (URI) s.readObject();
        s.defaultReadObject();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (signature != null) {
            Set classes = signature.getClasses();
            if (classes != null) {
                sb.append("[");
                Iterator it = classes.iterator();
                boolean first = true;
                while (it.hasNext()) {
                    NodeClass nodeClass = (NodeClass) it.next();
                    if (first) {
                        sb.append(", ");
                        first = false;
                    }
                    sb.append(nodeClass);
                }
                sb.append("] ");
            } else sb.append("{no classes} ");
        } else sb.append("{no classes} ");
        sb.append(uri.toString());
        return sb.toString();
    }
}
