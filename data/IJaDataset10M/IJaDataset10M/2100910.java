package cn.poco.bean;

import java.io.Serializable;

public class Mail implements Serializable {

    int total;

    int mainId;

    int userId;

    int state;

    String usreName;

    String title;

    String date;

    String content;

    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMainId() {
        return mainId;
    }

    public void setMainId(int mainId) {
        this.mainId = mainId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUsreName() {
        return usreName;
    }

    public void setUsreName(String usreName) {
        this.usreName = usreName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Mail [total=" + total + ", mainId=" + mainId + ", userId=" + userId + ", state=" + state + ", usreName=" + usreName + ", title=" + title + ", date=" + date + ", content=" + content + ", imageUrl=" + imageUrl + "]";
    }
}
