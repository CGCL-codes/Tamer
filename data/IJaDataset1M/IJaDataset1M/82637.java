package packageException;

/**
 *
 * @author Lionel & Julien
 */
public class CommandeException extends Exception {

    private String e;

    /**
     * 
     * @param e
     */
    public CommandeException(String e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return e;
    }
}
