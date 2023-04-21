package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Container for SeqNr associated with some extra properties
 */
public class TimeStampCell implements IColoredTextCell, IColoredLongCell {

    private long timeStamp;

    private Color color;

    public TimeStampCell() {
        this.timeStamp = 0;
        this.color = Color.white;
    }

    /**
	 * Returns the color.
	 * @return Color
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * Returns the timeStamp.
	 * @return int
	 */
    public long getValue() {
        return timeStamp;
    }

    /**
	 * Sets the color.
	 * @param color The color to set
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * Sets the timeStamp.
	 * @param timeStamp The timeStamp to set
	 */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toString() {
        return (new Long(timeStamp)).toString();
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.gui.IColoredTextCell#getText()
     */
    public String getText() {
        Date timeStampDate = new Date(timeStamp);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.UK);
        return df.format(timeStampDate);
    }
}
