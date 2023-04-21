package de.saly.javacommonslib.base.os;

public interface ProcessHandler {

    public void onErrorOutput(String output, Process process);

    public void onStandardOutput(String output, Process process);

    public void onException(Exception ex, Process process);

    public void onProcessExit(Process process);

    public static ProcessHandler DEFAULT_HANDLER = new DefaultProcessHandler();

    public static class DefaultProcessHandler implements ProcessHandler {

        public void onErrorOutput(String output, Process process) {
            System.err.println(output);
        }

        public void onException(Exception ex, Process process) {
            ex.printStackTrace();
        }

        public void onProcessExit(Process process) {
            System.out.println("Process exit with exit value " + process.exitValue());
        }

        public void onStandardOutput(String output, Process process) {
            System.out.println(output);
        }
    }
}
