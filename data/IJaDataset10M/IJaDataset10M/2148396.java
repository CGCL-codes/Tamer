package demo.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Weibo implements Serializable {

    private Long id;

    private String content;

    private Date time;

    private String picture;

    private String client;

    private Integer userid;

    private Long forwardid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Long getForwardid() {
        return forwardid;
    }

    public void setForwardid(Long forwardid) {
        this.forwardid = forwardid;
    }
}
