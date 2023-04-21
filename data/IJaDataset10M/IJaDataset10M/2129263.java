package cn.chengdu.in.type;

import java.io.Serializable;

/**
 * 勋章的获得信息
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-4-21
 */
public class BadgeGot implements IcdType, Serializable {

    private Place where;

    private String when;

    /**
     * 这个id在use badge 和 从timeline里面view badge的时候才有用
     * 
     */
    private String userOwnBadgeId;

    public Place getWhere() {
        return where;
    }

    public void setWhere(Place where) {
        this.where = where;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getUserOwnBadgeId() {
        return userOwnBadgeId;
    }

    public void setUserOwnBadgeId(String userOwnBadgeId) {
        this.userOwnBadgeId = userOwnBadgeId;
    }
}
