package org.paradise.dms.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description: 
 * 学院信息表
 * Copyright (c) 2008-2009  Neo. 
 * All Rights Reserved.
 * @version 1.0  Mar 19, 2009 12:19:34 AM 李萌（neolimeng@gmail.com）created
 */
@Entity
@Table(name = "collegeinfo")
public class CollegeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int collegeinfoid;

    private String collegeinfoname;

    public int getCollegeinfoid() {
        return collegeinfoid;
    }

    public void setCollegeinfoid(int collegeinfoid) {
        this.collegeinfoid = collegeinfoid;
    }

    public String getCollegeinfoname() {
        return collegeinfoname;
    }

    public void setCollegeinfoname(String collegeinfoname) {
        this.collegeinfoname = collegeinfoname;
    }
}
