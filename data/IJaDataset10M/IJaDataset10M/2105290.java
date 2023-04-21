package gui;

import com.borland.jbcl.layout.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import net.*;
import util.collection.IConstList;

/**
 * @author  Jesper Nordenberg
 * @created  7. Mai 2002
 * @version  $Revision: 1.5 $ $Date: 2002/06/08 22:09:20 $
 */
public class HubListComponent extends JPanel {

    static ResourceBundle res = ResourceBundle.getBundle("gui.resource");

    private ListTableModel model = new ListTableModel(new String[] { res.getString("Name"), res.getString("Address"), res.getString("Description"), res.getString("Users") }, new IElementColumnCreator() {

        public Object getColumn(Object element, int column) {
            TDCHub hub = (TDCHub) element;
            switch(column) {
                case 0:
                    return hub.getName();
                case 1:
                    return hub.getHost();
                case 2:
                    return hub.getDescription();
                case 3:
                    return new Integer(hub.getUserCount());
            }
            throw new RuntimeException(res.getString("Invalid_column_"));
        }
    });

    private SortableTable hubsTable = new SortableTable(new int[] { 150, 150, -1, 50 }, model, res.getString("hubs"));

    private JButton connectButton = new JButton(res.getString("Connect"));

    private JButton updateButton = new JButton(res.getString("Update"));

    private JToolBar toolBar = new JToolBar();

    private JTextField hostField = new JTextField();

    private ArrayList listeners = new ArrayList();

    private static final ImageIcon connectIcon = new ImageIcon("images/16/connect.png");

    private static final ImageIcon updateIcon = new ImageIcon("images/16/reload.png");

    JPanel jPanel1 = new JPanel();

    JButton hostButton = new JButton();

    BorderLayout borderLayout1 = new BorderLayout();

    BorderLayout borderLayout2 = new BorderLayout();

    TitledBorder titledBorder1;

    /**
     *  Constructor for the HubListComponent object
     */
    public HubListComponent() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Adds a feature to the Listener attribute of the HubListComponent object
     *
     * @param  listener The feature to be added to the Listener attribute
     */
    public void addListener(IHubListComponentListener listener) {
        listeners.add(listener);
    }

    /**
     *  Description of the Method
     *
     * @param  hub Description of the Parameter
     */
    protected void fireHubSelected(TDCHub hub) {
        for (int i = 0; i < listeners.size(); i++) {
            ((IHubListComponentListener) listeners.get(i)).hubSelected(hub);
        }
    }

    /**
     *  Description of the Method
     */
    public void update() {
        THubList.getInstance().update();
    }

    /**
     *  Connect to selected hub
     */
    private void connect() {
        int rows[] = hubsTable.getSelectedRows();
        if (rows.length == 0) {
            return;
        }
        for (int i = 0; i < rows.length; i++) {
            ((TDCHub) model.getElement(rows[i])).connect();
            RecentHubsComponent.getInstance().addHub((TDCHub) model.getElement(rows[i]));
        }
        fireHubSelected((TDCHub) model.getElement(rows[0]));
    }

    /**
     *  Add selected Hubs to Favorites.
     */
    private void addFavorites() {
        int rows[] = hubsTable.getSelectedRows();
        if (rows.length == 0) {
            return;
        }
        for (int i = 0; i < rows.length; i++) {
            FavoriteHubsComponent.getInstance().addHub((TDCHub) model.getElement(rows[i]));
        }
    }

    /**
     *  Connect us to a hub
     *
     * @param  hub Description of the Parameter
     */
    public void connect(TDCHub hub) {
        hub.connect();
        fireHubSelected(hub);
    }

    /**
     *  Description of the Method
     *
     * @exception  Exception Description of the Exception
     */
    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)), res.getString("Manual_Connect"));
        this.setLayout(borderLayout2);
        model.setList(THubList.getInstance().getHubs());
        hubsTable.addListener(new SortableTableAdapter() {

            public void cellSelected(int row, int column) {
                connect();
            }

            public void showPopupClicked(final int row, final int column, MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem connect = new JMenuItem(res.getString("Connect"));
                JMenuItem addFavorites = new JMenuItem(res.getString("Add_to_Favorites"));
                connect.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        connect();
                    }
                });
                addFavorites.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        addFavorites();
                    }
                });
                popup.add(connect);
                popup.addSeparator();
                popup.add(addFavorites);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        hostField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String host = hostField.getText();
                TDCHub hub = host.indexOf(":") < 0 ? new TDCHub(new HostInfo(host, 411)) : new TDCHub(new HostInfo(host));
                hub.connect();
                fireHubSelected(hub);
            }
        });
        updateButton.setIcon(updateIcon);
        updateButton.setMargin(new Insets(0, 4, 0, 4));
        updateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        connectButton.setIcon(connectIcon);
        connectButton.setMargin(new Insets(0, 4, 0, 4));
        connectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        toolBar.setFloatable(false);
        hostButton.setIcon(connectIcon);
        hostButton.setText("Connect to");
        hostButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hostButton_actionPerformed(e);
            }
        });
        hostField.setMaximumSize(new Dimension(2147483647, 21));
        hostField.setMinimumSize(new Dimension(100, 21));
        hostField.setPreferredSize(new Dimension(100, 21));
        jPanel1.setLayout(borderLayout1);
        jPanel1.setBorder(titledBorder1);
        jPanel1.setMaximumSize(new Dimension(32767, 22));
        jPanel1.setMinimumSize(new Dimension(181, 22));
        jPanel1.setPreferredSize(new Dimension(181, 52));
        toolBar.add(connectButton);
        toolBar.add(updateButton);
        hubsTable.sortByColumn(3, false);
        hubsTable.getTable().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.add(toolBar, BorderLayout.NORTH);
        this.add(hubsTable, BorderLayout.CENTER);
        this.add(jPanel1, BorderLayout.SOUTH);
        jPanel1.add(hostField, BorderLayout.CENTER);
        jPanel1.add(hostButton, BorderLayout.EAST);
        update();
    }

    /**
     *  This Method is called when the "connect to" button is pressed
     *
     * @param  e Description of the Parameter
     */
    void hostButton_actionPerformed(ActionEvent e) {
        String host = hostField.getText();
        TDCHub hub = host.indexOf(":") < 0 ? new TDCHub(new HostInfo(host, 411)) : new TDCHub(new HostInfo(host));
        hub.connect();
        fireHubSelected(hub);
    }
}
