package smu.mm;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class testpf extends Frame implements ActionListener {

    public static final int NONE = 0;

    public static final int ADD_P = 1;

    public static final int UPDATE_P = 2;

    public static final int TOTAL_P = 3;

    public static Choice Gender, Year, Month, Day;

    TextArea display;

    TextField pf_name;

    Label lstudent_number, lmajor, lname, lphone, le_mail, lnumber;

    Button add_p, update_p, total_p, cancel;

    Connection conn;

    Statement stat;

    public static int cmd;

    public testpf() {
        super("Student_record_management_system");
        setLayout(new BorderLayout());
        display = new TextArea();
        display.setEditable(false);
        Panel left = new Panel();
        left.setLayout(new GridLayout(7, 1));
        Panel pinfo_p = new Panel();
        pinfo_p.add(new Label("Professor management"));
        Panel ppf_name = new Panel();
        ppf_name.add(new Label("Name"));
        ppf_name.add(pf_name = new TextField(10));
        Panel gender = new Panel();
        gender.add(new Label("Gender"));
        gender.add(Gender = new Choice());
        Gender.add("F");
        Gender.add("M");
        Panel pbirth_p = new Panel();
        pbirth_p.add(new Label("Birth Day"));
        Panel year = new Panel();
        year.add(new Label("Year"));
        year.add(Year = new Choice());
        for (int i = 1940; i < 2012; i++) {
            Year.add("" + i + "");
        }
        Panel month = new Panel();
        month.add(new Label("Month"));
        month.add(Month = new Choice());
        for (int i = 1; i < 13; i++) {
            Month.add("" + i + "");
        }
        Panel day = new Panel();
        day.add(new Label("Day"));
        day.add(Day = new Choice());
        for (int i = 1; i < 32; i++) {
            Day.add("" + i + "");
        }
        left.add(pinfo_p);
        left.add(ppf_name);
        left.add(gender);
        left.add(pbirth_p);
        left.add(year);
        left.add(month);
        left.add(day);
        Panel bottom = new Panel();
        bottom.add(add_p = new Button("ADD"));
        add_p.addActionListener(this);
        bottom.add(update_p = new Button("UPDATE"));
        update_p.addActionListener(this);
        bottom.add(cancel = new Button("CANCER"));
        cancel.addActionListener(this);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                destroy();
                setVisible(false);
                dispose();
                manage seoyoung = new manage();
                seoyoung.setSize(1000, 850);
                seoyoung.setVisible(true);
            }
        });
        add("Center", display);
        add("West", left);
        add("South", bottom);
        cmd = NONE;
        init();
    }

    private void init() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ce) {
            System.out.println(ce);
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Student_record_management_system", "sa", "0328");
            stat = conn.createStatement();
        } catch (SQLException se) {
            System.out.println(se);
        } finally {
        }
        initialize();
    }

    public void initialize() {
        pf_name.setEditable(false);
        Gender.setEnabled(false);
        Year.setEnabled(false);
        Month.setEnabled(false);
        Day.setEnabled(false);
    }

    private void destroy() {
        try {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
        }
    }

    public void setEditable(int n) {
        switch(n) {
            case ADD_P:
                pf_name.setEditable(true);
                Gender.setEnabled(true);
                Year.setEnabled(true);
                Month.setEnabled(true);
                Day.setEnabled(true);
                break;
            case UPDATE_P:
                pf_name.setEditable(true);
                Gender.setEnabled(true);
                Year.setEnabled(true);
                Month.setEnabled(true);
                Day.setEnabled(true);
                break;
        }
    }

    public void setEnable(int n) {
        add_p.setEnabled(false);
        update_p.setEnabled(false);
        switch(n) {
            case ADD_P:
                add_p.setEnabled(true);
                setEditable(ADD_P);
                cmd = ADD_P;
                break;
            case UPDATE_P:
                update_p.setEnabled(true);
                setEditable(UPDATE_P);
                cmd = UPDATE_P;
                break;
            case NONE:
                add_p.setEnabled(true);
                update_p.setEnabled(true);
                total_p.setEnabled(true);
        }
    }

    public void clear() {
        pf_name.setText("");
    }

    public void actionPerformed(ActionEvent e) {
        ResultSet rs = null;
        String te = null;
        Component c = (Component) e.getSource();
        try {
            if (c == add_p) {
                rs = stat.executeQuery("SELECT * FROM Professor_management");
                while (rs.next()) {
                    te = rs.getString(1);
                    System.out.print(te);
                }
                if (cmd != ADD_P) setEnable(ADD_P); else {
                    if (te == null) {
                        String vpf_name = pf_name.getText().trim();
                        String vgender = Gender.getSelectedItem();
                        String vyear = Year.getSelectedItem();
                        String vmonth = Month.getSelectedItem();
                        String vday = Day.getSelectedItem();
                        if (vpf_name == null || vgender == null || vyear == null || vmonth == null || vday == null || vpf_name.length() == 0 || vgender.length() == 0 || vyear.length() == 0 || vmonth.length() == 0 || vday.length() == 0) return;
                        String sql = "insert into Professor_management values(?,?,?,?,?) ";
                        PreparedStatement stat = conn.prepareStatement(sql);
                        stat.setString(1, vpf_name);
                        stat.setString(2, vgender);
                        stat.setString(3, vyear);
                        stat.setString(4, vmonth);
                        stat.setString(5, vday);
                        stat.executeUpdate();
                        setEnable(NONE);
                        clear();
                        cmd = NONE;
                        initialize();
                    } else {
                        display.setText("�̹� ������ �����մϴ�. ���Ÿ� �����մϴ�.");
                    }
                }
            } else if (c == update_p) {
                if (cmd != UPDATE_P) setEnable(UPDATE_P); else {
                    String vpf_name = pf_name.getText().trim();
                    String vgender = Gender.getSelectedItem();
                    String vyear = Year.getSelectedItem();
                    String vmonth = Month.getSelectedItem();
                    String vday = Day.getSelectedItem();
                    if (vpf_name == null || vpf_name.length() == 0) return;
                    String sql = "update Professor_management set Name = '" + vpf_name + "', Gender = '" + vgender + "' , Year = '" + vyear + "', Month = '" + vmonth + "', Day = '" + vday + "' Where Name= '" + vpf_name + "'";
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.executeUpdate();
                    setEnable(NONE);
                    clear();
                    cmd = NONE;
                    initialize();
                }
            } else if (c == cancel) {
                setEnable(NONE);
                initialize();
                cmd = NONE;
            }
        } catch (Exception ex) {
        }
        return;
    }
}
