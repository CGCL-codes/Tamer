package newgen.presentation.sm;

/**
 *
 * @author  Administrator
 */
public class ViewLogicalListDailog extends javax.swing.JDialog {

    private javax.swing.table.DefaultTableModel defTbModel = null;

    private javax.swing.table.DefaultTableModel defTbModel1 = null;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private newgen.presentation.component.Utility utility = null;

    private java.util.ResourceBundle resourceBundle = null;

    /** Creates new form ViewLogicalListDailog */
    public ViewLogicalListDailog() {
        initComponents();
        this.setSize(400, 350);
        this.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(400, 350));
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        resourceBundle = newGenMain.getMyResource();
        this.defTbModel = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("LibraryName") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table1.setModel(defTbModel);
        table1.getColumnModel().getColumn(0).setPreferredWidth(120);
        table1.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table1.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent lEvt) {
                if (!lEvt.getValueIsAdjusting()) {
                    if (table1.getSelectedRow() != -1) {
                        getsubscriptionDetails();
                    }
                }
            }
        });
        this.defTbModel1 = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("SubscriptionId"), resourceBundle.getString("SubscriptionLibraryId"), resourceBundle.getString("RegId"), resourceBundle.getString("Title"), resourceBundle.getString("Publisher"), resourceBundle.getString("ISSN"), resourceBundle.getString("VolumeNo"), resourceBundle.getString("LibraryName"), resourceBundle.getString("LogicalListId") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table2.setModel(defTbModel1);
        table2.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table2.getColumnModel().getColumn(0).setMinWidth(0);
        table2.getColumnModel().getColumn(0).setMaxWidth(0);
        table2.getColumnModel().getColumn(0).setPreferredWidth(0);
        table2.getColumnModel().getColumn(1).setMinWidth(0);
        table2.getColumnModel().getColumn(1).setMaxWidth(0);
        table2.getColumnModel().getColumn(1).setPreferredWidth(0);
        table2.getColumnModel().getColumn(2).setMinWidth(0);
        table2.getColumnModel().getColumn(2).setMaxWidth(0);
        table2.getColumnModel().getColumn(2).setPreferredWidth(0);
        table2.getColumnModel().getColumn(7).setMinWidth(0);
        table2.getColumnModel().getColumn(7).setMaxWidth(0);
        table2.getColumnModel().getColumn(7).setPreferredWidth(0);
        table2.getColumnModel().getColumn(8).setMinWidth(0);
        table2.getColumnModel().getColumn(8).setMaxWidth(0);
        table2.getColumnModel().getColumn(8).setPreferredWidth(0);
        setInitialData();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    public void setInitialData() {
        String xmlStr = "";
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "1");
        org.jdom.Element libraryid = new org.jdom.Element("LibraryID");
        libraryid.setText(utility.getTestedString(newGenMain.getLibraryID()));
        root.addContent(libraryid);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("PreparePhysicalListServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            Object[] object = new Object[0];
            try {
                object = root1.getChildren("SubscriptionDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (object.length > 0) {
                    for (int i = 0; i < object.length; i++) {
                        org.jdom.Element element = (org.jdom.Element) object[i];
                        Object[][] row = new Object[1][1];
                        defTbModel.addRow(row);
                        defTbModel.setValueAt(utility.getTestedString(newGenMain.getLibraryName(element.getChildText("LibraryName"))), defTbModel.getRowCount() - 1, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
    }

    public void getsubscriptionDetails() {
        String xmlStr = "";
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "2");
        org.jdom.Element libraryid = new org.jdom.Element("LibraryId");
        libraryid.setText(newGenMain.getLibraryId(defTbModel.getValueAt(table1.getSelectedRow(), 0).toString()));
        root.addContent(libraryid);
        org.jdom.Element sublibraryid = new org.jdom.Element("SubScriptionLibraryId");
        sublibraryid.setText(newGenMain.getLibraryID());
        root.addContent(sublibraryid);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("PreparePhysicalListServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            Object[] object = new Object[0];
            try {
                object = root1.getChildren("SubscriptionDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (object.length > 0) {
                    for (int i = 0; i < object.length; i++) {
                        org.jdom.Element element = (org.jdom.Element) object[i];
                        Object[][] row = new Object[1][1];
                        defTbModel1.addRow(row);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubScriptionId")), defTbModel1.getRowCount() - 1, 0);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubScriptionLibraryId")), defTbModel1.getRowCount() - 1, 1);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("RegId")), defTbModel1.getRowCount() - 1, 2);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("Title")), defTbModel1.getRowCount() - 1, 3);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("Publisher")), defTbModel1.getRowCount() - 1, 4);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("ISSN")), defTbModel1.getRowCount() - 1, 5);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("VolumeNo")), defTbModel1.getRowCount() - 1, 6);
                        defTbModel1.setValueAt(utility.getTestedString(newGenMain.getLibraryName(element.getChildText("LibraryId"))), defTbModel1.getRowCount() - 1, 7);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("LogicalListId")), defTbModel1.getRowCount() - 1, 8);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        bnOk = new javax.swing.JButton();
        bnHelp = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));
        setTitle("LogicalListPreparedSubscriptions");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jScrollPane1.setViewportView(table1);
        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel1.add(jPanel3);
        jPanel4.setLayout(new java.awt.BorderLayout());
        table2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jScrollPane2.setViewportView(table2);
        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jPanel1.add(jPanel4);
        getContentPane().add(jPanel1);
        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        bnOk.setMnemonic('o');
        bnOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnOkActionPerformed(evt);
            }
        });
        jPanel2.add(bnOk);
        bnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bnHelp.setMnemonic('h');
        jPanel2.add(bnHelp);
        bnCancel.setMnemonic('c');
        bnCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(bnCancel);
        getContentPane().add(jPanel2);
        pack();
    }

    private void bnOkActionPerformed(java.awt.event.ActionEvent evt) {
        if (table2.getSelectedRow() != -1) {
            org.jdom.Element root = new org.jdom.Element("OperationId");
            root.setAttribute("no", "3");
            String subLibId = defTbModel1.getValueAt(table2.getSelectedRow(), 1).toString();
            String logical = defTbModel1.getValueAt(table2.getSelectedRow(), 8).toString();
            volume1 = defTbModel1.getValueAt(table2.getSelectedRow(), 6).toString();
            issn1 = defTbModel1.getValueAt(table2.getSelectedRow(), 5).toString();
            publisher1 = defTbModel1.getValueAt(table2.getSelectedRow(), 4).toString();
            title2 = defTbModel1.getValueAt(table2.getSelectedRow(), 3).toString();
            org.jdom.Element libraryid = new org.jdom.Element("SubscriptionLibraryId");
            libraryid.setText(subLibId);
            root.addContent(libraryid);
            org.jdom.Element subId = new org.jdom.Element("SubscriptionId");
            subId.setText(defTbModel1.getValueAt(table2.getSelectedRow(), 0).toString());
            root.addContent(subId);
            org.jdom.Element regId = new org.jdom.Element("RegId");
            regId.setText(defTbModel1.getValueAt(table2.getSelectedRow(), 2).toString());
            root.addContent(regId);
            org.jdom.Element volNo = new org.jdom.Element("VolumeNo");
            volNo.setText(defTbModel1.getValueAt(table2.getSelectedRow(), 6).toString());
            root.addContent(volNo);
            org.jdom.Element libId = new org.jdom.Element("LibraryId");
            libId.setText(newGenMain.getLibraryId(defTbModel1.getValueAt(table2.getSelectedRow(), 7).toString()));
            root.addContent(libId);
            org.jdom.Element logicid = new org.jdom.Element("LogicalListId");
            logicid.setText(logical);
            root.addContent(logicid);
            org.jdom.Document doc = new org.jdom.Document(root);
            xml = (new org.jdom.output.XMLOutputter()).outputString(doc);
            System.out.println("xml is" + xml);
            xml = newgen.presentation.component.ServletConnector.getInstance().sendRequest("PreparePhysicalListServlet", xml);
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table2.grabFocus();
        }
        this.dispose();
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnHelp;

    private javax.swing.JButton bnOk;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTable table1;

    private javax.swing.JTable table2;

    private String logicalid1 = "";

    private String libid = "";

    public String volume = "";

    public String volume1 = "";

    public String title1 = "";

    public String title2 = "";

    public String publisher1 = "";

    public String issn1 = "";

    public String xml = "";
}
