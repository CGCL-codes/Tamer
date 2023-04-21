package pde.test.utils;

import org.eclipse.jdt.internal.junit.model.ITestRunListener2;
import org.eclipse.jdt.internal.junit.model.RemoteTestRunnerClient;

public final class PDETestResultsCollector {

    private static PDETestListener pdeTestListener;

    private String suiteName;

    private String junit_report_output;

    private PDETestResultsCollector(String suite, String junit_report_output) {
        suiteName = suite;
        this.junit_report_output = junit_report_output;
    }

    private void run(int port) throws InterruptedException {
        pdeTestListener = new PDETestListener(this, suiteName, junit_report_output);
        new RemoteTestRunnerClient().startListening(new ITestRunListener2[] { pdeTestListener }, port);
        System.out.println("Listening on port " + port + " for test suite " + suiteName + " results ...");
        synchronized (this) {
            wait();
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("usage: PDETestResultsCollector <test suite name> <port number> <junit report output>");
            System.exit(0);
        }
        try {
            new PDETestResultsCollector(args[0], args[2]).run(Integer.parseInt(args[1]));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (pdeTestListener != null && pdeTestListener.failed()) {
            System.exit(1);
        }
    }
}
