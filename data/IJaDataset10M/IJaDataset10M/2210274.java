package com.hcs.protocol.model;

import java.io.Serializable;

/**
 * 系统名： HCSMobileApp
 * 子系统名：相关人员数据结构类
 * 著作权：COPYRIGHT (C) 2011 HAND INFORMATION SYSTEMS CORPORATION ALL
 * 			RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 9, 2011
 */
public class People implements Serializable {

    private String relation;

    private String name;

    private String phone;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
