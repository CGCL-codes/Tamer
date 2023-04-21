package rap;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 * A class with a form to create a new event
 * @author  Markus Bellgardt
 */
public class EventCreationView extends javax.swing.JFrame {

    private boolean edit;

    /** Creates new form EventCreationView */
    public EventCreationView() {
        initComponents();
        this.edit = true;
        spn_beamer.setVisible(false);
        spn_boarder.setVisible(false);
        spn_pc.setVisible(false);
        spn_sold.setVisible(false);
        spn_amount.setEnabled(false);
        spn_persons.setValue(1);
        spn_amount.setValue(1);
        tab_bar.setEnabledAt(3, false);
    }

    private void initComponents() {
        lblTitle = new java.awt.Label();
        btn_next = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        tab_bar = new javax.swing.JTabbedPane();
        pnl_start = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_leader = new javax.swing.JTextField();
        txt_topic = new javax.swing.JTextField();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        pnl_details = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txt_date = new javax.swing.JTextField();
        txt_time_begin = new javax.swing.JTextField();
        spn_persons = new javax.swing.JSpinner();
        txt_time_end = new javax.swing.JTextField();
        sel_freq = new javax.swing.JComboBox();
        spn_amount = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnl_requier = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        chk_beamer = new javax.swing.JCheckBox();
        spn_pc = new javax.swing.JSpinner();
        spn_beamer = new javax.swing.JSpinner();
        chk_pc = new javax.swing.JCheckBox();
        chk_boarder = new javax.swing.JCheckBox();
        spn_sold = new javax.swing.JSpinner();
        chk_sold = new javax.swing.JCheckBox();
        spn_boarder = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lst_rooms = new javax.swing.JList();
        setTitle("Veranstaltung erstellen");
        setBounds(new java.awt.Rectangle(400, 300, 0, 0));
        setName("Veranstaltung erstellen");
        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14));
        lblTitle.setName("lblTitle");
        lblTitle.setText("Veranstaltung erstellen");
        btn_next.setText("Suchen");
        btn_next.setName("btn_next");
        btn_next.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        btn_cancel.setText("Abbrechen");
        btn_cancel.setName("btn_cancel");
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });
        tab_bar.setName("tab_bar");
        pnl_start.setName("pnl_start");
        jLabel1.setFont(new java.awt.Font("Tahoma 14", 1, 12));
        jLabel1.setText("Dozent");
        jLabel1.setName("jLabel1");
        jLabel2.setFont(new java.awt.Font("Tahoma 14", 1, 12));
        jLabel2.setText("Thema");
        jLabel2.setName("jLabel2");
        txt_leader.setFont(new java.awt.Font("Tahoma 14", 1, 12));
        txt_leader.setName("txt_leader");
        txt_topic.setFont(new java.awt.Font("Tahoma 14", 1, 12));
        txt_topic.setName("txt_topic");
        jDesktopPane1.setName("jDesktopPane1");
        javax.swing.GroupLayout pnl_startLayout = new javax.swing.GroupLayout(pnl_start);
        pnl_start.setLayout(pnl_startLayout);
        pnl_startLayout.setHorizontalGroup(pnl_startLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnl_startLayout.createSequentialGroup().addGroup(pnl_startLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(pnl_startLayout.createSequentialGroup().addContainerGap().addGroup(pnl_startLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(pnl_startLayout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txt_leader)).addGroup(pnl_startLayout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txt_topic, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))).addContainerGap(122, Short.MAX_VALUE)));
        pnl_startLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jLabel1, jLabel2 });
        pnl_startLayout.setVerticalGroup(pnl_startLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnl_startLayout.createSequentialGroup().addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(72, 72, 72).addGroup(pnl_startLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(txt_leader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnl_startLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(txt_topic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(87, Short.MAX_VALUE)));
        tab_bar.addTab("Veranstaltung", pnl_start);
        pnl_details.setName("pnl_details");
        jPanel2.setName("jPanel2");
        txt_date.setName("txt_date");
        txt_time_begin.setName("txt_time_begin");
        spn_persons.setName("spn_persons");
        txt_time_end.setName("txt_time_end");
        sel_freq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Einmalig", "Täglich", "Wöchentlich", "2-Wöchentlich", "4-Wöchentlich" }));
        sel_freq.setName("sel_freq");
        sel_freq.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sel_freqActionPerformed(evt);
            }
        });
        sel_freq.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                sel_freqPropertyChange(evt);
            }
        });
        spn_amount.setModel(new javax.swing.SpinnerNumberModel());
        spn_amount.setName("spn_amount");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(sel_freq, 0, 126, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(spn_persons, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE).addComponent(txt_time_begin, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE).addComponent(txt_date, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE).addComponent(txt_time_end, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)).addComponent(spn_amount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { sel_freq, spn_persons, txt_date, txt_time_begin, txt_time_end });
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(spn_persons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txt_time_begin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txt_time_end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(sel_freq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(spn_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10, 10, 10)));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { spn_persons, txt_date, txt_time_begin });
        jPanel3.setName("jPanel3");
        jLabel3.setText("Personenzahl");
        jLabel3.setName("jLabel3");
        jLabel5.setText("Uhrzeit beginn (HH:mm)");
        jLabel5.setName("jLabel5");
        jLabel4.setText("Datum (dd.mm.yyyy)");
        jLabel4.setName("jLabel4");
        jLabel6.setText("Uhrzeit ende (HH:mm)");
        jLabel6.setName("jLabel6");
        jLabel7.setText("Regelmäßigkeit");
        jLabel7.setName("jLabel7");
        jLabel8.setText("Anzahl Termine");
        jLabel8.setName("jLabel8");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(jLabel5).addComponent(jLabel4).addComponent(jLabel6).addComponent(jLabel7).addComponent(jLabel8)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jLabel3, jLabel4, jLabel5 });
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jLabel3).addGap(18, 18, 18).addComponent(jLabel4).addGap(18, 18, 18).addComponent(jLabel5).addGap(18, 18, 18).addComponent(jLabel6).addGap(18, 18, 18).addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE).addComponent(jLabel8).addContainerGap()));
        javax.swing.GroupLayout pnl_detailsLayout = new javax.swing.GroupLayout(pnl_details);
        pnl_details.setLayout(pnl_detailsLayout);
        pnl_detailsLayout.setHorizontalGroup(pnl_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnl_detailsLayout.createSequentialGroup().addContainerGap().addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(57, Short.MAX_VALUE)));
        pnl_detailsLayout.setVerticalGroup(pnl_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnl_detailsLayout.createSequentialGroup().addContainerGap(14, Short.MAX_VALUE).addGroup(pnl_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
        tab_bar.addTab("Details", pnl_details);
        pnl_requier.setName("pnl_requier");
        jPanel5.setName("jPanel5");
        chk_beamer.setText("Beamer");
        chk_beamer.setName("chk_beamer");
        chk_beamer.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_beamerActionPerformed(evt);
            }
        });
        spn_pc.setName("spn_pc");
        spn_beamer.setName("spn_beamer");
        chk_pc.setText("Computer");
        chk_pc.setName("chk_pc");
        chk_pc.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chk_pcMouseClicked(evt);
            }
        });
        chk_pc.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_pcActionPerformed(evt);
            }
        });
        chk_boarder.setText("Tafel");
        chk_boarder.setName("chk_boarder");
        chk_boarder.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_boarderActionPerformed(evt);
            }
        });
        spn_sold.setName("spn_sold");
        chk_sold.setText("Lötstation");
        chk_sold.setName("chk_sold");
        chk_sold.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_soldActionPerformed(evt);
            }
        });
        spn_boarder.setName("spn_boarder");
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(chk_pc).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(spn_pc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(chk_beamer).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(spn_beamer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(chk_boarder).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(spn_boarder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(chk_sold).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(spn_sold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { chk_beamer, chk_boarder, chk_pc, chk_sold, spn_beamer, spn_boarder, spn_pc, spn_sold });
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(spn_pc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chk_pc)).addGap(18, 18, 18).addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(spn_beamer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chk_beamer)).addGap(18, 18, 18).addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(chk_boarder).addComponent(spn_boarder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(spn_sold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chk_sold)).addContainerGap(13, Short.MAX_VALUE)));
        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { spn_beamer, spn_boarder, spn_pc, spn_sold });
        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { chk_beamer, chk_boarder, chk_pc, chk_sold });
        javax.swing.GroupLayout pnl_requierLayout = new javax.swing.GroupLayout(pnl_requier);
        pnl_requier.setLayout(pnl_requierLayout);
        pnl_requierLayout.setHorizontalGroup(pnl_requierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnl_requierLayout.createSequentialGroup().addContainerGap().addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(179, Short.MAX_VALUE)));
        pnl_requierLayout.setVerticalGroup(pnl_requierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnl_requierLayout.createSequentialGroup().addGap(20, 20, 20).addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(20, Short.MAX_VALUE)));
        tab_bar.addTab("Anforderungen", pnl_requier);
        jPanel1.setName("jPanel1");
        jScrollPane1.setName("jScrollPane1");
        lst_rooms.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lst_rooms.setName("lst_rooms");
        jScrollPane1.setViewportView(lst_rooms);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE).addContainerGap()));
        tab_bar.addTab("Raumsuche", jPanel1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tab_bar, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(btn_cancel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE).addComponent(btn_next)).addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { btn_cancel, btn_next });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(20, 20, 20).addComponent(tab_bar, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btn_cancel).addComponent(btn_next)).addContainerGap(16, Short.MAX_VALUE)));
        pack();
    }

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {
        if (edit) {
            this.setVisible(false);
        } else {
            tab_bar.setEnabledAt(3, false);
            tab_bar.setEnabledAt(0, true);
            tab_bar.setEnabledAt(1, true);
            tab_bar.setEnabledAt(2, true);
            tab_bar.setSelectedIndex(0);
            edit = true;
            btn_next.setText("Suchen");
            btn_next.setEnabled(true);
            btn_cancel.setText("Abbrechen");
        }
    }

    private String createSelect() {
        String select = new String();
        select = "where r.capacity >= " + (Integer) spn_persons.getValue();
        if ((Integer) spn_beamer.getValue() >= 1) {
            select += " and r.obj.q_beamer >= " + (Integer) spn_beamer.getValue();
        }
        if ((Integer) spn_boarder.getValue() >= 1) {
            select += " and r.obj.q_board >= " + (Integer) spn_boarder.getValue();
        }
        if ((Integer) spn_pc.getValue() >= 1) {
            select += " and r.obj.q_pc >= " + (Integer) spn_pc.getValue();
        }
        if ((Integer) spn_sold.getValue() >= 1) {
            select += " and r.obj.q_sldstn >= " + (Integer) spn_sold.getValue();
        }
        return select;
    }

    private void calculateDates(Date date_start, Date date_end, Date[] start_dates, Date[] end_dates) {
        start_dates[0] = date_start;
        end_dates[0] = date_end;
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMANY);
        DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
        for (int i = 1; i < (Integer) spn_amount.getValue(); i++) {
            try {
                String[] tmp = df.format(start_dates[i - 1]).split("[.]");
                start_dates[i] = dtf.parse(calcNextDate(tmp) + " " + txt_time_begin.getText());
                tmp = df.format(end_dates[i - 1]).split("[.]");
                end_dates[i] = dtf.parse(calcNextDate(tmp) + " " + txt_time_end.getText());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private String calcNextDate(String[] dts) {
        String erg = new String();
        int frq = 0;
        switch(sel_freq.getSelectedIndex()) {
            case 0:
                break;
            case 1:
                frq = 1;
                break;
            case 2:
                frq = 7;
                break;
            case 3:
                frq = 14;
                break;
            case 4:
                frq = 4 * 7;
                break;
        }
        Calendar cal = new GregorianCalendar(new Integer(dts[2]), new Integer(dts[1]) - 1, new Integer(dts[0]));
        cal.add(Calendar.DAY_OF_MONTH, frq);
        erg = cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
        return erg;
    }

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {
        if (edit) {
            if ((Integer) spn_amount.getValue() < 0 || (Integer) spn_beamer.getValue() < 0 || (Integer) spn_boarder.getValue() < 0 || (Integer) spn_pc.getValue() < 0 || (Integer) spn_sold.getValue() < 0 || (Integer) spn_persons.getValue() < 0) {
                JOptionPane.showMessageDialog(this, "Es dürfen keine Werte " + "kleiner 0 sein", "Fehler", JOptionPane.ERROR_MESSAGE);
            } else if (txt_leader.getText().isEmpty() || txt_topic.getText().isEmpty() || txt_date.getText().isEmpty() || txt_time_begin.getText().isEmpty() || txt_time_end.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nicht alle benötigten  " + "Felder ausgefüllt", "Fehler", JOptionPane.ERROR_MESSAGE);
            } else {
                int amount = (Integer) spn_amount.getValue();
                REDCoordinatorDAO red_dao = DAOadmin.getREDCoordinatorDAO();
                RoomDAO room_dao = DAOadmin.getRoomDAO();
                String select = createSelect();
                DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
                Date date_start = null;
                Date date_end = null;
                try {
                    date_start = df.parse(txt_date.getText() + " " + txt_time_begin.getText());
                    date_end = df.parse(txt_date.getText() + " " + txt_time_end.getText());
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Datumsformat ungültig", "Fehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }
                Date[] start_dates = new Date[amount];
                Date[] end_dates = new Date[amount];
                calculateDates(date_start, date_end, start_dates, end_dates);
                List<REDCoordinator> redList = red_dao.findByHQL("select red from REDCoordinator red inner join red.room as r " + select);
                List<Room> roomList = room_dao.findByHQL("select r from Room r " + select);
                for (int k = 0; k < (Integer) spn_amount.getValue(); k++) {
                    for (int i = 0; i < redList.size(); i++) {
                        REDCoordinator red = redList.get(i);
                        if (red.getDtd().getBegin_date().after(end_dates[k]) || red.getDtd().getEnd_date().before(start_dates[k])) {
                        } else {
                            for (int j = 0; j < roomList.size(); j++) {
                                Room curr = roomList.get(j);
                                if (curr.getId().equals(red.getRoom().getId())) {
                                    roomList.remove((Room) curr);
                                    break;
                                }
                            }
                        }
                    }
                }
                lst_rooms.setListData(roomList.toArray());
                tab_bar.setEnabledAt(3, true);
                tab_bar.setSelectedIndex(3);
                tab_bar.setEnabledAt(0, false);
                tab_bar.setEnabledAt(1, false);
                tab_bar.setEnabledAt(2, false);
                edit = false;
                btn_next.setText("Speichern");
                btn_cancel.setText("Zurück");
                if (roomList.isEmpty()) {
                    btn_next.setEnabled(false);
                } else {
                    btn_next.setEnabled(true);
                }
            }
        } else {
            int amount = (Integer) spn_amount.getValue();
            Event event = new Event((Integer) spn_persons.getValue(), txt_topic.getText(), txt_leader.getText());
            event.setFreq(sel_freq.getSelectedIndex());
            event.setAmount((Integer) spn_amount.getValue());
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
            Date date_start = null;
            Date date_end = null;
            try {
                date_start = df.parse(txt_date.getText() + " " + txt_time_begin.getText());
                date_end = df.parse(txt_date.getText() + " " + txt_time_end.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Datumsformat ungültig", "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            Date[] start_dates = new Date[amount];
            Date[] end_dates = new Date[amount];
            calculateDates(date_start, date_end, start_dates, end_dates);
            DateTimeDuration[] dtd = new DateTimeDuration[amount];
            REDCoordinator[] red = new REDCoordinator[amount];
            DAOadmin.getEventDAO().save(event);
            for (int i = 0; i < amount; i++) {
                dtd[i] = new DateTimeDuration();
                dtd[i].setBegin_date(start_dates[i]);
                dtd[i].setEnd_date(end_dates[i]);
                red[i] = new REDCoordinator();
                red[i].setDtd(dtd[i]);
                red[i].setEvent(event);
                red[i].setRoom((Room) lst_rooms.getSelectedValue());
                DAOadmin.getDateTimeDurationDAO().save(dtd[i]);
                DAOadmin.getREDCoordinatorDAO().save(red[i]);
            }
            this.setVisible(false);
        }
    }

    private void chk_pcActionPerformed(java.awt.event.ActionEvent evt) {
        if (chk_pc.isSelected()) {
            spn_pc.setVisible(true);
            spn_pc.setValue(1);
        } else {
            spn_pc.setVisible(false);
            spn_pc.setValue(0);
        }
    }

    private void chk_pcMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void chk_beamerActionPerformed(java.awt.event.ActionEvent evt) {
        if (chk_beamer.isSelected()) {
            spn_beamer.setVisible(true);
            spn_beamer.setValue(1);
        } else {
            spn_beamer.setVisible(false);
            spn_beamer.setValue(0);
        }
    }

    private void chk_boarderActionPerformed(java.awt.event.ActionEvent evt) {
        if (chk_boarder.isSelected()) {
            spn_boarder.setVisible(true);
            spn_boarder.setValue(1);
        } else {
            spn_boarder.setVisible(false);
            spn_boarder.setValue(0);
        }
    }

    private void chk_soldActionPerformed(java.awt.event.ActionEvent evt) {
        if (chk_sold.isSelected()) {
            spn_sold.setVisible(true);
            spn_sold.setValue(1);
        } else {
            spn_sold.setVisible(false);
            spn_sold.setValue(0);
        }
    }

    private void sel_freqPropertyChange(java.beans.PropertyChangeEvent evt) {
        if (sel_freq.getSelectedIndex() != 0) {
            spn_amount.setEnabled(true);
        } else {
            spn_amount.setEnabled(false);
        }
    }

    private void sel_freqActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /**
     * Opens a form to create a new event
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new EventCreationView().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btn_cancel;

    private javax.swing.JButton btn_next;

    private javax.swing.JCheckBox chk_beamer;

    private javax.swing.JCheckBox chk_boarder;

    private javax.swing.JCheckBox chk_pc;

    private javax.swing.JCheckBox chk_sold;

    private javax.swing.JDesktopPane jDesktopPane1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JScrollPane jScrollPane1;

    private java.awt.Label lblTitle;

    private javax.swing.JList lst_rooms;

    private javax.swing.JPanel pnl_details;

    private javax.swing.JPanel pnl_requier;

    private javax.swing.JPanel pnl_start;

    private javax.swing.JComboBox sel_freq;

    private javax.swing.JSpinner spn_amount;

    private javax.swing.JSpinner spn_beamer;

    private javax.swing.JSpinner spn_boarder;

    private javax.swing.JSpinner spn_pc;

    private javax.swing.JSpinner spn_persons;

    private javax.swing.JSpinner spn_sold;

    private javax.swing.JTabbedPane tab_bar;

    private javax.swing.JTextField txt_date;

    private javax.swing.JTextField txt_leader;

    private javax.swing.JTextField txt_time_begin;

    private javax.swing.JTextField txt_time_end;

    private javax.swing.JTextField txt_topic;
}
