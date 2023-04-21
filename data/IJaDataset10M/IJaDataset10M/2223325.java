package com.ejpmodel.bean;

import com.ejpmodel.orm.attribute;
import com.james.database.mydb;
import com.james.Ecode.MD5;
import com.james.util.Test;

/**
 *
 * @author James liu
 */
public class saveData {

    private int cid;

    private int eid;

    private String SQL;

    public saveData(int eid, int cid) {
        this.eid = eid;
        this.cid = cid;
    }

    /**
 *设置属性值,自动判别类型
 */
    public void setValue(int aid, String value) {
        attribute attri = new attribute(aid);
        if (attri != null) {
            if (attri.getType().equals("string")) {
                setString(aid, value);
            } else if (attri.getType().equals("password")) {
                MD5 md5 = new MD5();
                String ecodeStr = md5.getMD5ofStr(value);
                if (value.length() != 32) setString(aid, ecodeStr);
            } else if (attri.getType().equals("int")) {
                setNumber(aid, value);
            } else if (attri.getType().equals("float")) {
                setNumber(aid, value);
            } else if (attri.getType().equals("radio")) {
                setNumber(aid, value);
            } else if (attri.getType().equals("checkbox")) {
                setString(aid, value);
            } else if (attri.getType().equals("list")) {
                setNumber(aid, value);
            } else if (attri.getType().equals("text")) {
                setString(aid, value);
            } else if (attri.getType().equals("datetime")) {
                setString(aid, value);
            } else if (attri.getType().equals("image")) {
                setString(aid, value);
            } else if (attri.getType().equals("file")) {
                setString(aid, value);
            }
        }
    }

    /**
 * 数字类型的更新语句构造
 * @param aid
 * @param value
 * @return
 */
    private String setNumber(int aid, String value) {
        if (!Test.isDecimal(value)) return "";
        if (SQL != null && !SQL.equals("")) SQL += ",";
        if (SQL == null) SQL = "";
        SQL += "`$" + aid + "`=" + value;
        return SQL;
    }

    /**
 * 字符类型更新语句构造
 * @param aid
 * @param value
 * @return
 */
    private String setString(int aid, String value) {
        if (value == null) value = "";
        value = value.replaceAll("'", "&acute;").replaceAll("\\uE562{1,}", "");
        if (SQL != null && !SQL.equals("")) SQL += ",";
        if (SQL == null) SQL = "";
        SQL += "`$" + aid + "`='" + value + "'";
        return SQL;
    }

    /**
 * 提交更新
 */
    public boolean commit() {
        boolean res = false;
        if (SQL != null && !SQL.equals("")) {
            SQL += ",lastModify=now() where id=" + eid;
            SQL = "update $" + cid + " set " + SQL;
            if (mydb.executeUpdate(SQL) > 0) res = true;
        }
        SQL = null;
        return res;
    }

    @Override
    protected void finalize() throws Throwable {
        if (SQL != null && !SQL.equals("")) commit();
    }
}
