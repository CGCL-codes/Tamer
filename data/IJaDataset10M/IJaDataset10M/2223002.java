package org.apache.batik.parser;

/**
 * This interface must be implemented and then registred as the
 * handler of a <code>AngleParser</code> instance in order to be
 * notified of parsing events.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AngleHandler.java,v 1.1 2005/11/21 09:51:38 dev Exp $
 */
public interface AngleHandler {

    /**
     * Invoked when the angle attribute parsing starts.
     * @exception ParseException if an error occured while processing the angle
     */
    void startAngle() throws ParseException;

    /**
     * Invoked when a float value has been parsed.
     * @exception ParseException if an error occured while processing the angle
     */
    void angleValue(float v) throws ParseException;

    /**
     * Invoked when 'deg' has been parsed.
     * @exception ParseException if an error occured while processing the angle
     */
    void deg() throws ParseException;

    /**
     * Invoked when 'grad' has been parsed.
     * @exception ParseException if an error occured while processing the angle
     */
    void grad() throws ParseException;

    /**
     * Invoked when 'rad' has been parsed.
     * @exception ParseException if an error occured while processing the angle
     */
    void rad() throws ParseException;

    /**
     * Invoked when the angle attribute parsing ends.
     * @exception ParseException if an error occured while processing the angle
     */
    void endAngle() throws ParseException;
}
