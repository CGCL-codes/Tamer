package net.sf.bluex.components.monitors;

import net.sf.bluex.controller.FileModule;
import net.sf.bluex.threads.InterruptableThread;
import java.io.File;

/**
 *
 * @author Blue
 */
public class InstanceMonitor implements Runnable {

    private InterruptableThread it;

    public static void startMonitoring() {
        InstanceMonitor tvm = new InstanceMonitor();
        tvm.it = InterruptableThread.getMyThread(tvm, "Instance Monitor");
        tvm.it.start();
    }

    public void run() {
        File newInst = new File(FileModule.CONFIG_FOLDER, FileModule.NEW_INSTANCE_FILE);
        while (true) {
            if (newInst.exists()) {
                newInst.delete();
                new net.sf.bluex.boundary.BaseWindow(null);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
            }
        }
    }
}
