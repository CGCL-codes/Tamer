package com.ejpmodel.bean;

import com.ejpmodel.orm.attribute;
import com.ejpmodel.orm.cmsClass;
import com.ejpmodel.orm.entity;
import java.util.List;
import com.james.database.mydb;

/**
 *
 * @author James liu
 */
public class destroy {

    /**
 * 删除类
 */
    public boolean deleteClass(int id) {
        cmsClass cls = new cmsClass(id);
        List list = mydb.executeQuery("select id from cms_class  where pid=" + id + "");
        if (!list.isEmpty()) return false;
        if (cls.getLockme()) return false;
        mydb.executeUpdate("delete from cms_class where id=" + id + "");
        mydb.executeUpdate("delete from attribute where cid=" + id + "");
        deleteDataList(id);
        mydb.executeUpdate("DROP TABLE IF EXISTS `$" + id + "`");
        return true;
    }

    /**
     * 删除一个属性
     * @param id
     * @return
     */
    public boolean deleteAttribute(int id) {
        attribute att = new attribute(id);
        if (att.getLockme()) return false;
        mydb.executeUpdate("delete from attribute where id=" + id + "");
        String sq = "ALTER TABLE $" + att.getCid() + " drop `$" + id + "`";
        mydb.executeUpdate(sq);
        return true;
    }

    /**
     * 删除某条数据
     * @param id
     * @return
     */
    public boolean deleteData(int id) {
        entity ent = new entity(id);
        mydb.executeUpdate("delete from entity where id=" + id + "");
        mydb.executeUpdate("delete  from $" + ent.getCid() + " where id=" + id + "");
        return true;
    }

    /**
  * 删除某一类的所有数据
  * @param cid
  * @return
  */
    private boolean deleteDataList(int cid) {
        mydb.executeUpdate("delete from entity where cid =" + cid + "");
        mydb.executeUpdate("delete from $" + cid);
        return true;
    }

    /**
     * 删除指定的数据条目
     * @param list
     * @return
     */
    public int deleteDataList(List<Integer> list) {
        int r = 0;
        for (int i = 0; i < list.size(); i++) {
            if (deleteData(list.get(i).intValue())) r++;
        }
        return r;
    }
}
