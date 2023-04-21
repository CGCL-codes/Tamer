package org.sourceforge.jemm.client.descriptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import java.lang.reflect.Method;
import org.junit.Test;
import org.sourceforge.jemm.client.classes.Shadowed;
import org.sourceforge.jemm.client.descriptor.Descriptor;
import org.sourceforge.jemm.lifecycle.AttributeUse;

public class DescriptorTest {

    String DEFAULT_CLASS = "org.sourceforge.jemm.client.classes.DescriptorDummy";

    @Test
    public void constructorPartialSignature() {
        Descriptor d = new Descriptor(DEFAULT_CLASS, "aMethod", "(IF)V");
        assertEquals(DEFAULT_CLASS, d.getClassName());
        assertEquals("aMethod", d.getMethodName());
        String[] params = d.getParameterStrings();
        assertEquals("I", params[0]);
        assertEquals("V", d.getReturnString());
    }

    @Test
    public void getMethodName() {
        Descriptor d = new Descriptor(DEFAULT_CLASS + "#aMethod ()V");
        assertEquals("aMethod", d.getMethodName());
    }

    @Test
    public void getClassName() {
        String signature = DEFAULT_CLASS + "#aMethod ()V";
        Descriptor d = new Descriptor(signature);
        assertEquals(DEFAULT_CLASS, d.getClassName());
    }

    @Test(expected = DescriptorParsingException.class)
    public void failNull() {
        new Descriptor(null);
    }

    @Test(expected = DescriptorParsingException.class)
    public void missingEnding() {
        new Descriptor(DEFAULT_CLASS + "#aMethod");
    }

    @Test(expected = DescriptorParsingException.class)
    public void missingReturn() {
        new Descriptor(DEFAULT_CLASS + "#aMethod ()");
    }

    @Test(expected = DescriptorParsingException.class)
    public void missingMethodName() {
        new Descriptor(DEFAULT_CLASS + "()V");
    }

    @Test(expected = DescriptorParsingException.class)
    public void missingClassName() {
        new Descriptor("aMethod()V");
    }

    @Test
    public void getBasicParameterStrings() {
        String[] results = new Descriptor(DEFAULT_CLASS + "#aMethod (D)V").getParameterStrings();
        assertEquals(1, results.length);
        assertEquals("D", results[0]);
        results = new Descriptor(DEFAULT_CLASS + "#aMethod (DFI)V").getParameterStrings();
        assertEquals(3, results.length);
        assertEquals("D", results[0]);
        assertEquals("F", results[1]);
        assertEquals("I", results[2]);
    }

    @Test
    public void getObjectParameterStrings() {
        String[] results = new Descriptor(DEFAULT_CLASS + "#aMethod (Ljava/lang/Object;)V").getParameterStrings();
        assertEquals(1, results.length);
        assertEquals("Ljava.lang.Object;", results[0]);
    }

    @Test
    public void getArrayParameterStrings() {
        String[] results = new Descriptor(DEFAULT_CLASS + "#aMethod ([F)V").getParameterStrings();
        assertEquals(1, results.length);
        assertEquals("[F", results[0]);
    }

    @Test
    public void getArrayObjectParameterStrings() {
        String[] results = new Descriptor(DEFAULT_CLASS + "#aMethod ([Ljava/lang/Object;)V").getParameterStrings();
        assertEquals(1, results.length);
        assertEquals("[Ljava.lang.Object;", results[0]);
    }

    @Test
    public void getMixedParameterStrings() {
        String[] results = new Descriptor(DEFAULT_CLASS + "#aMethod (FLjava/lang/Object;F)V").getParameterStrings();
        assertEquals(3, results.length);
        assertEquals("F", results[0]);
        assertEquals("Ljava.lang.Object;", results[1]);
        assertEquals("F", results[2]);
    }

    @Test
    public void initialisingArrayClasses() throws ClassNotFoundException {
        Class<?> c = Class.forName("[D");
        assertNotNull(c);
        assertEquals(double[].class, c);
    }

    @Test
    public void initialisingArrayClassesObjects() throws ClassNotFoundException {
        Class<?> c = Class.forName("[Ljava.lang.Object;");
        assertNotNull(c);
        assertEquals(Object[].class, c);
    }

    @Test
    public void getPrimitiveParameterClasses() throws ClassNotFoundException {
        Class<?>[] results = new Descriptor(DEFAULT_CLASS + "#aMethod (F)V").getParameterClasses();
        assertEquals(float.class, results[0]);
    }

    @Test
    public void getObjectParameterClasses() throws ClassNotFoundException {
        Class<?>[] results = new Descriptor(DEFAULT_CLASS + "#aMethod (Ljava/lang/Object;)V").getParameterClasses();
        assertEquals(Object.class, results[0]);
    }

