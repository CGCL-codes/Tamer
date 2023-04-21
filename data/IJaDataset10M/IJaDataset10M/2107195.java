package org.nakedobjects.plugins.xml.objectstore.internal.data;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;

public class ObjectDataVectorTest {

    private ObjectDataVector objectDataVector;

    private ObjectData objectData;

    private TestProxySpecification spec;

    private SerialOid oid;

    private FileVersion version;

    @Before
    public void setUp() throws Exception {
        boolean isTransient = true;
        long serialNum = Long.parseLong("1", 16);
        oid = isTransient ? SerialOid.createTransient(serialNum) : SerialOid.createPersistent(serialNum);
        spec = new TestProxySpecification(this.getClass());
        spec.fields = new NakedObjectAssociation[0];
        version = new FileVersion("", System.currentTimeMillis());
        objectData = new ObjectData(spec, oid, version);
        objectDataVector = new ObjectDataVector();
    }

    @Test
    public void validatesObjectDataIsStored() throws Exception {
        objectDataVector.addElement(objectData);
        assertTrue(objectDataVector.contains(objectData));
        assertTrue(objectDataVector.element(0).equals(objectData));
    }

    @Test
    public void validatesObjectDataVectorSize() throws Exception {
        objectDataVector.addElement(objectData);
        assertTrue(objectDataVector.size() == 1);
    }
}
