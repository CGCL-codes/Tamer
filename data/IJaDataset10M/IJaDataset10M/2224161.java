package com.maicuole.story.model;

import java.util.Date;
import com.maicuole.user.model.User;

/**
 * 标签：打到product,story，shop和上面
 * @author pl
 *
 */
public class TagNode {

    private long id;

    private int tagType;

    private long sourceId;

    private User user;

    private String name;

    private Date tagTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTagTime() {
        return tagTime;
    }

    public void setTagTime(Date tagTime) {
        this.tagTime = tagTime;
    }
}
