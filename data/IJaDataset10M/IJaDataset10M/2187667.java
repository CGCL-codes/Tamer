package cn.com.wblog.pojo;

import org.nutz.dao.entity.annotation.*;

@Table("wb_comment")
public class Comment {

    @Column
    @Id
    private int id;

    @Column
    private int bodyId;

    @Column
    private int userId;

    @Column
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBodyId() {
        return bodyId;
    }

    public void setBodyId(int bodyId) {
        this.bodyId = bodyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
