package com.hk.bean;

import com.hk.frame.dao.annotation.Table;

@Table(name = "cmpproductfav", id = "oid")
public class CmpProductFav {

    private long oid;

    private long userId;

    private long productId;

    private long companyId;

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
