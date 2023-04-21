package jp.hpl.map.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import jp.hpl.map.mouseevent.MapCanvas;

public class ObjectPopupMenu extends JPopupMenu {

    MapCanvas canvas;

    ObjectPopupMenu(MapCanvas c) {
        super("Object menu");
        canvas = c;
        JMenuItem miProperty = new JMenuItem("Show property");
        JMenuItem miDelete = new JMenuItem("Delete object");
        miDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
            }
        });
        this.add(miProperty);
        this.add(miDelete);
    }
}
