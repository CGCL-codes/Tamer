package sc.fgrid.gui;

import java.util.Map;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import sc.fgrid.client.Server;
import sc.fgrid.client.ServerEmbedded;
import sc.fgrid.client.ServerRemote;

/**
 * 
 * @author stoll
 */
public class WorldPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    private WorldTableModel tableModel = new WorldTableModel();

    private Map<String, Server> servers = null;

    int nServers = 0;

    String[] nicknames;

    String[] urls;

    String[] users;

    /** Creates new form WorldPanel2 */
    public WorldPanel(Map<String, Server> servers) {
        this.servers = servers;
        updateTable();
        initComponents();
        TableColumn col_nickname = jTable1.getColumnModel().getColumn(0);
        col_nickname.setPreferredWidth(150);
        TableColumn col_url = jTable1.getColumnModel().getColumn(1);
        col_url.setPreferredWidth(400);
        TableColumn col_user = jTable1.getColumnModel().getColumn(2);
        col_user.setPreferredWidth(150);
    }

    public void updateTable() {
        synchronized (servers) {
            nServers = servers.size();
            nicknames = new String[nServers];
            urls = new String[nServers];
            users = new String[nServers];
            int i = 0;
            for (String nickname : servers.keySet()) {
                nicknames[i] = nickname;
                Server server = servers.get(nickname);
                String url = null;
                if (server instanceof ServerRemote) {
                    url = ((ServerRemote) server).getURL().toString();
                } else if (server instanceof ServerEmbedded) {
                    url = "file://" + ((ServerEmbedded) server).getFgRoot();
                } else {
                    throw new RuntimeException("Unknown instanceof Server");
                }
                urls[i] = url;
                users[i] = server.getUser();
                i++;
            }
        }
        tableModel.fireTableDataChanged();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        jTable1.setModel(tableModel);
        jScrollPane2.setViewportView(jTable1);
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
        add(jPanel1);
    }

    private class WorldTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        public int getRowCount() {
            return nServers;
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int row, int col) {
            switch(col) {
                case 0:
                    return nicknames[row];
                case 1:
                    return urls[row];
                case 2:
                    return users[row];
                default:
                    throw new RuntimeException("unexpected case in switch");
            }
        }

        public String getColumnName(int col) {
            switch(col) {
                case 0:
                    return "Nickname";
                case 1:
                    return "URL";
                case 2:
                    return "User";
                default:
                    throw new RuntimeException("unexpected case in switch");
            }
        }
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTable jTable1;
}
