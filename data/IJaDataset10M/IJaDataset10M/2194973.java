package org.softsmithy.lib.swing.customizer;

import java.awt.*;
import java.awt.event.*;
import org.softsmithy.lib.swing.*;

public interface State {

    void mouseMoved(MouseEvent e);

    void mouseDragged(MouseEvent e);

    void applyCursor();

    void focusGained(FocusEvent e);

    void mousePressed(MouseEvent e);

    void mouseClicked(MouseEvent e);

    void focusLost(FocusEvent e);

    JCustomizer getCustomizer();

    void mouseReleased(MouseEvent e);

    void mouseExited(MouseEvent e);

    void configureCustomizer();

    void unconfigureCustomizer();

    void applyBorder();

    void mouseEntered(MouseEvent e);

    void resetBorder(Color borderColor);

    /** Getter for property active.
     * @return Value of property active.
     *
     */
    public boolean isActive();

    /** Setter for property active.
     * @param active New value of property active.
     *
     */
    public void setActive(boolean active);
}
