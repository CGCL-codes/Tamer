package org.nakedobjects.xmlpersistence.objectstore.internal.data.xml;

import java.io.File;
import java.io.FilenameFilter;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.nakedobjects.xmlpersistence.objectstore.internal.clock.TestClock;
import org.nakedobjects.xmlpersistence.objectstore.internal.data.ObjectData;
import org.nakedobjects.xmlpersistence.objectstore.internal.data.ReferenceVector;
import org.nakedobjects.xmlpersistence.objectstore.internal.data.Role;
import org.nakedobjects.xmlpersistence.objectstore.internal.data.Team;
import org.nakedobjects.xmlpersistence.objectstore.internal.version.FileVersion;

public class XmlDataManagerTest extends ProxyJunit3TestCase {

    protected XmlDataManager manager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FileVersion.setClock(new TestClock());
        clearTestDirectory();
        manager = new XmlDataManager(new XmlFile(system.getConfiguration(), "tmp/tests"));
    }

    @Override
    protected void tearDown() throws Exception {
        system.shutdown();
    }

    protected static void clearTestDirectory() {
        final File directory = new File("tmp" + File.separator + "tests");
        final String[] files = directory.list(new FilenameFilter() {

            public boolean accept(final File arg0, final String name) {
                return name.endsWith(".xml");
            }
        });
        if (files != null) {
            for (int f = 0; f < files.length; f++) {
                new File(directory, files[f]).delete();
            }
        }
    }

    public void testCreateOid() throws Exception {
        SerialOid oid = manager.createOid();
        final long start = oid.getSerialNo();
        long next = start + 1;
        for (int i = 0; i < 3; i++) {
            oid = manager.createOid();
            assertEquals(next++, oid.getSerialNo());
        }
    }

    public void testWriteReadTypeOidAndVersion() {
        final ObjectData data = createData(Role.class, 99, new FileVersion("user", 19));
        manager.insertObject(data);
        final ObjectData read = manager.loadObjectData(data.getOid());
        assertEquals(data.getOid(), read.getOid());
        assertEquals(data.getClassName(), read.getClassName());
        assertEquals(data.getVersion(), read.getVersion());
    }

    public void testNextId() throws Exception {
        final long first = manager.nextId();
        assertEquals(first + 1, manager.nextId());
        assertEquals(first + 2, manager.nextId());
        assertEquals(first + 3, manager.nextId());
    }

    public void testInsertObjectWithFields() throws ObjectPersistenceException {
        final ObjectData data = createData(Role.class, 99, new FileVersion("user", 13));
        data.set("Person", SerialOid.createPersistent(101));
        assertNotNull(data.get("Person"));
        data.set("Name", "Harry");
        assertNotNull(data.get("Name"));
        manager.insertObject(data);
        final ObjectData read = manager.loadObjectData(data.getOid());
        assertEquals(data.getOid(), read.getOid());
        assertEquals(data.getClassName(), read.getClassName());
        assertEquals(data.get("Person"), read.get("Person"));
        assertEquals(data.get("Name"), read.get("Name"));
    }

    public void testInsertObjectWithEmptyOneToManyAssociations() throws ObjectPersistenceException {
        final ObjectData data = createData(Team.class, 99, new FileVersion("user", 13));
        data.initCollection("Members");
        manager.insertObject(data);
        final ObjectData read = manager.loadObjectData(data.getOid());
        assertEquals(data.getOid(), read.getOid());
        assertEquals(data.getClassName(), read.getClassName());
        final ReferenceVector c = read.elements("Members");
        assertNull(c);
    }

    public void testInsertObjectWithOneToManyAssociations() throws ObjectPersistenceException {
        final ObjectData data = createData(Team.class, 99, new FileVersion("user", 13));
        data.initCollection("Members");
        final SerialOid oid[] = new SerialOid[3];
        for (int i = 0; i < oid.length; i++) {
            oid[i] = SerialOid.createPersistent(104 + i);
            data.addElement("Members", oid[i]);
        }
        manager.insertObject(data);
        final ObjectData read = manager.loadObjectData(data.getOid());
        assertEquals(data.getOid(), read.getOid());
        assertEquals(data.getClassName(), read.getClassName());
        final ReferenceVector c = read.elements("Members");
        for (int i = 0; i < oid.length; i++) {
            assertEquals(oid[i], c.elementAt(i));
        }
    }

    private ObjectData createData(final Class<?> type, final long id, final FileVersion version) {
        final NakedObjectSpecification noSpec = NakedObjectsContext.getSpecificationLoader().loadSpecification(type);
        final SerialOid oid = SerialOid.createPersistent(id);
        return new ObjectData(noSpec, oid, version);
    }
}
