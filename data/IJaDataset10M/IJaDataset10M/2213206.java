package org.sourceforge.jemm.database.passthrough;

import java.util.HashMap;
import java.util.HashSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sourceforge.jemm.client.shared.ValueEncoder;
import org.sourceforge.jemm.database.ClassId;
import org.sourceforge.jemm.database.ClassInfo;
import org.sourceforge.jemm.database.ClientId;
import org.sourceforge.jemm.database.ClientThreadId;
import org.sourceforge.jemm.database.Database;
import org.sourceforge.jemm.database.EnumId;
import org.sourceforge.jemm.database.EnumInfo;
import org.sourceforge.jemm.database.EventComparator;
import org.sourceforge.jemm.database.FieldInfo;
import org.sourceforge.jemm.database.GetObjectResponse;
import org.sourceforge.jemm.database.ObjectState;
import org.sourceforge.jemm.database.StructureModifiedException;
import org.sourceforge.jemm.database.debug.TrackingDummyDatabaseImpl;
import org.sourceforge.jemm.lifecycle.ObjectRequest;
import org.sourceforge.jemm.lifecycle.ObjectRequestType;
import org.sourceforge.jemm.lifecycle.ObjectResponse;
import org.sourceforge.jemm.lifecycle.ValueVisitor;
import org.sourceforge.jemm.types.ID;

public abstract class PassthroughDatabaseTest {

    protected ClientId clientId = new ClientId("C1");

    ClientThreadId clientThreadId = new ClientThreadId(clientId, "1");

    ClassId classId = new ClassId(7);

    ClassInfo classInfo = new ClassInfo("com.test.Foo", new HashSet<FieldInfo>());

    EnumId enumId = new EnumId(64);

    EnumInfo enumInfo = new EnumInfo("com.test.Foo", new HashSet<String>());

    ID objId = new ID(364);

    GetObjectResponse getObjResp = new GetObjectResponse(classId, new ObjectState(objId, 1, new HashMap<FieldInfo, Object>()));

    ID rootId = new ID(45345);

    ID collectionId = new ID(157);

    /** dut = database under test */
    protected Database dutDB;

    protected EventComparator expected = new EventComparator();

    protected TrackingDummyDatabaseImpl testDB;

