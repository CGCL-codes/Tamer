package org.impl;

import org.UIComponent;

/**
 * Component implementation
 * @author Ponec
 */
public class UIComponentLong implements UIComponent<Long> {

    private Long value;

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public void setStyle(String attr, String value) {
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
