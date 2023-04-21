package packjacket.tasks;

/**
 * Any tasks that is able to run, and stop
 * @author Amandeep Grewal
 */
public interface Task {

    /**
     * Start the task
     * @throws java.io.IOException if any i/O error occurs
     */
    void go() throws java.io.IOException;

    /**
     * Abruptly stop the task (in case of user cancelling or crashing)
     */
    void stop();
}
