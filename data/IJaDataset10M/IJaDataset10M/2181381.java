package org.genxdm.processor.w3c.xs.exception.scc;

import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.exceptions.ComponentConstraintException;

@SuppressWarnings("serial")
public final class SccMinLengthLessThanEqualToMaxLengthException extends ComponentConstraintException {

    private final int m_minLength;

    private final int m_maxLength;

    public SccMinLengthLessThanEqualToMaxLengthException(final int minLength, final int maxLength) {
        super(ValidationOutcome.SCC_MinLengthLessThanEqualToMaxLength, "4.3.2.4");
        m_minLength = minLength;
        m_maxLength = maxLength;
    }

    @Override
    public String getMessage() {
        return "The {value}, " + m_minLength + ", of minLength must be less than or equal to the {value}, " + m_maxLength + ", of maxLength.";
    }
}
