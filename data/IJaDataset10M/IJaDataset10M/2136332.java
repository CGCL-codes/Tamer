package it.xtypes.runtime.tests;

import it.xtypes.runtime.RuntimeEnvironmentEntry;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;
import it.xtypes.tests.common.TypeSystemAbstractTests;
import it.xtypes.tests.common.TypeSystemRuntimeAbstractTests;
import it.xtypes.typesystem.Rule;
import it.xtypes.typesystem.TypeSystemDefinition;
import it.xtypes.typesystem.TypingJudgment;
import org.eclipse.emf.common.util.EList;
import it.xtypes.example.fj.fj.Class;
import it.xtypes.example.fj.fj.Field;
import it.xtypes.example.fj.fj.FjFactory;

@SuppressWarnings("unused")
public class EnvironmentTest extends TypeSystemRuntimeAbstractTests {

    public void testIncrementAndDecrement() throws Exception {
        TypingJudgmentEnvironment environment = new TypingJudgmentEnvironment();
        Field field = FjFactory.eINSTANCE.createField();
        field.setName("foo");
        Field field2 = FjFactory.eINSTANCE.createField();
        field2.setName("foo2");
        Class c = FjFactory.eINSTANCE.createClass();
        c.setName("myclass");
        EList<Field> fields = c.getFields();
        fields.add(field);
        Class c2 = FjFactory.eINSTANCE.createClass();
        c2.setName("myclass2");
        EList<Field> fields2 = c2.getFields();
        fields2.add(field2);
        RuntimeEnvironmentEntry typingStatement3 = new RuntimeEnvironmentEntry(field, c);
        RuntimeEnvironmentEntry typingStatement4 = new RuntimeEnvironmentEntry(fields2, fields);
        RuntimeEnvironmentEntry typingStatement5 = new RuntimeEnvironmentEntry("first", "second");
        environment.add(typingStatement3);
        TypingJudgmentEnvironment environment2 = new TypingJudgmentEnvironment();
        environment2.add(typingStatement4);
        environment2.add(typingStatement5);
        environment.increment(environment2);
        assertEquals(3, environment.getEnvironment().size());
        environment.decrement(environment2);
        assertEquals(1, environment.getEnvironment().size());
        assertEquals(c, environment.get(field));
        environment.increment(typingStatement4);
        assertEquals(2, environment.getEnvironment().size());
        environment.decrement(typingStatement4.getLeft());
        assertEquals(1, environment.getEnvironment().size());
    }

    public void testUnion() throws Exception {
        TypingJudgmentEnvironment environment = new TypingJudgmentEnvironment();
        Field field = FjFactory.eINSTANCE.createField();
        field.setName("foo");
        Field field2 = FjFactory.eINSTANCE.createField();
        field2.setName("foo2");
        Class c = FjFactory.eINSTANCE.createClass();
        c.setName("myclass");
        EList<Field> fields = c.getFields();
        fields.add(field);
        Class c2 = FjFactory.eINSTANCE.createClass();
        c2.setName("myclass2");
        EList<Field> fields2 = c2.getFields();
        fields2.add(field2);
        RuntimeEnvironmentEntry typingStatement3 = new RuntimeEnvironmentEntry(field, c);
        RuntimeEnvironmentEntry typingStatement4 = new RuntimeEnvironmentEntry(fields2, fields);
        RuntimeEnvironmentEntry typingStatement5 = new RuntimeEnvironmentEntry("first", "second");
        environment.add(typingStatement3);
        TypingJudgmentEnvironment environment2 = new TypingJudgmentEnvironment();
        environment2.add(typingStatement4);
        environment2.add(typingStatement5);
        assertTrue(environment.union(environment2));
        assertEquals(3, environment.getEnvironment().size());
        assertFalse(environment.union(environment2));
        typingStatement5 = new RuntimeEnvironmentEntry("first", "foobar");
        assertFalse(environment.union(typingStatement5));
    }

    public void testList() throws Exception {
        TypingJudgmentEnvironment environment1 = new TypingJudgmentEnvironment();
        Field field = FjFactory.eINSTANCE.createField();
        field.setName("foo");
        Field field2 = FjFactory.eINSTANCE.createField();
        field2.setName("foo2");
        Class c = FjFactory.eINSTANCE.createClass();
        c.setName("myclass");
        EList<Field> fields = c.getFields();
        fields.add(field);
        Class c2 = FjFactory.eINSTANCE.createClass();
        c2.setName("myclass2");
        EList<Field> fields2 = c2.getFields();
        fields2.add(field2);
        RuntimeEnvironmentEntry typingStatement3 = new RuntimeEnvironmentEntry(field, c);
        RuntimeEnvironmentEntry typingStatement4 = new RuntimeEnvironmentEntry(fields2, fields);
        RuntimeEnvironmentEntry typingStatement5 = new RuntimeEnvironmentEntry("first", "second");
        environment1.add(typingStatement3);
        TypingJudgmentEnvironment environment2 = new TypingJudgmentEnvironment();
        environment2.add(typingStatement4);
        environment2.add(typingStatement5);
        environment1.setNext(environment2);
        Object value;
        value = environment1.get("first");
        assertTrue(value != null);
        assertEquals("second", value);
        value = environment1.get(fields2);
        assertTrue(value != null);
        assertEquals(fields, value);
        value = environment1.get("foobar");
        assertTrue(value == null);
        RuntimeEnvironmentEntry typingStatement6 = new RuntimeEnvironmentEntry("another", "one");
        environment2.setNext(typingStatement6);
        value = environment1.get("another");
        assertTrue(value != null);
        assertEquals("one", value);
    }
}
