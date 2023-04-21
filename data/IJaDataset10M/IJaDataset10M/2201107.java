package com.zhiyun.admin.vo;

import java.io.Serializable;
import java.util.Date;

public class EbMessage implements Serializable {

    private static final long serialVersionUID = -7794597348602963413L;

    private Long id;

    private EbUser user;

    private String messageContent;

    private Date createTime;

    /** default constructor */
    public EbMessage() {
    }

    /** full constructor */
    public EbMessage(EbUser user, String messageContent, Date createTime) {
        this.user = user;
        this.messageContent = messageContent;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EbUser getUser() {
        return user;
    }

    public void setUser(EbUser user) {
        this.user = user;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
