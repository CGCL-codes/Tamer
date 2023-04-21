package org.w2b.blog;

import java.util.Date;

public class WPItem {

    public static final int STATUS_PUBLISH = 1;

    public static final int STATUS_DRAFT = 0;

    private String title;

    private Date postDate;

    private String[] categories;

    private String content;

    private int status;

    private WPComment[] comments;

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public WPComment[] getComments() {
        return comments;
    }

    public void setComments(WPComment[] comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
