package org.openorb.compiler.object;

import org.openorb.compiler.parser.IdlParser;
import org.openorb.compiler.parser.IdlType;
import org.openorb.compiler.parser.Token;

/**
 * Cette classe represente l'objet IDL Simple
 *
 * @author Jerome Daniel
 * @version $Revision: 1.3 $ $Date: 2004/02/10 21:02:39 $
 */
public class IdlSimple extends IdlObject implements org.openorb.compiler.idl.reflect.idlPrimitive {

    /**
     * Identifiant du type simple
     */
    private int kind;

    /**
     * Le type simple void
     */
    public static IdlSimple void_type;

    /**
     * Le type simple boolean
     */
    public static IdlSimple boolean_type;

    /**
     * Le type simple float
     */
    public static IdlSimple float_type;

    /**
     * Le type simple double
     */
    public static IdlSimple double_type;

    /**
     * Le type simple longdouble
     */
    public static IdlSimple longdouble_type;

    /**
     * Le type simple short
     */
    public static IdlSimple short_type;

    /**
     * Le type simple unsigned short
     */
    public static IdlSimple ushort_type;

    /**
     * Le type simple long
     */
    public static IdlSimple long_type;

    /**
     * Le type simple unsigned long
     */
    public static IdlSimple ulong_type;

    /**
     * Le type simple long long
     */
    public static IdlSimple longlong_type;

    /**
     * Le type simple unsigned long long
     */
    public static IdlSimple ulonglong_type;

    /**
     * Le type simple char
     */
    public static IdlSimple char_type;

    /**
     * Le type simple wchar
     */
    public static IdlSimple wchar_type;

    /**
     * Le type simple octet
     */
    public static IdlSimple octet_type;

    /**
     * Le type simple Object
     */
    public static IdlSimple object_type;

    /**
     * Le type simple Any
     */
    public static IdlSimple any_type;

    /**
     * Le type simple TypeCode
     */
    public static IdlSimple typecode_type;

    /**
     * Le type simple ValueBase
     */
    public static IdlSimple valuebase_type;

    /**
    * Cree un objet IDL Simple
    */
    public IdlSimple(int t, IdlParser parser) {
        super(IdlType.e_simple, null, parser);
        kind = t;
    }

    /**
    * Retourne le type interne d'un type simple
    *
    * @return le type interne
    */
    public int internal() {
        return kind;
    }

    /**
    * Change prefix to my self but also to all contained objects
    */
    public void changePrefix(String prefix) {
    }

    public static void initIdlSimple(IdlParser parser) {
        void_type = new IdlSimple(Token.t_void, parser);
        boolean_type = new IdlSimple(Token.t_boolean, parser);
        float_type = new IdlSimple(Token.t_float, parser);
        double_type = new IdlSimple(Token.t_double, parser);
        longdouble_type = new IdlSimple(Token.t_longdouble, parser);
        short_type = new IdlSimple(Token.t_short, parser);
        ushort_type = new IdlSimple(Token.t_ushort, parser);
        long_type = new IdlSimple(Token.t_long, parser);
        ulong_type = new IdlSimple(Token.t_ulong, parser);
        longlong_type = new IdlSimple(Token.t_longlong, parser);
        ulonglong_type = new IdlSimple(Token.t_ulonglong, parser);
        char_type = new IdlSimple(Token.t_char, parser);
        wchar_type = new IdlSimple(Token.t_wchar, parser);
        octet_type = new IdlSimple(Token.t_octet, parser);
        object_type = new IdlSimple(Token.t_object, parser);
        any_type = new IdlSimple(Token.t_any, parser);
        typecode_type = new IdlSimple(Token.t_typecode, parser);
        valuebase_type = new IdlSimple(Token.t_ValueBase, parser);
    }

    public int primitive() {
        switch(kind) {
            case Token.t_void:
                return VOID;
            case Token.t_boolean:
                return BOOLEAN;
            case Token.t_float:
                return FLOAT;
            case Token.t_double:
                return DOUBLE;
            case Token.t_longdouble:
                return LONGDOUBLE;
            case Token.t_short:
                return SHORT;
            case Token.t_ushort:
                return USHORT;
            case Token.t_long:
                return LONG;
            case Token.t_ulong:
                return ULONG;
            case Token.t_longlong:
                return LONGLONG;
            case Token.t_ulonglong:
                return ULONGLONG;
            case Token.t_char:
                return CHAR;
            case Token.t_wchar:
                return WCHAR;
            case Token.t_octet:
                return OCTET;
            case Token.t_object:
                return OBJECT;
            case Token.t_any:
                return ANY;
            case Token.t_typecode:
                return TYPECODE;
            case Token.t_ValueBase:
                return VALUEBASE;
            default:
                return -1;
        }
    }
}
