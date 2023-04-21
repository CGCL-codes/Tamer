package newgen.presentation.circulation;

/**
 *
 * @author  N VASU PRAVEEN
 */
public final class CPM extends javax.swing.JPanel {

    private static CPM instance = null;

    private javax.swing.table.DefaultTableModel defTbModel1;

    private javax.swing.table.DefaultTableModel defTbModel2;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private newgen.presentation.component.Utility utility = null;

    private java.util.ResourceBundle resourceBundle = null;

    private java.util.Vector vCategoryID = null;

    private java.util.Vector vCategoryName = null;

    private java.util.ArrayList alFine = null;

    private boolean mostRecent = false;

    /** Creates new form CirculationPrivilegeMatrix */
    public CPM() {
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        resourceBundle = newGenMain.getMyResource();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        vCategoryID = new java.util.Vector(1, 1);
        vCategoryName = new java.util.Vector(1, 1);
        initComponents();
        initComps();
        refreshData();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    public void refreshData() {
        System.out.println("in refresh data");
        cLibrary.setModel(utility.getLibraries());
        utility.getMaterialTypes(cMatType);
        cMatType.setSelectedItem("Book (Print, Microform, Electronic, etc.)");
        cLibrary.setSelectedItem(newGenMain.getLibraryName(newGenMain.getLibraryID()));
        getAvailablePatronCategoriesForTheLibrary(cLibrary.getSelectedItem().toString());
    }

    private void initComps() {
        this.defTbModel1 = new javax.swing.table.DefaultTableModel(new Object[] { newGenMain.getMyResource().getString("PhysicalPresentationForm"), newGenMain.getMyResource().getString("Loanlimit"), newGenMain.getMyResource().getString("Loanperiod"), newGenMain.getMyResource().getString("Renewallimit"), newGenMain.getMyResource().getString("Finesdefined?"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CeilingOfOverdue"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("DefaultCheckinDate") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setModel(defTbModel1);
        table1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.defTbModel2 = new javax.swing.table.DefaultTableModel(new Object[] { newGenMain.getMyResource().getString("From"), newGenMain.getMyResource().getString("To"), newGenMain.getMyResource().getString("Overdue") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return true;
            }
        };
        table2.getTableHeader().setReorderingAllowed(false);
        table2.setModel(defTbModel2);
    }

    private void getAvailablePatronCategoriesForTheLibrary(String library) {
        alFine = new java.util.ArrayList();
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryID", newGenMain.getLibraryId(library));
        String xmlStr = servletConnector.sendRequest("CPM", newGenXMLGenerator.buildXMLDocument("2", ht));
        ht = newGenXMLGenerator.parseXMLDocument(xmlStr);
        Object[] obj = new java.util.TreeSet(ht.keySet()).toArray();
        vCategoryID.removeAllElements();
        vCategoryName.removeAllElements();
        for (int i = 0; i < obj.length; i++) {
            vCategoryID.addElement(obj[i].toString().substring(3));
            vCategoryName.addElement(ht.get(obj[i]));
        }
        System.out.println("vCatName : " + vCategoryName);
        cPatCat.setModel(new javax.swing.DefaultComboBoxModel(vCategoryName));
    }

    public void getMostRecentCPMForThePatronCategory() {
        refreshScreen();
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryID", newGenMain.getLibraryID());
        ht.put("PatronCategoryLibraryID", newGenMain.getLibraryId(cLibrary.getSelectedItem().toString()));
        ht.put("PatronCategoryID", vCategoryID.elementAt(vCategoryName.indexOf(cPatCat.getSelectedItem())).toString());
        String xmlStr = servletConnector.sendRequest("CPM", newGenXMLGenerator.buildXMLDocument("4", ht));
        org.jdom.Element root = newGenXMLGenerator.getRootElement(xmlStr);
        String wef = root.getChildText("WEF");
        if (utility.getTestedString(wef).length() > 0) {
            java.text.SimpleDateFormat sdft = new java.text.SimpleDateFormat("MMM dd, yyyy");
            try {
                tWEF.setValue(sdft.parse(wef));
            } catch (java.text.ParseException ex) {
                System.out.println("ParseException while setting data");
            }
        }
        Object[] object = root.getChildren("Privilege").toArray();
        if (object.length > 0) {
            mostRecent = true;
            for (int i = 0; i < object.length; i++) {
                java.util.Vector vector = new java.util.Vector(1, 1);
                org.jdom.Element element = (org.jdom.Element) object[i];
                Object[][] row = new Object[1][1];
                defTbModel1.addRow(row);
                defTbModel1.setValueAt(element.getChildText("MaterialType"), i, 0);
                defTbModel1.setValueAt(new Integer(element.getChildText("LoanLimit")), i, 1);
                defTbModel1.setValueAt(new Integer(element.getChildText("LoanPeriod")), i, 2);
                defTbModel1.setValueAt(new Integer(element.getChildText("RenewalLimit")), i, 3);
                defTbModel1.setValueAt(new Double(element.getChildText("CeilingOfOverdue")), i, 5);
                defTbModel1.setValueAt(new java.util.Date(Long.parseLong(element.getChildText("DefaultCheckinDate"))), i, 6);
                Object[] object1 = element.getChildren("Fine").toArray();
                if (object1.length > 0) {
                    defTbModel1.setValueAt(new Boolean(true), i, 4);
                    for (int j = 0; j < object1.length; j++) {
                        org.jdom.Element element1 = (org.jdom.Element) object1[j];
                        vector.addElement(element1.getChildText("From"));
                        vector.addElement(element1.getChildText("To"));
                        vector.addElement(element1.getChildText("Fine"));
                    }
                } else {
                    defTbModel1.setValueAt(new Boolean(false), i, 4);
                }
                alFine.add(vector);
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Circulationprivilegematrixnotyetdefined"));
            mostRecent = false;
            cMatType.grabFocus();
        }
        System.out.println("mostRecent : " + mostRecent);
    }

    public void refreshScreen() {
        tWEF.setDate();
        for (int i = defTbModel1.getRowCount(); i > 0; i--) {
            defTbModel1.removeRow(i - 1);
        }
        for (int i = defTbModel2.getRowCount(); i > 0; i--) {
            defTbModel2.removeRow(i - 1);
        }
        tLoanLimit.setValue(new Integer(0));
        tLoanPeriod.setValue(new Integer(0));
        tRenewalLimit.setValue(new Integer(0));
        bEdit.setSelected(false);
        alFine = new java.util.ArrayList();
        mostRecent = false;
    }

    public void updateDatabase() {
        if (cLibrary.getSelectedIndex() != -1 && cPatCat.getSelectedIndex() != -1 && cMatType.getSelectedIndex() != -1 && defTbModel1.getRowCount() > 0 && !bEdit.isSelected()) {
            org.jdom.Element root = new org.jdom.Element("OperationID");
            root.setAttribute("no", "1");
            utility.addLoginDetailsToTheRootElement(root);
            org.jdom.Element element = new org.jdom.Element("PatronCategoryID");
            element.setText(vCategoryID.elementAt(vCategoryName.indexOf(cPatCat.getSelectedItem())).toString());
            root.addContent(element);
            element = new org.jdom.Element("PatronCategoryLibraryID");
            element.setText(newGenMain.getLibraryId(cLibrary.getSelectedItem().toString()));
            root.addContent(element);
            element = new org.jdom.Element("WEF");
            element.setText(tWEF.getDate());
            root.addContent(element);
            for (int i = 0; i < defTbModel1.getRowCount(); i++) {
                element = new org.jdom.Element("Privilege");
                org.jdom.Element element1 = new org.jdom.Element("MaterialTypeID");
                element1.setText(newGenMain.getMaterialId(defTbModel1.getValueAt(i, 0).toString()));
                element.addContent(element1);
                element1 = new org.jdom.Element("LoanLimit");
                element1.setText(defTbModel1.getValueAt(i, 1).toString());
                element.addContent(element1);
                element1 = new org.jdom.Element("LoanPeriod");
                element1.setText(defTbModel1.getValueAt(i, 2).toString());
                element.addContent(element1);
                element1 = new org.jdom.Element("RenewalLimit");
                element1.setText(defTbModel1.getValueAt(i, 3).toString());
                element.addContent(element1);
                element1 = new org.jdom.Element("CeilingOfOverdue");
                element1.setText(defTbModel1.getValueAt(i, 5).toString());
                element.addContent(element1);
                element1 = new org.jdom.Element("DefaultCheckinDate");
                element1.setText(String.valueOf(((java.util.Date) defTbModel1.getValueAt(i, 6)).getTime()));
                element.addContent(element1);
                element1 = new org.jdom.Element("FineDetails");
                java.util.Vector vector = new java.util.Vector(1, 1);
                try {
                    vector = (java.util.Vector) alFine.get(i);
                } catch (IndexOutOfBoundsException ex) {
                }
                for (int j = 0; j < vector.size(); j += 3) {
                    org.jdom.Element element3 = new org.jdom.Element("Fine");
                    org.jdom.Element element2 = new org.jdom.Element("From");
                    element2.setText(vector.elementAt(j).toString());
                    element3.addContent(element2);
                    element2 = new org.jdom.Element("To");
                    element2.setText(vector.elementAt(j + 1).toString());
                    element3.addContent(element2);
                    element2 = new org.jdom.Element("Fine");
                    element2.setText(vector.elementAt(j + 2).toString());
                    element3.addContent(element2);
                    element1.addContent(element3);
                }
                element.addContent(element1);
                root.addContent(element);
            }
            String xmlStr = newGenXMLGenerator.buildXMLDocument(root);
            xmlStr = servletConnector.sendRequest("CPM", xmlStr);
            java.util.Hashtable ht = newGenXMLGenerator.parseXMLDocument(xmlStr);
            if (ht != null && ht.size() > 0) {
                if (utility.getTestedString(ht.get("Success").toString()).equals("Y")) {
                    refreshScreen();
                    newGenMain.showInformationMessage(resourceBundle.getString("Ciculationprivilegematrixsuccessfullydefined"));
                } else if (utility.getTestedString(ht.get("Success").toString()).equals("N")) {
                    if (utility.getTestedString(ht.get("Exception").toString()).equals("Duplicate")) {
                        newGenMain.showInformationMessage(resourceBundle.getString("Circulationprivilegematrixdefinedalready"));
                    } else {
                        newGenMain.showErrorMessage(resourceBundle.getString("Problemoccuredwhilepostingdatatodatabase"));
                    }
                }
            } else {
                newGenMain.showErrorMessage(resourceBundle.getString("Problemoccuredwhilepostingdatatodatabase"));
            }
        } else {
            if (cLibrary.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nolibraries,contactyoursystemadministrator"));
            } else if (cPatCat.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nopatroncategories,contactyoursystemadministrator"));
            } else if (cMatType.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nophysical/presentationforms,contactyoursystemadministrator"));
            } else if (defTbModel1.getRowCount() == 0) {
                newGenMain.showInsufficientDataDialog(resourceBundle.getString("Definecirculationprivilegematrix"));
                cMatType.grabFocus();
            } else if (bEdit.isSelected()) {
                newGenMain.showInsufficientDataDialog(resourceBundle.getString("In\"Editmode\"cannotdopostingforCPM"));
                bEdit.grabFocus();
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pl2 = new javax.swing.JPanel();
        lLibrary = new javax.swing.JLabel();
        cLibrary = new javax.swing.JComboBox();
        lPatCat = new javax.swing.JLabel();
        cPatCat = new javax.swing.JComboBox();
        bCPM = new javax.swing.JButton();
        pl1 = new javax.swing.JPanel();
        lMatType = new javax.swing.JLabel();
        cMatType = new javax.swing.JComboBox();
        pl5 = new javax.swing.JPanel();
        lWEF = new javax.swing.JLabel();
        tWEF = new newgen.presentation.component.DateField();
        pl4 = new javax.swing.JPanel();
        spTable1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        pl6 = new javax.swing.JPanel();
        pl3 = new javax.swing.JPanel();
        lLoanPeriod = new javax.swing.JLabel();
        lLoanLimit = new javax.swing.JLabel();
        lRenewalLimit = new javax.swing.JLabel();
        tLoanLimit = new javax.swing.JFormattedTextField();
        tLoanPeriod = new javax.swing.JFormattedTextField();
        tRenewalLimit = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfDefaultCheckindate = new newgen.presentation.component.DateField();
        tfCeiling = new newgen.presentation.component.DoubleTextField();
        pl7 = new javax.swing.JPanel();
        spTable2 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        pl8 = new javax.swing.JPanel();
        bDelete = new javax.swing.JButton();
        bAdd = new javax.swing.JButton();
        pl9 = new javax.swing.JPanel();
        bAdd1 = new javax.swing.JButton();
        bDelete1 = new javax.swing.JButton();
        bEdit = new javax.swing.JToggleButton();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        pl2.setLayout(new java.awt.GridBagLayout());
        pl2.setBorder(new javax.swing.border.EtchedBorder());
        pl2.setPreferredSize(new java.awt.Dimension(425, 120));
        lLibrary.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Library"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl2.add(lLibrary, gridBagConstraints);
        cLibrary.setMinimumSize(new java.awt.Dimension(330, 25));
        cLibrary.setPreferredSize(new java.awt.Dimension(330, 25));
        cLibrary.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cLibraryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl2.add(cLibrary, gridBagConstraints);
        lPatCat.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Patroncategory"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl2.add(lPatCat, gridBagConstraints);
        cPatCat.setMinimumSize(new java.awt.Dimension(330, 25));
        cPatCat.setPreferredSize(new java.awt.Dimension(330, 25));
        cPatCat.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cPatCatActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl2.add(cPatCat, gridBagConstraints);
        bCPM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/FIND.GIF")));
        bCPM.setMnemonic('i');
        bCPM.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Showprivileges"));
        bCPM.setPreferredSize(new java.awt.Dimension(252, 26));
        bCPM.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCPMActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pl2.add(bCPM, gridBagConstraints);
        add(pl2);
        pl1.setLayout(new java.awt.GridBagLayout());
        pl1.setBorder(new javax.swing.border.EtchedBorder());
        pl1.setPreferredSize(new java.awt.Dimension(494, 70));
        lMatType.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalPresentationForm"));
        pl1.add(lMatType, new java.awt.GridBagConstraints());
        cMatType.setMinimumSize(new java.awt.Dimension(330, 25));
        cMatType.setPreferredSize(new java.awt.Dimension(330, 25));
        cMatType.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cMatTypeActionPerformed(evt);
            }
        });
        pl1.add(cMatType, new java.awt.GridBagConstraints());
        add(pl1);
        pl5.setLayout(new java.awt.GridBagLayout());
        pl5.setBorder(new javax.swing.border.EtchedBorder());
        pl5.setPreferredSize(new java.awt.Dimension(199, 70));
        lWEF.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("WEF"));
        pl5.add(lWEF, new java.awt.GridBagConstraints());
        tWEF.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                tWEFFocusLost(evt);
            }
        });
        pl5.add(tWEF, new java.awt.GridBagConstraints());
        add(pl5);
        pl4.setLayout(new java.awt.BorderLayout());
        pl4.setPreferredSize(new java.awt.Dimension(453, 150));
        table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        spTable1.setViewportView(table1);
        pl4.add(spTable1, java.awt.BorderLayout.CENTER);
        add(pl4);
        pl6.setLayout(new javax.swing.BoxLayout(pl6, javax.swing.BoxLayout.X_AXIS));
        pl6.setPreferredSize(new java.awt.Dimension(1473, 130));
        pl3.setLayout(new java.awt.GridBagLayout());
        pl3.setBorder(new javax.swing.border.EtchedBorder());
        pl3.setPreferredSize(new java.awt.Dimension(400, 150));
        lLoanPeriod.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Loanperiod(days)"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl3.add(lLoanPeriod, gridBagConstraints);
        lLoanLimit.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Loanlimit(number)"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl3.add(lLoanLimit, gridBagConstraints);
        lRenewalLimit.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Renewallimit(times)"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl3.add(lRenewalLimit, gridBagConstraints);
        tLoanLimit.setColumns(10);
        tLoanLimit.setValue(new Integer(0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl3.add(tLoanLimit, gridBagConstraints);
        tLoanPeriod.setColumns(10);
        tLoanPeriod.setValue(new Integer(0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl3.add(tLoanPeriod, gridBagConstraints);
        tRenewalLimit.setColumns(10);
        tRenewalLimit.setValue(new Integer(0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl3.add(tRenewalLimit, gridBagConstraints);
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CeilingOfOverdue"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl3.add(jLabel1, gridBagConstraints);
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("DefaultCheckinDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl3.add(jLabel2, gridBagConstraints);
        tfDefaultCheckindate.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        pl3.add(tfDefaultCheckindate, gridBagConstraints);
        tfCeiling.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        pl3.add(tfCeiling, gridBagConstraints);
        pl6.add(pl3);
        pl7.setLayout(new java.awt.BorderLayout());
        pl7.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Overduescharges")));
        pl7.setPreferredSize(new java.awt.Dimension(400, 403));
        table2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        spTable2.setViewportView(table2);
        pl7.add(spTable2, java.awt.BorderLayout.CENTER);
        pl6.add(pl7);
        pl8.setLayout(new java.awt.GridBagLayout());
        pl8.setBorder(new javax.swing.border.EtchedBorder());
        pl8.setPreferredSize(new java.awt.Dimension(120, 55));
        bDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Delete16.gif")));
        bDelete.setMnemonic('d');
        bDelete.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        bDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        pl8.add(bDelete, gridBagConstraints);
        bAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/New16.gif")));
        bAdd.setMnemonic('n');
        bAdd.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("New"));
        bAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddActionPerformed(evt);
            }
        });
        pl8.add(bAdd, new java.awt.GridBagConstraints());
        pl6.add(pl8);
        add(pl6);
        pl9.setLayout(new java.awt.GridBagLayout());
        pl9.setBorder(new javax.swing.border.EtchedBorder());
        pl9.setPreferredSize(new java.awt.Dimension(10, 50));
        bAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/New16.gif")));
        bAdd1.setMnemonic('n');
        bAdd1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("New"));
        bAdd1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAdd1ActionPerformed(evt);
            }
        });
        pl9.add(bAdd1, new java.awt.GridBagConstraints());
        bDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Delete16.gif")));
        bDelete1.setMnemonic('d');
        bDelete1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        bDelete1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDelete1ActionPerformed(evt);
            }
        });
        pl9.add(bDelete1, new java.awt.GridBagConstraints());
        bEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/modify.gif")));
        bEdit.setMnemonic('m');
        bEdit.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Modify"));
        bEdit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEditActionPerformed(evt);
            }
        });
        pl9.add(bEdit, new java.awt.GridBagConstraints());
        add(pl9);
    }

    private void cMatTypeActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void bAddActionPerformed(java.awt.event.ActionEvent evt) {
        if (cLibrary.getSelectedIndex() != -1 && cPatCat.getSelectedIndex() != -1 && cMatType.getSelectedIndex() != -1 && tWEF.getText().trim().length() > 0 && tLoanLimit.getText().trim().length() > 0 && tLoanPeriod.getText().trim().length() > 0 && tRenewalLimit.getText().trim().length() > 0) {
            Object[][] row = new Object[1][1];
            defTbModel2.addRow(row);
            if (defTbModel2.getRowCount() == 1) {
                defTbModel2.setValueAt(new Integer(0), defTbModel2.getRowCount() - 1, 0);
                defTbModel2.setValueAt(new Integer(0), defTbModel2.getRowCount() - 1, 1);
                defTbModel2.setValueAt(new Double(0.0), defTbModel2.getRowCount() - 1, 2);
            } else {
                int day = Integer.parseInt(defTbModel2.getValueAt(defTbModel2.getRowCount() - 2, 1).toString()) + 1;
                int diff = Integer.parseInt(defTbModel2.getValueAt(defTbModel2.getRowCount() - 2, 1).toString()) - Integer.parseInt(defTbModel2.getValueAt(defTbModel2.getRowCount() - 2, 0).toString());
                defTbModel2.setValueAt(new Integer(day), defTbModel2.getRowCount() - 1, 0);
                defTbModel2.setValueAt(new Integer(day + diff), defTbModel2.getRowCount() - 1, 1);
                defTbModel2.setValueAt(new Double(0.0), defTbModel2.getRowCount() - 1, 2);
            }
        }
    }

    private void cPatCatActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println(new java.util.Date().toString());
        refreshScreen();
    }

    private void cLibraryActionPerformed(java.awt.event.ActionEvent evt) {
        getAvailablePatronCategoriesForTheLibrary(cLibrary.getSelectedItem().toString());
        refreshScreen();
    }

    private void tWEFFocusLost(java.awt.event.FocusEvent evt) {
        if (utility.getDaysBetween(new java.util.Date(), tWEF.getValueAsDate()) < 0) {
            tWEF.setDate();
        }
    }

    private void bCPMActionPerformed(java.awt.event.ActionEvent evt) {
        if (cLibrary.getSelectedIndex() != -1 && cPatCat.getSelectedIndex() != -1) {
            getMostRecentCPMForThePatronCategory();
        } else {
            if (cLibrary.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nolibraries,contactyoursystemadministrator"));
            } else if (cPatCat.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nopatroncategories,contactyoursystemadministrator"));
            }
        }
    }

    private void bEditActionPerformed(java.awt.event.ActionEvent evt) {
        if (table1.getSelectedRow() > -1) {
            if (bEdit.isSelected()) {
                tLoanLimit.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 1).toString());
                tLoanPeriod.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 2).toString());
                tRenewalLimit.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 3).toString());
                tfDefaultCheckindate.setValue(defTbModel1.getValueAt(table1.getSelectedRow(), 6));
                tfCeiling.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 5).toString());
                java.util.Vector vector = new java.util.Vector(1, 1);
                try {
                    vector = (java.util.Vector) alFine.get(table1.getSelectedRow());
                } catch (IndexOutOfBoundsException ex) {
                }
                System.out.println("vector1 : " + vector);
                for (int i = defTbModel2.getRowCount(); i > 0; i--) {
                    defTbModel2.removeRow(i - 1);
                }
                if (mostRecent) {
                    for (int i = 0; i < vector.size(); i += 3) {
                        Object[][] row = new Object[1][1];
                        defTbModel2.addRow(row);
                        defTbModel2.setValueAt(new Integer(vector.elementAt(i).toString()), defTbModel2.getRowCount() - 1, 0);
                        defTbModel2.setValueAt(new Integer(vector.elementAt(i + 1).toString()), defTbModel2.getRowCount() - 1, 1);
                        defTbModel2.setValueAt(new Double(vector.elementAt(i + 2).toString()), defTbModel2.getRowCount() - 1, 2);
                    }
                } else {
                    for (int i = vector.size(); i > 0; i -= 3) {
                        Object[][] row = new Object[1][1];
                        defTbModel2.addRow(row);
                        defTbModel2.setValueAt(new Integer(vector.elementAt(i - 3).toString()), defTbModel2.getRowCount() - 1, 0);
                        defTbModel2.setValueAt(new Integer(vector.elementAt(i - 2).toString()), defTbModel2.getRowCount() - 1, 1);
                        defTbModel2.setValueAt(new Double(vector.elementAt(i - 1).toString()), defTbModel2.getRowCount() - 1, 2);
                    }
                }
            } else {
            }
        } else {
            bEdit.setSelected(false);
            if (table1.getRowCount() > 0) {
                newGenMain.showInsufficientDataDialog(resourceBundle.getString("Selectarecordtobeedited"));
                table1.grabFocus();
            } else {
                newGenMain.showInsufficientDataDialog(resourceBundle.getString("Definecirculationprivilegematrix"));
                tLoanLimit.grabFocus();
            }
        }
    }

    private void bDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int[] row = table2.getSelectedRows();
        if (row.length > 0) {
            for (int i = row.length; i > 0; i--) {
                defTbModel2.removeRow(row[i - 1]);
            }
            bAdd.grabFocus();
        } else {
            if (table2.getRowCount() > 0) {
                newGenMain.showInsufficientDataDialog(resourceBundle.getString("Selectarecordtobedeleted"));
                table2.grabFocus();
            }
        }
    }

    private void bDelete1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (table1.getSelectedRow() != -1) {
            try {
                alFine.remove(table1.getSelectedRow());
            } catch (IndexOutOfBoundsException ex) {
            }
            if (bEdit.isSelected()) {
                tLoanLimit.setText("");
                tLoanPeriod.setText("");
                tRenewalLimit.setText("");
                for (int i = defTbModel2.getRowCount(); i > 0; i--) {
                    defTbModel2.removeRow(i - 1);
                }
            }
            defTbModel1.removeRow(table1.getSelectedRow());
            cMatType.grabFocus();
        }
        bEdit.setSelected(false);
    }

    private void bAdd1ActionPerformed(java.awt.event.ActionEvent evt) {
        boolean privilegeDefined = false;
        for (int i = 0; i < defTbModel1.getRowCount(); i++) {
            if (defTbModel1.getValueAt(i, 0).toString().equals(cMatType.getSelectedItem().toString())) {
                privilegeDefined = true;
                break;
            }
        }
        if (cLibrary.getSelectedIndex() != -1 && cPatCat.getSelectedIndex() != -1 && cMatType.getSelectedIndex() != -1 && tWEF.getText().trim().length() > 0 && tLoanLimit.getText().trim().length() > 0 && tLoanPeriod.getText().trim().length() > 0 && tRenewalLimit.getText().trim().length() > 0) {
            if (!bEdit.isSelected()) {
                if (!privilegeDefined) {
                    Object[][] row = new Object[1][1];
                    defTbModel1.addRow(row);
                    defTbModel1.setValueAt(cMatType.getSelectedItem(), defTbModel1.getRowCount() - 1, 0);
                    defTbModel1.setValueAt(new Integer(tLoanLimit.getText()), defTbModel1.getRowCount() - 1, 1);
                    defTbModel1.setValueAt(new Integer(tLoanPeriod.getText()), defTbModel1.getRowCount() - 1, 2);
                    defTbModel1.setValueAt(new Integer(tRenewalLimit.getText()), defTbModel1.getRowCount() - 1, 3);
                    defTbModel1.setValueAt(new Double(tfCeiling.getValue().toString()), defTbModel1.getRowCount() - 1, 5);
                    defTbModel1.setValueAt((java.util.Date) tfDefaultCheckindate.getValue(), defTbModel1.getRowCount() - 1, 6);
                    tLoanLimit.setText("");
                    tLoanPeriod.setText("");
                    tRenewalLimit.setText("");
                    if (defTbModel2.getRowCount() > 0) {
                        table2.editCellAt(0, 0);
                        defTbModel1.setValueAt(new Boolean(true), defTbModel1.getRowCount() - 1, 4);
                        java.util.Vector vector = new java.util.Vector(1, 1);
                        for (int i = defTbModel2.getRowCount(); i > 0; i--) {
                            vector.addElement(defTbModel2.getValueAt(i - 1, 0));
                            vector.addElement(defTbModel2.getValueAt(i - 1, 1));
                            vector.addElement(defTbModel2.getValueAt(i - 1, 2));
                            defTbModel2.removeRow(i - 1);
                        }
                        System.out.println("vector : " + vector);
                        alFine.add(vector);
                    } else {
                        defTbModel1.setValueAt(new Boolean(false), defTbModel1.getRowCount() - 1, 4);
                    }
                } else {
                    newGenMain.showInformationMessage(resourceBundle.getString("Circulationprivilegematrixdefinedfor:") + cMatType.getSelectedItem());
                }
            } else {
                defTbModel1.setValueAt(new Integer(tLoanLimit.getText()), table1.getSelectedRow(), 1);
                defTbModel1.setValueAt(new Integer(tLoanPeriod.getText()), table1.getSelectedRow(), 2);
                defTbModel1.setValueAt(new Integer(tRenewalLimit.getText()), table1.getSelectedRow(), 3);
                tLoanLimit.setText("");
                tLoanPeriod.setText("");
                tRenewalLimit.setText("");
                if (defTbModel2.getRowCount() > 0) {
                    table2.editCellAt(0, 0);
                    defTbModel1.setValueAt(new Boolean(true), table1.getSelectedRow(), 4);
                    java.util.Vector vector = new java.util.Vector(1, 1);
                    try {
                        vector = (java.util.Vector) alFine.get(table1.getSelectedRow());
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    vector.removeAllElements();
                    for (int i = defTbModel2.getRowCount(); i > 0; i--) {
                        vector.addElement(defTbModel2.getValueAt(i - 1, 0));
                        vector.addElement(defTbModel2.getValueAt(i - 1, 1));
                        vector.addElement(defTbModel2.getValueAt(i - 1, 2));
                        defTbModel2.removeRow(i - 1);
                    }
                    System.out.println("vector : " + vector);
                } else {
                    defTbModel1.setValueAt(new Boolean(false), table1.getSelectedRow(), 4);
                }
                bEdit.setSelected(false);
            }
            cMatType.grabFocus();
        } else {
            if (cLibrary.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nolibraries,contactyoursystemadministrator"));
            } else if (cPatCat.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nopatroncategories,contactyoursystemadministrator"));
            } else if (cMatType.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Nophysical/presentationforms,contactyoursystemadministrator"));
            } else if (tWEF.getText().trim().length() == 0) {
                newGenMain.showInformationMessage(resourceBundle.getString("Enterwef"));
                tWEF.grabFocus();
            } else if (tLoanLimit.getText().trim().length() == 0) {
                newGenMain.showInformationMessage(resourceBundle.getString("Enterloanlimit"));
                tLoanLimit.grabFocus();
            } else if (tLoanPeriod.getText().trim().length() == 0) {
                newGenMain.showInformationMessage(resourceBundle.getString("Enterloanperiod"));
                tLoanPeriod.grabFocus();
            } else if (tRenewalLimit.getText().trim().length() == 0) {
                newGenMain.showInformationMessage(resourceBundle.getString("Enterrenewallimit"));
                tRenewalLimit.grabFocus();
            }
        }
    }

    public static CPM getInstance() {
        if (instance == null) instance = new CPM();
        return instance;
    }

    private javax.swing.JButton bAdd;

    private javax.swing.JButton bAdd1;

    private javax.swing.JButton bCPM;

    private javax.swing.JButton bDelete;

    private javax.swing.JButton bDelete1;

    private javax.swing.JToggleButton bEdit;

    private javax.swing.JComboBox cLibrary;

    private javax.swing.JComboBox cMatType;

    private javax.swing.JComboBox cPatCat;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel lLibrary;

    private javax.swing.JLabel lLoanLimit;

    private javax.swing.JLabel lLoanPeriod;

    private javax.swing.JLabel lMatType;

    private javax.swing.JLabel lPatCat;

    private javax.swing.JLabel lRenewalLimit;

    private javax.swing.JLabel lWEF;

    private javax.swing.JPanel pl1;

    private javax.swing.JPanel pl2;

    private javax.swing.JPanel pl3;

    private javax.swing.JPanel pl4;

    private javax.swing.JPanel pl5;

    private javax.swing.JPanel pl6;

    private javax.swing.JPanel pl7;

    private javax.swing.JPanel pl8;

    private javax.swing.JPanel pl9;

    private javax.swing.JScrollPane spTable1;

    private javax.swing.JScrollPane spTable2;

    private javax.swing.JFormattedTextField tLoanLimit;

    private javax.swing.JFormattedTextField tLoanPeriod;

    private javax.swing.JFormattedTextField tRenewalLimit;

    private newgen.presentation.component.DateField tWEF;

    private javax.swing.JTable table1;

    private javax.swing.JTable table2;

    private newgen.presentation.component.DoubleTextField tfCeiling;

    private newgen.presentation.component.DateField tfDefaultCheckindate;
}
