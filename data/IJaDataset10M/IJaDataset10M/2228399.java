package org.paradise.dms.pojo;

import java.sql.Timestamp;

/**
 * domain公共父类
 */
public class Base {

    /**
	 * 物理主键，自动生成
	 */
    private long id;

    /**
	 * 性别
	 */
    private String sex;

    /**
	 * 
	 * 创建人ID
	 */
    private long createUserId;

    /**
	 * 修改人ID
	 */
    private long modifyUserId;

    /**
	 * 创建人
	 */
    private String createUser;

    /**
	 * 创建人IP
	 */
    private String createIP;

    /**
	 * 创建时间
	 */
    private Timestamp createTime;

    /**
	 * 修改人
	 */
    private String modifyUser;

    /**
	 * 修改人IP
	 */
    private String modifyIP;

    /**
	 * 修改时间
	 */
    private Timestamp modifyTime;

    /**
	 * 排序号
	 */
    private int rank;

    public String getCreateIP() {
        return createIP;
    }

    public void setCreateIP(String createIP) {
        this.createIP = createIP;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModifyIP() {
        return modifyIP;
    }

    public void setModifyIP(String modifyIP) {
        this.modifyIP = modifyIP;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    public long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
