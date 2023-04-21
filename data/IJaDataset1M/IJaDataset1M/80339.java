package newgen.presentation.circulation;

/**
 *
 * @author  vasu praveen
 */
public class ILLRequestPlacing extends javax.swing.JPanel {

    private static ILLRequestPlacing instance = null;

    private javax.swing.table.DefaultTableModel defTbModel = null;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private newgen.presentation.component.Utility utility = null;

    private java.util.ResourceBundle resourceBundle = null;

    private java.util.Hashtable htLibrary = null;

    private java.util.Hashtable htOtherLibrary = null;

    private String volID = "";

    private String serID = "";

    private javax.swing.DefaultComboBoxModel defMatType = null;

    private java.util.Hashtable htBudgetDetails = null;

    /** Creates new form CheckoutToBinder */
    private ILLRequestPlacing() {
        initComponents();
        java.util.ArrayList aldat = new java.util.ArrayList();
        initComps();
        pl1.add(pl11);
        pl1.add(pl4, java.awt.BorderLayout.LINE_END);
        aldat.add(newGenMain.getLibraryID());
        this.htBudgetDetails = this.budgetPanel.getBudgetIds();
        this.budgetPanel.setData(aldat, 0.0, newGenMain.getEntryID(), null, this.htBudgetDetails);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    private void initComps() {
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        resourceBundle = newGenMain.getMyResource();
        defMatType = new javax.swing.DefaultComboBoxModel();
        cMatType.setModel(defMatType);
        cbOtherLibrary.setEnabled(false);
        cILLLibrary.setEnabled(false);
        chNetwork.setSelected(false);
        chOtherLibrary.setSelected(false);
        if (newGenMain.getPrivilege().getILLApprovalAuthority() == 'Y') budgetPanel.setEditableTrue(); else budgetPanel.setEditableFalse();
        java.util.ArrayList arrayList = new java.util.ArrayList();
        arrayList.add(newGenMain.getLibraryID());
        budgetPanel.setLibID(arrayList);
        budgetPanel.setPatLibID(newGenMain.getLibraryID());
        budgetPanel.setPatronID(newGenMain.getEntryID());
        budgetPanel.showLibraryNameInTable(false);
        htLibrary = utility.getLibraryDetails();
        htOtherLibrary = utility.getOtherLibraryDetails();
        cILLLibrary.setModel(getLibraries());
        cbOtherLibrary.setModel(getOtherLibraries());
        volumeSerialPanel.setEnabledFalse();
    }

    private void getMaterialTypes(java.util.Vector vector) {
        System.out.println("vector : " + vector);
        defMatType = new javax.swing.DefaultComboBoxModel(new java.util.TreeSet(vector).toArray());
        cMatType.setModel(defMatType);
    }

    protected void updateDatabase() {
        if (validateScreen() && volumeSerialPanel.validateScreen()) {
            if (maxillperpatron.intValue() <= 0) {
                org.jdom.Element root = new org.jdom.Element("OperationID");
                root.setAttribute("no", "1");
                utility.addLoginDetailsToTheRootElement(root);
                org.jdom.Element element = new org.jdom.Element("Network");
                if (chNetwork.isSelected()) {
                    element.setText("Y");
                }
                if (chOtherLibrary.isSelected()) {
                    element.setText("N");
                }
                root.addContent(element);
                element = new org.jdom.Element("ILLLibraryID");
                if (chNetwork.isSelected()) {
                    element.setText("" + htLibrary.get(cILLLibrary.getSelectedItem()));
                }
                if (chOtherLibrary.isSelected()) {
                    element.setText("" + htOtherLibrary.get(cbOtherLibrary.getSelectedItem()));
                }
                root.addContent(element);
                element = new org.jdom.Element("MaterialType");
                element.setText("" + cMatType.getSelectedItem());
                root.addContent(element);
                element = new org.jdom.Element("PatronID");
                element.setText(tPatronID.getText());
                root.addContent(element);
                if (chOtherLibrary.isSelected()) {
                    element = new org.jdom.Element("Title");
                    element.setText(tTitle.getText().trim());
                    root.addContent(element);
                    element = new org.jdom.Element("Author");
                    element.setText(tAuthor.getText().trim());
                    root.addContent(element);
                    element = new org.jdom.Element("Series");
                    element.setText(tSeries.getText().trim());
                    root.addContent(element);
                    element = new org.jdom.Element("Publisher");
                    element.setText(tPublisher.getText().trim());
                    root.addContent(element);
                    element = new org.jdom.Element("Edition");
                    element.setText(tEdition.getText().trim());
                    root.addContent(element);
                    element = new org.jdom.Element("ISBN");
                    element.setText(tISBN.getText().trim());
                    root.addContent(element);
                    if (volumeSerialPanel.isSerialSelected()) {
                        element = new org.jdom.Element("Serial");
                        element.setText("Y");
                        root.addContent(element);
                        element = new org.jdom.Element("SerialVolumeNumber");
                        element.setText(volumeSerialPanel.getSerialVolumeNumber());
                        root.addContent(element);
                        element = new org.jdom.Element("SerialIssueNumber");
                        element.setText(volumeSerialPanel.getSerialIssueNumber());
                        root.addContent(element);
                        element = new org.jdom.Element("SerialIssueYear");
                        element.setText(volumeSerialPanel.getSerialIssueYear());
                        root.addContent(element);
                    } else {
                        element = new org.jdom.Element("Serial");
                        element.setText("N");
                        root.addContent(element);
                        element = new org.jdom.Element("VolumeNumber");
                        element.setText(volumeSerialPanel.getVolumeNumber());
                        root.addContent(element);
                        element = new org.jdom.Element("PartSubDivision");
                        element.setText(volumeSerialPanel.getPartSubDivision());
                        root.addContent(element);
                    }
                }
                if (chNetwork.isSelected()) {
                    if (volumeSerialPanel.isSerialSelected()) {
                        element = new org.jdom.Element("Serial");
                        element.setText("Y");
                        root.addContent(element);
                        element = new org.jdom.Element("SerialID");
                        element.setText("" + serID);
                        root.addContent(element);
                    } else {
                        element = new org.jdom.Element("Serial");
                        element.setText("N");
                        root.addContent(element);
                        element = new org.jdom.Element("VolumeID");
                        element.setText("" + volID);
                        root.addContent(element);
                    }
                }
                element = new org.jdom.Element("PhotoCopy");
                if (chPhotoCopy.isSelected()) element.setText("Y"); else element.setText("N");
                root.addContent(element);
                element = new org.jdom.Element("EntryID");
                element.setText(newGenMain.getEntryID());
                root.addContent(element);
                element = new org.jdom.Element("Status");
                System.out.println("newGenMain.getPrivilege() : " + newGenMain.getPrivilege());
                System.out.println("newGenMain.getPrivilege().getILLApprovalAuthority() : " + newGenMain.getPrivilege().getILLApprovalAuthority());
                String status = "";
                if (newGenMain.getPrivilege().getILLApprovalAuthority() == 'Y') {
                    element.setText("D");
                    status = "D";
                } else {
                    element.setText("A");
                    status = "A";
                }
                root.addContent(element);
                budgetPanel.addBudgetDetailsToRootElement(root);
                String xmlStr = newGenXMLGenerator.buildXMLDocument(root);
                java.util.Hashtable htReturn = newGenXMLGenerator.parseXMLDocument(servletConnector.sendRequest("ILLRequestPlacing", xmlStr));
                if (htReturn != null && htReturn.size() > 0) {
                    if (utility.getTestedString("" + htReturn.get("Success")).equals("Y")) {
                        refreshScreen();
                        if (status.equals("D")) {
                            newgen.presentation.component.MailDetailsDialog mailDetailsDialog = new newgen.presentation.component.MailDetailsDialog(javax.swing.JOptionPane.getFrameForComponent(newGenMain), true, newgen.presentation.component.NewGenConstants.SINGLE_DISPATCH_MODE);
                            String email = "N";
                            String print = "N";
                            if (htReturn.get("EMailDispatched").equals("Y")) email = "Y";
                            if (htReturn.get("PrintJobAdded").equals("Y")) print = "Y";
                            mailDetailsDialog.setMailDispatchDetails(htReturn.get("MailRecipent").toString(), email, print, "ILL request sent to : ");
                            refreshScreen();
                            mailDetailsDialog.show();
                            int printCopies = new Integer(htReturn.get("PrintCopies").toString()).intValue();
                            if (printCopies > 0) {
                                String formid = utility.getTestedString(htReturn.get("FormID"));
                                if (!formid.equals("") && !formid.equals("0")) {
                                    String[] formId = new String[1];
                                    formId[0] = htReturn.get("FormID").toString();
                                    newgen.presentation.component.PrintComponentDialog.getInstance().setData(new Integer(newGenMain.getLibraryID()), formId);
                                    newgen.presentation.component.PrintComponentDialog.getInstance().setModal(true);
                                    newgen.presentation.component.PrintComponentDialog.getInstance().show();
                                }
                            }
                        } else {
                            newGenMain.showInformationMessage(newGenMain.getMyResource().getString("ILLRequestsentforapproval"));
                        }
                    } else {
                        newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Problemoccuredwhilepostingtodatabase"));
                    }
                } else {
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Problemoccuredwhilepostingtodatabase"));
                }
            } else {
                if (pendingrequests.intValue() <= maxillperpatron.intValue()) {
                    org.jdom.Element root = new org.jdom.Element("OperationID");
                    root.setAttribute("no", "1");
                    utility.addLoginDetailsToTheRootElement(root);
                    org.jdom.Element element = new org.jdom.Element("Network");
                    if (chNetwork.isSelected()) {
                        element.setText("Y");
                    }
                    if (chOtherLibrary.isSelected()) {
                        element.setText("N");
                    }
                    root.addContent(element);
                    element = new org.jdom.Element("ILLLibraryID");
                    if (chNetwork.isSelected()) {
                        element.setText("" + htLibrary.get(cILLLibrary.getSelectedItem()));
                    }
                    if (chOtherLibrary.isSelected()) {
                        element.setText("" + htOtherLibrary.get(cbOtherLibrary.getSelectedItem()));
                    }
                    root.addContent(element);
                    element = new org.jdom.Element("MaterialType");
                    element.setText("" + cMatType.getSelectedItem());
                    root.addContent(element);
                    element = new org.jdom.Element("PatronID");
                    element.setText(tPatronID.getText());
                    root.addContent(element);
                    if (chOtherLibrary.isSelected()) {
                        element = new org.jdom.Element("Title");
                        element.setText(tTitle.getText().trim());
                        root.addContent(element);
                        element = new org.jdom.Element("Author");
                        element.setText(tAuthor.getText().trim());
                        root.addContent(element);
                        element = new org.jdom.Element("Series");
                        element.setText(tSeries.getText().trim());
                        root.addContent(element);
                        element = new org.jdom.Element("Publisher");
                        element.setText(tPublisher.getText().trim());
                        root.addContent(element);
                        element = new org.jdom.Element("Edition");
                        element.setText(tEdition.getText().trim());
                        root.addContent(element);
                        element = new org.jdom.Element("ISBN");
                        element.setText(tISBN.getText().trim());
                        root.addContent(element);
                        if (volumeSerialPanel.isSerialSelected()) {
                            element = new org.jdom.Element("Serial");
                            element.setText("Y");
                            root.addContent(element);
                            element = new org.jdom.Element("SerialVolumeNumber");
                            element.setText(volumeSerialPanel.getSerialVolumeNumber());
                            root.addContent(element);
                            element = new org.jdom.Element("SerialIssueNumber");
                            element.setText(volumeSerialPanel.getSerialIssueNumber());
                            root.addContent(element);
                            element = new org.jdom.Element("SerialIssueYear");
                            element.setText(volumeSerialPanel.getSerialIssueYear());
                            root.addContent(element);
                        } else {
                            element = new org.jdom.Element("Serial");
                            element.setText("N");
                            root.addContent(element);
                            element = new org.jdom.Element("VolumeNumber");
                            element.setText(volumeSerialPanel.getVolumeNumber());
                            root.addContent(element);
                            element = new org.jdom.Element("PartSubDivision");
                            element.setText(volumeSerialPanel.getPartSubDivision());
                            root.addContent(element);
                        }
                    }
                    if (chNetwork.isSelected()) {
                        if (volumeSerialPanel.isSerialSelected()) {
                            element = new org.jdom.Element("Serial");
                            element.setText("Y");
                            root.addContent(element);
                            element = new org.jdom.Element("SerialID");
                            element.setText("" + serID);
                            root.addContent(element);
                        } else {
                            element = new org.jdom.Element("Serial");
                            element.setText("N");
                            root.addContent(element);
                            element = new org.jdom.Element("VolumeID");
                            element.setText("" + volID);
                            root.addContent(element);
                        }
                    }
                    element = new org.jdom.Element("PhotoCopy");
                    if (chPhotoCopy.isSelected()) element.setText("Y"); else element.setText("N");
                    root.addContent(element);
                    element = new org.jdom.Element("EntryID");
                    element.setText(newGenMain.getEntryID());
                    root.addContent(element);
                    element = new org.jdom.Element("Status");
                    System.out.println("newGenMain.getPrivilege() : " + newGenMain.getPrivilege());
                    System.out.println("newGenMain.getPrivilege().getILLApprovalAuthority() : " + newGenMain.getPrivilege().getILLApprovalAuthority());
                    String status = "";
                    if (newGenMain.getPrivilege().getILLApprovalAuthority() == 'Y') {
                        element.setText("D");
                        status = "D";
                    } else {
                        element.setText("A");
                        status = "A";
                    }
                    root.addContent(element);
                    budgetPanel.addBudgetDetailsToRootElement(root);
                    String xmlStr = newGenXMLGenerator.buildXMLDocument(root);
                    java.util.Hashtable htReturn = newGenXMLGenerator.parseXMLDocument(servletConnector.sendRequest("ILLRequestPlacing", xmlStr));
                    if (htReturn != null && htReturn.size() > 0) {
                        if (utility.getTestedString("" + htReturn.get("Success")).equals("Y")) {
                            refreshScreen();
                            if (status.equals("D")) {
                                newgen.presentation.component.MailDetailsDialog mailDetailsDialog = new newgen.presentation.component.MailDetailsDialog(javax.swing.JOptionPane.getFrameForComponent(newGenMain), true, newgen.presentation.component.NewGenConstants.SINGLE_DISPATCH_MODE);
                                String email = "N";
                                String print = "N";
                                if (htReturn.get("EMailDispatched").equals("Y")) email = "Y";
                                if (htReturn.get("PrintJobAdded").equals("Y")) print = "Y";
                                mailDetailsDialog.setMailDispatchDetails(htReturn.get("MailRecipent").toString(), email, print, "ILL request sent to : ");
                                refreshScreen();
                                mailDetailsDialog.show();
                                int printCopies = new Integer(htReturn.get("PrintCopies").toString()).intValue();
                                if (printCopies > 0) {
                                    String formid = utility.getTestedString(htReturn.get("FormID"));
                                    if (!formid.equals("") && !formid.equals("0")) {
                                        String[] formId = new String[1];
                                        formId[0] = htReturn.get("FormID").toString();
                                        newgen.presentation.component.PrintComponentDialog.getInstance().setData(new Integer(newGenMain.getLibraryID()), formId);
                                        newgen.presentation.component.PrintComponentDialog.getInstance().setModal(true);
                                        newgen.presentation.component.PrintComponentDialog.getInstance().show();
                                    }
                                }
                            } else {
                                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("ILLRequestsentforapproval"));
                            }
                        } else {
                            newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Problemoccuredwhilepostingtodatabase"));
                        }
                    } else {
                        newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Problemoccuredwhilepostingtodatabase"));
                    }
                } else {
                    newGenMain.showInformationMessage(resourceBundle.getString("LimitReachedOnMaximamNumberofIlls"));
                }
            }
        }
    }

