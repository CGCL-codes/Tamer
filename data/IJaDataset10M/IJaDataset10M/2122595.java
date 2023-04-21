package org.jugile.util.db;

import java.util.List;
import org.jugile.util.DBConnection;
import org.jugile.util.DBPool;
import org.jugile.util.Timer;
import org.jugile.util.U;

public class DBTest {

    public static void main(String args[]) throws Exception {
        U.print("hello");
        DBPool pool = DBPool.getPool();
        DBConnection c = pool.getConnection();
        c.update("drop table if exists test");
        String sql = "create table test(" + " name varchar(20), " + " age int, " + " PRIMARY KEY (name), " + " KEY I_dtable_name (age) " + ")";
        c.update(sql);
        c.update("insert into test values ('jukka',38)");
        c.update("alter table test add tel varchar(30)");
        c.update("insert into test values ('pekka',40,'040 2233333')");
        for (List row : c.select("select name,age from test")) {
            String name = (String) row.get(0);
            Integer age = (Integer) row.get(1);
            U.print("name: " + name + " age: " + age);
        }
        Timer t = new Timer();
        for (int i = 0; i < 10000; i++) {
            c.updateN("insert into test values('test" + i + "'," + i + ",'')");
        }
        c.commit();
        U.print("added 10000 rows: " + t.stop());
        t = new Timer();
        c.prepare("insert into test values(?,?,'')");
        for (int i = 10000; i < 20000; i++) {
            c.param("test" + i);
            c.param(i);
            c.execute();
        }
        c.commit();
        U.print("added another 10000 rows: " + t.stop());
        t = new Timer();
        c.prepare("insert into test (name, age) values(?,?)");
        for (int i = 20000; i < 30000; i++) {
            c.param("test" + i);
            c.param(i);
            c.execute();
        }
        c.commit();
        U.print("added yet another 10000 rows: " + t.stop());
        c.free();
        U.print("done");
    }
}
