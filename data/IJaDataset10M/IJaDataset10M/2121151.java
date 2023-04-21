package edu.kds.circuitIO.input;

import edu.kds.circuit.types.*;
import edu.kds.circuit.types.numbertypes.*;

class BitSizeTypeParser {

    public static NumberType parseString(String s) throws DReadException {
        s = s.trim();
        int type = getExprType(s);
        switch(type) {
            case INTEGER_EXPR:
                try {
                    Integer i = Integer.parseInt(s);
                    return new IntNumberType(i);
                } catch (NumberFormatException exc) {
                }
            case UNKNOWN_EXPR:
                return lookup(s);
        }
        String[] exprs = s.split("\\" + operators[type], 2);
        switch(type) {
            case ADD_EXPR:
                return new AddNumberType(parseString(exprs[0]), parseString(exprs[1]));
            case SUB_EXPR:
                return new SubNumberType(parseString(exprs[0]), parseString(exprs[1]));
            case MUL_EXPR:
                return new MultNumberType(parseString(exprs[0]), parseString(exprs[1]));
            case DIV_EXPR:
                return new DivNumberType(parseString(exprs[0]), parseString(exprs[1]));
            case POW_EXPR:
                return new PowNumberType(parseString(exprs[0]), parseString(exprs[1]));
            default:
                return null;
        }
    }

    private static CParameterType lookup(String needle) throws DReadException {
        for (RefObject<CParameterType> obj : ComponenttypeParser.ReferenceManager.getParameters()) {
            if (obj.matches(needle)) return obj.getObject();
        }
        throw new DReadException("Error: variable \"" + needle + "\" could not be located in parametertypes");
    }

    public static void main(String[] args) {
        ComponenttypeParser.ReferenceManager.addParameter(new CParameterType("sz", 1, 1));
        String str1 = "2 * 2";
        String str2 = "sz^2 -1";
        String str3 = "2 + 2 - 2";
        String str4 = "2 * 2 + 2";
        String str5 = "2 * SZ + 2";
        String str6 = "SIZE^2 + 1";
        try {
            System.out.println(str1 + " = " + parseString(str1));
            System.out.println(str2 + " = " + parseString(str2));
            System.out.println(str3 + " = " + parseString(str3));
            System.out.println(str4 + " = " + parseString(str4));
            System.out.println(str5 + " = " + parseString(str5));
            System.out.println(str6 + " = " + parseString(str6));
        } catch (DReadException e) {
            System.err.println(e.getMessage());
        }
    }

    private static final int getExprType(String expr) {
        if (-1 < expr.indexOf(operators[SUB_EXPR])) {
            return SUB_EXPR;
        }
        if (-1 < expr.indexOf(operators[ADD_EXPR])) {
            return ADD_EXPR;
        }
        if (-1 < expr.indexOf(operators[MUL_EXPR])) {
            return MUL_EXPR;
        }
        if (-1 < expr.indexOf(operators[DIV_EXPR])) {
            return DIV_EXPR;
        }
        if (-1 < expr.indexOf(operators[POW_EXPR])) {
            return POW_EXPR;
        }
        try {
            Integer.parseInt(expr);
            return INTEGER_EXPR;
        } catch (NumberFormatException e) {
        }
        return UNKNOWN_EXPR;
    }

    private static final int SUB_EXPR = 0;

    private static final int ADD_EXPR = 1;

    private static final int MUL_EXPR = 2;

    private static final int DIV_EXPR = 3;

    private static final int POW_EXPR = 4;

    private static final int INTEGER_EXPR = 5;

    private static final int UNKNOWN_EXPR = 6;

    private static final String[] operators = { "-", "+", "*", "/", "^" };
}
