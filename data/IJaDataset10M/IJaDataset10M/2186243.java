package avinteraction.backend;

/**
 * Generic AVInteraction Backend, does nothing, just sits there
 * to be called
 *
 * @author Gina Haeussge, huge(at)rbg.informatik.tu-darmstadt.de
 */
public class GenericBackend implements BackendInterface {

    /** The Debug level */
    private static boolean DEBUG;

    /** Name of the Backend */
    private final String NAME = "generic";

    /**
	 * Standard constructor, disables debugging
	 */
    public GenericBackend() {
        this(false);
    }

    /**
	 * Sets debugging to tf.
	 *
	 * @param tf The debug level.
	 */
    public GenericBackend(boolean tf) {
        DEBUG = tf;
    }

    /**
	 * Submit the answer. For further details see the
	 * documentation of the BackendInterface class.
	 *
	 * @param questionID DOCUMENT ME!
	 * @param correct DOCUMENT ME!
	 * @param points DOCUMENT ME!
	 * @param achieved DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean submitAnswer(String questionID, boolean correct, int points, int achieved, Integer[] conceptIdentifier) {
        System.out.print("\t\tBackend received answer to \"" + questionID + "\": ... ");
        if (correct) {
            System.out.println("correct.");
        } else {
            System.out.println("wrong.");
        }
        return false;
    }

    /**
	 * Tells the interaction module whether to re-enable the
	 * submit button or not.
	 *
	 * @return A boolean value which determines whether the
	 * 		   submit button should be re-enabled or not.
	 */
    public boolean enableSubmit() {
        return false;
    }

    /**
	 * Retrieves the name of the backend
	 *
	 * @return a String containing the name of the backend
	 */
    public String toString() {
        return NAME;
    }
}
