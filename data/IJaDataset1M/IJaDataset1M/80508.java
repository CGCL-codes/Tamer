package newgen.presentation.cataloguing.authorityFiles;

import org.jdom.*;
import org.jdom.output.*;

public class AuthorityUniformTitleSearchPanel extends javax.swing.JPanel {

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private newgen.presentation.component.Utility utility = null;

    javax.swing.table.DefaultTableModel dftsearchtab = null;

    javax.swing.JPopupMenu popup = null;

    javax.swing.JMenuItem popupmenu = null, modifyMenu = null, delete = null;

    /** Creates new form AuthorityUniformTitleSearchPanel */
    public AuthorityUniformTitleSearchPanel() {
        initComponents();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        utility = newgen.presentation.component.Utility.getInstance();
        setSize(750, 550);
        popup = new javax.swing.JPopupMenu();
        popupmenu = new javax.swing.JMenuItem();
        modifyMenu = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        bnSeeAlso.setEnabled(false);
        bnAuthorised.setEnabled(false);
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        String col[] = { "PersonalNameID", "SeeID", "SearchTerm", "Authorised/See" };
        Object res[] = { "", "PersonalNameID", "LibraryId", "SeeID", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchTerm"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Type"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Relation"), "Note", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TypeofHeading") };
        this.dftsearchtab = new javax.swing.table.DefaultTableModel(res, 0) {

            public boolean isCellEditable(int r, int c) {
                if (c == 0) return true; else return false;
            }

            public Class getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }
        };
        tabsearch.setModel(dftsearchtab);
        tabsearch.getTableHeader().setSize(new java.awt.Dimension(100, tabsearch.getHeight()));
        tabsearch.getTableHeader().setPreferredSize(new java.awt.Dimension(100, tabsearch.getRowHeight()));
        tabsearch.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabsearch.getColumnModel().getColumn(1).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(1).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(1).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(2).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(2).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(2).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(3).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(3).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(3).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(4).setPreferredWidth(400);
        tabsearch.getColumnModel().getColumn(5).setPreferredWidth(180);
        tabsearch.getColumnModel().getColumn(6).setPreferredWidth(130);
        tabsearch.getColumnModel().getColumn(7).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(7).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(7).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(8).setPreferredWidth(100);
        tabsearch.setRowHeight(tabsearch.getRowHeight() + 10);
        tabsearch.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tabsearch.getSelectedRow();
                String type = (String) dftsearchtab.getValueAt(row, 5);
                if (type.equals("Authorised Heading")) {
                    bnAuthorised.setEnabled(false);
                    bnSeeAlso.setEnabled(true);
                } else if (type.equals("See Term")) {
                    bnAuthorised.setEnabled(true);
                    bnSeeAlso.setEnabled(false);
                }
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    getPopMenu();
                    popup.show(tabsearch, e.getX(), e.getY());
                }
            }
        });
        popupmenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    popupmenuActionPerformed(evt);
                } catch (Exception e) {
                }
            }
        });
        modifyMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    bnModify.doClick();
                } catch (Exception e) {
                }
            }
        });
        delete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    bnDelete.doClick();
                } catch (Exception e) {
                }
            }
        });
    }

    public void setDefaultFocus() {
        tfsearch.grabFocus();
    }

    public void reloadLocales() {
        bnNew.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("New"));
        bnModify.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Modify"));
        bnDelete.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        bnAuthorised.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AuthorisedHeading"));
        bnAuthorised.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AuthorisedHeading"));
        bnSeeAlso.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SeeAlso"));
        bnSeeAlso.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SeeAlso"));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchTerm"));
        bngo_on_search.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Search"));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TermInReference"));
        jPanel2.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchResults")));
        Object res[] = { "", "PersonalNameID", "LibraryId", "SeeID", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchTerm"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Type"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Relation"), "Note", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TypeofHeading") };
        this.dftsearchtab.setColumnIdentifiers(res);
        tabsearch.setModel(dftsearchtab);
        tabsearch.getColumnModel().getColumn(1).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(1).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(1).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(2).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(2).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(2).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(3).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(3).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(3).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(7).setMinWidth(0);
        tabsearch.getColumnModel().getColumn(7).setMaxWidth(0);
        tabsearch.getColumnModel().getColumn(7).setPreferredWidth(0);
        tabsearch.getColumnModel().getColumn(8).setPreferredWidth(100);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        tfsearch.grabFocus();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        bnNew = new javax.swing.JButton();
        bnModify = new javax.swing.JButton();
        bnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        bnAuthorised = new javax.swing.JButton();
        bnSeeAlso = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfsearch = new newgen.presentation.UnicodeTextField();
        bngo_on_search = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblTermRef = new javax.swing.JLabel();
        msgLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabsearch = new javax.swing.JTable();
        setLayout(new java.awt.BorderLayout());
        jPanel1.setLayout(new java.awt.BorderLayout());
        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/New16.gif")));
        bnNew.setMnemonic('n');
        bnNew.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("New"));
        bnNew.setPreferredSize(new java.awt.Dimension(28, 28));
        bnNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnNewActionPerformed(evt);
            }
        });
        jToolBar1.add(bnNew);
        bnModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Edit16.gif")));
        bnModify.setMnemonic('m');
        bnModify.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Modify"));
        bnModify.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnModifyActionPerformed(evt);
            }
        });
        jToolBar1.add(bnModify);
        bnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Delete16.gif")));
        bnDelete.setMnemonic('d');
        bnDelete.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        bnDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(bnDelete);
        jSeparator1.setMaximumSize(new java.awt.Dimension(28, 28));
        jSeparator1.setPreferredSize(new java.awt.Dimension(28, 28));
        jToolBar1.add(jSeparator1);
        bnAuthorised.setMnemonic('a');
        bnAuthorised.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AuthorisedHeading"));
        bnAuthorised.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AuthorisedHeading"));
        bnAuthorised.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnAuthorisedActionPerformed(evt);
            }
        });
        jToolBar1.add(bnAuthorised);
        bnSeeAlso.setMnemonic('l');
        bnSeeAlso.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SeeAlso"));
        bnSeeAlso.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SeeAlso"));
        bnSeeAlso.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnSeeAlsoActionPerformed(evt);
            }
        });
        jToolBar1.add(bnSeeAlso);
        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);
        jPanel4.setLayout(new java.awt.GridBagLayout());
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchTerm"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel4.add(jLabel1, gridBagConstraints);
        tfsearch.setToolTipText("Please Enter Search Term");
        tfsearch.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfsearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel4.add(tfsearch, gridBagConstraints);
        bngo_on_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Find16.gif")));
        bngo_on_search.setMnemonic('s');
        bngo_on_search.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Search"));
        bngo_on_search.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bngo_on_searchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel4.add(bngo_on_search, gridBagConstraints);
        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 80));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TermInReference"));
        jPanel3.add(jLabel3);
        lblTermRef.setForeground(new java.awt.Color(0, 0, 171));
        lblTermRef.setPreferredSize(new java.awt.Dimension(350, 20));
        jPanel3.add(lblTermRef);
        msgLabel.setForeground(new java.awt.Color(255, 0, 0));
        jPanel3.add(msgLabel);
        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);
        add(jPanel1, java.awt.BorderLayout.NORTH);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchResults")));
        tabsearch.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jScrollPane1.setViewportView(tabsearch);
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }

    private void tfsearchActionPerformed(java.awt.event.ActionEvent evt) {
        bngo_on_search.doClick();
    }

    private void bnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int count = 0;
            int flag = 0;
            String xmlStr = "";
            for (int i = 0; i < tabsearch.getRowCount(); i++) {
                Boolean checked;
                String sid = "";
                checked = (Boolean) dftsearchtab.getValueAt(i, 0);
                if (checked.equals(new Boolean(true))) {
                    sid = (String) dftsearchtab.getValueAt(i, 3);
                    if (!sid.trim().equals("0")) {
                        flag = 1;
                        break;
                    }
                } else {
                    count++;
                }
            }
            if (tabsearch.getRowCount() == 0) {
                new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseSearchtheRecordsandChecktheRecordstobeDelete"), "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else if (flag == 1) {
                new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseTickAuthorisedRecordsToDelete"), "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else if (count == tabsearch.getRowCount()) {
                new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseTickTheRecordsToDelete"), "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            if (flag == 0 && count != tabsearch.getRowCount()) {
                newgen.presentation.cataloguing.authorityFiles.AuthorityDeletionConfirmDialog k = new newgen.presentation.cataloguing.authorityFiles.AuthorityDeletionConfirmDialog();
                k.show();
                if (k.getDeletionMode().trim().equals("Delete") || k.getDeletionMode().trim().equals("DeleteAndReplace")) {
                    java.util.Vector vecDll = new java.util.Vector(1, 1);
                    for (int i = 0; i < tabsearch.getRowCount(); i++) {
                        Boolean checked;
                        String sid = "";
                        checked = (Boolean) dftsearchtab.getValueAt(i, 0);
                        sid = (String) dftsearchtab.getValueAt(i, 3);
                        if (checked.equals(new Boolean(true)) && sid.trim().equals("0")) {
                            vecDll.addElement((String) dftsearchtab.getValueAt(i, 1));
                            vecDll.addElement((String) dftsearchtab.getValueAt(i, 2));
                        }
                    }
                    String xmlDel = "";
                    int cancelFlag = 0;
                    if (k.getDeletionMode().trim().equals("Delete")) {
                        xmlDel = new newgen.presentation.cataloguing.authorityFiles.AFFrequentlyUsedElemntComparator().getXMLForDelete(vecDll, "", "", "Delete", "12");
                    } else if (k.getDeletionMode().trim().equals("DeleteAndReplace")) {
                        String searc = tfsearch.getText().trim();
                        newgen.presentation.cataloguing.authorityFiles.AuthorityUniformTitleSearchDialog searchDialog = new newgen.presentation.cataloguing.authorityFiles.AuthorityUniformTitleSearchDialog();
                        searchDialog.setAction("Authorised");
                        searchDialog.setSearch(searc.trim());
                        searchDialog.setCreateButtonVisible(false);
                        searchDialog.setTitle("UniformTitle Search (Authorised Heading)");
                        searchDialog.show();
                        if (!searchDialog.getCancelSaveAction()) {
                            cancelFlag = 1;
                        } else {
                            String Id = searchDialog.getUniformTitleId();
                            String libId = searchDialog.getLibraryId();
                            xmlDel = new newgen.presentation.cataloguing.authorityFiles.AFFrequentlyUsedElemntComparator().getXMLForDelete(vecDll, Id, libId, "DeleteAndReplace", "12");
                        }
                    }
                    if (cancelFlag == 0 && !xmlDel.equals("")) {
                        xmlStr = servletConnector.getInstance().sendRequest("UniformTitleServlet", xmlDel);
                        org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                        org.jdom.Document doc1 = sax.build(new java.io.StringReader(xmlStr));
                        org.jdom.Element root = doc1.getRootElement();
                        if (root.getText().trim().equals("SuccessFullyDeleted")) {
                            bngo_on_search.doClick();
                            new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TaskSuccessful"));
                        } else {
                            new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FailureinDeletingRecordsPleaseContactTheVendor"), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bnModifyActionPerformed(java.awt.event.ActionEvent evt) {
        int k = -1;
        k = tabsearch.getSelectedRow();
        if (k != -1) {
            String nameId = "", libId = "", seeId = "", xmlNote = "";
            seeId = (String) tabsearch.getValueAt(k, 3);
            if (seeId.trim().equals("0")) {
                nameId = (String) tabsearch.getValueAt(k, 1);
                libId = (String) tabsearch.getValueAt(k, 2);
                xmlNote = (String) tabsearch.getValueAt(k, 7);
                newgen.presentation.cataloguing.authorityFiles.AuthorityNewUniformTitleDialog panel = new newgen.presentation.cataloguing.authorityFiles.AuthorityNewUniformTitleDialog(true);
                panel.setMode(2);
                panel.setSize(600, 500);
                panel.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(700, 500));
                panel.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ModifyUniformTitle"));
                panel.getDisplay(xmlNote);
                panel.getSeeAlsoRecForModify(nameId, libId);
                panel.setModifyNameId(nameId);
                panel.setModifyLibId(libId);
                panel.show();
                if (panel.getButtonMode().equals("MODIFY")) bngo_on_search.doClick();
            } else {
                new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ThisRecordIsnotModifiable"), "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } else {
            new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseSelectTheRowWhichYouwanttoModify"), "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void bnNewActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            AuthorityNewUniformTitleDialog newDialog = new AuthorityNewUniformTitleDialog(true);
            newDialog.setMode(1);
            newDialog.setSize(600, 500);
            newDialog.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(700, 500));
            newDialog.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NewUniformTitle"));
            newDialog.setUniformTitle(tfsearch.getText().trim());
            newDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bnAuthorisedActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.authorisedHeadingDetails();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void bnSeeAlsoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.seeAlsoDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bngo_on_searchActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            java.util.Hashtable ht = new java.util.Hashtable();
            if (!(tfsearch.getText().trim()).equals("")) {
                msgLabel.setText("please wait searching..");
                ht.put("SearchTerm", tfsearch.getText());
                newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
                String xmlStr = newGenXMLGenerator.buildXMLDocument("6", ht);
                String xmlRes = "";
                xmlRes = servletConnector.getInstance().sendRequest("UniformTitleServlet", xmlStr);
                String row[] = null;
                if (!(xmlRes.equals("") || xmlRes == null)) this.searchResult(xmlRes); else new javax.swing.JOptionPane().showMessageDialog(this, "Not Found ", "ERROR", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterSearchTerm"), "ERROR", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void searchResult(java.lang.String xmlStr) throws java.lang.Exception {
        msgLabel.setText("");
        dftsearchtab.setRowCount(0);
        org.jdom.Element root = newGenXMLGenerator.getRootElement(xmlStr);
        java.util.List childList = root.getChildren();
        String tempterm = tfsearch.getText();
        this.cancel();
        tfsearch.setText(tempterm);
        if (childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++) {
                org.jdom.Element searchrec = (org.jdom.Element) childList.get(i);
                String pid = "";
                pid = searchrec.getChild("UniformTitleID").getText();
                String libid = searchrec.getChild("libid").getText();
                String sid = searchrec.getChild("SeeID").getText();
                String sterm = searchrec.getChild("SeeTerm").getText();
                String relation = searchrec.getChild("Relation").getText();
                String series = searchrec.getChild("series").getText();
                String note = "";
                if (searchrec.getChild("Note") != null) note = searchrec.getChild("Note").getText();
                String type = "";
                if (sid.equals("0")) type = "Authorised Heading"; else type = "See Term";
                if (relation.equalsIgnoreCase("RT")) relation = "Related Term"; else if (relation.equalsIgnoreCase("BT")) relation = "Broader Term"; else if (relation.equalsIgnoreCase("NT")) relation = "Narrow Term";
                if (series.equals("A")) series = "Series"; else if (series.equals("B")) series = "non-Series";
                String row1[] = { "", pid, sid, sterm, type, relation, note };
                Object objrow[] = new Object[9];
                objrow[0] = new Boolean(false);
                objrow[1] = pid;
                objrow[2] = libid;
                objrow[3] = sid;
                objrow[4] = sterm;
                objrow[5] = type;
                objrow[6] = relation;
                objrow[7] = note;
                objrow[8] = series;
                dftsearchtab.addRow(objrow);
            }
        }
        if (dftsearchtab.getRowCount() > 0) {
            tabsearch.setRowSelectionInterval(0, 0);
        }
        if (dftsearchtab.getRowCount() == 0) {
            if (mode == VALIDATION_MODE) {
                String strError = "<html><head><body>" + "<p><font color=\"#FF0000\">This term is not available in current Authorityfile.</font></p>" + "<p><font color=\"#FF0000\">-You may create a new entry by clicking on Create New .</font></p>" + "<p><font color=\"#FF0000\">-Or refine your search and may select an existing entry.</font></p>" + "</body></html>";
                this.msgLabel.setText(strError);
            } else if (mode == SEARCH_MODE) {
                String strError = "<html><head><body>" + "<p><font color=\"#FF0000\">This term is not available in current Authorityfile.</font></p>" + "<p><font color=\"#FF0000\">-Please refine your search</font></p>" + "</body></html>";
                this.msgLabel.setText(strError);
            }
        }
    }

    public void cancel() {
        tfsearch.setText("");
        msgLabel.setText("");
        for (int i = dftsearchtab.getRowCount(); i > 0; i--) dftsearchtab.removeRow(i - 1);
    }

    public void getPopMenu() {
        popup.add(popupmenu);
        int row = tabsearch.getSelectedRow();
        int col = 5;
        String menuItemName = "";
        menuItemName = (String) dftsearchtab.getValueAt(row, 5);
        popup.add(modifyMenu);
        popup.add(delete);
        if (menuItemName.equals("Authorised Heading")) {
            popupmenu.setText("View See Also Terms");
            modifyMenu.setText("Modify");
        } else if (menuItemName.equals("See Term")) {
            popup.remove(modifyMenu);
            popupmenu.setText("View Authorised Heading");
        }
        delete.setText("Delete/Delete&Replace");
    }

    public void authorisedHeadingDetails() throws Exception {
        int row = tabsearch.getSelectedRow();
        int col = 0;
        String perId = (String) dftsearchtab.getValueAt(row, 1);
        String libId = (String) dftsearchtab.getValueAt(row, 2);
        String sid = (String) dftsearchtab.getValueAt(row, 3);
        String sterm = (String) dftsearchtab.getValueAt(row, 4);
        String type = (String) dftsearchtab.getValueAt(row, 5);
        String rowdata[] = { "", perId, libId, sid, sterm, type };
        Object obj[] = new Object[9];
        obj[0] = new Boolean(false);
        obj[1] = perId;
        obj[2] = libId;
        obj[3] = sid;
        obj[4] = sterm;
        obj[5] = type;
        obj[6] = "";
        obj[7] = "";
        obj[8] = "";
        this.cancel();
        dftsearchtab.addRow(rowdata);
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("UniformTitleId", perId);
        String xmlStr = newGenXMLGenerator.buildXMLDocument("7", ht);
        xmlStr = servletConnector.getInstance().sendRequest("UniformTitleServlet", xmlStr);
        this.searchResult(xmlStr);
        tfsearch.setText(sterm);
        dftsearchtab.insertRow(0, obj);
    }

    public void popupmenuActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
        if (popupmenu.getText().equals("View Authorised Heading")) {
            this.authorisedHeadingDetails();
        } else if (popupmenu.getText().equals("View See Also Terms")) {
            this.seeAlsoDetails();
        }
    }

    public void seeAlsoDetails() throws java.lang.Exception {
        int row = tabsearch.getSelectedRow();
        int col = 0;
        Object obj[] = new Object[9];
        String perId = (String) dftsearchtab.getValueAt(row, 1);
        String libId = (String) dftsearchtab.getValueAt(row, 2);
        String sid = (String) dftsearchtab.getValueAt(row, 3);
        String sterm = (String) dftsearchtab.getValueAt(row, 4);
        String type = (String) dftsearchtab.getValueAt(row, 5);
        String note = (String) dftsearchtab.getValueAt(row, 7);
        obj[0] = new Boolean(false);
        obj[1] = perId;
        obj[2] = libId;
        obj[3] = sid;
        obj[4] = sterm;
        obj[5] = type;
        obj[6] = "";
        obj[7] = note;
        obj[8] = "";
        this.cancel();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("UniformTitleId", perId);
        String xmlStr = newGenXMLGenerator.buildXMLDocument("8", ht);
        xmlStr = servletConnector.getInstance().sendRequest("UniformTitleServlet", xmlStr);
        this.searchResult(xmlStr);
        tfsearch.setText(sterm);
        dftsearchtab.insertRow(0, obj);
    }

    public void setSearch(java.lang.String searchTerm) {
        tfsearch.setText(searchTerm);
        bngo_on_search.doClick(1000);
    }

    public String getUniformTitleId() throws Exception {
        int row = tabsearch.getSelectedRow();
        String personalID = "";
        String sid = (String) dftsearchtab.getValueAt(row, 3);
        if (sid.equals("0")) personalID = (String) dftsearchtab.getValueAt(row, 1); else personalID = "";
        return personalID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDateOfwork(String str) {
        this.datework = str;
    }

    public String getDateOfWork() {
        return this.datework;
    }

    public void setLanguage(String lang) {
        this.language = lang;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setVersion(String ver) {
        this.version = ver;
    }

    public String getVersion() {
        return this.version;
    }

    public void setNameOrPart(java.util.ArrayList arr) {
        this.nameorpart = arr;
    }

    public java.util.ArrayList getNameOrpart() {
        return this.nameorpart;
    }

    public void setLibraryId(String lib) {
        this.libId = lib;
    }

    public String getLibraryId() {
        return this.libId;
    }

    public void setSearchRecXML(String str) {
        this.searchRecXML = str;
    }

    public String getSearchRecXML() {
        return this.searchRecXML;
    }

    public void setParameters() {
        try {
            this.nameorpart = new java.util.ArrayList();
            int row = tabsearch.getSelectedRow();
            String personalID = "";
            String sid = (String) dftsearchtab.getValueAt(row, 3);
            if (sid.equals("0")) {
                personalID = (String) dftsearchtab.getValueAt(row, 1);
                String libid = (String) dftsearchtab.getValueAt(row, 2);
                this.setLibraryId(libid);
                String xmlNote = "";
                xmlNote = (String) dftsearchtab.getValueAt(row, 7);
                org.jdom.input.SAXBuilder saxb = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = saxb.build(new java.io.StringReader(xmlNote));
                org.jdom.Element root = doc.getRootElement();
                java.util.List childList = root.getChildren();
                this.setSearchRecXML(xmlNote);
                String display = (String) dftsearchtab.getValueAt(row, 4);
                this.setDisplayString(display);
                for (int i = 0; i < childList.size(); i++) {
                    org.jdom.Element mostfreq = (org.jdom.Element) childList.get(i);
                    if (mostfreq.getAttributeValue("tag").equals("130")) {
                        java.util.List perChild = mostfreq.getChildren();
                        java.util.ArrayList partSection = new java.util.ArrayList();
                        for (int sub = 0; sub < perChild.size(); sub++) {
                            org.jdom.Element subfield = (org.jdom.Element) perChild.get(sub);
                            if (subfield.getAttributeValue("code").equals("a")) {
                                this.setTitle(utility.getTestedString(subfield.getText()));
                            }
                            if (subfield.getAttributeValue("code").equals("f")) {
                                this.setDateOfwork(utility.getTestedString(subfield.getText()));
                            }
                            if (subfield.getAttributeValue("code").equals("l")) {
                                this.setLanguage(utility.getTestedString(subfield.getText()));
                            }
                            if (subfield.getAttributeValue("code").equals("s")) {
                                this.setVersion(utility.getTestedString(subfield.getText()));
                            }
                            if (subfield.getAttributeValue("code").equals("p")) {
                                partSection.add(utility.getTestedString(subfield.getText()));
                            }
                        }
                        this.setNameOrPart(partSection);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Getter for property displayString.
     * @return Value of property displayString.
     *
     */
    public java.lang.String getDisplayString() {
        return displayString;
    }

    /** Setter for property displayString.
     * @param displayString New value of property displayString.
     *
     */
    public void setDisplayString(java.lang.String displayString) {
        this.displayString = displayString;
    }

    public java.util.Vector getIdforExactSearchRecord(String xmlCatlogue) {
        if (dftsearchtab.getRowCount() > 0) {
            for (int i = 0; i < dftsearchtab.getRowCount(); i++) {
                String sid = (String) dftsearchtab.getValueAt(i, 3);
                if (sid.equals("0")) {
                    String xmlNote = (String) dftsearchtab.getValueAt(i, 7);
                    if (isXMLRecordEqual(xmlCatlogue, xmlNote)) {
                        java.util.Vector vec = new java.util.Vector(1, 1);
                        String Id = (String) dftsearchtab.getValueAt(i, 1);
                        String libId = (String) dftsearchtab.getValueAt(i, 2);
                        vec.addElement(Id);
                        vec.addElement(libId);
                        return vec;
                    }
                }
            }
        }
        return null;
    }

    public boolean isXMLRecordEqual(String xmlCat, String xmlNote) {
        boolean equal = false;
        try {
            org.jdom.input.SAXBuilder saxSearch = new org.jdom.input.SAXBuilder();
            org.jdom.Document docSearch = saxSearch.build(new java.io.StringReader(xmlCat));
            org.jdom.Element record = docSearch.getRootElement().getChild("record");
            java.util.List lstCat = record.getChildren();
            org.jdom.Element catEle = null;
            for (int k = 0; k < lstCat.size(); k++) {
                org.jdom.Element datafield = (org.jdom.Element) lstCat.get(k);
                String tag = datafield.getAttributeValue("tag");
                if (tag.equals("130")) {
                    catEle = datafield;
                    break;
                }
            }
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = sax.build(new java.io.StringReader(xmlNote));
            org.jdom.Element rec = doc.getRootElement();
            java.util.List lstAutht = rec.getChildren();
            org.jdom.Element authEle = null;
            for (int k = 0; k < lstAutht.size(); k++) {
                org.jdom.Element datafield = (org.jdom.Element) lstAutht.get(k);
                String tag = datafield.getAttributeValue("tag");
                if (tag.equals("130")) {
                    authEle = datafield;
                    break;
                }
            }
            if (authEle != null && catEle != null) {
                equal = (new AFFrequentlyUsedElemntComparator().equals(authEle, catEle));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equal;
    }

    public void setTermInReference(String str) {
        this.lblTermRef.setText(str.trim());
    }

    public boolean isAccessToSelectExisting() {
        boolean valid = false;
        int k = -1;
        k = tabsearch.getSelectedRow();
        if (tabsearch.getRowCount() > 0 && k != -1) {
            valid = true;
        }
        if (tabsearch.getRowCount() <= 0) {
        } else if (k == -1) {
            new javax.swing.JOptionPane().showMessageDialog(this, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseSelectTheRow"), "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        return valid;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private javax.swing.JButton bnAuthorised;

    private javax.swing.JButton bnDelete;

    private javax.swing.JButton bnModify;

    private javax.swing.JButton bnNew;

    private javax.swing.JButton bnSeeAlso;

    private javax.swing.JButton bngo_on_search;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JLabel lblTermRef;

    private javax.swing.JLabel msgLabel;

    private javax.swing.JTable tabsearch;

    private newgen.presentation.UnicodeTextField tfsearch;

    private final int VALIDATION_MODE = 0;

    private final int SEARCH_MODE = 1;

    private int mode = -1;

    private String title = "";

    private String datework = "";

    private String language = "";

    private String version = "";

    private java.util.ArrayList nameorpart = null;

    private String libId = "";

    private String uniformId = "";

    private String searchRecXML = "";

    private String displayString = "";
}
