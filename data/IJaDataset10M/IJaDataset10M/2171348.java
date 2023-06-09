package jmri.jmrix.grapevine;

/**
 * Contains the data payload of a serial reply
 * packet.  Note that its _only_ the payload.
 *
 * @author	Bob Jacobsen  Copyright (C) 2002, 2006, 2007, 2008
 * @version     $Revision: 1.8 $
 */
public class SerialReply extends jmri.jmrix.AbstractMRReply {

    public SerialReply() {
        super();
        setBinary(true);
    }

    public SerialReply(String s) {
        super(s);
        setBinary(true);
    }

    public SerialReply(SerialReply l) {
        super(l);
        setBinary(true);
    }

    /**
     * Is reply to poll message
     */
    public int getAddr() {
        return getElement(0) & 0x7F;
    }

    public boolean isUnsolicited() {
        return true;
    }

    protected int skipPrefix(int index) {
        return index;
    }

    public int getBank() {
        return ((getElement(3) & 0x70) >> 4);
    }

    public boolean isError() {
        return (getElement(0) & 0x7F) == 0;
    }

    public boolean isFromParallelSensor() {
        if ((getElement(3) & 0x70) != 0x50) return false;
        if ((getElement(1) & 0x20) != 0x00) return false;
        return true;
    }

    public boolean isFromOldSerialSensor() {
        if ((getElement(3) & 0x70) != 0x50) return false;
        if ((getElement(1) & 0x20) != 0x20) return false;
        return true;
    }

    public boolean isFromNewSerialSensor() {
        if ((getElement(3) & 0x70) != 0x40) return false;
        return true;
    }

    public void setNumDataElements(int len) {
        if (len > _nDataChars) {
            log.error("Can't shorten reply from " + _nDataChars + " to " + len);
            return;
        }
        _nDataChars = len;
    }

    /**
     * Format the reply as human-readable text.
     * <P>
     * Since Grapevine doesn't distinguish between message 
     * and reply, this uses the Message method.
     */
    @SuppressWarnings("fallthrough")
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "SF_SWITCH_FALLTHROUGH")
    public String format() {
        int b1 = -1;
        int b2 = -1;
        int b3 = -1;
        int b4 = -1;
        switch(getNumDataElements()) {
            case 4:
                b4 = getElement(3) & 0xff;
            case 3:
                b3 = getElement(2) & 0xff;
            case 2:
                b2 = getElement(1) & 0xff;
            case 1:
                b1 = getElement(0) & 0xff;
        }
        return SerialMessage.staticFormat(b1, b2, b3, b4);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SerialReply.class.getName());
}
