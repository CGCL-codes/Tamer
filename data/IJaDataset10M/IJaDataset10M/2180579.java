package hogs.tools;

import hogs.common.Controller;
import hogs.common.InternalException;
import hogs.net.NetException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * Uses the simple Ping implementation to present a graphical interface to the
 * user.
 * 
 * @author dapachec
 */
public class JPing {

    /** runs the thing */
    public static void main(String[] args) {
        JPing pinger = null;
        if (args.length > 0) {
            if (args.length > 1) pinger = new JPing(args[0], Integer.parseInt(args[1]), true); else pinger = new JPing(args[0], true);
        } else pinger = new JPing(true);
        pinger.run();
    }

    private Ping m_pinger;

    private boolean m_modal;

    private JLabel m_status = new JLabel("Ready");

    private JFrame m_frame;

    private JTextField m_hostfield = new JTextField(15);

    private JTextField m_portfield = new JTextField(4);

    private JButton m_connectbutton = new JButton("Ping");

    private JButton m_joinbutton = new JButton("Play!");

    private JLabel m_sname = new JLabel();

    private JLabel m_map = new JLabel();

    private JLabel m_time = new JLabel();

    private JLabel m_players = new JLabel();

    private JLabel m_pingtime = new JLabel();

    private JTable m_table;

    private String[] m_t_colnames = new String[] { "id", "Handle", "Frags", "Deaths" };

    private String[][] m_t_rowdata = new String[][] {};

    private JLabel m_hostname = new JLabel();

    private JLabel m_owner = new JLabel();

    public JPing(String hn, int port, boolean modal) {
        this(modal);
        m_pinger = new Ping(hn, port);
        m_hostfield.setText(hn);
        m_portfield.setText(String.valueOf(port));
    }

    public JPing(String hn, boolean modal) {
        this(modal);
        m_pinger = new Ping(hn);
        m_hostfield.setText(hn);
        m_portfield.setText(String.valueOf(hogs.net.server.Server.DEFAULT_PORT));
    }

    public JPing(boolean modal) {
        m_modal = modal;
        m_frame = new JFrame("JPing (hogs)");
        m_frame.setUndecorated(false);
        if (modal) m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_frame.setContentPane(buildMainPanel());
        m_hostfield.setText("localhost");
        m_portfield.setText(String.valueOf(hogs.net.server.Server.DEFAULT_PORT));
        clear();
    }

    private JPanel buildMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton quitButton = new JButton("Close");
        quitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_modal) System.exit(0); else m_frame.dispose();
            }
        });
        panel.add(buildConnectPanel());
        panel.add(new JSeparator());
        panel.add(buildInfoPanel());
        panel.add(new JSeparator());
        panel.add(buildTablePanel());
        panel.add(new JSeparator());
        panel.add(quitButton);
        panel.add(new JSeparator());
        panel.add(m_status);
        return panel;
    }

    /**
     * @return the panel with server-specific information
     */
    private JPanel buildInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Hostname:"));
        panel.add(m_hostname);
        panel.add(new JLabel("Ping time:"));
        panel.add(m_pingtime);
        panel.add(new JLabel("Server name:"));
        panel.add(m_sname);
        panel.add(new JLabel("Owner:"));
        panel.add(m_owner);
        panel.add(new JLabel("Current map:"));
        panel.add(m_map);
        panel.add(new JLabel("Time:"));
        panel.add(m_time);
        panel.add(new JLabel("Players:"));
        panel.add(m_players);
        return panel;
    }

    private JPanel buildTablePanel() {
        m_table = new JTable(new AbstractTableModel() {

            private static final long serialVersionUID = 3546082453920690225L;

            public String getColumnName(int col) {
                return m_t_colnames[col];
            }

            public int getRowCount() {
                return m_t_rowdata.length;
            }

            public int getColumnCount() {
                return m_t_colnames.length;
            }

            public Object getValueAt(int row, int col) {
                return m_t_rowdata[row][col];
            }

            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });
        JPanel tr = new JPanel();
        tr.add(new JScrollPane(m_table));
        return tr;
    }

    /**
     * @return the panel which goes at the top of the thing
     */
    private JPanel buildConnectPanel() {
        JPanel panel = new JPanel();
        m_connectbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                runDoPing();
            }
        });
        m_joinbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String hostname = m_hostname.getText();
                new Thread() {

                    public void run() {
                        try {
                            if (!hostname.equals("")) {
                                Controller ctrlr = new Controller(hostname, "noteam", false);
                                ctrlr.run();
                            } else {
                                JOptionPane.showMessageDialog(m_frame, "Server not responding. Try pinging first!", "Can't play", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (InternalException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        m_joinbutton.setEnabled(false);
        panel.add(new JLabel("Host:"));
        panel.add(m_hostfield);
        panel.add(new JLabel("Port:"));
        panel.add(m_portfield);
        panel.add(m_connectbutton);
        panel.add(m_joinbutton);
        return panel;
    }

    /**
     * Runs the graphical utility
     */
    public void run() {
        if (m_pinger != null) doPing();
        m_frame.pack();
        m_frame.setVisible(true);
    }

    private void clear() {
        m_sname.setText("");
        m_map.setText("");
        m_pingtime.setText("");
        m_time.setText("");
        m_players.setText("");
    }

    private void runDoPing() {
        int port = 0;
        try {
            port = Integer.parseInt(m_portfield.getText());
            m_pinger = new Ping(m_hostfield.getText(), port);
        } catch (NumberFormatException e) {
            m_status.setText("Invalid port; using default");
            m_pinger = new Ping(m_hostfield.getText());
        }
        doPing();
    }

    /** Actually performs a ping operation */
    void doPing() {
        try {
            m_pinger.ping();
            m_sname.setText(m_pinger.getServerName());
            m_map.setText(m_pinger.getMapName());
            m_time.setText(m_pinger.getMapTime());
            m_pingtime.setText(String.valueOf(m_pinger.getPingTime()));
            m_players.setText(m_pinger.getNumConnectedPlayers() + "/" + m_pinger.getMaxPlayers());
            m_owner.setText(m_pinger.getUserName());
            m_joinbutton.setEnabled(!m_pinger.getHostName().equals(""));
            m_hostname.setText(m_pinger.getHostName());
            m_t_rowdata = new String[m_pinger.getNumConnectedPlayers()][4];
            List<Ping.ClientInfo> clients = m_pinger.getClients();
            Iterator<Ping.ClientInfo> it = clients.iterator();
            for (int i = 0; i < m_t_rowdata.length; ++i) {
                Ping.ClientInfo info = it.next();
                m_t_rowdata[i] = new String[] { String.valueOf(info.id), info.handle, String.valueOf(info.frags), String.valueOf(info.deaths) };
            }
            m_table.tableChanged(null);
        } catch (UnknownHostException e) {
            m_joinbutton.setEnabled(false);
            JOptionPane.showMessageDialog(m_frame, "Host not found.", "Ack!", JOptionPane.ERROR_MESSAGE);
            if (!m_modal) {
                m_frame.setVisible(false);
                m_frame.dispose();
                m_pinger = null;
            } else clear();
        } catch (NetException e) {
            m_joinbutton.setEnabled(false);
            JOptionPane.showMessageDialog(m_frame, e.getMessage(), "Ack!", JOptionPane.ERROR_MESSAGE);
            if (!m_modal) {
                m_frame.setVisible(false);
                m_frame.dispose();
                m_pinger = null;
            } else clear();
        }
    }
}
