package test.org.nakedobjects.object.reflect;

import java.util.Vector;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.nof.reflect.spec.OneToOneAssociationImpl;
import org.nakedobjects.nof.reflect.spec.ValueAssociationImpl;
import org.nakedobjects.testing.DummyNakedObject;
import org.nakedobjects.testing.DummyNakedValue;
import org.nakedobjects.testing.DummyOid;
import org.nakedobjects.testing.TestSpecification;
import org.nakedobjects.testing.TestSystem;
import test.org.nakedobjects.object.reflect.defaults.TestValue;

/**
 * Creates a all the objects (Pojo, NakedObject, NakedObjectSpecification etc) for use in a test. By creating
 * a set of inter-related builders you can simply create the necessary graphs and all the needed suipporting
 * objects.
 */
public class TestObjectBuilder {

    private final Object pojo;

    private DummyNakedObject adapter;

    private TestSpecification specification;

    private DummyOid oid;

    private ResolveState resolveState;

    private final Vector fieldNames;

    private final Vector fieldContents;

    public TestObjectBuilder(final Object pojo) {
        this.pojo = pojo;
        resolveState = ResolveState.NEW;
        fieldNames = new Vector();
        fieldContents = new Vector();
    }

    public void setReferenceField(final String name, final TestObjectBuilder reference) {
        fieldNames.addElement(name);
        fieldContents.addElement(reference);
    }

    public void setResolveState(final ResolveState resolveState) {
        this.resolveState = resolveState;
    }

    public void init(final TestSystem system) {
        init(system, new Vector());
    }

    private void init(final TestSystem system, final Vector previous) {
        String className = pojo.getClass().getName();
        if (specification == null) {
            specification = new TestSpecification(className);
        }
        if (adapter == null) {
            adapter = new DummyNakedObject(pojo.toString());
        }
        adapter.setupResolveState(resolveState);
        adapter.setupObject(pojo);
        adapter.setupSpecification(specification);
        if (oid != null) {
            system.addRecreatedAdapterToObjectLoader(oid, adapter);
        }
        system.addSpecificationToLoader(specification);
        NakedObjectField[] fields = new NakedObjectField[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            String name = (String) fieldNames.elementAt(i);
            Object content = fieldContents.elementAt(i);
            if (content instanceof TestObjectBuilder) {
                TestObjectBuilder testObjectSpec = ((TestObjectBuilder) content);
                if (!previous.contains(testObjectSpec)) {
                    previous.addElement(testObjectSpec);
                    testObjectSpec.init(system, previous);
                    fields[i] = new OneToOneAssociationImpl(name, testObjectSpec.specification, new TestPojoReferencePeer());
                    adapter.setupFieldValue(name, testObjectSpec.getAdapter());
                }
            } else if (content instanceof TestValue) {
                TestSpecification valueFieldSpec = new TestSpecification();
                valueFieldSpec.setupIsValue();
                fields[i] = new ValueAssociationImpl(name, valueFieldSpec, new TestPojoValuePeer());
                adapter.setupFieldValue(name, new DummyNakedValue());
            }
        }
        specification.setupFields(fields);
    }

    public void setSpecification(final TestSpecification specification) {
        this.specification = specification;
    }

    public void setAdapter(final DummyNakedObject adapter) {
        this.adapter = adapter;
    }

    public void setOid(final DummyOid oid) {
        this.oid = oid;
    }

    public static void main(final String[] args) {
        TestObjectBuilder obj;
        obj = new TestObjectBuilder(new TestPojo());
        obj.setOid(new DummyOid(123));
        TestValue value = new TestValue(new TestPojoValuePeer());
        obj.setValueField("value", value);
        TestObjectBuilder referencedObject;
        referencedObject = new TestObjectBuilder(new TestPojo());
        referencedObject.setOid(new DummyOid(345));
        obj.setReferenceField("reference", referencedObject);
        TestSystem system = new TestSystem();
        system.init();
        obj.init(system);
    }

    /**
     * Sets up a fields with the specified value. The specification for the field is also added to the
     * parent's spec.
     */
    public void setValueField(final String name, final TestValue value) {
        fieldNames.addElement(name);
        fieldContents.addElement(value);
    }

    public NakedObject getAdapter() {
        return adapter;
    }

    public Object getPojo() {
        return pojo;
    }
}
