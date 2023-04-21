package org.openorb.compiler.object;

import org.openorb.compiler.parser.IdlType;
import org.openorb.util.CharacterCache;
import org.openorb.util.NumberCache;

/**
 * Cette classe represente l'objet IDL Const
 *
 * @author Jerome Daniel
 * @version $Revision: 1.4 $ $Date: 2004/02/10 21:02:38 $
 */
public class IdlConst extends IdlObject implements org.openorb.compiler.idl.reflect.idlConst {

    /**
     * Expression de la constante
     */
    private String exp;

    /**
     * Valeur entiere de la constante
     */
    private long ivalue;

    /**
     * Valeur flotante de la constante
     */
    private double fvalue;

    /**
     * Cree un objet IDL Const
     */
    public IdlConst(IdlObject father) {
        super(IdlType.e_const, father);
    }

    /**
     * Fixe la valeur de l'expression constante
     *
     * @param expr l'expression constante
     */
    public void expression(String expr) {
        exp = expr;
    }

    /**
     * Retourne l'expression constante
     *
     * @return l'expression
     */
    public String expression() {
        return exp;
    }

    /**
     * Fixe la valeur entiere de la constante
     *
     * @param val la valeur entiere
     */
    public void intValue(long val) {
        ivalue = val;
    }

    /**
     * Renvoie la valeur entiere de la constante
     *
     * @return la valeur de la constante
     */
    public long intValue() {
        return ivalue;
    }

    /**
     * Fixe la valeur flottante de la constante
     *
     * @param val la valeur flottante
     */
    public void floatValue(double val) {
        fvalue = val;
    }

    /**
     * Renvoie la valeur flottante de la constante
     *
     * @return la valeur de la constante
     */
    public double floatValue() {
        return fvalue;
    }

    /**
     * Change prefix to my self but also to all contained objects
     */
    public void changePrefix(String prefix) {
        if (_prefix_explicit != true) {
            _prefix = prefix;
        }
    }

    public java.util.Enumeration content() {
        return new org.openorb.compiler.idl.reflect.idlEnumeration(null);
    }

    public Object value() {
        org.openorb.compiler.idl.reflect.idlObject ctype = constantType();
        switch(ctype.idlConcreteType()) {
            case org.openorb.compiler.idl.reflect.idlType.PRIMITIVE:
                org.openorb.compiler.idl.reflect.idlPrimitive pr = (org.openorb.compiler.idl.reflect.idlPrimitive) ctype;
                switch(pr.primitive()) {
                    case org.openorb.compiler.idl.reflect.idlPrimitive.OCTET:
                        return NumberCache.getByte((byte) ivalue);
                    case org.openorb.compiler.idl.reflect.idlPrimitive.BOOLEAN:
                        return (ivalue == 1) ? Boolean.TRUE : Boolean.FALSE;
                    case org.openorb.compiler.idl.reflect.idlPrimitive.FLOAT:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.DOUBLE:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.LONGDOUBLE:
                        return NumberCache.getDouble(fvalue);
                    case org.openorb.compiler.idl.reflect.idlPrimitive.SHORT:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.USHORT:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.LONG:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.ULONG:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.LONGLONG:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.ULONGLONG:
                        return NumberCache.getLong(ivalue);
                    case org.openorb.compiler.idl.reflect.idlPrimitive.CHAR:
                    case org.openorb.compiler.idl.reflect.idlPrimitive.WCHAR:
                        return CharacterCache.getCharacter(expression().charAt(1));
                }
                break;
            case org.openorb.compiler.idl.reflect.idlType.STRING:
            case org.openorb.compiler.idl.reflect.idlType.WSTRING:
                return expression().substring(1, expression().length() - 1);
            case org.openorb.compiler.idl.reflect.idlType.FIXED:
                return NumberCache.getDouble(fvalue);
            case org.openorb.compiler.idl.reflect.idlType.ENUM:
                return NumberCache.getLong(ivalue);
        }
        return null;
    }

    public org.openorb.compiler.idl.reflect.idlObject constantType() {
        reset();
        return (org.openorb.compiler.idl.reflect.idlObject) current();
    }
}
