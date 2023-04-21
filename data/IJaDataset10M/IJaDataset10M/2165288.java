package org.objectstyle.cayenne.remote;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.objectstyle.cayenne.CayenneContext;
import org.objectstyle.cayenne.MockPersistentObject;
import org.objectstyle.cayenne.ObjectId;
import org.objectstyle.cayenne.PersistenceState;
import org.objectstyle.cayenne.Persistent;
import org.objectstyle.cayenne.QueryResponse;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.map.ObjEntity;
import org.objectstyle.cayenne.query.SelectQuery;
import org.objectstyle.cayenne.remote.ClientChannel;
import org.objectstyle.cayenne.unit.CayenneTestCase;
import org.objectstyle.cayenne.util.GenericResponse;

public class ClientChannelTst extends CayenneTestCase {

    public void testOnQuerySelect() {
        final MockPersistentObject o1 = new MockPersistentObject();
        ObjectId oid1 = new ObjectId("test_entity");
        o1.setObjectId(oid1);
        MockClientConnection connection = new MockClientConnection(new GenericResponse(Arrays.asList(new Object[] { o1 })));
        ClientChannel channel = new ClientChannel(connection);
        CayenneContext context = new CayenneContext(channel);
        ObjEntity entity = new ObjEntity("test_entity");
        entity.setClassName(MockPersistentObject.class.getName());
        DataMap dataMap = new DataMap("test");
        dataMap.addObjEntity(entity);
        Collection entities = Collections.singleton(dataMap);
        context.setEntityResolver(new EntityResolver(entities));
        QueryResponse response = channel.onQuery(context, new SelectQuery("test_entity"));
        assertNotNull(response);
        List list = response.firstList();
        assertNotNull(list);
        assertEquals(1, list.size());
        Persistent o1_1 = (Persistent) list.get(0);
        assertEquals(o1.getObjectId(), o1_1.getObjectId());
        assertEquals(context, o1_1.getObjectContext());
        assertSame(o1_1, context.getGraphManager().getNode(oid1));
    }

    public void testOnQuerySelectOverrideCached() {
        ObjEntity entity = new ObjEntity("test_entity");
        entity.setClassName(MockPersistentObject.class.getName());
        DataMap dataMap = new DataMap("test");
        dataMap.addObjEntity(entity);
        Collection entities = Collections.singleton(dataMap);
        EntityResolver resolver = new EntityResolver(entities);
        CayenneContext context = new CayenneContext();
        context.setEntityResolver(resolver);
        ObjectId oid = new ObjectId("test_entity", "x", "y");
        MockPersistentObject o1 = new MockPersistentObject(oid);
        context.getGraphManager().registerNode(oid, o1);
        assertSame(o1, context.getGraphManager().getNode(oid));
        MockPersistentObject o2 = new MockPersistentObject(oid);
        MockClientConnection connection = new MockClientConnection(new GenericResponse(Arrays.asList(new Object[] { o2 })));
        ClientChannel channel = new ClientChannel(connection);
        context.setChannel(channel);
        QueryResponse response = channel.onQuery(context, new SelectQuery("test_entity"));
        assertNotNull(response);
        List list = response.firstList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertTrue("Expected cached object, got: " + list, list.contains(o1));
        assertSame(o1, context.getGraphManager().getNode(oid));
    }

    public void testOnQuerySelectOverrideModifiedCached() {
        ObjEntity entity = new ObjEntity("test_entity");
        entity.setClassName(MockPersistentObject.class.getName());
        DataMap dataMap = new DataMap("test");
        dataMap.addObjEntity(entity);
        Collection entities = Collections.singleton(dataMap);
        EntityResolver resolver = new EntityResolver(entities);
        CayenneContext context = new CayenneContext();
        context.setEntityResolver(resolver);
        ObjectId oid = new ObjectId("test_entity", "x", "y");
        MockPersistentObject o1 = new MockPersistentObject(oid);
        o1.setPersistenceState(PersistenceState.MODIFIED);
        context.getGraphManager().registerNode(oid, o1);
        assertSame(o1, context.getGraphManager().getNode(oid));
        MockPersistentObject o2 = new MockPersistentObject(oid);
        MockClientConnection connection = new MockClientConnection(new GenericResponse(Arrays.asList(new Object[] { o2 })));
        ClientChannel channel = new ClientChannel(connection);
        context.setChannel(channel);
        QueryResponse response = channel.onQuery(context, new SelectQuery("test_entity"));
        assertNotNull(response);
        assertEquals(1, response.size());
        List list = response.firstList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertTrue("Expected cached object, got: " + list, list.contains(o1));
        assertSame(o1, context.getGraphManager().getNode(oid));
    }
}
