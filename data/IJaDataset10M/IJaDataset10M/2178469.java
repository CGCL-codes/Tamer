package org.apache.myfaces.shared_impl.taglib.core;

import java.util.Locale;
import java.util.TimeZone;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.webapp.ConverterTag;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import org.apache.myfaces.shared_impl.util.LocaleUtils;

/**
 * @author Manfred Geiler (latest modification by $Author: schof $)
 * @version $Revision: 382015 $ $Date: 2006-03-01 14:47:11 +0100 (Wed, 01 Mar 2006) $
 */
public class ConvertDateTimeTagBase extends ConverterTag {

    private static final long serialVersionUID = -757757296071312897L;

    private String _dateStyle = "default";

    private String _locale = null;

    private String _pattern = null;

    private String _timeStyle = "default";

    private String _timeZone = null;

    private String _type = null;

    public void setDateStyle(String dateStyle) {
        _dateStyle = dateStyle;
    }

    public void setLocale(String locale) {
        _locale = locale;
    }

    public void setPattern(String pattern) {
        _pattern = pattern;
    }

    public void setTimeStyle(String timeStyle) {
        _timeStyle = timeStyle;
    }

    public void setTimeZone(String timeZone) {
        _timeZone = timeZone;
    }

    public void setType(String type) {
        _type = type;
    }

    protected Converter createConverter() throws JspException {
        DateTimeConverter converter = (DateTimeConverter) super.createConverter();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        setConverterDateStyle(facesContext, converter, _dateStyle);
        setConverterLocale(facesContext, converter, _locale);
        setConverterPattern(facesContext, converter, _pattern);
        setConverterTimeStyle(facesContext, converter, _timeStyle);
        setConverterTimeZone(facesContext, converter, _timeZone);
        setConverterType(facesContext, converter, _type);
        return converter;
    }

    protected static void setConverterLocale(FacesContext facesContext, DateTimeConverter converter, String value) {
        Locale locale = null;
        Object _value = null;
        if (value == null) return;
        if (UIComponentTag.isValueReference(value)) {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            _value = vb.getValue(facesContext);
            if (_value instanceof Locale) {
                locale = (Locale) _value;
            } else {
                locale = LocaleUtils.converterTagLocaleFromString(_value.toString());
            }
        } else {
            locale = LocaleUtils.converterTagLocaleFromString(value);
        }
        converter.setLocale(locale);
    }

    private static void setConverterDateStyle(FacesContext facesContext, DateTimeConverter converter, String value) {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value)) {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setDateStyle((String) vb.getValue(facesContext));
        } else {
            converter.setDateStyle(value);
        }
    }

    private static void setConverterPattern(FacesContext facesContext, DateTimeConverter converter, String value) {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value)) {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setPattern((String) vb.getValue(facesContext));
        } else {
            converter.setPattern(value);
        }
    }

    private static void setConverterTimeStyle(FacesContext facesContext, DateTimeConverter converter, String value) {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value)) {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setTimeStyle((String) vb.getValue(facesContext));
        } else {
            converter.setTimeStyle(value);
        }
    }

    private static void setConverterTimeZone(FacesContext facesContext, DateTimeConverter converter, String value) {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value)) {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            TimeZone timeZone = null;
            Object _value = vb.getValue(facesContext);
            if (_value instanceof TimeZone) {
                timeZone = (TimeZone) _value;
            } else {
                timeZone = TimeZone.getTimeZone(_value.toString());
            }
            converter.setTimeZone(timeZone);
        } else {
            converter.setTimeZone(TimeZone.getTimeZone(value));
        }
    }

    private static void setConverterType(FacesContext facesContext, DateTimeConverter converter, String value) {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value)) {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setType((String) vb.getValue(facesContext));
        } else {
            converter.setType(value);
        }
    }
}
