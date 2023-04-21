package org.rendersnake.servlet;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * FormDataPopulator is used to take parameter values specified by a FormHandler and set by the HTML page
 * and populate them as field values of an Object. 
 * <p>
 * The parameter names are matched (name and type) with the field names of an Object.
 *
 * @author ernest.micklei@gmail.com
 */
@Deprecated
public class FormDataPopulator {

    private static final Logger LOG = Logger.getLogger("org.rendersnake.servlet.FormDataPopulator");

    public static final String VAR_NAME_POSTFIX = "_rs_";

    /**
     * Take the parameters from the request and those that are set by a FormHandler
     * are populated as field values of the target object.
     * @param request
     * @param target
     */
    public void populate(HttpServletRequest request, Object target) {
        @SuppressWarnings("unchecked") Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String each = names.nextElement();
            if (each.endsWith(VAR_NAME_POSTFIX)) {
                String varname = each.substring(0, each.length() - VAR_NAME_POSTFIX.length());
                this.trySetFieldOfTo(varname, target, request.getParameter(each));
            }
        }
    }

    /**
     * For the target object, try setting the value of a field after type conversion.
     * @param varname
     * @param target
     * @param valueString
     */
    private void trySetFieldOfTo(String varname, Object target, String valueString) {
        try {
            Field field = target.getClass().getDeclaredField(varname);
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                field.set(target, valueString);
            } else if (fieldType == int.class) {
                field.set(target, Integer.valueOf(valueString));
            } else if (fieldType == boolean.class) {
                field.set(target, Boolean.valueOf(valueString));
            } else {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.warning(String.format("Unknown type [%s] to set field [%s] to [%s] of [%p]", fieldType.getName(), varname, valueString, target));
                }
            }
        } catch (Exception ex) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.warning(String.format("Unable to set field [%s] to [%s] of [%s]", varname, valueString, target.toString()));
            }
        }
    }
}
