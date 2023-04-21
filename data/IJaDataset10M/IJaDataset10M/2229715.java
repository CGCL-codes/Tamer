package org.bm.firestarter.propertysheet.editor;

import java.beans.PropertyEditor;
import java.lang.reflect.Array;

/**
 * <p>
 *   <code>IndexedPropertyEditor</code> allows for string-based editing of indexed
 *   properties. This requires that the underlying data type supports the
 *   <code>getAsText()</code> and <code>setAsText()</code> methods, <i>without using commas</i>.
 * </p>
 *
 * @author Elisha Peterson
 */
public class IndexedPropertyEditor extends MPropertyEditorSupport {

    /** The editor that handles individual components of the array. */
    PropertyEditor baseEditor;

    /** For use by the class setting and getting text. */
    String[] splits;

    public IndexedPropertyEditor() {
    }

    /** @return component type of the underlying array. */
    public Class getComponentType() {
        Object[] array = (Object[]) getValue();
        return array.getClass().getComponentType();
    }

    @Override
    protected void initEditorValue() {
        Object[] array = (Object[]) getValue();
        baseEditor = EditorRegistration.getRegisteredEditor(getComponentType());
        baseEditor.setValue(array[0]);
    }

    @Override
    public String getAsText() {
        Object[] array = (Object[]) getValue();
        String result = "";
        for (int i = 0; i < array.length; i++) {
            baseEditor.setValue(array[i]);
            result += (i == 0 ? "" : ", ") + baseEditor.getAsText();
        }
        return result;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        splits = text.split(",");
        if (splits.length != splits.length) {
            throw new IllegalArgumentException();
        }
        setAsText(splits);
    }

    private void setAsText(String... splits) {
        Object[] result = (Object[]) Array.newInstance(getComponentType(), splits.length);
        for (int i = 0; i < result.length; i++) {
            baseEditor.setAsText(splits[i]);
            result[i] = baseEditor.getValue();
        }
        setValue(result);
    }
}
