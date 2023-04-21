package jsystem.framework.report;

import jsystem.runner.ErrorLevel;

/**
 * General execution events.
 * @author goland
 */
public interface ExecutionListener extends ExtendTestListener {

    public void remoteExit();

    public void remotePause();

    public void executionEnded(String scenarioName);

    public void errorOccured(String title, String message, ErrorLevel level);
}
