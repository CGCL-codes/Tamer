package org.nakedobjects.xmlpersistence.objectstore.internal.data.xml;

import java.io.File;
import java.io.FilenameFilter;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.xmlpersistence.objectstore.internal.clock.TestClock;
import org.nakedobjects.xmlpersistence.objectstore.internal.data.ObjectData;
import org.nakedobjects.xmlpersistence.objectstore.internal.data.ObjectDataVector;
import org.nakedobjects.xmlpersistence.objectstore.internal.version.FileVersion;

public class XmlDataManagerInstancesTest extends ProxyJunit3TestCase {

    private static final Logger LOG = Logger.getLogger(XmlDataManagerInstancesTest.class);

    protected XmlDataManager manager;

    protected final int SIZE = 5;

    private SerialOid oids[];

    private ObjectData data[];

    private ObjectData pattern;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(XmlDataManagerInstancesTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clearTestDirectory();
        manager = new XmlDataManager(new XmlFile(system.getConfiguration(), "tmp/tests"));
        FileVersion.setClock(new TestClock());
        oids = new SerialOid[SIZE];
        data = new ObjectData[SIZE];
        final NakedObjectSpecification type = system.getSpecification(Object.class);
        pattern = new ObjectData(type, null, new FileVersion("user", 13));
        for (int i = 0; i < SIZE; i++) {
            oids[i] = SerialOid.createPersistent(i);
            data[i] = new ObjectData(type, oids[i], new FileVersion("user", 13));
            manager.insertObject(data[i]);
        }
        super.setUp();
        LOG.debug("test starting...");
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

    @Override
    protected void tearDown() throws Exception {
        system.shutdown();
        super.tearDown();
    }

    public void testNumberOfInstances() {
        assertEquals(SIZE, manager.numberOfInstances(pattern));
    }

    public void testRemove() throws Exception {
        final SerialOid oid = oids[2];
        manager.remove(oid);
        assertEquals(SIZE - 1, manager.numberOfInstances(pattern));
        final ObjectDataVector instances = manager.getInstances(pattern);
        for (int i = 0; i < instances.size(); i++) {
            assertFalse(instances.element(i) == data[2]);
        }
        assertNull(manager.loadObjectData(oid));
    }

    public void testSaveObject() throws Exception {
        data[2].set("Person", SerialOid.createPersistent(231));
        data[2].set("Name", "Fred");
        manager.save(data[2]);
        assertTrue(manager.getInstances(pattern).contains(data[2]));
        final ObjectData read = manager.loadObjectData(oids[2]);
        assertEquals(data[2], read);
        assertEquals(data[2].get("Name"), read.get("Name"));
        assertEquals(data[2].get("Person"), read.get("Person"));
    }
}
