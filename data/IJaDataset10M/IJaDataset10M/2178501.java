package org.marre.sms.brew;

import org.marre.sms.SmsMessage;
import org.marre.sms.SmsDcs;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsPduUtil;
import org.marre.sms.SmsUserData;

/**
 * Brew directed SMS message (BDSMS).
 * 
 * I haven't tried this myself but someone might find it useful. I'm not even
 * sure if there are any BREW enabled GSM devices.
 * 
 * See these links for more information.
 * http://brewforums.qualcomm.com/showthread.php?t=6543&highlight=brew+directed
 * http://www.simplewire.com/downloads/download.html?DOWNLOAD_ID=1165 
 * 
 * @author Markus
 * @version $Id: SmsBrewDirectedMessage.java 410 2006-03-13 19:48:31Z c95men $
 */
public class SmsBrewDirectedMessage implements SmsMessage {

    private String text_;

    private String classId_;

    /**
     * Creates a BREW directed SMS message.
     * 
     * @param classId ClassId for the receiving app. Example: "0x00000000"
     * @param text Text to send.
     */
    public SmsBrewDirectedMessage(String classId, String text) {
        classId_ = classId;
        text_ = text;
    }

    /**
     * Returns the text message. 
     */
    public String getText() {
        return text_;
    }

    /**
     * Returns the classId. 
     */
    public String getClassId() {
        return classId_;
    }

    public SmsPdu[] getPdus() {
        String bdsmsText = "//BREW:" + classId_ + ":" + text_;
        SmsUserData userData = new SmsUserData(SmsPduUtil.getSeptets(bdsmsText), bdsmsText.length(), SmsDcs.getGeneralDataCodingDcs(SmsDcs.ALPHABET_8BIT, SmsDcs.MSG_CLASS_UNKNOWN));
        return new SmsPdu[] { new SmsPdu(null, userData) };
    }
}
