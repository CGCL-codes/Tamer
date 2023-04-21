package org.tranche.gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.plaf.basic.BasicMenuUI;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class GenericMenu extends JMenu {

    private GenericMenuUI ui = new GenericMenuUI();

    public GenericMenu(String title) {
        this(title, false);
    }

    public GenericMenu(String title, boolean isSubMenu) {
        super(title);
        getPopupMenu().setBorder(Styles.BORDER_BLACK_1);
        if (!isSubMenu) {
            setUI(ui);
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            setBackground(Styles.COLOR_MENU_BAR_BACKGROUND);
            setForeground(Styles.COLOR_MENU_BAR_FOREGROUND);
            setFont(Styles.FONT_12PT_BOLD);
        } else {
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            setForeground(Color.BLACK);
            setFont(Styles.FONT_12PT);
        }
    }

    private class GenericMenuUI extends BasicMenuUI {

        public GenericMenuUI() {
            super.selectionBackground = Styles.COLOR_MENU_BAR_SELECTION_BACKGROUND;
            super.selectionForeground = Styles.COLOR_MENU_BAR_SELECTION_FOREGROUND;
        }
    }
}
