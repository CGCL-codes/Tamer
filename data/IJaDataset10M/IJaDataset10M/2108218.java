package newgen.presentation.circulation;

/**
 *
 * @author  vasu praveen
 */
public class CheckinFromBinder extends javax.swing.JPanel {

    private static CheckinFromBinder instance = null;

    private javax.swing.table.DefaultTableModel defTbModel = null;

    private javax.swing.table.DefaultTableModel dtmBudgets = null;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private newgen.presentation.component.Utility utility = null;

    private java.util.Vector vBinderID = null;

    private java.util.Vector vBinderName = null;

    private java.util.Vector vBinderAddress = null;

    private java.util.Vector vAmt = null;

    private java.util.Vector vOrderDate = null;

    private java.util.Vector vDueDate = null;

    /** Creates new form CheckoutToBinder */
    private CheckinFromBinder() {
        initComponents();
        initComps();
        pl1.add(pl12);
        pl1.add(pl22, java.awt.BorderLayout.LINE_END);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    private void initComps() {
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        java.util.ArrayList arrayList = new java.util.ArrayList();
        arrayList.add(newGenMain.getLibraryID());
        String[] cols = { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Amount") };
        dtmBudgets = new javax.swing.table.DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tableBudgets.setModel(dtmBudgets);
        tOrderAmt.setValue(new Double(0.00));
        this.defTbModel = new javax.swing.table.DefaultTableModel(new Object[] { newGenMain.getMyResource().getString("Barcode"), newGenMain.getMyResource().getString("Title/Author"), newGenMain.getMyResource().getString("Volume"), newGenMain.getMyResource().getString("PhysicalPresentationForm"), newGenMain.getMyResource().getString("Bindingtype"), newGenMain.getMyResource().getString("Amount"), newGenMain.getMyResource().getString("Verify") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        tabItem.setModel(defTbModel);
        tabItem.setRowHeight(20);
        tBarcode.grabFocus();
    }

    private void getOrderDetailsForTheDocument() {
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("Barcode", tBarcode.getText().trim());
        ht.put("LibraryID", newGenMain.getLibraryID());
        String xmlStr = servletConnector.sendRequest("CheckinFromBinder", newGenXMLGenerator.buildXMLDocument("5", ht));
        System.out.println("clt xmlStr : " + xmlStr);
        org.jdom.Element root = newGenXMLGenerator.getRootElement(xmlStr);
        String itemStatus = root.getChildText("ItemStatus");
        System.out.println("item status : " + itemStatus);
        if (itemStatus.equals("Sent for binding")) {
            cBinder.setText(root.getChild("OrderDetails").getChildText("BinderName"));
            taAddress.setText(root.getChild("OrderDetails").getChildText("BinderAddress"));
            taAddress.setCaretPosition(0);
            cOrderNo.setText(root.getChild("OrderDetails").getChildText("OrderNumber"));
            tOrderDate.setDate(root.getChild("OrderDetails").getChildText("OrderDate"));
            tDueDate.setDate(root.getChild("OrderDetails").getChildText("DueDate"));
            Object[] document = root.getChild("DocumentDetails").getChildren().toArray();
            for (int i = 0; i < document.length; i++) {
                defTbModel.addRow(new Object[1][1]);
                defTbModel.setValueAt(((org.jdom.Element) document[i]).getChildText("Barcode"), i, 0);
                defTbModel.setValueAt(utility.getTestedString(((org.jdom.Element) document[i]).getChildText("Title")), i, 1);
                defTbModel.setValueAt(utility.getTestedString(((org.jdom.Element) document[i]).getChildText("Volume")), i, 2);
                defTbModel.setValueAt(((org.jdom.Element) document[i]).getChildText("MaterialType"), i, 3);
                defTbModel.setValueAt(((org.jdom.Element) document[i]).getChildText("BindType"), i, 4);
                defTbModel.setValueAt(new Double("" + ((org.jdom.Element) document[i]).getChildText("Amount")), i, 5);
                if (tBarcode.getText().trim().equalsIgnoreCase(((org.jdom.Element) document[i]).getChildText("Barcode"))) defTbModel.setValueAt(new Boolean(true), i, 6); else defTbModel.setValueAt(new Boolean(false), i, 6);
            }
            Object[] budget = root.getChild("BudgetDetails").getChildren().toArray();
            double totAmt = 0.0;
            for (int i = 0; i < budget.length; i++) {
                org.jdom.Element elex = (org.jdom.Element) budget[i];
                Object[] row = new Object[3];
                row[0] = elex.getChildText("BudgetID");
                row[1] = elex.getChildText("BudgetHead");
                row[2] = elex.getChildText("Amount");
                dtmBudgets.addRow(row);
                double amt = Double.parseDouble("" + row[2]);
                totAmt += amt;
            }
            tOrderAmt.setValue(new Double(totAmt));
            tBarcode.setText("");
        } else {
            if (itemStatus.equals("Invalid")) {
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Invalidbarcode"));
                tBarcode.grabFocus();
            } else if (itemStatus.equals("Checked out")) {
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Documentcheckedout"));
                tBarcode.grabFocus();
            } else if (itemStatus.equals("Available")) {
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Availableinlibrary"));
                tBarcode.grabFocus();
            } else if (itemStatus.equals("Seperated for binding")) {
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Seperatedforbinding"));
                tBarcode.grabFocus();
            }
        }
    }

    private boolean validateScreen() {
        boolean verified = false;
        for (int i = 0; i < defTbModel.getRowCount(); i++) {
            if (!((Boolean) defTbModel.getValueAt(i, 6)).booleanValue()) {
                verified = false;
                break;
            } else {
                verified = true;
                continue;
            }
        }
        if (cBinder.getText().trim().length() > 0 && cOrderNo.getText().trim().length() > 0 && tReturnDate.getText().trim().length() > 0 && verified) {
            return true;
        } else {
            if (cBinder.getText().trim().length() == 0) {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Nobindersforthislibrary"));
            } else if (cOrderNo.getText().trim().length() == 0) {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Nopendingorders"));
                cOrderNo.grabFocus();
            } else if (tReturnDate.getText().trim().length() == 0) {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Fillreturndate"));
                tReturnDate.grabFocus();
            } else if (!verified) {
                if (defTbModel.getRowCount() > 0) {
                    newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Verifyallthedocumentsintheorder"));
                    tBarcode.grabFocus();
                } else {
                    newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Nodocumentsinthisorder"));
                    if (cOrderNo.getText().trim().length() > 0) {
                        cOrderNo.grabFocus();
                    } else if (cBinder.getText().trim().length() > 0) {
                        cBinder.grabFocus();
                    }
                }
            }
            return false;
        }
    }

    protected void updateDatabase() {
        if (validateScreen()) {
            String invoiceNumber = javax.swing.JOptionPane.showInputDialog(this, newGenMain.getMyResource().getString("Fillinvoicenumber"), newGenMain.getMyResource().getString("Invoicenumber"), javax.swing.JOptionPane.OK_CANCEL_OPTION);
            if (invoiceNumber.trim().length() > 0) {
                org.jdom.Element root = new org.jdom.Element("OperationID");
                root.setAttribute("no", "4");
                org.jdom.Element element = new org.jdom.Element("OrderNumber");
                element.setText("" + cOrderNo.getText());
                root.addContent(element);
                element = new org.jdom.Element("ReturnDate");
                element.setText(tReturnDate.getDate());
                root.addContent(element);
                element = new org.jdom.Element("InvoiceNumber");
                element.setText(invoiceNumber);
                root.addContent(element);
                utility.addLoginDetailsToTheRootElement(root);
                element = new org.jdom.Element("DocumentDetails");
                for (int i = 0; i < defTbModel.getRowCount(); i++) {
                    if (((Boolean) defTbModel.getValueAt(i, 6)).booleanValue()) {
                        org.jdom.Element subElement = new org.jdom.Element("DocumentID");
                        subElement.setText("" + defTbModel.getValueAt(i, 0));
                        element.addContent(subElement);
                    }
                }
                root.addContent(element);
                String xmlStr = newGenXMLGenerator.buildXMLDocument(root);
                xmlStr = servletConnector.sendRequest("CheckinFromBinder", xmlStr);
                String success = utility.getTestedString(newGenXMLGenerator.getRootElement(xmlStr).getChildText("Success"));
                if (success.equals("N")) {
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Problemoccuredwhilecheckingin"));
                } else if (success.equals("Y")) {
                    refreshScreen();
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("TaskSuccessful"));
                } else {
                    refreshScreen();
                    java.util.Vector[] vector = newGenXMLGenerator.parseXMLDocument(new String[] { "Barcode", "Patron", "EMail", "Print", "FormId", "PayslipFormID", "PrintCopies" }, xmlStr);
                    newgen.presentation.component.MailDetailsDialog mailDetailsDialog = new newgen.presentation.component.MailDetailsDialog(javax.swing.JOptionPane.getFrameForComponent(newGenMain), true, newgen.presentation.component.NewGenConstants.BATCH_DISPATCH_MODE);
                    mailDetailsDialog.setMailDispatchDetails(vector);
                    mailDetailsDialog.show();
                    int printCopies = new Integer(vector[6].elementAt(0).toString()).intValue();
                    if (printCopies > 0) {
                        String formid = utility.getTestedString(vector[4].elementAt(0).toString());
                        if (!formid.equals("") && !formid.equals("0")) {
                            String[] formId = new String[2];
                            formId[0] = vector[4].elementAt(0).toString();
                            formId[1] = vector[5].elementAt(0).toString();
                            newgen.presentation.component.PrintComponentDialog.getInstance().setData(new Integer(newGenMain.getLibraryID()), formId);
                            newgen.presentation.component.PrintComponentDialog.getInstance().setModal(true);
                            newgen.presentation.component.PrintComponentDialog.getInstance().show();
                        }
                    }
                }
            }
        }
    }

    protected void refreshScreen() {
        cBinder.setText("");
        taAddress.setText("");
        cOrderNo.setText("");
        tDueDate.setText("");
        tOrderAmt.setText("0.00");
        tOrderDate.setDate();
        tReturnDate.setDate();
        tBarcode.setText("");
        for (int i = defTbModel.getRowCount(); i > 0; i--) {
            defTbModel.removeRow(i - 1);
        }
        dtmBudgets.setRowCount(0);
    }

    public void reloadLocales() {
        lBinder.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Binder"));
        lAddress.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Address"));
        lOrderNo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ordernumber"));
        lOrderDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderdate"));
        lReturnDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Returndate"));
        lDueDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Duedate"));
        lOrderAmt.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Amount"));
        lBarcode.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Barcode"));
        bGo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        pl21.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Documentssentforbinding")));
        this.defTbModel.setColumnIdentifiers(new Object[] { newGenMain.getMyResource().getString("Barcode"), newGenMain.getMyResource().getString("Title/Author"), newGenMain.getMyResource().getString("Volume"), newGenMain.getMyResource().getString("PhysicalPresentationForm"), newGenMain.getMyResource().getString("Bindingtype"), newGenMain.getMyResource().getString("Amount"), newGenMain.getMyResource().getString("Verify") });
        tabItem.setModel(defTbModel);
        String[] cols = { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Amount") };
        dtmBudgets.setColumnIdentifiers(cols);
        tableBudgets.setModel(dtmBudgets);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pl1 = new javax.swing.JPanel();
        pl12 = new javax.swing.JPanel();
        lBinder = new javax.swing.JLabel();
        spAddress = new javax.swing.JScrollPane();
        taAddress = new newgen.presentation.VKTextArea();
        cBinder = new newgen.presentation.UnicodeTextField();
        lAddress = new javax.swing.JLabel();
        lOrderNo = new javax.swing.JLabel();
        cOrderNo = new newgen.presentation.UnicodeTextField();
        lOrderDate = new javax.swing.JLabel();
        tOrderDate = new newgen.presentation.component.DateField();
        tReturnDate = new newgen.presentation.component.DateField();
        lReturnDate = new javax.swing.JLabel();
        tDueDate = new newgen.presentation.component.DateField();
        lDueDate = new javax.swing.JLabel();
        lOrderAmt = new javax.swing.JLabel();
        tOrderAmt = new javax.swing.JFormattedTextField();
        pl22 = new javax.swing.JPanel();
        lBarcode = new javax.swing.JLabel();
        tBarcode = new newgen.presentation.UnicodeTextField();
        bGo = new javax.swing.JButton();
        pl2 = new javax.swing.JPanel();
        pl21 = new javax.swing.JPanel();
        spItems = new javax.swing.JScrollPane();
        tabItem = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBudgets = new javax.swing.JTable();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        pl1.setLayout(new java.awt.BorderLayout());
        pl1.setPreferredSize(new java.awt.Dimension(10, 200));
        pl12.setLayout(new java.awt.GridBagLayout());
        pl12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lBinder.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Binder"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lBinder, gridBagConstraints);
        taAddress.setColumns(30);
        taAddress.setEditable(false);
        taAddress.setLineWrap(true);
        taAddress.setRows(3);
        taAddress.setWrapStyleWord(true);
        taAddress.setFont(new java.awt.Font("Dialog", 0, 12));
        spAddress.setViewportView(taAddress);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl12.add(spAddress, gridBagConstraints);
        cBinder.setColumns(30);
        cBinder.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl12.add(cBinder, gridBagConstraints);
        lAddress.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Address"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lAddress, gridBagConstraints);
        lOrderNo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ordernumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lOrderNo, gridBagConstraints);
        cOrderNo.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        pl12.add(cOrderNo, gridBagConstraints);
        lOrderDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderdate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lOrderDate, gridBagConstraints);
        tOrderDate.setEditable(false);
        tOrderDate.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl12.add(tOrderDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl12.add(tReturnDate, gridBagConstraints);
        lReturnDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Returndate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lReturnDate, gridBagConstraints);
        tDueDate.setEditable(false);
        tDueDate.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl12.add(tDueDate, gridBagConstraints);
        lDueDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Duedate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lDueDate, gridBagConstraints);
        lOrderAmt.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Amount"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lOrderAmt, gridBagConstraints);
        tOrderAmt.setColumns(15);
        tOrderAmt.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl12.add(tOrderAmt, gridBagConstraints);
        pl1.add(pl12, java.awt.BorderLayout.CENTER);
        pl22.setLayout(new java.awt.GridBagLayout());
        pl22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lBarcode.setForeground(new java.awt.Color(170, 0, 0));
        lBarcode.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Barcode"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pl22.add(lBarcode, gridBagConstraints);
        tBarcode.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBarcodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pl22.add(tBarcode, gridBagConstraints);
        bGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bGo.setMnemonic('g');
        bGo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bGo.setPreferredSize(new java.awt.Dimension(20, 20));
        bGo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl22.add(bGo, gridBagConstraints);
        pl1.add(pl22, java.awt.BorderLayout.EAST);
        add(pl1);
        pl2.setLayout(new java.awt.BorderLayout());
        pl2.setPreferredSize(new java.awt.Dimension(463, 200));
        pl21.setLayout(new java.awt.BorderLayout());
        pl21.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Documentssentforbinding")));
        tabItem.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        spItems.setViewportView(tabItem);
        pl21.add(spItems, java.awt.BorderLayout.CENTER);
        pl2.add(pl21, java.awt.BorderLayout.CENTER);
        add(pl2);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setPreferredSize(new java.awt.Dimension(453, 100));
        tableBudgets.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane1.setViewportView(tableBudgets);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel1);
    }

