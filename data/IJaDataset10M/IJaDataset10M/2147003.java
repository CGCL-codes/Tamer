package org.jabber.jabberbeans.Extension;

import java.util.Hashtable;

/**
 * An <code>IQTimeBuilder</code> is used to generate a jabber:iq:time namespace
 * object.
 *
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: mass $
 * @version $Revision: 1.4 $
 */
public class IQTimeBuilder implements ExtensionBuilder {

    /** The time in GMT/UTC, in the ISO format (YYYYMMDDTHH:MM:SS*/
    private String time;

    /** The zone which the remote party is located */
    private String zone;

    /** A textual description of the time which the client is located */
    private String display;

    /** Construct a new <code>IQTimeBuilder</code> object */
    public IQTimeBuilder() {
        reset();
    }

    /** reset this builder to a default state, for reuse */
    public void reset() {
        time = null;
        zone = null;
        display = null;
    }

    /** 
     * get the time in GMT/UTC, in ISO format 
     *
     * @return String holding time
     */
    public String getTime() {
        return time;
    }

    /** set the time 
     *
     * @param value Time in GMT/UTC, in ISO format
     */
    public void setTime(String value) {
        time = value;
    }

    /**
     * get the time zone
     *
     * @return <code>String</code> holding time zone
     */
    public String getZone() {
        return zone;
    }

    /**
     * set the time zone
     *
     * @param value <code>String</code> holding time zone
     */
    public void setZone(String value) {
        zone = value;
    }

    /**
     * get the textual display string
     *
     * @return <code>String</code> holding textual representation of the time
     */
    public String getDisplay() {
        return display;
    }

    /**
     * set the textual display string
     *
     * @param value <code>String</code> holding textual representation of
     *              the time
     */
    public void setDisplay(String value) {
        display = value;
    }

    /**
     * Build an <code>IQTime Extension</code> based on the current builder
     * state.
     *
     * @return <code>IQTime Extension</code> based on the current builder
     * state.
     */
    public Extension build() {
        return new IQTime(this);
    }
}
