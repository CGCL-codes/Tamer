package exceptons;

/**
 * Wird f�r <code>JLabelTextField</code> verwendet, um bei Validierung eine
 * Fehlermeldung zur�ck zu geben.
 * 
 * @author christian antic <e0525482[at]student.tuwien.ac.at>
 */
@SuppressWarnings("serial")
public class InputValidationException extends Exception {

    /**
	 * 
	 * @param msg
	 */
    public InputValidationException(String msg) {
        super(msg);
    }
}
