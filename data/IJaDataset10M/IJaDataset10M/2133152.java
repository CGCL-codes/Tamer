package newgen.presentation.sm.smadv;

import newgen.presentation.*;
import java.util.*;
import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import newgen.presentation.sm.*;

/**
 *
 * @author  Administrator
 */
public class AddToSubscriptionList extends javax.swing.JPanel implements ListSelectionListener {

    ListSelectionModel lsm;

    ListSelectionModel lsmBudget;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.Utility utility = null;

    /** Creates new form sm_AddToSubscriptionList */
    public AddToSubscriptionList(String mode, String subscriptionId) {
        initComponents();
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));
        jPanel8.add(jLabel2, java.awt.BorderLayout.LINE_START);
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        jScrollPane1.setAutoscrolls(true);
        tSatellite.setAutoscrolls(true);
        String[] col1 = { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("LibraryName"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoOfCopies") };
        this.dtmSatellite = new javax.swing.table.DefaultTableModel(col1, 0) {

            public boolean isCellEditable(int row, int column) {
                if (column == 0) return false; else return true;
            }

            public Class getColumnClass(int columnIndex) {
                Object obj = getValueAt(0, columnIndex);
                if (obj == null) setValueAt(new Integer(0), 0, columnIndex);
                return getValueAt(0, columnIndex).getClass();
            }
        };
        this.tSatellite.setModel(this.dtmSatellite);
        tSatellite.setFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.BOLD, 12));
        this.tSatellite.getColumnModel().getColumn(0).setPreferredWidth(400);
        htsatlib = NewGenMain.getAppletInstance().getSerialManagementSatelliteLibraries();
        if (mode.equals("NEW")) {
            Object[] coldata1 = htsatlib.toArray();
            Object[] coldata = new Object[coldata1.length + 1];
            for (int i = 0; i < coldata1.length; i++) {
                coldata[i] = NewGenMain.getAppletInstance().getLibraryName(coldata1[i].toString());
            }
            coldata[coldata.length - 1] = NewGenMain.getAppletInstance().getLibraryName(NewGenMain.getAppletInstance().getLibraryID());
            String libidownnm = "";
            libidownnm = NewGenMain.getAppletInstance().getLibraryName(NewGenMain.getAppletInstance().getLibraryID());
            for (int i = 0; i < coldata.length; i++) {
                Object[] row = new Object[2];
                row[0] = coldata[i].toString();
                if (libidownnm.trim().equals(coldata[i].toString().trim())) {
                    row[1] = new Integer(1);
                } else {
                    row[1] = new Integer(0);
                }
                dtmSatellite.addRow(row);
            }
            tSatellite.removeRowSelectionInterval(0, 0);
        } else if (mode.equals("MODIFY")) {
            this.bNew.setEnabled(false);
            this.bSearch.setEnabled(false);
            this.setSubscriptionId(subscriptionId);
            java.util.Vector vtable = new java.util.Vector();
            java.util.Vector vec = this.getSubscribedLibraries();
            java.util.Hashtable ht = new java.util.Hashtable();
            for (int i = 0; i < htsatlib.size(); i++) {
                ht.put(htsatlib.get(i).toString(), "0");
            }
            ht.put(NewGenMain.getAppletInstance().getLibraryID(), "0");
            for (int i = 0; i < vec.size(); i += 2) {
                if (ht.containsKey(vec.elementAt(i))) {
                    ht.remove(vec.elementAt(i));
                    ht.put(vec.elementAt(i), vec.elementAt(i + 1));
                } else {
                    ht.put(vec.elementAt(i), "0");
                }
            }
            java.util.Enumeration enu = ht.keys();
            for (int i = 0; i < ht.size(); i++) {
                Object obj = enu.nextElement();
                Object[] row = new Object[2];
                row[0] = NewGenMain.getAppletInstance().getLibraryName(obj.toString());
                row[1] = new Integer(ht.get(obj).toString());
                dtmSatellite.addRow(row);
            }
            tSatellite.removeRowSelectionInterval(0, 0);
        }
        String[] col2 = { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Select"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetId"), "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetSource"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Balance"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CarryOverStatus"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FiscalYear") };
        this.dtmBudget = new javax.swing.table.DefaultTableModel(col2, 0) {

            public boolean isCellEditable(int row, int column) {
                if (column == 0) return true; else return false;
            }

            public Class getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }
        };
        this.tBudget.setModel(this.dtmBudget);
        this.tBudget.setRowHeight(this.tBudget.getRowHeight() + 5);
        tBudget.getColumnModel().getColumn(2).setMinWidth(0);
        tBudget.getColumnModel().getColumn(2).setPreferredWidth(0);
        tBudget.getColumnModel().getColumn(2).setMaxWidth(0);
        tBudget.getColumnModel().getColumn(0).setPreferredWidth(50);
        tBudget.getColumnModel().getColumn(1).setPreferredWidth(135);
        tBudget.getColumnModel().getColumn(3).setPreferredWidth(111);
        tBudget.getColumnModel().getColumn(4).setPreferredWidth(112);
        tBudget.getColumnModel().getColumn(5).setPreferredWidth(95);
        tBudget.getColumnModel().getColumn(6).setPreferredWidth(68);
        tBudget.getColumnModel().getColumn(7).setPreferredWidth(62);
        lsm = tSatellite.getSelectionModel();
        tSatellite.setSelectionModel(lsm);
        lsm.addListSelectionListener(this);
        lsmBudget = tBudget.getSelectionModel();
        tBudget.setSelectionModel(lsmBudget);
        tfTitle.setEditable(true);
        tfISSN.setEditable(true);
        tfPublisher.setEditable(true);
        bNew.setSelected(true);
        tSatellite.setRowSelectionInterval(0, 0);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        tfTitle.grabFocus();
    }

    /**
     *
     */
    public void reloadLocales() {
        String[] col2 = { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Select"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetId"), "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetSource"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Balance"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CarryOverStatus"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FiscalYear") };
        this.dtmBudget.setColumnIdentifiers(col2);
        tBudget.getColumnModel().getColumn(2).setMinWidth(0);
        tBudget.getColumnModel().getColumn(2).setMaxWidth(0);
        tBudget.getColumnModel().getColumn(2).setPreferredWidth(0);
        tBudget.getColumnModel().getColumn(0).setPreferredWidth(50);
        tBudget.getColumnModel().getColumn(1).setPreferredWidth(135);
        tBudget.getColumnModel().getColumn(3).setPreferredWidth(111);
        tBudget.getColumnModel().getColumn(4).setPreferredWidth(112);
        tBudget.getColumnModel().getColumn(5).setPreferredWidth(95);
        tBudget.getColumnModel().getColumn(6).setPreferredWidth(68);
        tBudget.getColumnModel().getColumn(7).setPreferredWidth(62);
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterNotesAboutGratis"));
        bSearch.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchCatalogue"));
        bSearch.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchCatalogue"));
        bNew.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NewEntryEdit"));
        bNew.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NewEntryEdit"));
        chbSkip.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SkipOrderProcessYouCanStartRegisteringIssues"));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Acquisitionby"));
        radSubscription.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Subscription"));
        radGratis.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Gratis"));
        radExchange.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Exchange"));
        lTitle.setText(NewGenMain.getAppletInstance().getMyResource().getString("Title"));
        lPublisher.setText(NewGenMain.getAppletInstance().getMyResource().getString("Publisher"));
        lISSN.setText(NewGenMain.getAppletInstance().getMyResource().getString("ISSN"));
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Notes"));
        String[] col1 = { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("LibraryName"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoOfCopies") };
        this.dtmSatellite.setColumnIdentifiers(col1);
        if (dtmSatellite.getRowCount() > 0) {
            tSatellite.setRowSelectionInterval(0, 0);
        }
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AvailableBudgetAllocations"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(170, 0, 0)));
        tfTitle.grabFocus();
    }

    public void getBudgetDetails() {
        dtmBudget.setRowCount(0);
        java.util.Vector vAllBudgets = retrieveAllBudgets();
        for (int i = 0; i < vAllBudgets.size(); i += 8) {
            java.util.Vector vRow = new java.util.Vector();
            vRow.addElement(vAllBudgets.elementAt(i));
            vRow.addElement(vAllBudgets.elementAt(i + 1));
            vRow.addElement(vAllBudgets.elementAt(i + 2));
            vRow.addElement(vAllBudgets.elementAt(i + 3));
            vRow.addElement(vAllBudgets.elementAt(i + 4));
            vRow.addElement(vAllBudgets.elementAt(i + 5));
            vRow.addElement(vAllBudgets.elementAt(i + 6));
            vRow.addElement(vAllBudgets.elementAt(i + 7));
            this.dtmBudget.addRow(vRow);
        }
        tfTitle.grabFocus();
        setBudgetHeads();
    }

    public void registryBudgets() {
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.systemNodeForPackage(this.getClass());
            String budgets = prefs.get("BUDGETHEADS", "");
            String budgetsstr = "";
            for (int i = 0; i < dtmBudget.getRowCount(); i++) {
                boolean selected = ((Boolean) dtmBudget.getValueAt(i, 0)).booleanValue();
                if (selected) {
                    if (budgetsstr.trim().equals("")) {
                        budgetsstr = dtmBudget.getValueAt(i, 1).toString();
                    } else {
                        budgetsstr = budgetsstr + "|" + dtmBudget.getValueAt(i, 1).toString();
                    }
                }
            }
            prefs.put("BUDGETHEADS", budgetsstr.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBudgetHeads() {
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.systemNodeForPackage(this.getClass());
            String budgets = prefs.get("BUDGETHEADS", "");
            System.out.println("budgets  " + budgets);
            if (budgets != null && !budgets.trim().equals("")) {
                java.util.StringTokenizer stk = new java.util.StringTokenizer(budgets, "|");
                while (stk.hasMoreTokens()) {
                    String token = "";
                    token = stk.nextToken();
                    for (int i = 0; i < dtmBudget.getRowCount(); i++) {
                        String bhead = "";
                        bhead = dtmBudget.getValueAt(i, 1).toString();
                        if (token.trim().equalsIgnoreCase(bhead.trim())) {
                            dtmBudget.setValueAt(new Boolean(true), i, 0);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        bSearch = new javax.swing.JButton();
        bNew = new javax.swing.JToggleButton();
        chbSkip = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        radSubscription = new javax.swing.JRadioButton();
        radGratis = new javax.swing.JRadioButton();
        radExchange = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        lTitle = new javax.swing.JLabel();
        lPublisher = new javax.swing.JLabel();
        lISSN = new javax.swing.JLabel();
        tfTitle = new newgen.presentation.UnicodeTextField();
        tfPublisher = new newgen.presentation.UnicodeTextField();
        tfISSN = new newgen.presentation.UnicodeTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tSatellite = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tBudget = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textGratise = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        setPreferredSize(new java.awt.Dimension(600, 500));
        addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        jPanel4.setLayout(new java.awt.GridBagLayout());
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setFocusCycleRoot(true);
        bSearch.setMnemonic('s');
        bSearch.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchCatalogue"));
        bSearch.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchCatalogue"));
        bSearch.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSearchActionPerformed(evt);
            }
        });
        jPanel4.add(bSearch, new java.awt.GridBagConstraints());
        bNew.setMnemonic('n');
        bNew.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NewEntryEdit"));
        bNew.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NewEntryEdit"));
        bNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNewActionPerformed(evt);
            }
        });
        jPanel4.add(bNew, new java.awt.GridBagConstraints());
        chbSkip.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SkipOrderProcessYouCanStartRegisteringIssues"));
        chbSkip.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chbSkip.setNextFocusableComponent(radSubscription);
        chbSkip.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbSkipActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel4.add(chbSkip, gridBagConstraints);
        add(jPanel4);
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setFocusCycleRoot(true);
        jPanel7.setPreferredSize(new java.awt.Dimension(117, 110));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Acquisitionby"));
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel7.add(jLabel3);
        buttonGroup1.add(radSubscription);
        radSubscription.setSelected(true);
        radSubscription.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Subscription"));
        radSubscription.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radSubscriptionActionPerformed(evt);
            }
        });
        jPanel7.add(radSubscription);
        buttonGroup1.add(radGratis);
        radGratis.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Gratis"));
        radGratis.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radGratisActionPerformed(evt);
            }
        });
        jPanel7.add(radGratis);
        buttonGroup1.add(radExchange);
        radExchange.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Exchange"));
        radExchange.setNextFocusableComponent(tfTitle);
        radExchange.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radExchangeActionPerformed(evt);
            }
        });
        jPanel7.add(radExchange);
        add(jPanel7);
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 200));
        lTitle.setForeground(new java.awt.Color(165, 0, 0));
        lTitle.setText(NewGenMain.getAppletInstance().getMyResource().getString("Title"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(lTitle, gridBagConstraints);
        lPublisher.setText(NewGenMain.getAppletInstance().getMyResource().getString("Publisher"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(lPublisher, gridBagConstraints);
        lISSN.setText(NewGenMain.getAppletInstance().getMyResource().getString("ISSN"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(lISSN, gridBagConstraints);
        tfTitle.setColumns(40);
        tfTitle.setEditable(false);
        tfTitle.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTitleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfTitle, gridBagConstraints);
        tfPublisher.setColumns(40);
        tfPublisher.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfPublisher, gridBagConstraints);
        tfISSN.setEditable(false);
        tfISSN.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfISSNActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel1.add(tfISSN, gridBagConstraints);
        add(jPanel1);
        jPanel2.setLayout(new java.awt.BorderLayout());
        tSatellite.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        tSatellite.setNextFocusableComponent(tBudget);
        jScrollPane3.setViewportView(tSatellite);
        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);
        add(jPanel2);
        jPanel5.setLayout(new java.awt.CardLayout());
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AvailableBudgetAllocations"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(170, 0, 0)));
        tBudget.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        tBudget.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tBudget);
        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel5.add(jPanel3, "budget");
        jPanel5.add(jPanel6, "nobudget");
        jPanel8.setLayout(new java.awt.BorderLayout());
        textGratise.setColumns(10);
        textGratise.setEditable(false);
        textGratise.setRows(10);
        jScrollPane2.setViewportView(textGratise);
        jPanel8.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jLabel2.setForeground(new java.awt.Color(170, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Notes"));
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setAlignmentY(0.0F);
        jPanel8.add(jLabel2, java.awt.BorderLayout.WEST);
        jLabel1.setForeground(new java.awt.Color(0, 0, 170));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterNotesAboutGratis"));
        jPanel8.add(jLabel1, java.awt.BorderLayout.NORTH);
        jPanel5.add(jPanel8, "gratis");
        add(jPanel5);
    }

    private void tfTitleActionPerformed(java.awt.event.ActionEvent evt) {
        bSearch.doClick();
    }

    private void radExchangeActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.CardLayout cal = (java.awt.CardLayout) jPanel5.getLayout();
        if (radExchange.isSelected()) {
            chbSkip.setEnabled(false);
            chbSkip.setSelected(false);
            tfTitle.grabFocus();
            this.textGratise.setEditable(true);
            this.textGratise.setEnabled(true);
            cal.show(jPanel5, "gratis");
            jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterNotesAboutExchange"));
        } else {
            chbSkip.setEnabled(true);
            chbSkip.setSelected(true);
        }
    }

    private void radGratisActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.CardLayout cal = (java.awt.CardLayout) jPanel5.getLayout();
        if (radGratis.isSelected()) {
            chbSkip.setEnabled(false);
            chbSkip.setSelected(false);
            tfTitle.grabFocus();
            this.textGratise.setEditable(true);
            this.textGratise.setEnabled(true);
            cal.show(jPanel5, "gratis");
            jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterNotesAboutGratis"));
        }
    }

    private void radSubscriptionActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.CardLayout cal = (java.awt.CardLayout) jPanel5.getLayout();
        if (radSubscription.isSelected()) {
            chbSkip.setEnabled(true);
            tfTitle.grabFocus();
            if (!chbSkip.isSelected()) {
                cal.show(jPanel5, "budget");
            } else {
                cal.show(jPanel5, "nobudget");
            }
        }
    }

    private void chbSkipActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.CardLayout cal = (java.awt.CardLayout) jPanel5.getLayout();
        if (!chbSkip.isSelected()) {
            if (radGratis.isSelected()) {
                cal.show(jPanel5, "gratis");
            } else {
                cal.show(jPanel5, "budget");
            }
        } else {
            if (radSubscription.isSelected()) {
                cal.show(jPanel5, "nobudget");
            } else {
                cal.show(jPanel5, "gratis");
            }
        }
    }

    private void bSearchActionPerformed(java.awt.event.ActionEvent evt) {
        bNew.setSelected(false);
        searchCatalogueDialog = new newgen.presentation.cataloguing.SearchCatalogueDialog(searchCatalogueDialog.CATLOGUE_RECORD_SELECTION_MODE, tfTitle.getText(), "TITLE");
        String[] catreckey = searchCatalogueDialog.getCataloguerecordkey();
        for (int i = 0; i < catreckey.length; i++) catalogueRecordId = catreckey[0].toString();
        ownerLibraryId = catreckey[1].toString();
        java.util.Hashtable htx = null;
        htx = new java.util.Hashtable();
        htx = searchCatalogueDialog.scl.getBasicDetailsForSelectedRecord();
        tfTitle.setEditable(false);
        this.tfISSN.setEditable(false);
        tfPublisher.setEditable(false);
        tfTitle.setText(htx.get("Title").toString());
        tfPublisher.setText(htx.get("Publisher").toString());
        tfISSN.setText(htx.get("ISSN").toString());
    }

    private void bNewActionPerformed(java.awt.event.ActionEvent evt) {
        bSearch.setSelected(false);
        if (bNew.isSelected()) {
            tfTitle.setEditable(true);
            tfPublisher.setEditable(true);
            tfISSN.setEditable(true);
            tfTitle.grabFocus();
        } else {
            tfTitle.setEditable(false);
            tfPublisher.setEditable(false);
            tfISSN.setEditable(false);
        }
    }

    private void formFocusGained(java.awt.event.FocusEvent evt) {
    }

    private void tfISSNActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        String lib1 = null;
        String noOfCopies = null;
        if (dtmBudget.getRowCount() > 0) {
            setBudgetHeads();
        }
        if (!listSelectionEvent.getValueIsAdjusting()) {
            if (listSelectionEvent.getSource() == lsm) {
                if (tSatellite.getSelectedRowCount() > 0) {
                    try {
                        lib1 = libraryId;
                        if (dtmBudget.getRowCount() > 0) {
                            Vector budgetList = new Vector();
                            for (int j = 0; j < dtmBudget.getRowCount(); j++) {
                                if (dtmBudget.getValueAt(j, 0).toString() == "true") {
                                    budgetList.add(dtmBudget.getValueAt(j, 1).toString());
                                }
                            }
                            if (htBudgetLib != null) htBudgetLib.put(lib1, budgetList);
                            dtmBudget.setNumRows(0);
                        }
                        if (mode.equals("NEW")) {
                            java.util.Vector vAllBudgets = retrieveAllBudgets();
                            if (vAllBudgets.size() > 0) {
                                for (int i = 0; i < vAllBudgets.size(); i += 8) {
                                    java.util.Vector vRow = new java.util.Vector();
                                    vRow.addElement(vAllBudgets.elementAt(i));
                                    vRow.addElement(vAllBudgets.elementAt(i + 1));
                                    vRow.addElement(vAllBudgets.elementAt(i + 2));
                                    vRow.addElement(vAllBudgets.elementAt(i + 3));
                                    vRow.addElement(vAllBudgets.elementAt(i + 4));
                                    vRow.addElement(vAllBudgets.elementAt(i + 5));
                                    vRow.addElement(vAllBudgets.elementAt(i + 6));
                                    vRow.addElement(vAllBudgets.elementAt(i + 7));
                                    this.dtmBudget.addRow(vRow);
                                }
                            }
                        } else if (mode.equals("MODIFY")) {
                            java.util.Vector vAllBudgets = retrieveAllBudgets();
                            java.util.Vector vSubBudgets = getSubscribedBudgets(dtmSatellite.getValueAt(tSatellite.getSelectedRow(), 0).toString(), "OLD");
                            java.util.Hashtable ht = new java.util.Hashtable();
                            for (int i = 0; i < vAllBudgets.size(); i += 8) {
                                ht.put(vAllBudgets.elementAt(i + 1), "false");
                            }
                            for (int i = 0; i < vSubBudgets.size(); i += 7) {
                                ht.contains(vSubBudgets.elementAt(i + 1));
                                ht.remove(vSubBudgets.elementAt(i + 1));
                                ht.put(vSubBudgets.elementAt(i + 1), "true");
                            }
                            if (vAllBudgets.size() > 0) {
                                for (int i = 0; i < vAllBudgets.size(); i += 8) {
                                    if (ht.get(vAllBudgets.elementAt(i + 1)).toString().equals("true")) {
                                        java.util.Vector vRow = new java.util.Vector();
                                        vRow.addElement(new Boolean(true));
                                        vRow.addElement(vAllBudgets.elementAt(i + 1));
                                        vRow.addElement(vAllBudgets.elementAt(i + 2));
                                        vRow.addElement(vAllBudgets.elementAt(i + 3));
                                        vRow.addElement(vAllBudgets.elementAt(i + 4));
                                        vRow.addElement(vAllBudgets.elementAt(i + 5));
                                        vRow.addElement(vAllBudgets.elementAt(i + 6));
                                        vRow.addElement(vAllBudgets.elementAt(i + 7));
                                        this.dtmBudget.addRow(vRow);
                                    } else if (ht.get(vAllBudgets.elementAt(i + 1)).toString().equals("false")) {
                                        java.util.Vector vRow = new java.util.Vector();
                                        vRow.addElement(new Boolean(false));
                                        vRow.addElement(vAllBudgets.elementAt(i + 1));
                                        vRow.addElement(vAllBudgets.elementAt(i + 2));
                                        vRow.addElement(vAllBudgets.elementAt(i + 3));
                                        vRow.addElement(vAllBudgets.elementAt(i + 4));
                                        vRow.addElement(vAllBudgets.elementAt(i + 5));
                                        vRow.addElement(vAllBudgets.elementAt(i + 6));
                                        vRow.addElement(vAllBudgets.elementAt(i + 7));
                                        this.dtmBudget.addRow(vRow);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                    if (htBudgetLib != null || htBudgetLib.size() > 0) {
                        if (htBudgetLib.containsKey((Object) NewGenMain.getAppletInstance().getLibraryId(dtmSatellite.getValueAt(tSatellite.getSelectedRow(), 0).toString()))) {
                            java.util.Vector vBudId = (java.util.Vector) htBudgetLib.get((Object) NewGenMain.getAppletInstance().getLibraryId(dtmSatellite.getValueAt(tSatellite.getSelectedRow(), 0).toString()));
                            for (int i = 0; i < vBudId.size(); i++) {
                                for (int j = 0; j < dtmBudget.getRowCount(); j++) {
                                    if (dtmBudget.getValueAt(j, 1).toString().equals(vBudId.elementAt(i).toString())) {
                                        dtmBudget.setValueAt(new Boolean(true), j, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (dtmBudget.getRowCount() > 0) {
            setBudgetHeads();
        }
    }

    public boolean validateAddToSubScriptionList() {
        boolean valid = true;
        try {
            String staryr = "", noofcopies = "";
            try {
                Integer styr = new Integer(staryr.trim());
            } catch (NumberFormatException n) {
                valid = false;
                newgen.presentation.NewGenMain.getAppletInstance().showWarningMessage("Subscription start year should be in Integer and YYYY format");
                return valid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valid;
    }

    public void OnOkClick() {
        registryBudgets();
        tSatellite.editCellAt(0, 0);
        if (tSatellite.getSelectedRow() != -1) {
            int rowint = tSatellite.getSelectedRow();
            tSatellite.clearSelection();
            tSatellite.setRowSelectionInterval(rowint, rowint);
        }
        try {
            for (int i = 0; i < dtmSatellite.getRowCount(); i++) {
                String lib2 = NewGenMain.getAppletInstance().getLibraryId(tSatellite.getValueAt(i, 0).toString());
                if (tSatellite.getValueAt(i, 1) != null) {
                    Integer copies = (Integer) (tSatellite.getValueAt(i, 1));
                    if (copies == null) copies = new Integer(0);
                    if (!(copies.equals(null) || copies.intValue() <= 0)) {
                        htSubLib.put(lib2, copies);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = false;
        for (int k = 0; k < dtmBudget.getRowCount(); k++) {
            if (((Boolean) dtmBudget.getValueAt(k, 0)).booleanValue()) {
                flag = true;
                break;
            }
        }
        if (chbSkip.isSelected()) {
        } else {
        }
        if (flag || chbSkip.isSelected() || radGratis.isSelected() || radExchange.isSelected()) {
            if ((!this.tfTitle.getText().trim().equals(""))) {
                if (htSubLib.size() > 0) {
                    String statuss = "";
                    if (chbSkip.isSelected()) statuss = "B"; else statuss = "P";
                    String gratiseStatus = "", gratiseText = "";
                    if (radGratis.isSelected()) {
                        gratiseStatus = "A";
                        if (radExchange.isSelected()) gratiseText = "In Exchange for: " + textGratise.getText().trim(); else if (radGratis.isSelected()) gratiseText = textGratise.getText().trim();
                    } else {
                        gratiseStatus = "B";
                    }
                    String xml = SMXMLGenerator.getInstance().saveSubscription2("10", NewGenMain.getAppletInstance().getLibraryID(), catalogueRecordId.trim(), ownerLibraryId.trim(), htSubLib, htBudgetLib, NewGenMain.getAppletInstance().getEntryID(), statuss, tfTitle.getText(), tfISSN.getText(), tfPublisher.getText(), chbSkip.isSelected(), "", gratiseStatus, gratiseText);
                    String resxml = newgen.presentation.component.ServletConnector.getInstance().sendRequest("SubscriptionServlet", xml);
                    try {
                        SAXBuilder saxb = new SAXBuilder();
                        Document retdoc = saxb.build(new java.io.StringReader(resxml));
                        Element rootelement = retdoc.getRootElement();
                        String status = rootelement.getChildText("Status");
                        if (status.equals("FAIL")) NewGenMain.getAppletInstance().showErrorMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SerailMastercreationupdationfailed")); else if (status.equals("SUCCESS")) {
                            String subscriptionIdRet = rootelement.getChildText("SubscriptionId");
                            String libraryIdRet = rootelement.getChildText("LibraryId");
                            int code = newgen.presentation.NewGenMain.getAppletInstance().showQuestionMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Task successfulDoyouwanttoview theSerialMaster?"));
                            if (code == javax.swing.JOptionPane.YES_OPTION) {
                                newgen.presentation.holdings.SerialMasterAdvDialog smd = new newgen.presentation.holdings.SerialMasterAdvDialog();
                                smd.setSubscription(subscriptionIdRet, libraryIdRet);
                                if (chbSkip.isSelected()) smd.setMode(SerialMasterDialog.ADD_TO_SUBSCRIPTION_LIST_MODE_SKIP_ORDER); else smd.setMode(SerialMasterDialog.ADD_TO_SUBSCRIPTION_LIST_MODE);
                                smd.show();
                            }
                            this.setDialogStatus("SUCCESS");
                            OnCancelClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("No of copies should be greater than zero");
            } else {
                newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TitleofserialismandatoryPleaseentertitle"));
                tfTitle.grabFocus();
            }
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SelectatleastoneBudgetId"));
        }
    }

    public void modifySubscription() {
        tSatellite.editCellAt(0, 0);
        if (tSatellite.getSelectedRow() != -1) {
            int rowint = tSatellite.getSelectedRow();
            tSatellite.clearSelection();
            tSatellite.setRowSelectionInterval(rowint, rowint);
        }
        try {
            for (int i = 0; i < dtmSatellite.getRowCount(); i++) {
                String lib2 = NewGenMain.getAppletInstance().getLibraryId(tSatellite.getValueAt(i, 0).toString());
                if (tSatellite.getValueAt(i, 1) != null) {
                    Integer copies = (Integer) (tSatellite.getValueAt(i, 1));
                    if (copies == null) copies = new Integer(0);
                    if (!(copies.equals(null) || copies.intValue() <= 0)) {
                        htSubLib.put(lib2, copies);
                    }
                }
            }
        } catch (Exception e) {
        }
        boolean flag = false;
        for (int k = 0; k < dtmBudget.getRowCount(); k++) {
            if (((Boolean) dtmBudget.getValueAt(k, 0)).booleanValue()) {
                flag = true;
                break;
            }
        }
        if (flag || chbSkip.isSelected() || radGratis.isSelected()) {
            if ((!this.tfTitle.getText().trim().equals(""))) {
                if (htSubLib.size() > 0) {
                    String status = "";
                    if (chbSkip.isSelected()) status = "B"; else status = "P";
                    String gratiseStatus = "", gratiseText = "";
                    if (radGratis.isSelected()) {
                        gratiseStatus = "A";
                        gratiseText = textGratise.getText().trim();
                    } else {
                        gratiseStatus = "B";
                    }
                    String xml = SMXMLGenerator.getInstance().modifySubscription2("12", NewGenMain.getAppletInstance().getLibraryID(), catalogueRecordId.trim(), ownerLibraryId.trim(), htSubLib, htBudgetLib, NewGenMain.getAppletInstance().getEntryID(), status, tfTitle.getText(), tfISSN.getText(), tfPublisher.getText(), subscriptionId, "", gratiseStatus, gratiseText);
                    String resxml = newgen.presentation.component.ServletConnector.getInstance().sendRequest("SubscriptionServlet", xml);
                    try {
                        SAXBuilder saxb = new SAXBuilder();
                        Document retdoc = saxb.build(new java.io.StringReader(resxml));
                        Element rootelement = retdoc.getRootElement();
                        status = rootelement.getChildText("Status");
                        if (status.equals("FAIL")) NewGenMain.getAppletInstance().showErrorMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Databaseexceptionraised")); else if (status.equals("SUCCESS")) {
                            int code = newgen.presentation.NewGenMain.getAppletInstance().showQuestionMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Task successfulDoyouwanttoview theSerialMaster?"));
                            if (code == javax.swing.JOptionPane.YES_OPTION) {
                                String subscriptionIdRet = rootelement.getChildText("SubscriptionId");
                                String libraryIdRet = rootelement.getChildText("LibraryId");
                                newgen.presentation.holdings.SerialMasterAdvDialog smd = new newgen.presentation.holdings.SerialMasterAdvDialog();
                                smd.setSubscription(subscriptionIdRet, libraryIdRet);
                                smd.show();
                            }
                            this.setDialogStatus("SUCCESS");
                            OnCancelClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("No of copies should be greater than zero");
            } else {
                newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TitleofserialismandatoryPleaseentertitle"));
            }
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SelectatleastoneBudgetHead"));
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void OnCancelClick() {
        java.util.Vector vAllBudgets = retrieveAllBudgets();
        if (vAllBudgets.size() > 0) {
            dtmBudget.setRowCount(0);
            for (int i = 0; i < vAllBudgets.size(); i += 8) {
                java.util.Vector vRow = new java.util.Vector();
                vRow.addElement(vAllBudgets.elementAt(i));
                vRow.addElement(vAllBudgets.elementAt(i + 1));
                vRow.addElement(vAllBudgets.elementAt(i + 2));
                vRow.addElement(vAllBudgets.elementAt(i + 3));
                vRow.addElement(vAllBudgets.elementAt(i + 4));
                vRow.addElement(vAllBudgets.elementAt(i + 5));
                vRow.addElement(vAllBudgets.elementAt(i + 6));
                vRow.addElement(vAllBudgets.elementAt(i + 7));
                this.dtmBudget.addRow(vRow);
            }
            setBudgetHeads();
        }
        String loginlibid = "";
        loginlibid = newgen.presentation.NewGenMain.getAppletInstance().getLibraryID();
        htSubLib = new Hashtable();
        htBudgetLib = new Hashtable();
        catalogueRecordId = "";
        ownerLibraryId = "";
        htsatlib = new ArrayList();
        htsatlib.clear();
        tfTitle.setText("");
        tfISSN.setText("");
        textGratise.setText("");
        tfPublisher.setText("");
        tfTitle.setEditable(true);
        tfISSN.setEditable(true);
        tfPublisher.setEditable(true);
        this.catalogueRecordId = "";
        this.ownerLibraryId = "";
        tfTitle.grabFocus();
    }

    public void setSubscriptionId(String subId) {
        this.subscriptionId = subId;
    }

    public void settitle(String title) {
        tfTitle.setText(title);
    }

    public void setPublisher(String publisher) {
        tfPublisher.setText(publisher);
    }

    public void setStartYear(String startYear) {
    }

    public void setISSN(String issn) {
        this.tfISSN.setText(issn);
    }

    public java.util.Vector getSubscribedBudgets(String library, String status) {
        String libraryId = NewGenMain.getAppletInstance().getLibraryId(library);
        String xmlreq = "";
        xmlreq = SMXMLGenerator.getInstance().getSubscribedBudgets("6", libraryId, subscriptionId, status, libraryId);
        String xmlres = "";
        java.util.Vector vec = new java.util.Vector();
        try {
            xmlres = newgen.presentation.component.ServletConnector.getInstance().sendRequest("SubscriptionServlet", xmlreq);
            System.out.println("xml response====" + xmlres);
            if (xmlres != null && !xmlres.trim().equals("")) {
                SAXBuilder saxb = new SAXBuilder();
                Document retdoc = saxb.build(new java.io.StringReader(xmlres));
                Element rootelement = retdoc.getRootElement();
                if (rootelement.getText() != null && rootelement.getText().trim().equals("NOBUDGET")) {
                } else {
                    java.util.List onelist = rootelement.getChildren("Record");
                    java.util.Vector vBudgetId = new java.util.Vector();
                    java.util.Vector vBudgetHead = new java.util.Vector();
                    java.util.Vector vBalance = new java.util.Vector();
                    java.util.Vector vAllocation = new java.util.Vector();
                    java.util.Vector vAmount = new java.util.Vector();
                    if (onelist.size() > 0) {
                        for (int i = 0; i < onelist.size(); i++) {
                            Element rec = (Element) onelist.get(i);
                            vec.addElement(new Boolean(false));
                            vec.addElement(rec.getChild("BudgetId").getText());
                            vec.addElement(rec.getChild("BudgetSourceId").getText());
                            vec.addElement(rec.getChild("BudgetHead").getText());
                            vec.addElement(rec.getChild("Balance").getText());
                            vec.addElement(rec.getChild("CarryStatus").getText());
                            vec.addElement(rec.getChild("FiscalYear").getText());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vec;
    }

    public java.util.Vector getSubscribedLibraries() {
        String libraryId = newgen.presentation.NewGenMain.getAppletInstance().getLibraryID();
        String xmlreq = SMXMLGenerator.getInstance().getSubscribedLibraries("5", libraryId, subscriptionId);
        String xmlres;
        java.util.Vector vec = new java.util.Vector();
        try {
            xmlres = newgen.presentation.component.ServletConnector.getInstance().sendRequest("SubscriptionServlet", xmlreq);
            if (!xmlres.equals("")) {
                SAXBuilder saxb = new SAXBuilder();
                Document retdoc = saxb.build(new java.io.StringReader(xmlres));
                Element rootelement = retdoc.getRootElement();
                java.util.List onelist = rootelement.getChildren("Record");
                java.util.Vector vLibrary = new java.util.Vector();
                java.util.Vector vCopies = new java.util.Vector();
                if (onelist.size() > 0) {
                    for (int i = 0; i < onelist.size(); i++) {
                        Element rec = (Element) onelist.get(i);
                        vec.add(rec.getChild("LibraryId").getText());
                        vec.add(rec.getChild("Copies").getText());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vec;
    }

    public java.util.Vector retrieveAllBudgets() {
        java.util.Vector r = new java.util.Vector();
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "14");
        org.jdom.Element library = new org.jdom.Element("LibraryId");
        libraryId = NewGenMain.getAppletInstance().getLibraryId(tSatellite.getValueAt(tSatellite.getSelectedRow(), 0).toString());
        library.setText(libraryId);
        root.addContent(library);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("SubscriptionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            SAXBuilder saxb = new SAXBuilder();
            try {
                java.util.List onelist = root1.getChildren();
                if (onelist.size() > 0) {
                    for (int i = 0; i < onelist.size(); i++) {
                        Element rec = (Element) onelist.get(i);
                        String budgetdetail = utility.getTestedString(rec.getChild("BudgetId"));
                        if (!budgetdetail.equals("")) {
                            r.addElement(new Boolean(false));
                            r.addElement(rec.getChild("BudgetId").getText());
                            r.addElement(rec.getChild("BudgetSourceId").getText());
                            r.addElement(rec.getChild("BudgetSourceName").getText());
                            r.addElement(rec.getChild("BudgetHead").getText());
                            r.addElement(rec.getChild("Balance").getText());
                            r.addElement(rec.getChild("CarryStatus").getText());
                            r.addElement(rec.getChild("FiscalYear").getText());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return r;
    }

    /** Getter for property dialogStatus.
     * @return Value of property dialogStatus.
     *
     */
    public java.lang.String getDialogStatus() {
        return dialogStatus;
    }

    /** Setter for property dialogStatus.
     * @param dialogStatus New value of property dialogStatus.
     *
     */
    public void setDialogStatus(java.lang.String dialogStatus) {
        this.dialogStatus = dialogStatus;
    }

    public void setGratis(boolean gratis) {
        System.out.println("gratis..." + gratis);
        if (gratis) {
            radGratis.doClick();
        }
    }

    /** Getter for property gratisText.
     * @return Value of property gratisText.
     *
     */
    public java.lang.String getGratisText() {
        return gratisText;
    }

    /** Setter for property gratisText.
     * @param gratisText New value of property gratisText.
     *
     */
    public void setGratisText(java.lang.String gratisText) {
        textGratise.setText(gratisText);
    }

    private javax.swing.JToggleButton bNew;

    private javax.swing.JButton bSearch;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JCheckBox chbSkip;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JLabel lISSN;

    private javax.swing.JLabel lPublisher;

    private javax.swing.JLabel lTitle;

    private javax.swing.JRadioButton radExchange;

    private javax.swing.JRadioButton radGratis;

    private javax.swing.JRadioButton radSubscription;

    public javax.swing.JTable tBudget;

    private javax.swing.JTable tSatellite;

    private javax.swing.JTextArea textGratise;

    private newgen.presentation.UnicodeTextField tfISSN;

    private newgen.presentation.UnicodeTextField tfPublisher;

    private newgen.presentation.UnicodeTextField tfTitle;

    private javax.swing.table.DefaultTableModel dtmSatellite;

    public javax.swing.table.DefaultTableModel dtmBudget;

    private newgen.presentation.cataloguing.SearchCatalogueDialog searchCatalogueDialog;

    private newgen.presentation.cataloguing.BasicCatalogueRecordDialog basicCatalogueRecordDialog;

    private int searchmode;

    private ArrayList htsatlib = new ArrayList();

    private String catalogueRecordId = "";

    private String ownerLibraryId = "";

    private Hashtable htSubLib = new Hashtable();

    private Hashtable htBudgetLib = new Hashtable();

    String libraryId = "";

    String copies;

    private String mode = "NEW";

    private String subscriptionId;

    private String dialogStatus = "";

    private String gratisText = "";
}
