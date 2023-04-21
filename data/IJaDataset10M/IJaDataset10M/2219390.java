package ingenias.jade.exception;

public class NoAgentsFound extends Exception {

    public NoAgentsFound() {
    }

    public NoAgentsFound(String message) {
        super(message);
    }

    public NoAgentsFound(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAgentsFound(Throwable cause) {
        super(cause);
    }
}
