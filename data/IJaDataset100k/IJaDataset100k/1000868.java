package fr.esrf.tangoatk.widget.util;

import java.awt.*;
import javax.swing.*;

public class JSmoothDelayDialog extends JDialog {

    private static final int REFRESH_DELAY = 100;

    private JSmoothProgressBar jp = null;

    private JSmoothDelayDialog selfref = null;

    private int delay = 0;

    /** Constructor takes JDialog parent, title */
    public JSmoothDelayDialog(JDialog owner, String title) {
        super(owner, title, true);
        init_component(0);
        selfref = this;
    }

    /** Constructor takes JDialog parent, title, delay in milli seconds arguments */
    public JSmoothDelayDialog(JDialog owner, String title, int d) {
        super(owner, title, true);
        init_component(d);
        selfref = this;
    }

    /** Constructor takes Frame parent, title, delay in milli seconds arguments */
    public JSmoothDelayDialog(Frame owner, String title, int d) {
        super(owner, title, true);
        init_component(d);
        selfref = this;
    }

    private void init_component(int d) {
        delay = d;
        jp = new JSmoothProgressBar();
        jp.setValue(0);
        jp.setMaximum(delay);
        jp.setStringPainted(true);
        this.getContentPane().add(jp);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int d) {
        if (d > 0) {
            delay = d;
            jp.setValue(0);
            jp.setMaximum(delay);
        }
    }

    public void start() {
        new Thread() {

            public void run() {
                try {
                    for (int i = 0; i < delay / REFRESH_DELAY; i++) {
                        Thread.sleep(REFRESH_DELAY);
                        jp.setValue(i * REFRESH_DELAY);
                        selfref.repaint();
                    }
                    selfref.dispose();
                } catch (InterruptedException i) {
                }
            }
        }.start();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        JSmoothDelayDialog JD = new JSmoothDelayDialog((JFrame) null, "Attente", 10000);
        JD.setSize(200, 50);
        JD.start();
    }
}
