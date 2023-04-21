package org.dbwiki.data.query.condition;

import org.dbwiki.exception.WikiFatalException;
import org.dbwiki.exception.data.WikiQueryException;

public class ValueOpFactory {

    public static final String EQ = "=";

    public static final String GEQ = ">=";

    public static final String GT = ">";

    public static final String IN = "IN";

    public static final String LEQ = "<=";

    public static final String LIKE = "LIKE";

    public static final String LT = "<";

    public static final String MATCHES = "MATCHES";

    public static final String NEQ1 = "!=";

    public static final String NEQ2 = "<>";

    public ValueOp get(String operatorDef, String value) throws org.dbwiki.exception.WikiException {
        final byte typeInt = 1;
        final byte typeString = 2;
        double numericValue = 0;
        String strValue = null;
        byte type;
        if (value.startsWith("'")) {
            if (value.endsWith("'")) {
                strValue = value.replaceAll("\\\\", "\\").replaceAll("\\\'", "\'");
                strValue = strValue.substring(1, strValue.length() - 1);
                type = typeString;
            } else {
                throw new WikiQueryException(WikiQueryException.InvalidQueryStatement, "Missing ' in " + value);
            }
        } else {
            try {
                numericValue = Double.parseDouble(value);
                type = typeInt;
            } catch (java.lang.NumberFormatException nfe) {
                throw new WikiQueryException(WikiQueryException.InvalidQueryStatement, "Format of " + value + " not recognized");
            }
        }
        if (operatorDef.equals(EQ)) {
            if (type == typeInt) {
                return new EQNumeric(numericValue);
            } else {
                return new EQString(strValue);
            }
        } else if ((operatorDef.equals(NEQ1)) || (operatorDef.equals(NEQ2))) {
            if (type == typeInt) {
                return new NEQDouble(numericValue);
            } else {
                return new NEQString(strValue);
            }
        } else if (operatorDef.equals(LT)) {
            if (type == typeInt) {
                return new LTNumeric(numericValue);
            } else {
                return new LTString(strValue);
            }
        } else if (operatorDef.equals(LEQ)) {
            if (type == typeInt) {
                return new LEQNumeric(numericValue);
            } else {
                return new LEQString(strValue);
            }
        } else if (operatorDef.equals(GT)) {
            if (type == typeInt) {
                return new GTNumeric(numericValue);
            } else {
                return new GTString(strValue);
            }
        } else if (operatorDef.equals(GEQ)) {
            if (type == typeInt) {
                return new GEQNumeric(numericValue);
            } else {
                return new GEQString(strValue);
            }
        } else if (operatorDef.equalsIgnoreCase(LIKE)) {
            return new LIKEString(strValue);
        } else if (operatorDef.equalsIgnoreCase(MATCHES)) {
            return new MATCHESString(strValue);
        } else if (operatorDef.equalsIgnoreCase(IN)) {
            INOp inOp = new INOp();
            if (type == typeInt) {
                inOp.add(new EQNumeric(numericValue));
            } else {
                inOp.add(new EQString(strValue));
            }
            return inOp;
        } else {
            throw new WikiFatalException("Unexpected operator " + operatorDef);
        }
    }
}
