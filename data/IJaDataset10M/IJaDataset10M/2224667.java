package edu.calstatela.coolstatela.workflow;

import java.util.Date;

public class MediaHistory {

    private int mediahistoryID;

    private int storyID;

    private int mediaID;

    private int userID;

    private Date creationdate;

    private String comments;

    public int getMediahistoryID() {
        return mediahistoryID;
    }

    public void setMediahistoryID(int mediahistoryID) {
        this.mediahistoryID = mediahistoryID;
    }

    public int getStoryID() {
        return storyID;
    }

    public void setStoryID(int storyID) {
        this.storyID = storyID;
    }

    public int getMediaID() {
        return mediaID;
    }

    public void setMediaID(int mediaID) {
        this.mediaID = mediaID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
