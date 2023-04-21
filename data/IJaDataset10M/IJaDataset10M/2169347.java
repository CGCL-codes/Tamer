package org.dml.database;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.UnsupportedEncodingException;
import org.dml.JUnits.Consts;
import org.dml.database.bdb.level1.AllTupleBindings;
import org.dml.database.bdb.level1.Level1_Storage_BerkeleyDB;
import org.dml.database.bdb.level1.OneToOneDBMap;
import org.dml.error.AssumptionError;
import org.dml.error.BadCallError;
import org.dml.tools.RunTime;
import org.dml.tracking.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.references.method.MethodParams;
import org.references.method.PossibleParams;
import com.sleepycat.je.DatabaseException;

/**
 * 
 *
 */
public class OneToOneDBMapTest {

    OneToOneDBMap<String, String> x;

    final String _a = "AAAAAAAAAAAAAAAAAAA";

    final String _b = "BBBBBBBBBBBBBBBBBBBBBBBBB";

    Level1_Storage_BerkeleyDB bdb;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws DatabaseException {
        MethodParams params = MethodParams.getNew();
        params.set(PossibleParams.homeDir, Consts.BDB_ENV_PATH);
        params.set(PossibleParams.jUnit_wipeDB, true);
        params.set(PossibleParams.jUnit_wipeDBWhenDone, true);
        bdb = Factory.getNewInstanceAndInit(Level1_Storage_BerkeleyDB.class, params);
        params.clear();
        params.set(PossibleParams.level1_BDBStorage, bdb);
        params.set(PossibleParams.dbName, "someMap");
        x = Factory.getNewInstanceAndInit(OneToOneDBMap.class, params, String.class, AllTupleBindings.getBinding(String.class), String.class, AllTupleBindings.getBinding(String.class));
    }

    @After
    public void tearDown() {
        Factory.deInitIfAlreadyInited(x);
        Factory.deInitIfAlreadyInited(bdb);
    }

    @Test
    public void linkTest() throws DatabaseException {
        assertFalse(x.link(_a, _b));
        assertTrue(x.getKey(_b).equals(_a));
        assertTrue(x.getData(_a).equals(_b));
        assertTrue(_a != x.getKey(_b));
        assertTrue(_b != x.getData(_a));
        assertTrue(_b.equals(x.getData(x.getKey(_b))));
        assertTrue(x.link(_a, _b));
    }

    @Test
    public void extendedTest() throws DatabaseException {
        MethodParams params = MethodParams.getNew();
        params.set(PossibleParams.level1_BDBStorage, bdb);
        params.set(PossibleParams.dbName, "extendsMap");
        @SuppressWarnings("unchecked") OneToOneDBMap<JUnit_Base1, JUnit_Base1> map = Factory.getNewInstanceAndInit(OneToOneDBMap.class, params, JUnit_Base1.class, AllTupleBindings.getBinding(JUnit_Base1.class), JUnit_Base1.class, AllTupleBindings.getBinding(JUnit_Base1.class));
        JUnit_Ex2 e = new JUnit_Ex2();
        boolean threw = false;
        try {
            map.getKey(e);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            } else {
                RunTime.throWrapped(t);
            }
        }
        assertTrue(threw);
        threw = false;
        try {
            map.getData(e);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
    }

    @Test
    public void integrityTest() throws DatabaseException {
        MethodParams params = MethodParams.getNew();
        params.set(PossibleParams.level1_BDBStorage, bdb);
        params.set(PossibleParams.dbName, "irrelevant");
        @SuppressWarnings("unchecked") OneToOneDBMap<JUnit_Base1, String> map = Factory.getNewInstanceAndInit(OneToOneDBMap.class, params, JUnit_Base1.class, AllTupleBindings.getBinding(JUnit_Base1.class), String.class, AllTupleBindings.getBinding(String.class));
        JUnit_Base1 key1 = null;
        JUnit_Ex2 key2 = null;
        String data = null;
        boolean threw = false;
        try {
            map.getData(key1);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
        threw = false;
        try {
            map.getData(key2);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
        threw = false;
        try {
            map.getKey(data);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
        data = "some";
        key1 = new JUnit_Base1();
        map.getData(key1);
        key2 = new JUnit_Ex2();
        threw = false;
        try {
            map.getData(key2);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
        map.link(key1, data);
        key1 = new JUnit_Ex2();
        threw = false;
        try {
            map.getData(key1);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
        threw = false;
        try {
            map.link(key1, data);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
        threw = false;
        try {
            map.link(key2, data);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
    }
}
