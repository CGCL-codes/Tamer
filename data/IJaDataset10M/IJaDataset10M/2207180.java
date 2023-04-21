package org.databene.commons.validator;

import org.databene.commons.Validator;

/**
 * Validates the suffix of a String.<br/><br/>
 * Created: 02.08.2011 07:29:25
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class SuffixValidator extends SubStringValidator {

    protected String suffix;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public SuffixValidator(String suffix) {
        super(-2, null, (Validator) new ConstantValidator(suffix));
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + suffix + ']';
    }
}
