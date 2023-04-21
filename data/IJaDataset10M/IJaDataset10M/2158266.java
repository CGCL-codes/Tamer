package expenser;

import java.awt.GridLayout;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Klasa zarz�daj�ca wydatkami. Filtracja wydatk�w wg kategorii, podkategoii, roku, miesi�ca.
 * Dodawanie, edycja, usuwanie wydakt�w. Wy�wietlanie podsumowania HTML.
 * Podgl�d wszystkich danych.
 *
 */
public class Query extends javax.swing.JPanel {

    Core myCore = null;

    private java.sql.Statement stmt;

    int id_cat[];

    int id_subcat[];

    ResultSet rs_cat, rs_subcat, rs_years;

    boolean wait = true;

    /** Creates new form Query */
    public Query(Core ptrCore) {
        initComponents();
        myCore = ptrCore;
        try {
            getCategories();
            getResultFromExpenser();
            getYears();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        jTable.getSelectionModel().addListSelectionListener(new myTableListener());
    }

    /** Kontrola zmienianej pozycji na w tabeli prezentuj�cej dane.
     *  Wy�wietlenie pe�nej informacji o danej pozycji.
     */
    class myTableListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {
            if (jTable.getSelectedRow() >= 0) {
                ResultSet rs = myCore.conn.Select("Select * from expenser_data where id=" + Integer.parseInt(jTable.getModel().getValueAt(jTable.getSelectedRow(), 0).toString()) + "");
                try {
                    if (rs.first()) {
                        jLabData.setText(rs.getString("day") + "-" + rs.getString("month") + "-" + rs.getString("year"));
                        jLabMiejsce.setText(rs.getString("place"));
                        jLabNazwa.setText(rs.getString("name"));
                        jLabBrutto.setText(Double.toString(rs.getDouble("value_brutto")));
                        jLabNetto.setText(Double.toString(rs.getDouble("value_netto")));
                        jLabVat.setText(Double.toString(rs.getDouble("value_vat")));
                        jLabSprzedawca.setText(rs.getString("company"));
                        jLabUwagi.setText(rs.getString("comment"));
                        if (rs.getInt("id_subcat") == 0) jLabCat.setText(myCore.conn.getCategoryFromId(rs.getInt("id_cat"))); else jLabCat.setText(myCore.conn.getCategoryFromId(rs.getInt("id_cat")) + "/" + myCore.conn.getSubCategoryFromId(rs.getInt("id_subcat")));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /** Ustawianie aktywnego miesi�ca. */
    public void setCurrentMonth(int m) {
        jCboYears.setSelectedIndex(jCboYears.getItemCount() - 1);
        jCboMonths.setSelectedIndex(m);
    }

    /** Pobranie przefiltrowanych danych o wydatkach z tabeli data.
     *  Zaprezentowanie pobranych danych w tabeli.
     */
    public void getResultFromExpenser() throws SQLException {
        ResultSet rs, rscat, rssubcat;
        String data, kategorie, sql2 = "";
        DefaultTableModel dtm = new DefaultTableModel();
        Object[] Headers = { "Id", "Dzie�", "Miesi�c", "Rok", "Nazwa", "Brutto" };
        if (id_cat[jCboCat.getSelectedIndex()] == -1) {
            kategorie = "";
        } else {
            if (id_subcat[jCboSubCat.getSelectedIndex()] != -1) {
                kategorie = "id_cat=" + id_cat[jCboCat.getSelectedIndex()] + " and id_subcat=" + id_subcat[jCboSubCat.getSelectedIndex()];
            } else {
                kategorie = "id_cat=" + id_cat[jCboCat.getSelectedIndex()];
            }
        }
        if (jCboYears.getSelectedIndex() <= 0) {
            data = "";
        } else {
            if (jCboMonths.getSelectedIndex() <= 0) {
                data = "year=" + Integer.parseInt(jCboYears.getSelectedItem().toString());
            } else {
                data = "year=" + Integer.parseInt(jCboYears.getSelectedItem().toString()) + " and month=" + jCboMonths.getSelectedIndex();
            }
        }
        if (data != "" || kategorie != "") sql2 = "where " + data + kategorie;
        if (data != "" && kategorie != "") sql2 = "where " + data + " and " + kategorie;
        rs = myCore.conn.Select("Select id, day, month, year, name, value_brutto from expenser_data " + sql2);
        if (rs.first()) {
            myCore.conn.putTableModel(rs, dtm, Headers);
            jTable.setModel(dtm);
            double ws[] = { 0, 0.1, 0.1, 0.1, 0.5, 0.2 };
            myCore.conn.setColumnsWidth(jTable, ws, 580);
            jTable.setRowSelectionInterval(0, 0);
        } else jTable.setModel(dtm);
    }

    /** Pobiera list� lat. */
    private void getYears() throws SQLException {
        int i = 1;
        jCboYears.removeAllItems();
        rs_years = myCore.conn.Select("Select year from expenser_data group by year");
        if (rs_years.first()) {
            jCboYears.addItem("*Wszystkie");
            jCboYears.addItem(rs_years.getString("year"));
            while (rs_years.next()) {
                jCboYears.addItem(rs_years.getString("year"));
            }
        } else {
            jCboYears.addItem("*Wszystkie");
        }
        jCboYears.setSelectedIndex(0);
    }

    /** Pobiera list� kategorii. */
    public void getCategories() throws SQLException {
        int i = 1;
        wait = true;
        jCboCat.removeAllItems();
        rs_cat = myCore.conn.Select("Select id, name from expenser_cat");
        if (rs_cat.first()) {
            rs_cat.first();
            rs_cat.last();
            id_cat = new int[rs_cat.getRow() + 1];
            rs_cat.first();
            id_cat[0] = -1;
            jCboCat.addItem("*Wszystkie");
            id_cat[i] = rs_cat.getInt("id");
            jCboCat.addItem(rs_cat.getString("name"));
            while (rs_cat.next()) {
                i++;
                id_cat[i] = rs_cat.getInt("id");
                jCboCat.addItem(rs_cat.getString("name"));
            }
        } else {
            id_cat = new int[1];
            id_cat[0] = -1;
            jCboCat.addItem("*Wszystkie");
        }
        jCboCat.setSelectedIndex(0);
        getSubCategories(id_cat[jCboCat.getSelectedIndex()]);
        wait = false;
    }

    /** Pobiera list� podkategorii. */
    private void getSubCategories(int id) throws SQLException {
        int i = 1;
        wait = true;
        jCboSubCat.removeAllItems();
        if (id != -1) {
            jCboSubCat.setEnabled(true);
            rs_subcat = myCore.conn.Select("Select id, name from expenser_subcat where id_cat=" + id + ";");
            if (rs_subcat.first()) {
                rs_subcat.first();
                rs_subcat.last();
                id_subcat = new int[rs_subcat.getRow() + 1];
                rs_subcat.first();
                id_subcat[i] = rs_subcat.getInt("id");
                id_subcat[0] = -1;
                jCboSubCat.addItem("*Wszystkie");
                jCboSubCat.addItem(rs_subcat.getString("name"));
                while (rs_subcat.next()) {
                    i++;
                    id_subcat[i] = rs_subcat.getInt("id");
                    jCboSubCat.addItem(rs_subcat.getString("name"));
                }
            } else {
                id_subcat = new int[1];
                id_subcat[0] = -1;
                jCboSubCat.addItem("*Wszystkie");
            }
        } else {
            id_subcat = new int[1];
            id_subcat[0] = -1;
            jCboSubCat.addItem("*Wszystkie");
            jCboSubCat.setEnabled(false);
        }
        wait = false;
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jCboCat = new javax.swing.JComboBox();
        jCboSubCat = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jCzas = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jCboYears = new javax.swing.JComboBox();
        jCboMonths = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabVat = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabNetto = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabData = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabMiejsce = new javax.swing.JLabel();
        jLabNazwa = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabUwagi = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabSprzedawca = new javax.swing.JLabel();
        jLabCat = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabBrutto = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnRef = new javax.swing.JButton();
        jNowaPozycja = new javax.swing.JButton();
        jBtnEdycja = new javax.swing.JButton();
        jBtnDel = new javax.swing.JButton();
        jPods = new javax.swing.JButton();
        jBtnZamknij = new javax.swing.JButton();
        setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Kategorie"));
        jPanel2.setOpaque(false);
        jCboCat.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCboCatItemStateChanged(evt);
            }
        });
        jCboCat.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCboCatActionPerformed(evt);
            }
        });
        jCboSubCat.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCboSubCatItemStateChanged(evt);
            }
        });
        jCboSubCat.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCboSubCatActionPerformed(evt);
            }
        });
        jLabel7.setText("Kategoria:");
        jLabel8.setText("Podkategoria:");
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel7).add(jLabel8)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 25, Short.MAX_VALUE).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(jCboSubCat, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jCboCat, 0, 173, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jCboCat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel7)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jCboSubCat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel8)).add(10, 10, 10)));
        jCzas.setBorder(javax.swing.BorderFactory.createTitledBorder("Ramy czasowe"));
        jCzas.setOpaque(false);
        jLabel9.setText("Rok:");
        jLabel10.setText("Miesiąc:");
        jCboMonths.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "*Wszystkie", "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień" }));
        org.jdesktop.layout.GroupLayout jCzasLayout = new org.jdesktop.layout.GroupLayout(jCzas);
        jCzas.setLayout(jCzasLayout);
        jCzasLayout.setHorizontalGroup(jCzasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jCzasLayout.createSequentialGroup().addContainerGap().add(jCzasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel9).add(jLabel10)).add(73, 73, 73).add(jCzasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jCboYears, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jCboMonths, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(23, 23, 23)));
        jCzasLayout.setVerticalGroup(jCzasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jCzasLayout.createSequentialGroup().add(jCzasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel9).add(jCboYears, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCzasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel10).add(jCboMonths, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setOpaque(false);
        jTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Id", "Data", "Nazwa", "Kwota brutto" }) {

            boolean[] canEdit = new boolean[] { false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable.setOpaque(false);
        jScrollPane1.setViewportView(jTable);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pozycja", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 0, 11)));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Kategoria/podkategoria:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 130, -1));
        jLabVat.setForeground(new java.awt.Color(153, 0, 0));
        jLabVat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabVat.setText("(brak)");
        jPanel3.add(jLabVat, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, 70, -1));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Data:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, -1, -1));
        jLabNetto.setForeground(new java.awt.Color(153, 0, 0));
        jLabNetto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabNetto.setText("(brak)");
        jPanel3.add(jLabNetto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 70, -1));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Miejsce:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));
        jLabData.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabData.setForeground(new java.awt.Color(153, 0, 0));
        jLabData.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabData.setText("(brak)");
        jPanel3.add(jLabData, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 70, -1));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Sprzedawca:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 70, -1));
        jLabMiejsce.setForeground(new java.awt.Color(153, 0, 0));
        jLabMiejsce.setText("(brak)");
        jPanel3.add(jLabMiejsce, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, -1, -1));
        jLabNazwa.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabNazwa.setForeground(new java.awt.Color(153, 0, 0));
        jLabNazwa.setText("(brak)");
        jPanel3.add(jLabNazwa, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Nazwa:");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 40, -1));
        jLabel15.setText("Wartość netto:");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, -1, -1));
        jLabUwagi.setForeground(new java.awt.Color(153, 0, 0));
        jLabUwagi.setText("(brak)");
        jPanel3.add(jLabUwagi, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, -1, -1));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Uwagi:");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 40, -1));
        jLabSprzedawca.setForeground(new java.awt.Color(153, 0, 0));
        jLabSprzedawca.setText("(brak)");
        jPanel3.add(jLabSprzedawca, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));
        jLabCat.setForeground(new java.awt.Color(153, 0, 0));
        jLabCat.setText("(brak)");
        jPanel3.add(jLabCat, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, -1));
        jLabel20.setText("brutto:");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 120, -1, -1));
        jLabBrutto.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabBrutto.setForeground(new java.awt.Color(153, 0, 0));
        jLabBrutto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabBrutto.setText("(brak)");
        jPanel3.add(jLabBrutto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 70, -1));
        jLabel22.setText("VAT:");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, -1, -1));
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCzas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(15, 15, 15)).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jCzas, 0, 82, Short.MAX_VALUE).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 144, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setFloatable(false);
        jBtnRef.setBackground(new java.awt.Color(255, 255, 255));
        jBtnRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sch24.gif")));
        jBtnRef.setText("Odśwież");
        jBtnRef.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRefActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnRef);
        jNowaPozycja.setBackground(new java.awt.Color(255, 255, 255));
        jNowaPozycja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new24.gif")));
        jNowaPozycja.setText("Nowa");
        jNowaPozycja.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNowaPozycjaActionPerformed(evt);
            }
        });
        jToolBar1.add(jNowaPozycja);
        jBtnEdycja.setBackground(new java.awt.Color(255, 255, 255));
        jBtnEdycja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edt24.gif")));
        jBtnEdycja.setText("Edycja");
        jBtnEdycja.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEdycjaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnEdycja);
        jBtnDel.setBackground(new java.awt.Color(255, 255, 255));
        jBtnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/del24.gif")));
        jBtnDel.setText("Usuń");
        jBtnDel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnDel);
        jPods.setBackground(new java.awt.Color(255, 255, 255));
        jPods.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sum24.gif")));
        jPods.setText("Podsumowanie");
        jPods.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPodsActionPerformed(evt);
            }
        });
        jToolBar1.add(jPods);
        jBtnZamknij.setBackground(new java.awt.Color(255, 255, 255));
        jBtnZamknij.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/end24.gif")));
        jBtnZamknij.setText("Zamknij");
        jBtnZamknij.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnZamknijActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnZamknij);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    /** Wy�wietla zestawienie HTML. */
    private void jPodsActionPerformed(java.awt.event.ActionEvent evt) {
        TextPrint t = new TextPrint(myCore);
        t.tryb = 1;
        t.rebuild();
        myCore.getTree().add("Zestawienie HTML", t);
        myCore.getTree().setSelectedIndex(myCore.getTree().getSelectedIndex() + 1);
        java.net.URL imageURL = this.getClass().getClassLoader().getResource("images/tab.gif");
        ImageIcon myImage = new ImageIcon(imageURL);
        myCore.getTree().setIconAt(myCore.getTree().getSelectedIndex(), myImage);
    }

    private void jBtnRefActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            getResultFromExpenser();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void jBtnZamknijActionPerformed(java.awt.event.ActionEvent evt) {
        myCore.delTab();
    }

    /** Wywo�uje edycj� danego wydatku. */
    private void jBtnEdycjaActionPerformed(java.awt.event.ActionEvent evt) {
        Expense NewEditFrame = new Expense(myCore, 1);
        NewEditFrame.initQuery(this);
        ResultSet rs = myCore.conn.Select("Select * from expenser_data where id=" + Integer.parseInt(jTable.getModel().getValueAt(jTable.getSelectedRow(), 0).toString()) + "");
        NewEditFrame.id = Integer.parseInt(jTable.getModel().getValueAt(jTable.getSelectedRow(), 0).toString());
        try {
            if (rs.first()) {
                int data[] = new int[3];
                data[0] = rs.getInt("day");
                data[1] = rs.getInt("month");
                data[2] = rs.getInt("year");
                String dane[] = new String[4];
                dane[0] = rs.getString("place");
                dane[1] = rs.getString("company");
                dane[2] = rs.getString("name");
                dane[3] = rs.getString("comment");
                double kwoty[] = new double[3];
                kwoty[0] = rs.getDouble("value_netto");
                kwoty[1] = rs.getDouble("value_vat");
                kwoty[2] = rs.getDouble("value_brutto");
                int idcat = rs.getInt("id_cat");
                int idsubcat = rs.getInt("id_subcat");
                NewEditFrame.setDatas(idcat, idsubcat, data, dane, kwoty);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        NewEditFrame.setLocationRelativeTo(myCore);
        NewEditFrame.setVisible(true);
    }

    /** Zmiana podkategorii. */
    private void jCboSubCatItemStateChanged(java.awt.event.ItemEvent evt) {
        try {
            if (!wait) getResultFromExpenser();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Zmienia kategori�. */
    private void jCboCatItemStateChanged(java.awt.event.ItemEvent evt) {
        try {
            if (!wait) {
                getSubCategories(id_cat[jCboCat.getSelectedIndex()]);
                getResultFromExpenser();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void jCboSubCatActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /** Usuwanie zaznaczonej pozycji. */
    private void jBtnDelActionPerformed(java.awt.event.ActionEvent evt) {
        int id = Integer.parseInt(jTable.getModel().getValueAt(jTable.getSelectedRow(), 0).toString());
        int res = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usun�� pozycj�!", "UWAGA!", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            myCore.conn.Execute("delete from expenser_data where `id`=" + id + "");
            try {
                getResultFromExpenser();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void jCboCatActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /** Wywo�uje dodawanie nowej pozycji. */
    private void jNowaPozycjaActionPerformed(java.awt.event.ActionEvent evt) {
        Expense NewEditFrame = new Expense(myCore, 0);
        NewEditFrame.initQuery(this);
        NewEditFrame.setLocationRelativeTo(myCore);
        NewEditFrame.setVisible(true);
    }

    private javax.swing.JButton jBtnDel;

    private javax.swing.JButton jBtnEdycja;

    private javax.swing.JButton jBtnRef;

    private javax.swing.JButton jBtnZamknij;

    private javax.swing.JComboBox jCboCat;

    private javax.swing.JComboBox jCboMonths;

    private javax.swing.JComboBox jCboSubCat;

    private javax.swing.JComboBox jCboYears;

    private javax.swing.JPanel jCzas;

    private javax.swing.JLabel jLabBrutto;

    private javax.swing.JLabel jLabCat;

    private javax.swing.JLabel jLabData;

    private javax.swing.JLabel jLabMiejsce;

    private javax.swing.JLabel jLabNazwa;

    private javax.swing.JLabel jLabNetto;

    private javax.swing.JLabel jLabSprzedawca;

    private javax.swing.JLabel jLabUwagi;

    private javax.swing.JLabel jLabVat;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JButton jNowaPozycja;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JButton jPods;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable;

    private javax.swing.JToolBar jToolBar1;
}
