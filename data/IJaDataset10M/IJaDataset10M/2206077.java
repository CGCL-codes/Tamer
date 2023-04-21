package ircam.jmax.editors.sequence.menus;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import ircam.jmax.toolkit.*;
import ircam.jmax.toolkit.actions.*;
import ircam.jmax.editors.sequence.*;
import ircam.jmax.editors.sequence.actions.*;
import ircam.jmax.editors.sequence.track.*;

public class TempoBarPopupMenu extends JPopupMenu {

    public TempoBarPopupMenu(TempoBar bar) {
        super();
        tempoBar = bar;
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JRadioButtonMenuItem itm = (JRadioButtonMenuItem) e.getSource();
                tempoBar.setPropertyToDraw(itm.getText(), itm.isSelected());
            }
        };
        initMenu();
        pack();
    }

    public void initMenu() {
        String name;
        JRadioButtonMenuItem item;
        FtsTrackObject markers = tempoBar.getMarkers();
        if (markers != null) for (Enumeration e = markers.getPropertyNames(); e.hasMoreElements(); ) {
            name = (String) e.nextElement();
            if (!name.equals("type")) {
                item = new JRadioButtonMenuItem(name);
                item.addActionListener(listener);
                add(item);
            }
        }
    }

    public void update() {
        for (int i = 0; i < tempoBar.propertyToDraw.length; i++) ((JMenuItem) getComponent(i)).setSelected(tempoBar.propertyToDraw[i]);
    }

    TempoBar tempoBar = null;

    ActionListener listener = null;
}
