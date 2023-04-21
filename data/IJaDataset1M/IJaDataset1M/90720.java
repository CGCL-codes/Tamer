package knu.dmrl.ipamss.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import knu.dmrl.ipamss.packet.PacketManager;
import jpcap.*;

public class MainFrame extends JFrame {

    private UserGroupPanel ugp;

    private IpListPanel ilp;

    public String framework = "embedded";

    public String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    public String protocol = "jdbc:derby:";

    public Connection conn = null;

    public Statement st = null;

    public MainFrame() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(protocol + "IPAMSS");
            conn.setAutoCommit(false);
            st = conn.createStatement();
            ilp = new IpListPanel();
            loadDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("IP�ּ� �� ���� ���α׷�");
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menu = new JMenu("���α׷�");
        menuBar.add(menu);
        JMenuItem item = new JMenuItem("DB ��");
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getFrame(), "DB�� ���մϴ�.");
            }
        };
        item.addActionListener(listener);
        menu.add(item);
        item = new JMenuItem("���α׷� ����");
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        item.addActionListener(listener);
        menu.add(item);
        menu = new JMenu("����� ��");
        menuBar.add(menu);
        item = new JMenuItem("����� �߰�");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String usrName = JOptionPane.showInputDialog(getFrame(), "�̸��� �Է��ϼ���:");
                String usrCode = JOptionPane.showInputDialog(getFrame(), "�й��� �Է��ϼ���:");
                int stuNum = Integer.parseInt(usrCode);
                String usrTel = JOptionPane.showInputDialog(getFrame(), "����ó�� �Է��ϼ���:");
                String usrGroup = JOptionPane.showInputDialog(getFrame(), "�׷��� �Է��ϼ���:");
                String ipAddr = JOptionPane.showInputDialog(getFrame(), "IP�ּҸ� �Է��ϼ���:");
                String macAddr = JOptionPane.showInputDialog(getFrame(), "MAC�ּҸ� �Է��ϼ���:");
                boolean ipSet = false;
                boolean macSet = false;
                Calendar cal = Calendar.getInstance();
                cal.add(cal.YEAR, 1);
                String date = cal.get(cal.YEAR) + "-" + cal.get(cal.MONTH) + "-" + cal.get(cal.DAY_OF_MONTH);
                try {
                    String userSQL = "insert into userList values (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(userSQL);
                    pstmt.setString(1, usrName);
                    pstmt.setInt(2, stuNum);
                    pstmt.setString(3, usrTel);
                    pstmt.setString(4, usrGroup);
                    pstmt.executeUpdate();
                    String addrSQL = "insert into addrList values (?, ?, ?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(addrSQL);
                    pstmt.setInt(1, stuNum);
                    pstmt.setString(2, ipAddr);
                    pstmt.setString(3, macAddr);
                    pstmt.setBoolean(4, ipSet);
                    pstmt.setBoolean(5, macSet);
                    pstmt.setString(6, date);
                    pstmt.executeUpdate();
                    conn.commit();
                    ArrayList<Object> tmp = new ArrayList<Object>();
                    tmp.add(usrName);
                    tmp.add(ipAddr);
                    tmp.add(macAddr);
                    tmp.add(ipSet);
                    tmp.add(macSet);
                    tmp.add(date);
                    ilp.tm.addColumn(tmp);
                    ilp.ipTable.repaint();
                } catch (Exception se) {
                    se.printStackTrace();
                }
            }
        };
        item.addActionListener(listener);
        item = new JMenuItem("����� �˻�");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(getFrame(), "�̸��� �Է��ϼ���:");
                if (name == null) return;
                String sql = "select * from userList where usrName = '" + name + "'";
                try {
                    ResultSet rs = st.executeQuery(sql);
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(getFrame(), "����ڰ� ����ϴ�");
                        return;
                    }
                    String usrName = rs.getString("usrName");
                    int stuNum = rs.getInt("stuNum");
                    String usrTel = rs.getString("usrTel");
                    String usrGroup = rs.getString("usrGroup");
                    JOptionPane.showMessageDialog(getFrame(), "�̸�: " + usrName + "\n�й�: " + stuNum + "\n����ó: " + usrTel + "\n�׷�: " + usrGroup);
                } catch (Exception se) {
                    se.printStackTrace();
                }
            }
        };
        item.addActionListener(listener);
        item = new JMenuItem("����� ����");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(getFrame(), "�̸��� �Է��ϼ���:");
                if (name == null) return;
                String sql = "select stuNum from userList where usrName = '" + name + "'";
                try {
                    ResultSet rs = st.executeQuery(sql);
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(getFrame(), "����ڰ� ����ϴ�");
                        return;
                    }
                    int stuNum = rs.getInt("stuNum");
                    st.executeUpdate("delete from addrList where usrCode = " + stuNum);
                    st.executeUpdate("delete from userList where usrName = '" + name + "'");
                    conn.commit();
                } catch (Exception se) {
                    se.printStackTrace();
                }
                if (ilp.tm.delColumn(name)) {
                    JOptionPane.showMessageDialog(getFrame(), "�����: " + name + "�� �����߽��ϴ�");
                    ilp.repaint();
                } else {
                    JOptionPane.showMessageDialog(getFrame(), "�����: " + name + "�� ����ϴ�");
                }
            }
        };
        item.addActionListener(listener);
        menu = new JMenu("IP�ּ� ��");
        menuBar.add(menu);
        item = new JMenuItem("IP�ּ� �˻�");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String addr = JOptionPane.showInputDialog(getFrame(), "IP�ּҸ� �Է��ϼ���:");
                if (addr == null) return;
                String sql = "select usrCode from addrList where ipAddr = '" + addr + "'";
                try {
                    ResultSet rs = st.executeQuery(sql);
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(getFrame(), "IP�ּҰ� ����ϴ�.");
                        return;
                    }
                    int stuNum = rs.getInt("usrCode");
                    String sql2 = "select usrName, usrTel from userList where stuNum = " + stuNum;
                    ResultSet rs2 = st.executeQuery(sql2);
                    if (!rs2.next()) {
                        JOptionPane.showMessageDialog(getFrame(), "DB�� ������ �̻�!!");
                    }
                    String usrName = rs2.getString("usrName");
                    String usrTel = rs2.getString("usrTel");
                    JOptionPane.showMessageDialog(getFrame(), "IP�ּ�: " + addr + "\n�����: " + usrName + "\n����ó: " + usrTel);
                } catch (Exception se) {
                    se.printStackTrace();
                }
            }
        };
        item.addActionListener(listener);
        item = new JMenuItem("IP�ּ� ����");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String addr = JOptionPane.showInputDialog(getFrame(), "IP�ּҸ� �Է��ϼ���:");
                if (addr == null) return;
                try {
                    String sql = "update addrList set ipSet = 1 where ipAddr = ?";
                    PreparedStatement psUpdate = conn.prepareStatement(sql);
                    ArrayList values = new ArrayList();
                    values.add(psUpdate);
                    psUpdate.setString(1, addr);
                    psUpdate.executeUpdate();
                    conn.commit();
                } catch (Exception se) {
                    JOptionPane.showMessageDialog(getFrame(), se.getMessage());
                }
                for (int i = 0; i < ilp.tm.getRowCount(); i++) {
                    String ip = (String) ilp.tm.getValueAt(i, 1);
                    if (ip.equals(addr)) {
                        ilp.tm.setValueAt(true, i, 3);
                        ilp.repaint();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(getFrame(), "IP�ּҰ� �������� �ʽ��ϴ�");
            }
        };
        item.addActionListener(listener);
        item = new JMenuItem("MAC�ּ� ����");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String addr = JOptionPane.showInputDialog(getFrame(), "IP�ּҸ� �Է��ϼ���:");
                if (addr == null) return;
                try {
                    String sql = "update addrList set macSet = 1 where ipAddr = ?";
                    PreparedStatement psUpdate = conn.prepareStatement(sql);
                    ArrayList values = new ArrayList();
                    values.add(psUpdate);
                    psUpdate.setString(1, addr);
                    psUpdate.executeUpdate();
                    conn.commit();
                } catch (Exception se) {
                    JOptionPane.showMessageDialog(getFrame(), se.getMessage());
                }
                for (int i = 0; i < ilp.tm.getRowCount(); i++) {
                    String ip = (String) ilp.tm.getValueAt(i, 1);
                    if (ip.equals(addr)) {
                        ilp.tm.setValueAt(true, i, 4);
                        ilp.repaint();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(getFrame(), "IP�ּҰ� �������� �ʽ��ϴ�");
            }
        };
        item.addActionListener(listener);
        menu = new JMenu("���?");
        menuBar.add(menu);
        item = new JMenuItem("���? �ۼ�");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getFrame(), "xsl������ ���մϴ�.");
            }
        };
        item.addActionListener(listener);
        menu = new JMenu("����");
        item = new JMenuItem("���� ����");
        menu.add(item);
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getFrame(), "IP�ּ� �� ���α׷�\n������: ������\n������: DMRL");
            }
        };
        item.addActionListener(listener);
        menuBar.add(menu);
    }

    public JFrame getFrame() {
        return this;
    }

    public void loadDB() {
        try {
            String sql = "select * from addrList a, userList u where a.usrCode = u.stuNum";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int usrCode = rs.getInt("usrCode");
                String ipAddr = rs.getString("ipAddr");
                String macAddr = rs.getString("macAddr");
                int ipSet = rs.getInt("ipSet");
                int macSet = rs.getInt("macSet");
                Date d = rs.getDate("useDue");
                String usrName = rs.getString("usrName");
                ArrayList<Object> tmp = new ArrayList<Object>();
                tmp.add(usrName);
                tmp.add(ipAddr);
                tmp.add(macAddr);
                if (ipSet == 1) {
                    tmp.add(true);
                } else {
                    tmp.add(false);
                }
                if (macSet == 1) {
                    tmp.add(true);
                } else {
                    tmp.add(false);
                }
                tmp.add(d.toString());
                ilp.tm.addColumn(tmp);
            }
            ilp.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MainFrame f = new MainFrame();
        JSplitPane jsp = new JSplitPane();
        jsp.setDividerSize(1);
        f.ugp = new UserGroupPanel();
        f.ugp.setFrame(f);
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jsp.setLeftComponent(f.ugp);
        jsp.setRightComponent(f.ilp);
        f.add(jsp);
        f.setVisible(true);
        try {
            NetworkInterface[] devices = JpcapCaptor.getDeviceList();
            JpcapCaptor jpcap = JpcapCaptor.openDevice(devices[0], 65535, false, 20);
            jpcap.setFilter("arp", true);
            jpcap.loopPacket(-1, new PacketManager(jpcap));
        } catch (Exception e) {
            System.err.println("PacketManager Can't Running!!");
            e.printStackTrace();
        }
    }
}
