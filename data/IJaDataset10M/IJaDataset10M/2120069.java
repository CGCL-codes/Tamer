package be.lassi.ui.sheet;

import java.text.MessageFormat;
import com.jgoodies.validation.Validatable;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;
import be.lassi.base.Holder;
import be.lassi.ui.util.ValidationSupport;

/**
 * Validates renumbering parameters.
 */
public class CueRenumberParametersValidator implements Validatable {

    private static final int MIN_CUE_NUMBER = 1;

    private static final int MAX_CUE_NUMBER = 9999;

    private static final int MIN_INCREMENT = 1;

    private static final int MAX_INCREMENT = 10;

    private final CueRenumberParameters parameters;

    /**
     * Constructs a new validator.
     *
     * @param parameters the parameters to be validated
     */
    public CueRenumberParametersValidator(final CueRenumberParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResult validate() {
        ValidationSupport support = new ValidationSupport();
        validateFirstCueNumber(support);
        validateIncrement(support);
        return support.getResult();
    }

    private void validateFirstCueNumber(final ValidationSupport support) {
        Holder<String> holder = parameters.getFirstCueNumber();
        String value = holder.getValue();
        if (ValidationUtils.isBlank(value)) {
            String message = "The first cue number is mandatory";
            support.add(holder.getName(), message);
        } else if (!ValidationUtils.isNumeric(value)) {
            String message = "The cue number should be numeric";
            support.add(holder.getName(), message);
        } else {
            if (!support.isInRange(value, MIN_CUE_NUMBER, MAX_CUE_NUMBER)) {
                Object[] args = new Object[2];
                args[0] = MIN_CUE_NUMBER;
                args[1] = "" + MAX_CUE_NUMBER;
                String message = "The first cue number should be between {0} and {1}";
                message = MessageFormat.format(message, args);
                support.add(holder.getName(), message);
            }
        }
    }

    private void validateIncrement(final ValidationSupport support) {
        Holder<String> holder = parameters.getIncrement();
        String value = holder.getValue();
        if (ValidationUtils.isBlank(value)) {
            String message = "The increment is mandatory";
            support.add(holder.getName(), message);
        } else if (!ValidationUtils.isNumeric(value)) {
            String message = "The increment should be numeric";
            support.add(holder.getName(), message);
        } else {
            if (!support.isInRange(value, MIN_INCREMENT, MAX_INCREMENT)) {
                Object[] args = new Object[2];
                args[0] = MIN_INCREMENT;
                args[1] = MAX_INCREMENT;
                String message = "The increment should be between {0} and {1}";
                message = MessageFormat.format(message, args);
                support.add(holder.getName(), message);
            }
        }
    }
}
