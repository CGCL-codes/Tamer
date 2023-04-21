package metso.dal;

/**
 * Eccezione lanciata dai persistence manager in caso di errori di esecuzione.
 */
public class DalException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6635629730440189957L;

    public DalException() {
        super();
    }

    public DalException(String message, Throwable e) {
        super(message, e);
    }

    public DalException(String message) {
        super(message);
    }
}
