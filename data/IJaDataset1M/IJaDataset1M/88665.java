package de.bender_dv.as400.datastructure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * only usefull for internal use of AppServer4RPG
 * @author Dieter Bender
 * @version $Revision: 1.5 $ $Date: 2010/07/22 16:15:36 $
 */
public class RequestMessage extends DataStructure {

    private static final Log log = LogFactory.getLog(RequestMessage.class);

    private byte[] data;

    private String text;

    private String responseqName;

    private String eventName;

    public RequestMessage() {
        super(500);
    }

    public RequestMessage(byte[] data) {
        this();
        this.data = data;
        log.debug("created " + data);
    }

    public RequestMessage(byte[] data, String text) {
        this();
        this.data = data;
        this.text = text;
        this.responseqName = text.substring(0, 10);
        this.eventName = text.substring(10, 20);
        log.debug("created " + text);
        log.debug("responseQ " + responseqName);
        log.debug("event " + eventName);
    }

    public String getResponseqName() {
        return this.responseqName;
    }

    public String getEventName() {
        return this.eventName;
    }

    public byte[] getEventData() {
        int l = data.length - 20;
        byte[] t = new byte[l];
        System.arraycopy(data, 20, t, 0, l);
        log.debug("eventData " + t);
        return t;
    }
}
