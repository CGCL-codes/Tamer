package newgen.presentation.cataloguing.advanced;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;
import newgenlib.marccomponent.marcmodel.*;

/**
 *
 * @author  administrator
 */
public class MARCEditorComponentPanel extends javax.swing.JPanel implements javax.swing.event.TableModelListener {

    /** Creates new form MARCEditorComponentPanel */
    newgen.presentation.component.Utility utility = null;

    public MARCEditorComponentPanel() {
        initComponents();
        int dismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
        dismissDelay = Integer.MAX_VALUE;
        ToolTipManager.sharedInstance().setDismissDelay(dismissDelay);
        jPanel2.add(jPanel7, java.awt.BorderLayout.LINE_END);
        utility = newgen.presentation.component.Utility.getInstance();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        Object obj[] = { "NameId", "LibraryId", "INV_TAG", "INV_SF", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Field"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ind1"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ind2"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SubField"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Data") };
        this.dtmMarc = new javax.swing.table.DefaultTableModel(obj, 0) {

            public boolean isCellEditable(int r, int c) {
                String tg = dtmMarc.getValueAt(r, 2).toString().trim();
                if (c == 8 && (tg == null || tg.trim().equals(""))) return true; else return false;
            }

            public Class getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }
        };
        tabMarc.setModel(dtmMarc);
        tabMarc.getColumnModel().getColumn(0).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(0).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(1).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(1).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(1).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(2).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(2).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(2).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(3).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(3).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(3).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabMarc.getColumnModel().getColumn(5).setPreferredWidth(30);
        tabMarc.getColumnModel().getColumn(6).setPreferredWidth(30);
        tabMarc.getColumnModel().getColumn(7).setPreferredWidth(200);
        javax.swing.table.DefaultTableCellRenderer dftcellred = new javax.swing.table.DefaultTableCellRenderer();
        dftcellred.setForeground(java.awt.Color.RED);
        tabMarc.setFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.BOLD, 11));
        tabMarc.getColumnModel().getColumn(4).setCellRenderer(dftcellred);
        javax.swing.table.DefaultTableCellRenderer dftcellblue = new javax.swing.table.DefaultTableCellRenderer();
        dftcellblue.setForeground(java.awt.Color.BLUE);
        tabMarc.getColumnModel().getColumn(7).setCellRenderer(dftcellblue);
        tabMarc.getColumnModel().getColumn(8).setPreferredWidth(400);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    public void reloadLocales() {
        jPanel6.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FieldsAndSubFields")));
        baddField.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AddField"));
        baddSubField.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AddSubField"));
        bnDelete.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        brepeat.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Repeat"));
        bnIndicators.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Indiacators"));
        tagRadio.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Tags"));
        nameRadio.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Names"));
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        Object obj[] = { "NameId", "LibraryId", "INV_TAG", "INV_SF", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Field"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ind1"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ind2"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SubField"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Data") };
        dtmMarc.setColumnIdentifiers(obj);
        tabMarc.getColumnModel().getColumn(0).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(0).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(1).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(1).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(1).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(2).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(2).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(2).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(3).setMinWidth(0);
        tabMarc.getColumnModel().getColumn(3).setMaxWidth(0);
        tabMarc.getColumnModel().getColumn(3).setPreferredWidth(0);
        tabMarc.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabMarc.getColumnModel().getColumn(5).setPreferredWidth(30);
        tabMarc.getColumnModel().getColumn(6).setPreferredWidth(30);
        tabMarc.getColumnModel().getColumn(7).setPreferredWidth(200);
        tabMarc.getColumnModel().getColumn(8).setPreferredWidth(400);
        javax.swing.table.DefaultTableCellRenderer dftcellred = new javax.swing.table.DefaultTableCellRenderer();
        dftcellred.setForeground(java.awt.Color.RED);
        tabMarc.setFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.BOLD, 11));
        tabMarc.getColumnModel().getColumn(4).setCellRenderer(dftcellred);
        javax.swing.table.DefaultTableCellRenderer dftcellblue = new javax.swing.table.DefaultTableCellRenderer();
        dftcellblue.setForeground(java.awt.Color.BLUE);
        tabMarc.getColumnModel().getColumn(7).setCellRenderer(dftcellblue);
    }

    private void initComponents() {
        group1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabMarc = new JTable() {

            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText((String) getValueAt(rowIndex, vColIndex));
                }
                return c;
            }
        };
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        baddField = new javax.swing.JButton();
        baddSubField = new javax.swing.JButton();
        bnDelete = new javax.swing.JButton();
        brepeat = new javax.swing.JButton();
        bnIndicators = new javax.swing.JButton();
        bnAGR = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        tagRadio = new javax.swing.JRadioButton();
        nameRadio = new javax.swing.JRadioButton();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setFont(new java.awt.Font("Tahoma", 0, 10));
        tabMarc.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        tabMarc.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabMarc.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tabMarcPropertyChange(evt);
            }
        });
        tabMarc.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMarcMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabMarc);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel1);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FieldsAndSubFields")));
        baddField.setMnemonic('f');
        baddField.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AddField"));
        baddField.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AddField"));
        baddField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baddFieldActionPerformed(evt);
            }
        });
        jPanel6.add(baddField);
        baddSubField.setMnemonic('u');
        baddSubField.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AddSubField"));
        baddSubField.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AddSubField"));
        baddSubField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baddSubFieldActionPerformed(evt);
            }
        });
        jPanel6.add(baddSubField);
        bnDelete.setMnemonic('d');
        bnDelete.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        bnDelete.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        bnDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnDeleteActionPerformed(evt);
            }
        });
        jPanel6.add(bnDelete);
        brepeat.setMnemonic('t');
        brepeat.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Repeat"));
        brepeat.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Repeat"));
        brepeat.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brepeatActionPerformed(evt);
            }
        });
        jPanel6.add(brepeat);
        bnIndicators.setMnemonic('i');
        bnIndicators.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Indiacators"));
        bnIndicators.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Indiacators"));
        bnIndicators.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnIndicatorsActionPerformed(evt);
            }
        });
        jPanel6.add(bnIndicators);
        bnAGR.setMnemonic('g');
        bnAGR.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AGR"));
        bnAGR.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AGR"));
        bnAGR.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnAGRActionPerformed(evt);
            }
        });
        jPanel6.add(bnAGR);
        jPanel2.add(jPanel6, java.awt.BorderLayout.CENTER);
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));
        group1.add(tagRadio);
        tagRadio.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Tags"));
        tagRadio.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagRadioActionPerformed(evt);
            }
        });
        jPanel7.add(tagRadio);
        group1.add(nameRadio);
        nameRadio.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Names"));
        nameRadio.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameRadioActionPerformed(evt);
            }
        });
        jPanel7.add(nameRadio);
        jPanel2.add(jPanel7, java.awt.BorderLayout.EAST);
        add(jPanel2);
    }

    public String getAGRLinkNumber(String tag, int row) {
        String linkval = "";
        if (!this.isTagRepeatable(tag)) return "01";
        try {
            boolean found = false;
            int occurence = 0;
            for (int k = 0; k < dtmMarc.getRowCount(); k++) {
                String tagField = "";
                tagField = utility.getTestedString(dtmMarc.getValueAt(k, 2));
                if (tagField.trim().equals(tag.trim()) && k != row) {
                    found = false;
                    for (int j = k + 1; j < dtmMarc.getRowCount(); j++) {
                        String code = "", data = "";
                        code = utility.getTestedString(dtmMarc.getValueAt(j, 3));
                        data = utility.getTestedString(dtmMarc.getValueAt(j, 8));
                        if (code.trim().equals("6") && !data.trim().equals("")) {
                            int occr = 0;
                            java.util.StringTokenizer stk = new java.util.StringTokenizer(data, "-");
                            if (stk.hasMoreTokens()) {
                                String tagnn = stk.nextToken();
                            }
                            if (stk.hasMoreTokens()) {
                                String tagnn = stk.nextToken();
                                occr = Integer.parseInt(tagnn);
                                if (occurence < occr) {
                                    occurence = occr;
                                }
                            }
                        } else if (code.trim().equals("")) {
                            break;
                        }
                    }
                } else if (tagField.trim().equals(tag.trim()) && k == row) {
                    for (int p = row + 1; p < dtmMarc.getRowCount(); p++) {
                        String code = "", data = "";
                        code = utility.getTestedString(dtmMarc.getValueAt(p, 3));
                        data = utility.getTestedString(dtmMarc.getValueAt(p, 8));
                        if (code.trim().equals("6") && !data.trim().equals("")) {
                            int occr = 0;
                            java.util.StringTokenizer stk = new java.util.StringTokenizer(data, "-");
                            if (stk.hasMoreTokens()) {
                                String tagnn = stk.nextToken();
                            }
                            if (stk.hasMoreTokens()) {
                                String tagnn = stk.nextToken();
                                occurence = Integer.parseInt(tagnn);
                                found = true;
                                break;
                            }
                        } else if (code.trim().equals("")) {
                            break;
                        }
                    }
                    if (found) break;
                }
            }
            System.out.println("found  " + found);
            if (found) {
                String vvv = String.valueOf(occurence);
                if (vvv.length() == 1) {
                    vvv = "0" + vvv;
                }
                linkval = vvv;
            } else {
                occurence++;
                String vvv = String.valueOf(occurence);
                if (vvv.length() == 1) {
                    vvv = "0" + vvv;
                }
                linkval = vvv;
            }
            System.out.println("linkval " + linkval);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linkval;
    }

    private void bnAGRActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int k = -1;
            k = tabMarc.getSelectedRow();
            if (k != -1) {
                String tag = "";
                tag = (String) dtmMarc.getValueAt(k, 2);
                if (!tag.trim().equals("")) {
                    String ind1 = "", ind2 = "";
                    ind1 = utility.getTestedString(dtmMarc.getValueAt(k, 5));
                    ind2 = utility.getTestedString(dtmMarc.getValueAt(k, 6));
                    Object obj[] = new Object[9];
                    obj[0] = "";
                    obj[1] = "";
                    obj[2] = "880";
                    obj[3] = "";
                    if (this.nameRadio.isSelected()) {
                        obj[4] = this.getTagName("880");
                    } else {
                        obj[4] = "880";
                    }
                    obj[5] = ind1;
                    obj[6] = ind2;
                    obj[7] = "";
                    obj[8] = "";
                    dtmMarc.addRow(obj);
                    Object obj2[] = new Object[9];
                    obj2[0] = obj2[1] = obj2[2] = obj2[3] = obj2[4] = obj2[5] = obj2[6] = obj2[7] = obj2[8] = "";
                    obj2[3] = "a";
                    if (this.nameRadio.isSelected()) {
                        obj2[7] = this.getSubFieldName("880", "a");
                    } else {
                        obj2[7] = "a";
                    }
                    obj2[8] = "";
                    dtmMarc.addRow(obj2);
                    obj2[0] = obj2[1] = obj2[2] = obj2[3] = obj2[4] = obj2[5] = obj2[6] = obj2[7] = obj2[8] = "";
                    obj2[3] = "6";
                    if (this.nameRadio.isSelected()) {
                        obj2[7] = this.getSubFieldName(tag, "6");
                    } else {
                        obj2[7] = "6";
                    }
                    obj2[8] = tag + "-" + getAGRLinkNumber(tag, k);
                    dtmMarc.addRow(obj2);
                    boolean notfound = false;
                    System.out.println("in linking 43 " + getAGRLinkNumber(tag, k));
                    for (int i = k + 1; i < dtmMarc.getRowCount(); i++) {
                        String code = "";
                        code = utility.getTestedString(dtmMarc.getValueAt(i, 3));
                        if (code.trim().equals("6")) {
                            dtmMarc.setValueAt("880-" + getAGRLinkNumber(tag, k), i, 8);
                            notfound = true;
                            break;
                        } else if (code.trim().equals("")) {
                            notfound = false;
                            break;
                        }
                    }
                    if (!notfound) {
                        obj2[0] = obj2[1] = obj2[2] = obj2[3] = obj2[4] = obj2[5] = obj2[6] = obj2[7] = obj2[8] = "";
                        obj2[3] = "6";
                        if (this.nameRadio.isSelected()) {
                            obj2[7] = this.getSubFieldName(tag, "6");
                        } else {
                            obj2[7] = "6";
                        }
                        obj2[8] = "880-" + getAGRLinkNumber(tag, k);
                        dtmMarc.insertRow(k + 1, obj2);
                    }
                } else {
                    new javax.swing.JOptionPane().showMessageDialog(this, "Please Select Field/Tag Row", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Select the Field Row", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
        }
    }

    private void tabMarcMouseClicked(java.awt.event.MouseEvent evt) {
        int k = -1;
        k = tabMarc.getSelectedRow();
        if (k != -1) {
            String tagfield = "";
            tagfield = (String) dtmMarc.getValueAt(k, 2);
            this.setTagSelected(tagfield.trim());
        }
    }

    private void tabMarcPropertyChange(java.beans.PropertyChangeEvent evt) {
        int k = 0;
        k = tabMarc.getSelectedRow();
    }

    private void bnIndicatorsActionPerformed(java.awt.event.ActionEvent evt) {
        int k = -1;
        k = this.tabMarc.getSelectedRow();
        if (k >= 0) {
            String tag = "";
            tag = (String) dtmMarc.getValueAt(k, 2);
            if (!tag.trim().equals("")) {
                newgen.presentation.cataloguing.advanced.MarcEditorComponentIndicatorsDialog marcindicator = new newgen.presentation.cataloguing.advanced.MarcEditorComponentIndicatorsDialog();
                marcindicator.setMARC_MODE(MARC_MODE);
                marcindicator.getIndicators(tag);
                marcindicator.show();
                Object obj[] = (Object[]) marcindicator.getIndicatorObj();
                if (obj != null && obj.length == 2) {
                    dtmMarc.setValueAt(obj[0].toString(), k, 5);
                    dtmMarc.setValueAt(obj[1].toString(), k, 6);
                }
            } else {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Select Field");
            }
        } else {
            new javax.swing.JOptionPane().showMessageDialog(this, "Please Select a Row");
        }
    }

    private void brepeatActionPerformed(java.awt.event.ActionEvent evt) {
        int k = -1;
        k = tabMarc.getSelectedRow();
        if (k != -1) {
            String tag = "", code = "";
            tag = (String) dtmMarc.getValueAt(k, 2);
            code = (String) dtmMarc.getValueAt(k, 3);
            if (!tag.trim().equals("")) {
                boolean rep = this.isTagRepeatable(tag);
                if (rep) {
                    java.util.Vector tagRep = new java.util.Vector();
                    Object obj1[] = new Object[9];
                    obj1[0] = obj1[1] = obj1[2] = obj1[3] = obj1[4] = obj1[5] = obj1[6] = obj1[7] = obj1[8] = "";
                    obj1[2] = tag;
                    obj1[4] = (String) dtmMarc.getValueAt(k, 4);
                    tagRep.add(obj1);
                    k++;
                    for (int p = k; p < dtmMarc.getRowCount(); p++) {
                        String tag1 = "";
                        tag1 = (String) dtmMarc.getValueAt(p, 2);
                        Object obj2[] = new Object[9];
                        obj2[0] = obj2[1] = obj2[2] = obj2[3] = obj2[4] = obj2[5] = obj2[6] = obj2[7] = obj2[8] = "";
                        if (!tag1.trim().equals("")) break;
                        String[] str1 = new String[2];
                        str1[0] = (String) dtmMarc.getValueAt(p, 3);
                        str1[1] = (String) dtmMarc.getValueAt(p, 7);
                        obj2[3] = str1[0];
                        obj2[7] = str1[1];
                        tagRep.add(obj2);
                        k++;
                    }
                    for (int i = tagRep.size() - 1; i >= 0; i--) dtmMarc.insertRow(k, (Object[]) tagRep.get(i));
                } else new javax.swing.JOptionPane().showMessageDialog(this, "This Field is not Repeatable", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else if (!code.trim().equals("")) {
                String tagCode = "";
                for (int q = k - 1; q >= 0; q--) {
                    tagCode = (String) dtmMarc.getValueAt(q, 2);
                    if (!tagCode.trim().equals("")) break;
                }
                if (!tagCode.trim().equals("")) {
                    boolean repCode = this.isSubFieldRepeatable(tagCode, code);
                    if (repCode) {
                        Object obj2[] = new Object[9];
                        obj2[0] = obj2[1] = obj2[2] = obj2[3] = obj2[4] = obj2[5] = obj2[6] = obj2[7] = obj2[8] = "";
                        obj2[3] = (String) dtmMarc.getValueAt(k, 3);
                        obj2[7] = (String) dtmMarc.getValueAt(k, 7);
                        dtmMarc.insertRow(k + 1, obj2);
                    } else new javax.swing.JOptionPane().showMessageDialog(this, "This SubField is not repeatable", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            }
        } else new javax.swing.JOptionPane().showMessageDialog(this, "Please Select Field/Subfield for repeating");
    }

    public void deleteion880(String tag, int k) {
        if (tag.trim().equals("880")) {
            String data12 = "";
            for (int i = k + 1; i < dtmMarc.getRowCount(); i++) {
                String code12 = utility.getTestedString(dtmMarc.getValueAt(i, 3));
                if (code12.trim().equals("6")) {
                    data12 = utility.getTestedString(dtmMarc.getValueAt(i, 8));
                    break;
                } else if (code12.trim().equals("")) {
                    break;
                }
            }
            java.util.StringTokenizer stk = new java.util.StringTokenizer(data12, "-");
            if (stk.hasMoreTokens()) {
                stk.nextToken();
            }
            if (stk.hasMoreTokens()) {
                data12 = "880-" + stk.nextToken();
            }
            for (int i = 0; i < dtmMarc.getRowCount(); i++) {
                String data = utility.getTestedString(dtmMarc.getValueAt(i, 8));
                if (data12.trim().equals(data.trim())) {
                    dtmMarc.setValueAt("", i, 8);
                    break;
                }
            }
        } else {
            String data12 = "";
            for (int i = k + 1; i < dtmMarc.getRowCount(); i++) {
                String code12 = utility.getTestedString(dtmMarc.getValueAt(i, 3));
                if (code12.trim().equals("6")) {
                    data12 = utility.getTestedString(dtmMarc.getValueAt(i, 8));
                    break;
                } else if (code12.trim().equals("")) {
                    break;
                }
            }
            java.util.StringTokenizer stk = new java.util.StringTokenizer(data12, "-");
            if (stk.hasMoreTokens()) {
                stk.nextToken();
            }
            if (stk.hasMoreTokens()) {
                data12 = tag + "-" + stk.nextToken();
            }
            int stindex880 = -1, endindex880 = dtmMarc.getRowCount();
            for (int i = 0; i < dtmMarc.getRowCount(); i++) {
                String data = utility.getTestedString(dtmMarc.getValueAt(i, 8));
                if (data12.trim().equals(data.trim())) {
                    for (int p = i; p >= 0; p--) {
                        String code34343 = utility.getTestedString(dtmMarc.getValueAt(p, 3));
                        if (code34343.trim().equals("")) {
                            stindex880 = p;
                            break;
                        }
                    }
                    for (int p = i; p < dtmMarc.getRowCount(); p++) {
                        String code34343 = utility.getTestedString(dtmMarc.getValueAt(p, 3));
                        if (code34343.trim().equals("")) {
                            endindex880 = p;
                            break;
                        }
                    }
                    break;
                }
            }
            System.out.println("stindex880 " + stindex880 + "endindex880 " + endindex880);
            if (stindex880 > 0) {
                for (int kl = endindex880 - 1; kl >= 0; kl--) {
                    dtmMarc.removeRow(kl);
                }
            }
        }
    }

    private void bnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int k = -1;
        k = tabMarc.getSelectedRow();
        if (k != -1) {
            String tag = "", code = "";
            tag = (String) dtmMarc.getValueAt(k, 2);
            code = (String) dtmMarc.getValueAt(k, 3);
            int yesno = new javax.swing.JOptionPane().showConfirmDialog(this, "Do You want to delete", "Delete", javax.swing.JOptionPane.YES_NO_OPTION);
            if (yesno == javax.swing.JOptionPane.YES_OPTION) {
                if (!tag.trim().equals("")) {
                    deleteion880(tag, k);
                    int p = k, q = p;
                    for (q = p + 1; q < dtmMarc.getRowCount(); q++) {
                        String tg = (String) dtmMarc.getValueAt(q, 2);
                        if (!tg.trim().equals("")) {
                            break;
                        }
                    }
                    for (int i = q - 1; i >= p; i--) {
                        dtmMarc.removeRow(i);
                    }
                } else if (!code.trim().equals("")) {
                    String cd = "";
                    cd = (String) dtmMarc.getValueAt(k - 1, 2);
                    int sz = dtmMarc.getRowCount() - 1;
                    if (!cd.trim().equals("") && sz == k) {
                        dtmMarc.removeRow(k);
                        dtmMarc.removeRow(k - 1);
                    } else {
                        dtmMarc.removeRow(k);
                    }
                }
            }
        } else {
            new javax.swing.JOptionPane().showMessageDialog(this, "Please Select a Row", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void baddSubFieldActionPerformed(java.awt.event.ActionEvent evt) {
        int k = -1;
        k = tabMarc.getSelectedRow();
        if (k != -1) {
            String tag = "";
            tag = (String) dtmMarc.getValueAt(k, 2);
            if (!tag.trim().equals("")) {
                java.util.Vector vec = new java.util.Vector(1, 1);
                int p = 1;
                for (p = k + 1; p < dtmMarc.getRowCount(); p++) {
                    String code = "";
                    code = (String) dtmMarc.getValueAt(p, 3);
                    if (!code.trim().equals("")) {
                        vec.addElement(code);
                    } else {
                        break;
                    }
                }
                newgen.presentation.cataloguing.advanced.MarcEditorComponentSubFieldDialog marcSubfield = new newgen.presentation.cataloguing.advanced.MarcEditorComponentSubFieldDialog();
                marcSubfield.setMARC_MODE(MARC_MODE);
                marcSubfield.corporateSubFields(tag, vec);
                marcSubfield.show();
                java.util.Vector vecsubRes = marcSubfield.getVecSub();
                if (vecsubRes != null && vecsubRes.size() > 0) {
                    for (int q = 0; q < vecsubRes.size(); q++) {
                        Object[] objsb = new Object[9];
                        objsb[0] = objsb[1] = objsb[2] = objsb[3] = objsb[4] = objsb[5] = objsb[6] = objsb[7] = objsb[8] = "";
                        String str[] = (String[]) vecsubRes.get(q);
                        if (tagRadio.isSelected()) {
                            objsb[3] = objsb[7] = str[0];
                        } else {
                            objsb[3] = str[0];
                            objsb[7] = str[1];
                        }
                        dtmMarc.insertRow(p, objsb);
                        p++;
                    }
                }
            } else {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Select Field/Tag Row", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } else {
            new javax.swing.JOptionPane().showMessageDialog(this, "Please Select the Field Row", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void baddFieldActionPerformed(java.awt.event.ActionEvent evt) {
        newgen.presentation.cataloguing.advanced.MarcEditorComponentFieldDialog marcField = new newgen.presentation.cataloguing.advanced.MarcEditorComponentFieldDialog();
        java.util.Vector vecfield = new java.util.Vector(1, 1);
        for (int j = 0; j < dtmMarc.getRowCount(); j++) {
            String tg = "";
            tg = (String) dtmMarc.getValueAt(j, 2);
            if (!tg.trim().equals("")) {
                vecfield.addElement(tg);
            }
        }
        marcField.setVecField(vecfield);
        marcField.setMARC_MODE(MARC_MODE);
        marcField.show();
        java.util.Vector vec = marcField.getVecMarc();
        if (vec != null && vec.size() > 0) {
            Object obj[] = new Object[9];
            String taginfo[] = (String[]) vec.get(0);
            String tag = taginfo[0];
            java.util.Vector ind = (java.util.Vector) vec.get(1);
            java.util.Vector subVec = (java.util.Vector) vec.get(2);
            if (!tag.trim().equals("")) {
                obj[2] = obj[4] = tag;
                obj[0] = obj[1] = obj[3] = obj[5] = obj[6] = obj[7] = obj[8] = "";
                obj[5] = ind.get(0).toString();
                obj[6] = ind.get(1).toString();
                if (tagRadio.isSelected()) {
                    obj[2] = obj[4] = tag;
                } else {
                    obj[2] = tag;
                    obj[4] = taginfo[1];
                }
                dtmMarc.addRow(obj);
                if (subVec != null) {
                    for (int k = 0; k < subVec.size(); k++) {
                        Object[] objsb = new Object[9];
                        objsb[0] = objsb[1] = objsb[2] = objsb[3] = objsb[4] = objsb[5] = objsb[6] = objsb[7] = objsb[8] = "";
                        String str[] = (String[]) subVec.get(k);
                        if (tagRadio.isSelected()) {
                            objsb[3] = objsb[7] = str[0];
                        } else {
                            objsb[3] = str[0];
                            objsb[7] = str[1];
                        }
                        dtmMarc.addRow(objsb);
                    }
                }
            }
        }
    }

    private void nameRadioActionPerformed(java.awt.event.ActionEvent evt) {
        resizeColumns(false);
        String tag1 = "";
        for (int i = 0; i < dtmMarc.getRowCount(); i++) {
            String tag = "", code = "";
            tag = (String) dtmMarc.getValueAt(i, 2);
            code = (String) dtmMarc.getValueAt(i, 3);
            if (!tag.trim().equals("")) {
                tag1 = tag;
                String namtag = this.getTagName(tag);
                if (namtag != null && !namtag.trim().equals("")) {
                    dtmMarc.setValueAt(namtag, i, 4);
                } else {
                    dtmMarc.setValueAt(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("UndefinedField"), i, 4);
                }
            } else if (!code.trim().equals("")) {
                String sfName = this.getSubFieldName(tag1, code);
                if (sfName != null && !sfName.trim().equals("")) {
                    dtmMarc.setValueAt(sfName, i, 7);
                } else {
                    dtmMarc.setValueAt(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("UndefinedSubfield"), i, 7);
                }
            }
        }
    }

    private void resizeColumns(boolean isTags) {
        if (isTags) {
            tabMarc.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(1).setMinWidth(0);
            tabMarc.getColumnModel().getColumn(1).setMaxWidth(0);
            tabMarc.getColumnModel().getColumn(1).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(2).setMinWidth(0);
            tabMarc.getColumnModel().getColumn(2).setMaxWidth(0);
            tabMarc.getColumnModel().getColumn(2).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(3).setMinWidth(0);
            tabMarc.getColumnModel().getColumn(3).setMaxWidth(0);
            tabMarc.getColumnModel().getColumn(3).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(4).setPreferredWidth(80);
            tabMarc.getColumnModel().getColumn(5).setPreferredWidth(30);
            tabMarc.getColumnModel().getColumn(6).setPreferredWidth(30);
            tabMarc.getColumnModel().getColumn(7).setPreferredWidth(100);
            tabMarc.getColumnModel().getColumn(8).setPreferredWidth(500);
        } else {
            tabMarc.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(1).setMinWidth(0);
            tabMarc.getColumnModel().getColumn(1).setMaxWidth(0);
            tabMarc.getColumnModel().getColumn(1).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(2).setMinWidth(0);
            tabMarc.getColumnModel().getColumn(2).setMaxWidth(0);
            tabMarc.getColumnModel().getColumn(2).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(3).setMinWidth(0);
            tabMarc.getColumnModel().getColumn(3).setMaxWidth(0);
            tabMarc.getColumnModel().getColumn(3).setPreferredWidth(0);
            tabMarc.getColumnModel().getColumn(4).setPreferredWidth(200);
            tabMarc.getColumnModel().getColumn(5).setPreferredWidth(30);
            tabMarc.getColumnModel().getColumn(6).setPreferredWidth(30);
            tabMarc.getColumnModel().getColumn(7).setPreferredWidth(200);
            tabMarc.getColumnModel().getColumn(8).setPreferredWidth(400);
        }
    }

    private void tagRadioActionPerformed(java.awt.event.ActionEvent evt) {
        resizeColumns(true);
        for (int i = 0; i < dtmMarc.getRowCount(); i++) {
            String tag = "", code = "";
            tag = (String) dtmMarc.getValueAt(i, 2);
            code = (String) dtmMarc.getValueAt(i, 3);
            if (!tag.trim().equals("")) {
                dtmMarc.setValueAt(tag, i, 4);
            } else if (!code.trim().equals("")) {
                dtmMarc.setValueAt(code, i, 7);
            }
        }
    }

    /** Setter for property mode.
     * @param mode New value of property mode.
     *
     */
    public void setDataToMarcEditor(String xmlStr) {
        try {
            System.out.println("setDataToMarcEditor " + xmlStr);
            this.cancel();
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = sax.build(new java.io.StringReader(xmlStr.trim()));
            org.jdom.Element root = doc.getRootElement();
            java.util.List datafieldList = root.getChildren();
            for (int i = 0; i < datafieldList.size(); i++) {
                org.jdom.Element ele = (org.jdom.Element) datafieldList.get(i);
                java.util.List lst = ele.getChildren();
                if (lst.size() > 0) {
                    Object obj[] = { "", "", "", "", "", "", "", "", "" };
                    String tag = "", nameid = "", libId = "";
                    tag = ele.getAttributeValue("tag");
                    nameid = ele.getAttributeValue("nameid");
                    libId = ele.getAttributeValue("libid");
                    obj[0] = nameid;
                    obj[1] = libId;
                    if (tagRadio.isSelected()) {
                        obj[2] = obj[4] = tag;
                    } else {
                        obj[2] = tag;
                        obj[4] = this.getTagName(tag);
                    }
                    if (ele.getAttributeValue("ind1") != null) {
                        obj[5] = ele.getAttributeValue("ind1");
                    }
                    if (ele.getAttributeValue("ind2") != null) {
                        obj[6] = ele.getAttributeValue("ind2");
                    }
                    dtmMarc.addRow(obj);
                    for (int k = 0; k < lst.size(); k++) {
                        org.jdom.Element elesf = (org.jdom.Element) lst.get(k);
                        String code = "", data = "";
                        code = elesf.getAttributeValue("code");
                        data = elesf.getText();
                        if (!code.trim().equals("")) {
                            Object obj1[] = { "", "", "", "", "", "", "", "", "" };
                            if (tagRadio.isSelected()) {
                                obj1[3] = obj1[7] = code.trim();
                                obj1[8] = data.trim();
                            } else {
                                obj1[3] = code.trim();
                                obj1[7] = this.getSubFieldName(tag, code);
                                obj1[8] = data.trim();
                            }
                            dtmMarc.addRow(obj1);
                        }
                    }
                }
            }
            nameRadio.doClick();
            tabMarc.setRowSelectionInterval(0, 0);
            tabMarc.grabFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCellEditableReg(javax.swing.JTable tablDefault) {
        for (int i = 0; i < tablDefault.getRowCount(); i++) {
            tablDefault.editCellAt(i, 8);
        }
    }

    public String getMarcXML() {
        String str = "";
        try {
            setCellEditableReg(tabMarc);
            org.jdom.Element root = new org.jdom.Element("record");
            String tag2 = "", nameId = "", libId = "";
            org.jdom.Element datafield = null;
            for (int i = 0; i < dtmMarc.getRowCount(); i++) {
                String tag = "", code = "";
                tag = (String) dtmMarc.getValueAt(i, 2);
                code = (String) dtmMarc.getValueAt(i, 3);
                if (!tag.trim().equals("")) {
                    tag2 = tag;
                    nameId = (String) dtmMarc.getValueAt(i, 0);
                    libId = (String) dtmMarc.getValueAt(i, 1);
                    datafield = new org.jdom.Element("datafield");
                    datafield.setAttribute("tag", tag2);
                    datafield.setAttribute("nameid", nameId);
                    datafield.setAttribute("libid", libId);
                    datafield.setAttribute("ind1", dtmMarc.getValueAt(i, 5).toString().trim());
                    datafield.setAttribute("ind2", dtmMarc.getValueAt(i, 6).toString().trim());
                    root.addContent(datafield);
                } else if (!code.trim().equals("")) {
                    String data = "";
                    data = (String) dtmMarc.getValueAt(i, 8);
                    if (!data.trim().equals("")) {
                        org.jdom.Element subfield = new org.jdom.Element("subfield");
                        subfield.setAttribute("code", code);
                        subfield.setText(data.trim());
                        datafield.addContent(subfield);
                    }
                }
            }
            org.jdom.Document doc = new org.jdom.Document(root);
            org.jdom.output.XMLOutputter xml = new org.jdom.output.XMLOutputter();
            xml.setIndent(true);
            xml.setNewlines(true);
            xml.outputString(" ");
            str = xml.outputString(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public boolean isTagRepeatable(String str) {
        boolean rep = false;
        try {
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = null;
            try {
                doc = newgen.presentation.cataloguing.advanced.MARCXMLDictionary.getInstance().getDictionaryDocument(MARC_MODE);
            } catch (Exception e) {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Check To see XML DictionryFiles Existed or Not", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            if (doc != null) {
                org.jdom.Element root = doc.getRootElement();
                org.jdom.Element corporate = root.getChild("T" + str.trim());
                if (corporate != null) {
                    String tagRep = "";
                    tagRep = corporate.getChildText("Repeat");
                    if (tagRep.trim().equals("R")) rep = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rep;
    }

    public boolean isSubFieldRepeatable(String tag, String code) {
        boolean rep = false;
        try {
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = null;
            try {
                doc = newgen.presentation.cataloguing.advanced.MARCXMLDictionary.getInstance().getDictionaryDocument(MARC_MODE);
            } catch (Exception e) {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Check To see XML DictionryFiles Existed or Not", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            if (doc != null) {
                org.jdom.Element root = doc.getRootElement();
                org.jdom.Element corporate = root.getChild("T" + tag.trim());
                if (corporate != null) {
                    org.jdom.Element sub = corporate.getChild("SubFields");
                    org.jdom.Element sbfield = sub.getChild("SF" + code.trim());
                    if (sbfield != null) {
                        String repCode = "";
                        repCode = sbfield.getChildText("Repeat");
                        if (repCode.trim().equals("R")) rep = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rep;
    }

    public String getTagName(String tag) {
        String str = "";
        try {
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = null;
            try {
                doc = newgen.presentation.cataloguing.advanced.MARCXMLDictionary.getInstance().getDictionaryDocument(MARC_MODE);
            } catch (Exception e) {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Check To see XML DictionryFiles Existed or Not", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return null;
            }
            if (doc != null) {
                org.jdom.Element root = doc.getRootElement();
                org.jdom.Element corporate = root.getChild("T" + tag.trim());
                if (corporate != null) {
                    str = corporate.getChildText("Definition");
                }
            }
        } catch (Exception e) {
            new javax.swing.JOptionPane().showMessageDialog(this, "Please Check To See for XML Dictionary Available in the SystemFiles", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        return str;
    }

    public String getSubFieldName(String tag, String code) {
        String str = "";
        try {
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = null;
            try {
                doc = newgen.presentation.cataloguing.advanced.MARCXMLDictionary.getInstance().getDictionaryDocument(MARC_MODE);
            } catch (Exception e) {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Check To see XML DictionryFiles Existed or Not", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return null;
            }
            if (doc != null) {
                org.jdom.Element root = doc.getRootElement();
                org.jdom.Element corporate = null;
                if (root.getChild("T" + tag.trim()) != null) {
                    corporate = root.getChild("T" + tag.trim()).getChild("SubFields");
                }
                if (corporate != null) {
                    str = corporate.getChild("SF" + code.trim()).getChildText("Definition");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public void getAuthorityFile() {
    }

    public void cancel() {
        dtmMarc.setRowCount(0);
    }

    public void setISO2701(String stx) {
        try {
            this.cancel();
            newgenlib.marccomponent.conversion.Converter converter = new newgenlib.marccomponent.conversion.Converter();
            CatalogMaterialDescription cmd = converter.getMarcModelFromMarc(stx);
            Field[] datalist = cmd.getFields();
            for (int i = 0; i < datalist.length; i++) {
                Field daf = datalist[i];
                String tag = daf.getTag();
                if (tag != null && !tag.trim().equals("")) {
                    Object obj[] = { "", "", "", "", "", "", "", "", "" };
                    if (tagRadio.isSelected()) {
                        obj[2] = obj[4] = tag;
                    } else {
                        String tgnm = "";
                        tgnm = this.getTagName(tag.trim());
                        obj[2] = obj[4] = tag;
                        if (!tgnm.equals("")) obj[4] = tgnm.trim();
                    }
                    dtmMarc.addRow(obj);
                    SubField[] subfieldList = daf.getSubFields();
                    for (int sf = 0; sf < subfieldList.length; sf++) {
                        Object obj1[] = { "", "", "", "", "", "", "", "", "" };
                        SubField subf = subfieldList[sf];
                        if (subf != null) {
                            obj1[8] = new String(subf.getData());
                            obj1[3] = obj1[7] = String.valueOf(subf.getIdentifier());
                            if (nameRadio.isSelected()) {
                                obj1[7] = this.getSubFieldName(tag, String.valueOf(subf.getIdentifier()));
                            }
                            dtmMarc.addRow(obj1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setSeeAlso(String tagSeeAlso) {
        boolean valid = false;
        int k = -1;
        k = tabMarc.getSelectedRow();
        if (k != -1) {
            String tag = "";
            tag = this.getTagSelected();
            if (!tag.trim().equals("") && tag.equals(tagSeeAlso.trim())) {
                valid = true;
            }
        }
        return valid;
    }

    public void tableChanged(javax.swing.event.TableModelEvent e) {
        new javax.swing.JOptionPane().showMessageDialog(this, "this is table change event..");
    }

    /** Getter for property tagSelected.
     * @return Value of property tagSelected.
     *
     */
    public java.lang.String getTagSelected() {
        int k = -1;
        String str = "";
        k = tabMarc.getSelectedRow();
        if (k >= 0) {
            str = (String) dtmMarc.getValueAt(k, 2);
            if (!str.trim().equals("")) return str;
        } else str = "";
        return str;
    }

    /** Setter for property tagSelected.
     * @param tagSelected New value of property tagSelected.
     *
     */
    public void setTagSelected(java.lang.String tagSelected) {
        this.tagSelected = tagSelected;
    }

    public boolean isSelectedSeeAlsoIdExisted(String nameId) {
        boolean valid = false;
        for (int i = 0; i < tabMarc.getRowCount(); i++) {
            String nmId = "";
            nmId = (String) dtmMarc.getValueAt(i, 0);
            if (nmId.trim().equals(nameId.trim())) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    public void setSeeAlsoFields(String xmlStr, String tag, String nameId, String libId) {
        try {
            int k = -1, p = 0;
            String tagsel = "";
            k = tabMarc.getSelectedRow();
            if (!this.isSelectedSeeAlsoIdExisted(nameId)) {
                if (k != -1) {
                    tagsel = (String) dtmMarc.getValueAt(k, 2);
                    for (p = k + 1; p < tabMarc.getRowCount(); p++) {
                        String fld = "";
                        fld = (String) dtmMarc.getValueAt(p, 2);
                        if (!fld.trim().equals("")) {
                            break;
                        }
                    }
                    org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                    org.jdom.Document doc = sax.build(new java.io.StringReader(xmlStr));
                    org.jdom.Element root = doc.getRootElement();
                    java.util.List list = root.getChildren();
                    for (int i = 0; i < list.size(); i++) {
                        org.jdom.Element daf = (org.jdom.Element) list.get(i);
                        if (daf.getAttributeValue("tag").equals(tag.trim())) {
                            java.util.List sflist = daf.getChildren();
                            if (sflist.size() > 0) {
                                Object obj[] = { "", "", "", "", "", "", "", "", "" };
                                for (int q = p - 1; q >= k; q--) {
                                    dtmMarc.removeRow(q);
                                }
                                obj[0] = nameId.trim();
                                obj[1] = libId.trim();
                                if (tagRadio.isSelected()) {
                                    obj[2] = obj[4] = tagsel.trim();
                                } else {
                                    obj[2] = tagsel.trim();
                                    obj[4] = getTagName(tagsel.trim());
                                }
                                dtmMarc.insertRow(k, obj);
                                for (int j = 0; j < sflist.size(); j++) {
                                    org.jdom.Element sf = (org.jdom.Element) sflist.get(j);
                                    String code = "", val = "";
                                    code = sf.getAttributeValue("code");
                                    val = sf.getText();
                                    if (!code.trim().equals("") && !val.trim().equals("") && !code.trim().equals("libid") && !code.trim().equals("entryid") && !code.trim().equals("entrylibid")) {
                                        Object obj1[] = { "", "", "", "", "", "", "", "", "" };
                                        obj1[3] = obj1[7] = code.trim();
                                        obj1[8] = val.trim();
                                        k++;
                                        if (tagRadio.isSelected()) {
                                            obj1[3] = obj1[7] = code.trim();
                                            obj1[8] = val.trim();
                                        } else {
                                            obj1[3] = code.trim();
                                            obj1[7] = this.getSubFieldName(tagsel, code);
                                            obj1[8] = val.trim();
                                        }
                                        dtmMarc.insertRow(k, obj1);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                new javax.swing.JOptionPane().showMessageDialog(this, "Selected Record Already Existed", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        String title = "";
        for (int i = 0; i < dtmMarc.getRowCount(); i++) {
            String tit = "";
            tit = dtmMarc.getValueAt(i, 2).toString().trim();
            if (tit != null && tit.trim().equals("245")) {
                for (int j = i + 1; j < dtmMarc.getRowCount(); j++) {
                    String sf = "";
                    sf = dtmMarc.getValueAt(j, 3).toString().trim();
                    if (sf != null && sf.trim().equals("a")) {
                        title = dtmMarc.getValueAt(j, 8).toString().trim();
                        break;
                    }
                }
                break;
            }
        }
        return title;
    }

    /** Getter for property MARC_MODE.
     * @return Value of property MARC_MODE.
     *
     */
    public int getMARC_MODE() {
        return MARC_MODE;
    }

    /** Setter for property MARC_MODE.
     * @param MARC_MODE New value of property MARC_MODE.
     *
     */
    public void setMARC_MODE(int MARC_MODE) {
        this.MARC_MODE = MARC_MODE;
    }

    private javax.swing.JButton baddField;

    private javax.swing.JButton baddSubField;

    private javax.swing.JButton bnAGR;

    private javax.swing.JButton bnDelete;

    private javax.swing.JButton bnIndicators;

    private javax.swing.JButton brepeat;

    private javax.swing.ButtonGroup group1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JRadioButton nameRadio;

    private javax.swing.JTable tabMarc;

    private javax.swing.JRadioButton tagRadio;

    private javax.swing.table.DefaultTableModel dtmMarc = null;

    private int mode = 0;

    private int MARC_EDITOR_PN = 1;

    private int MARC_EDITOR_CN = 2;

    private int MARC_EDITOR_MN = 3;

    private String tagSelected = "";

    private int MARC_AUTH_MODE = 1;

    private int MARC_BIB_MODE = 2;

    private int MARC_MODE = 1;
}
