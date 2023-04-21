package org.demis.kobold;

/**
 * @version 1.0
 * @author <a href="mailto:demis27@demis27.net">Stéphane kermabon</a>
 */
public class MessageImpl implements Message {

    private int id = 0;

    private byte[] binaryContent = null;

    private long receivedTime = 0;

    public MessageImpl() {
    }

    public byte[] getBinaryContent() {
        return binaryContent;
    }

    public void setBinaryContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }
}
