package monopd;

import java.io.File;
import java.io.IOException;

public class Monopd implements Runnable {

    private ProcessBuilder builder;

    private volatile Process process;

    private final String[] startCommand = { "cmd", "/c", "start", "monopd.exe" };

    private final String[] killCommand = { "TASKKILL", "/IM", "monopd.exe" };

    private final File monopdDirectory = new File("monopd_windows");

    private volatile Thread t;

    private final Object lock = new Object();

    public static volatile boolean startUpFlag = false;

    public Monopd() {
        builder = new ProcessBuilder(startCommand);
        builder.directory(monopdDirectory);
    }

    public void start() throws Exception {
        t = new Thread(this);
        t.start();
    }

    public void stop() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void interrupt() {
        t.interrupt();
    }

    public void run() {
        synchronized (lock) {
            try {
                process = builder.start();
                try {
                    Monopd.startUpFlag = true;
                    lock.wait();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } catch (Exception e) {
                Monopd.startUpFlag = false;
                process.destroy();
                process = null;
                e.printStackTrace();
            }
            Monopd.startUpFlag = false;
            try {
                Runtime.getRuntime().exec(killCommand);
                process.destroy();
                process = null;
            } catch (Exception e) {
                System.out.print("kill command failed\n");
                e.printStackTrace();
            }
        }
    }

    /**
	 * @return the process
	 */
    public Process getProcess() {
        return process;
    }
}
