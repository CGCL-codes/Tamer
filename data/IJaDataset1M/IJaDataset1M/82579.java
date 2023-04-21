package com.google.gwt.editor.client;

/**
 * Indicates that an Editor requires an EditorDelegate.
 * 
 * @param <T> the type of object the EditorDelegate operates on
 */
public interface HasEditorDelegate<T> extends Editor<T> {

    /**
   * Called by the EditorDriver to provide access to the EditorDelegate the
   * Editor is peered with.
   *
   * @param delegate an {@link EditorDelegate} of type T
   */
    void setDelegate(EditorDelegate<T> delegate);
}
