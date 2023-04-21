package se.bambalam;

import javax.microedition.lcdui.Display;

public class VibratorRunnable implements Runnable {

    private EggTimer mid;

    private boolean vibrating;

    public VibratorRunnable(EggTimer mid) {
        this.mid = mid;
    }

    public void run() {
        this.vibrating = true;
        while (this.vibrating) {
            Display.getDisplay(mid).vibrate(800);
            try {
                Thread.sleep(2400);
            } catch (InterruptedException e) {
                this.vibrating = false;
                e.printStackTrace();
            }
        }
    }

    public void setVibrating(boolean vibrating) {
        this.vibrating = vibrating;
    }
}
