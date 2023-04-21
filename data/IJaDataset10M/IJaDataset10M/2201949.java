package org.xmlvm.demo.java.photovm.data;

/**
 * Data about a Picasa we album entry.
 */
public class Album {

    private String title;

    private String coverUrl;

    private int coverWidth;

    private int coverHeight;

    private String photoRequestUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPhotoRequestUrl() {
        return photoRequestUrl;
    }

    public void setPhotoRequestUrl(String photoRequestUrl) {
        this.photoRequestUrl = photoRequestUrl;
    }

    public int getCoverHeight() {
        return coverHeight;
    }

    public void setCoverHeight(String coverHeight) {
        this.coverHeight = Integer.parseInt(coverHeight);
    }

    public int getCoverWidth() {
        return coverWidth;
    }

    public void setCoverWidth(String coverWidth) {
        this.coverWidth = Integer.parseInt(coverWidth);
    }
}
