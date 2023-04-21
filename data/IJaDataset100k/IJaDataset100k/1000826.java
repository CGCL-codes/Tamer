package com.ec.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.ec.core.annotation.Table;
import com.ec.core.annotation.Field;

@Table(name = "t_admin")
public class Admin implements java.io.Serializable {

    private int ID;

    private String userId;

    private String userName;

    private String password;

    private int status;

    private Date createTime;

    public Admin() {
    }

    @Field(name = "ID", pk = true, readonly = true)
    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    @Field(name = "Userid")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Field(name = "Username")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Field(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Field(name = "Status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Field(name = "Createtime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