    private boolean validateScreen() {
        if (tName.getText().trim().length() > 0) {
            if (chNetwork.isSelected()) {
                if (cILLLibrary.getSelectedIndex() > -1 && (newGenMain.getLibraryID() != newGenMain.getLibraryId(cILLLibrary.getSelectedItem().toString()))) {
                    if (tTitle.getText().trim().length() > 0 && tAuthor.getText().trim().length() > 0) {
                        return true;
                    } else {
                        if (chNetwork.isSelected()) {
                            newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Pressserachcataloguebutton"));
                            bSearchCatalogue.grabFocus();
                        } else {
                            newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Enterdocumentinformation"));
                            if (tTitle.getText().trim().length() == 0) tTitle.grabFocus(); else tAuthor.grabFocus();
                        }
                        return false;
                    }
                } else {
                    if (cILLLibrary.getSelectedIndex() == -1) {
                        if (chNetwork.isSelected()) {
                            newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Nolibrariesinnetwork,contactyoursystemadministrator"));
                        } else {
                            newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Otherlibrarieshavenotbeenconfigured"));
                        }
                    } else {
                        newGenMain.showInformationMessage(newGenMain.getMyResource().getString("ILLcannotbeplacedtosamelibrary"));
                        cILLLibrary.grabFocus();
                    }
                    return false;
                }
            } else {
                return true;
            }
        } else {
            if (tPatronID.getText().trim().length() > 0) {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("FillvalidpatronID"));
            } else {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("FillpatronID"));
            }
            tPatronID.grabFocus();
            return false;
        }
    }

    protected void refreshScreen() {
        tPatronID.setText("");
        tName.setText("");
        try {
            cILLLibrary.setSelectedIndex(0);
            cILLLibrary.setEnabled(false);
            cILLLibrary.setModel(getLibraries());
            cbOtherLibrary.setSelectedIndex(0);
            cbOtherLibrary.setModel(getOtherLibraries());
            cbOtherLibrary.setEnabled(false);
        } catch (Exception ex) {
        }
        ;
        cMatType.removeAllItems();
        ;
        tTitle.setText("");
        tAuthor.setText("");
        volumeSerialPanel.refreshScreen();
        tPublisher.setText("");
        tSeries.setText("");
        tISBN.setText("");
        tEdition.setText("");
        chPhotoCopy.setSelected(false);
        chNetwork.setSelected(false);
        chOtherLibrary.setSelected(false);
        budgetPanel.refreshPanel();
        tPatronID.grabFocus();
    }

    private void getPatronDetails() {
        java.util.Hashtable ht = utility.getPatronDetails(newGenMain.getLibraryID(), tPatronID.getText().trim());
        if (ht.get("PatronName") != null) {
            getRequestDetails();
            tName.setText("" + ht.get("PatronName"));
            bSearchCatalogue.grabFocus();
        } else {
            tName.setText("");
            newGenMain.showInformationMessage(newGenMain.getMyResource().getString("InvalidpatronID"));
        }
    }

    private void getRequestDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "2");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element patronId = new org.jdom.Element("PatronID");
        patronId.setText(tPatronID.getText().toString());
        root.addContent(patronId);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("ILLRequestPlacing", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            String maxills = utility.getTestedString(root1.getChildText("MaxIllPerPatron"));
            String pendings = utility.getTestedString(root1.getChildText("PendingRequests"));
            if (!maxills.equals("")) {
                maxillperpatron = new Integer(root1.getChildText("MaxIllPerPatron"));
            }
            if (!pendings.equals("")) {
                pendingrequests = new Integer(root1.getChildText("PendingRequests"));
            }
        } else {
            newGenMain.showErrorMessage(resourceBundle.getString("Problemoccuredwhilefetchingdata"));
        }
    }

    private javax.swing.DefaultComboBoxModel getLibraries() {
        return new javax.swing.DefaultComboBoxModel(new java.util.TreeSet(htLibrary.keySet()).toArray());
    }

    private javax.swing.DefaultComboBoxModel getOtherLibraries() {
        return new javax.swing.DefaultComboBoxModel(new java.util.TreeSet(htOtherLibrary.keySet()).toArray());
    }

    public void reloadLocales() {
        volumeSerialPanel.reloadLocales();
        budgetPanel.reloadLocales();
        lPatronID.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PatronID"));
        lName.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Patronname"));
        bGo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bSearchCatalogue.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Searchcatalogue"));
        chNetwork.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Network?"));
        lLibrary.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ILLservinglibrary"));
        chOtherLibrary.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Otherlibrary"));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("LibraryName"));
        lMatType.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Materialtype"));
        lTitle.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title"));
        lAuthor.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Author"));
        lPublisher.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publisher"));
        lSeries.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Series"));
        lEdition.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edition"));
        lISBN.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ISBN"));
        chPhotoCopy.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Photocopy?"));
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pl1 = new javax.swing.JPanel();
        pl11 = new javax.swing.JPanel();
        lPatronID = new javax.swing.JLabel();
        tPatronID = new newgen.presentation.UnicodeTextField();
        lName = new javax.swing.JLabel();
        tName = new newgen.presentation.UnicodeTextField();
        bGo = new javax.swing.JButton();
        pl4 = new javax.swing.JPanel();
        bSearchCatalogue = new javax.swing.JButton();
        pl3 = new javax.swing.JPanel();
        chNetwork = new javax.swing.JCheckBox();
        lLibrary = new javax.swing.JLabel();
        cILLLibrary = new javax.swing.JComboBox();
        chOtherLibrary = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        cbOtherLibrary = new javax.swing.JComboBox();
        pl2 = new javax.swing.JPanel();
        pl21 = new javax.swing.JPanel();
        lMatType = new javax.swing.JLabel();
        cMatType = new javax.swing.JComboBox();
        lTitle = new javax.swing.JLabel();
        tTitle = new newgen.presentation.UnicodeTextField();
        lAuthor = new javax.swing.JLabel();
        tAuthor = new newgen.presentation.UnicodeTextField();
        lPublisher = new javax.swing.JLabel();
        tPublisher = new newgen.presentation.UnicodeTextField();
        tSeries = new newgen.presentation.UnicodeTextField();
        lSeries = new javax.swing.JLabel();
        lEdition = new javax.swing.JLabel();
        tEdition = new newgen.presentation.UnicodeTextField();
        lISBN = new javax.swing.JLabel();
        tISBN = new newgen.presentation.UnicodeTextField();
        volumeSerialPanel = new newgen.presentation.component.VolumeSerialPanel();
        pl22 = new javax.swing.JPanel();
        chPhotoCopy = new javax.swing.JCheckBox();
        budgetPanel = new newgen.presentation.component.BudgetPanel();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        pl1.setLayout(new java.awt.BorderLayout());
        pl11.setLayout(new java.awt.GridBagLayout());
        pl11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pl11.setPreferredSize(new java.awt.Dimension(400, 80));
        lPatronID.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PatronID"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pl11.add(lPatronID, gridBagConstraints);
        tPatronID.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tPatronIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pl11.add(tPatronID, gridBagConstraints);
        lName.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Patronname"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        pl11.add(lName, gridBagConstraints);
        tName.setColumns(30);
        tName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        pl11.add(tName, gridBagConstraints);
        bGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bGo.setMnemonic('g');
        bGo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bGo.setPreferredSize(new java.awt.Dimension(47, 20));
        bGo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pl11.add(bGo, gridBagConstraints);
        pl1.add(pl11, java.awt.BorderLayout.CENTER);
        pl4.setLayout(new java.awt.GridBagLayout());
        pl4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bSearchCatalogue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Find16.gif")));
        bSearchCatalogue.setMnemonic('s');
        bSearchCatalogue.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Searchcatalogue"));
        bSearchCatalogue.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchCatalogue"));
        bSearchCatalogue.setMinimumSize(new java.awt.Dimension(165, 26));
        bSearchCatalogue.setPreferredSize(new java.awt.Dimension(165, 26));
        bSearchCatalogue.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSearchCatalogueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        pl4.add(bSearchCatalogue, gridBagConstraints);
        pl1.add(pl4, java.awt.BorderLayout.EAST);
        add(pl1);
        pl3.setLayout(new java.awt.GridBagLayout());
        pl3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pl3.setPreferredSize(new java.awt.Dimension(10, 70));
        chNetwork.setMnemonic('w');
        chNetwork.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Network?"));
        chNetwork.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chNetwork.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/network.gif")));
        chNetwork.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chNetworkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        pl3.add(chNetwork, gridBagConstraints);
        lLibrary.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ILLservinglibrary"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        pl3.add(lLibrary, gridBagConstraints);
        cILLLibrary.setMinimumSize(new java.awt.Dimension(167, 25));
        cILLLibrary.setPreferredSize(new java.awt.Dimension(167, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        pl3.add(cILLLibrary, gridBagConstraints);
        chOtherLibrary.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Otherlibrary"));
        chOtherLibrary.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chOtherLibraryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pl3.add(chOtherLibrary, gridBagConstraints);
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("LibraryName"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl3.add(jLabel1, gridBagConstraints);
        cbOtherLibrary.setMinimumSize(new java.awt.Dimension(167, 25));
        cbOtherLibrary.setPreferredSize(new java.awt.Dimension(167, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        pl3.add(cbOtherLibrary, gridBagConstraints);
        add(pl3);
        pl2.setLayout(new java.awt.BorderLayout());
        pl2.setPreferredSize(new java.awt.Dimension(10, 200));
        pl21.setLayout(new java.awt.GridBagLayout());
        pl21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lMatType.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Materialtype"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl21.add(lMatType, gridBagConstraints);
        cMatType.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl21.add(cMatType, gridBagConstraints);
        lTitle.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl21.add(lTitle, gridBagConstraints);
        tTitle.setColumns(25);
        tTitle.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl21.add(tTitle, gridBagConstraints);
        lAuthor.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Author"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl21.add(lAuthor, gridBagConstraints);
        tAuthor.setColumns(25);
        tAuthor.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl21.add(tAuthor, gridBagConstraints);
        lPublisher.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publisher"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl21.add(lPublisher, gridBagConstraints);
        tPublisher.setColumns(25);
        tPublisher.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl21.add(tPublisher, gridBagConstraints);
        tSeries.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl21.add(tSeries, gridBagConstraints);
        lSeries.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Series"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl21.add(lSeries, gridBagConstraints);
        lEdition.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edition"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl21.add(lEdition, gridBagConstraints);
        tEdition.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl21.add(tEdition, gridBagConstraints);
        lISBN.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ISBN"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl21.add(lISBN, gridBagConstraints);
        tISBN.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl21.add(tISBN, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        pl21.add(volumeSerialPanel, gridBagConstraints);
        pl2.add(pl21, java.awt.BorderLayout.CENTER);
        pl22.setLayout(new java.awt.GridBagLayout());
        pl22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        chPhotoCopy.setMnemonic('p');
        chPhotoCopy.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Photocopy?"));
        chPhotoCopy.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl22.add(chPhotoCopy, gridBagConstraints);
        pl2.add(pl22, java.awt.BorderLayout.EAST);
        add(pl2);
        budgetPanel.setPreferredSize(new java.awt.Dimension(467, 270));
        add(budgetPanel);
    }

    private void chOtherLibraryActionPerformed(java.awt.event.ActionEvent evt) {
        if (chOtherLibrary.isSelected()) {
            if (tPatronID.getText().trim().length() > 0) {
                if (cbOtherLibrary.getSelectedIndex() > -1) {
                    cbOtherLibrary.setEnabled(true);
                    chNetwork.setSelected(false);
                    cILLLibrary.setEnabled(false);
                    tTitle.setEditable(true);
                    tAuthor.setEditable(true);
                    tEdition.setEditable(true);
                    tISBN.setEditable(true);
                    tPublisher.setEditable(true);
                    tSeries.setEditable(true);
                    volumeSerialPanel.setEnabledTrue();
                    getMaterialTypes(newGenMain.getMaterielTypes());
                } else {
                    chOtherLibrary.setSelected(false);
                    newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("Otherlibrarieshavenotbeenconfigured"));
                }
            } else {
                chOtherLibrary.setSelected(false);
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("EnterpatronID"));
                tPatronID.grabFocus();
            }
        } else {
            cbOtherLibrary.setEnabled(false);
            tTitle.setEditable(false);
            tAuthor.setEditable(false);
            tEdition.setEditable(false);
            tISBN.setEditable(false);
            tPublisher.setEditable(false);
            tSeries.setEditable(false);
            volumeSerialPanel.setEnabledFalse();
            getMaterialTypes(vMatType);
        }
    }

    private void bGoActionPerformed(java.awt.event.ActionEvent evt) {
        if (tPatronID.getText().trim().length() > 0) {
            getPatronDetails();
        } else {
            newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("EnterpatronID"));
        }
    }

    private void bSearchCatalogueActionPerformed(java.awt.event.ActionEvent evt) {
        if (tName.getText().trim().length() > 0 && cILLLibrary.getSelectedIndex() != -1) {
            newgen.presentation.cataloguing.SearchCatalogueDialog searchCatalogue = new newgen.presentation.cataloguing.SearchCatalogueDialog(newgen.presentation.cataloguing.SearchCatalogueDialog.HOLDINGS_SELECTION_MODE, cILLLibrary.getSelectedItem().toString());
            if (searchCatalogue.getRetcode() == 0) {
                System.out.println("scd vol ser id : " + searchCatalogue.getVolumeSerialId());
                System.out.println("scd is serial : " + searchCatalogue.isSerial());
                if (searchCatalogue.isSerial()) {
                    serID = searchCatalogue.getVolumeSerialId();
                    volumeSerialPanel.setSerialSelected(true);
                } else {
                    volID = searchCatalogue.getVolumeSerialId();
                    volumeSerialPanel.setSerialSelected(false);
                }
                java.util.Hashtable htbasic = searchCatalogue.getHtbasic();
                volumeSerialPanel.setVolume(searchCatalogue.getVolumeDetails());
                tTitle.setText(htbasic.get("Title").toString());
                tAuthor.setText(htbasic.get("Author").toString());
                tPublisher.setText(htbasic.get("Publisher").toString());
                tSeries.setText(htbasic.get("Series").toString());
                tISBN.setText(htbasic.get("ISBN/ISSN").toString());
                tEdition.setText(htbasic.get("Edition").toString());
                vMatType = searchCatalogue.getVMaterialTypes();
                getMaterialTypes(vMatType);
            } else {
                return;
            }
        } else {
            if (tName.getText().trim().length() == 0) {
                if (tPatronID.getText().trim().length() > 0) {
                    newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("EntervalidpatronID"));
                    tPatronID.grabFocus();
                } else {
                    newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("EnterpatronID"));
                    tPatronID.grabFocus();
                }
            } else if (cILLLibrary.getSelectedIndex() == -1) {
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString(("Nonetwork/otherlibraries,contactyoursystemadministrator")));
            }
        }
    }

    private void bEditActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void chNetworkActionPerformed(java.awt.event.ActionEvent evt) {
        if (chNetwork.isSelected()) {
            if (tPatronID.getText().trim().length() > 0) {
                cILLLibrary.setEnabled(true);
                chOtherLibrary.setSelected(false);
                cbOtherLibrary.setEnabled(false);
            } else {
                chNetwork.setSelected(false);
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("EnterpatronID"));
                tPatronID.grabFocus();
            }
        } else {
            cILLLibrary.setEnabled(false);
        }
    }

    private void tPatronIDActionPerformed(java.awt.event.ActionEvent evt) {
        bGo.doClick();
    }

    public static ILLRequestPlacing getInstance() {
        instance = new ILLRequestPlacing();
        return instance;
    }

    private javax.swing.JButton bGo;

    private javax.swing.JButton bSearchCatalogue;

    private newgen.presentation.component.BudgetPanel budgetPanel;

    private javax.swing.JComboBox cILLLibrary;

    private javax.swing.JComboBox cMatType;

    private javax.swing.JComboBox cbOtherLibrary;

    private javax.swing.JCheckBox chNetwork;

    private javax.swing.JCheckBox chOtherLibrary;

    private javax.swing.JCheckBox chPhotoCopy;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel lAuthor;

    private javax.swing.JLabel lEdition;

    private javax.swing.JLabel lISBN;

    private javax.swing.JLabel lLibrary;

    private javax.swing.JLabel lMatType;

    private javax.swing.JLabel lName;

    private javax.swing.JLabel lPatronID;

    private javax.swing.JLabel lPublisher;

    private javax.swing.JLabel lSeries;

    private javax.swing.JLabel lTitle;

    private javax.swing.JPanel pl1;

    private javax.swing.JPanel pl11;

    private javax.swing.JPanel pl2;

    private javax.swing.JPanel pl21;

    private javax.swing.JPanel pl22;

    private javax.swing.JPanel pl3;

    private javax.swing.JPanel pl4;

    private newgen.presentation.UnicodeTextField tAuthor;

    private newgen.presentation.UnicodeTextField tEdition;

    private newgen.presentation.UnicodeTextField tISBN;

    private newgen.presentation.UnicodeTextField tName;

    private newgen.presentation.UnicodeTextField tPatronID;

    private newgen.presentation.UnicodeTextField tPublisher;

    private newgen.presentation.UnicodeTextField tSeries;

    private newgen.presentation.UnicodeTextField tTitle;

    private newgen.presentation.component.VolumeSerialPanel volumeSerialPanel;

    private java.util.Vector vMatType = new java.util.Vector(1, 1);

    Integer maxillperpatron = new Integer(0);

    Integer pendingrequests = new Integer(0);
}
