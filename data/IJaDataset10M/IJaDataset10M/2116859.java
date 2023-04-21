package org.openorb.compiler.object;

import org.openorb.compiler.parser.IdlType;

/**
 * Cette classe represente l'objet IDL Param
 *
 * @author Jerome Daniel
 * @version $Revision: 1.3 $ $Date: 2004/02/10 21:02:39 $
 */
public class IdlParam extends IdlObject implements org.openorb.compiler.idl.reflect.idlParameter {

    /**
     * Flag qui indique l'attribut du parametre
     */
    private int _attr;

    /**
     * Cree un objet IDL Param
     */
    public IdlParam(IdlObject father) {
        super(IdlType.e_param, father);
    }

    /**
     * Retourne l'attribut du parametre
     *
     * @return l'attribut
     */
    public int param_attr() {
        return _attr;
    }

    /**
     * Fixe l'attribut du parametre
     *
     * @param val l'attribut
     */
    public void param_attr(int val) {
        _attr = val;
    }

    public int paramMode() {
        return _attr;
    }

    public org.openorb.compiler.idl.reflect.idlObject paramType() {
        reset();
        return current();
    }

    public java.util.Enumeration content() {
        return new org.openorb.compiler.idl.reflect.idlEnumeration(null);
    }
}
