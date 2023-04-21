package donnees;

/**
 *
 * @author rwan
 */
public class ExceptionCreationDonnees extends Exception {

    private String message;

    public ExceptionCreationDonnees(String _message) {
        super();
        message = _message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message.toString();
    }
}
