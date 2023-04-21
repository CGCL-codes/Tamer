package gov.sns.tools.scan;

import javax.swing.*;
import java.awt.*;

public class ScanStopper {

    private static ScanStopper inst = null;

    private boolean isRunning = false;

    private Object lockObj = new Object();

    private Component owner = null;

    private ScanStopper() {
    }

    public static ScanStopper getScanStopper() {
        if (inst == null) {
            inst = new ScanStopper();
        }
        return inst;
    }

    public void setOwner(Component ownerIn) {
        owner = ownerIn;
    }

    public void stop() {
        synchronized (lockObj) {
            isRunning = false;
        }
    }

    public void start() {
        synchronized (lockObj) {
            isRunning = true;
        }
    }

    public boolean isRunning() {
        synchronized (lockObj) {
            return isRunning;
        }
    }

    public void stop(String msg) {
        synchronized (lockObj) {
            isRunning = false;
        }
        if (msg != "") {
            String title = "ScanStopper MESSAGE";
            JOptionPane.showMessageDialog(owner, msg, title, JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void warning(String msg) {
        if (msg != "") {
            String title = "ScanStopper MESSAGE";
            JOptionPane.showMessageDialog(owner, msg, title, JOptionPane.WARNING_MESSAGE);
        }
    }
}
