package org.neodatis.odb.test.session;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.arraycollectionmap.PlayerWithList;
import org.neodatis.odb.test.vo.login.Function;

public class TestSession extends ODBTest {

    public void test1() throws Exception {
        deleteBase("session.neodatis");
        ODB odb = open("session.neodatis");
        odb.close();
        ODB odb2 = open("session.neodatis");
        Objects l = odb2.getObjects(PlayerWithList.class, true);
        assertEquals(0, l.size());
        odb2.close();
        deleteBase("session.neodatis");
    }

    public void test2() throws Exception {
        deleteBase("session.neodatis");
        ODB odb = open("session.neodatis");
        Function f = new Function("f1");
        odb.store(f);
        odb.commit();
        f.setName("f1 -1");
        odb.store(f);
        odb.close();
        odb = open("session.neodatis");
        Objects os = odb.getObjects(Function.class);
        assertEquals(1, os.size());
        Function f2 = (Function) os.getFirst();
        odb.close();
        deleteBase("session.neodatis");
        assertEquals("f1 -1", f2.getName());
    }
}
