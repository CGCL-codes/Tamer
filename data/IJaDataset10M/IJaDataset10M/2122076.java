package fr.uha.iupmiage.lohrengel;

import java.io.IOException;

/**
 *
 * @author Julien De Matteis
 */
public class ScanPS implements Runnable {

    private String ip;

    private int exitValue = -99;

    private Mutex mutex;

    private String sessionID;

    /** Creates a new instance of ScanPS */
    public ScanPS(String ip, Mutex mutex, String sessionID) {
        this.ip = ip;
        this.mutex = mutex;
        this.sessionID = sessionID;
    }

    public void run() {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("nmap " + ip + " -oX output_" + sessionID + ".xml -no-stylesheet");
            exitValue = p.waitFor();
            synchronized (mutex) {
                System.out.println("Thread 2ndr notifie mutex");
                mutex.setIsRunning(false);
                mutex.notify();
            }
        } catch (InterruptedException ex) {
            p.destroy();
            exitValue = -98;
        } catch (IOException ex) {
            p.destroy();
            ex.printStackTrace();
        }
    }

    public int getExitValue() {
        return exitValue;
    }
}
