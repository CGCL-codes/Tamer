package net.sourceforge.omov.app.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class OmovListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 5300720838872292929L;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        prepareComponent(c, isSelected, index, null);
        return c;
    }

    public static void prepareComponent(Component c, boolean isSelected, int index, Color unselectedHighlightColor) {
        if (isSelected == true) {
            c.setBackground(Constants.getColorSelectedBackground());
            c.setForeground(Constants.getColorSelectedForeground());
        } else {
            if (unselectedHighlightColor != null) {
                c.setBackground(unselectedHighlightColor);
            } else if (index % 2 == 1) {
                c.setBackground(Constants.getColorRowBackgroundOdd());
            } else if (index % 2 == 0) {
                c.setBackground(Constants.getColorRowBackgroundEven());
            } else {
                throw new FatalException("Unhandled state: index=" + index + "; isSelected=" + isSelected + "; unselectedHighlightColor=" + unselectedHighlightColor);
            }
        }
    }
}
