package de.imtek.pressurecontrol;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * 
 * @author mada
 * 
 */
public class ShowPressureThreadPCS400 extends Thread {

    private Label label;

    private String status;

    private PressureControlPCS400 pc;

    private Boolean run = true;

    /**
	 * Constructor
	 * @param label
	 * @param pc1
	 */
    public ShowPressureThreadPCS400(Label label, PressureControlPCS400 pc1) {
        this.label = label;
        this.pc = pc1;
    }

    public synchronized void run() {
        while (run) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                status = "Pressure: " + pc.getPressure();
                System.out.println(status);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    label.setText(status);
                }
            });
        }
    }

    /**
	 * stop this thread
	 * @param 
	 */
    public void pleaseStop() {
        run = false;
    }
}
