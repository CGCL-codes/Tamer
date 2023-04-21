package com.khotyn.heresy.bean;

/**
 * Error message.
 * 
 * @author khotyn
 * 
 */
public class HeresyErrorMessage {

    private String messageContent;

    private String title;

    private String jumpUrl;

    public HeresyErrorMessage() {
    }

    public HeresyErrorMessage(String messageContent, String title, String jumpUrl) {
        this.jumpUrl = jumpUrl;
        this.messageContent = messageContent;
        this.title = title;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    /**
	 * @param jumpUrl the jumpUrl to set
	 */
    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
