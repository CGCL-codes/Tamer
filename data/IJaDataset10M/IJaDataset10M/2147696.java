package org.nakedobjects.noa.facets.propparam.validate.regex;

import org.nakedobjects.noa.interactions.InteractionContext;
import org.nakedobjects.noa.interactions.InvalidException;

/**
 * The interaction is invalid because has exceeded a 
 * <tt>@MaxLength</tt> annotation.
 */
public class InvalidRegExException extends InvalidException {

    private static final long serialVersionUID = 1L;

    public InvalidRegExException(InteractionContext ic, String format, String validation, boolean caseSensitive) {
        this(ic, format, validation, caseSensitive, "Does not match pattern");
    }

    public InvalidRegExException(InteractionContext ic, String format, String validation, boolean caseSensitive, String message) {
        super(ic, message);
        this.format = format;
        this.validation = validation;
        this.caseSensitive = caseSensitive;
    }

    private String format;

    public String getFormat() {
        return format;
    }

    private String validation;

    public String getValidation() {
        return validation;
    }

    private boolean caseSensitive;

    public boolean isCaseSensitive() {
        return caseSensitive;
    }
}
