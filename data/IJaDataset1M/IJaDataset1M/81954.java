package com.hk.bean;

import java.util.Date;
import com.hk.frame.dao.annotation.Column;
import com.hk.frame.dao.annotation.Id;
import com.hk.frame.dao.annotation.Table;

/**
 * 正使用在用户身上的装备
 * 
 * @author akwei
 */
@Table(name = "userequenjoy")
public class UserEquEnjoy {

    @Id
    private long oid;

    /**
	 * 装备使用者
	 */
    @Column
    private long userId;

    /**
	 * 装备享受者，即目标人物
	 */
    @Column
    private long enjoyUserId;

    /**
	 * 装备id
	 */
    @Column
    private long ueid;

    @Column
    private Date createTime;

    /**
	 * 强制使用的eid，任何防具无效
	 */
    @Column
    private long forceEid;

    private UserEquipment userEquipment;

    public long getForceEid() {
        return forceEid;
    }

    public void setForceEid(long forceEid) {
        this.forceEid = forceEid;
    }

    public void setUserEquipment(UserEquipment userEquipment) {
        this.userEquipment = userEquipment;
    }

    public UserEquipment getUserEquipment() {
        return userEquipment;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
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

    public long getEnjoyUserId() {
        return enjoyUserId;
    }

    public void setEnjoyUserId(long enjoyUserId) {
        this.enjoyUserId = enjoyUserId;
    }

    public void setUeid(long ueid) {
        this.ueid = ueid;
    }

    public long getUeid() {
        return ueid;
    }
}
