package com.agentfactory.agentspeak.debugger.inspector;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.agentfactory.agentspeak.debugger.AgentSpeakSnapshot;
import com.agentfactory.visualiser.core.ISnapshot;
import com.agentfactory.visualiser.impl.InspectorPanel;

/**
 *
 * @author remcollier
 */
public class BeliefPanel extends JPanel implements InspectorPanel, ActionListener {

    private JList list;

    private AgentSpeakSnapshot snapshot;

    private RegexPanel rep;

    private List<String> filtered;

    public BeliefPanel() {
        this.setLayout(new BorderLayout());
        filtered = new LinkedList<String>();
        list = new JList(new DefaultListModel());
        JScrollPane pane = new JScrollPane(list);
        rep = new RegexPanel();
        rep.setBAL(this);
        this.add(BorderLayout.NORTH, rep);
        this.add(BorderLayout.CENTER, pane);
    }

    public synchronized void update(ISnapshot snapshot) {
        this.snapshot = (AgentSpeakSnapshot) snapshot;
        update();
    }

    private void update() {
        DefaultListModel model = new DefaultListModel();
        try {
            filter(rep.getText());
        } catch (Exception e) {
        }
        for (String belief : filtered) {
            model.addElement(belief);
        }
        list.setModel(model);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        update();
    }

    private void filter(String reg) {
        filtered.clear();
        for (String bel : snapshot.beliefs) {
            if (bel.matches(reg)) filtered.add(bel);
        }
    }
}
