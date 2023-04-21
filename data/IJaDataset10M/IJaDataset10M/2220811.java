package info.devcafe.devtalk.controller.mapping;

import java.util.List;
import info.devcafe.devtalk.model.Discussion;
import info.devcafe.devtalk.model.Keyword;
import info.devcafe.devtalk.model.Message;
import info.devcafe.devtalk.util.Moment;

public class DiscussionData {

    private Integer discussionId;

    private String discussionSubject;

    private Integer messageId;

    private String subject;

    private String text;

    private String keywords;

    public DiscussionData() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getDiscussionSubject() {
        return discussionSubject;
    }

    public void setDiscussionSubject(String discussionSubject) {
        this.discussionSubject = discussionSubject;
    }

    public Integer getDiscussionId() {
        return discussionId;
    }

    public void addDiscussionId(Integer discussionId) {
        this.discussionId = discussionId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void updateMessage(Message message) {
        message.setId(messageId);
        message.setSubject(subject);
        message.setPublication(new Moment());
        message.setText(text);
    }

    public void updateDiscussion(Discussion discussion, Message message, List<Keyword> keywords) {
        discussion.setSubject(message.getSubject());
        discussion.setPublication(message.getPublication());
        discussion.addKeywords(keywords);
        discussion.setLastModification(message.getPublication());
        message.setDiscussion(discussion);
        discussion.addMessage(message);
    }
}
