package bean;

import java.util.Date;

/**
 * PostComment entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class PostComment implements java.io.Serializable {

    private Integer cmtId;

    private User user;

    private Post post;

    private String commentTitle;

    private String commentContent;

    private Date createdAt;

    /** default constructor */
    public PostComment() {
    }

    /** full constructor */
    public PostComment(Integer cmtId, User user, Post post, String commentTitle, String commentContent, Date createdAt) {
        this.cmtId = cmtId;
        this.user = user;
        this.post = post;
        this.commentTitle = commentTitle;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
    }

    public Integer getCmtId() {
        return this.cmtId;
    }

    public void setCmtId(Integer cmtId) {
        this.cmtId = cmtId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getCommentTitle() {
        return this.commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getCommentContent() {
        return this.commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
