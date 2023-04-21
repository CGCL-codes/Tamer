package freemind.controller;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class MainToolBar extends JToolBar {

    private static final String[] zooms = { "50%", "75%", "100%", "125%", "150%" };

    JComboBox zoom;

    public MainToolBar(final Controller c) {
        JButton button;
        button = add(c.previousMap);
        button.setText("");
        button = add(c.nextMap);
        button.setText("");
        zoom = new JComboBox(zooms);
        zoom.setSelectedItem("100%");
        add(zoom);
        zoom.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String dirty = (String) e.getItem();
                String cleaned = dirty.substring(0, dirty.length() - 1);
                c.setZoom(Integer.parseInt(cleaned, 10) / 100F);
            }
        });
    }

    public void setAllActions(boolean enabled) {
        zoom.setEnabled(enabled);
    }
}
