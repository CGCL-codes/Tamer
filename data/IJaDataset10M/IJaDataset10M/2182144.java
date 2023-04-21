package it.xtypes.runtime.tests;

import it.xtypes.runtime.RuntimeEnvironmentEntry;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;
import it.xtypes.tests.common.TypeSystemRuntimeAbstractTests;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import it.xtypes.example.fj.fj.Class;
import it.xtypes.example.fj.fj.Field;
import it.xtypes.example.fj.fj.FjFactory;
import it.xtypes.example.fj.fj.FjPackage;
import it.xtypes.example.fj.fj.Parameter;
import it.xtypes.example.fj.fj.TypedElement;

public class JudgmentOperationsTest extends TypeSystemRuntimeAbstractTests {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testAssign() throws Exception {
        Variable left = createVariable(FjPackage.eINSTANCE.getField());
        Variable right = createVariable(FjPackage.eINSTANCE.getParameter());
        Field field = FjFactory.eINSTANCE.createField();
        field.setName("foo");
        Parameter param = FjFactory.eINSTANCE.createParameter();
        param.setName("param");
        judgmentOperations.assignment(left, right);
        assertEquals(right.getValue(), left.getValue());
        judgmentOperations.assignment(left, field);
        assertEquals(field, left.getValue());
        right.setValue(param);
        judgmentOperations.assignment(left, right);
        assertEquals(param, left.getValue());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testEquals() throws Exception {
        Variable left = createVariable(FjPackage.eINSTANCE.getField());
        Variable right = createVariable(FjPackage.eINSTANCE.getParameter());
        Field field = FjFactory.eINSTANCE.createField();
        field.setName("foo");
        Parameter param = FjFactory.eINSTANCE.createParameter();
        param.setName("param");
        judgmentOperations.assignment(left, right);
        assertTrue(judgmentOperations.equals(left, right));
        judgmentOperations.assignment(left, field);
        assertTrue(judgmentOperations.equals(left, field));
        assertFalse(judgmentOperations.equals(left, param));
        right.setValue(param);
        judgmentOperations.assignment(left, right);
        assertTrue(judgmentOperations.equals(left, right));
        assertTrue(judgmentOperations.equals(left, param));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testCastTo() {
        Variable typedElemVar = createVariable(FjPackage.eINSTANCE.getTypedElement());
        Field field = FjFactory.eINSTANCE.createField();
        field.setName("foo");
        Parameter param = FjFactory.eINSTANCE.createParameter();
        param.setName("param");
        TypedElement typedElement = FjFactory.eINSTANCE.createTypedElement();
        typedElement.setName("typed");
        assertFalse(judgmentOperations.checkCast("foo", TypedElement.class));
        assertTrue(judgmentOperations.checkCast(typedElement, TypedElement.class));
        assertTrue(judgmentOperations.checkCast(param, TypedElement.class));
        typedElemVar.setValue(param);
        assertTrue(judgmentOperations.checkCast(typedElemVar, Parameter.class));
        assertFalse(judgmentOperations.checkCast(typedElemVar, Field.class));
    }

    @SuppressWarnings("rawtypes")
    public void testGetAllNodesInRelation() {
        Class c1 = FjFactory.eINSTANCE.createClass();
        Class c2 = FjFactory.eINSTANCE.createClass();
        Class c3 = FjFactory.eINSTANCE.createClass();
        c1.setExtends(c2);
        c2.setExtends(c3);
        c3.setExtends(null);
        List l = judgmentOperations.getAllNodesInRelation(c1, c1.eClass().getEStructuralFeature("extends"));
        assertEquals(c3, l.get(0));
        assertEquals(c2, l.get(1));
    }

    public void testCastToWithLists() {
        Field field = FjFactory.eINSTANCE.createField();
        field.setName("foo");
        Class c = FjFactory.eINSTANCE.createClass();
        EList<Field> fields = c.getFields();
        fields.add(field);
        assertTrue(judgmentOperations.checkCast(fields, fields.getClass()));
        assertTrue(judgmentOperations.checkCast(fields, c.getMethods().getClass()));
    }

    @SuppressWarnings("rawtypes")
    public void testGetAll() {
        Field field1 = FjFactory.eINSTANCE.createField();
        field1.setName("foo");
        Field field2 = FjFactory.eINSTANCE.createField();
        field2.setName("foo2");
        Field field3 = FjFactory.eINSTANCE.createField();
        field3.setName("foo3");
        Class c1 = FjFactory.eINSTANCE.createClass();
        c1.setName("c1");
        EList<Field> fields = c1.getFields();
        fields.add(field1);
        Class c2 = FjFactory.eINSTANCE.createClass();
        c2.setName("c2");
        EList<Field> fields2 = c2.getFields();
        fields2.add(field2);
        Class c3 = FjFactory.eINSTANCE.createClass();
        c3.setName("c3");
        EList<Field> fields3 = c3.getFields();
        fields3.add(field3);
        c1.setExtends(c2);
        c2.setExtends(c3);
        c3.setExtends(null);
        List l = judgmentOperations.getAll(c1, c1.eClass().getEStructuralFeature("fields"), c1.eClass().getEStructuralFeature("extends"));
        assertEquals(field3, l.get(0));
        assertEquals(field2, l.get(1));
        assertEquals(field1, l.get(2));
        c3.setExtends(c1);
        l = judgmentOperations.getAll(c1, c1.eClass().getEStructuralFeature("fields"), c1.eClass().getEStructuralFeature("extends"));
        assertEquals(field1, l.get(0));
        assertEquals(field3, l.get(1));
        assertEquals(field2, l.get(2));
        c3.setExtends(c2);
        l = judgmentOperations.getAll(c1, c1.eClass().getEStructuralFeature("fields"), c1.eClass().getEStructuralFeature("extends"));
        assertEquals(field3, l.get(0));
        assertEquals(field2, l.get(1));
        assertEquals(field1, l.get(2));
        c1.setExtends(c2);
        c2.setExtends(c3);
        c3.setExtends(null);
        l = judgmentOperations.getAll(c1, c1.eClass().getEStructuralFeature("extends"), c1.eClass().getEStructuralFeature("extends"));
        assertEquals(c3, l.get(0));
        assertEquals(c2, l.get(1));
        c3.setExtends(c2);
        l = judgmentOperations.getAll(c1, c1.eClass().getEStructuralFeature("extends"), c1.eClass().getEStructuralFeature("extends"));
        assertEquals(c2, l.get(0));
        assertEquals(c3, l.get(1));
        l = judgmentOperations.getAll(c1, "extends", "extends");
        assertEquals(c2, l.get(0));
        assertEquals(c3, l.get(1));
        l = judgmentOperations.getAll(null, "extends", "extends");
        assertEquals(0, l.size());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testLenght() {
        Field field1 = FjFactory.eINSTANCE.createField();
        field1.setName("foo");
        Field field2 = FjFactory.eINSTANCE.createField();
        field1.setName("foo2");
        Field field3 = FjFactory.eINSTANCE.createField();
        field1.setName("foo3");
        Class c1 = FjFactory.eINSTANCE.createClass();
        EList<Field> fields = c1.getFields();
        fields.add(field1);
        fields.add(field2);
        fields.add(field3);
        int len = judgmentOperations.length(fields);
        assertEquals(3, len);
        Variable fieldVar = createVariable(FjPackage.eINSTANCE.getField(), true);
        fieldVar.setValue(fields);
        len = judgmentOperations.length(fields);
        assertEquals(3, len);
    }

    public void testCloneEObject() {
        Field field1 = FjFactory.eINSTANCE.createField();
        field1.setName("foo");
        Field clone = judgmentOperations.clone(field1);
        assertTrue(clone != field1);
        assertEquals(field1.getName(), clone.getName());
    }

    public void testCloneString() {
        String s = "foo";
        String c = judgmentOperations.clone(s);
        assertTrue(c == s);
    }

    public void testCloneList() {
        Field field1 = FjFactory.eINSTANCE.createField();
        field1.setName("foo");
        List<Field> fields = new LinkedList<Field>();
        fields.add(field1);
        List<Field> clone = judgmentOperations.clone(fields);
        assertTrue(clone != fields);
        assertTrue(field1 != clone.get(0));
    }

    public void testEnvUnion() {
        TypingJudgmentEnvironment environment1 = new TypingJudgmentEnvironment();
        TypingJudgmentEnvironment environment2 = new TypingJudgmentEnvironment();
        TypingJudgmentEnvironment union = judgmentOperations.union(environment1, environment2);
        assertTrue(union != environment1);
        assertTrue(union != environment2);
    }

    public void testEnvUnionWithEntries() {
        RuntimeEnvironmentEntry entry1 = new RuntimeEnvironmentEntry("first", "second");
        RuntimeEnvironmentEntry entry2 = new RuntimeEnvironmentEntry("foo", "bar");
        TypingJudgmentEnvironment union = judgmentOperations.union(entry1, entry2);
        assertTrue(union != null);
        assertEquals("second", union.get("first"));
        assertEquals("bar", union.get("foo"));
    }

    public void testEnvUnionWithEntriesFails() {
        RuntimeEnvironmentEntry entry1 = new RuntimeEnvironmentEntry("first", "second");
        RuntimeEnvironmentEntry entry2 = new RuntimeEnvironmentEntry("first", "bar");
        TypingJudgmentEnvironment union = judgmentOperations.union(entry1, entry2);
        assertTrue(union == null);
    }
}
