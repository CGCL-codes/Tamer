package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 我的附件
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "myattachments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Myattachments implements java.io.Serializable {

    private static final long serialVersionUID = -5744091422338073319L;

    private Integer id;

    private Topics topics;

    private Users users;

    private Postid postid;

    private Attachments attachments;

    private String attachment;

    private String description;

    private String postdatetime;

    private Integer downloads;

    private String filename;

    private String extname;

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "tid", unique = false, nullable = false, insertable = true, updatable = true)
    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", unique = false, nullable = false, insertable = true, updatable = true)
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", unique = false, nullable = false, insertable = true, updatable = true)
    public Postid getPostid() {
        return postid;
    }

    public void setPostid(Postid postid) {
        this.postid = postid;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "aid", unique = true, nullable = false, insertable = true, updatable = false)
    public Attachments getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachments attachments) {
        this.attachments = attachments;
    }

    @Column(name = "attachment", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Column(name = "description", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
    public String getPostdatetime() {
        return postdatetime;
    }

    public void setPostdatetime(String postdatetime) {
        this.postdatetime = postdatetime;
    }

    @Column(name = "downloads", unique = false, nullable = false, insertable = true, updatable = true)
    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Column(name = "filename", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Column(name = "extname", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
    public String getExtname() {
        return extname;
    }

    public void setExtname(String extname) {
        this.extname = extname;
    }
}
