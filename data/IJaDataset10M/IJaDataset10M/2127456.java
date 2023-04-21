package org.jazzteam.testEngine.db;

/**
 * Interface realize input/output data
 * 
 * @author Noxt
 * @version $Rev: $
 */
public interface IDB {

    String readInputValueOfTest(final int taskId, final int testId);

    String readCorrectOutputValueOfTest(final int taskId, final int testId);

    int getCountOfTests(final int taskId);

    int loadCheckResult(final int solutionId);

    String readTitleOfTask(final int taskId);

    String readTextOfTask(final int taskId);

    String readAuthorOfTask(final int taskId);

    int readTimeLimitOfTask(final int taskId);

    int getCountOfTasks();
}
