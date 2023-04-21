package com.fluidmobility.wsdl2ksoap2.model;

/**
 * Represents an element tag.
 */
public class XsdElement implements Cloneable, IQNameProvider {

    private String name;

    private String namespace;

    private String uniqueName;

    private String typeName;

    private String typeNamespace;

    private int minOccurs = 1;

    private int maxOccurs = 1;

    private boolean nillable = false;

    private Object typeResolution;

    public Object clone() {
        XsdElement that = new XsdElement();
        that.name = this.name;
        that.namespace = this.namespace;
        that.typeName = this.typeName;
        that.typeNamespace = this.typeNamespace;
        that.minOccurs = this.minOccurs;
        that.maxOccurs = this.maxOccurs;
        that.nillable = this.nillable;
        that.typeResolution = this.typeResolution;
        return that;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTypeNamespace() {
        return typeNamespace;
    }

    public void setTypeNamespace(String namespace) {
        this.typeNamespace = namespace;
    }

    public boolean isNillable() {
        return nillable;
    }

    public void setNillable(boolean nillable) {
        this.nillable = nillable;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String type) {
        this.typeName = type;
    }

    public Object getTypeResolution() {
        return typeResolution;
    }

    public void setTypeResolution(Object typeResolution) {
        this.typeResolution = typeResolution;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("<element ").append(" minOccurs=\"").append(minOccurs).append("\" maxOccurs=\"");
        if (maxOccurs == Integer.MAX_VALUE) {
            buf.append("unbounded");
        } else {
            buf.append(maxOccurs);
        }
        buf.append("\" name=\"").append(name).append("\" type\"").append(typeName).append("\" />");
        return buf.toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof XsdElement) {
            XsdElement that = (XsdElement) obj;
            boolean namespaceEqual = this.getNamespace() != null && this.getNamespace().equals(that.getNamespace());
            boolean nameEqual = this.getName() != null && this.getName().toUpperCase().equals(that.getName().toUpperCase());
            equals = namespaceEqual && nameEqual;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 37 * h + (getNamespace() != null ? getNamespace().hashCode() : 0);
        h = 37 * h + (getName() != null ? getName().toUpperCase().hashCode() : 0);
        return h;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}
