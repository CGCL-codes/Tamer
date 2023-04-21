package net.edokun.view.helpers;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;

/**
 * This Class helps to add more GUI effects
 * it allows to change the timer color when the time cout
 * is paused.
 *
 * @author Eduardo Vidal
 * @date 15-02-2010
 * @version 1.0
 */
public class LabelTicker {

    private JLabel lblTimer;

    private Timer timer1;

    private boolean stopped;

    private boolean colored;

    public LabelTicker(JLabel label) {
        stopped = true;
        lblTimer = label;
        colored = true;
        timer1 = new Timer();
        Counter counter = new Counter();
        timer1.schedule(counter, 1000, 1000);
    }

    public void start() {
        stopped = false;
    }

    public void stop() {
        stopped = true;
        lblTimer.setForeground(Color.BLACK);
    }

    private class Counter extends TimerTask {

        public void run() {
            if (!stopped) {
                if (colored) {
                    lblTimer.setForeground(Color.GRAY);
                    colored = false;
                } else {
                    lblTimer.setForeground(Color.BLACK);
                    colored = true;
                }
            }
        }
    }
}
