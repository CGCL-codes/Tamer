package com.nodeshop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * 实体类 - 消息
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshop98FCE833E4347550503D1E5C947BB0B2
 
 */
@Entity
public class Message extends BaseEntity {

    private static final long serialVersionUID = -112310144651384975L;

    public static final int DEFAULT_MESSAGE_LIST_PAGE_SIZE = 12;

    public enum DeleteStatus {

        nonDelete, fromDelete, toDelete
    }

    ;

    private String title;

    private String content;

    private DeleteStatus deleteStatus;

    private Boolean isRead;

    private Boolean isSaveDraftbox;

    private Member fromMember;

    private Member toMember;

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 10000, nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Enumerated
    @Column(nullable = false)
    public DeleteStatus getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(DeleteStatus deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    @Column(nullable = false)
    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    @Column(nullable = false)
    public Boolean getIsSaveDraftbox() {
        return isSaveDraftbox;
    }

    public void setIsSaveDraftbox(Boolean isSaveDraftbox) {
        this.isSaveDraftbox = isSaveDraftbox;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Member getFromMember() {
        return fromMember;
    }

    public void setFromMember(Member fromMember) {
        this.fromMember = fromMember;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Member getToMember() {
        return toMember;
    }

    public void setToMember(Member toMember) {
        this.toMember = toMember;
    }
}
