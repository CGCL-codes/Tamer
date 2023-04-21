package lib;

import java.util.Date;

public class LostMessage implements Comparable<LostMessage> {

    private String processId;

    private String ip;

    private int messageId;

    private Date date;

    public LostMessage(String processId, String ip, int messageId, Date date) {
        this.processId = processId;
        this.ip = ip;
        this.messageId = messageId;
        this.date = date;
    }

    public String getProcessId() {
        return processId;
    }

    public String getIp() {
        return ip;
    }

    public int getMessageId() {
        return messageId;
    }

    public Date getDate() {
        return date;
    }

    public int compareTo(LostMessage lostMessage) {
        return date.compareTo(lostMessage.getDate());
    }

    public String toString() {
        return getMessageId() + " by " + getProcessId();
    }
}
