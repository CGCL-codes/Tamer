package gui2d;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

/**
 * Timer Object to store the time spent playing.
 *
 * @author		Christophe Hertigers
 * @version     Thursday, October 17 2002, 23:42:30
 */
public class DCTimer {

    /**
	 * Inner class. Handles the ActionEvents of the timer.
	 *
	 */
    class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            s++;
            timerLabel.setText(getTime());
        }
    }

    private static final int ONE_SECOND = 1000;

    private long s;

    private Timer time;

    private JLabel timerLabel;

    /**
	 * Class Constructor. Initializes the time to 0.
	 *
	 */
    public DCTimer(JLabel myTimerLabel) {
        this(0, myTimerLabel);
    }

    /**
	 * Class Constructor. Initializes the time to the number of seconds given.
	 *
	 * @param sInt		the number of seconds the timer starts with.
	 */
    public DCTimer(long sInt, JLabel myTimerLabel) {
        s = sInt;
        time = new Timer(ONE_SECOND, new ActionHandler());
        timerLabel = myTimerLabel;
    }

    /**
	 * Starts the timer.
	 *
	 */
    public void start() {
        time.start();
    }

    /**
	 * Stops the timer.
	 *
	 */
    public void stop() {
        time.stop();
    }

    /**
	 * Gets the time.
	 *
	 * @returns		a String containing the time (HH:MM:SS)
	 */
    public String getTime() {
        return ((s / 3600) + ":" + ((s / 60) % 60) + ":" + (s % 60));
    }
}
