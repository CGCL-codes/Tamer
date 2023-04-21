package ch.ethz.mxquery.util;

import ch.ethz.mxquery.datamodel.types.TypeLexicalConstraints;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;

public class URIUtils {

    public static boolean isValidURI(String value) {
        if (value.indexOf("<") > -1 || value.indexOf(">") > -1 || value.indexOf("{") > -1 || value.indexOf("}") > -1 || value.indexOf("|") > -1 || value.indexOf("^") > -1) return false;
        if (value.indexOf(':') == 0 && value.indexOf(':') == value.length() - 1) return false;
        int idx = value.indexOf("//");
        if (idx >= 0 && idx == value.length() - 2) return false;
        return true;
    }

    public static boolean isRelativeURI(String value) {
        if (isValidURI(value) && !(value.indexOf(":/") >= 0 || value.indexOf(":\\") >= 0)) return true;
        return false;
    }

    public static boolean isAbsoluteURI(String value) {
        if (isValidURI(value) && value.indexOf(":/") > 0) return true;
        return false;
    }

    public static String resolveURI(String base, String add, QueryLocation loc) throws DynamicException {
        if (!TypeLexicalConstraints.isRelativeURI(add)) {
            return add;
        } else {
            String add1 = add;
            if (add1.startsWith("/")) add1 = add1.substring(1);
            String tmp = base + add1;
            if (!isValidURI(tmp)) throw new DynamicException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Invalid values to resolve URI", loc);
            return tmp;
        }
    }
}
