package com.microstream.lift.client;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MsUser {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String userName;

    @Persistent
    private String userPassword;

    @Persistent
    private Date registerDate;

    @Persistent
    private String eMail;

    public MsUser(String userName, String passWord, String eMail, Date registerDate) {
        this.userName = userName;
        this.userPassword = passWord;
        this.eMail = eMail;
        this.registerDate = registerDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return userPassword;
    }

    public void setPassWord(String passWord) {
        this.userPassword = passWord;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String mail) {
        eMail = mail;
    }
}
