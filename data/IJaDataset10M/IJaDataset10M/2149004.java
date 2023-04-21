package org.neodatis.odb.test.server.trigger;

import org.neodatis.odb.DatabaseId;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.tool.IOUtil;

public class TestServerWithTrigger extends ODBTest {

    public static String BASE_NAME = "server-trigger.neodatis";

    private int PORT = 10001;

    public void testInsert() throws Exception {
        IOUtil.deleteFile("server-trigger");
        ODBServer server = ODBFactory.openServer(PORT);
        server.addBase(BASE_NAME, "server-trigger");
        MyInsertTrigger insertTrigger = new MyInsertTrigger();
        MyInsertTriggerAllClasses insertTriggerAllClasses = new MyInsertTriggerAllClasses();
        server.addInsertTrigger(BASE_NAME, Function.class.getName(), insertTrigger);
        server.addInsertTrigger(BASE_NAME, null, insertTriggerAllClasses);
        server.startServer(true);
        Thread.sleep(300);
        ODB odb = ODBFactory.openClient("localhost", PORT, BASE_NAME);
        odb.store(new Function("Function 1"));
        odb.close();
        server.close();
        assertEquals(1, insertTrigger.getNbInsertsBefore());
        assertEquals(1, insertTrigger.getNbInsertsAfter());
        assertEquals(1, insertTriggerAllClasses.getNbInsertsBefore());
        assertEquals(1, insertTriggerAllClasses.getNbInsertsAfter());
    }

    public void testInsert2() throws Exception {
        IOUtil.deleteFile("server-trigger");
        ODBServer server = ODBFactory.openServer(PORT);
        server.addBase(BASE_NAME, "server-trigger");
        MyInsertTrigger insertTrigger = new MyInsertTrigger();
        MyInsertTriggerAllClasses insertTriggerAllClasses = new MyInsertTriggerAllClasses();
        server.addInsertTrigger(BASE_NAME, Profile.class.getName(), insertTrigger);
        server.addInsertTrigger(BASE_NAME, null, insertTriggerAllClasses);
        server.startServer(true);
        Thread.sleep(300);
        ODB odb = ODBFactory.openClient("localhost", PORT, BASE_NAME);
        Profile profile = new Profile("Profile name", new Function("F1"));
        odb.store(profile);
        odb.close();
        server.close();
        assertEquals(1, insertTrigger.getNbInsertsBefore());
        assertEquals(1, insertTrigger.getNbInsertsAfter());
        assertEquals(2, insertTriggerAllClasses.getNbInsertsBefore());
        assertEquals(2, insertTriggerAllClasses.getNbInsertsAfter());
    }

    public void testInsertToGetExternalOid() throws Exception {
        IOUtil.deleteFile("server-trigger");
        ODBServer server = ODBFactory.openServer(PORT);
        server.addBase(BASE_NAME, "server-trigger");
        server.startServer(true);
        Thread.sleep(100);
        ODB odb = ODBFactory.openClient("localhost", PORT, BASE_NAME);
        DatabaseId databaseId = odb.ext().getDatabaseId();
        odb.close();
        println(databaseId);
        odb = ODBFactory.openClient("localhost", PORT, BASE_NAME);
        SessionEngine engine = Dummy.getEngine(odb);
        DatabaseId databaseId2 = engine.getSession().getDatabaseId();
        odb.close();
        println(databaseId2);
        assertEquals(databaseId, databaseId2);
        ReplicationInsertTrigger replicationInsertTrigger = new ReplicationInsertTrigger();
        server.addInsertTrigger(BASE_NAME, Profile.class.getName(), replicationInsertTrigger);
        odb = ODBFactory.openClient("localhost", PORT, BASE_NAME);
        Profile profile = new Profile("Profile name", new Function("F1"));
        odb.store(profile);
        odb.close();
        server.close();
        assertEquals(1, replicationInsertTrigger.getNbInsertsAfter());
    }

