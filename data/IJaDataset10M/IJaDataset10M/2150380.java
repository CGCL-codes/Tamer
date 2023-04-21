package de.sic_consult.forms.editor;

import java.beans.PropertyDescriptor;

public interface FieldEditorFactory<T, U extends FieldEditor<T>> {

    U getEditor(Class<?> parentClass, PropertyDescriptor prop) throws Exception;
}
