package dioscuri.exception;

/**
 * @author Bram Lohman
 * @author Bart Kiers
 */
@SuppressWarnings("serial")
public class CPUInstructionException extends Exception {

    /**
     * @param message
     */
    public CPUInstructionException(String message) {
        super(message);
    }
}
