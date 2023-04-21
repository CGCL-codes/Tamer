package ch.trackedbean.validator.defaultValidators;

import java.util.*;
import ch.trackedbean.validator.*;
import ch.trackedbean.validator.internal.*;

/**
 * Validator for {@link Past}.<br>
 * 
 * @author M. Hautle
 */
public class PastValidator extends AbstractValidator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(IPropertyValidatorConfiguration cfg) {
        error = new ErrorDescription(cfg.getString(Past.ATTR_MESSAGE, Past.DEFAULT_MESSAGE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isValid(Object value) {
        if (value == null) return true;
        if (value instanceof Date) return ((Date) value).getTime() < System.currentTimeMillis();
        if (value instanceof Calendar) return ((Calendar) value).getTimeInMillis() < System.currentTimeMillis();
        return false;
    }
}
