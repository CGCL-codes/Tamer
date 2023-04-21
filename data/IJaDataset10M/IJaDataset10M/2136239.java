package org.easyform.component;

import org.easyform.FieldComponent;

/**
 * @author ZhuYanYu
 * @version $Revision: 1.1
 * @since 2009-7-28
 */
public class DateField extends FieldComponent {

    private String defaultValue;

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public int countHeight() {
        if (isHeightChanged()) {
            return getHeight();
        }
        return fontSize + 8;
    }

    public int countWidth() {
        if (isWidthChanged()) {
            return getWidth();
        }
        return fontSize * 12;
    }
}
