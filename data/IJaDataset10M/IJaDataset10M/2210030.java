package mn.more.wits.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mn.more.wits.client.dto.ExamAnswerDTO;
import mn.more.wits.client.dto.ExamListDTO;
import mn.more.wits.client.dto.ExamQuestionDTO;
import mn.more.wits.client.dto.ExamResultSummaryDTO;
import mn.more.wits.client.dto.ExamSessionDTO;
import mn.more.wits.client.dto.Payload;

public interface ExamSessionManagerAsync {

    /** list all available exams ({@link ExamListDTO}) for the current user. */
    void getAvailableExams(AsyncCallback async);

    /**
	 * this method should be called <b>AT THE START</b> of a "test session", and
	 * <b>BEFORE</b> any other methods.  This method is crucial, in that:
	 * <ul>
	 * <li>it verifies that the "test taker" is authorized to take the test
	 * <li>it verifies that the specified test is available and all the artifacts (questions, responses, database,
	 * etc.) is ready
	 * <li>it requests the system to prepare the question set for a test session
	 * <li>it updates the system to signify the start of a test session (i.e. "the student has entered the test
	 * room, has sat down, and is ready to start the test")
	 * <li>returns a <code>{@link ExamSessionDTO}</code> object which contains summary information of a test session.
	 * </ul>
	 * <p/>
	 * Note that "init" does not mean that the test has started.  The test's stopwatch will start after the return of
	 * the first question ({@link ExamSessionManager#getFirstQuestion()}.  This "init" simply sets up everything
	 * necessary for the specified test session to start.
	 * <p/>
	 * Returns an instance of {@link ExamSessionDTO}, requires a testId ({@link String}).
	 *
	 * @return test session information
	 */
    void init(Payload testId, AsyncCallback async);

    /**
	 * retrieves the first question of a test session.  Consequently, the method will enable and start the stopwatch
	 * for the current test session.
	 * <p/>
	 * <code><b>This method must be called after the {@link ExamSessionManager#init(Payload)}</b> method. </code>
	 *
	 * @return first test question
	 */
    void getFirstQuestion(AsyncCallback async);

    /**
	 * after the test taker has read the question, he/she must submit the answer in order to obtain the next test
	 * question (see below for exceptions).  This method accepts the test taker's answer, persists it, and then sends
	 * the next question back to the test taker's UI.  The {@link ExamAnswerDTO} class contains both information
	 * regarding the test taker's answer as well as the next question to retrieve.  Furthermore, this
	 * {@link ExamAnswerDTO} object contains timestamp information to synchronize the "test clock" with the server-side.
	 * <p/>
	 * There are situation where the answer is not required in order to obtain the next question in line.  Such
	 * variations should be configured by the test creator prior to the start of a test.  Similarly, test creator
	 * may decides the "direction" of the next question that is allowed (i.e. test taker can only go forward to the
	 * next question, or test taker can go forward or backward to other questions).
	 * <p/>
	 * Returns an instance of {@link ExamQuestionDTO}, requires an instance of {@link ExamAnswerDTO}
	 */
    void sendAnswerAndGetQuestion(Payload answer, AsyncCallback async);

    /**
	 * calling this method signifies that a test session is completed and the scoring process can begin.  However,
	 * the actual scoring process is not guaranteed to be synchronized with the calling of this method.
	 * <p/>
	 * Returns an instance of {@link ExamResultSummaryDTO}
	 */
    void complete(AsyncCallback async);

    /**
	 * calling this method signifies that the test taker has decided to forfeit the current test session.  The score
	 * for this test session is automatically set to 0 -- no scoring process will be executed.
	 * <p/>
	 * Returns an instance of {@link ExamResultSummaryDTO}
	 */
    void forfeit(AsyncCallback async);
}
