package org.enerj.enhancer;

import org.objectweb.asm.Type;

/**
 * Represents an ASM Field.<p>
 * 
 * @version $Id: Field.java,v 1.3 2005/08/12 02:56:49 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad</a>
 */
class Field {

    private String mName;

    private String mDescriptor;

    private int mAccessModifiers;

    private String mInternalName;

    /**
     * Construct a Field. 
     *
     * @param aName the field's name.
     * @param aDescriptor the JVM type descriptor. 
     * @param aModifiers the field's access modifiers, as defined by ASM Opcodes.ACC_*. 
     */
    protected Field(String aName, String aDescriptor, int aModifiers) {
        mName = aName;
        mDescriptor = aDescriptor;
        mAccessModifiers = aModifiers;
        if (mDescriptor.length() == 1 || mDescriptor.charAt(0) == '[') {
            mInternalName = mDescriptor;
        } else {
            mInternalName = Type.getType(mDescriptor).getInternalName();
        }
    }

    /**
     * Gets the field's Access Modifiers.
     *
     * @return a int.
     */
    int getAccessModifiers() {
        return mAccessModifiers;
    }

    /**
     * Gets the field's type Descriptor.
     *
     * @return a String.
     */
    String getDescriptor() {
        return mDescriptor;
    }

    /**
     * Gets the field's Name.
     *
     * @return a String.
     */
    String getName() {
        return mName;
    }

    /**
     * Gets the Internal Name of the field's type (ASM style).
     *
     * @return a String.
     */
    String getInternalName() {
        return mInternalName;
    }

    /** 
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object someObj) {
        if (!(someObj instanceof Field)) {
            return false;
        }
        Field other = (Field) someObj;
        return mName.equals(other.mName);
    }

    /** 
     * 
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return mName.hashCode();
    }
}
