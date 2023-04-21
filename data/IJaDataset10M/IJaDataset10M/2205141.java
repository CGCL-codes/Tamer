package tapioca.util;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import tapioca.testing.AppEngineTestCase;
import tapioca.testing.Matchers.CollectionMatcher;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Maps;

public class EntityGroupBatchJobTest_v2 extends AppEngineTestCase {

    private final IMocksControl mockControl = EasyMock.createControl();

    private final DatastoreService mockDs = mockControl.createMock(DatastoreService.class);

    private final Transaction mockTxn = mockControl.createMock(Transaction.class);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockControl.reset();
    }

    @Override
    public void tearDown() throws Exception {
        mockControl.verify();
        super.tearDown();
    }

    private void retryableFailureTest(RuntimeException toThrow) throws Exception {
        Entity f1Eg1 = new Entity("foo", "bar");
        Entity f2Eg1 = new Entity("foo", "bar", f1Eg1.getKey());
        List<Key> eg1 = Lists.newArrayList(f1Eg1.getKey(), f2Eg1.getKey());
        Map<Key, Entity> eg1Map = Maps.newLinkedHashMap();
        eg1Map.put(f1Eg1.getKey(), f1Eg1);
        eg1Map.put(f2Eg1.getKey(), f2Eg1);
        Entity f3Eg2 = new Entity("foo", "baz");
        Entity f4Eg2 = new Entity("foo", "baz", f3Eg2.getKey());
        List<Key> eg2 = Lists.newArrayList(f3Eg2.getKey(), f4Eg2.getKey());
        Map<Key, Entity> eg2Map = Maps.newLinkedHashMap();
        eg2Map.put(f3Eg2.getKey(), f3Eg2);
        eg2Map.put(f4Eg2.getKey(), f4Eg2);
        List<Key> entities = Lists.newArrayList(f1Eg1.getKey(), f2Eg1.getKey(), f3Eg2.getKey(), f4Eg2.getKey());
        EasyMock.expect(mockDs.beginTransaction()).andReturn(mockTxn);
        EasyMock.expect(mockDs.get(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg1))).andReturn(eg1Map);
        mockDs.delete(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg1));
        EasyMock.expect(mockTxn.isActive()).andReturn(true);
        mockTxn.commit();
        EasyMock.expect(mockTxn.isActive()).andReturn(false);
        EasyMock.expect(mockDs.beginTransaction()).andReturn(mockTxn);
        EasyMock.expect(mockDs.get(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2))).andReturn(eg2Map);
        mockDs.delete(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2));
        EasyMock.expect(mockTxn.isActive()).andReturn(true);
        mockTxn.commit();
        EasyMock.expectLastCall().andThrow(toThrow);
        EasyMock.expect(mockTxn.isActive()).andReturn(false);
        EasyMock.expect(mockDs.beginTransaction()).andReturn(mockTxn);
        EasyMock.expect(mockDs.get(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2))).andReturn(eg2Map);
        mockDs.delete(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2));
        EasyMock.expect(mockTxn.isActive()).andReturn(true);
        mockTxn.commit();
        EasyMock.expect(mockTxn.isActive()).andReturn(false);
        mockControl.replay();
        EntityGroupBatchJob_v2<Key> batchJob = new EntityGroupBatchJob_v2<Key>(mockDs, entities) {

            @Override
            public Key getKeyFromItem(Key key) {
                return key;
            }

            public void processEntityGroup(Transaction txn, List<Key> entityGroup) {
                Map<Key, Entity> retrieved = mockDs.get(txn, entityGroup);
                mockDs.delete(txn, retrieved.keySet());
            }
        };
        batchJob.execute();
    }

    public void testExecute() throws Exception {
        Entity f1Eg1 = new Entity("foo", "bar");
        Entity f2Eg1 = new Entity("foo", "bar", f1Eg1.getKey());
        List<Key> eg1 = Lists.newArrayList(f1Eg1.getKey(), f2Eg1.getKey());
        Map<Key, Entity> eg1Map = Maps.newLinkedHashMap();
        eg1Map.put(f1Eg1.getKey(), f1Eg1);
        eg1Map.put(f2Eg1.getKey(), f2Eg1);
        Entity f3Eg2 = new Entity("foo", "baz");
        Entity f4Eg2 = new Entity("foo", "baz", f3Eg2.getKey());
        List<Key> eg2 = Lists.newArrayList(f3Eg2.getKey(), f4Eg2.getKey());
        Map<Key, Entity> eg2Map = Maps.newLinkedHashMap();
        eg2Map.put(f3Eg2.getKey(), f3Eg2);
        eg2Map.put(f4Eg2.getKey(), f4Eg2);
        List<Key> entities = Lists.newArrayList(f1Eg1.getKey(), f2Eg1.getKey(), f3Eg2.getKey(), f4Eg2.getKey());
        EasyMock.expect(mockDs.beginTransaction()).andReturn(mockTxn);
        EasyMock.expect(mockDs.get(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg1))).andReturn(eg1Map);
        mockDs.delete(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg1));
        EasyMock.expect(mockTxn.isActive()).andReturn(true);
        mockTxn.commit();
        EasyMock.expect(mockTxn.isActive()).andReturn(false);
        EasyMock.expect(mockDs.beginTransaction()).andReturn(mockTxn);
        EasyMock.expect(mockDs.get(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2))).andReturn(eg2Map);
        mockDs.delete(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2));
        EasyMock.expect(mockTxn.isActive()).andReturn(true);
        mockTxn.commit();
        EasyMock.expect(mockTxn.isActive()).andReturn(false);
        mockControl.replay();
        EntityGroupBatchJob_v2<Key> batchJob = new EntityGroupBatchJob_v2<Key>(mockDs, entities) {

            @Override
            public Key getKeyFromItem(Key key) {
                return key;
            }

            public void processEntityGroup(Transaction txn, List<Key> entityGroup) {
                Map<Key, Entity> retrieved = mockDs.get(txn, entityGroup);
                mockDs.delete(txn, retrieved.keySet());
            }
        };
        batchJob.execute();
    }

    public void testExecute_datastoreConcurrentModificationException() throws Exception {
        retryableFailureTest(new ConcurrentModificationException("fail"));
    }

    public void testExecute_datastoreTimeoutException() throws Exception {
        retryableFailureTest(new DatastoreTimeoutException("fail"));
    }

    public void testExecute_datastoreFailureException() throws Exception {
        retryableFailureTest(new DatastoreFailureException("fail"));
    }

    public void testExecute_unrecognizedException() throws Exception {
        RuntimeException fake = new RuntimeException("fake");
        Entity f1Eg1 = new Entity("foo", "bar");
        Entity f2Eg1 = new Entity("foo", "bar", f1Eg1.getKey());
        List<Key> eg1 = Lists.newArrayList(f1Eg1.getKey(), f2Eg1.getKey());
        Map<Key, Entity> eg1Map = Maps.newLinkedHashMap();
        eg1Map.put(f1Eg1.getKey(), f1Eg1);
        eg1Map.put(f2Eg1.getKey(), f2Eg1);
        Entity f3Eg2 = new Entity("foo", "baz");
        Entity f4Eg2 = new Entity("foo", "baz", f3Eg2.getKey());
        List<Key> eg2 = Lists.newArrayList(f3Eg2.getKey(), f4Eg2.getKey());
        Map<Key, Entity> eg2Map = Maps.newLinkedHashMap();
        eg2Map.put(f3Eg2.getKey(), f3Eg2);
        eg2Map.put(f4Eg2.getKey(), f4Eg2);
        List<Key> entities = Lists.newArrayList(f1Eg1.getKey(), f2Eg1.getKey(), f3Eg2.getKey(), f4Eg2.getKey());
        EasyMock.expect(mockDs.beginTransaction()).andReturn(mockTxn);
        EasyMock.expect(mockDs.get(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg1))).andReturn(eg1Map);
        mockDs.delete(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg1));
        EasyMock.expect(mockTxn.isActive()).andReturn(true);
        mockTxn.commit();
        EasyMock.expect(mockTxn.isActive()).andReturn(false);
        EasyMock.expect(mockDs.beginTransaction()).andReturn(mockTxn);
        EasyMock.expect(mockDs.get(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2))).andReturn(eg2Map);
        mockDs.delete(EasyMock.eq(mockTxn), CollectionMatcher.eqCollection(eg2));
        EasyMock.expect(mockTxn.isActive()).andReturn(true);
        mockTxn.commit();
        EasyMock.expectLastCall().andThrow(fake);
        EasyMock.expect(mockTxn.isActive()).andReturn(false);
        mockControl.replay();
        EntityGroupBatchJob_v2<Key> batchJob = new EntityGroupBatchJob_v2<Key>(mockDs, entities) {

            @Override
            public Key getKeyFromItem(Key key) {
                return key;
            }

            public void processEntityGroup(Transaction txn, List<Key> entityGroup) {
                Map<Key, Entity> retrieved = mockDs.get(txn, entityGroup);
                mockDs.delete(txn, retrieved.keySet());
            }
        };
        try {
            batchJob.execute();
            fail("Unknown exception should have caused this DT to exit.");
        } catch (RuntimeException e) {
            assertEquals(fake, e);
        }
    }

    public void testGatherEntityGroups() throws Exception {
        mockControl.replay();
        Entity f1 = createAndSaveDtEntity("foo", "bar");
        Entity f2 = createAndSaveDtEntity("foo", "bar");
        Entity f3 = createDtEntityWithParent("foo", "bar", f2.getKey());
        List<Entity> toProcess = Lists.newArrayList(f1, f2, f3);
        EntityGroupBatchJob_v2<Entity> job = new EntityGroupBatchJob_v2<Entity>(ds, toProcess) {

            @Override
            public Key getKeyFromItem(Entity item) {
                return item.getKey();
            }

            @Override
            public void processEntityGroup(Transaction txn, List<Entity> entityGroup) {
            }
        };
        List<List<Entity>> entityGroups = job.gatherEntityGroups();
        assertEquals(2, entityGroups.size());
        List<Entity> entityGroup = entityGroups.get(0);
        assertEquals(1, entityGroup.size());
        assertEquals(f1, entityGroup.get(0));
        entityGroup = entityGroups.get(1);
        assertEquals(2, entityGroup.size());
        assertEquals(f2, entityGroup.get(0));
        assertEquals(f3, entityGroup.get(1));
    }
}
