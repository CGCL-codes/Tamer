package com.viddler.apiclient.responses;

import java.util.List;

/**
 * Video response
 * 
 * @author Maciej Dragan
 * 
 */
public class Video implements ApiResponse {

    private static final long serialVersionUID = -8822543890607723093L;

    private String author;

    private String id;

    private String title;

    private int lengthSeconds;

    private int width;

    private int height;

    private String description;

    private int viewCount;

    private long uploadTime;

    private long madePublicTime;

    private int commentCount;

    private Tags tags;

    private String url;

    private String thumbnailUrl;

    private long updateTime;

    private Comments commentsHolder;

    private Permissions permissions;

    private String permalink;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
    }

    public void setLengthSeconds(int lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public long getMadePublicTime() {
        return madePublicTime;
    }

    public void setMadePublicTime(long madePublicTime) {
        this.madePublicTime = madePublicTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public Comments getCommentsHolder() {
        return commentsHolder;
    }

    public void setCommentsHolder(Comments comments) {
        this.commentsHolder = comments;
    }

    public List<Comment> getComments() {
        return getCommentsHolder() != null ? getCommentsHolder().getList() : null;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }
}
