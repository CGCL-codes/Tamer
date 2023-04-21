package com.genia.toolbox.web.gwt.basics.client.i18n;

import org.springframework.util.StringUtils;
import com.genia.toolbox.basics.bean.AbstractEncodePropertyEditor;

/**
 * This class allows spring to generated <code>GwtI18nMessage</code> from
 * <code>String</code>.
 */
public class GwtI18nMessageEditor extends AbstractEncodePropertyEditor {

    /**
   * Get a new <code>String</code> from a <code>GwtI18nMessage</code>.
   * 
   * @return the String representation of a <code>GwtI18nMessage</code>.
   */
    @Override
    public String getAsText() {
        GwtI18nMessage i18nMessage = (GwtI18nMessage) getValue();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i18nMessage.getKey());
        for (String s : encode(i18nMessage.getParameters())) {
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    /**
   * Generate a new <code>GwtI18nMessage</code> from a <code>String</code>.
   * 
   * @param text
   *          the <code>String</code> that represents the gwt i18n message
   */
    @Override
    public void setAsText(String text) {
        String[] values = decode(text.split(SEPARATOR));
        if (values.length == 0 || !StringUtils.hasText(values[0])) {
            throw new IllegalArgumentException("The permission must be of the form .*:(.*:)*");
        }
        GwtI18nMessage i18nMessage = new GwtI18nMessage(values[0]);
        if (values.length > 1) {
            String[] parameters = new String[values.length - 1];
            for (int i = 1; i < values.length; ++i) {
                parameters[i - 1] = values[i];
            }
            i18nMessage.setParameters(parameters);
        }
        setValue(i18nMessage);
    }
}
