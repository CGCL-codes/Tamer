package megamek.client.util.widget;

import java.awt.*;
import java.awt.event.*;

/**
 * Abstract class which defines common functionality for all hot areas such as
 * event handling and dispatching.
 */
public abstract class PMGenericHotArea implements PMHotArea {

    private ActionListener actionListener = null;

    private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor c) {
        cursor = c;
    }

    public synchronized void addActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.add(actionListener, l);
    }

    public synchronized void removeActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    public void onMouseClick(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = "";
        if (0 != (modifiers & InputEvent.BUTTON1_MASK)) {
            command = PMHotArea.MOUSE_CLICK_LEFT;
        } else if (0 != (modifiers & InputEvent.BUTTON2_MASK)) {
            command = PMHotArea.MOUSE_CLICK_RIGHT;
        }
        if (e.getClickCount() > 1) command = PMHotArea.MOUSE_DOUBLE_CLICK;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    public void onMouseOver(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_OVER;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    public void onMouseExit(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_EXIT;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    public void onMouseDown(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_DOWN;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    public void onMouseUp(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_UP;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    private void dispatchEvent(ActionEvent ae) {
        if (actionListener != null) {
            actionListener.actionPerformed(ae);
        }
    }
}
