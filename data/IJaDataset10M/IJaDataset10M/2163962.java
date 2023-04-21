package org.yehongyu.websale.db.po.mydb;

import java.io.Serializable;

/**
 *�־���
 */
public class SysUserMenu implements Serializable {

    private long id;

    private long userid;

    private long menuid;

    /**
    * ȡ��id
    * @return 
    */
    public long getId() {
        return id;
    }

    /**
    * ����id
    * @param ������id
    */
    public void setId(long id) {
        this.id = id;
    }

    /**
    * ȡ��userid
    * @return 
    */
    public long getUserid() {
        return userid;
    }

    /**
    * ����userid
    * @param ������userid
    */
    public void setUserid(long userid) {
        this.userid = userid;
    }

    /**
    * ȡ��menuid
    * @return 
    */
    public long getMenuid() {
        return menuid;
    }

    /**
    * ����menuid
    * @param ������menuid
    */
    public void setMenuid(long menuid) {
        this.menuid = menuid;
    }
}
