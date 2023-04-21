package automenta.netention.ui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import automenta.netention.Agent;
import automenta.netention.Network;

public class NetworkPanel extends JPanel {

    private Network net;

    private JPanel agentSelectPanel;

    private AgentPanel agentPanel;

    public NetworkPanel(Network net) {
        super(new BorderLayout());
        this.net = net;
        agentSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        DefaultComboBoxModel agentList = new DefaultComboBoxModel();
        final JComboBox agentSelector = new JComboBox(agentList);
        agentSelectPanel.add(agentSelector);
        agentSelector.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                viewAgent((Agent) agentSelector.getSelectedItem());
            }
        });
        for (Agent a : net.getAgents()) {
            agentList.addElement(a);
        }
        add(agentSelectPanel, BorderLayout.NORTH);
        viewAgent(net.getAgents().get(0));
    }

    protected void viewAgent(Agent a) {
        if (agentPanel != null) {
            remove(agentPanel);
        }
        agentPanel = new AgentPanel(a, net.getSchema());
        add(agentPanel, BorderLayout.CENTER);
        updateUI();
    }
}
