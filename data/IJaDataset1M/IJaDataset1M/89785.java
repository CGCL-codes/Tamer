package up2p.peer.gnutella;

import com.echomine.common.ParseException;
import com.echomine.gnutella.GnutellaMessage;
import com.echomine.gnutella.GnutellaMessageHeader;
import com.echomine.util.ParseUtil;

/**
 * Base class for UP2P XML messages.
 * 
 * @author Neal Arthorne
 * @version 1.0
 */
public abstract class Up2pTextMsg extends GnutellaMessage {

    protected String message;

    public Up2pTextMsg(GnutellaMessageHeader header) {
        super(header);
    }

    public Up2pTextMsg(String msg, GnutellaMessageHeader header) {
        super(header);
        message = msg;
    }

    public void copy(Up2pTextMsg q) {
        super.copy(q);
        this.message = q.message;
    }

    public int getSize() {
        return super.getSize() + message.length() + 1;
    }

    public int serialize(byte[] outbuf, int offset) throws ParseException {
        offset = super.serialize(outbuf, offset);
        offset = ParseUtil.serializeString(message, outbuf, offset);
        outbuf[offset++] = 0;
        return offset;
    }

    public int deserialize(byte[] inbuf, int offset, int length) throws ParseException {
        offset = super.deserialize(inbuf, offset, length);
        StringBuffer buf = new StringBuffer();
        offset = ParseUtil.deserializeString(inbuf, offset, buf);
        if (offset < inbuf.length && inbuf[offset] == 0) offset++;
        message = buf.toString();
        return offset;
    }

    /**
     * Get the message body.
     * 
     * @return message body
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message body.
     * 
     * @param msg message body
     */
    public void setMessage(String msg) {
        message = msg;
    }

    /**
     * Sub-classes must implement the toString method.
     * 
     * @return description of the message
     */
    public abstract String toString();
}
