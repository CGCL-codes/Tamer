package com.ems.client.action.biz.demo.widgetdemo.vo;

import com.ems.system.client.vo.ExtGridVO;

public class TestQueryInfoVO extends ExtGridVO {

    private String userName;

    private String sex;

    private String birthday;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
