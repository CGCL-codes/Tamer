package com.blogspot.qbeukes.addp.tool.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author quintin
 */
public class LinkLabel extends JLabel implements MouseListener {

    private List<ActionListener> listeners = new ArrayList<ActionListener>(1);

    private String actionCommand;

    private String currentText;

    private LinkState state = LinkState.NORMAL;

    private static enum LinkState {

        PRESSED("red", "gray"), NORMAL("blue", "gray");

        private String enabledColor;

        private String disabledColor;

        LinkState(String enabledColor, String disabledColor) {
            this.enabledColor = enabledColor;
            this.disabledColor = disabledColor;
        }
    }

    /**
   * Create a new link label.
   * @param actionCommand Used for the action event's "actionCommand"
   * @param text
   */
    public LinkLabel(String actionCommand, String text) {
        this.actionCommand = actionCommand;
        setText(text);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
    }

    @Override
    public void setText(String text) {
        this.currentText = text;
        updateView();
    }

    /**
   * Updates the text view
   */
    protected void updateView() {
        super.setText("<html><body><div style='color:" + getTextColor() + ";text-decoration:underlined'>" + currentText + "</div></body></html>");
    }

    /**
   * @return Text color based on current state
   */
    public String getTextColor() {
        if (state == null) {
            return "black";
        }
        return isEnabled() ? state.enabledColor : state.disabledColor;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateView();
    }

    /**
   * Change the state and update the view
   */
    private synchronized void setState(LinkState state) {
        if (this.state != state) {
            this.state = state;
            updateView();
        }
    }

    public synchronized void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public synchronized void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    private synchronized void fireActionEvent() {
        if (listeners.size() == 0) {
            return;
        }
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand);
        for (ActionListener l : listeners) {
            l.actionPerformed(evt);
        }
    }

    public void mouseClicked(MouseEvent e) {
        fireActionEvent();
    }

    public void mousePressed(MouseEvent e) {
        setState(LinkState.PRESSED);
    }

    public void mouseReleased(MouseEvent e) {
        setState(LinkState.NORMAL);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
