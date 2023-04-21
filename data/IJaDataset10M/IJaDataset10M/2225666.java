package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.*;
import java.util.ArrayList;

/**
 * Super class for FieldGen and MethodGen objects, since they have
 * some methods in common!
 *
 * @version $Id: FieldGenOrMethodGen.java,v 1.1.2.1 2005/07/31 23:44:49 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public abstract class FieldGenOrMethodGen extends AccessFlags implements NamedAndTyped, Cloneable {

    protected String name;

    protected Type type;

    protected ConstantPoolGen cp;

    private ArrayList attribute_vec = new ArrayList();

    protected FieldGenOrMethodGen() {
    }

    public void setType(Type type) {
        if (type.getType() == Constants.T_ADDRESS) throw new IllegalArgumentException("Type can not be " + type);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /** @return name of method/field.
   */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstantPoolGen getConstantPool() {
        return cp;
    }

    public void setConstantPool(ConstantPoolGen cp) {
        this.cp = cp;
    }

    /**
   * Add an attribute to this method. Currently, the JVM knows about
   * the `Code', `ConstantValue', `Synthetic' and `Exceptions'
   * attributes. Other attributes will be ignored by the JVM but do no
   * harm.
   *
   * @param a attribute to be added
   */
    public void addAttribute(Attribute a) {
        attribute_vec.add(a);
    }

    /**
   * Remove an attribute.
   */
    public void removeAttribute(Attribute a) {
        attribute_vec.remove(a);
    }

    /**
   * Remove all attributes.
   */
    public void removeAttributes() {
        attribute_vec.clear();
    }

    /**
   * @return all attributes of this method.
   */
    public Attribute[] getAttributes() {
        Attribute[] attributes = new Attribute[attribute_vec.size()];
        attribute_vec.toArray(attributes);
        return attributes;
    }

    /** @return signature of method/field.
   */
    public abstract String getSignature();

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
            return null;
        }
    }
}
