package org.suren.core.gui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public abstract class SuRenJPopup extends JPopupMenu {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6638994413663878967L;

    protected JComponent comp;

    protected String[] ITEMS;

    public SuRenJPopup(JComponent comp, String[] items) throws MissingComponentException {
        if (comp == null) throw new MissingComponentException();
        if (items == null) items = new String[] {};
        this.ITEMS = items;
        this.comp = comp;
        init();
    }

    public abstract void MenuAction(ActionEvent e);

    private void init() {
        for (String item : ITEMS) {
            JMenuItem menuItem = new JMenuItem(item);
            this.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MenuAction(e);
                }
            });
        }
        final SuRenJPopup menu = this;
        comp.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    menu.show(comp, e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (menu.isShowing()) menu.setVisible(false);
            }
        });
    }
}
