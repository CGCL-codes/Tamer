package org.omg.Security;

/**
 * Struct definition: ExtensibleFamily.
 * 
 * @author OpenORB Compiler
*/
public final class ExtensibleFamily implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member family_definer
     */
    public short family_definer;

    /**
     * Struct member family
     */
    public short family;

    /**
     * Default constructor
     */
    public ExtensibleFamily() {
    }

    /**
     * Constructor with fields initialization
     * @param family_definer family_definer struct member
     * @param family family struct member
     */
    public ExtensibleFamily(short family_definer, short family) {
        this.family_definer = family_definer;
        this.family = family;
    }
}
