package com.mycompany.myweblib.server.business;

import java.util.Date;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class BusinessObject implements IsSerializable {

    private Date sysCreatedDate = null;

    private Date sysModifiedDate = null;

    private String sysCreatedUser = "";

    private String sysModifiedUser = "";

    private Boolean sysVisible = false;

    public Date getSysCreatedDate() {
        return sysCreatedDate;
    }

    public void setSysCreatedDate(Date sysCreatedDate) {
        this.sysCreatedDate = sysCreatedDate;
    }

    public Date getSysModifiedDate() {
        return sysModifiedDate;
    }

    public void setSysModifiedDate(Date sysModifiedDate) {
        this.sysModifiedDate = sysModifiedDate;
    }

    public String getSysCreatedUser() {
        return sysCreatedUser;
    }

    public void setSysCreatedUser(String sysCreatedUser) {
        this.sysCreatedUser = sysCreatedUser;
    }

    public String getSysModifiedUser() {
        return sysModifiedUser;
    }

    public void setSysModifiedUser(String sysModifiedUser) {
        this.sysModifiedUser = sysModifiedUser;
    }

    public Boolean getSysVisible() {
        return sysVisible;
    }

    public void setSysVisible(Boolean sysVisible) {
        this.sysVisible = sysVisible;
    }
}
