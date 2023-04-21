package lt.ku.ik.recon.logic.model;

/**
 *
 * @author linas
 */
public class DataException extends Exception {

    public DataException(Exception exception) {
        super(exception.getCause());
    }
}
