package org.middleheaven.util.validation;

import java.util.Collection;
import java.util.Map;
import org.middleheaven.util.collections.Enumerable;

public class LengthValidator<T> implements Validator<T> {

    private int min;

    private int max;

    public LengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ValidationResult validate(Object obj) {
        DefaultValidationResult result = new DefaultValidationResult();
        int size = 0;
        if (obj == null) {
            size = 0;
        } else if (obj instanceof Collection) {
            size = ((Collection<?>) obj).size();
        } else if (obj instanceof CharSequence) {
            size = ((CharSequence) obj.toString().trim()).length();
        } else if (obj instanceof Map) {
            size = ((Map<?, ?>) obj).size();
        } else if (obj instanceof Enumerable) {
            size = ((Enumerable<?>) obj).size();
        } else {
            return result;
        }
        if (size < min) {
            result.add(MessageInvalidationReason.error(obj, "invalid.length.min", min, max));
        } else if (size > max) {
            result.add(MessageInvalidationReason.error(obj, "invalid.length.max", min, max));
        }
        return result;
    }
}
