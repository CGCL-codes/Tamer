package com.mainatom.db;

import com.mainatom.af.*;
import com.mainatom.tab.*;
import com.mainatom.testutils.*;
import junit.framework.*;

public class Jdbc_databaseTest extends TestCase {

    DbTestUtils tu;

    ADataBase db;

    protected void setUp() throws Exception {
        super.setUp();
        tu = new DbTestUtils(this, AApp.class);
        db = tu.connect("_database_ini.xml", "test");
    }

    public void testLoadTables() throws Exception {
        tu.logOn();
        ListAObject<ATable> lst = db.getTables();
        assertEquals(lst.size() > 4, true);
        ATable t = lst.get("tab1");
        assertEquals(t.getCountFields() > 4, true);
    }

    public void testGenerateSql() throws Exception {
        assertEquals(db.generateSqlInsert("", "tab3", true), "insert into tab3(id,f1,f2,tab2ref) values (:{id},:{f1},:{f2},:{tab2ref})");
        assertEquals(db.generateSqlInsert("", "tab3", false), "insert into tab3(f1,f2,tab2ref) values (:{f1},:{f2},:{tab2ref})");
        assertEquals(db.generateSqlUpdate("", "tab3"), "update tab3 set f1=:{f1},f2=:{f2},tab2ref=:{tab2ref} where id=:{id}");
        assertEquals(db.generateSqlDelete("", "tab3"), "delete from tab3 where id=:{id}");
    }
}
