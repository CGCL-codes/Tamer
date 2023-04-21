package net.suberic.util.gui.propedit;

/**
 * This listens to a PropertyEditorUI and reacts to changes.
 */
public interface PropertyEditorListener {

    /**
   * Called when a property is about to change.  If the value is not ok
   * with the listener, a PropertyValueVetoException should be thrown.
   */
    public void propertyChanging(PropertyEditorUI source, String property, String newValue) throws PropertyValueVetoException;

    /**
   * Called after a property changes.
   */
    public void propertyChanged(PropertyEditorUI source, String property, String newValue);
}
