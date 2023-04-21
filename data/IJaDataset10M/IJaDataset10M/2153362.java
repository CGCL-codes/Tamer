package com.babelstudio.cpasss.hibernate;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Code entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "code", catalog = "zp41e1_db")
public class Code implements java.io.Serializable {

    private Integer id;

    private String name;

    private String content;

    private String memo;

    private String children;

    private Integer pid;

    private Timestamp createtime;

    /** default constructor */
    public Code() {
    }

    /** full constructor */
    public Code(String name, String content, String memo, String children, Integer pid, Timestamp createtime) {
        this.name = name;
        this.content = content;
        this.memo = memo;
        this.children = children;
        this.pid = pid;
        this.createtime = createtime;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", length = 128)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "content", length = 128)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "memo", length = 128)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "children")
    public String getChildren() {
        return this.children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    @Column(name = "pid")
    public Integer getPid() {
        return this.pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Column(name = "createtime", length = 19)
    public Timestamp getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }
}
