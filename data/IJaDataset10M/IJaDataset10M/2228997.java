package com.velocityme.client.gui.calendar;

/**
 *
 * @author  fouche
 */
public class CalendarTimer extends Thread {

    /** Creates a new instance of DefaultTimer */
    public CalendarTimer(CalendarTimerListener l, int b) {
        interval = 500;
        target = l;
        button = b;
    }

    public void run() {
        try {
            while (!interrupted()) {
                sleep(interval);
                target.timeElapsed(this);
                if (button == CalendarFrame.YEAR_MINUS || button == CalendarFrame.YEAR_PLUS) {
                    if (counter < 20) counter++;
                    if (counter == 3) {
                        interval = 200;
                    } else if (counter == 10) {
                        interval = 75;
                    } else if (counter == 20) {
                        interval = 40;
                    }
                }
            }
        } catch (InterruptedException ie) {
        }
    }

    private CalendarTimerListener target;

    private int interval, counter, button;
}
