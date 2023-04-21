package de.offis.semanticmm4u.failures.media_elements_connectors;

import de.offis.semanticmm4u.failures.MM4UMediaElementsConnectorException;

public class MM4UCannotCloseMediaElementsConnectionException extends MM4UMediaElementsConnectorException {

    protected MM4UCannotCloseMediaElementsConnectionException() {
    }

    public MM4UCannotCloseMediaElementsConnectionException(Object incorrectObject, String incorrectMethod, String comment) {
        super(incorrectObject, incorrectMethod, comment);
    }
}
