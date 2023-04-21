package com.l2fprod.common.propertysheet;

import java.beans.*;
import java.util.List;
import junit.framework.TestCase;
import com.l2fprod.common.beans.editor.BooleanPropertyEditor;
import com.l2fprod.common.beans.editor.DoublePropertyEditor;
import com.l2fprod.common.beans.editor.LongPropertyEditor;
import com.l2fprod.common.beans.editor.StringPropertyEditor;

/**
 * PropertyEditorRegistryTest. <br>
 */
public class PropertyEditorRegistryUnitTest extends TestCase {

    public PropertyEditorRegistryUnitTest(String name) {
        super(name);
    }

    public void testBasic() {
        PropertyEditor editor = null;
        PropertyEditorRegistry registry = new PropertyEditorRegistry();
        DefaultProperty prop = new DefaultProperty();
        prop.setType(String.class);
        editor = registry.getEditor(String.class);
        assertTrue(editor instanceof StringPropertyEditor);
        editor = registry.getEditor(prop);
        assertTrue(editor instanceof StringPropertyEditor);
        registry.registerEditor(String.class, BooleanPropertyEditor.class);
        editor = registry.getEditor(String.class);
        assertTrue(editor instanceof BooleanPropertyEditor);
        editor = registry.getEditor(prop);
        assertTrue(editor instanceof BooleanPropertyEditor);
        editor = new DoublePropertyEditor();
        registry.registerEditor(String.class, editor);
        assertTrue(editor == registry.getEditor(String.class));
        assertTrue(editor == registry.getEditor(prop));
        registry.registerEditor(prop, LongPropertyEditor.class);
        editor = registry.getEditor(prop);
        assertTrue(editor instanceof LongPropertyEditor);
        registry.registerEditor(prop, editor);
        assertTrue(editor == registry.getEditor(prop));
        registry.registerDefaults();
        editor = registry.getEditor(String.class);
        assertTrue("editor is " + editor, editor instanceof StringPropertyEditor);
        editor = registry.getEditor(prop);
        assertTrue(editor instanceof StringPropertyEditor);
    }

    public void testEditorFromPropertyDescriptor() {
        PropertyEditorRegistry registry = new PropertyEditorRegistry();
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor("simpleProperty", MockBean.class);
            propertyDescriptor.setPropertyEditorClass(MockPropertyEditor.class);
            PropertyDescriptorAdapter property = new PropertyDescriptorAdapter(propertyDescriptor);
            PropertyEditor editor = registry.getEditor(property);
            assertEquals(MockPropertyEditor.class, editor.getClass());
        } catch (IntrospectionException e) {
            fail();
        }
    }

    public void testEditorFromPropertyManager() {
        PropertyEditorManager.registerEditor(List.class, MockPropertyEditor.class);
        PropertyEditorRegistry registry = new PropertyEditorRegistry();
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor("complexProperty", MockBean.class);
            PropertyDescriptorAdapter property = new PropertyDescriptorAdapter(propertyDescriptor);
            PropertyEditor editor = registry.getEditor(property);
            assertEquals(MockPropertyEditor.class, editor.getClass());
        } catch (IntrospectionException e) {
            fail();
        }
    }

    public static class MockPropertyEditor extends PropertyEditorSupport {
    }

    public static class MockBean {

        String simpleProperty;

        List complexProperty;

        public String getSimpleProperty() {
            return simpleProperty;
        }

        public void setSimpleProperty(String simpleProperty) {
            this.simpleProperty = simpleProperty;
        }

        public List getComplexProperty() {
            return complexProperty;
        }

        public void setComplexProperty(List complexProperty) {
            this.complexProperty = complexProperty;
        }
    }
}
