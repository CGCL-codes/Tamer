package cn.poco.bean;

import java.io.Serializable;

/**
 * ����ͼƬǽ
 * @author tanglx
 *
 */
public class ResBolg implements Serializable {

    private int total;

    private int userId;

    private String userName;

    private String leve;

    private String blogId;

    private String title;

    private String imgUrl;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLeve() {
        return leve;
    }

    public void setLeve(String leve) {
        this.leve = leve;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "ResBolg [total=" + total + ", userId=" + userId + ", userName=" + userName + ", leve=" + leve + ", blogId=" + blogId + ", title=" + title + ", imgUrl=" + imgUrl + "]";
    }
}
