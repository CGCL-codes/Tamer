package net.sf.cardic.games.yatni;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author Patrik Karlsson
 */
public class RowLabel extends JLabel {

    public RowLabel() {
    }

    public Color getCurrentBackgroundColor() {
        return currentBackgroundColor;
    }

    public void setBackground() {
        setBackground(currentBackgroundColor);
    }

    public void setCurrentBackgroundColor(Color currentBackgroundColor) {
        this.currentBackgroundColor = currentBackgroundColor;
    }

    private Color currentBackgroundColor;
}
