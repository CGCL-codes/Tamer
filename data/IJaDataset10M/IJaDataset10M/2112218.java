package research.persistence;

public class PersistenceException extends RuntimeException {

    static final long serialVersionUID = 123L;

    private String msg = null;

    public PersistenceException(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
