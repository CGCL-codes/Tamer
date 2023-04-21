package com.threerings.gwt.ui;

import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * Contains HTML5 form support.
 */
class FormSupport {

    /**
     * Set the placeholder text to use on the specified form field.
     * This text will be shown when the field is blank and unfocused.
     */
    public <B extends TextBoxBase> B setPlaceholderText(B box, String placeholder) {
        box.getElement().setAttribute("placeholder", placeholder);
        return box;
    }
}
