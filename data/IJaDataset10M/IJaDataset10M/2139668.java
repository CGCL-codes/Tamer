package com.agentfactory.teleoreactive.debugger.inspector;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.agentfactory.teleoreactive.debugger.TeleoReactiveSnapshot;
import com.agentfactory.visualiser.core.ISnapshot;
import com.agentfactory.visualiser.impl.InspectorPanel;

public class SensorPanel extends JPanel implements InspectorPanel {

    private JList list;

    private TeleoReactiveSnapshot snapshot;

    public SensorPanel() {
        this.setLayout(new BorderLayout());
        list = new JList(new DefaultListModel());
        JScrollPane pane = new JScrollPane(list);
        this.add(BorderLayout.CENTER, pane);
    }

    public synchronized void update(ISnapshot snapshot) {
        this.snapshot = (TeleoReactiveSnapshot) snapshot;
        update();
    }

    private void update() {
        DefaultListModel model = new DefaultListModel();
        for (String action : snapshot.Sensors) {
            model.addElement(action);
        }
        list.setModel(model);
    }
}
