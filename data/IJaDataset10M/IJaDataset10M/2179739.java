package org.eclipse.emf.edit.provider;

/**
 * This is the interface implemented by an item provider if it supports an updateable item text.
 * This is be used to support edit-in-place tree items.
 */
public interface IUpdateableItemText {

    /**
   * This returns the text that will be displayed when editing begins.
   */
    public String getUpdateableText(Object object);

    /**
   * This sets the given object's label text to the given text. 
   */
    public void setText(Object object, String text);
}
