package com.yinzhijie.alin.entity;

/**
 * 查询条件实体类
 * @author Sankooc
 *
 */
public class InputCondition {

    private int type;

    private String showname;

    private boolean forSQL;

    private String param;

    private int x;

    private int y;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShowname() {
        return showname;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public boolean isForSQL() {
        return forSQL;
    }

    public void setForSQL(boolean forSQL) {
        this.forSQL = forSQL;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getParam() {
        return param;
    }
}
