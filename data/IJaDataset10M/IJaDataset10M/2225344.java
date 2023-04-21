package com.ejpmodel.orm;

import com.james.database.mydb;
import com.james.datetime.datetime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 *     table="ENTITY"
 *
 */
public class entity {

    public entity() {
        int tid = mydb.executeInsert("insert into entity()values()");
        if (tid == 0) {
            throw null;
        }
        setId(tid);
    }

    public entity(int id) {
        setId(id);
    }

    private Date wtime;

    /**
  *   @hibernate.property
  */
    public Date getWtime() {
        return wtime;
    }

    public void setWtime(Date wtime) {
        this.wtime = wtime;
        datetime dt = new datetime();
        mydb.executeUpdate("update entity set wtime='" + dt.formatDate(wtime) + "' where id=" + id + "");
    }

    private Date lastModify;

    /**
  *   @hibernate.property
  */
    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        if (lastModify.compareTo(this.lastModify) != 0) {
            datetime dt = new datetime();
            String ddtr = dt.formatDate(lastModify);
            mydb.executeUpdate("update entity set lastmodify='" + ddtr + "' where id=" + id + "");
            if (this.cid == 0) {
                mydb.executeUpdate("update $" + cid + " set lastmodify='" + ddtr + "' where id=" + id + "");
            }
            this.lastModify = lastModify;
        }
    }

    private int cid;

    /**
  *   @hibernate.property
  */
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        mydb.executeUpdate("update entity set cid=" + cid + " where id=" + id + "");
        if (this.cid == 0) {
            mydb.executeUpdate("delete from $" + cid + " where id=" + id);
            mydb.executeUpdate("insert into $" + cid + "(id,cid,wtime,lastModify)values(" + id + "," + cid + ",now(),now())");
        }
        this.cid = cid;
    }

    private int id;

    /**
  *   @hibernate.id
  *     generator-class="increment"
  */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        init();
    }

    private void init() {
        List list = mydb.executeQuery("select * from entity where id=" + id + " limit 0,1");
        if (list.isEmpty()) throw new Error("The " + id + " is not exist!");
        Map v = (Map) list.get(0);
        if (v.get("cid") != null) this.cid = Integer.parseInt(v.get("cid").toString());
    }
}