    private void bGoActionPerformed(java.awt.event.ActionEvent evt) {
        verifyTheDocument();
    }

    private void verifyTheDocument() {
        if (tBarcode.getText().trim().length() > 0) {
            if (defTbModel.getRowCount() > 0) {
                boolean valid = false;
                for (int i = 0; i < defTbModel.getRowCount(); i++) {
                    if (tBarcode.getText().trim().equalsIgnoreCase("" + defTbModel.getValueAt(i, 0))) {
                        valid = true;
                        if (new Boolean("" + defTbModel.getValueAt(i, 6)).booleanValue()) defTbModel.setValueAt(new Boolean(false), i, 6); else defTbModel.setValueAt(new Boolean(true), i, 6);
                        tBarcode.setText("");
                        break;
                    }
                }
                if (!valid) {
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Invalidbarcode"));
                }
            } else {
                getOrderDetailsForTheDocument();
            }
        } else {
            newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Fillbarcode"));
        }
    }

    private void tBarcodeActionPerformed(java.awt.event.ActionEvent evt) {
        bGo.doClick();
    }

    private void cOrderNoActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void cBinderActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public static CheckinFromBinder getInstance() {
        instance = new CheckinFromBinder();
        return instance;
    }

    private javax.swing.JButton bGo;

    private newgen.presentation.UnicodeTextField cBinder;

    private newgen.presentation.UnicodeTextField cOrderNo;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lAddress;

    private javax.swing.JLabel lBarcode;

    private javax.swing.JLabel lBinder;

    private javax.swing.JLabel lDueDate;

    private javax.swing.JLabel lOrderAmt;

    private javax.swing.JLabel lOrderDate;

    private javax.swing.JLabel lOrderNo;

    private javax.swing.JLabel lReturnDate;

    private javax.swing.JPanel pl1;

    private javax.swing.JPanel pl12;

    private javax.swing.JPanel pl2;

    private javax.swing.JPanel pl21;

    private javax.swing.JPanel pl22;

    private javax.swing.JScrollPane spAddress;

    private javax.swing.JScrollPane spItems;

    private newgen.presentation.UnicodeTextField tBarcode;

    private newgen.presentation.component.DateField tDueDate;

    private javax.swing.JFormattedTextField tOrderAmt;

    private newgen.presentation.component.DateField tOrderDate;

    private newgen.presentation.component.DateField tReturnDate;

    private newgen.presentation.VKTextArea taAddress;

    private javax.swing.JTable tabItem;

    private javax.swing.JTable tableBudgets;
}
