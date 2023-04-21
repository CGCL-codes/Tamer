package ca.llsutherland.squash.domain.helper;

import ca.llsutherland.squash.domain.Clock;
import ca.llsutherland.squash.exceptions.ValidationException;
import ca.llsutherland.squash.utils.ErrorConstants;
import ca.llsutherland.squash.utils.StringUtils;

public class NamedDomainObjectHelper {

    public static void assertValidName(String name) {
        if (StringUtils.containsInvalidCharacters(name)) {
            throw new ValidationException(ErrorConstants.INVALID_NAME_ERROR);
        }
    }

    public static void assertValidDate(Clock clock) {
        if (clock == null) {
            throw new ValidationException(ErrorConstants.NULL_DATE_ERROR);
        }
    }
}
