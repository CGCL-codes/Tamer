package net.sf.opentranquera.integration.mf.format.validators;

import java.util.Map;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Valida que sea un campo numerico
 * <b>NOTA: En casos donde se requiere utilizar un valor numerico con puntos y comas, utilizar NumericFormatValidator</b>
 * @author Guillermo Meyer
 * @date 17/05/2005
 */
public class NumericValidator implements FieldValidator {

    private static final String DECIMAL_SEPARATOR = ".";

    private static final String THOUSAND_SEPARATOR = "";

    public boolean validate(Object src, Map args, Map valueHolder) {
        String decimalSeparator = (String) args.get("decimalSeparator");
        String milSeparator = (String) args.get("thousandSeparator");
        String value = src == null ? "0" : src.toString();
        if ((decimalSeparator == null && milSeparator == null) && (src != null && NumberUtils.isNumber(value))) {
            return true;
        } else {
            if ((milSeparator != null) && (!milSeparator.equals(THOUSAND_SEPARATOR))) {
                value = value.replaceAll("\\" + milSeparator, THOUSAND_SEPARATOR);
            }
            if ((decimalSeparator != null) && (!decimalSeparator.equals(DECIMAL_SEPARATOR))) {
                value = value.replaceAll("\\" + decimalSeparator, DECIMAL_SEPARATOR);
            }
            return NumberUtils.isNumber(value);
        }
    }
}
