package de.highbyte_le.weberknecht.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import de.highbyte_le.weberknecht.util.TextFormat;

/**
 * hold data for web pages.
 * 
 * <p>This could be a replacement for {@link GenericFormBean} and some of your hand made page beans.</p>  
 * 
 * @see GenericFormBean
 * @see GenericListBean
 * @author pmairif
 */
public class GenericBean<T> implements Serializable {

    private Map<T, String> values = new HashMap<T, String>();

    private static final long serialVersionUID = -2587187021195865174L;

    public GenericBean() {
    }

    public GenericBean(Map<T, String> valueMap) {
        populate(valueMap);
    }

    public void populate(Map<T, String> valueMap) {
        this.values.putAll(valueMap);
    }

    public void putValue(T fieldName, String value) {
        values.put(fieldName, value);
    }

    public void putValue(T fieldName, int value) {
        values.put(fieldName, Integer.toString(value));
    }

    /**
	 * check, if value is present
	 */
    public boolean hasValue(T field) {
        return values.containsKey(field) && values.get(field) != null;
    }

    /**
	 * get value
	 */
    public String getValue(T field) {
        String val = values.get(field);
        if (null == val) val = "";
        return val;
    }

    /**
	 * get value HTML escaped
	 */
    public String getValue_htmlEscaped(T field) {
        return StringEscapeUtils.escapeHtml(getValue(field));
    }

    /**
	 * convert line breaks etc.
	 */
    public String getValue_htmlFormatted(T field) {
        String text = getValue_htmlEscaped(field);
        return TextFormat.getTextHtmlFormatted(text);
    }

    /**
	 * check, if value is present
	 */
    public boolean has(T field) {
        return hasValue(field);
    }

    /**
	 * get value
	 */
    public String val(T field) {
        return getValue(field);
    }

    /**
	 * get value HTML escaped
	 */
    public String valHtmlEsc(T field) {
        return getValue_htmlEscaped(field);
    }

    public Set<T> getValKeySet() {
        return values.keySet();
    }
}
