package com.zhiyun.estore.common.vo;

import java.util.List;

public class EbBrand implements java.io.Serializable {

    private static final long serialVersionUID = 9210400030657987144L;

    private Long id;

    private EbCatagory catagory;

    private String name;

    private String remark;

    private Integer delFlag;

    private List<EbItem> items;

    /** default constructor */
    public EbBrand() {
    }

    /** full constructor */
    public EbBrand(EbCatagory catagory, String name, String remark, Integer delFlag) {
        this.catagory = catagory;
        this.name = name;
        this.remark = remark;
        this.delFlag = delFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EbCatagory getCatagory() {
        return catagory;
    }

    public void setCatagory(EbCatagory catagory) {
        this.catagory = catagory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public List<EbItem> getItems() {
        return items;
    }

    public void setItems(List<EbItem> items) {
        this.items = items;
    }
}
