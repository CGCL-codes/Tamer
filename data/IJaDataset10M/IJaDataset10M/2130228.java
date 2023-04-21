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
 * 版主操作记录
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "moderatormanagelog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Moderatormanagelog implements java.io.Serializable {

    private static final long serialVersionUID = -1704135494417602569L;

    private Integer id;

    private Users users;

    private Usergroups usergroups;

    private Topics topics;

    private Forums forums;

    private String moderatorname;

    private String grouptitle;

    private String ip;

    private String postdatetime;

    private String fname;

    private String title;

    private String actions;

    private String reason;

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
    @JoinColumn(name = "moderatoruid", unique = false, nullable = true, insertable = true, updatable = true)
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid", unique = false, nullable = true, insertable = true, updatable = true)
    public Usergroups getUsergroups() {
        return usergroups;
    }

    public void setUsergroups(Usergroups usergroups) {
        this.usergroups = usergroups;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "tid", unique = false, nullable = true, insertable = true, updatable = true)
    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "fid", unique = false, nullable = true, insertable = true, updatable = true)
    public Forums getForums() {
        return forums;
    }

    public void setForums(Forums forums) {
        this.forums = forums;
    }

    @Column(name = "moderatorname", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getModeratorname() {
        return moderatorname;
    }

    public void setModeratorname(String moderatorname) {
        this.moderatorname = moderatorname;
    }

    @Column(name = "grouptitle", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getGrouptitle() {
        return grouptitle;
    }

    public void setGrouptitle(String grouptitle) {
        this.grouptitle = grouptitle;
    }

    @Column(name = "ip", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "postdatetime", unique = false, nullable = true, insertable = true, updatable = true, length = 23)
    public String getPostdatetime() {
        return postdatetime;
    }

    public void setPostdatetime(String postdatetime) {
        this.postdatetime = postdatetime;
    }

    @Column(name = "fname", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Column(name = "title", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "actions", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    @Column(name = "reason", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
