package breed.model.util;

/**
 * Any exception purely for breed.
 */
public class BreedException extends NullPointerException {

    private static final long serialVersionUID = 1L;

    private String sMsg;

    public BreedException(String sMsg) {
        super(sMsg);
        this.sMsg = sMsg;
    }

    public String toString() {
        return "Breed Exception \"" + sMsg + "\".";
    }
}
