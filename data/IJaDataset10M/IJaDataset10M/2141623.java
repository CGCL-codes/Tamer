package org.rubato.composer;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.*;
import org.rubato.composer.network.JNetwork;

public class JNetworkList extends JPanel {

    public JNetworkList() {
        setLayout(new BorderLayout());
        listModel = new NetworkListModel();
        networkList = new JList(listModel);
        networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        networkList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showNetwork();
                }
                super.mouseClicked(e);
            }
        });
        add(new JScrollPane(networkList), BorderLayout.CENTER);
        newButton = new JButton(Messages.getString("JNetworkList.newnetwork"));
        add(newButton, BorderLayout.SOUTH);
    }

    public void addNewButtonAction(Action action) {
        newButton.addActionListener(action);
    }

    public void addNetwork(JNetwork jnetwork) {
        NetworkInfo info = new NetworkInfo();
        info.jnetwork = jnetwork;
        listModel.addNetwork(info);
    }

    public void removeNetwork(JNetwork jnetwork) {
        listModel.removeNetwork(jnetwork);
    }

    public void clear() {
        listModel.removeAllElements();
    }

    public JNetwork getCurrentNetwork() {
        NetworkInfo info = (NetworkInfo) networkList.getSelectedValue();
        if (info == null) {
            return null;
        }
        return info.jnetwork;
    }

    public void setShowAction(Action action) {
        showAction = action;
    }

    protected void showNetwork() {
        if (showAction != null) {
            showAction.actionPerformed(null);
        }
    }

    public void refresh() {
        listModel.refresh();
        repaint();
    }

    private JButton newButton;

    private JList networkList;

    private Action showAction;

    private NetworkListModel listModel;

    class NetworkListModel extends DefaultListModel {

        public void addNetwork(NetworkInfo info) {
            addElement(info);
            NetworkInfo[] infoList = new NetworkInfo[getSize()];
            for (int i = 0; i < getSize(); i++) {
                infoList[i] = (NetworkInfo) getElementAt(i);
            }
            Arrays.sort(infoList);
            removeAllElements();
            for (int i = 0; i < infoList.length; i++) {
                addElement(infoList[i]);
            }
        }

        public void removeNetwork(JNetwork network) {
            for (int i = 0; i < getSize(); i++) {
                NetworkInfo info = (NetworkInfo) getElementAt(i);
                if (info.jnetwork == network) {
                    removeElement(info);
                }
            }
            refresh();
        }

        public void refresh() {
            NetworkInfo[] infoList = new NetworkInfo[getSize()];
            for (int i = 0; i < getSize(); i++) {
                infoList[i] = (NetworkInfo) getElementAt(i);
            }
            Arrays.sort(infoList);
            removeAllElements();
            for (int i = 0; i < infoList.length; i++) {
                addElement(infoList[i]);
            }
        }

        private static final long serialVersionUID = 350660102483018392L;
    }

    class NetworkInfo implements Comparable<NetworkInfo> {

        public JNetwork jnetwork;

        public String toString() {
            return jnetwork.toString();
        }

        public int compareTo(NetworkInfo ni) {
            return jnetwork.toString().compareTo(ni.jnetwork.toString());
        }
    }

    private static final long serialVersionUID = 6664745520931046206L;
}
