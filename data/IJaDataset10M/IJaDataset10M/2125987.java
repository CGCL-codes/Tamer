package perun.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import perun.admin.AdminRI;
import perun.client.panels.IsleInformationViewer;
import perun.client.panels.UpdateCounter;
import perun.client.util.GBCreator;
import perun.client.util.ListModel;
import perun.client.util.Ticker;
import perun.common.configuration.Configuration;
import perun.common.distrib.Contact;
import perun.common.log.Log;
import perun.isle.IsleRI;

public class AdminViewer extends JPanel implements Viewer {

    private Contact lastContact;

    private final ListModel<Contact> model = new ListModel<Contact>();

    private final JTextField jAdminAddr;

    private final IsleInformationViewer jIsleInfoPanel;

    private final JList jIsleList;

    private final Ticker jTicker;

    private final UpdateCounter jUpdateCntPanel;

    public AdminViewer() {
        setLayout(new GridBagLayout());
        JLabel jAdminLabel = new JLabel("Admin HostName:");
        jAdminAddr = new JTextField(Configuration.getStringProperty("client.admin.hostname"));
        jIsleList = new JList(model);
        jIsleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jIsleList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openIsle();
                }
            }
        });
        jIsleList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent evt) {
                refreshIslePanel();
            }
        });
        jIsleList.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    openIsle();
                }
            }
        });
        JScrollPane jIsleScroll = new JScrollPane(jIsleList);
        jIsleScroll.setBorder(BorderFactory.createEtchedBorder());
        jIsleInfoPanel = new IsleInformationViewer(null);
        jUpdateCntPanel = new UpdateCounter(null);
        jTicker = new Ticker(this);
        add(jAdminLabel, new GBCreator(0, 0).east().gb);
        add(jAdminAddr, new GBCreator(1, 0).fill(GridBagConstraints.HORIZONTAL).weightX(1.0).gb);
        add(jTicker, new GBCreator(2, 0).gb);
        add(jIsleScroll, new GBCreator(0, 1).fill().weight(0.3, 1.0).remHeight().gb);
        add(jIsleInfoPanel, new GBCreator(1, 1).fill().remWidth().weight(1.0, 1.0).gb);
        add(jUpdateCntPanel, new GBCreator(1, 2).remWidth().fill(GridBagConstraints.HORIZONTAL).gb);
        addHierarchyListener(new HierarchyListener() {

            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) jTicker.setVisibility(isShowing());
            }
        });
    }

    public Contact getSelectedContact() {
        int ind = jIsleList.getSelectedIndex();
        return model.get(ind);
    }

    private void refreshIslePanel() {
        Contact con = getSelectedContact();
        if (lastContact == con) return;
        if ((lastContact != null) && (con != null)) {
            if (lastContact.getID() == con.getID()) return;
        }
        lastContact = con;
        IsleRI isle = null;
        try {
            if (con != null) isle = (IsleRI) con.getRemoteObject();
        } catch (Exception e) {
            Client.netError("Failed to retrieve isle info", e);
        }
        jIsleInfoPanel.setIsle(isle);
        jUpdateCntPanel.setIsle(isle);
        Client.getInstance().updateMenuItems();
    }

    private void refreshIsles() {
        try {
            Contact con = Contact.getContact(jAdminAddr.getText());
            AdminRI remoteAdmin = (AdminRI) con.getRemoteObject();
            model.setData(remoteAdmin.getContacts());
        } catch (RemoteException re) {
            Log.exception(Log.INFO, "no registry is running", re);
        } catch (Exception e) {
            Client.netError("Exception while retrieving list of isles from admin", e);
        }
    }

    private void openIsle() {
        Client.getInstance().openIsle(getSelectedContact());
    }

    public void refreshAll() {
        refresh();
    }

    public void refresh() {
        refreshIsles();
        refreshIslePanel();
        jIsleInfoPanel.refresh();
        jUpdateCntPanel.refresh();
    }
}
