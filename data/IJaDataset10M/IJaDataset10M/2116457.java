package ubermunchkin.client.component;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.*;

/**
* Used for radio buttons and checkboxes.
* buttons[0] 	checked
* buttons[1]	unchecked
* buttons[2]	disabled
*/
public class CustomRadioButton extends JPanel {

    private class RadioButtonMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (enabled) {
                if ((state == CustomRadioButton.CHECKED) && (!noMouseUncheck)) {
                    state = CustomRadioButton.UNCHECKED;
                    fireActionEvent();
                } else if (state == CustomRadioButton.UNCHECKED) {
                    state = CustomRadioButton.CHECKED;
                    fireActionEvent();
                }
            }
            repaint();
        }
    }

    private RadioButtonMouseAdapter adapter;

    private ArrayList listeners;

    private ImageIcon[] buttons;

    private String lb;

    private Font rbFont;

    private int state;

    private boolean noMouseUncheck;

    private boolean enabled;

    private Toolkit tk;

    public static final int CHECKED = 1;

    public static final int UNCHECKED = 2;

    public CustomRadioButton(ImageIcon[] buttons, String label) {
        this.buttons = buttons;
        this.lb = label;
        this.state = UNCHECKED;
        this.enabled = true;
        this.setOpaque(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rbFont = new Font("SansSerif", Font.BOLD, 12);
        setComponentSize(50, 15);
        adapter = new RadioButtonMouseAdapter();
        this.addMouseListener(adapter);
        this.listeners = new ArrayList();
    }

    /** 
    * Sets the default size for the radio button.
    */
    private void setComponentSize(int width, int height) {
        int w, h;
        w = 15 + 5 + width;
        h = height;
        setSize(w, h);
    }

    /**
    * Renders the radio button.
    */
    public void paintComponent(Graphics g) {
        if (enabled) {
            if (state == CustomRadioButton.CHECKED) {
                g.drawImage(buttons[0].getImage(), 0, 0, null);
                g.setColor(Color.white);
            } else if (state == CustomRadioButton.UNCHECKED) {
                g.drawImage(buttons[1].getImage(), 0, 0, null);
                g.setColor(Color.white);
            }
        } else {
            g.drawImage(buttons[2].getImage(), 0, 0, null);
            g.setColor(Color.lightGray);
        }
        g.setFont(rbFont);
        g.drawString(lb, 20, 13);
    }

    /**
    * Returns the current state of the radio button: CHECKED or UNCHECKED.
    */
    public int getState() {
        return state;
    }

    /**
    * Set the state of the radio button.
    * If an invalid state is given, this method does nothing.
    */
    public void setState(int state) {
        if ((state == CustomRadioButton.CHECKED) || (state == CustomRadioButton.UNCHECKED)) {
            this.state = state;
            repaint();
        }
    }

    /**
    * Returns true if radio button is enabled.
    */
    public boolean isEnabled() {
        return enabled;
    }

    /**
    * Enables or disables the radio button.
    */
    public void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
        repaint();
    }

    /**
    * Returns the text label for the radio button.
    */
    public String getText() {
        return lb;
    }

    /**
    * Add a listener to the radio button's list of listeners.
    */
    public void addActionListener(ActionListener lis) {
        if (lis != null) listeners.add(lis);
    }

    /**
    * Remove a listener from the radio buttons list of listeners.
    */
    public void removeActionListener(ActionListener lis) {
        if (lis != null) listeners.remove(lis);
    }

    /**
    * State of the radio button has been changed. Fire an action 
    * event to let listeners know. Command string will be the button's
    * label. Called from the AWT event dispatch thread.
    */
    private void fireActionEvent() {
        Iterator i;
        ActionListener lis;
        i = listeners.iterator();
        while (i.hasNext()) {
            lis = (ActionListener) i.next();
            lis.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, lb));
        }
    }

    /**
    * Determines whether the user can uncheck the button by clicking on
    * it. Providing a true value to this method means that the radio
    * button cannot be toggled to the unchecked state by means of the 
    * mouse.
    */
    public void setNoMouseUncheck(boolean uncheck) {
        this.noMouseUncheck = uncheck;
    }
}
