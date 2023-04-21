package cn.webwheel.database.test.domain;

import cn.webwheel.database.annotations.Select;
import cn.webwheel.database.annotations.Update;
import java.util.List;

public abstract class Fields {

    public String s;

    public int d;

    @Update("insert into Fields values(#this.s#, #this.d#)")
    public abstract void insert() throws Exception;

    @Select("select * from Fields")
    public abstract List<Fields> getall() throws Exception;
}
