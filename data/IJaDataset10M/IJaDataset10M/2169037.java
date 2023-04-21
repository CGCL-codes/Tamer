package de.caffeine.jargus;

import java.util.*;

/**
 * Checks if one of the given parameter exists.
 *
 * @author Thomas Foertsch
 * @version $Id: OrValidator.java,v 1.2 2001/04/25 18:17:32 blob79 Exp $
 */
public class OrValidator extends AbstractValidator {

    /**
	 *
	 */
    public boolean validate(HashMap parameters) {
        Iterator vpar = getValidationParameters();
        while (vpar.hasNext()) {
            String pValue = (String) parameters.get(vpar.next());
            if (pValue != null) {
                pValue = isTrim() ? pValue.trim() : pValue;
                if (pValue.length() > 0) return true;
            }
        }
        return false;
    }

    public void addValidationArgument(String argument) {
        throw new UnsupportedOperationException("Not Supported");
    }
}
