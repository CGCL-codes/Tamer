package com.google.gwt.editor.client;

/**
 * Editors whose behavior changes based on the value being edited will implement
 * this interface.
 * 
 * @param <T> the type of composite object the editor can display
 */
public interface ValueAwareEditor<T> extends HasEditorDelegate<T> {

    /**
   * Indicates that the Editor cycle is finished. This method will be called in
   * a depth-first order by the EditorDriver, so Editors do not generally need
   * to flush their sub-editors.
   */
    void flush();

    /**
   * Notifies the Editor that one or more value properties have changed. Not all
   * backing services support property-based notifications.
   * 
   * @param paths a list of String paths
   */
    void onPropertyChange(String... paths);

    /**
   * Called by the EditorDriver to set the object the Editor is peered with
   * <p>
   * ValueAwareEditors should preferentially use sub-editors to alter the
   * properties of the object being edited.
   * 
   * @param value a value of type T
   */
    void setValue(T value);
}
