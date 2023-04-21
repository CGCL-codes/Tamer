package vmap.modes.mindmapmode;

import vmap.main.Tools;
import vmap.modes.MindIcon;
import java.lang.Integer;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.Action;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;

public class MindMapToolBar extends JToolBar {

    private static final String[] sizes = { "8", "10", "12", "14", "16", "18", "20", "24", "28" };

    private MindMapController c;

    private JComboBox fonts, size;

    private JToolBar buttonToolBar;

    private boolean fontSize_IgnoreChangeEvent = false;

    private boolean fontFamily_IgnoreChangeEvent = false;

    public MindMapToolBar(MindMapController controller) {
        this.c = controller;
        this.setRollover(true);
        this.setFloatable(false);
        JButton button;
        button = add(c.newMap);
        button.setText("");
        button = add(c.newTpl);
        button.setText("");
        button = add(c.open);
        button.setText("");
        button = add(c.save);
        button.setText("");
        button = add(c.saveAs);
        button.setText("");
        button = add(c.savetpl);
        button.setText("");
        button = add(c.importPkg);
        button.setText("");
        button = add(c.exportPkg);
        button.setText("");
        button = add(c.exportPres);
        button.setText("");
        fonts = new JComboBox(Tools.getAvailableFontFamilyNamesAsVector());
        fonts.setMaximumRowCount(9);
        fonts.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                if (fontFamily_IgnoreChangeEvent) {
                    fontFamily_IgnoreChangeEvent = false;
                    return;
                }
                c.setFontFamily((String) e.getItem());
            }
        });
        size = new JComboBox(sizes);
        size.setEditor(new BasicComboBoxEditor());
        size.setEditable(true);
        size.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                if (fontSize_IgnoreChangeEvent) {
                    fontSize_IgnoreChangeEvent = false;
                    return;
                }
                try {
                    c.setFontSize(Integer.parseInt((String) e.getItem(), 10));
                } catch (NumberFormatException nfe) {
                }
            }
        });
        buttonToolBar = new JToolBar();
        buttonToolBar.setRollover(true);
        buttonToolBar.setFloatable(false);
        button = buttonToolBar.add(c.editId);
        button.setText("");
        for (int i = 0; i < c.iconActions.size(); ++i) {
            button = buttonToolBar.add((Action) c.iconActions.get(i));
        }
        button = buttonToolBar.add(c.cloudColor);
        button.setText("");
        buttonToolBar.setOrientation(JToolBar.VERTICAL);
    }

    public void addTextFormat() {
        JButton button;
        button = add(c.bold);
        button.setText("");
        button = add(c.italic);
        button.setText("");
        add(fonts);
        add(size);
    }

    public void selectFontSize(String fontSize) {
        fontSize_IgnoreChangeEvent = true;
        size.setSelectedItem(fontSize);
        fontSize_IgnoreChangeEvent = false;
    }

    JToolBar getLeftToolBar() {
        return buttonToolBar;
    }

    public void selectFontName(String fontName) {
        fontFamily_IgnoreChangeEvent = true;
        fonts.setEditable(true);
        fonts.setSelectedItem(fontName);
        fonts.setEditable(false);
        fontFamily_IgnoreChangeEvent = false;
    }

    void setAllActions(boolean enabled) {
        fonts.setEnabled(enabled);
        size.setEnabled(enabled);
    }
}