    @Before
    public void setup() throws Exception {
        try {
            testDB = new TrackingDummyDatabaseImpl();
            initialiseDUT();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public abstract void initialiseDUT() throws Exception;

    @Test
    public void testGetClassInfo() {
        testDB.getClassInfoReturn = classInfo;
        Assert.assertEquals(classInfo, dutDB.getClassInfo(clientId, classId));
        expected.add("getClassInfo", clientId, classId);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testGetEnumInfo() {
        testDB.getEnumInfoReturn = enumInfo;
        Assert.assertEquals(enumInfo, dutDB.getEnumInfo(clientId, enumId));
        expected.add("getEnumInfo", clientId, enumId);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testGetObject() {
        testDB.getObjectResponseReturn = getObjResp;
        Assert.assertEquals(getObjResp, dutDB.getObject(clientId, objId));
        expected.add("getObject", clientId, objId);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testGetRoot() {
        testDB.getRootReturn = rootId;
        Assert.assertEquals(rootId, dutDB.getRoot(clientId, "root1"));
        expected.add("getRoot", clientId, "root1");
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testNewObject() {
        testDB.newObjectReturn = objId;
        Assert.assertEquals(objId, dutDB.newObject(clientId, classId, null));
        expected.add("newObject", clientId, classId, null);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testNewType() {
        testDB.newObjectReturn = objId;
        Object[] args = { "ABC" };
        Assert.assertEquals(objId, dutDB.newObject(clientId, classId, args));
        expected.add("newObject", clientId, classId, args);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testReferenceCleared() {
        dutDB.referenceCleared(clientId, objId);
        expected.add("referenceCleared", clientId, new ID[] { objId });
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testReferencesCleared() {
        ID objId2 = new ID(65);
        dutDB.referenceCleared(clientId, objId, objId2);
        expected.add("referenceCleared", clientId, new ID[] { objId, objId2 });
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testRegisterClass() throws Exception {
        testDB.registerClassReturn = classId;
        Assert.assertEquals(classId, dutDB.registerClass(clientId, classInfo));
        expected.add("registerClass", clientId, classInfo);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testRegisterEnum() throws StructureModifiedException {
        testDB.registerEnumReturn = enumId;
        Assert.assertEquals(enumId, dutDB.registerEnum(clientId, enumInfo));
        expected.add("registerEnum", clientId, enumInfo);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testReleaseLock() {
        dutDB.releaseLock(clientThreadId, objId);
        expected.add("releaseLock", clientThreadId, objId);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testAcquireLock() {
        dutDB.acquireLock(clientThreadId, objId);
        expected.add("acquireLock", clientThreadId, objId);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testRemoveLockAcquireListener() {
        MonitoringLockListener listener = new MonitoringLockListener();
        dutDB.setClientLockAcquiredListener(clientId, listener);
        testDB.triggerLockAquireEvent(clientThreadId, new ID(1));
        listener.checkForCount(1);
        dutDB.removeLockAcquiredListener(clientId);
        testDB.triggerLockAquireEvent(clientThreadId, new ID(1));
        listener.checkForCount(1);
    }

    @Test
    public void testSetClientLockAcquiredListener() throws Exception {
        MonitoringLockListener listener = new MonitoringLockListener();
        dutDB.setClientLockAcquiredListener(clientId, listener);
        testDB.triggerLockAquireEvent(clientThreadId, new ID(1));
        listener.checkForCount(1);
    }

    @Test
    public void testSetRoot() {
        dutDB.setRoot(clientId, "testRoot", objId);
        expected.add("setRoot", clientId, "testRoot", objId);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testGetDebugIF() {
        Assert.assertNull(dutDB.getDebugInterface());
    }

    @Test
    public void testSetRootIfNull() {
        ID setId = new ID(56);
        testDB.setRootIfNullReturn = setId;
        Assert.assertEquals(setId, dutDB.setRootIfNull(clientId, "testRoot", objId));
        expected.add("setRootIfNull", clientId, "testRoot", objId);
        expected.assertMatches(testDB.seen);
    }

    @Test
    public void testClientDisconnect() {
        dutDB.clientDisconnect(clientId);
        expected.add("clientDisconnect", clientId);
        expected.assertMatches(testDB.seen);
    }

    enum TestRequestAction implements ObjectRequestType {

        TEST_ACTION
    }

    static class TestTypeRequest extends ObjectRequest<TestTypeRequest> {

        private static final long serialVersionUID = 1L;

        public final int value;

        public TestTypeRequest(int value) {
            super(TestRequestAction.TEST_ACTION);
            this.value = value;
        }

        @Override
        public TestTypeRequest encode(ValueEncoder encoder) {
            return this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            TestTypeRequest other = (TestTypeRequest) obj;
            if (value != other.value) return false;
            return true;
        }
    }

    static class TestTypeResponse extends ObjectResponse<TestTypeResponse> {

        private static final long serialVersionUID = 1L;

        public final int value;

        public TestTypeResponse(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            TestTypeResponse other = (TestTypeResponse) obj;
            if (value != other.value) return false;
            return true;
        }

        @Override
        public TestTypeResponse encode(ValueEncoder encoder) {
            return this;
        }

        @Override
        public void visit(ValueVisitor visitor) {
        }
    }

    @Test
    public void testProcessTypeRequest() {
        TestTypeRequest request = new TestTypeRequest(54);
        TestTypeResponse expectedResponse = new TestTypeResponse(23);
        testDB.processTypeRequestResponse = expectedResponse;
        ObjectResponse<?> response = dutDB.processObjectRequest(clientId, objId, request);
        expected.add("processTypeRequest", clientId, objId, request);
        expected.assertMatches(testDB.seen);
        Assert.assertNotNull(response);
        Assert.assertEquals(expectedResponse, response);
    }
}
