package com.liusoft.dlog4j.beans;

import com.liusoft.dlog4j.base._ReplyBean;

/**
 * ��Ƭ����
 * 
 * @author Winter Lau
 */
public class PhotoReplyBean extends _ReplyBean {

    protected PhotoOutlineBean photo;

    public PhotoOutlineBean getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoOutlineBean photo) {
        this.photo = photo;
    }

    public int getParentId() {
        return photo.getId();
    }

    /** *** The methods below is for search proxy **** */
    public String name() {
        return "photo_reply";
    }

    public String[] getStoreFields() {
        return new String[] { "site.id", "site.friendlyName", "author", "replyTime", "user.id", "user.nickname", "photo.id", "photo.name", "photo.previewURL", "content" };
    }
}
