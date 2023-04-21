package br.ufmg.saotome.arangi.view.converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.ConverterException;

/**
 * Just add some i18n to the original behavior
 * @author Ildeu Fernandes
 * 
 */
public class BigDecimalConverterI18N extends BigDecimalConverter {

    /**
     * <p>Return the <code>Locale</code> to be used when parsing or formatting
     * dates and times. The <code>Locale</code> stored in the 
     * {@link javax.faces.component.UIViewRoot} for the current
     * request is returned.</p>
     */
    private Locale getLocale() {
        return (FacesContext.getCurrentInstance().getViewRoot().getLocale());
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (value == null) {
            return (null);
        }
        value = value.trim();
        if (value.length() < 1) {
            return (null);
        }
        try {
            NumberFormat nf = NumberFormat.getInstance(getLocale());
            double d = (nf.parse(value)).doubleValue();
            BigDecimal bd = new BigDecimal(d);
            return (bd);
        } catch (NumberFormatException nfe) {
            throw new ConverterException(com.sun.faces.util.MessageFactory.getMessage(context, DECIMAL_ID, value, "198.23", com.sun.faces.util.MessageFactory.getLabel(context, component)));
        } catch (Exception e) {
            throw new ConverterException(com.sun.faces.util.MessageFactory.getMessage(context, DECIMAL_ID, value, "198.23", com.sun.faces.util.MessageFactory.getLabel(context, component)));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String) value;
        }
        try {
            NumberFormat nf = NumberFormat.getInstance(getLocale());
            return (nf.format(((BigDecimal) value).doubleValue()));
        } catch (Exception e) {
            throw new ConverterException(com.sun.faces.util.MessageFactory.getMessage(context, STRING_ID, value, com.sun.faces.util.MessageFactory.getLabel(context, component)), e);
        }
    }
}