    public void testUpdate() throws Exception {
        IOUtil.deleteFile("server-trigger");
        int port = PORT + 3;
        ODBServer server = ODBFactory.openServer(port);
        server.addBase(BASE_NAME, "server-trigger");
        MyUpdateTrigger updateTrigger = new MyUpdateTrigger();
        MyUpdateTriggerAllClasses updateTriggerAllClasses = new MyUpdateTriggerAllClasses();
        server.addUpdateTrigger(BASE_NAME, Function.class.getName(), updateTrigger);
        server.addUpdateTrigger(BASE_NAME, null, updateTriggerAllClasses);
        server.startServer(true);
        Thread.sleep(300);
        ODB odb = ODBFactory.openClient("localhost", port, BASE_NAME);
        odb.store(new Function("Function 1"));
        odb.close();
        odb = ODBFactory.openClient("localhost", port, BASE_NAME);
        Function f = (Function) odb.getObjects(Function.class).getFirst();
        f.setName("new name");
        odb.store(f);
        odb.close();
        server.close();
        assertEquals(1, updateTrigger.getNbUpdatesBefore());
        assertEquals(1, updateTrigger.getNbUpdatesAfter());
        assertEquals(1, updateTriggerAllClasses.getNbUpdatesBefore());
        assertEquals(1, updateTriggerAllClasses.getNbUpdatesAfter());
    }

    public void testDelete() throws Exception {
        IOUtil.deleteFile("server-trigger");
        int port = PORT + 3;
        ODBServer server = ODBFactory.openServer(port);
        server.addBase(BASE_NAME, "server-trigger");
        MyDeleteTrigger deleteTrigger = new MyDeleteTrigger();
        MyDeleteTriggerForAllClasses deleteTriggerAllClasses = new MyDeleteTriggerForAllClasses();
        server.addDeleteTrigger(BASE_NAME, Function.class.getName(), deleteTrigger);
        server.addDeleteTrigger(BASE_NAME, null, deleteTriggerAllClasses);
        server.startServer(true);
        Thread.sleep(300);
        ODB odb = ODBFactory.openClient("localhost", port, BASE_NAME);
        odb.store(new Function("Function 1"));
        odb.close();
        odb = ODBFactory.openClient("localhost", port, BASE_NAME);
        Function f = (Function) odb.getObjects(Function.class).getFirst();
        odb.delete(f);
        odb.close();
        server.close();
        assertEquals(1, deleteTrigger.getNbDeletesBefore());
        assertEquals(1, deleteTrigger.getNbDeletesAfter());
        assertEquals(1, deleteTriggerAllClasses.getNbDeletesBefore());
        assertEquals(1, deleteTriggerAllClasses.getNbDeletesAfter());
    }

    /**
	 * Uses server side trigger to build a simple replication mechanism
	 * 
	 * @throws Exception
	 */
    public void testSimpleReplication() throws Exception {
        String baseName = getBaseName();
        String replicatedBaseName = "replicated-" + getBaseName();
        int port = PORT + 1;
        ODBServer server = ODBFactory.openServer(port);
        server.addBase(baseName, baseName);
        server.startServer(true);
        Thread.sleep(100);
        ODB replicatedODB = ODBFactory.open(replicatedBaseName);
        RealReplicationInsertTrigger replicationInsertTrigger = new RealReplicationInsertTrigger(replicatedODB);
        server.addInsertTrigger(baseName, null, replicationInsertTrigger);
        ODB odb = ODBFactory.openClient("localhost", port, baseName);
        Profile profile = new Profile("Profile name", new Function("F1"));
        odb.store(profile);
        odb.close();
        server.close();
        replicatedODB.close();
        assertEquals(2, replicationInsertTrigger.getNbInsertsAfter());
        replicatedODB = ODBFactory.open(replicatedBaseName);
        Objects profiles = replicatedODB.getObjects(Profile.class);
        Objects functions = replicatedODB.getObjects(Function.class);
        replicatedODB.close();
        assertEquals(1, profiles.size());
        assertEquals(1, functions.size());
    }

    /**
	 * Uses server side trigger to build a simple replication mechanism
	 * 
	 * @throws Exception
	 */
    public void testSimpleReplicationOneFunction() throws Exception {
        String baseName = getBaseName();
        String replicatedBaseName = "replicated-" + getBaseName();
        int port = PORT + 1;
        ODBServer server = ODBFactory.openServer(port);
        server.addBase(baseName, baseName);
        server.startServer(true);
        Thread.sleep(100);
        ODB replicatedODB = ODBFactory.open(replicatedBaseName);
        RealReplicationInsertTrigger replicationInsertTrigger = new RealReplicationInsertTrigger(replicatedODB);
        server.addInsertTrigger(baseName, null, replicationInsertTrigger);
        ODB odb = ODBFactory.openClient("localhost", port, baseName);
        Function f = new Function("F1");
        odb.store(f);
        odb.close();
        server.close();
        replicatedODB.close();
        assertEquals(1, replicationInsertTrigger.getNbInsertsAfter());
        replicatedODB = ODBFactory.open(replicatedBaseName);
        Objects functions = replicatedODB.getObjects(Function.class);
        replicatedODB.close();
        assertEquals(1, functions.size());
        Function f2 = (Function) functions.getFirst();
        assertEquals("F1", f2.getName());
    }

