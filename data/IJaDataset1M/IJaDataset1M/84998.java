package rbsla.environment.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import rbsla.environment.ECAConstraints;
import rbsla.environment.view.SpringUtilities;

public class StatsQuery extends JFrame implements ActionListener, ListSelectionListener {

    private Connection con;

    private JPanel westPanel;

    private JPanel centerPanel;

    private JScrollPane serverPane;

    private JList serverList;

    private Vector servers;

    private JPanel cNorthPanel;

    private JLabel labelDate;

    private JTextField textDate;

    private JPanel cButtonPanel;

    private JButton close;

    private JButton refresh;

    private JPanel cCenterPanel;

    public StatsQuery() {
        super("Query");
        this.getContentPane().setLayout(new BorderLayout());
        try {
            Class.forName(ECAConstraints._DB_DRIVER);
            con = DriverManager.getConnection(ECAConstraints._DB_NAME, ECAConstraints._DB_USER, ECAConstraints._DB_PWD);
            westPanel = new JPanel();
            centerPanel = new JPanel(new BorderLayout());
            this.add(centerPanel, BorderLayout.CENTER);
            this.add(westPanel, BorderLayout.WEST);
            cNorthPanel = new JPanel(new FlowLayout(SwingConstants.LEFT));
            cCenterPanel = new JPanel(new SpringLayout());
            cButtonPanel = new JPanel(new FlowLayout(SwingConstants.RIGHT));
            centerPanel.add(cNorthPanel, BorderLayout.NORTH);
            centerPanel.add(cCenterPanel, BorderLayout.CENTER);
            centerPanel.add(cButtonPanel, BorderLayout.SOUTH);
            labelDate = new JLabel("TimePoint (dd-mm-yyyy HH:mm:ss");
            textDate = new JTextField(20);
            textDate.setText("01-01-2006 00:00:01");
            cNorthPanel.add(labelDate);
            cNorthPanel.add(textDate);
            close = new JButton(ECAConstraints._EXIT);
            refresh = new JButton(ECAConstraints._REFRESH);
            close.addActionListener(this);
            refresh.addActionListener(this);
            cButtonPanel.add(refresh);
            cButtonPanel.add(close);
            serverList = new JList();
            serverList.addListSelectionListener(this);
            serverPane = new JScrollPane(serverList);
            serverPane.setPreferredSize(new Dimension(150, 230));
            westPanel.add(serverPane);
            servers = new Vector();
            String getServers = "SELECT DISTINCT host FROM availability";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(getServers);
            while (rs.next()) {
                servers.add(rs.getString("host"));
            }
            serverList.setListData(servers);
            serverList.setSelectedIndex(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.setSize(new Dimension(600, 300));
        this.setResizable(false);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals(ECAConstraints._EXIT)) {
            this.dispose();
        } else if (cmd.equals(ECAConstraints._REFRESH)) {
            if (serverList.getSelectedIndex() > -1) setQueryResults();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (serverList.getSelectedIndex() > -1) setQueryResults();
    }

    private void setQueryResults() {
        HashMap infos = new HashMap();
        String srv = (String) serverList.getSelectedValue();
        String timePoint = textDate.getText();
        infos.put("Availability", "N/A");
        infos.put("Eskalation Level", "N/A");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss", Locale.GERMANY);
            sdf.setCalendar(Calendar.getInstance());
            Date d = sdf.parse(timePoint);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(d);
            Statement st = con.createStatement();
            String query = "SELECT event FROM happens " + "WHERE (event LIKE 'serverGeht(%" + srv + "%'" + "OR event LIKE 'serverAusfall(%" + srv + "%')" + "AND DateMS <=" + calendar.getTimeInMillis();
            ResultSet rs = st.executeQuery(query);
            String isAval = "OK";
            if (rs.last()) {
                String ev = rs.getString("event");
                if (ev.startsWith("serverAusfall")) {
                    isAval = "DOWN";
                }
            }
            query = "SELECT event FROM happens " + "WHERE event LIKE 'eskLvl(%" + srv + "%'" + "AND DateMS <=" + calendar.getTimeInMillis();
            String eskLvl = "N/A";
            rs = st.executeQuery(query);
            if (rs.last()) {
                String ev = rs.getString("event");
                String[] tmp = ev.split("_");
                for (int i = 0; i < tmp.length; i++) {
                    String tr = tmp[i];
                    if (tr.equals("0") || tr.equals("1") || tr.equals("2") || tr.equals("3")) {
                        eskLvl = tr;
                    }
                }
            }
            infos.put("Availability", isAval);
            infos.put("Eskalation Level", eskLvl);
            displayInfos(infos);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayInfos(HashMap infos) {
        cCenterPanel = new JPanel(new SpringLayout());
        cCenterPanel.removeAll();
        centerPanel.add(cCenterPanel, BorderLayout.CENTER);
        Set keysTmp = infos.keySet();
        Object[] keys = keysTmp.toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            String val = (String) infos.get(key);
            cCenterPanel.add(new JLabel(key));
            cCenterPanel.add(new JLabel(val));
        }
        SpringUtilities.makeCompactGrid(cCenterPanel, keys.length, 2, 8, 8, 8, 8);
        cCenterPanel.updateUI();
    }
}
