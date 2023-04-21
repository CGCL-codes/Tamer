package tw.idv.cut7man.cuttle.core.exception;

public class SessionPersistentException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5866131341066367332L;

    private String action;

    private String process;

    private String message;

    public String getAction() {
        return action;
    }

    public SessionPersistentException setAction(String action) {
        this.action = action;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SessionPersistentException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getProcess() {
        return process;
    }

    public SessionPersistentException setProcess(String process) {
        this.process = process;
        return this;
    }
}
