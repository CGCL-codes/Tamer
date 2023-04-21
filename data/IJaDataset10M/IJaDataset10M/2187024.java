package test.entity;

import java.io.Serializable;

public class SourceStat implements Serializable {

    private static final long serialVersionUID = 2205831529927461888L;

    private static final String INSTER_SQL = "t.serialVersionUID";

    private int id;

    private Integer gid;

    private Integer sid;

    private String name;

    private java.util.Date regDate;

    private java.util.Date chargeDate;

    private Integer recomend;

    private Integer users;

    private Integer chargeUsers;

    private Integer chargeMoney;

    private Integer level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * 游戏ID
	 **/
    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    /**
	 * 游戏服务器ID
	 **/
    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /**
	 * 渠道
	 **/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * 注册日期
	 **/
    public java.util.Date getRegDate() {
        return regDate;
    }

    public void setRegDate(java.util.Date regDate) {
        this.regDate = regDate;
    }

    /**
	 * 充值日期
	 **/
    public java.util.Date getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(java.util.Date chargeDate) {
        this.chargeDate = chargeDate;
    }

    /**
	 * 推荐数量
	 **/
    public Integer getRecomend() {
        return recomend;
    }

    public void setRecomend(Integer recomend) {
        this.recomend = recomend;
    }

    /**
	 * 角色数量
	 **/
    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    /**
	 * 充值人数
	 **/
    public Integer getChargeUsers() {
        return chargeUsers;
    }

    public void setChargeUsers(Integer chargeUsers) {
        this.chargeUsers = chargeUsers;
    }

    /**
	 * 充值总数
	 **/
    public Integer getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(Integer chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    /**
	 * 等级
	 **/
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SourceStat [id:" + id + ",gid:" + gid + ",sid:" + sid + ",name:" + name + ",regDate:" + regDate + ",chargeDate:" + chargeDate + ",recomend:" + recomend + ",users:" + users + ",chargeUsers:" + chargeUsers + ",chargeMoney:" + chargeMoney + ",level:" + level + "]";
    }
}
