package net.sf.gui;

import java.io.IOException;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JToolBar;

public class MainToolbar {

    private JToolBar toolBar;

    private JComboBox mapChooser;

    public MainToolbar(KeggFrame kf) {
        super();
        toolBar = new JToolBar();
        mapChooser = new JComboBox();
        String mapId = null;
        try {
            for (Iterator<String> it = Keggmaps.getMapIterator(); it.hasNext(); ) {
                String mapDesc = it.next();
                mapId = mapDesc.split(" - ")[0];
                mapChooser.addItem(mapDesc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        toolBar.add(mapChooser);
        mapChooser.addItemListener(kf);
    }

    public JComponent getComponent() {
        return toolBar;
    }
}
