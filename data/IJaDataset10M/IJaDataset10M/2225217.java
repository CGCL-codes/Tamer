package org.apache.myfaces.trinidadinternal.convert;

import java.util.Collection;
import java.util.Collections;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import org.apache.myfaces.trinidad.convert.ClientConverter;
import org.apache.myfaces.trinidad.util.IntegerUtils;

/**
 * <p>Implementation for <code>java.lang.Integer</code> values.</p>
 *
 *
 */
public class IntegerConverter extends javax.faces.convert.IntegerConverter implements ClientConverter {

    /**
     * <p>The message identifier of the FacesMessage to be created if
     * the value is greater than Integer.MAX_VALUE.
     * The message format string for this
     * message may optionally include a <code>{2}</code> placeholder, which
     * will be replaced by Integer.MAX_VALUE.</p>
     */
    public static final String MAXIMUM_MESSAGE_ID = "org.apache.myfaces.trinidad.convert.IntegerConverter.MAXIMUM";

    /**
     * <p>The message identifier of the FacesMessage to be created if
     * the value is less than Integer.MIN_VALUE.
     * The message format string for this
     * message may optionally include a <code>{2}</code> placeholder, which
     * will be replaced by Integer.MIN_VALUE.</p>
     */
    public static final String MINIMUM_MESSAGE_ID = "org.apache.myfaces.trinidad.convert.IntegerConverter.MINIMUM";

    /**
     * <p>The message identifier of the FacesMessage to be created if
     * the value cannot be converted to an integer
     */
    public static final String CONVERT_MESSAGE_ID = "org.apache.myfaces.trinidad.convert.IntegerConverter.CONVERT";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return super.getAsObject(context, component, value);
        } catch (ConverterException ce) {
            throw ConverterUtils.getIntegerConverterException(context, component, ce, value, CONVERT_MESSAGE_ID, MAXIMUM_MESSAGE_ID, _INT_MAX, MINIMUM_MESSAGE_ID, _INT_MIN);
        }
    }

    public String getClientScript(FacesContext context, UIComponent component) {
        return null;
    }

    public String getClientLibrarySource(FacesContext context) {
        return null;
    }

    /**
   * @param context
   * @return
   */
    public String getClientConversion(FacesContext context, UIComponent component) {
        return _getTrIntegerConverter(context, component, _INT_MAX, _INT_MIN);
    }

    public Collection<String> getClientImportNames() {
        return _IMPORT_NAMES;
    }

    private static String _getTrIntegerConverter(FacesContext context, UIComponent component, String maxVal, String minVal) {
        StringBuilder outBuffer = new StringBuilder(250);
        outBuffer.append("new TrIntegerConverter(");
        outBuffer.append("null,null,0,");
        outBuffer.append(maxVal);
        outBuffer.append(',');
        outBuffer.append(minVal);
        outBuffer.append(")");
        return outBuffer.toString();
    }

    private static final String _INT_MIN = IntegerUtils.getString(Integer.MIN_VALUE);

    private static final String _INT_MAX = IntegerUtils.getString(Integer.MAX_VALUE);

    private static final Collection<String> _IMPORT_NAMES = Collections.singletonList("TrNumberConverter()");
}
