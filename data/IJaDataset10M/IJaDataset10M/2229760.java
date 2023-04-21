package oxil.util;

import org.apache.commons.lang.StringUtils;

public class HexaUtil {

    public static String hexaValue(byte b) {
        String retValue = null;
        if (b >= 0) {
            retValue = Integer.toHexString((int) b);
        } else {
            int intValue = 256 + b;
            retValue = Integer.toHexString(intValue);
        }
        return retValue.length() % 2 == 0 ? retValue.toUpperCase() : "0" + retValue.toUpperCase();
    }

    public static String hexaValue(byte b, int quantidadeCasas) {
        String val = hexaValue(b);
        if (val != null && val.length() < quantidadeCasas) {
            val = StringUtils.leftPad(val, quantidadeCasas, "0");
        }
        return val;
    }

    public static String hexaValue(int i, Integer quantidadeCasas) {
        String val = Integer.toHexString(i);
        if (quantidadeCasas != null) {
            if (val != null && val.length() < quantidadeCasas) {
                val = StringUtils.leftPad(val, quantidadeCasas, "0");
            }
        }
        return val.toUpperCase();
    }

    public static String hexaValue(int i) {
        String retValue = hexaValue(i, null);
        return retValue.length() % 2 == 0 ? retValue : "0" + retValue;
    }
}
