package net.sourceforge.jfunctions.patterns;

import static org.junit.Assert.*;
import org.junit.Test;
import example.mixin.*;

public class MixedBaseTest {

    public MixedBaseTest() {
    }

    protected Model createModel() {
        return new Model();
    }

    @Test
    public void testHasFeatureSelf() {
        Model model = createModel();
        assertTrue(model.hasFeaturesOf(Model.class));
        Model self = model.as(Model.class);
        assertNotNull(self);
        assertSame(model, self);
    }

    @Test
    public void testHasFeatureSelfInterface() {
        Model model = createModel();
        assertTrue(model.hasFeaturesOf(Cloneable.class));
        Cloneable self = model.as(Cloneable.class);
        assertNotNull(self);
        assertSame(model, self);
    }

    @Test
    public void testExtend() {
        Model model = createModel();
        SimpleExtension simple = model.extend(SimpleExtension.class);
        assertSame(simple, model.as(SimpleExtension.class));
        assertTrue(model.hasFeaturesOf(SimpleExtension.class));
    }

    @Test
    public void testExtendWithOwnInterface() {
        Model model = createModel();
        Cloneable self = model.extend(Cloneable.class);
        assertSame(model, self);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendWithOwnInterfaceImpl() {
        Model model = createModel();
        model.extend(Cloneable.class, SimpleExtension.class);
    }

    @Test
    public void testExtendDecorator() {
        Model model = createModel();
        SimpleDecorator simple = model.extend(SimpleDecorator.class);
        assertSame(simple, model.as(SimpleDecorator.class));
        assertSame(model, simple.unwrap());
        assertTrue(model.hasFeaturesOf(SimpleDecorator.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendWithInterfaceException() {
        Model model = createModel();
        model.extend(ExtensionType.class);
    }

    @Test
    public void testExtendWithImplementation() {
        Model model = createModel();
        ExtensionA ext = new ExtensionA("ext", 5);
        ExtensionType value = model.extend(ExtensionType.class, ext);
        assertSame(ext, value);
        assertSame(value, model.as(ExtensionType.class));
        assertTrue(model.hasFeaturesOf(ExtensionType.class));
    }

    @Test
    public void testExtendWithImplementationType() {
        Model model = createModel();
        ExtensionType value = model.extend(ExtensionType.class, ExtensionB.class);
        assertTrue(value instanceof ExtensionB);
        assertSame(value, model.as(ExtensionType.class));
        assertTrue(model.hasFeaturesOf(ExtensionType.class));
    }

    @Test
    public void testReplace() {
        Model model = createModel();
        ExtensionA ext = new ExtensionA("ext", 5);
        model.extend(ExtensionType.class, ext);
        ExtensionType value2 = model.extend(ExtensionType.class, ExtensionB.class);
        assertTrue(value2 instanceof ExtensionB);
        assertSame(value2, model.as(ExtensionType.class));
        assertEquals(1, model.features().size());
    }

    @Test
    public void testCopy() {
        Model model = createModel();
        SimpleExtension simple = new SimpleExtension();
        model.extend(SimpleExtension.class, simple);
        model.extend(ExtensionType.class, new ExtensionA("ext", 5));
        Model copy = model.clone();
        SimpleExtension simpleCopy = copy.as(SimpleExtension.class);
        ExtensionType eTypeCopy = copy.as(ExtensionType.class);
        assertNotNull(simpleCopy);
        assertNotNull(eTypeCopy);
        assertNotSame(simple, simpleCopy);
        assertTrue(eTypeCopy instanceof ExtensionA);
        assertEquals("ext", eTypeCopy.getName());
        assertEquals(5, ((ExtensionA) eTypeCopy).getValue());
    }
}
