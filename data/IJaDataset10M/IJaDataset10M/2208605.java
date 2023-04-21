package examples.visual.s02;

import componere.base.VisualBox;

public class LightBulbThread extends Thread {

    private static VisualBox jP1;

    public void run() {
        while (true) {
            try {
                Thread.sleep(300);
                if (!jP1.getLastImageURL().equalsIgnoreCase("../loff.png")) jP1.setWImageLocation("../loff.png", this); else jP1.setWImageLocation("../lon.png", this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPanel(VisualBox param) {
        LightBulbThread.jP1 = param;
    }
}
