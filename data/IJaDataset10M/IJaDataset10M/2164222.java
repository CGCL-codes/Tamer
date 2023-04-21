package org.marre.wap.push;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.marre.mime.MimeBodyPart;
import org.marre.sms.SmsUserData;
import org.marre.util.StringUtil;
import org.marre.wap.mms.MmsConstants;
import org.marre.wap.mms.MmsHeaderEncoder;

/**
 * Simple MMS notification message sent over Sms.
 * 
 * @version $Id: SmsMmsNotificationMessage.java 410 2006-03-13 19:48:31Z c95men $
 */
public class SmsMmsNotificationMessage extends SmsWapPushMessage {

    private static final int DEFAULT_TRANSACTION_ID_LENGTH = 5;

    private static final long DEFAULT_EXPIRY = 3 * 24 * 60 * 60;

    protected String transactionId_;

    protected String from_;

    protected String subject_;

    protected int messageClassId_ = MmsConstants.X_MMS_MESSAGE_CLASS_ID_PERSONAL;

    protected long size_;

    protected long expiry_;

    protected String contentLocation_;

    public SmsMmsNotificationMessage(String contentLocation, long size) {
        super();
        contentLocation_ = contentLocation;
        transactionId_ = StringUtil.randString(DEFAULT_TRANSACTION_ID_LENGTH);
        expiry_ = DEFAULT_EXPIRY;
        size_ = size;
    }

    protected void writeNotificationTo(OutputStream os) throws IOException {
        MmsHeaderEncoder.writeHeaderXMmsMessageType(os, MmsConstants.X_MMS_MESSAGE_TYPE_ID_M_NOTIFICATION_IND);
        MmsHeaderEncoder.writeHeaderXMmsTransactionId(os, transactionId_);
        MmsHeaderEncoder.writeHeaderXMmsMmsVersion(os, MmsConstants.X_MMS_MMS_VERSION_ID_1_0);
        if ((from_ != null) && (from_.length() > 0)) {
            MmsHeaderEncoder.writeHeaderFrom(os, from_);
        }
        if ((subject_ != null) && (subject_.length() > 0)) {
            MmsHeaderEncoder.writeHeaderSubject(os, subject_);
        }
        MmsHeaderEncoder.writeHeaderXMmsMessageClass(os, messageClassId_);
        MmsHeaderEncoder.writeHeaderXMmsMessageSize(os, size_);
        MmsHeaderEncoder.writeHeaderXMmsExpiryRelative(os, expiry_);
        MmsHeaderEncoder.writeHeaderContentLocation(os, contentLocation_);
    }

    public void setMessageClass(int messageClassId) {
        messageClassId_ = messageClassId;
    }

    public void setSubject(String subject) {
        subject_ = subject;
    }

    public void setExpiry(int i) {
        expiry_ = i;
    }

    public void setFrom(String string) {
        from_ = string;
    }

    public void setTransactionId(String transactionId) {
        transactionId_ = transactionId;
    }

    public SmsUserData getUserData() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        try {
            writeNotificationTo(baos);
            baos.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        pushMsg_ = new MimeBodyPart(baos.toByteArray(), "application/vnd.wap.mms-message");
        setXWapApplicationId("x-wap-application:mms.ua");
        return super.getUserData();
    }
}
