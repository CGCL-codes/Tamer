package codebush.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

/**
 * the variables are not the same as the table article columns, but join the table users columns.
 * @author Fution Bai
 * @since 1.0
 */
public class Article implements Serializable {

    private static final long serialVersionUID = 8079312700463828838L;

    private long id;

    private String title;

    private String content;

    private Date post_time;

    private int user_id;

    private int reward;

    private int type;

    private String user_name;

    private byte[] picture;

    private String tag_name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPost_time() {
        return post_time;
    }

    public void setPost_time(Date post_time) {
        this.post_time = post_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
