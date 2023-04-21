package eu.maydu.gwt.validation.client.validators.standard;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.Validator;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;

/**
 * Validates that a field has some (arbitrary) data put in.
 * 
 * It can be configured to trim the input or to not trim the input.
 * When the input is trimmed a String like "    " will trigger the
 * validation actions. If trimming is not enabled the validation would
 * succeed in that case.
 * 
 * This validator ignores the <code>required</code> field setting.
 * 
 * @author Anatol Mayen
 *
 */
public class NotEmptyValidator extends Validator<NotEmptyValidator> {

    private TextBoxBase textBox = null;

    private SuggestBox suggestBox = null;

    /**
	 * Default mode uses trimming
	 */
    private boolean trim = true;

    public NotEmptyValidator(TextBoxBase text) {
        this(text, null);
    }

    public NotEmptyValidator(TextBoxBase text, String customMsgKey) {
        this.textBox = text;
        this.setCustomMsgKey(customMsgKey);
    }

    public NotEmptyValidator(SuggestBox suggest) {
        this(suggest, null);
    }

    public NotEmptyValidator(SuggestBox suggest, String customMsgKey) {
        this.suggestBox = suggest;
        this.setCustomMsgKey(customMsgKey);
    }

    public NotEmptyValidator(TextBoxBase text, boolean trimBeforeValidation) {
        this(text, trimBeforeValidation, null);
    }

    public NotEmptyValidator(TextBoxBase text, boolean trimBeforeValidation, String customMsgKey) {
        this.textBox = text;
        this.trim = trimBeforeValidation;
        this.setCustomMsgKey(customMsgKey);
    }

    public NotEmptyValidator(SuggestBox suggest, boolean trimBeforeValidation) {
        this(suggest, trimBeforeValidation, null);
    }

    public NotEmptyValidator(SuggestBox suggest, boolean trimBeforeValidation, String customMsgKey) {
        this.suggestBox = suggest;
        this.trim = trimBeforeValidation;
        this.setCustomMsgKey(customMsgKey);
    }

    @Override
    public void invokeActions(ValidationResult result) {
        if (textBox != null) {
            for (ValidationAction<TextBoxBase> va : this.getFailureActions()) va.invoke(result, textBox);
        } else {
            for (ValidationAction<SuggestBox> va : this.getFailureActions()) va.invoke(result, suggestBox);
        }
    }

    @Override
    public <V extends ValidationMessages> ValidationResult validate(V messages) {
        String text;
        if (suggestBox != null) text = suggestBox.getText(); else text = textBox.getText();
        if (trim) text = text.trim();
        if (text.length() == 0) return new ValidationResult(getErrorMessage(messages, messages.getStandardMessages().notEmpty()));
        return null;
    }
}
