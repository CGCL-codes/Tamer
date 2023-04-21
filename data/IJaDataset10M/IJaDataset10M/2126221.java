package net.sf.atmodem4j.core.gsm;

/**
 *
 * @author aploese
 */
public class Pdu {

    /**
     * @return the compressed
     */
    public boolean isCompressed() {
        return compressed;
    }

    /**
     * @param compressed the compressed to set
     */
    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    /**
     * @return the encoding
     */
    public Encoding getEncoding() {
        return data.getEncoding();
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(Encoding encoding) {
        switch(encoding) {
            case TEXT_7BIT:
                data = new UserData.Text7Bit();
                break;
            case DATA_8BIT:
                data = new UserData.Data8Bit();
                break;
            case TEXT_16BIT:
                data = new UserData.Text16Bit();
                break;
        }
    }

    /**
     * @return the smscAddress
     */
    public String getSmscAddress() {
        return smscAddress;
    }

    /**
     * @param smscAddress the smscAddress to set
     */
    public void setSmscAddress(String smscAddress) {
        this.smscAddress = smscAddress;
    }

    /**
     * @return the partIndex
     */
    public short getPartIndex() {
        return partIndex;
    }

    /**
     * @param partIndex the partIndex to set
     */
    public void setPartIndex(short partIndex) {
        this.partIndex = partIndex;
    }

    /**
     * @return the partCount
     */
    public short getPartCount() {
        return partCount;
    }

    /**
     * @param partCount the partCount to set
     */
    public void setPartCount(short partCount) {
        this.partCount = partCount;
    }

    /**
     * @return the msgId
     */
    public int getMsgId() {
        return msgId;
    }

    /**
     * @param msgId the msgId to set
     */
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return data;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(byte[] data, int userDataLength, int userDataHeaderLength) {
        this.data.decodePduData(data, userDataLength, userDataHeaderLength);
    }

    public static enum Encoding {

        TEXT_7BIT, DATA_8BIT, TEXT_16BIT
    }

    ;

    public static class EncodingException extends Exception {

        private static final long serialVersionUID = -8158245714142434697L;

        EncodingException(String message) {
            super(message);
        }
    }

    public static enum TimeToLife {

        HOURS, DAYS, WEEKS
    }

    ;

    private String smscAddress;

    private boolean compressed;

    private short partIndex;

    private short partCount;

    private int msgId;

    private UserData data;

    public String getText() {
        return data.getText();
    }
}
