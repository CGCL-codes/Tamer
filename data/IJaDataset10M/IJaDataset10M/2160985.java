package net.sf.csv2sql.grammars.mysql;

import java.util.HashMap;
import java.util.Properties;
import net.sf.csv2sql.grammars.AbstractField;
import net.sf.csv2sql.grammars.exceptions.FieldRenderException;
import net.sf.csv2sql.grammars.exceptions.MissingRequiredParameterException;

/**
 * @see AbstractField
 * @author <a href="mailto:dconsonni@enter.it">Davide Consonni</a>
 */
public final class MysqlFieldDouble extends AbstractField {

    public static final float MIN_VALUE = Float.parseFloat("-1.7976931348623157E+308");

    public static final float MAX_VALUE = Float.parseFloat("1.7976931348623157E+308");

    public MysqlFieldDouble(String fieldName, Properties properties) throws MissingRequiredParameterException {
        super(fieldName, properties);
    }

    /**
     * @see AbstractField#render
     */
    protected String doRender(Object value) throws FieldRenderException {
        if ((value == null) || ("".equals(value))) {
            return getDefaulNullValue();
        }
        try {
            String strValue = ((String) value).replaceAll(",", ".");
            if ("null".equalsIgnoreCase(strValue)) {
                return getDefaulNullValue();
            }
            float result = Float.parseFloat(strValue.trim());
            if (result < MIN_VALUE || result > MAX_VALUE) {
                throw new FieldRenderException(this, value, "float value out of range (" + strValue + ")", null);
            }
            return String.valueOf(strValue);
        } catch (NumberFormatException e) {
            throw new FieldRenderException(this, value, "not an number", e);
        } catch (Exception e) {
            throw new FieldRenderException(this, value, "generic error", e);
        }
    }

    protected HashMap requiredParameterList() {
        return null;
    }

    protected HashMap optionalParameterList() {
        return null;
    }
}
