package core;

/**
 * QueueContainer
 * Container in dem alle Nachrichten verschickt werden
 * @author Mattes Besuden, Patrik Kluge, Nadine Pollmann, Tobias Pude, Thomas Rix, Tobias Teichmann
 */
public class QueueContainer {

    private int messageID;

    private Integer senderID;

    private IQueueData data;

    public QueueContainer(int messageID, IQueueData data) {
        this.messageID = messageID;
        this.data = data;
    }

    public QueueContainer(int messageID, IQueueData data, Integer senderID) {
        this.messageID = messageID;
        this.data = data;
        this.senderID = senderID;
    }

    public int getMessageID() {
        return messageID;
    }

    public IQueueData getData() {
        return data;
    }

    public Integer getSenderID() {
        return senderID;
    }
}
