package innerbus.logtrap.gui;

import innerbus.logtrap.common.ProjectListXmlParser;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.Vector;

public class DrawTraffice extends Panel implements Runnable {

    private Thread thread;

    private Vector vcCondition = new Vector();

    private boolean isStop;

    private int DELAY_TIME = 1000;

    public void startDraw() {
        try {
            isStop = false;
            thread = new Thread(this);
            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopDraw() {
        try {
            if (thread != null) {
                isStop = true;
                thread.interrupt();
                thread = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setDelayTime(int delay) {
        DELAY_TIME = delay;
    }

    public void run() {
        try {
            while (!isStop || !thread.isInterrupted()) {
                drawTraffic();
                Thread.sleep(DELAY_TIME);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void drawTraffic() {
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0, size = vcCondition.size(); i < size; i++) {
        }
    }

    public void update(Graphics g) {
        paint(g);
    }
}
