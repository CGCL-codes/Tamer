package ch.photoindex.servlet.actions.result;

/**
 * The result of an action.
 * 
 * @author Lukas Blunschi
 * 
 */
public interface Result {

    /**
	 * @return true if action was successfully executed, false otherwise.
	 */
    boolean wasSuccessful();

    /**
	 * @return string to append to reload URL if successful, null otherwise.
	 */
    String getReloadSuffix();

    /**
	 * @return null if successful, error message otherwise.
	 */
    String getErrorMessage();
}
