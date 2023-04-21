package test.entity;

import java.io.Serializable;

public class PlayerSaleLog implements Serializable {

    private static final long serialVersionUID = 8979451015505058816L;

    private static final String INSTER_SQL = "t.serialVersionUID";

    private int id;

    private Integer gid;

    private Integer sid;

    private Integer uid;

    private String roleName;

    private Integer goodsId;

    private String goods;

    private Integer price;

    private Integer amount;

    private Integer money;

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
	 * 物品ID
	 **/
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
	 * 物品名称
	 **/
    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    /**
	 * 物品价格
	 **/
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
	 * 出售数量
	 **/
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
	 * 出售总金额
	 **/
    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
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
        return "PlayerSaleLog [id:" + id + ",gid:" + gid + ",sid:" + sid + ",uid:" + uid + ",roleName:" + roleName + ",goodsId:" + goodsId + ",goods:" + goods + ",price:" + price + ",amount:" + amount + ",money:" + money + ",createTime:" + createTime + "]";
    }
}
