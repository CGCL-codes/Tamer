package blog;

import java.util.Date;

public class Comment {

    private int coId;

    private int postId;

    private String name;

    private String coPw;

    private String content;

    private Date createdAt;

    public Comment() {
    }

    ;

    public Comment(int coId, int postId, String name, String content, Date createdAt) {
        this.setCoId(coId);
        this.postId = postId;
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void setCoId(int coId) {
        this.coId = coId;
    }

    public int getCoId() {
        return coId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCoPw(String coPw) {
        this.coPw = coPw;
    }

    public String getCoPw() {
        return coPw;
    }
}
