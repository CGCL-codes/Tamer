package com.pioneer.app.dev.osuser.user;

/**
 * TUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TUser implements java.io.Serializable {

    private Integer id;

    private String code;

    private String loginName;

    private String nickName;

    private String name;

    private String telphone;

    private String mobil;

    private String email;

    private String address;

    private String passwore;

    private String status;

    private String lastVisitTime;

    private String photoPath;

    private String postName;

    private String isCryptograph;

    /** default constructor */
    public TUser() {
    }

    /** full constructor */
    public TUser(String code, String loginName, String nickName, String name, String telphone, String mobil, String email, String address, String passwore, String status, String lastVisitTime, String photoPath, String postName, String isCryptograph) {
        this.code = code;
        this.loginName = loginName;
        this.nickName = nickName;
        this.name = name;
        this.telphone = telphone;
        this.mobil = mobil;
        this.email = email;
        this.address = address;
        this.passwore = passwore;
        this.status = status;
        this.lastVisitTime = lastVisitTime;
        this.photoPath = photoPath;
        this.postName = postName;
        this.isCryptograph = isCryptograph;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphone() {
        return this.telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getMobil() {
        return this.mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswore() {
        return this.passwore;
    }

    public void setPasswore(String passwore) {
        this.passwore = passwore;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastVisitTime() {
        return this.lastVisitTime;
    }

    public void setLastVisitTime(String lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPostName() {
        return this.postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getIsCryptograph() {
        return this.isCryptograph;
    }

    public void setIsCryptograph(String isCryptograph) {
        this.isCryptograph = isCryptograph;
    }
}
