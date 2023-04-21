package org.neodatis.odb.test.ee.bdb;

import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.Objects;
import org.neodatis.odb.OdbConfiguration;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.layers.layer4.memory.InMemoryLayer4;
import org.neodatis.odb.plugins.se.bdb.NeoDatisBDBPlugin;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.tool.MemoryMonitor;

/**
 * @author olivier
 *
 */
public class TestBdb extends ODBTest {

    public void test1() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        Function function = new Function("function");
        ODB odb = open(baseName);
        ObjectOid oid = odb.store(function);
        Function f = (Function) odb.getObjectFromId(oid);
        Objects<Function> functions = odb.getObjects(Function.class);
        assertEquals(1, functions.size());
        odb.close();
        odb = open(baseName);
        functions = odb.getObjects(Function.class);
        assertEquals(1, functions.size());
        odb.close();
    }

    /** test udpate*/
    public void testUpdate1() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        Function function = new Function("function");
        ODB odb = open(baseName);
        ObjectOid oid = odb.store(function);
        Function f = (Function) odb.getObjectFromId(oid);
        Objects<Function> functions = odb.getObjects(Function.class);
        assertEquals(1, functions.size());
        f.setName("ff");
        odb.store(f);
        odb.close();
        odb = open(baseName);
        functions = odb.getObjects(Function.class);
        assertEquals(1, functions.size());
        assertEquals("ff", functions.getFirst().getName());
        odb.close();
    }

    public void testUpdate1WithClose() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        Function function = new Function("function");
        ODB odb = open(baseName);
        ObjectOid oid = odb.store(function);
        odb.close();
        odb = open(baseName);
        Function f = (Function) odb.getObjectFromId(oid);
        Objects<Function> functions = odb.getObjects(Function.class);
        assertEquals(1, functions.size());
        f.setName("ff");
        odb.store(f);
        odb.close();
        odb = open(baseName);
        functions = odb.getObjects(Function.class);
        assertEquals(1, functions.size());
        assertEquals("ff", functions.getFirst().getName());
        odb.close();
    }

    /** test udpate*/
    public void testUpdate2() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        Function function = new Function("function");
        Profile profile = new Profile("profile", function);
        ODB odb = open(baseName);
        ObjectOid oid = odb.store(profile);
        Profile p = (Profile) odb.getObjectFromId(oid);
        profile.setName("pp");
        odb.store(profile);
        odb.close();
        odb = open(baseName);
        Objects<Profile> profiles = odb.getObjects(Profile.class);
        assertEquals(1, profiles.size());
        assertEquals("pp", profiles.getFirst().getName());
        odb.close();
    }

    public void test13() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        Function function = new Function("function");
        Profile profile = new Profile("profile", function);
        ODB odb = open(baseName);
        ObjectOid oid = odb.store(profile);
        Profile p = (Profile) odb.getObjectFromId(oid);
        Objects<Function> functions = odb.getObjects(Function.class);
        Objects<Profile> profiles = odb.getObjects(Profile.class);
        assertEquals(1, functions.size());
        assertEquals(1, profiles.size());
        odb.close();
        odb = open(baseName);
        functions = odb.getObjects(Function.class);
        assertEquals(1, functions.size());
        odb.close();
    }

    public void test2() {
        int size = 100000;
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        Function function = new Function("function");
        ODB odb = open(baseName);
        OID oid = null;
        long t0 = System.currentTimeMillis();
        long t = t0;
        for (int i = 0; i < size; i++) {
            oid = odb.store(new Function("function " + (i + 1)));
            if (i % 10000 == 0) {
                long now = System.currentTimeMillis();
                MemoryMonitor.displayCurrentMemory("store i=" + i + " " + (now - t) + " cache size= " + Dummy.getEngine(odb).getSession().getCache().getSize(), false);
                t = now;
            }
        }
        odb.close();
        long t1 = System.currentTimeMillis();
        System.out.println("Total time =" + (t1 - t0));
        odb = open(baseName);
        Objects<Function> functions = odb.getObjects(Function.class, false);
        long t2 = System.currentTimeMillis();
        println(String.format("Time to retrieve %d objects, time = %d ", functions.size(), (t2 - t1)));
        int i = 0;
        while (functions.hasNext()) {
            Function f = functions.next();
            if (i % 10000 == 0) {
                long now = System.currentTimeMillis();
                t = now;
                MemoryMonitor.displayCurrentMemory("read i=" + i + " " + (now - t) + " cache size= " + Dummy.getEngine(odb).getSession().getCache().getSize(), false);
            }
            i++;
        }
        long t3 = System.currentTimeMillis();
        println(String.format("Time to actually retrieve %d objects = %d ", functions.size(), (t3 - t2)));
        odb.close();
    }

    public void test3InMemory() {
        int size = 100;
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(InMemoryLayer4.class);
        Function function = new Function("function");
        ODB odb = open(baseName);
        OID oid = null;
        long t0 = System.currentTimeMillis();
        long t = t0;
        for (int i = 0; i < size; i++) {
            oid = odb.store(new Function("function " + (i + 1)));
            if (i % 10000 == 0) {
                long now = System.currentTimeMillis();
                System.out.println(i + " - " + (now - t));
                t = now;
            }
        }
        odb.close();
        long t1 = System.currentTimeMillis();
        System.out.println("Total time =" + (t1 - t0));
        Objects<Function> functions = odb.getObjects(Function.class);
        long t2 = System.currentTimeMillis();
        println(String.format("Time to retrieve %d objects = %d ", functions.size(), (t2 - t1)));
        odb.close();
    }

    public void testDelete() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        int size = 10000;
        ODB odb = open(baseName);
        for (int i = 0; i < size; i++) {
            odb.store(new Function("function " + i));
        }
        odb.close();
        println("stored");
        odb = open(baseName);
        Objects<Function> functions = odb.getObjects(Function.class);
        assertEquals(size, functions.size());
        while (functions.hasNext()) {
            odb.delete(functions.next());
        }
        println("deleted");
        functions = odb.getObjects(Function.class);
        println("got");
        odb.close();
        assertEquals(0, functions.size());
    }

    public void testDeleteReadAfter() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        ODB odb = open(baseName);
        odb.store(new Function("function"));
        odb.close();
        odb = open(baseName);
        Objects<Function> functions = odb.getObjects(Function.class);
        odb.delete(functions.getFirst());
        odb.close();
        odb = open(baseName);
        functions = odb.getObjects(Function.class, false);
        if (functions.size() > 0) {
            Function f = functions.getFirst();
        }
        assertEquals(0, functions.size());
    }

    public void testDelete3() {
        String baseName = getBaseName();
        OdbConfiguration.getCoreProvider().setLayer4Class(NeoDatisBDBPlugin.class);
        int size = 200000;
        ODB odb = open(baseName);
        for (int i = 0; i < size; i++) {
            odb.store(new Function("function " + i));
        }
        odb.close();
        println("stored");
        odb = open(baseName);
        Objects<Function> functions = odb.getObjects(Function.class, false);
        assertEquals(size, functions.size());
        int i = 0;
        while (functions.hasNext()) {
            odb.delete(functions.next());
            if (i % 10000 == 0) {
                MemoryMonitor.displayCurrentMemory("" + i, false);
            }
            i++;
        }
        println("deleted");
        long start = System.currentTimeMillis();
        functions = odb.getObjects(Function.class, false);
        println("got is " + (System.currentTimeMillis() - start));
        odb.close();
        assertEquals(0, functions.size());
    }
}
