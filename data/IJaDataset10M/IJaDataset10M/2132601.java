package org.marre.sms.nokia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsPortAddressedMessage;
import org.marre.sms.SmsUserData;

/**
 * Baseclass for Nokia Multipart Messages
 * <p>
 * Baseclass for messages that rely on the Nokia Multipart Messages
 * 
 * @author Markus Eriksson
 * @version $Id: NokiaMultipartMessage.java 410 2006-03-13 19:48:31Z c95men $
 */
abstract class NokiaMultipartMessage extends SmsPortAddressedMessage {

    private List parts_ = new LinkedList();

    /**
     * Creates a Nokia Multipart Message
     */
    protected NokiaMultipartMessage() {
        super(SmsConstants.PORT_NOKIA_MULTIPART_MESSAGE, 0);
    }

    /**
     * Adds a part to this multipart message
     * 
     * @param theItemType
     *            Type
     * @param data
     *            Content
     */
    protected void addMultipart(byte theItemType, byte[] data) {
        parts_.add(new NokiaPart(theItemType, data));
    }

    /**
     * Removes all parts from the message
     */
    protected void clear() {
        parts_.clear();
    }

    public SmsUserData getUserData() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(140);
        try {
            baos.write(0x30);
            for (Iterator i = parts_.iterator(); i.hasNext(); ) {
                NokiaPart part = (NokiaPart) i.next();
                byte[] data = part.getData();
                baos.write(part.getItemType());
                baos.write((byte) ((data.length >> 8) & 0xff));
                baos.write((byte) (data.length & 0xff));
                baos.write(data);
            }
            baos.close();
        } catch (IOException ex) {
        }
        return new SmsUserData(baos.toByteArray());
    }
}
