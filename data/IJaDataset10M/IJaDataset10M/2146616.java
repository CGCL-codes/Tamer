package traviaut.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import traviaut.Main;
import traviaut.data.BuildNames;

public class BuildMenu {

    private final JFrame frame;

    private final JButton btn;

    private final JPopupMenu menu = new JPopupMenu();

    private BuildNames names;

    private int[] mapToID;

    public BuildMenu(JFrame fr, BuildNames n, String title) {
        frame = fr;
        names = n;
        btn = new JButton(title);
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnActionPerformed();
            }
        });
        menu.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
                if (names == null || !names.doSelect()) return;
                try {
                    boolean[] sel = new boolean[menu.getComponentCount()];
                    for (int i = 0; i < sel.length; i++) {
                        JCheckBoxMenuItem it = (JCheckBoxMenuItem) menu.getComponent(i);
                        sel[i] = it.isSelected();
                    }
                    if (sel != null && sel.length > 0) {
                        btnBackground(sel[0]);
                    }
                    names.setSelected(sel, mapToID);
                    Main.saveSettings();
                } catch (Exception ex) {
                    new ExceptionDialog(frame, ex).setVisible(true);
                }
            }
        });
    }

    private void btnBackground(boolean b) {
        Color c = b ? Color.GREEN : null;
        btn.setBackground(c);
    }

    public void setNames(BuildNames n) {
        names = n;
        if (names != null && names.doSelect()) {
            List<String> strs = new ArrayList<String>();
            mapToID = names.getNames(strs);
            boolean[] sel = names.getSelected(mapToID);
            btnBackground(sel[0]);
        }
    }

    public JButton getButton() {
        return btn;
    }

    private void addItem(String str, boolean enabled) {
        JCheckBoxMenuItem ch = new JCheckBoxMenuItem(str, enabled);
        ch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                menu.setVisible(true);
            }
        });
        menu.add(ch);
    }

    private void btnActionPerformed() {
        menu.removeAll();
        if (names == null) return;
        List<String> strs = new ArrayList<String>();
        mapToID = names.getNames(strs);
        boolean[] sel = names.getSelected(mapToID);
        if (names.doSelect()) {
            for (int i = 0; i < strs.size(); i++) {
                addItem(strs.get(i), sel[i]);
            }
        } else {
            for (String s : strs) menu.add(s);
        }
        menu.show(btn, 0, btn.getHeight());
    }
}
