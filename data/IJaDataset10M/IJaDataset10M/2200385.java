package org.apache.myfaces.trinidadinternal.convert;

import java.math.BigInteger;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.util.MessageFactory;
import org.apache.myfaces.trinidadinternal.ui.laf.base.xhtml.XhtmlLafUtils;

/**
 * Private utilities for working with converters
 */
public class ConverterUtils {

    private ConverterUtils() {
    }

    /**
   * This is integer in the mathematical sense, not the java sense.
   * This method is called when a ConverterException has already occurred
   */
    public static final ConverterException getIntegerConverterException(FacesContext context, UIComponent component, ConverterException originalCE, String value, String convertMessageId, String maxMessageId, String maxString, String minMessageId, String minString) {
        if (value != null) {
            value = value.trim();
            try {
                new BigInteger(value);
                boolean isNegative = false;
                if (value.startsWith("-")) isNegative = true;
                if (isNegative) {
                    return createConverterException(context, component, minMessageId, value, minString);
                } else {
                    return createConverterException(context, component, maxMessageId, value, maxString);
                }
            } catch (NumberFormatException nfe) {
                return createConverterException(context, component, convertMessageId, value);
            }
        }
        return originalCE;
    }

    public static final ConverterException createConverterException(FacesContext context, UIComponent component, String messageId, String value) {
        return createConverterException(context, component, messageId, value, null);
    }

    public static final ConverterException createConverterException(FacesContext context, UIComponent component, String messageId, String value, String param) {
        Object label = _getLabel(component);
        FacesMessage message = MessageFactory.getMessage(context, messageId, new Object[] { label, value, param }, label);
        return new ConverterException(message);
    }

    private static final Object _getLabel(UIComponent component) {
        Object o = component.getAttributes().get("label");
        if (o == null) o = component.getValueBinding("label");
        return o;
    }

    /**
   * Create a converter for a type.
   */
    public static Converter createConverter(FacesContext context, Class<?> converterType) {
        if (converterType == null || converterType == Object.class) {
            return null;
        }
        try {
            Application application = context.getApplication();
            return application.createConverter(converterType);
        } catch (FacesException e) {
            _LOG.warning("CANNOT_CREATE_CONVERTER_LIKELY_BECAUSE_NO_CONVERTER_REGISTERED", converterType.toString());
            return null;
        }
    }

    /**
   * @deprecated method needs to an overhaul
   */
    @Deprecated
    public static String getClientValidation(FacesContext context, UIComponent component, String maxId, String minId, String defaultId, String maxVal, String minVal, String type) {
        return _getClientConversion(context, component, maxId, minId, defaultId, maxVal, minVal, false, type);
    }

    public static String getClientConversion(FacesContext context, UIComponent component, String maxId, String minId, String defaultId, String maxVal, String minVal) {
        return _getClientConversion(context, component, maxId, minId, defaultId, maxVal, minVal, true, null);
    }

    private static String _getClientConversion(FacesContext context, UIComponent component, String maxId, String minId, String defaultId, String maxVal, String minVal, boolean isConverter, String validatorType) {
        StringBuilder outBuffer = new StringBuilder(250);
        if (isConverter) outBuffer.append("new TrNumberConverter("); else outBuffer.append("new " + validatorType + "(");
        outBuffer.append("{LV:'");
        FacesMessage maxMessage = MessageFactory.getMessage(context, maxId, new Object[] { "{0}", "{1}", maxVal });
        outBuffer.append(XhtmlLafUtils.escapeJS(maxMessage.getDetail()));
        outBuffer.append("',LV_S:'");
        outBuffer.append(XhtmlLafUtils.escapeJS(maxMessage.getSummary()));
        outBuffer.append("',MV:'");
        FacesMessage minMessage = MessageFactory.getMessage(context, minId, new Object[] { "{0}", "{1}", minVal });
        outBuffer.append(XhtmlLafUtils.escapeJS(minMessage.getDetail()));
        outBuffer.append("',MV_S:'");
        outBuffer.append(XhtmlLafUtils.escapeJS(minMessage.getSummary()));
        outBuffer.append("',D:'");
        FacesMessage defaultMessage = MessageFactory.getMessage(context, defaultId, new Object[] { "{0}", "{1}" });
        outBuffer.append(XhtmlLafUtils.escapeJS(defaultMessage.getDetail()));
        outBuffer.append("',D_S:'");
        outBuffer.append(XhtmlLafUtils.escapeJS(defaultMessage.getSummary()));
        outBuffer.append("'},null,0,");
        outBuffer.append(maxVal);
        outBuffer.append(',');
        outBuffer.append(minVal);
        outBuffer.append(")");
        return outBuffer.toString();
    }

    /**
   * @deprecated not used currently
   * @param context
   * @param component
   * @param defaultId
   * @return
   */
    @Deprecated
    public static String getClientConversion(FacesContext context, UIComponent component, String defaultId) {
        StringBuilder outBuffer = new StringBuilder(250);
        outBuffer.append("new TrNumberConverter(");
        outBuffer.append("{D:'");
        FacesMessage defaultMessage = MessageFactory.getMessage(context, defaultId, new Object[] { "{0}", "{1}" });
        outBuffer.append(XhtmlLafUtils.escapeJS(defaultMessage.getDetail()));
        outBuffer.append("',D_S:'");
        outBuffer.append(XhtmlLafUtils.escapeJS(defaultMessage.getSummary()));
        outBuffer.append("'})");
        return outBuffer.toString();
    }

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(ConverterUtils.class);
}
