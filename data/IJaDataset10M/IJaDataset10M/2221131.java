package br.com.linkcom.neo.bean.editors;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import org.springframework.util.StringUtils;

/**
 * @author rogelgarcia
 */
public class CustomSqlDateEditor extends PropertyEditorSupport {

    private final DateFormat dateFormat;

    private final boolean allowEmpty;

    /**
	 * Create a new CustomDateEditor instance, using the given DateFormat
	 * for parsing and rendering.
	 * <p>The "allowEmpty" parameter states if an empty String should
	 * be allowed for parsing, i.e. get interpreted as null value.
	 * Otherwise, an IllegalArgumentException gets thrown in that case.
	 * @param dateFormat DateFormat to use for parsing and rendering
	 * @param allowEmpty if empty strings should be allowed
	 */
    public CustomSqlDateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
    }

    /**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        } else {
            try {
                setValue(new java.sql.Date(this.dateFormat.parse(text).getTime()));
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage());
            }
        }
    }

    /**
	 * Format the Date as String, using the specified DateFormat.
	 */
    public String getAsText() {
        return (getValue() == null ? "" : this.dateFormat.format((Date) getValue()));
    }

    @Override
    public Object getValue() {
        Object object = super.getValue();
        if (!(object instanceof java.sql.Date) && object instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date) object).getTime());
        }
        return object;
    }
}
