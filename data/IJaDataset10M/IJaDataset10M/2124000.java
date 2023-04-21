package com.volantis.mcs.xml.schema.impl.validation;

import com.volantis.mcs.xml.schema.model.Content;
import java.util.Set;

public class UnlimitedValidator implements ContentValidator {

    private final ContentValidator validator;

    public UnlimitedValidator(ContentValidator validator) {
        if (validator.requiresPerElementState()) {
            throw new IllegalArgumentException("Only supports stateless validators");
        }
        this.validator = validator;
    }

    public ContentValidator createValidator() {
        return this;
    }

    public boolean requiresPerElementState() {
        return false;
    }

    public ValidationEffect check(Content content, ValidationState state) {
        ValidationEffect effect = validator.check(content, state);
        if (effect == ValidationEffect.WOULD_FAIL) {
            effect = ValidationEffect.WOULD_SATISFY;
        } else if (effect == ValidationEffect.CONSUMED_SATISFIED) {
            effect = ValidationEffect.CONSUMED;
        }
        return effect;
    }

    public boolean determineNextExpectedContent(Set nextExpectedContent, ValidationState state) {
        validator.determineNextExpectedContent(nextExpectedContent, state);
        return true;
    }

    public SatisfactionLevel checkSatisfactionLevel() {
        return SatisfactionLevel.PARTIAL;
    }

    public void reset() {
    }
}
