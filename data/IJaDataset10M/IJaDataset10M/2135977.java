package edu.cp.ical;

/**
 * iCalendar Attribute class. Can be used to store an attribute such as LANGUAGE
 * as in: <br/> <tt>LOCATION;LANGUAGE=en:Germany</tt>
 * @version $Id: Attribute.java,v 1.3 2007/04/08 01:10:23 cknudsen Exp $
 * @author Craig Knudsen, craig@k5n.us
 */
public class Attribute {

    /** Attribute name (always uppercase) */
    public String name;

    /** Attribute value */
    public String value;

    /**
    * TODO
    * @param name
    * @param value
    */
    public Attribute(String name, String value) {
        this.name = name.toUpperCase();
        this.value = value.trim();
    }
}
