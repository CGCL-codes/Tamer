package test.planner;

import jminidb.JMiniDB;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import exception.CheckFailError;
import planner.Planner;

public class TestPriKeyPlanner2 extends TestPlanner {

    @Test
    public void testUpdatePriKey1() {
        clean();
        Planner p = getPlane();
        int u = p.executeUpdatePassStr("update t set t.a=20 where t.a = 10", null);
        JMiniDB.Close();
    }

    public void testUpdatePriKey2() {
        clean();
        Planner p = getPlane();
        try {
            int u = p.executeUpdatePassStr("update t set t.a=0 where t.a=10", null);
        } catch (CheckFailError e) {
            return;
        } finally {
            JMiniDB.Close();
        }
        Assert.fail();
    }

    @Test
    public void testInsertPriKey1() {
        clean();
        Planner p = getPlane();
        int u = p.executeUpdatePassStr("insert into t values(20)", null);
        JMiniDB.Close();
    }

    @Test
    public void testInsertPriKey2() {
        Planner p = getPlane();
        try {
            int u = p.executeUpdatePassStr("insert into t values(10)", null);
        } catch (CheckFailError e) {
            return;
        } finally {
            JMiniDB.Close();
        }
        Assert.fail();
    }

    @BeforeClass
    public static void clean() {
        TestPlanner.clean();
        Planner p = getPlane();
        p.executeUpdatePassStr("create table t (a int, primary key (a))", null);
        for (int i = 0; i < 12; i++) {
            p.executeUpdatePassStr("insert into t values (" + i + ")", null);
        }
        JMiniDB.Close();
    }
}
