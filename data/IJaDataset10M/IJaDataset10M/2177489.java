package jp.eisbahn.eclipse.plugins.twitterclipse.internal;

import jp.eisbahn.eclipse.plugins.twitterclipse.HttpRequestTimeoutException;
import jp.eisbahn.eclipse.plugins.twitterclipse.TwitterCommunicationException;
import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class TestRunListenerImpl implements ITestRunListener {

    private boolean failed = false;

    public void testEnded(String testId, String testName) {
    }

    public void testFailed(int status, String testId, String testName, String trace) {
        failed = true;
    }

    public void testReran(String testId, String testClass, String testName, int status, String trace) {
    }

    public void testRunEnded(long elapsedTime) {
        try {
            TwitterclipsePlugin plugin = TwitterclipsePlugin.getDefault();
            TwitterclipseConfig config = plugin.getTwitterclipseConfiguration();
            if (failed && config.isUpdateStatusAtTestFailed()) {
                plugin.updateStatus(config.getStatusAtTestFailed());
            } else if (config.isUpdateStatusAtTestSucceed()) {
                plugin.updateStatus(config.getStatusAtTestSucceed());
            }
        } catch (TwitterCommunicationException e) {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            MessageDialog.openError(shell, "Twitterclipse", e.getMessage());
        } catch (HttpRequestTimeoutException e) {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            MessageDialog.openError(shell, "Twitterclipse", e.getMessage());
        }
    }

    public void testRunStarted(int testCount) {
        failed = false;
    }

    public void testRunStopped(long elapsedTime) {
    }

    public void testRunTerminated() {
    }

    public void testStarted(String testId, String testName) {
    }
}
