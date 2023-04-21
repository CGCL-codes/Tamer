package avinteraction.backend;

/**
 * Interface of backends usable with AVInteraction
 */
public interface BackendInterface {

    /**
	 * Submits the answer to interaction "questionID" to the
	 * backend and awaits a boolean value which signifies
	 * whether or not to display the solution in the
	 * interaction window. A backend connecting to a server for
	 * grading reasons for example normally would not display
	 * the answer, a practice-only backend on the other hand
	 * would normally want to give the student  a feedback
	 * whether he was right or wrong.
	 *
	 * @param questionID Contains the ID of the answered
	 * 		  question.
	 * @param correct Contains whether the question was answered
	 * 		  correctly or not.
	 * @param points Number of points one gets for answering the
	 * 		  question right.
	 * @param achieved Number of points user achieved.
	 *
	 * @return A boolean value which determines whether the
	 * 		   question window should display the answer of the
	 * 		   question.
	 */
    public boolean submitAnswer(String questionID, boolean correct, int points, int achieved, Integer[] conceptIdentifier);

    /**
	 * Tells the interaction module whether to re-enable the
	 * submit button or not.
	 *
	 * @return A boolean value which determines whether the
	 * 		   submit button should be re-enabled or not.
	 */
    public boolean enableSubmit();

    /**
	 * Returns a string representation of the backend object,
	 * most commonly this would be the name of the backend.
	 *
	 * @return A String identifying the backend object.
	 */
    public String toString();
}
