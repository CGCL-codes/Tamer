package org.digitall.lib.data;

import java.sql.Types;

public class DataTypes {

    public static final int INTEGER = 1;

    public static final int DOUBLE = 2;

    public static final int DATE = 3;

    public static final int STRING = 4;

    public static final int COLOR = 5;

    public static final int MONEY = 6;

    public static final int PERCENT = 7;

    public static final int SIMPLEDATE = 8;

    public static final int GEOMETRY = 9;

    public static final int CUSTOM_FORMAT = 1000;

    public static final int DOUBLE_EXTENDED = 21;

    public static final int MONEY_EXTENDED = 61;

    public static final int BOOLEAN = Types.BOOLEAN;

    public static String FORMATO_PATENTE_AUTOS_VIEJA = "U-#######";

    public static String FORMATO_PATENTE_AUTOS_NUEVA = "UUU-###";

    public static String FORMATO_PATENTE_MOTOS = "###-UUU";

    public static String FORMATO_DNI = "########";

    public static String FORMATO_CUIT_CUIL = "##-########-#";

    public static String FORMATO_NUMERO_COMPROBANTE = "####-######";

    public static String validateType(int dataType, String value) {
        try {
            switch(dataType) {
                case INTEGER:
                    Integer.parseInt(value);
                    break;
                case DOUBLE:
                case DOUBLE_EXTENDED:
                    Double.parseDouble(value);
                    break;
            }
            return value;
        } catch (NumberFormatException x) {
            return "*";
        }
    }
}
