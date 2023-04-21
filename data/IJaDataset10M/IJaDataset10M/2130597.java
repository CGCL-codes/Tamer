package org.ztest.classinfo.impl;

import org.jdom.Element;
import org.ztest.classinfo.ZIMethod;
import org.ztest.jdom.ZXMLUtil;

public class ZMethod implements ZIMethod {

    private final String name;

    private final int modifiers;

    private final String descriptor;

    public ZMethod(String name, int modifiers, String desc) {
        super();
        this.descriptor = desc;
        this.name = name;
        this.modifiers = modifiers;
    }

    public Element toElement() {
        Element ret = new Element("method");
        ret.setAttribute("name", name);
        ret.setAttribute("modifiers", Integer.toString(modifiers, 2));
        ret.setAttribute("descriptor", descriptor);
        return ret;
    }

    public String toString() {
        return toXML();
    }

    public String toXML() {
        return ZXMLUtil.toXML(toElement());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ZIMethod)) {
            return false;
        }
        ZIMethod other = (ZIMethod) obj;
        return name.equals(other.getName()) && descriptor.equals(other.getDescriptor()) && modifiers == other.getModifiers();
    }

    @Override
    public int hashCode() {
        return name.hashCode() + descriptor.hashCode();
    }

    public int compareTo(ZIMethod o) {
        int ret = name.compareTo(o.getName());
        if (ret != 0) {
            return ret;
        }
        ret = descriptor.compareTo(o.getDescriptor());
        if (ret != 0) {
            return ret;
        }
        return modifiers < o.getModifiers() ? 1 : modifiers == o.getModifiers() ? 0 : -1;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getName() {
        return name;
    }

    public int getModifiers() {
        return modifiers;
    }
}
