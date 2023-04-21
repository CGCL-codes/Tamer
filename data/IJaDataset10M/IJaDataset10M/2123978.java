package com.creawor.hz_market.t_infor_review;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.creawor.km.util.DateUtil;

public class t_infor_review implements Serializable {

    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int parm) {
        id = parm;
    }

    private int infor_id = 0;

    public int getInfor_id() {
        return infor_id;
    }

    public void setInfor_id(int parm) {
        infor_id = parm;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String parm) {
        content = parm;
    }

    private String formatDay;

    public String getFormatDay() {
        return formatDay;
    }

    public void setFormatDay(String formatDay) {
        this.formatDay = formatDay;
    }

    private java.util.Date insert_day;

    public java.util.Date getInsert_day() {
        return insert_day;
    }

    public void setInsert_day(java.util.Date parm) {
        insert_day = parm;
        this.formatDay = DateUtil.getStr(this.insert_day, "yyyy-MM-dd HH:mm:ss");
    }

    private java.util.Date update_day;

    public java.util.Date getUpdate_day() {
        return update_day;
    }

    public void setUpdate_day(java.util.Date parm) {
        update_day = parm;
    }

    private String create_name;

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String parm) {
        create_name = parm;
    }

    private Iterator attaches;

    public Iterator getAttaches() {
        return attaches;
    }

    public void setAttaches(Iterator attaches) {
        this.attaches = attaches;
    }

    private String attachname1;

    private String attachname2;

    private String attachname3;

    private String attachfullname1;

    private String attachfullname2;

    private String attachfullname3;

    public String getAttachfullname1() {
        return attachfullname1;
    }

    public void setAttachfullname1(String attachfullname1) {
        this.attachfullname1 = attachfullname1;
    }

    public String getAttachfullname2() {
        return attachfullname2;
    }

    public void setAttachfullname2(String attachfullname2) {
        this.attachfullname2 = attachfullname2;
    }

    public String getAttachfullname3() {
        return attachfullname3;
    }

    public void setAttachfullname3(String attachfullname3) {
        this.attachfullname3 = attachfullname3;
    }

    public String getAttachname1() {
        return attachname1;
    }

    public void setAttachname1(String attachname1) {
        this.attachname1 = attachname1;
    }

    public String getAttachname2() {
        return attachname2;
    }

    public void setAttachname2(String attachname2) {
        this.attachname2 = attachname2;
    }

    public String getAttachname3() {
        return attachname3;
    }

    public void setAttachname3(String attachname3) {
        this.attachname3 = attachname3;
    }
}
