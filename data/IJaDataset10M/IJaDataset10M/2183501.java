package com.definity.toolkit.web.taglib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.beanutils.PropertyUtils;
import com.definity.toolkit.i18n.I18n;
import com.definity.toolkit.i18n.I18nFactory;

public abstract class BaseTag extends TagSupport {

    private static final long serialVersionUID = -312889443503545852L;

    private String pattern;

    private String styleClass;

    private String listAttribute;

    private String i18n;

    public BaseTag() {
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getListAttribute() {
        return listAttribute;
    }

    public void setListAttribute(String listAttribute) {
        this.listAttribute = listAttribute;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    protected final String getContextPath() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return request.getContextPath();
    }

    protected String createStyleClass(String... styles) {
        if (styles.length == 0 && (styleClass == null || styleClass.length() == 0)) return "";
        StringBuilder html = new StringBuilder();
        html.append(" class=\"");
        if (styleClass != null) html.append(styleClass);
        for (String style : styles) {
            html.append(" ");
            html.append(style);
        }
        html.append("\" ");
        return html.toString();
    }

    protected String valueToString(Object value) throws JspException {
        if (value == null) return "";
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern == null ? "dd/MM/yyyy" : pattern);
            return sdf.format(value);
        } else if (value instanceof Double) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            return nf.format(value);
        } else if (value instanceof BigDecimal) {
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            return nf.format(value);
        } else if (value instanceof Boolean) {
            return i18n().tl("Boolean." + value);
        } else if (value.getClass().isEnum()) {
            String enumName = value.getClass().getSimpleName();
            return i18n().tl(enumName + "." + value);
        } else if (value instanceof Collection<?>) {
            String text = "";
            Collection<?> list = (Collection<?>) value;
            for (Object object : list) {
                if (!text.isEmpty()) text += ", ";
                text += getValue(object, listAttribute);
            }
            return text;
        }
        return value.toString();
    }

    private Object getAttributeValue(Object object, String attribute) throws JspException {
        if (object == null) return null;
        try {
            return PropertyUtils.getProperty(object, attribute);
        } catch (IllegalAccessException e) {
            throw new JspException(e);
        } catch (InvocationTargetException e) {
            throw new JspException(e);
        } catch (NoSuchMethodException e) {
            throw new JspException(e);
        }
    }

    protected Object getValue(Object object, String attribute) throws JspException {
        if (!attribute.contains(".")) return getAttributeValue(object, attribute);
        String[] variables = attribute.split("\\.");
        int i = 0;
        Object value = object;
        String variable = variables[i];
        while (value != null) {
            value = getAttributeValue(value, variable);
            if (++i >= variables.length) break;
            variable = variables[i];
        }
        return value;
    }

    protected void write(StringBuilder html) {
        write(html.toString());
    }

    protected void write(String html) {
        if (html == null) return;
        JspWriter out = pageContext.getOut();
        try {
            out.print(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected final I18n i18n() {
        if (i18n != null) return I18nFactory.create(i18n);
        return I18nFactory.getApplication();
    }
}
