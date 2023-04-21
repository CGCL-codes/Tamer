package org.synthful.gwt.vaadin.ui.FormFieldProperties;

import org.synthful.lang.Empty;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class FormTextFieldProperty extends FormFieldProperty {

    private static final long serialVersionUID = 1L;

    public FormTextFieldProperty(Object value) {
        super(value);
    }

    public FormTextFieldProperty(String id, Object value, String caption, String width, String height, boolean visible) {
        super(value, String.class, true);
        this.id = id;
        this.caption = caption;
        this.width = width;
        this.height = height;
        this.visible = visible;
    }

    public FormTextFieldProperty(String id, String caption, String width, String height, int minLength, int maxLength, boolean required, boolean visible, boolean readOnly) {
        super(Empty.Blank, String.class, readOnly);
        this.id = id;
        this.caption = caption;
        this.width = width;
        this.height = height;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
        this.visible = visible;
    }

    public FormTextFieldProperty(String id, String caption, String width, String height, int minLength, int maxLength, boolean required, boolean visible, boolean readOnly, boolean secret) {
        super(Empty.Blank, String.class, readOnly);
        this.id = id;
        this.caption = caption;
        this.width = width;
        this.height = height;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
        this.visible = visible;
        this.secret = secret;
    }

    public Field createField(Object propertyId) {
        TextField f = new TextField();
        if (this.secret) f.setSecret(true);
        StringLengthValidator validator = new StringLengthValidator(this.caption + " must be " + this.minLength + " - " + this.maxLength + " characters", this.minLength, this.maxLength, false);
        f.addValidator(validator);
        return f;
    }

    public int minLength = 0, maxLength = 10;

    private boolean secret = false;
}
