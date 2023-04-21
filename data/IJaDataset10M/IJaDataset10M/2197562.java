package org.openorb.compiler.object;

import org.openorb.compiler.parser.IdlType;

/**
 * Cette classe represente l'objet IDL Sequence
 *
 * @author Jerome Daniel
 * @version $Revision: 1.3 $ $Date: 2004/02/10 21:02:39 $
 */
public class IdlSequence extends IdlObject implements org.openorb.compiler.idl.reflect.idlSequence {

    /**
    * Taille de la sequence
    */
    private int size;

    /**
     * Cree un objet IDL Sequence
     */
    public IdlSequence(IdlObject father) {
        super(IdlType.e_sequence, father);
    }

    /**
     * Retourne la taille de la sequence
     *
     * @return la taille
     */
    public int getSize() {
        return size;
    }

    /**
     * Fixe la taille de la sequence
     *
     * @param sz la taille
     */
    public void setSize(int sz) {
        size = sz;
    }

    public int length() {
        return size;
    }

    public org.openorb.compiler.idl.reflect.idlObject internal() {
        reset();
        return current();
    }

    public java.util.Enumeration content() {
        return new org.openorb.compiler.idl.reflect.idlEnumeration(null);
    }
}
