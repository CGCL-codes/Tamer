package com.ejpmodel.orm;

import com.james.database.mydb;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 *table="ATTRIBUTE"
 *
 */
public class attribute {

    public attribute() {
        int tid = mydb.executeInsert("insert into attribute(name,sort,type,legth,lockme,listColumn,newEdit,updateEdit,position)values('init',0,'string',255,false,true,true,true,'default')");
        if (tid == 0) new NullPointerException("创建属性失败");
        setId(tid);
    }

    public attribute(int id) {
        setId(id);
    }

    public attribute(Object obj) {
        Map map = (Map) obj;
        int $id = Integer.parseInt(map.get("id").toString());
        setId($id);
    }

    private int cid;

    /**
  *   @hibernate.property
  */
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        if (this.cid != cid) {
            mydb.executeUpdate("update attribute set cid=" + cid + " where id=" + id + "");
            this.cid = cid;
            alterFiled();
        }
    }

    /**
 * 更新物理表字段
 * @return
 */
    private boolean alterFiled() {
        if (this.id != 0 && this.type != null && this.cid != 0) {
            List list = mydb.executeQuery("show columns from $" + this.cid + " like '$" + this.id + "'");
            if (list.size() > 0) {
                String sql = "";
                if (this.type.equals("text")) {
                    sql = "ALTER TABLE $" + cid + " drop `$" + this.id + "`";
                    mydb.executeUpdate(sql);
                    sql = "ALTER TABLE $" + cid + " ADD `$" + this.id + "` " + fieldDescription();
                    mydb.executeUpdate(sql);
                } else {
                    sql = "ALTER TABLE $" + cid + " MODIFY `$" + this.id + "` " + fieldDescription();
                    mydb.executeUpdate(sql);
                }
            } else {
                String sql = "ALTER TABLE $" + cid + " ADD `$" + this.id + "` " + fieldDescription();
                if (!this.type.equals("text")) sql += " ,add KEY `index_" + this.id + "` (`$" + this.id + "`)";
                mydb.executeUpdate(sql);
            }
        }
        return true;
    }

    /**
 * 返回字段描述，用于增/改物理表字段时
 * @param typie
 * @return
 */
    private String fieldDescription() {
        String result = "varchar(255) default null";
        String typie = this.getType();
        if (typie.equals("int")) result = "int default 0"; else if (typie.equals("float")) result = "float default 0"; else if (typie.equals("password")) result = "varchar(50) default ''"; else if (typie.equals("list")) result = "int default 0"; else if (typie.equals("radio")) result = "int default 0"; else if (typie.equals("checkbox")) result = "varchar(255) default ''"; else if (typie.equals("datetime")) result = "datetime default '1985-11-3 06:05:15'"; else if (typie.equals("image")) result = "varchar(100) default ''"; else if (typie.equals("file")) result = "varchar(100) default ''"; else if (typie.equals("text")) result = "text";
        return result;
    }

    private int sort;

    /**
  *   @hibernate.property
  */
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
        mydb.executeUpdate("update attribute set sort=" + sort + " where id=" + id + "");
    }

    private String name;

    /**
  *   @hibernate.property
  */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        mydb.executeUpdate("update attribute set name='" + name + "' where id=" + id + "");
    }

    private String type;

    /**
  *   @hibernate.property
  */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (!this.type.equals(type)) {
            this.type = type;
            mydb.executeUpdate("update attribute set type='" + type + "' where id=" + id + "");
            alterFiled();
        }
    }

    private int legth;

    /**
  *   @hibernate.property
  */
    public int getLegth() {
        return legth;
    }

    public void setLegth(int legth) {
        this.legth = legth;
        mydb.executeUpdate("update attribute set legth=" + legth + " where id=" + id + "");
    }

    private boolean lockme;

    /**
  *   @hibernate.property
  */
    public boolean getLockme() {
        return lockme;
    }

    public void setLockme(boolean lockme) {
        this.lockme = lockme;
        mydb.executeUpdate("update attribute set lockme=" + lockme + " where id=" + id + "");
    }

    private boolean listColumn;

    /**
  *   @hibernate.property
  */
    public boolean getListColumn() {
        return listColumn;
    }

    public void setListColumn(boolean listColumn) {
        this.listColumn = listColumn;
        mydb.executeUpdate("update attribute set listColumn=" + listColumn + " where id=" + id + "");
    }

    private boolean newEdit;

    /**
  *   @hibernate.property
  */
    public boolean getNewEdit() {
        return newEdit;
    }

    public void setNewEdit(boolean newEdit) {
        this.newEdit = newEdit;
        mydb.executeUpdate("update attribute set newEdit=" + newEdit + " where id=" + id + "");
    }

    private boolean updateEdit;

    /**
  *   @hibernate.property
  */
    public boolean getUpdateEdit() {
        return updateEdit;
    }

    public void setUpdateEdit(boolean updateEdit) {
        this.updateEdit = updateEdit;
        mydb.executeUpdate("update attribute set updateEdit=" + updateEdit + " where id=" + id + "");
    }

    private int valueList;

    /**
  *   @hibernate.property
  */
    public int getValueList() {
        return valueList;
    }

    public void setValueList(int valueList) {
        this.valueList = valueList;
        mydb.executeUpdate("update attribute set valueList=" + valueList + " where id=" + id + "");
    }

    private String defaultValue;

    /**
  *   @hibernate.property
  */
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        mydb.executeUpdate("update attribute set defaultValue='" + defaultValue + "' where id=" + id + "");
    }

    private String position;

    /**
  *   @hibernate.property
  */
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        mydb.executeUpdate("update attribute set position='" + position + "' where id=" + id + "");
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
        List list = mydb.executeQuery("select * from attribute where id=" + id + " limit 0,1");
        if (list.isEmpty()) throw new NullPointerException();
        Map v = (Map) list.get(0);
        if (v.get("name") != null) this.name = v.get("name").toString();
        if (v.get("sort") != null) this.sort = Integer.parseInt(v.get("sort").toString());
        if (v.get("type") != null) this.type = v.get("type").toString();
        if (v.get("legth") != null) this.legth = Integer.parseInt(v.get("legth").toString());
        if (v.get("lockme") != null) this.lockme = Boolean.valueOf(v.get("lockme").toString());
        if (v.get("listColumn") != null) this.listColumn = Boolean.valueOf(v.get("listColumn").toString());
        if (v.get("newEdit") != null) this.newEdit = Boolean.valueOf(v.get("newEdit").toString());
        if (v.get("updateEdit") != null) this.updateEdit = Boolean.valueOf(v.get("updateEdit").toString());
        if (v.get("valueList") != null) this.valueList = Integer.parseInt(v.get("valueList").toString());
        if (v.get("defaultValue") != null) this.defaultValue = v.get("defaultValue").toString();
        if (v.get("position") != null) this.position = v.get("position").toString();
        if (v.get("cid") != null) this.cid = Integer.parseInt(v.get("cid").toString());
    }
}
