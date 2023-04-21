package org.paradise.dms.pojo;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Description: 
 * Copyright (c) 2008-2009  Neo. 
 * All Rights Reserved.
 * @version 1.0  Mar 30, 2009 9:28:52 PM 李萌（neolimeng@gmail.com）created
 */
@Entity()
@Table(name = "registerinfo")
public class RegisterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer registerinfoid;

    private String registusername;

    private String registuserstudentno;

    private Date registerinfotime;

    private String registerinforecordedby;

    private String registerinfostatus;

    private Integer registerinfoind;

    private Date registereturntime;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "REGISTERINFOTYPEID")
    private RegisterInfoType registerinfotype;

    public Integer getRegisterinfoid() {
        return this.registerinfoid;
    }

    public void setRegisterinfoid(Integer registerinfoid) {
        this.registerinfoid = registerinfoid;
    }

    public String getRegistusername() {
        return this.registusername;
    }

    public void setRegistusername(String registusername) {
        this.registusername = registusername;
    }

    public String getRegistuserstudentno() {
        return this.registuserstudentno;
    }

    public void setRegistuserstudentno(String registuserstudentno) {
        this.registuserstudentno = registuserstudentno;
    }

    public Date getRegisterinfotime() {
        return this.registerinfotime;
    }

    public void setRegisterinfotime(Date registerinfotime) {
        this.registerinfotime = registerinfotime;
    }

    public String getRegisterinforecordedby() {
        return this.registerinforecordedby;
    }

    public void setRegisterinforecordedby(String registerinforecordedby) {
        this.registerinforecordedby = registerinforecordedby;
    }

    public String getRegisterinfostatus() {
        return this.registerinfostatus;
    }

    public void setRegisterinfostatus(String registerinfostatus) {
        this.registerinfostatus = registerinfostatus;
    }

    public Integer getRegisterinfoind() {
        return this.registerinfoind;
    }

    public void setRegisterinfoind(Integer registerinfoind) {
        this.registerinfoind = registerinfoind;
    }

    public Date getRegistereturntime() {
        return this.registereturntime;
    }

    public void setRegistereturntime(Date registereturntime) {
        this.registereturntime = registereturntime;
    }

    /**
	 * @return the registerinfotype
	 */
    public RegisterInfoType getRegisterinfotype() {
        return registerinfotype;
    }

    /**
	 * @param registerinfotype the registerinfotype to set
	 */
    public void setRegisterinfotype(RegisterInfoType registerinfotype) {
        this.registerinfotype = registerinfotype;
    }
}
