package test.entity;

import java.io.Serializable;

public class PlayerGoldLog implements Serializable {

    private static final long serialVersionUID = 7920611689781901312L;

    private static final String INSTER_SQL = "t.serialVersionUID";

    private int id;

    private Integer gid;

    private Integer sid;

    private Integer uid;

    private String roleName;

    private Integer type;

    private Integer amount;

    private java.util.Date createTime;

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
	 * 玩家id
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
	 * 获得方式(1、捡到  
            2、打怪  
            3、做任务（包括副本）
             4、跟人交易  
            5、npc出售 
            6、其他)
	 **/
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
	 * 获得数量
	 **/
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
	 * 操作时间
	 **/
    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PlayerGoldLog [id:" + id + ",gid:" + gid + ",sid:" + sid + ",uid:" + uid + ",roleName:" + roleName + ",type:" + type + ",amount:" + amount + ",createTime:" + createTime + "]";
    }
}
