package org.openremote.web.console.widget.panel.form;

import org.openremote.web.console.widget.InteractiveConsoleComponent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormField extends InteractiveConsoleComponent implements KeyUpHandler, BlurHandler {

    public static final String CLASS_NAME = "formFieldComponent";

    public static final String LABEL_CLASS_NAME = "formFieldLabelComponent";

    public static final String INPUT_CLASS_NAME = "formFieldInputComponent";

    private String label = null;

    private EnumFormInputType inputType;

    private Label lbl = null;

    private Widget input = null;

    private String validationStr = null;

    private boolean isValid;

    private boolean isOptional = false;

    private String name = null;

    private String defaultValue = null;

    public enum EnumFormInputType {

        TEXTBOX("textbox"), PASSWORD("password"), TEXTAREA("textarea"), SELECT("select");

        private final String name;

        EnumFormInputType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static EnumFormInputType getInputType(String name) {
            EnumFormInputType result = null;
            for (EnumFormInputType input : EnumFormInputType.values()) {
                if (input.getName().equalsIgnoreCase(name)) {
                    result = input;
                    break;
                }
            }
            return result;
        }
    }

    public FormField() {
        super(new VerticalPanel(), CLASS_NAME);
        VerticalPanel container = (VerticalPanel) getWidget();
        container.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        container.setSpacing(0);
        lbl = new Label();
        lbl.setHeight("20px");
        lbl.setWidth("100%");
        lbl.setStylePrimaryName(LABEL_CLASS_NAME);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setInputType(EnumFormInputType inputType) {
        this.inputType = inputType;
    }

    public EnumFormInputType getInputType() {
        return inputType;
    }

    public void setValidationString(String validationStr) {
        this.validationStr = validationStr;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setValue(String value) {
        switch(inputType) {
            case TEXTBOX:
            case PASSWORD:
                ((TextBox) input).setText(value);
                break;
        }
    }

    public String getValue() {
        String value = "";
        switch(inputType) {
            case TEXTBOX:
            case PASSWORD:
                value = ((TextBox) input).getText();
                break;
        }
        return value;
    }

    private void setInputValid(boolean valid) {
        isValid = valid;
        if (valid) {
            lbl.removeStyleName("invalid");
        } else {
            lbl.addStyleName("invalid");
        }
    }

    private void validateInput() {
        String value = "";
        switch(inputType) {
            case TEXTBOX:
                value = ((TextBox) input).getValue();
                break;
            case TEXTAREA:
                value = ((TextArea) input).getValue();
                break;
            case PASSWORD:
                value = ((PasswordTextBox) input).getValue();
                break;
        }
        if (isOptional && value.length() == 0) {
            setInputValid(true);
        } else {
            if (validationStr != null) {
                setInputValid(value.matches(validationStr));
            } else {
                setInputValid(true);
            }
        }
    }

    @Override
    public void onRender(int width, int height) {
        if (!isInitialised) {
            switch(inputType) {
                case TEXTBOX:
                    input = new TextBox();
                    input.addStyleName("formInputComponent");
                    input.addStyleName("formTextBoxComponent");
                    ((TextBox) input).setText(defaultValue);
                    break;
                case PASSWORD:
                    input = new PasswordTextBox();
                    input.addStyleName("formInputComponent");
                    input.addStyleName("formPasswordComponent");
                    ((TextBox) input).setText(defaultValue);
            }
            if (label != null && input != null) {
                int fieldWidth = (int) Math.round(width * .95);
                int leftMargin = (int) Math.round(width * .02);
                lbl.setText(label);
                lbl.setWidth(fieldWidth + "px");
                input.setWidth(fieldWidth + "px");
                DOM.setStyleAttribute(lbl.getElement(), "marginLeft", leftMargin * 2 + "px");
                DOM.setStyleAttribute(input.getElement(), "marginLeft", leftMargin + "px");
                input.setStylePrimaryName(INPUT_CLASS_NAME);
                ((VerticalPanel) getWidget()).add(lbl);
                ((VerticalPanel) getWidget()).add(input);
                setVisible(true);
                validateInput();
                if (validationStr != null && (inputType == EnumFormInputType.TEXTBOX || inputType == EnumFormInputType.TEXTAREA || inputType == EnumFormInputType.PASSWORD)) {
                    input.addDomHandler(this, KeyUpEvent.getType());
                }
            }
        }
        setHeight("50px");
        setWidth(width + "px");
        switch(inputType) {
            case TEXTBOX:
                ((TextBox) input).setText(defaultValue);
                break;
            case PASSWORD:
                ((TextBox) input).setText(defaultValue);
        }
        if (label != null && input != null) {
            setVisible(true);
            validateInput();
            if (validationStr != null && (inputType == EnumFormInputType.TEXTBOX || inputType == EnumFormInputType.TEXTAREA || inputType == EnumFormInputType.PASSWORD)) {
                input.addDomHandler(this, KeyUpEvent.getType());
                input.addDomHandler(this, BlurEvent.getType());
            }
        }
    }

    @Override
    public void onUpdate(int width, int height) {
        int fieldWidth = (int) Math.round(width * .95);
        int leftMargin = (int) Math.round(width * .02);
        this.width = width;
        setWidth(width + "px");
        input.setWidth(fieldWidth + "px");
        lbl.setWidth(fieldWidth + "px");
        DOM.setStyleAttribute(lbl.getElement(), "marginLeft", leftMargin * 2 + "px");
        DOM.setStyleAttribute(input.getElement(), "marginLeft", leftMargin + "px");
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        validateInput();
    }

    @Override
    public void onBlur(BlurEvent event) {
        validateInput();
    }
}
