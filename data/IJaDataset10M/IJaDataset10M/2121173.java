package Felper.message;

import Felper.user.User;
import java.math.BigInteger;

/**
 * @version 1.0
 * @created 13-Jun-2007 11:58:16
 */
public class TopicMessage extends ServiceMessage {

    private Integer commandField;

    private BigInteger topicID;

    private User topicOwner;

    private String topicTitle;

    private User[] topicSubscriber;

    public TopicMessage() {
    }

    public void finalize() throws Throwable {
    }

    public Integer getCommandField() {
        return commandField;
    }

    public BigInteger getTopicID() {
        return topicID;
    }

    public User getTopicOwner() {
        return topicOwner;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    /**
     * @param newVal
     */
    public void setCommandField(Integer newVal) {
        commandField = newVal;
    }

    /**
     * @param newVal
     */
    public void setTopicID(BigInteger newVal) {
        topicID = newVal;
    }

    /**
     * @param newVal
     */
    public void setTopicOwner(User newVal) {
        topicOwner = newVal;
    }

    /**
     * @param newVal
     */
    public void setTopicTitle(String newVal) {
        topicTitle = newVal;
    }

    public User[] getTopicSubscriber() {
        return topicSubscriber;
    }

    /**
     * @param newVal
     */
    public void setTopicSubscriber(User[] newVal) {
        topicSubscriber = newVal;
    }
}
