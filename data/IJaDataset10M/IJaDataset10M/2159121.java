package eu.maydu.gwt.validation.client.groupValidators;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import eu.maydu.gwt.validation.client.GroupValidator;
import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.transformers.TrimTransformer;
import eu.maydu.gwt.validation.client.validators.strings.StringLengthValidator;

/**
 * 
 * @author Anatol Mayen
 *
 */
public class TrimmedTextLengthValidator extends GroupValidator<TrimmedTextLengthValidator> {

    private TextBoxBase text;

    private SuggestBox suggest;

    public TrimmedTextLengthValidator(TextBoxBase text, int min, int max, boolean preventsPropagation) {
        super(preventsPropagation);
        if (text == null) throw new IllegalArgumentException("text must not be null");
        this.text = text;
        setup(min, max);
    }

    public TrimmedTextLengthValidator(TextBoxBase text, int min, int max) {
        this(text, min, max, false);
    }

    public TrimmedTextLengthValidator(SuggestBox suggest, int min, int max, boolean preventsPropagation) {
        super(preventsPropagation);
        if (suggest == null) throw new IllegalArgumentException("suggest must not be null");
        this.suggest = suggest;
        setup(min, max);
    }

    public TrimmedTextLengthValidator(SuggestBox suggest, int min, int max) {
        this(suggest, min, max, false);
    }

    private void setup(int min, int max) {
        if (text != null) {
            addValidators(new TrimTransformer(this.text), new StringLengthValidator(this.text, min, max));
        } else {
            addValidators(new TrimTransformer(this.suggest), new StringLengthValidator(this.suggest, min, max));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invokeActions(ValidationResult result) {
        super.invokeActions(result);
        if (text != null) {
            for (ValidationAction action : getFailureActions()) {
                action.invoke(result, text);
            }
        } else {
            for (ValidationAction action : getFailureActions()) {
                action.invoke(result, suggest);
            }
        }
    }
}
