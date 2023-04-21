package org.gudy.azureus2.plugins.logging;

/**
 * A <tt>LogAlert</tt> represents a message that should be displayed to the
 * user.
 *
 * <p><b>Note:</b> Only for implementation by Azureus, not plugins.</p>
 * 
 * @since 3.1.1.1
 */
public interface LogAlert {

    /**
	 * Log Type: Information 
	 */
    public int LT_INFORMATION = 1;

    /**
	 * Log Type: Warning
	 */
    public int LT_WARNING = 2;

    /**
	 * Log Type: Error
	 */
    public int LT_ERROR = 3;

    /**
	 * How long should to display the alert for - this will be the value that was given
	 * when creating the alert.
	 * 
	 * @return How long to display the timeout for - <tt>-1</tt> for no explicit value,
	 * <tt>0</tt> to display it indefinitely, otherwise it is the number of seconds to
	 * display the value for. 
	 */
    public int getGivenTimeoutSecs();

    /**
	 * How long should to display the alert for - this will be either the explicit value
	 * given when creating the alert, or a value determined from Azureus's behaviour and
	 * its configuration settings. 
	 * 
	 * @return How long to display the timeout for - <tt>0</tt> to display it indefinitely,
	 * otherwise it is the number of seconds to display the value for. 
	 */
    public int getTimeoutSecs();

    /**
	 * Returns the text of the message - this may include formatting tags (for example,
	 * hyperlinks).
	 * 
	 * @return The text to display.
	 */
    public String getText();

    /**
	 * Returns the text of the message - this will have any formatting stripped out.
	 * 
	 * @return The text to display. 
	 */
    public String getPlainText();

    /**
	 * Returns the error associated with the alert - <tt>null</tt> if there is no error.
	 */
    public Throwable getError();

    /**
	 * Returns the log type of the alert - the value of which will be one of the <tt>LT_</tt>
	 * constants defined above. 
	 */
    public int getType();

    /**
	 * Returns the objects associated with the alert - this will return <tt>null</tt> if
	 * the alert isn't associated with such any objects.
	 */
    public Object[] getContext();
}
