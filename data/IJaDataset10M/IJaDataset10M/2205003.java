package exception;

public class ObjectNotExistError extends RuntimeException {

    public ObjectNotExistError(String tblName) {
        super(tblName);
    }
}
