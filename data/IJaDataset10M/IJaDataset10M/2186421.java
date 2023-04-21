package com.hibernate.pojo;

import java.util.Date;

public class ProductAtrribute implements java.io.Serializable {

    private Long id;

    private String atrribute_name;

    private Date create_time;

    private Date edit_time;

    private Byte is_del;

    private Long fatherId;

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAtrribute_name() {
        return atrribute_name;
    }

    public void setAtrribute_name(String atrribute_name) {
        this.atrribute_name = atrribute_name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(Date edit_time) {
        this.edit_time = edit_time;
    }

    public Byte getIs_del() {
        return is_del;
    }

    public void setIs_del(Byte is_del) {
        this.is_del = is_del;
    }
}
