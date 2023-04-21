package com.jtri.struts;

import org.apache.commons.beanutils.ConversionException;
import com.jtri.exception.KeyConversionException;

/**
 * @author atorres
 * @version 1.0, 11/03/2005
 * 
 */
public class PasswordConfirmedConverter implements FormConverter {

    public Object convert(Class type, Object value) {
        if (value == null) return null;
        if (type == String.class) {
            String[] pass = (String[]) value;
            if (pass[0] == null && pass[1] == null) return null;
            if (pass[0].equals(pass[1])) {
                return pass[0];
            }
            throw new KeyConversionException("exception.passwordconfirmation");
        } else if (type == String[].class) {
            String pass = (String) value;
            if (pass == null) pass = "";
            return new String[] { pass, pass };
        }
        if (type == String.class) {
            return value.toString();
        }
        throw new ConversionException("Could not convert from " + value.getClass() + " to " + type);
    }

    public Class fromClass() {
        return String[].class;
    }

    public Class toClass() {
        return String.class;
    }
}
