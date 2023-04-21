package us.k5n.ical;

/**
 * Defines an interface for receive parse errors.
 * 
 * @version $Id: ParseErrorListener.java,v 1.3 2007/04/08 01:10:23 cknudsen Exp $
 * @author Craig Knudsen, craig@k5n.us
 */
interface ParseErrorListener {

    /**
	 * This method will be called when a iCalendar parse error is encountered.
	 */
    public void reportParseError(ParseError error);
}
