package org.marre.sms.nokia;

import java.io.*;
import org.marre.sms.*;
import org.marre.sms.util.*;
import org.marre.wap.*;
import org.marre.wap.util.*;
import org.marre.mime.*;

/**
 *  Nokia OTA Settings Message (based on 7.0 spec)
 * 
 * @author Fabio Corneti
 * @version
 */
public class NokiaOtaSettingsMessage extends SmsConcatMessage {

    private byte[] myOtaSettingsMsg;

    protected NokiaOtaSettingsMessage() {
        super(SmsConstants.DCS_DEFAULT_8BIT);
    }

    /** 
     * Creates a Generic Nokia OTA Settings message 
     *
     * @param theOtaBrowserSettingsMsg byte array containing the OTA message (WSP
     * Encoded)
     * 
     * @param theDestPort integer containing the destination WDP port
     * @param theOrigPort integer containing the origin WDP port
     */
    public NokiaOtaSettingsMessage(byte[] theOtaSettingsMsg, int theDestPort, int theOrigPort) {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        myOtaSettingsMsg = theOtaSettingsMsg;
        setContent(new SmsUdhElement[] { SmsUdhUtil.get16BitApplicationPortUdh(theDestPort, theOrigPort) }, myOtaSettingsMsg, myOtaSettingsMsg.length);
    }

    protected void createMessage(byte[] theOtaSettingsWbxml, MimeContentType theContentType, int theDestPort, int theOrigPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            WspUtil.writeUint8(baos, 0x01);
            WspUtil.writeUint8(baos, WapConstants.PDU_TYPE_PUSH);
            ByteArrayOutputStream headers = new ByteArrayOutputStream();
            WspUtil.writeContentType(headers, theContentType);
            headers.close();
            WspUtil.writeUintvar(baos, headers.size());
            baos.write(headers.toByteArray());
            baos.write(theOtaSettingsWbxml);
            baos.close();
        } catch (IOException ex) {
        }
        myOtaSettingsMsg = baos.toByteArray();
        setContent(new SmsUdhElement[] { SmsUdhUtil.get16BitApplicationPortUdh(theDestPort, theOrigPort) }, myOtaSettingsMsg, myOtaSettingsMsg.length);
    }
}
