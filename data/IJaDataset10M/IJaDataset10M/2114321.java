package org.nakedobjects.distribution;

import org.nakedobjects.distribution.dummy.DummyObjectData;
import org.nakedobjects.distribution.dummy.DummyValueData;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.ResolveState;
import org.nakedobjects.object.defaults.NullDirtyObjectSet;
import org.nakedobjects.object.persistence.NullVersion;
import org.nakedobjects.testing.DummyNakedObject;
import org.nakedobjects.testing.DummyOid;
import org.nakedobjects.testing.TestOneToOneAssociation;
import org.nakedobjects.testing.TestSpecification;
import org.nakedobjects.testing.TestSystem;
import org.nakedobjects.testing.TestValueAssociation;
import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import test.org.nakedobjects.object.reflect.DummyVersion;
import test.org.nakedobjects.object.reflect.TestPojo;

public class ObjectDecoderTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ObjectDecoderTest.class);
    }

    private TestSystem system;

    protected void setUp() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);
        ObjectDecoder.setUpdateNotifer(new NullDirtyObjectSet());
        system = new TestSystem();
        system.addSpecification(setupSpec());
        system.addSpecification(new TestSpecification("referenced") {

            public Object newInstance() {
                return new StringBuffer("hello");
            }
        });
        system.init();
    }

    private TestSpecification setupSpec() {
        TestSpecification testSpecification;
        testSpecification = new TestSpecification(TestPojo.class) {

            public Object newInstance() {
                return new TestPojo();
            }
        };
        NakedObjectField[] fields = new NakedObjectField[2];
        TestSpecification valueFieldSpec = new TestSpecification();
        valueFieldSpec.setupIsValue();
        fields[1] = new TestValueAssociation(TestPojo.class.getName(), "field-2", valueFieldSpec);
        fields[0] = new TestOneToOneAssociation(TestPojo.class.getName(), "field-1", testSpecification);
        testSpecification.setupFields(fields);
        return testSpecification;
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testRecreatedObjectIsPartResolved() {
        DummyOid oid = new DummyOid(123, true);
        Data fields[] = new Data[0];
        ObjectData data = new DummyObjectData(oid, TestPojo.class.getName(), false, new DummyVersion());
        data.setFieldContent(fields);
        NakedObject naked = (NakedObject) ObjectDecoder.restore(data);
        assertEquals(ResolveState.PART_RESOLVED, ((NakedObject) naked).getResolveState());
    }

    public void testRecreatedObjectIsResolved() {
        DummyOid oid = new DummyOid(123, true);
        Data fields[] = new Data[0];
        ObjectData data = new DummyObjectData(oid, TestPojo.class.getName(), true, new DummyVersion());
        data.setFieldContent(fields);
        NakedObject naked = (NakedObject) ObjectDecoder.restore(data);
        assertEquals(ResolveState.RESOLVED, ((NakedObject) naked).getResolveState());
    }

    public void testRecreateObjectWithFieldData() {
        Data fields[] = new Data[2];
        fields[0] = new DummyObjectData(new DummyOid(456, true), TestPojo.class.getName(), false, new DummyVersion(6));
        fields[1] = new DummyValueData(new Integer(13), "");
        ObjectData data = new DummyObjectData(new DummyOid(123, true), TestPojo.class.getName(), true, new DummyVersion(4));
        data.setFieldContent(fields);
        DummyNakedObject restored = (DummyNakedObject) ObjectDecoder.restore(data);
        assertEquals(new DummyOid(123, true), restored.getOid());
        assertEquals(new DummyVersion(4), restored.getVersion());
        assertEquals(ResolveState.RESOLVED, restored.getResolveState());
        assertEquals(new Integer(13), restored.getSpecification().getField("field-2").get(restored).getObject());
        NakedObject field1 = (NakedObject) restored.getSpecification().getField("field-1").get(restored);
        assertEquals(new DummyOid(456, true), field1.getOid());
        assertEquals(null, field1.getVersion());
        assertEquals(ResolveState.GHOST, field1.getResolveState());
        TestPojo pojo = (TestPojo) restored.getObject();
        assertEquals(TestPojo.class, pojo.getClass());
    }

    public void testRecreateObjectWithRecursiveFieldData() {
        Data fields[] = new Data[2];
        ObjectData data = new DummyObjectData(new DummyOid(123, true), TestPojo.class.getName(), true, new DummyVersion(4));
        fields[0] = data;
        fields[1] = new DummyValueData(new Integer(13), "");
        data.setFieldContent(fields);
        DummyNakedObject restored = (DummyNakedObject) ObjectDecoder.restore(data);
        assertEquals(new DummyOid(123, true), restored.getOid());
        assertEquals(new DummyVersion(4), restored.getVersion());
        assertEquals(ResolveState.RESOLVED, restored.getResolveState());
        assertEquals(new Integer(13), restored.getSpecification().getField("field-2").get(restored).getObject());
        assertEquals(restored, restored.getSpecification().getField("field-1").get(restored));
        TestPojo pojo = (TestPojo) restored.getObject();
        assertEquals(TestPojo.class, pojo.getClass());
    }

    public void testRecreateObjectWithEmptyFieldData() {
        Data fields[] = new Data[2];
        fields[1] = new DummyValueData(new Integer(13), "");
        ObjectData data = new DummyObjectData(new DummyOid(123, true), TestPojo.class.getName(), false, new DummyVersion(4));
        data.setFieldContent(fields);
        DummyNakedObject restored = (DummyNakedObject) ObjectDecoder.restore(data);
        assertEquals(new DummyVersion(4), restored.getVersion());
        assertEquals(new DummyOid(123, true), restored.getOid());
        assertEquals(new Integer(13), restored.getSpecification().getField("field-2").get(restored).getObject());
        TestPojo pojo = (TestPojo) restored.getObject();
        assertEquals(TestPojo.class, pojo.getClass());
        assertEquals(null, pojo.getReference());
        assertEquals(0, pojo.getCollection().size());
        assertEquals(ResolveState.PART_RESOLVED, restored.getResolveState());
    }

    public void testRecreateObjectWithNoFieldData() {
        Data data = new DummyObjectData(new DummyOid(123, true), TestPojo.class.getName(), false, new NullVersion());
        NakedObject naked = (NakedObject) ObjectDecoder.restore(data);
        assertEquals(TestPojo.class, naked.getObject().getClass());
        assertEquals("no field data therefore only passing across reference", null, naked.getVersion());
        assertEquals(new DummyOid(123, true), naked.getOid());
        assertEquals(ResolveState.GHOST, ((NakedObject) naked).getResolveState());
    }

    public void testRecreateTransientObjectGivenDataObject() {
        Data data = new DummyObjectData(new DummyOid(1), TestPojo.class.getName(), false, new DummyVersion());
        NakedObject naked = (NakedObject) ObjectDecoder.restore(data);
        assertEquals("no field data therefore only passing across reference", null, naked.getVersion());
        assertEquals(new DummyOid(1), naked.getOid());
        assertEquals(ResolveState.TRANSIENT, naked.getResolveState());
    }

    public void testRecreateTransientObjectWithFieldData() {
        ObjectData data = new DummyObjectData(new DummyOid(1), TestPojo.class.getName(), false, new DummyVersion());
        data.setFieldContent(new Data[0]);
        NakedObject naked = (NakedObject) ObjectDecoder.restore(data);
        assertEquals("transient objects have no version number", null, naked.getVersion());
        assertEquals(new DummyOid(1), naked.getOid());
        assertEquals(ResolveState.TRANSIENT, naked.getResolveState());
    }

    public void testRecreateValue() {
        Data data = new DummyValueData(new Integer(11), "");
        Naked naked = ObjectDecoder.restore(data);
        assertEquals(new Integer(11), naked.getObject());
    }
}
