package org.paradise.dms.pojo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

/**
 * 
 * Description: 学生床位连接表
 * 
 * 
 * Copyright (c) 2008-2009 paraDise sTudio(DT). All Rights Reserved.
 * 
 * @version 1.0 2009-3-18 下午09:51:01 李双江（paradise.lsj@gmail.com）created
 * @Versio 1.1 增加dormitoryid字段
 */
@Entity
@Table(name = "studentbedlink")
@BatchSize(size = 50)
public class StudentBedLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int studentbedlinkid;

    private int accomodationstatusid;

    private int dormitorybedid;

    public int getDormitorybedid() {
        return dormitorybedid;
    }

    public void setDormitorybedid(int dormitorybedid) {
        this.dormitorybedid = dormitorybedid;
    }

    private int studentid;

    private int dormitoryid;

    public int getDormitoryid() {
        return dormitoryid;
    }

    public void setDormitoryid(int dormitoryid) {
        this.dormitoryid = dormitoryid;
    }

    private Date studentbedstartintime;

    private Date studentbedendouttime;

    public int getStudentbedlinkid() {
        return studentbedlinkid;
    }

    public void setStudentbedlinkid(int studentbedlinkid) {
        this.studentbedlinkid = studentbedlinkid;
    }

    public int getAccomodationstatusid() {
        return accomodationstatusid;
    }

    public void setAccomodationstatusid(int accomodationstatusid) {
        this.accomodationstatusid = accomodationstatusid;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public Date getStudentbedstartintime() {
        return studentbedstartintime;
    }

    public void setStudentbedstartintime(Date studentbedstartintime) {
        this.studentbedstartintime = studentbedstartintime;
    }

    public Date getStudentbedendouttime() {
        return studentbedendouttime;
    }

    public void setStudentbedendouttime(Date studentbedendouttime) {
        this.studentbedendouttime = studentbedendouttime;
    }
}