    public void testSelectTrigger() throws Exception {
        ODB odb = null;
        MyServerSelectTrigger myTrigger = new MyServerSelectTrigger();
        ODBServer server = null;
        String baseName = getBaseName();
        try {
            server = ODBFactory.openServer(PORT);
            server.addBase(baseName, DIRECTORY + baseName);
            server.startServer(true);
            Thread.sleep(100);
            odb = ODBFactory.openClient("localhost", PORT, baseName);
            Function f1 = new Function("function1");
            Function f2 = new Function("function2");
            Profile profile = new Profile("profile1", f1);
            User user = new User("oli", "oli@neodatis.com", profile);
            odb.store(user);
            odb.store(f2);
        } finally {
            if (odb != null) {
                odb.close();
            }
        }
        odb = ODBFactory.openClient("localhost", PORT, baseName);
        try {
            odb.addSelectTrigger(Function.class, myTrigger);
            fail("Should have thrown exception because of associating server trigger to local or client odb");
        } catch (Exception e) {
        }
        server.addSelectTrigger(baseName, Function.class.getName(), myTrigger);
        Objects<Function> functions = odb.getObjects(Function.class);
        odb.close();
        server.close();
        assertEquals(2, functions.size());
        assertEquals(2, myTrigger.nbCalls);
    }

    public void testSelectTrigger2() throws Exception {
        ODB odb = null;
        MyServerSelectTrigger myTrigger = new MyServerSelectTrigger();
        ODBServer server = null;
        String baseName = getBaseName();
        try {
            server = ODBFactory.openServer(PORT);
            server.addBase(baseName, DIRECTORY + baseName);
            server.startServer(true);
            Thread.sleep(100);
            odb = ODBFactory.openClient("localhost", PORT, baseName);
            Function f1 = new Function("function1");
            Function f2 = new Function("function2");
            Profile profile = new Profile("profile1", f1);
            User user = new User("oli", "oli@neodatis.com", profile);
            odb.store(user);
            odb.store(f2);
        } finally {
            if (odb != null) {
                odb.close();
            }
        }
        odb = ODBFactory.openClient("localhost", PORT, baseName);
        try {
            odb.addSelectTrigger(Function.class, myTrigger);
            fail("Should have thrown exception because of associating server trigger to local or client odb");
        } catch (Exception e) {
        }
        server.addSelectTrigger(baseName, Function.class.getName(), myTrigger);
        Objects<Function> functions = odb.getObjects(new CriteriaQuery(Function.class, Where.equal("name", "function1")));
        odb.close();
        server.close();
        assertEquals(1, functions.size());
        assertEquals(1, myTrigger.nbCalls);
    }

    public void testSelectTrigger3() throws Exception {
        ODB odb = null;
        MyServerSelectTrigger myTrigger = new MyServerSelectTrigger();
        ODBServer server = null;
        String baseName = getBaseName();
        try {
            server = ODBFactory.openServer(PORT);
            server.addBase(baseName, DIRECTORY + baseName);
            server.startServer(true);
            Thread.sleep(100);
            odb = ODBFactory.openClient("localhost", PORT, baseName);
            Function f1 = new Function("function1");
            Function f2 = new Function("function2");
            Profile profile = new Profile("profile1", f1);
            User user = new User("oli", "oli@neodatis.com", profile);
            odb.store(user);
            odb.store(f2);
        } finally {
            if (odb != null) {
                odb.close();
            }
        }
        odb = ODBFactory.openClient("localhost", PORT, baseName);
        try {
            odb.addSelectTrigger(Function.class, myTrigger);
            fail("Should have thrown exception because of associating server trigger to local or client odb");
        } catch (Exception e) {
        }
        server.addSelectTrigger(baseName, null, myTrigger);
        Objects<User> users = odb.getObjects(new CriteriaQuery(User.class, Where.equal("name", "oli")));
        odb.close();
        server.close();
        assertEquals(1, users.size());
        assertEquals(3, myTrigger.nbCalls);
    }
}
