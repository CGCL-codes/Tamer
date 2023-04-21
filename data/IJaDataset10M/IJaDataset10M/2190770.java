package newgen.presentation.acquisitions.acqAdv;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class TechnicalProcessingPanel extends javax.swing.JPanel implements newgen.presentation.cataloguing.CatalogueIntegrationInterface, java.awt.event.MouseListener {

    private static final TechnicalProcessingPanel SINGLETON = new TechnicalProcessingPanel();

    public static TechnicalProcessingPanel getInstance() {
        return SINGLETON;
    }

    /** Creates new form TechnicalProcessingPanel */
    private TechnicalProcessingPanel() {
        initComponents();
        System.out.println("In tech processing");
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        this.defTbModel = new javax.swing.table.DefaultTableModel(new Object[] { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalPresentationForm"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Barcode"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AccessionNumber"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BookNumber"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ClassificationNumber"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CallNumber"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Acquisitionmode"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("RequestId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Requestedlibrary"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("VolumeId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("VolumeNo"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PartSubDivision"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Author"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edition"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Series"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ISBN"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publisher"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publicationplace"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PublicationYear"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Location"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Volume") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table1.setModel(defTbModel);
        table1.getTableHeader().setPreferredSize(new java.awt.Dimension(100, table1.getRowHeight()));
        table1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table1.getColumnModel().getColumn(0).setPreferredWidth(250);
        table1.getColumnModel().getColumn(1).setPreferredWidth(250);
        table1.getColumnModel().getColumn(2).setPreferredWidth(150);
        table1.getColumnModel().getColumn(3).setPreferredWidth(0);
        table1.getColumnModel().getColumn(3).setMinWidth(0);
        table1.getColumnModel().getColumn(3).setMaxWidth(0);
        table1.getColumnModel().getColumn(4).setPreferredWidth(0);
        table1.getColumnModel().getColumn(4).setMinWidth(0);
        table1.getColumnModel().getColumn(4).setMaxWidth(0);
        table1.getColumnModel().getColumn(5).setPreferredWidth(0);
        table1.getColumnModel().getColumn(5).setMinWidth(0);
        table1.getColumnModel().getColumn(5).setMaxWidth(0);
        table1.getColumnModel().getColumn(6).setPreferredWidth(150);
        table1.getColumnModel().getColumn(7).setPreferredWidth(150);
        table1.getColumnModel().getColumn(8).setPreferredWidth(0);
        table1.getColumnModel().getColumn(8).setMinWidth(0);
        table1.getColumnModel().getColumn(8).setMaxWidth(0);
        table1.getColumnModel().getColumn(9).setPreferredWidth(150);
        table1.getColumnModel().getColumn(10).setPreferredWidth(0);
        table1.getColumnModel().getColumn(10).setMinWidth(0);
        table1.getColumnModel().getColumn(10).setMaxWidth(0);
        table1.getColumnModel().getColumn(11).setPreferredWidth(0);
        table1.getColumnModel().getColumn(11).setMinWidth(0);
        table1.getColumnModel().getColumn(11).setMaxWidth(0);
        table1.getColumnModel().getColumn(12).setPreferredWidth(0);
        table1.getColumnModel().getColumn(12).setMinWidth(0);
        table1.getColumnModel().getColumn(12).setMaxWidth(0);
        table1.getColumnModel().getColumn(13).setPreferredWidth(0);
        table1.getColumnModel().getColumn(13).setMinWidth(0);
        table1.getColumnModel().getColumn(13).setMaxWidth(0);
        table1.getColumnModel().getColumn(14).setPreferredWidth(0);
        table1.getColumnModel().getColumn(14).setMinWidth(0);
        table1.getColumnModel().getColumn(14).setMaxWidth(0);
        table1.getColumnModel().getColumn(15).setPreferredWidth(0);
        table1.getColumnModel().getColumn(15).setMinWidth(0);
        table1.getColumnModel().getColumn(15).setMaxWidth(0);
        table1.getColumnModel().getColumn(16).setPreferredWidth(0);
        table1.getColumnModel().getColumn(16).setMinWidth(0);
        table1.getColumnModel().getColumn(16).setMaxWidth(0);
        table1.getColumnModel().getColumn(17).setPreferredWidth(0);
        table1.getColumnModel().getColumn(17).setMinWidth(0);
        table1.getColumnModel().getColumn(17).setMaxWidth(0);
        table1.getColumnModel().getColumn(18).setPreferredWidth(0);
        table1.getColumnModel().getColumn(18).setMinWidth(0);
        table1.getColumnModel().getColumn(18).setMaxWidth(0);
        table1.getColumnModel().getColumn(19).setPreferredWidth(0);
        table1.getColumnModel().getColumn(19).setMinWidth(0);
        table1.getColumnModel().getColumn(19).setMaxWidth(0);
        table1.getColumnModel().getColumn(20).setPreferredWidth(0);
        table1.getColumnModel().getColumn(20).setMinWidth(0);
        table1.getColumnModel().getColumn(20).setMaxWidth(0);
        table1.getColumnModel().getColumn(21).setPreferredWidth(150);
        table1.getTableHeader().addMouseListener(this);
        table1.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent lEvt) {
                if (!lEvt.getValueIsAdjusting()) {
                    if (table1.getSelectedRow() != -1) {
                        String holdinglibrary = defTbModel.getValueAt(table1.getSelectedRow(), 9).toString();
                        tfHoldinglibrary.setText(holdinglibrary);
                        String materialtype = defTbModel.getValueAt(table1.getSelectedRow(), 1).toString();
                        cbPresentationform.setSelectedItem(materialtype);
                        String barcode = defTbModel.getValueAt(table1.getSelectedRow(), 2).toString();
                        tfBarcode.setText(barcode);
                        String accessionNumber = defTbModel.getValueAt(table1.getSelectedRow(), 3).toString();
                        tfAccessionnumber.setText(accessionNumber);
                        String bookNo = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 4).toString());
                        tfBooknumber.setText(bookNo);
                        String classificationNo = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 5).toString());
                        tfClassfication.setText(classificationNo);
                        String callNo = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 6).toString());
                        tfCallno.setText(callNo);
                        String location = defTbModel.getValueAt(table1.getSelectedRow(), 20).toString();
                        cbLocation.setSelectedItem(location);
                    }
                }
            }
        });
        this.defTbModel1 = new javax.swing.table.DefaultTableModel(new Object[] { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("VolumeID"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("VolumeNo"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PartSubDivision") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        getAccessionDetails();
        utility.getMaterialTypes(cbPresentationform);
        getLocationDetails();
        setSize(750, 550);
        setVisible(true);
        newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().applyOrientation(this);
        table1.getTableHeader().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                System.out.println("Event Raised");
                if (e.getClickCount() == 2) {
                    System.out.println("Count is 2");
                    int column = table1.getTableHeader().columnAtPoint(e.getPoint());
                    System.out.println(column);
                    if (column == 0) {
                        System.out.println("Clicked on column 0");
                        Vector vec = defTbModel.getDataVector();
                        Collections.sort(vec, new ItemReadyForTechProcessingComparator());
                    }
                }
            }
        });
    }

    public void reloadLocales() {
        try {
            jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("HoldingsLibrary(Acquiredfor)"));
            jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Barcode"));
            jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("ClassificationNumber"));
            jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("CallNumber"));
            jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("PhysicalPresentationForm"));
            jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Location"));
            jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("AccessionNumber"));
            jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("BookNo"));
            this.defTbModel.setColumnIdentifiers(new Object[] { newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Title"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("PhysicalPresentationForm"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Barcode"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("AccessionNumber"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("BookNumber"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("ClassificationNumber"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("CallNumber"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Acquisitionmode"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("RequestId"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Requestedlibrary"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("VolumeId"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("VolumeNo"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("PartSubDivision"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Author"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Edition"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Series"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("ISBN"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Publisher"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Publicationplace"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("PublicationYear"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Location"), newgen.presentation.NewGenMain.getAppletInstance().getAppletInstance().getMyResource().getString("Volume") });
            table1.setModel(defTbModel);
            table1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            table1.getColumnModel().getColumn(0).setPreferredWidth(250);
            table1.getColumnModel().getColumn(1).setPreferredWidth(250);
            table1.getColumnModel().getColumn(2).setPreferredWidth(150);
            table1.getColumnModel().getColumn(3).setPreferredWidth(0);
            table1.getColumnModel().getColumn(3).setMinWidth(0);
            table1.getColumnModel().getColumn(3).setMaxWidth(0);
            table1.getColumnModel().getColumn(4).setPreferredWidth(0);
            table1.getColumnModel().getColumn(4).setMinWidth(0);
            table1.getColumnModel().getColumn(4).setMaxWidth(0);
            table1.getColumnModel().getColumn(5).setPreferredWidth(0);
            table1.getColumnModel().getColumn(5).setMinWidth(0);
            table1.getColumnModel().getColumn(5).setMaxWidth(0);
            table1.getColumnModel().getColumn(6).setPreferredWidth(150);
            table1.getColumnModel().getColumn(7).setPreferredWidth(150);
            table1.getColumnModel().getColumn(8).setPreferredWidth(0);
            table1.getColumnModel().getColumn(8).setMinWidth(0);
            table1.getColumnModel().getColumn(8).setMaxWidth(0);
            table1.getColumnModel().getColumn(9).setPreferredWidth(150);
            table1.getColumnModel().getColumn(10).setPreferredWidth(0);
            table1.getColumnModel().getColumn(10).setMinWidth(0);
            table1.getColumnModel().getColumn(10).setMaxWidth(0);
            table1.getColumnModel().getColumn(11).setPreferredWidth(0);
            table1.getColumnModel().getColumn(11).setMinWidth(0);
            table1.getColumnModel().getColumn(11).setMaxWidth(0);
            table1.getColumnModel().getColumn(12).setPreferredWidth(0);
            table1.getColumnModel().getColumn(12).setMinWidth(0);
            table1.getColumnModel().getColumn(12).setMaxWidth(0);
            table1.getColumnModel().getColumn(13).setPreferredWidth(0);
            table1.getColumnModel().getColumn(13).setMinWidth(0);
            table1.getColumnModel().getColumn(13).setMaxWidth(0);
            table1.getColumnModel().getColumn(14).setPreferredWidth(0);
            table1.getColumnModel().getColumn(14).setMinWidth(0);
            table1.getColumnModel().getColumn(14).setMaxWidth(0);
            table1.getColumnModel().getColumn(15).setPreferredWidth(0);
            table1.getColumnModel().getColumn(15).setMinWidth(0);
            table1.getColumnModel().getColumn(15).setMaxWidth(0);
            table1.getColumnModel().getColumn(16).setPreferredWidth(0);
            table1.getColumnModel().getColumn(16).setMinWidth(0);
            table1.getColumnModel().getColumn(16).setMaxWidth(0);
            table1.getColumnModel().getColumn(17).setPreferredWidth(0);
            table1.getColumnModel().getColumn(17).setMinWidth(0);
            table1.getColumnModel().getColumn(17).setMaxWidth(0);
            table1.getColumnModel().getColumn(18).setPreferredWidth(0);
            table1.getColumnModel().getColumn(18).setMinWidth(0);
            table1.getColumnModel().getColumn(18).setMaxWidth(0);
            table1.getColumnModel().getColumn(19).setPreferredWidth(0);
            table1.getColumnModel().getColumn(19).setMinWidth(0);
            table1.getColumnModel().getColumn(19).setMaxWidth(0);
            table1.getColumnModel().getColumn(20).setPreferredWidth(0);
            table1.getColumnModel().getColumn(20).setMinWidth(0);
            table1.getColumnModel().getColumn(20).setMaxWidth(0);
            table1.getColumnModel().getColumn(21).setPreferredWidth(150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfHoldinglibrary = new newgen.presentation.UnicodeTextField();
        jLabel2 = new javax.swing.JLabel();
        tfBarcode = new newgen.presentation.UnicodeTextField();
        jLabel3 = new javax.swing.JLabel();
        tfAccessionnumber = new newgen.presentation.UnicodeTextField();
        jLabel5 = new javax.swing.JLabel();
        tfClassfication = new newgen.presentation.UnicodeTextField();
        jLabel6 = new javax.swing.JLabel();
        tfBooknumber = new newgen.presentation.UnicodeTextField();
        jLabel7 = new javax.swing.JLabel();
        tfCallno = new newgen.presentation.UnicodeTextField();
        jLabel8 = new javax.swing.JLabel();
        cbPresentationform = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cbLocation = new javax.swing.JComboBox();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setLayout(new java.awt.BorderLayout());
        table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        table1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(table1);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel1);
        jPanel3.setLayout(new java.awt.GridBagLayout());
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setForeground(new java.awt.Color(170, 0, 0));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("HoldingsLibrary(Acquiredfor)"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel1, gridBagConstraints);
        tfHoldinglibrary.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(tfHoldinglibrary, gridBagConstraints);
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Barcode"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel2, gridBagConstraints);
        tfBarcode.setEditable(false);
        tfBarcode.setMinimumSize(new java.awt.Dimension(169, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(tfBarcode, gridBagConstraints);
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AccessionNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel3, gridBagConstraints);
        tfAccessionnumber.setEditable(false);
        tfAccessionnumber.setMinimumSize(new java.awt.Dimension(169, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(tfAccessionnumber, gridBagConstraints);
        jLabel5.setForeground(new java.awt.Color(170, 0, 0));
        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ClassificationNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel5, gridBagConstraints);
        tfClassfication.setMinimumSize(new java.awt.Dimension(169, 19));
        tfClassfication.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfClassficationCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(tfClassfication, gridBagConstraints);
        jLabel6.setForeground(new java.awt.Color(170, 0, 0));
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BookNo"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel6, gridBagConstraints);
        tfBooknumber.setMinimumSize(new java.awt.Dimension(169, 19));
        tfBooknumber.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfBooknumberCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(tfBooknumber, gridBagConstraints);
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CallNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel7, gridBagConstraints);
        tfCallno.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(tfCallno, gridBagConstraints);
        jLabel8.setForeground(new java.awt.Color(170, 0, 0));
        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalPresentationForm"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel8, gridBagConstraints);
        cbPresentationform.setPreferredSize(new java.awt.Dimension(167, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(cbPresentationform, gridBagConstraints);
        jLabel9.setForeground(new java.awt.Color(170, 0, 0));
        jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Location"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel9, gridBagConstraints);
        cbLocation.setPreferredSize(new java.awt.Dimension(176, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(cbLocation, gridBagConstraints);
        add(jPanel3);
    }

    private void tfBooknumberCaretUpdate(javax.swing.event.CaretEvent evt) {
        tfCallno.setText(tfClassfication.getText() + " " + tfBooknumber.getText());
    }

    private void tfClassficationCaretUpdate(javax.swing.event.CaretEvent evt) {
        tfCallno.setText(tfClassfication.getText() + " " + tfBooknumber.getText());
    }

    public void getAccessionDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "8");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("TechnicalServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            for (int i = defTbModel.getRowCount(); i > 0; i--) {
                defTbModel.removeRow(i - 1);
            }
            Object[] objAccessionDetails = root1.getChild("FirmOrder").getChildren("AccessionDetails").toArray();
            if (objAccessionDetails.length > 0) {
                for (int i = 0; i < objAccessionDetails.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) objAccessionDetails[i];
                    Object[][] row = new Object[1][1];
                    defTbModel.addRow(row);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Title")), defTbModel.getRowCount() - 1, 0);
                    defTbModel.setValueAt(utility.getTestedString(newgen.presentation.NewGenMain.getAppletInstance().getMaterialName(element.getChildText("MaterialTypeId"))), defTbModel.getRowCount() - 1, 1);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("AccessionNo")), defTbModel.getRowCount() - 1, 2);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Barcode")), defTbModel.getRowCount() - 1, 3);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("BookNo")), defTbModel.getRowCount() - 1, 4);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("ClassificationNo")), defTbModel.getRowCount() - 1, 5);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("CallNo")), defTbModel.getRowCount() - 1, 6);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("FirmOrder")), defTbModel.getRowCount() - 1, 7);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("CopyId")), defTbModel.getRowCount() - 1, 8);
                    defTbModel.setValueAt(newgen.presentation.NewGenMain.getAppletInstance().getLibraryName(element.getChildText("HoldingLibrary")), defTbModel.getRowCount() - 1, 9);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("VolumeId")), defTbModel.getRowCount() - 1, 10);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("VolumeNo")), defTbModel.getRowCount() - 1, 11);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("PartSubDivision")), defTbModel.getRowCount() - 1, 12);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Author")), defTbModel.getRowCount() - 1, 13);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Edition")), defTbModel.getRowCount() - 1, 14);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Series")), defTbModel.getRowCount() - 1, 15);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("ISBN")), defTbModel.getRowCount() - 1, 16);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Publisher")), defTbModel.getRowCount() - 1, 17);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("PublishPlace")), defTbModel.getRowCount() - 1, 18);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("PublishYear")), defTbModel.getRowCount() - 1, 19);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("LocationId")), defTbModel.getRowCount() - 1, 20);
                    String volInfo = "";
                    if (utility.getTestedString(element.getChildText("VolumeNo")).startsWith("<")) volInfo = utility.getVolumeInfoDisplay(element.getChildText("VolumeNo")); else if (!utility.getTestedString(element.getChildText("PartSubDivision")).equals("")) volInfo = "Vol." + element.getChildText("VolumeNo") + ":Part." + element.getChildText("PartSubDivision"); else if (!utility.getTestedString(element.getChildText("VolumeNo")).equals("")) volInfo = "Vol." + element.getChildText("VolumeNo");
                    defTbModel.setValueAt(volInfo, defTbModel.getRowCount() - 1, 21);
                }
            }
            Object[] solicitedGiftDetails = new Object[0];
            solicitedGiftDetails = root1.getChild("SolicitedGift").getChildren("AccessionDetails").toArray();
            if (solicitedGiftDetails.length > 0) {
                for (int i = 0; i < solicitedGiftDetails.length; i++) {
                    org.jdom.Element element1 = (org.jdom.Element) solicitedGiftDetails[i];
                    Object[][] row = new Object[1][1];
                    defTbModel.addRow(row);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("Title")), defTbModel.getRowCount() - 1, 0);
                    defTbModel.setValueAt(utility.getTestedString(newgen.presentation.NewGenMain.getAppletInstance().getMaterialName(element1.getChildText("MaterialTypeId"))), defTbModel.getRowCount() - 1, 1);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("AccessionNo")), defTbModel.getRowCount() - 1, 2);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("Barcode")), defTbModel.getRowCount() - 1, 3);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("BookNo")), defTbModel.getRowCount() - 1, 4);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("ClassificationNo")), defTbModel.getRowCount() - 1, 5);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("CallNo")), defTbModel.getRowCount() - 1, 6);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("SolicitedGift")), defTbModel.getRowCount() - 1, 7);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("RequestId")), defTbModel.getRowCount() - 1, 8);
                    defTbModel.setValueAt(newgen.presentation.NewGenMain.getAppletInstance().getLibraryName(element1.getChildText("HoldingLibrary")), defTbModel.getRowCount() - 1, 9);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("VolumeId")), defTbModel.getRowCount() - 1, 10);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("VolumeNo")), defTbModel.getRowCount() - 1, 11);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("PartSubDivision")), defTbModel.getRowCount() - 1, 12);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("Author")), defTbModel.getRowCount() - 1, 13);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("Edition")), defTbModel.getRowCount() - 1, 14);
                    defTbModel.setValueAt("", defTbModel.getRowCount() - 1, 15);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("ISBN")), defTbModel.getRowCount() - 1, 16);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("Publisher")), defTbModel.getRowCount() - 1, 17);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("PublishPlace")), defTbModel.getRowCount() - 1, 18);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("PublishYear")), defTbModel.getRowCount() - 1, 19);
                    defTbModel.setValueAt(utility.getTestedString(element1.getChildText("LocationId")), defTbModel.getRowCount() - 1, 20);
                    String volInfo = "";
                    if (utility.getTestedString(element1.getChildText("VolumeNo")).startsWith("<")) volInfo = utility.getVolumeInfoDisplay(element1.getChildText("VolumeNo")); else if (!utility.getTestedString(element1.getChildText("PartSubDivision")).equals("")) volInfo = "Vol." + element1.getChildText("VolumeNo") + ":Part." + element1.getChildText("PartSubDivision"); else if (!utility.getTestedString(element1.getChildText("VolumeNo")).equals("")) volInfo = "Vol." + element1.getChildText("VolumeNo");
                    defTbModel.setValueAt(volInfo, defTbModel.getRowCount() - 1, 21);
                }
            }
            Object[] unsolicitedGiftDetails = new Object[0];
            unsolicitedGiftDetails = root1.getChild("UnsolicitedGift").getChildren("AccessionDetails").toArray();
            if (unsolicitedGiftDetails.length > 0) {
                for (int i = 0; i < unsolicitedGiftDetails.length; i++) {
                    org.jdom.Element element2 = (org.jdom.Element) unsolicitedGiftDetails[i];
                    Object[][] row = new Object[1][1];
                    defTbModel.addRow(row);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("Title")), defTbModel.getRowCount() - 1, 0);
                    defTbModel.setValueAt(utility.getTestedString(newgen.presentation.NewGenMain.getAppletInstance().getMaterialName(element2.getChildText("MaterialTypeId"))), defTbModel.getRowCount() - 1, 1);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("AccessionNo")), defTbModel.getRowCount() - 1, 2);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("Barcode")), defTbModel.getRowCount() - 1, 3);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("BookNo")), defTbModel.getRowCount() - 1, 4);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("ClassificationNo")), defTbModel.getRowCount() - 1, 5);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("CallNo")), defTbModel.getRowCount() - 1, 6);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("UnsolicitedGift")), defTbModel.getRowCount() - 1, 7);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("RequestId")), defTbModel.getRowCount() - 1, 8);
                    defTbModel.setValueAt(newgen.presentation.NewGenMain.getAppletInstance().getLibraryName(element2.getChildText("HoldingLibrary")), defTbModel.getRowCount() - 1, 9);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("VolumeId")), defTbModel.getRowCount() - 1, 10);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("VolumeNo")), defTbModel.getRowCount() - 1, 11);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("PartSubDivision")), defTbModel.getRowCount() - 1, 12);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("Author")), defTbModel.getRowCount() - 1, 13);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("Edition")), defTbModel.getRowCount() - 1, 14);
                    defTbModel.setValueAt("", defTbModel.getRowCount() - 1, 15);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("ISBN")), defTbModel.getRowCount() - 1, 16);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("Publisher")), defTbModel.getRowCount() - 1, 17);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("PublishPlace")), defTbModel.getRowCount() - 1, 18);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("PublishYear")), defTbModel.getRowCount() - 1, 19);
                    defTbModel.setValueAt(utility.getTestedString(element2.getChildText("LocationId")), defTbModel.getRowCount() - 1, 20);
                    String volInfo = "";
                    if (utility.getTestedString(element2.getChildText("VolumeNo")).startsWith("<")) volInfo = utility.getVolumeInfoDisplay(element2.getChildText("VolumeNo")); else if (!utility.getTestedString(element2.getChildText("PartSubDivision")).equals("")) volInfo = "Vol." + element2.getChildText("VolumeNo") + ":Part." + element2.getChildText("PartSubDivision"); else if (!utility.getTestedString(element2.getChildText("VolumeNo")).equals("")) volInfo = "Vol." + element2.getChildText("VolumeNo");
                    defTbModel.setValueAt(volInfo, defTbModel.getRowCount() - 1, 21);
                }
            }
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Problemoccuredwhileretrievingdata"));
        }
    }

    public void getCatalogueDetails() {
        if (validateScreen()) {
            volumeID = "";
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("The system will now search the catalogue.\nIf the system is unable to find any matches you may rephrase your search.\nIn the search catalogue click on CANCEL if no appropriate matches are found this will initiate cataloguerecord creation");
            String titlesearch = "";
            String searchtitle = defTbModel.getValueAt(table1.getSelectedRow(), 0).toString();
            if (searchtitle.equals("")) titlesearch = ""; else {
                java.util.StringTokenizer stk = new java.util.StringTokenizer(searchtitle, "/");
                titlesearch = stk.nextToken();
            }
            searchCatalogue = new newgen.presentation.cataloguing.SearchCatalogueDialog(newgen.presentation.cataloguing.SearchCatalogueDialog.CATLOGUE_RECORD_SELECTION_MODE, titlesearch, "");
            if (searchCatalogue.getRetcode() == 0) {
                String[] catRecKey = searchCatalogue.getCataloguerecordkey();
                catRecId = catRecKey[0];
                ownLibId = catRecKey[1];
                setVolumeId();
                if (!volumeID.equals("")) modifyDetails();
                bnRefreshActionPerformed();
            } else {
                searchCatalogue = null;
                String title = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 0));
                String author = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 13));
                String placeofpublication = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 18));
                ;
                String publishyear = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 19));
                ;
                String publisher = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 17));
                String series = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 15));
                String edition = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 14));
                String isbn = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 16));
                newgenlib.marccomponent.marcmodel.CatalogMaterialDescription cmd = newgen.presentation.component.Utility.getInstance().getCatalogMaterialDescription(title, author, edition, series, isbn, publishyear, publisher, placeofpublication);
                newgen.presentation.cataloguing.MaterialTypeBibliographicLevel mbl = new newgen.presentation.cataloguing.MaterialTypeBibliographicLevel();
                mbl.setMode(newgen.presentation.cataloguing.MaterialTypeBibliographicLevel.MONOGRAPH_MODE);
                mbl.show();
                if (mbl.getReturnCode() == 0) {
                    cmd.setFixedField(mbl.getFfd());
                    newgen.presentation.cataloguing.MarcNonMarcDecision mnmd = newgen.presentation.cataloguing.MarcNonMarcDecision.getInstance();
                    mnmd.setCatalogueMaterialDescription(cmd);
                    mnmd.setMode(2);
                    mnmd.setFromInstance(this);
                    mnmd.show();
                }
            }
        }
    }

    public boolean validateScreen() {
        boolean valid = false;
        if (table1.getSelectedRow() == -1) {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Selectarecordfromtable"));
            table1.grabFocus();
        } else if (tfClassfication.getText().equals("")) {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("Please enter classification number");
            tfClassfication.grabFocus();
        } else if (tfBooknumber.getText().equals("")) {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("Please enter book number");
            tfBooknumber.grabFocus();
        } else valid = true;
        return valid;
    }

    public void setVolumeId() {
        java.util.Hashtable ht = new java.util.Hashtable(1, 1);
        ht.put("EntryId", newgen.presentation.NewGenMain.getAppletInstance().getEntryID());
        ht.put("EntryLibId", newgen.presentation.NewGenMain.getAppletInstance().getLibraryID());
        ht.put("CatRecId", catRecId);
        ht.put("OwnLibId", ownLibId);
        Object volNo = defTbModel.getValueAt(table1.getSelectedRow(), 11);
        Object part = defTbModel.getValueAt(table1.getSelectedRow(), 12);
        ht.put("VolNo", volNo);
        ht.put("PartSubDiv", part);
        String xmlStr = servletConnector.sendRequest("TechnicalServlet", newGenXMLGenerator.buildXMLDocument("10", ht));
        org.jdom.Element root = newGenXMLGenerator.getRootElement(xmlStr);
        int count = root.getChildren("VolId").size();
        if (count == 1) volumeID = root.getChildText("VolId"); else {
            EnumChronoDialog enumChronoDialog = new EnumChronoDialog(true, catRecId, ownLibId, tfHoldinglibrary.getText(), volNo, part);
            volumeID = enumChronoDialog.getVolId();
        }
    }

    public void modifyDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "9");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element copyId = new org.jdom.Element("CopyId");
        copyId.setText(utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 8)));
        root.addContent(copyId);
        org.jdom.Element volumeId = new org.jdom.Element("VolumeId");
        volumeId.setText(utility.getTestedString(volumeID));
        root.addContent(volumeId);
        org.jdom.Element classificationNo = new org.jdom.Element("ClassificationNo");
        classificationNo.setText(tfClassfication.getText().trim());
        root.addContent(classificationNo);
        org.jdom.Element bookNo = new org.jdom.Element("BookNo");
        bookNo.setText(tfBooknumber.getText().trim());
        root.addContent(bookNo);
        org.jdom.Element physicalForm = new org.jdom.Element("PhysicalForm");
        physicalForm.setText(cbPresentationform.getSelectedItem().toString());
        root.addContent(physicalForm);
        org.jdom.Element location = new org.jdom.Element("Location");
        location.setText(cbLocation.getSelectedItem().toString());
        root.addContent(location);
        org.jdom.Element acqMode = new org.jdom.Element("AcqMode");
        acqMode.setText(utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 7)));
        root.addContent(acqMode);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("TechnicalServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1.getChildText("Success").equals("Y")) {
            bnRefreshActionPerformed();
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TaskSuccessful"));
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TransactionforItemreadyforCataloguingfailed"));
        }
    }

    public void getLocationDetails() {
        cbLocation.removeAllItems();
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryID", newgen.presentation.NewGenMain.getAppletInstance().getLibraryID());
        org.jdom.Element root = newGenXMLGenerator.getRootElement(servletConnector.sendRequest("TechnicalServlet", newGenXMLGenerator.buildXMLDocument("3", ht)));
        if (root != null) {
            java.util.ArrayList arrLocation = new java.util.ArrayList();
            Object[] object = new Object[0];
            try {
                object = root.getChildren("LocationDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (object.length > 0) {
                for (int i = 0; i < object.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) object[i];
                    arrLocation.add(element.getChildText("Location").toString());
                }
                java.util.Collections.sort(arrLocation);
                Object[] objectLocation = arrLocation.toArray();
                for (int k = 0; k < objectLocation.length; k++) {
                    cbLocation.addItem(objectLocation[k]);
                }
            } else {
                newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoLocationRecords"));
            }
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Problemoccuredwhilepostingdata"));
        }
    }

    public void loadAllLibrariesLocations() {
        cbLocation.removeAllItems();
        java.util.Hashtable ht = new java.util.Hashtable();
        String xmlres = servletConnector.sendRequest("AccessioningServlet", newGenXMLGenerator.buildXMLDocument("7", ht));
        org.jdom.Element rootele = newGenXMLGenerator.getRootElement(xmlres);
        htLocations = new java.util.Hashtable();
        java.util.List liLocations = rootele.getChildren("Location");
        for (int i = 0; i < liLocations.size(); i++) {
            org.jdom.Element eleli = (org.jdom.Element) liLocations.get(i);
            String locid = eleli.getChildTextTrim("LocationId");
            String libid = eleli.getChildTextTrim("LibraryId");
            String locname = eleli.getChildTextTrim("LocationName");
            cbLocation.addItem(locname);
            if (htLocations.get(libid) == null) {
                java.util.Hashtable htloc = new java.util.Hashtable();
                htloc.put(locid, locname);
                htLocations.put(libid, htloc);
            } else {
                java.util.Hashtable htloc = (java.util.Hashtable) htLocations.get(libid);
                if (htloc.get(locid) == null) {
                    htloc.put(locid, locname);
                } else {
                }
            }
        }
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {
    }

    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    public void bnRefreshActionPerformed() {
        getAccessionDetails();
        tfHoldinglibrary.setText("");
        tfBarcode.setText("");
        tfAccessionnumber.setText("");
        tfClassfication.setText("");
        tfBooknumber.setText("");
        tfCallno.setText("");
        utility.getMaterialTypes(cbPresentationform);
        getLocationDetails();
    }

    public void clearSelection() {
        table1.clearSelection();
        tfHoldinglibrary.setText("");
        tfBarcode.setText("");
        tfAccessionnumber.setText("");
        tfClassfication.setText("");
        tfBooknumber.setText("");
        tfCallno.setText("");
        cbPresentationform.setSelectedIndex(0);
        cbLocation.setSelectedIndex(0);
    }

    public void bnReloadLocationsActionPerformed() {
        loadAllLibrariesLocations();
    }

    public newgenlib.marccomponent.marcmodel.CatalogMaterialDescription catalogueRecordDescription() {
        return new newgenlib.marccomponent.marcmodel.CatalogMaterialDescription();
    }

    public void refreshDetails() {
        bnRefreshActionPerformed();
    }

    public void setAllVolumes(java.util.Hashtable htVolumes) {
    }

    public void setCatalogueRecord(String catalogueRecordId, String ownerLibraryId) {
        catRecId = catalogueRecordId;
        ownLibId = ownerLibraryId;
        setVolumeId();
        if (!volumeID.equals("")) modifyDetails();
        bnRefreshActionPerformed();
    }

    public void setClassificationNumbersHT(java.util.Hashtable htClassification) {
    }

    public void setVolumeInformation(String volumeId) {
    }

    private javax.swing.JComboBox cbLocation;

    private javax.swing.JComboBox cbPresentationform;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable table1;

    private newgen.presentation.UnicodeTextField tfAccessionnumber;

    private newgen.presentation.UnicodeTextField tfBarcode;

    private newgen.presentation.UnicodeTextField tfBooknumber;

    private newgen.presentation.UnicodeTextField tfCallno;

    private newgen.presentation.UnicodeTextField tfClassfication;

    private newgen.presentation.UnicodeTextField tfHoldinglibrary;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.Utility utility = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private javax.swing.table.DefaultTableModel defTbModel = null;

    private javax.swing.table.DefaultTableModel defTbModel1 = null;

    private java.util.Hashtable htLocations = null;

    private newgen.presentation.cataloguing.SearchCatalogueDialog searchCatalogue = null;

    private String volumeID = "", catRecId = "", ownLibId = "";
}

class ItemReadyForTechProcessingComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Vector v1 = (Vector) o1;
        Vector v2 = (Vector) o2;
        return v1.elementAt(0).toString().compareTo(v2.elementAt(0).toString());
    }
}
