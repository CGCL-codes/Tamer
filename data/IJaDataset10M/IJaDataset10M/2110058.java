package de.iritgo.openmetix.core.gui.swing;

import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

/**
 * A text field that displays the current time.
 *
 * @version $Id: IClockTextField.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IClockTextField extends JTextField implements ActionListener {

    /** The current time. */
    private Date currentTime;

    /** Timer. */
    private Timer timer;

    /** Formatter. */
    private DateFormat dateFormat;

    /** The format to use. */
    private int format;

    /** Wether to display the time. */
    private boolean showTime;

    /** Wether to display the date. */
    private boolean showDate;

    /**
	 * Create a new IClockTextField.
	 */
    public IClockTextField() {
        setHorizontalAlignment(JTextField.CENTER);
        currentTime = new Date();
        format = DateFormat.MEDIUM;
        showTime = true;
        showDate = false;
        actionPerformed(null);
        timer = new Timer(1000, this);
    }

    /**
	 * Start the clock.
	 */
    public void start() {
        timer.start();
    }

    /**
	 * Stop the clock.
	 */
    public void stop() {
        timer.stop();
    }

    /**
	 * Set the format.
	 *
	 * @param format See <code>DateFormat</code> for details.
	 */
    public void setFormat(int format) {
        this.format = format;
    }

    /**
	 * Determine wether to display the time.
	 *
	 * @param showTime If true the time is displayed.
	 */
    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    /**
	 * Determine wether to display the date.
	 *
	 * @param showDate If true the date is displayed.
	 */
    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    /**
	 * Called every second.
	 *
	 * @param e The action event.
	 */
    public void actionPerformed(ActionEvent e) {
        currentTime.setTime(System.currentTimeMillis());
        setText(getFormatter().format(currentTime));
    }

    /**
	 * Get the date/time formatter.
	 *
	 * @return The formatter.
	 */
    private DateFormat getFormatter() {
        if (dateFormat == null) {
            if (showTime && showDate) {
                dateFormat = DateFormat.getDateTimeInstance(format, format);
            } else if (showDate) {
                dateFormat = DateFormat.getDateInstance(format);
            } else {
                dateFormat = DateFormat.getTimeInstance(format);
            }
        }
        return dateFormat;
    }
}
