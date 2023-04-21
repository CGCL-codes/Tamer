package felper.exception.user;

import felper.exception.FelperException;

/**
 * @author Golubev Andrey
 */
public class FelperUserAddRemoveOperationException extends FelperException {

    public FelperUserAddRemoveOperationException() {
    }

    public FelperUserAddRemoveOperationException(String message) {
        super(message);
    }
}
