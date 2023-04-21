package de.caffeine.jargus;

import java.util.*;

/**
 * Check if parameter exists.
 *
 * @author Thomas Foertsch
 * @version $Id: ExistValidator.java,v 1.2 2001/04/25 18:17:32 blob79 Exp $
 *
 */
public class ExistValidator extends AbstractValidator {

    /**
	 * @param parameter checked Parameter
	 * @param trim Trim whitespaces. Default: true
	 */
    public ExistValidator(String parameter, boolean trim) {
        this.addValidationParameter(parameter);
        this._trim = trim;
    }

    /**
	 * @param parameter checked Parameter
	 */
    public ExistValidator(String parameter) {
        this.addValidationParameter(parameter);
    }

    /**
	 * Keep standard constructor.
	 */
    public ExistValidator() {
    }

    /**
	 * @param trim Trim parameter values.
	 */
    public ExistValidator(boolean trim) {
        _trim = trim;
    }

    /**
	 * Check if Parameter exist
	 */
    public boolean validate(HashMap parameters) {
        Iterator vparams = this.getValidationParameters();
        while (vparams.hasNext()) {
            String parameter = (String) parameters.get(vparams.next());
            if (parameter == null) return false; else {
                if (_trim) parameter = parameter.trim();
                if (parameter.length() <= 0) return false;
            }
        }
        return true;
    }

    public void addValidationArgument(String argument) {
        throw new UnsupportedOperationException("Not Supported");
    }
}