    @Test
    public void getObjectArrayParameterClasses() throws ClassNotFoundException {
        Class<?>[] results = new Descriptor(DEFAULT_CLASS + "#aMethod ([Ljava/lang/Object;)V").getParameterClasses();
        assertEquals(Object[].class, results[0]);
    }

    @Test
    public void getVoidParameterClasses() throws ClassNotFoundException {
        Class<?>[] results = new Descriptor(DEFAULT_CLASS + "#aMethod (V)V").getParameterClasses();
        assertEquals(0, results.length);
    }

    @Test
    public void cleanClassNamePrimitiveArray() {
        String result = new Descriptor(DEFAULT_CLASS + "#aMethod ()V").cleanClassName("[F");
        assertEquals("[F", result);
    }

    @Test
    public void cleanClassNameObject() {
        String result = new Descriptor(DEFAULT_CLASS + "#aMethod ()V").cleanClassName("Ljava.lang.Object;");
        assertEquals("java.lang.Object", result);
    }

    @Test
    public void cleanClassNameObjectArray() {
        String result = new Descriptor(DEFAULT_CLASS + "#aMethod ()V").cleanClassName("[[Ljava.lang.Object;");
        assertEquals("[[Ljava.lang.Object;", result);
    }

    @Test
    public void returnParameterPrimitive() {
        String result = new Descriptor(DEFAULT_CLASS + "#aMethod ()F").getReturnString();
        assertEquals("F", result);
    }

    @Test
    public void returnParameterObject() {
        String result = new Descriptor(DEFAULT_CLASS + "#aMethod ()Ljava/lang/Object;").getReturnString();
        assertEquals("Ljava/lang/Object;", result);
    }

    @Test
    public void returnParameterArrayObject() {
        String result = new Descriptor(DEFAULT_CLASS + "#aMethod ()[[Ljava/lang/Object;").getReturnString();
        assertEquals("[[Ljava/lang/Object;", result);
    }

    @Test
    public void returnClassPrimitive() throws ClassNotFoundException {
        Class<?> result = new Descriptor(DEFAULT_CLASS + "#aMethod ()F").getReturnClass();
        assertEquals(Float.TYPE, result);
    }

    @Test
    public void existsGetClass() throws Exception {
        Class<?> result = new Descriptor(Shadowed.class.getName() + "#aMethod ()F").getClassContainingMethod();
        assertEquals(Shadowed.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void notExistsGetClass() throws Exception {
        new Descriptor("fantasyClass#aMethod ()F").getClassContainingMethod();
    }

    @Test
    public void getMethodExists() throws SecurityException, NoSuchMethodException, ClassNotFoundException {
        Method m = new Descriptor(Shadowed.class.getName() + "#doesNothing ()V").getMethod();
        assertNotNull(m);
        assertEquals("doesNothing", m.getName());
    }

    @Test
    public void areEqual() {
        Descriptor d1 = new Descriptor(Shadowed.class.getName(), "aMethod", "V", "F");
        Descriptor d2 = new Descriptor(Shadowed.class.getName(), "aMethod", "V", "F");
        Descriptor d3 = new Descriptor(Shadowed.class.getName(), "aMethod", "(F)V");
        assertEquals(d1, d2);
        assertEquals(d1, d3);
    }

    @Test
    public void notEqual() {
        Descriptor d1 = new Descriptor(Shadowed.class.getName(), "aMethod", "V", "F");
        Descriptor d2 = new Descriptor(Shadowed.class.getName(), "aMethod", "()V");
        Descriptor d3 = new Descriptor(Shadowed.class.getName(), "aMethod2", "V", "F");
        Descriptor d4 = new Descriptor(Object.class.getName(), "aMethod", "V", "F");
        assertFalse(d1.equals(d2));
        assertFalse(d1.equals(d3));
        assertFalse(d1.equals(d4));
    }

    @Test
    public void methodUsesNoAnnotation() {
        Descriptor d = new Descriptor(Shadowed.class.getName() + "#doesNothing ()V");
        AttributeUse[] fields = d.methodUses();
        assertEquals(0, fields.length);
    }

    @Test
    public void methodUsesSingleAnnotation() {
        Descriptor d = new Descriptor(Shadowed.class.getName() + "#setObject (Lorg/sourceforge/jemm/util/JEMMObject;)V");
        AttributeUse[] fields = d.methodUses();
        assertEquals(1, fields.length);
        assertEquals(Shadowed.class.getName(), fields[0].clazz());
        assertEquals("obj", fields[0].name());
    }

    @Test
    public void methodUsesMultipleAnnotation() {
        Descriptor d = new Descriptor(Shadowed.class.getName() + "#accessAllObjects ()V");
        AttributeUse[] fields = d.methodUses();
        assertEquals(2, fields.length);
        assertEquals(Shadowed.class.getName(), fields[0].clazz());
        assertEquals("obj", fields[0].name());
        assertEquals(Shadowed.class.getName(), fields[1].clazz());
        assertEquals("obj2", fields[1].name());
    }
}
