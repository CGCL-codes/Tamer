package com.company.sys.model;

/**
 * TSParams entity. @author MyEclipse Persistence Tools
 */
public class TSParams implements java.io.Serializable {

    private String id;

    private String code;

    private String name;

    private String value;

    private String remark;

    /** default constructor */
    public TSParams() {
    }

    /** minimal constructor */
    public TSParams(String id, String code, String name, String value) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.value = value;
    }

    /** full constructor */
    public TSParams(String id, String code, String name, String value, String remark) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.value = value;
        this.remark = remark;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
