package ec.edu.epn.fis.uil4midp.views;

import ec.edu.epn.fis.uil4midp.actions.ActionListener;
import ec.edu.epn.fis.uil4midp.components.controls.ListItem;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;

/**
 * A List is a kind of view which holds only ListItems.
 * @author Carlos Andrés Oquendo
 */
public abstract class List extends Form {

    protected ActionListener itemSelectionActionListener;

    /**
     * Creates a new instance of the List class with the specified caption.
     * @param caption Text to be used on the titlebar of the List.
     */
    public List(String caption) {
        super(caption);
        super.setMargin(0);
        super.setControlSeparation(0);
        this.loadDelay = 2000;
    }

    /**
     * Creates a new instance of the List class with the specified caption and
     * a value indicating a delay for the load event.
     * @param caption Text to be used on the titlebar of the List.
     * @param loadDelay Time in milliseconds that must pass before the load event is fired.
     */
    public List(String caption, int loadDelay) {
        this(caption);
        this.loadDelay = loadDelay;
    }

    /**
     * Adds a new text item to the list.
     * @param caption Text to show on the list item.
     */
    public final void addListItem(String caption) {
        ListItem li = new ListItem(caption);
        li.setValue(caption);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Adds a new text item to the list with an associated value.
     * @param caption Text to show on the list item.
     * @param value Object representing the value of the list item.
     */
    public final void addListItem(String caption, Object value) {
        ListItem li = new ListItem(caption);
        li.setValue(value);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Adds a new text item to the list with a secondary text.
     * @param caption Text to show on the list item.
     * @param text Additional text to show on the list item.
     */
    public final void addListItem(String caption, String text) {
        ListItem li = new ListItem(caption, text);
        li.setValue(caption);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Adds a new text item to the list with a secondary text and an associated value
     * @param caption Text to show on the list item.
     * @param text Additional text to show on the list item.
     * @param value Object representing the value of the list item.
     */
    public final void addListItem(String caption, String text, Object value) {
        ListItem li = new ListItem(caption, text);
        li.setValue(value);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Adds a new text item to the list with an image.
     * @param caption Text to show on the list item.
     * @param image Additional image to show on the list item.
     * @param isIcon Flag that indicated if the image is an icon or not.
     */
    public final void addListItem(String caption, Image image, boolean isIcon) {
        ListItem li = new ListItem(caption, image, isIcon);
        li.setValue(caption);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Adds a new text item to the list with an image and an associated value.
     * @param caption Text to show on the list item.
     * @param image Additional image to show on the list item.
     * @param isIcon Flag that indicated if the image is an icon or not.
     * @param value Object representing the value of the list item.
     */
    public final void addListItem(String caption, Image image, boolean isIcon, Object value) {
        ListItem li = new ListItem(caption, image, isIcon);
        li.setValue(value);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Adds a new text item to the list with an image and a secondary text.
     * @param caption Text to show on the list item.
     * @param image Additional image to show on the list item.
     * @param text Additional text to show on the list item.
     * @param isIcon Flag that indicated if the image is an icon or not.
     */
    public final void addListItem(String caption, Image image, String text, boolean isIcon) {
        ListItem li = new ListItem(caption, text, image, isIcon);
        li.setValue(caption);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Adds a new text item to the list with an image, a secondary text and an associated value.
     * @param caption Text to show on the list item.
     * @param image Additional image to show on the list item.
     * @param text Additional text to show on the list item.
     * @param isIcon Flag that indicated if the image is an icon or not.
     * @param value Object representing the value of the list item.
     */
    public final void addListItem(String caption, Image image, String text, boolean isIcon, Object value) {
        ListItem li = new ListItem(caption, text, image, isIcon);
        li.setValue(value);
        li.setPadding(tm.getListitemPadding());
        addVisualComponent(li);
    }

    /**
     * Sets an ActionListener to be used by the FIRE key event handler.
     * The logic within this ActionListener must try to use the selected item
     * on the list. This ActionListener will be called when the FIRE key is
     * pressed and a list item is selected and has the focus.
     * @param actionListener ActionListener of the List.
     */
    public final void setItemSelectionActionListener(ActionListener itemSelectionActionListener) {
        this.itemSelectionActionListener = itemSelectionActionListener;
    }

    /**
     * Gets the selected ListItem on the List.
     * @return SelectedListItem on the List. If there is no selected item, null is returned
     */
    public final ListItem getSelectedListItem() {
        return (ListItem) getSelectedVisualComponent();
    }

    /**
     * Handles the key events.
     * @param action Canvas' key action number.
     * @param keyCode Pressed key code. This code may be device-specific.
     * @return True if the key event was handled, else, False.
     */
    public boolean keyPressed(int action, int keyCode) {
        switch(action) {
            case Canvas.FIRE:
                if (itemSelectionActionListener != null && baseContainer.isFocused()) {
                    itemSelectionActionListener.execute();
                    return true;
                }
                return super.keyPressed(action, keyCode);
            default:
                return super.keyPressed(action, keyCode);
        }
    }

    /**
     * Sets the margin of the Form
     * @param margin Margin of the Form.
     */
    public void setMargin(int margin) {
        super.setMargin(0);
    }

    /**
     * Sets the distance between VisualComponents when drawn on screen.
     * @param controlSeparation Distance between VisualComponents.
     */
    public void setControlSeparation(int controlSeparation) {
        super.setControlSeparation(0);
    }
}
