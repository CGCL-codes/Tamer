package cn.webwheel.database.test.domain;

import cn.webwheel.database.annotations.Include;
import cn.webwheel.database.annotations.Select;
import cn.webwheel.database.annotations.Update;

public abstract class TableA {

    private String a;

    private String b;

    public String getA() {
        return a;
    }

    @Include(false)
    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    @Include(false)
    public void setB(String b) {
        this.b = b;
    }

    @Update("insert into TableA values('aa','bb')")
    public abstract void insert() throws Exception;

    @Select("select * from TableA")
    public abstract TableA get() throws Exception;

    @Select(value = "select * from TableA", result = "-* +a")
    public abstract TableA get2() throws Exception;
}
