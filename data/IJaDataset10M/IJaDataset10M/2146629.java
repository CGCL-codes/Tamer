package de.offis.semanticmm4u.failures.user_profiles_connectors;

import de.offis.semanticmm4u.failures.MM4UUserProfilesConnectorException;

public class MM4UCannotCloseUserProfilesConnectionException extends MM4UUserProfilesConnectorException {

    protected MM4UCannotCloseUserProfilesConnectionException() {
    }

    public MM4UCannotCloseUserProfilesConnectionException(Object incorrectObject, String incorrectMethod, String comment) {
        super(incorrectObject, incorrectMethod, comment);
    }
}
