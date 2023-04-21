package vlan.webgame.manage.entity;

import java.io.Serializable;

public class PropsLog implements Serializable {

    private static final long serialVersionUID = -6329181173009798144L;

    private int id;

    private Integer pid;

    private Integer sid;

    private Integer uid;

    private String roleName;

    private Integer equipId;

    private String equipName;

    private Integer amount;

    private String remark;

    private java.sql.Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * 平台ID
	 **/
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
	 * 服务器ID
	 **/
    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /**
	 * 玩家帐号id
	 **/
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
	 * 玩家角色名称
	 **/
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
	 * 装备id
	 **/
    public Integer getEquipId() {
        return equipId;
    }

    public void setEquipId(Integer equipId) {
        this.equipId = equipId;
    }

    /**
	 * 装备名称
	 **/
    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    /**
	 * 当前数量
	 **/
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
	 * 说明
	 **/
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
	 * 时间
	 **/
    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PropsLog [id:" + id + ",pid:" + pid + ",sid:" + sid + ",uid:" + uid + ",roleName:" + roleName + ",equipId:" + equipId + ",equipName:" + equipName + ",amount:" + amount + ",remark:" + remark + ",createTime:" + createTime + "]";
    }
}
