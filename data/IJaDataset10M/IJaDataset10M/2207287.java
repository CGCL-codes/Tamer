package com.laoer.bbscs.bean;

import java.io.Serializable;

public class Commend implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 3639230134895436380L;

    private String id;

    private long boardID;

    private String boardName;

    private String postID;

    private String postMainID;

    private String userID;

    private String userName;

    private long commendBoardID;

    private int commendTop;

    private String title;

    private String boardCategory;

    private String topCategory;

    private long createTime;

    public Commend() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBoardID(long boardID) {
        this.boardID = boardID;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setPostMainID(String postMainID) {
        this.postMainID = postMainID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCommendBoardID(long commendBoardID) {
        this.commendBoardID = commendBoardID;
    }

    public void setCommendTop(int commendTop) {
        this.commendTop = commendTop;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBoardCategory(String boardCategory) {
        this.boardCategory = boardCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public long getBoardID() {
        return boardID;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getPostID() {
        return postID;
    }

    public String getPostMainID() {
        return postMainID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public long getCommendBoardID() {
        return commendBoardID;
    }

    public int getCommendTop() {
        return commendTop;
    }

    public String getTitle() {
        return title;
    }

    public String getBoardCategory() {
        return boardCategory;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public long getCreateTime() {
        return createTime;
    }
}
