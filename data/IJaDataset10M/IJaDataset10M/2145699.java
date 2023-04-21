package com.techstar.dmis.entity;

import java.io.Serializable;

/**
 * Domain classe for 发电批准书附件数据表
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class FsAllappendix implements Serializable {

    public FsAllappendix() {
    }

    private String f_entityname;

    private String f_note;

    private byte[] f_content;

    private String type;

    private String sys_fille;

    private String sys_filldept;

    private java.sql.Timestamp sys_filltime;

    private int sys_isvalid;

    private String sys_dataowner;

    private String f_id;

    private int version;

    private com.techstar.dmis.entity.FsApprovebook zfsallappendix1;

    /**
     * getters and setters
     */
    public void setF_entityname(String f_entityname) {
        this.f_entityname = f_entityname;
    }

    public String getF_entityname() {
        return f_entityname;
    }

    public void setF_note(String f_note) {
        this.f_note = f_note;
    }

    public String getF_note() {
        return f_note;
    }

    public void setF_content(byte[] f_content) {
        this.f_content = f_content;
    }

    public byte[] getF_content() {
        return f_content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setSys_fille(String sys_fille) {
        this.sys_fille = sys_fille;
    }

    public String getSys_fille() {
        return sys_fille;
    }

    public void setSys_filldept(String sys_filldept) {
        this.sys_filldept = sys_filldept;
    }

    public String getSys_filldept() {
        return sys_filldept;
    }

    public void setSys_filltime(java.sql.Timestamp sys_filltime) {
        this.sys_filltime = sys_filltime;
    }

    public java.sql.Timestamp getSys_filltime() {
        return sys_filltime;
    }

    public void setSys_isvalid(int sys_isvalid) {
        this.sys_isvalid = sys_isvalid;
    }

    public int getSys_isvalid() {
        return sys_isvalid;
    }

    public void setSys_dataowner(String sys_dataowner) {
        this.sys_dataowner = sys_dataowner;
    }

    public String getSys_dataowner() {
        return sys_dataowner;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_id() {
        return f_id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZfsallappendix1(com.techstar.dmis.entity.FsApprovebook zfsallappendix1) {
        this.zfsallappendix1 = zfsallappendix1;
    }

    public com.techstar.dmis.entity.FsApprovebook getZfsallappendix1() {
        return zfsallappendix1;
    }
}
