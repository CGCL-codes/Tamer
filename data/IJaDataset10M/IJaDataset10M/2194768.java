package com.xiaxueqi.entity.book;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CodeGen --powered by Sean
 *
 */
public class Site implements Serializable {

    private Integer id;

    private String name;

    private String url;

    private String deleted;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDeleted() {
        return deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
