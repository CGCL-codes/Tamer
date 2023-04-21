package com.hk.bean;

import com.hk.frame.dao.annotation.Table;

@Table(name = "cmplink", id = "linkid")
public class CmpLink {

    private int linkId;

    private long companyId;

    private long linkCompanyId;

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getLinkCompanyId() {
        return linkCompanyId;
    }

    public void setLinkCompanyId(long linkCompanyId) {
        this.linkCompanyId = linkCompanyId;
    }
}
