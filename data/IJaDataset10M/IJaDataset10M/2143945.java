package com.simpleftp.ftp.server.objects;

import java.io.File;
import java.util.Date;
import com.simpleftp.ftp.server.filesystem.FtpFile;
import com.simpleftp.ftp.server.utils.FileUtil;
import com.simpleftp.ftp.server.utils.TextUtil;

/**
 * Represents a user in Ftp Server
 * @author sajil
 *
 */
public class FtpUser {

    private Date createdDate = null;

    private Date expireDate = null;

    private String userName = "";

    private String root = "";

    private String password = "";

    private String account = "";

    private String userHome = null;

    private String pwd = null;

    private int userId;

    public FtpUser(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHome() {
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        pwd = FileUtil.removeStartingSlash(pwd);
        pwd = FileUtil.removeEndingSlash(pwd);
        this.pwd = File.separator + pwd;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String toString() {
        return " Name " + userName + "Home " + userHome + "Expire Date " + expireDate + " Current Working Directory " + pwd;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
