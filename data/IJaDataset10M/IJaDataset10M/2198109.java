package com.hk.bean;

/**
 * 企业功能，可选择的企业功能，每个企业可以选择多个功能，并提供功能其他提前附属功能
 * 
 * @author akwei
 */
public class CmpFunc {

    private long oid;

    /**
	 * 功能名称
	 */
    private String name;

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
