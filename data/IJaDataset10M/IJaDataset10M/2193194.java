package com.malethan.seemorej.taglib;

import com.malethan.seemorej.taglib.elements.Select;
import java.util.Collection;

public class SelectTag extends ElementTag {

    Collection options;

    String optionLabel;

    String optionValue;

    public void setOptions(Collection options) {
        this.options = options;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    protected String constructElement() {
        Select select = new Select();
        setCommonAttributes(select);
        select.setOptions(options);
        select.setOptionLabel(optionLabel);
        select.setOptionValue(optionValue);
        return select.toString();
    }

    public void release() {
        super.release();
        this.options = null;
        this.optionLabel = null;
        this.optionValue = null;
    }
}
