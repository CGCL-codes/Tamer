package net.sf.mmm.ui.toolkit.api.attribute;

/**
 * This is the interface for an object of the UI framework that is selectable.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface UiWriteSelected extends UiReadSelected {

    /**
   * This method sets the selection status of this object.<br>
   * 
   * NOTE: This method does NOT
   * {@link net.sf.mmm.ui.toolkit.api.event.UiEventListener#onEvent(net.sf.mmm.ui.toolkit.api.view.UiNode, net.sf.mmm.ui.toolkit.api.event.ActionType)
   * invoke} an action event.
   * 
   * @param selected - if <code>true</code> the object will be selected, else it
   *        will be de-selected.
   */
    void setSelected(boolean selected);
}
