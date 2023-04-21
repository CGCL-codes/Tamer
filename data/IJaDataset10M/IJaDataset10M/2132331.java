package dopisolver;

import java.io.*;

/**
 * Logs the event to the process' standard console (or error console in case of failure)
 */
public class LogSolverTaskListener extends BaseSolverTaskListener {

    private PrintWriter out;

    private PrintWriter err;

    /** Builds the listener */
    public LogSolverTaskListener() {
        this.out = new PrintWriter(System.out);
        this.err = new PrintWriter(System.err);
    }

    /**@see dopisolver.BaseSolverTaskListener#jobCreated(java.lang.String)*/
    @Override
    protected synchronized void jobCreated(String taskName) {
        out.println("New job: " + taskName);
        out.flush();
    }

    /**@see dopisolver.BaseSolverTaskListener#jobDone(int, java.lang.String, dopisolver.SolverInput, dopisolver.SolverResult)*/
    @Override
    protected void jobDone(int instance, String taskName, SolverInput input, SolverResult result) {
        out.println(taskName + " for n=" + input.n() + ", k=" + input.k() + "(in " + result.getTime() + " sec):\t\t" + result.getValue());
        out.flush();
    }

    /**@see dopisolver.BaseSolverTaskListener#jobFailed(int, java.lang.String, dopisolver.SolverInput, java.lang.Throwable)*/
    @Override
    protected void jobFailed(int instance, String taskName, SolverInput input, Throwable th) {
        err.println("Failed " + taskName + " for n=" + input.n() + ", k=" + input.k() + ": ");
        th.printStackTrace(err);
        err.flush();
    }
}
