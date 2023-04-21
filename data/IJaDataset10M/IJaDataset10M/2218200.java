package se.sics.contiki.collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class MoteProgrammerProcess {

    public static final String BSL_WINDOWS = "./tools/msp430-bsl-windows.exe";

    public static final String BSL_LINUX = "./tools/msp430-bsl-linux";

    private final String moteID;

    private final String firmwareFile;

    private final String[][] commandSet;

    private int retry = 3;

    private Process currentProcess;

    private Thread commandThread;

    private boolean isRunning;

    private boolean hasError;

    public MoteProgrammerProcess(String moteID, String firmwareFile) {
        this.moteID = moteID;
        this.firmwareFile = firmwareFile;
        String osName = System.getProperty("os.name").toLowerCase();
        String bslCommand;
        if (osName.startsWith("win")) {
            bslCommand = BSL_WINDOWS;
        } else {
            bslCommand = BSL_LINUX;
        }
        commandSet = new String[][] { { bslCommand, "--telosb", "-c", moteID, "-e" }, { bslCommand, "--telosb", "-c", moteID, "-I", "-p", firmwareFile }, { bslCommand, "--telosb", "-c", moteID, "-r" } };
    }

    public String getMoteID() {
        return moteID;
    }

    public String getFirmwareFile() {
        return firmwareFile;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean hasError() {
        return hasError;
    }

    public void start() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        commandThread = new Thread(new Runnable() {

            public void run() {
                try {
                    int count = 0;
                    do {
                        if (count > 0) {
                            logLine("An error occurred. Retrying.", true, null);
                        }
                        count++;
                        hasError = false;
                        for (int j = 0, m = commandSet.length; j < m && isRunning && !hasError; j++) {
                            runCommand(commandSet[j]);
                            Thread.sleep(2000);
                        }
                    } while (isRunning && hasError && count < retry);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isRunning = false;
                    processEnded();
                }
            }
        });
        commandThread.start();
    }

    public void stop() {
        isRunning = false;
        Process process = currentProcess;
        if (process != null) {
            process.destroy();
        }
    }

    public void waitForProcess() throws InterruptedException {
        if (isRunning && commandThread != null) {
            commandThread.join();
        }
    }

    protected void processEnded() {
    }

    private void runCommand(String[] cmd) throws IOException, InterruptedException {
        if (currentProcess != null) {
            currentProcess.destroy();
        }
        currentProcess = Runtime.getRuntime().exec(cmd);
        final BufferedReader input = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
        final BufferedReader err = new BufferedReader(new InputStreamReader(currentProcess.getErrorStream()));
        Thread readInput = new Thread(new Runnable() {

            public void run() {
                String line;
                try {
                    while ((line = input.readLine()) != null) {
                        handleLine(line, false);
                    }
                    input.close();
                } catch (IOException e) {
                    logLine("Error reading from command", false, e);
                }
            }
        }, "read stdout thread");
        Thread readError = new Thread(new Runnable() {

            public void run() {
                String line;
                try {
                    while ((line = err.readLine()) != null) {
                        handleLine(line, true);
                    }
                    err.close();
                } catch (IOException e) {
                    logLine("Error reading from command", true, e);
                }
            }
        }, "read stderr thread");
        readInput.start();
        readError.start();
        readInput.join();
        currentProcess = null;
    }

    private void handleLine(String line, boolean stderr) {
        if (line.toLowerCase().contains("error")) {
            hasError = true;
        }
        logLine(line, stderr, null);
    }

    protected void logLine(String line, boolean stderr, Throwable e) {
        if (stderr) {
            System.err.println("Programmer@" + moteID + "> " + line);
        } else {
            System.out.println("Programmer@" + moteID + "> " + line);
        }
        if (e != null) {
            e.printStackTrace();
        }
    }

    protected String toString(String[] cmd) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = cmd.length; i < n; i++) {
            if (i > 0) sb.append(' ');
            sb.append(cmd[i]);
        }
        return sb.toString();
    }
}
