package townhall.exceptions;

import townhall.exceptions.BaseException;

/**
 *
 * @author Bartosz Tuszyński
 */
public class DataStoreException extends BaseException {

    public DataStoreException() {
        super();
    }

    public DataStoreException(String message) {
        super(message);
    }

    public DataStoreException(Exception e) {
        super(e);
    }

    public DataStoreException(String message, Exception e) {
        super(message, e);
    }
}
