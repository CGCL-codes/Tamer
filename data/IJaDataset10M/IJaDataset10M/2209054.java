package com.hk.bean.taobao;

import java.util.Date;
import com.hk.frame.dao.annotation.Column;
import com.hk.frame.dao.annotation.Id;
import com.hk.frame.dao.annotation.Table;

@Table(name = "tb_sysuser")
public class Tb_Sysuser {

    @Id
    private long userid;

    @Column
    private Date create_time;

    private Tb_User tbUser;

    public void setTbUser(Tb_User tbUser) {
        this.tbUser = tbUser;
    }

    public Tb_User getTbUser() {
        return tbUser;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date createTime) {
        create_time = createTime;
    }
}
