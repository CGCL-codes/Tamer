package au.gov.naa.digipres.dpr.task.step;

/**
 * @author Justin Waddell
 *
 */
public interface ProcessingErrorHandler {

    /**
	 * Possible actions to be taken if an error has occurred.
	 */
    public enum ProcessingErrorAction {

        RESET, STOP_PROCESSING, SAVE
    }

    /**
	 * Determine the action which should be taken given the errors stored in StepResults.
	 * 
	 * @param stepName
	 * @param results
	 * @return
	 */
    public ProcessingErrorAction determineAction(String stepName, StepResults results);

    /**
	 * Determine if processing the step should continue despite an error occurring.
	 * 
	 * @param stepName
	 * @param results
	 * @return
	 */
    public boolean continueDespiteError(String stepName, String errorMessage);
}
