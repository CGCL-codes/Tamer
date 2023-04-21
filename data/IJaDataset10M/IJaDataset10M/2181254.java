package frost.messaging.freetalk.boards;

@SuppressWarnings("serial")
public class FreetalkBoard extends AbstractFreetalkNode {

    private int messageCount;

    private long firstSeenDate;

    private long latestMessageDate;

    public FreetalkBoard(final String name) {
        super(name);
        this.messageCount = 0;
        this.firstSeenDate = 0L;
        this.latestMessageDate = 0L;
    }

    public FreetalkBoard(final String name, final int messageCount, final long firstSeenDate, final long lastMessageDate) {
        super(name);
        this.messageCount = messageCount;
        this.firstSeenDate = firstSeenDate;
        this.latestMessageDate = lastMessageDate;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(final int messageCount) {
        this.messageCount = messageCount;
    }

    public long getFirstSeenDate() {
        return firstSeenDate;
    }

    public void setFirstSeenDate(final long firstSeenDate) {
        this.firstSeenDate = firstSeenDate;
    }

    public long getLastMessageDate() {
        return latestMessageDate;
    }

    public void setLastMessageDate(final long lastMessageDate) {
        this.latestMessageDate = lastMessageDate;
    }

    @Override
    public boolean isBoard() {
        return true;
    }
}
