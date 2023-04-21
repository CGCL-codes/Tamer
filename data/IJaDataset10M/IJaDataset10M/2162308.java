package newgen.presentation.acquisitions;

/**
 *
 * @author  Administrator
 */
public class PaymentFunctionPanel extends javax.swing.JPanel {

    private static PaymentFunctionPanel instance = null;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.Utility utility = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private javax.swing.table.DefaultTableModel defTbModel1 = null;

    private javax.swing.table.DefaultTableModel defTbModel2 = null;

    private javax.swing.table.DefaultTableModel defTbModel3 = null;

    private java.util.ResourceBundle resourceBundle = null;

    private newgen.presentation.acquisitions.PaymentFunctionDialog paymentFunctionDialog = null;

    private boolean removeRow = false;

    public static PaymentFunctionPanel getInstance() {
        instance = new PaymentFunctionPanel();
        return instance;
    }

    /** Creates new form PaymentFunctionPanel */
    public PaymentFunctionPanel() {
        initComponents();
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        resourceBundle = newGenMain.getMyResource();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        this.defTbModel1 = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("InvoiceId"), resourceBundle.getString("InvoiceNumber"), resourceBundle.getString("InvoiceDate"), resourceBundle.getString("InvoiceAmount"), resourceBundle.getString("CurrencyCode"), resourceBundle.getString("Vendor"), resourceBundle.getString("OrderNumber"), resourceBundle.getString("OrderDate"), resourceBundle.getString("AdvancePayment") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table1.setModel(defTbModel1);
        table1.getTableHeader().setPreferredSize(new java.awt.Dimension(100, table1.getRowHeight()));
        table1.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setMaxWidth(0);
        table1.getColumnModel().getColumn(1).setPreferredWidth(150);
        table1.getColumnModel().getColumn(2).setPreferredWidth(150);
        table1.getColumnModel().getColumn(3).setPreferredWidth(0);
        table1.getColumnModel().getColumn(3).setMinWidth(0);
        table1.getColumnModel().getColumn(3).setMaxWidth(0);
        table1.getColumnModel().getColumn(4).setPreferredWidth(0);
        table1.getColumnModel().getColumn(4).setMinWidth(0);
        table1.getColumnModel().getColumn(4).setMaxWidth(0);
        table1.getColumnModel().getColumn(5).setPreferredWidth(150);
        table1.getColumnModel().getColumn(6).setPreferredWidth(150);
        table1.getColumnModel().getColumn(7).setPreferredWidth(0);
        table1.getColumnModel().getColumn(7).setMinWidth(0);
        table1.getColumnModel().getColumn(7).setMaxWidth(0);
        table1.getColumnModel().getColumn(8).setPreferredWidth(0);
        table1.getColumnModel().getColumn(8).setMinWidth(0);
        table1.getColumnModel().getColumn(8).setMaxWidth(0);
        this.defTbModel2 = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("RequestId"), resourceBundle.getString("Title/Author"), resourceBundle.getString("PhysicalPresentationForm"), resourceBundle.getString("AccessionNo"), resourceBundle.getString("HoldingLibrary"), resourceBundle.getString("OrderedNoOfCopies"), resourceBundle.getString("InvoicedNoOfCopies"), resourceBundle.getString("OrderedAmount"), resourceBundle.getString("InvoiceAmount") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table2.setModel(defTbModel2);
        table2.getTableHeader().setPreferredSize(new java.awt.Dimension(100, table2.getRowHeight()));
        table2.getColumnModel().getColumn(0).setPreferredWidth(0);
        table2.getColumnModel().getColumn(0).setMinWidth(0);
        table2.getColumnModel().getColumn(0).setMaxWidth(0);
        table2.getColumnModel().getColumn(1).setPreferredWidth(200);
        table2.getColumnModel().getColumn(2).setPreferredWidth(200);
        table2.getColumnModel().getColumn(3).setPreferredWidth(100);
        table2.getColumnModel().getColumn(4).setPreferredWidth(150);
        table2.getColumnModel().getColumn(5).setPreferredWidth(100);
        table2.getColumnModel().getColumn(6).setPreferredWidth(100);
        table2.getColumnModel().getColumn(7).setPreferredWidth(100);
        table2.getColumnModel().getColumn(8).setPreferredWidth(100);
        table2.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table2.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent lEvt) {
                if (!lEvt.getValueIsAdjusting() && !removeRow) {
                    budgetDetails();
                }
            }
        });
        this.defTbModel3 = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("BudgetId"), resourceBundle.getString("BudgetHead"), resourceBundle.getString("OrderedAmount"), resourceBundle.getString("InvoiceAmount"), resourceBundle.getString("BalanceAmount"), resourceBundle.getString("CommittedAmount"), resourceBundle.getString("ExpenditureAmount"), resourceBundle.getString("BudgetLibraryId"), resourceBundle.getString("BudgetTaId"), resourceBundle.getString("Orderedcopies"), resourceBundle.getString("Receivedcopies"), resourceBundle.getString("Price") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table3.setModel(defTbModel3);
        table3.getTableHeader().setPreferredSize(new java.awt.Dimension(100, table3.getRowHeight()));
        table3.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table3.getColumnModel().getColumn(0).setPreferredWidth(150);
        table3.getColumnModel().getColumn(1).setPreferredWidth(200);
        table3.getColumnModel().getColumn(2).setPreferredWidth(110);
        table3.getColumnModel().getColumn(3).setPreferredWidth(110);
        table3.getColumnModel().getColumn(4).setPreferredWidth(110);
        table3.getColumnModel().getColumn(5).setPreferredWidth(110);
        table3.getColumnModel().getColumn(6).setPreferredWidth(110);
        table3.getColumnModel().getColumn(7).setPreferredWidth(0);
        table3.getColumnModel().getColumn(7).setMinWidth(0);
        table3.getColumnModel().getColumn(7).setMaxWidth(0);
        table3.getColumnModel().getColumn(8).setPreferredWidth(0);
        table3.getColumnModel().getColumn(8).setMinWidth(0);
        table3.getColumnModel().getColumn(8).setMaxWidth(0);
        table3.getColumnModel().getColumn(9).setPreferredWidth(0);
        table3.getColumnModel().getColumn(9).setMinWidth(0);
        table3.getColumnModel().getColumn(9).setMaxWidth(0);
        table3.getColumnModel().getColumn(10).setPreferredWidth(0);
        table3.getColumnModel().getColumn(10).setMinWidth(0);
        table3.getColumnModel().getColumn(10).setMaxWidth(0);
        table3.getColumnModel().getColumn(11).setPreferredWidth(0);
        table3.getColumnModel().getColumn(11).setMinWidth(0);
        table3.getColumnModel().getColumn(11).setMaxWidth(0);
        tfinvoicenumber.setEditable(false);
        tfinvoicedate.setEditable(false);
        tfinvoiceamount.setEditable(false);
        tfvendor.setEditable(false);
        tforderno.setEditable(false);
        tforderdate.setEditable(false);
        tfcurrencycode.setEditable(false);
        rbInvoiceno.setSelected(true);
        rbOrderno.setSelected(false);
        rbVendor.setSelected(false);
        ((java.awt.CardLayout) this.jPanel5.getLayout()).show(jPanel5, "pInvoceNumber");
        refreshScreen();
        tfinvoiceno1.setText("");
        lAdvacepay.setVisible(false);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(jDialog1);
    }

    public void refreshScreen() {
        tfinvoicenumber.setText("");
        tfinvoicedate.setText("");
        tfinvoiceamount.setText("");
        tfvendor.setText("");
        tforderno.setText("");
        tforderdate.setText("");
        tfcurrencycode.setText("");
        lAdvacepay.setVisible(false);
        refreshScreen2();
        refreshScreen3();
    }

    public void refreshScreen1() {
        for (int i = defTbModel1.getRowCount(); i > 0; i--) {
            removeRow = true;
            defTbModel1.removeRow(i - 1);
            removeRow = false;
        }
        rbInvoiceno.setSelected(true);
        rbOrderno.setSelected(false);
        rbVendor.setSelected(false);
        ((java.awt.CardLayout) this.jPanel5.getLayout()).show(jPanel5, "pInvoceNumber");
        tfinvoiceno1.setText("");
        tforderno1.setText("");
    }

    public void refreshScreen2() {
        for (int j = defTbModel2.getRowCount(); j > 0; j--) {
            removeRow = true;
            defTbModel2.removeRow(j - 1);
            removeRow = false;
        }
    }

    public void refreshScreen3() {
        for (int k = defTbModel3.getRowCount(); k > 0; k--) {
            removeRow = true;
            defTbModel3.removeRow(k - 1);
            removeRow = false;
        }
    }

    public void refreshScreen4() {
        for (int k = defTbModel1.getRowCount(); k > 0; k--) {
            removeRow = true;
            defTbModel1.removeRow(k - 1);
            removeRow = false;
        }
    }

    private void removeSelectedRowFromTable3() {
        removeRow = true;
        defTbModel1.removeRow(table3.getSelectedRow());
        removeRow = false;
    }

    public void getDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "1");
        org.jdom.Element libraryId = new org.jdom.Element("LibraryId");
        libraryId.setText(newGenMain.getLibraryID());
        root.addContent(libraryId);
        org.jdom.Element invoiceNo = new org.jdom.Element("InvoiceNo");
        invoiceNo.setText(tfinvoiceno1.getText().trim().toString());
        root.addContent(invoiceNo);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is Client xmlStr:" + xmlStr);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            Object[] objLocal = new Object[0];
            objLocal = root1.getChildren("InvoiceDetails").toArray();
            System.out.println("This is InvoiceDetails:" + objLocal);
            if (objLocal.length > 0) {
                for (int i = 0; i < objLocal.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) objLocal[i];
                    Object[][] row = new Object[1][1];
                    defTbModel1.addRow(row);
                    defTbModel1.setValueAt(new Integer(element.getChildText("InvoiceId")), defTbModel1.getRowCount() - 1, 0);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("InvoiceNo")), defTbModel1.getRowCount() - 1, 1);
                    defTbModel1.setValueAt(utility.getDate(element.getChildText("InvoiceDate")), defTbModel1.getRowCount() - 1, 2);
                    defTbModel1.setValueAt(new Double(element.getChildText("InvoiceAmount")), defTbModel1.getRowCount() - 1, 3);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("CurrencyCode")), defTbModel1.getRowCount() - 1, 4);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("VendorName")), defTbModel1.getRowCount() - 1, 5);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("OrderId")), defTbModel1.getRowCount() - 1, 6);
                    defTbModel1.setValueAt(utility.getDate(element.getChildText("OrderDate")), defTbModel1.getRowCount() - 1, 7);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("AdvancePay")), defTbModel1.getRowCount() - 1, 8);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoInvoicenumberDetails"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretrievingdata"));
        }
    }

    public void getAccessionLibraryDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "2");
        org.jdom.Element libraryId = new org.jdom.Element("LibraryId");
        libraryId.setText(newGenMain.getLibraryID());
        root.addContent(libraryId);
        org.jdom.Element invoiceId = new org.jdom.Element("InvoiceId");
        Integer invoiceid = new Integer(defTbModel1.getValueAt(table1.getSelectedRow(), 0).toString());
        invoiceId.setText(invoiceid.toString());
        root.addContent(invoiceId);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is Client xmlStr:" + xmlStr);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        Object[] objLocal1 = new Object[0];
        objLocal1 = root1.getChildren("AccessionDetails").toArray();
        if (root1 != null) {
            if (objLocal1.length > 0) {
                for (int k = defTbModel2.getRowCount(); k > 0; k--) {
                    removeRow = true;
                    defTbModel2.removeRow(k - 1);
                    removeRow = false;
                }
                for (int i = 0; i < objLocal1.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) objLocal1[i];
                    Object[][] row = new Object[1][1];
                    defTbModel2.addRow(row);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("RequestId")), defTbModel2.getRowCount() - 1, 0);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("Title")), defTbModel2.getRowCount() - 1, 1);
                    defTbModel2.setValueAt(utility.getTestedString(newGenMain.getMaterialName(element.getChildText("MaterialTypeId"))), defTbModel2.getRowCount() - 1, 2);
                    String accessionnumber = "";
                    try {
                        accessionnumber = utility.getTestedString(element.getChildText("AccessionNo"));
                    } catch (NumberFormatException ex) {
                        System.out.println("NumberFormatException while not accessioned");
                    } catch (NullPointerException ex) {
                        System.out.println("NullPointerException while not accessioned");
                    }
                    defTbModel2.setValueAt(accessionnumber, defTbModel2.getRowCount() - 1, 3);
                    Integer holdinglibrary = new Integer(0);
                    try {
                        holdinglibrary = new Integer(element.getChildText("HoldingLibrary"));
                    } catch (NumberFormatException ex) {
                        System.out.println("NumberFormatException while not accessioned");
                    } catch (NullPointerException ex) {
                        System.out.println("NullPointerException while not accessioned");
                    }
                    if (holdinglibrary.intValue() == 0) {
                        defTbModel2.setValueAt(holdinglibrary, defTbModel2.getRowCount() - 1, 4);
                    } else {
                        defTbModel2.setValueAt(utility.getTestedString(newGenMain.getLibraryName(holdinglibrary.toString())), defTbModel2.getRowCount() - 1, 4);
                    }
                    Integer orderedcopies = new Integer(element.getChildText("OrderedCopies"));
                    defTbModel2.setValueAt(new Integer(element.getChildText("OrderedCopies")), defTbModel2.getRowCount() - 1, 5);
                    Integer receivedcopies = new Integer(element.getChildText("ReceivedCopies"));
                    defTbModel2.setValueAt(new Integer(element.getChildText("ReceivedCopies")), defTbModel2.getRowCount() - 1, 6);
                    Double price = new Double(element.getChildText("Price"));
                    double orderamt = price.doubleValue();
                    String currencycode = utility.getTestedString(element.getChildText("CurrencyCode"));
                    double price2 = new Double(orderamt).doubleValue();
                    java.util.Hashtable ht = new java.util.Hashtable();
                    ht.put("LibraryID", newGenMain.getLibraryID());
                    ht.put("CurrencyCode", currencycode);
                    ht = newGenXMLGenerator.parseXMLDocument(servletConnector.sendRequest("Utility", newGenXMLGenerator.buildXMLDocument("8", ht)));
                    double conversionRate = 0.0d;
                    try {
                        conversionRate = new Double(ht.get("ConversionRate").toString()).doubleValue();
                    } catch (NumberFormatException ex) {
                        System.out.println("NumberFormatException while calculating conversion rate");
                    }
                    double orderamt1 = new Double(price2 * conversionRate).doubleValue();
                    defTbModel2.setValueAt(new Double(orderamt1), defTbModel2.getRowCount() - 1, 7);
                    Double invoiceprice = new Double(element.getChildText("InvoiceAmount"));
                    double invoiceamt = invoiceprice.doubleValue();
                    double invoiceprice2 = new Double(invoiceamt).doubleValue();
                    defTbModel2.setValueAt(new Double(invoiceprice2), defTbModel2.getRowCount() - 1, 8);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("Oneormoreitemsnotattobereceived\nPaymentcannotbeprocessed"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretrievingdata"));
        }
    }

    public void budgetDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "5");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element requestId = new org.jdom.Element("RequestId");
        String requestid = defTbModel2.getValueAt(table2.getSelectedRow(), 0).toString();
        requestId.setText(requestid);
        root.addContent(requestId);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            Object[] objLocal = new Object[0];
            objLocal = root1.getChildren("BudgetDetails").toArray();
            if (objLocal.length > 0) {
                for (int k = defTbModel3.getRowCount(); k > 0; k--) {
                    removeRow = true;
                    defTbModel3.removeRow(k - 1);
                    removeRow = false;
                }
                for (int i = 0; i < objLocal.length; i++) {
                    int length = objLocal.length;
                    org.jdom.Element element = (org.jdom.Element) objLocal[i];
                    Object[][] row = new Object[1][1];
                    defTbModel3.addRow(row);
                    defTbModel3.setValueAt(utility.getTestedString(element.getChildText("BudgetId")), defTbModel3.getRowCount() - 1, 0);
                    defTbModel3.setValueAt(utility.getTestedString(element.getChildText("BudgetHead")), defTbModel3.getRowCount() - 1, 1);
                    String trans = utility.getTestedString(element.getChildText("TransactionAmount"));
                    if (!trans.equals("")) {
                        defTbModel3.setValueAt(new Double(element.getChildText("TransactionAmount")), defTbModel3.getRowCount() - 1, 2);
                    } else {
                        double transamt = 0.0;
                        java.math.BigDecimal price = new java.math.BigDecimal(defTbModel2.getValueAt(table2.getSelectedRow(), 7).toString());
                        double price1 = price.doubleValue();
                        defTbModel3.setValueAt(new Double(price1), defTbModel3.getRowCount() - 1, 2);
                    }
                    defTbModel3.setValueAt(new Double(element.getChildText("BalanceAmount")), defTbModel3.getRowCount() - 1, 4);
                    defTbModel3.setValueAt(new Double(element.getChildText("CommittedAmount")), defTbModel3.getRowCount() - 1, 5);
                    defTbModel3.setValueAt(new Double(element.getChildText("ExpenditureAmount")), defTbModel3.getRowCount() - 1, 6);
                    defTbModel3.setValueAt(utility.getTestedString(element.getChildText("BudgetLibraryId")), defTbModel3.getRowCount() - 1, 7);
                    defTbModel3.setValueAt(utility.getTestedString(element.getChildText("BudgetTaId")), defTbModel3.getRowCount() - 1, 8);
                    Integer orderedcopies = new Integer(defTbModel2.getValueAt(table2.getSelectedRow(), 5).toString());
                    int orderedcopies1 = orderedcopies.intValue();
                    defTbModel3.setValueAt(new Integer(orderedcopies.toString()), defTbModel3.getRowCount() - 1, 9);
                    Integer receivedcopies = new Integer(defTbModel2.getValueAt(table2.getSelectedRow(), 6).toString());
                    int receivedcopies1 = receivedcopies.intValue();
                    defTbModel3.setValueAt(new Integer(receivedcopies.toString()), defTbModel3.getRowCount() - 1, 10);
                    if (!trans.equals("")) {
                        java.math.BigDecimal price = new java.math.BigDecimal(defTbModel2.getValueAt(table2.getSelectedRow(), 7).toString());
                        double price1 = price.doubleValue();
                        defTbModel3.setValueAt(new Double(price1), defTbModel3.getRowCount() - 1, 11);
                        java.math.BigDecimal invoiceamt = new java.math.BigDecimal(defTbModel2.getValueAt(table2.getSelectedRow(), 8).toString());
                        double invoiceamount = invoiceamt.doubleValue();
                        double transamount = new Double(element.getChildText("TransactionAmount")).doubleValue();
                        double invoiceamount1 = java.lang.Math.abs((transamount / price1) * invoiceamount);
                        System.out.println("This is invoiceamount before adding table:" + invoiceamount1);
                        defTbModel3.setValueAt(new Double(invoiceamount1), defTbModel3.getRowCount() - 1, 3);
                    } else {
                        defTbModel3.setValueAt(new Double(0.0), defTbModel3.getRowCount() - 1, 11);
                        java.math.BigDecimal invoiceamt = new java.math.BigDecimal(defTbModel2.getValueAt(table2.getSelectedRow(), 8).toString());
                        double invoiceamount = invoiceamt.doubleValue();
                        double invoiceamount1 = java.lang.Math.abs(invoiceamount / (double) length);
                        defTbModel3.setValueAt(new Double(invoiceamount1), defTbModel3.getRowCount() - 1, 3);
                    }
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("Nobudgetitems"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretrievingdata"));
        }
    }

    public void getOrderNoDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "3");
        org.jdom.Element libraryId = new org.jdom.Element("LibraryId");
        libraryId.setText(newGenMain.getLibraryID());
        root.addContent(libraryId);
        org.jdom.Element orderId = new org.jdom.Element("OrderId");
        orderId.setText(tforderno1.getText().trim().toString());
        root.addContent(orderId);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is Client xmlStr:" + xmlStr);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            Object[] objLocal = new Object[0];
            objLocal = root1.getChildren("Ordernodetails").toArray();
            if (objLocal.length > 0) {
                for (int i = 0; i < objLocal.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) objLocal[i];
                    Object[][] row = new Object[1][1];
                    defTbModel1.addRow(row);
                    defTbModel1.setValueAt(new Integer(element.getChildText("InvoiceId")), defTbModel1.getRowCount() - 1, 0);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("InvoiceNo")), defTbModel1.getRowCount() - 1, 1);
                    defTbModel1.setValueAt(utility.getDate(element.getChildText("InvoiceDate")), defTbModel1.getRowCount() - 1, 2);
                    defTbModel1.setValueAt(new Double(element.getChildText("InvoiceAmount")), defTbModel1.getRowCount() - 1, 3);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("CurrencyCode")), defTbModel1.getRowCount() - 1, 4);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("VendorName")), defTbModel1.getRowCount() - 1, 5);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("OrderId")), defTbModel1.getRowCount() - 1, 6);
                    defTbModel1.setValueAt(utility.getDate(element.getChildText("OrderDate")), defTbModel1.getRowCount() - 1, 7);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("AdvancePay")), defTbModel1.getRowCount() - 1, 8);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoInvoicenumberDetails"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretrievingdata"));
        }
    }

    public void getVendorDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "4");
        org.jdom.Element libraryId = new org.jdom.Element("LibraryId");
        libraryId.setText(newGenMain.getLibraryID());
        root.addContent(libraryId);
        org.jdom.Element vendorname = new org.jdom.Element("VendorName");
        vendorname.setText(cbVendor.getSelectedItem().toString());
        root.addContent(vendorname);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is Client xmlStr:" + xmlStr);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            Object[] objLocal = new Object[0];
            objLocal = root1.getChildren("VendorDetails").toArray();
            if (objLocal.length > 0) {
                for (int i = 0; i < objLocal.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) objLocal[i];
                    Object[][] row = new Object[1][1];
                    defTbModel1.addRow(row);
                    defTbModel1.setValueAt(new Integer(element.getChildText("InvoiceId")), defTbModel1.getRowCount() - 1, 0);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("InvoiceNo")), defTbModel1.getRowCount() - 1, 1);
                    defTbModel1.setValueAt(utility.getDate(element.getChildText("InvoiceDate")), defTbModel1.getRowCount() - 1, 2);
                    defTbModel1.setValueAt(new Double(element.getChildText("InvoiceAmount")), defTbModel1.getRowCount() - 1, 3);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("CurrencyCode")), defTbModel1.getRowCount() - 1, 4);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("VendorName")), defTbModel1.getRowCount() - 1, 5);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("OrderId")), defTbModel1.getRowCount() - 1, 6);
                    defTbModel1.setValueAt(utility.getDate(element.getChildText("OrderDate")), defTbModel1.getRowCount() - 1, 7);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("AdvancePay")), defTbModel1.getRowCount() - 1, 8);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoInvoicenumberDetails"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretrievingdata"));
        }
    }

    public void budgetDetailswithaccession() {
        boolean flag = false;
        double balanceamt = 0.0;
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "6");
        utility.addLoginDetailsToTheRootElement(root);
        Integer invoiceid = new Integer(defTbModel1.getValueAt(table1.getSelectedRow(), 0).toString());
        org.jdom.Element invoiceId = new org.jdom.Element("InvoiceId");
        invoiceId.setText(invoiceid.toString());
        root.addContent(invoiceId);
        org.jdom.Element vendorname = new org.jdom.Element("Vendor");
        vendorname.setText(tfvendor.getText().trim());
        root.addContent(vendorname);
        org.jdom.Element root2 = newGenXMLGenerator.getRootElement(xmlStr1);
        Object[] objaccessionDetails = root2.getChildren("AccessionDetails").toArray();
        if (objaccessionDetails.length > 0) {
            for (int i = 0; i < objaccessionDetails.length; i++) {
                org.jdom.Element eleaccessionDetails = (org.jdom.Element) objaccessionDetails[i];
                org.jdom.Element payment = new org.jdom.Element("PaymentDetails");
                org.jdom.Element requestId = new org.jdom.Element("RequestId");
                requestId.setText(eleaccessionDetails.getChildText("RequestId"));
                payment.addContent(requestId);
                org.jdom.Element orderamount = new org.jdom.Element("OrderedAmount");
                orderamount.setText(eleaccessionDetails.getChildText("OrderedAmount"));
                payment.addContent(orderamount);
                org.jdom.Element ordercopies = new org.jdom.Element("OrderedCopies");
                ordercopies.setText(eleaccessionDetails.getChildText("OrderedCopies"));
                payment.addContent(ordercopies);
                org.jdom.Element receivecopies = new org.jdom.Element("ReceivedCopies");
                receivecopies.setText(eleaccessionDetails.getChildText("ReceivedCopies"));
                payment.addContent(receivecopies);
                org.jdom.Element budgetDetails = eleaccessionDetails.getChild("BudgetDetails");
                Object[] objbudgetIdDetails = budgetDetails.getChildren("BudgetIdDetails").toArray();
                org.jdom.Element paymentBudgetDetails = new org.jdom.Element("PaymentBudgetDetails");
                if (objbudgetIdDetails.length > 0) {
                    for (int j = 0; j < objbudgetIdDetails.length; j++) {
                        org.jdom.Element elebudgetIdDetails = (org.jdom.Element) objbudgetIdDetails[j];
                        org.jdom.Element paymentBudgetIdDetails = new org.jdom.Element("PaymentBudgetIdDetails");
                        org.jdom.Element budgetLibraryId = new org.jdom.Element("BudgetLibraryId");
                        budgetLibraryId.setText(elebudgetIdDetails.getChildText("BudgetLibraryId"));
                        paymentBudgetIdDetails.addContent(budgetLibraryId);
                        org.jdom.Element budgetTaId = new org.jdom.Element("BudgetTaId");
                        budgetTaId.setText(elebudgetIdDetails.getChildText("BudgetTaId"));
                        paymentBudgetIdDetails.addContent(budgetTaId);
                        org.jdom.Element budgetId = new org.jdom.Element("BudgetId");
                        budgetId.setText(elebudgetIdDetails.getChildText("BudgetId"));
                        paymentBudgetIdDetails.addContent(budgetId);
                        org.jdom.Element budgetHead = new org.jdom.Element("BudgetHead");
                        budgetHead.setText(elebudgetIdDetails.getChildText("BudgetHead"));
                        paymentBudgetIdDetails.addContent(budgetHead);
                        org.jdom.Element amount = new org.jdom.Element("TransactionAmount");
                        String transamt = utility.getTestedString(elebudgetIdDetails.getChildText("TransactionAmount"));
                        balanceamt = new Double(elebudgetIdDetails.getChildText("BalanceAmount")).doubleValue();
                        if (!transamt.equals("")) {
                            double transactionamt = new Double(elebudgetIdDetails.getChildText("TransactionAmount")).doubleValue();
                            amount.setText(elebudgetIdDetails.getChildText("TransactionAmount"));
                            if (transactionamt >= balanceamt) {
                                flag = true;
                                break;
                            }
                        } else {
                            amount.setText(elebudgetIdDetails.getChildText("TransactionAmount"));
                        }
                        paymentBudgetIdDetails.addContent(amount);
                        org.jdom.Element balanceAmount = new org.jdom.Element("BalanceAmount");
                        balanceAmount.setText(elebudgetIdDetails.getChildText("BalanceAmount"));
                        paymentBudgetIdDetails.addContent(balanceAmount);
                        org.jdom.Element committedAmount = new org.jdom.Element("CommittedAmount");
                        committedAmount.setText(elebudgetIdDetails.getChildText("CommittedAmount"));
                        paymentBudgetIdDetails.addContent(committedAmount);
                        org.jdom.Element expenditureAmount = new org.jdom.Element("ExpenditureAmount");
                        expenditureAmount.setText(elebudgetIdDetails.getChildText("ExpenditureAmount"));
                        paymentBudgetIdDetails.addContent(expenditureAmount);
                        double invoiceamt = new Double(elebudgetIdDetails.getChildText("InvoiceAmount")).doubleValue();
                        org.jdom.Element invoiceamount = new org.jdom.Element("InvoiceAmount");
                        invoiceamount.setText(elebudgetIdDetails.getChildText("InvoiceAmount"));
                        paymentBudgetIdDetails.addContent(invoiceamount);
                        if (invoiceamt >= balanceamt) {
                            flag = true;
                            break;
                        }
                        paymentBudgetDetails.addContent(paymentBudgetIdDetails);
                    }
                }
                payment.addContent(paymentBudgetDetails);
                root.addContent(payment);
            }
        }
        if (flag) {
            newGenMain.showInformationMessage(resourceBundle.getString("Checkthebalanceamountandorderedamount"));
        } else {
            org.jdom.Document doc = new org.jdom.Document(root);
            xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
            System.out.println("This is client xmlStr:" + xmlStr);
            xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
            org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
            String success = root1.getChildText("Success");
            if (root1 != null) {
                if (success.equals("Y")) {
                    refreshScreen();
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("TaskSuccessful"));
                    int printCopies = new Integer(root1.getChildText("PrintCopies").toString()).intValue();
                    if (printCopies > 0) {
                        String formid = utility.getTestedString(root1.getChildText("FormId"));
                        if (!formid.equals("") && !formid.equals("0")) {
                            String[] formId = new String[1];
                            formId[0] = root1.getChildText("FormId");
                            newgen.presentation.component.PrintComponentDialog.getInstance().setData(new Integer(newGenMain.getLibraryID()), formId);
                            newgen.presentation.component.PrintComponentDialog.getInstance().setModal(true);
                            newgen.presentation.component.PrintComponentDialog.getInstance().show();
                        }
                    }
                } else if (success.equals("D")) {
                    newGenMain.showInformationMessage(resourceBundle.getString("AlreadyPaymentSuccessfullyProcessed"));
                } else {
                    newGenMain.showInformationMessage(resourceBundle.getString("PaymentProcessTransactionfailed"));
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhilepostingdata"));
            }
        }
    }

    public void updateDetails() {
        if (tfinvoicenumber.getText().trim().length() > 0) {
            accessionDetails();
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("EnterInvoiceNumber"));
            bInvoiceno.doClick();
        }
    }

    public void accessionDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "7");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element root2 = newGenXMLGenerator.getRootElement(xmlStr1);
        Object[] objLocal = root2.getChildren("AccessionDetails").toArray();
        if (objLocal.length > 0) {
            for (int i = 0; i < objLocal.length; i++) {
                org.jdom.Element accession = new org.jdom.Element("Accession");
                org.jdom.Element element = (org.jdom.Element) objLocal[i];
                org.jdom.Element requestId = new org.jdom.Element("RequestId");
                requestId.setText(element.getChildText("RequestId"));
                accession.addContent(requestId);
                root.addContent(accession);
            }
        }
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        Object[] objupdateAccessionDetails = new Object[0];
        objupdateAccessionDetails = root1.getChildren("UpdateAccessionDetails").toArray();
        if (root1 != null) {
            if (objupdateAccessionDetails.length > 0) {
                String accessionnumber = "";
                boolean flag = false;
                for (int m = 0; m < objupdateAccessionDetails.length; m++) {
                    org.jdom.Element objupdateAccession = (org.jdom.Element) objupdateAccessionDetails[m];
                    try {
                        accessionnumber = utility.getTestedString(objupdateAccession.getChildText("AccessionNumber"));
                    } catch (NumberFormatException ex) {
                        System.out.println("NumberFormatException while not accessioned");
                    } catch (NullPointerException ex) {
                        System.out.println("NullPointerException while not accessioned");
                    }
                    if (accessionnumber.equals("")) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    int option = javax.swing.JOptionPane.showConfirmDialog(this, resourceBundle.getString("Accessionnotyetdonedoyouwantaccessioned"), resourceBundle.getString("Confirm"), javax.swing.JOptionPane.YES_NO_OPTION);
                    if (option == javax.swing.JOptionPane.YES_OPTION) {
                        paymentFunctionDialog = newgen.presentation.acquisitions.PaymentFunctionDialog.getInstance();
                        paymentFunctionDialog.show();
                        refreshScreen();
                        bInvoiceno.doClick();
                    } else if (option == javax.swing.JOptionPane.NO_OPTION) {
                        int option1 = javax.swing.JOptionPane.CLOSED_OPTION;
                    }
                } else {
                    String advPay = defTbModel1.getValueAt(table1.getSelectedRow(), 8).toString();
                    if (advPay.equals("Y")) {
                        reconcilationStatement();
                    } else {
                        budgetDetailswithaccession();
                    }
                }
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
    }

    public void getPaymentDetails() {
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryId", newGenMain.getLibraryID());
        Integer invoiceid = new Integer(defTbModel1.getValueAt(table1.getSelectedRow(), 0).toString());
        ht.put("InvoiceId", invoiceid);
        xmlStr1 = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", newGenXMLGenerator.buildXMLDocument("8", ht));
        System.out.println("This is Return xmlStr:" + xmlStr1);
    }

    public void formLetter() {
        String formLetter = "";
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "9");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        String sts = "";
        if (root1 != null) {
            String format = root1.getChildText("FormLetter");
            java.util.StringTokenizer stk = new java.util.StringTokenizer(format, "\n", true);
            while (stk.hasMoreTokens()) {
                String stks = stk.nextToken();
                System.out.println("" + stks);
                sts += stks + "\r";
            }
            String refno = root1.getChildText("RefNo").toString();
            String libraryID = newGenMain.getLibraryID();
            String libraryName = newGenMain.getLibraryName(libraryID);
            String laddress1 = root1.getChildText("LAddress1");
            String laddress2 = root1.getChildText("LAddress2");
            String lcity = root1.getChildText("LCity");
            String lcountry = root1.getChildText("LCountry");
            String lstate = root1.getChildText("LState");
            String lpin = root1.getChildText("LPin");
            String patronFName = root1.getChildText("PatronFName");
            String patronMName = root1.getChildText("PatronMName");
            String patronLName = root1.getChildText("PatronLName");
            String patronName = utility.getTestedString(patronFName) + " " + utility.getTestedString(patronMName) + " " + utility.getTestedString(patronLName);
            String date = tfinvoicedate.getText().trim();
            String vendor = tfvendor.getText().trim();
            String address1 = root1.getChildText("Address1");
            String address2 = root1.getChildText("Address2");
            String city = root1.getChildText("City");
            String country = root1.getChildText("Country");
            String state = root1.getChildText("State");
            String pin = root1.getChildText("Pin");
            String entryName = newGenMain.getEntryName();
            Object[] objLocal = { refno, date, patronName, address1, address2, city, state, country, pin, "Administrative Department", vendor, date, entryName, "\r\n" };
            formLetter = newgen.presentation.component.Utility.getInstance().getFormContent(sts, objLocal);
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
        newgen.presentation.component.PrintComponent.getInstance().setData(formLetter, 1, "A8");
        newgen.presentation.component.PrintComponent.getInstance().setTitle("Print no due certificate");
        newgen.presentation.component.PrintComponent.getInstance().setModal(true);
        newgen.presentation.component.PrintComponent.getInstance().show();
    }

    public void reconcilationStatement() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "10");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element invoiceId = new org.jdom.Element("InvoiceId");
        Integer invoiceid = new Integer(defTbModel1.getValueAt(table1.getSelectedRow(), 0).toString());
        invoiceId.setText(invoiceid.toString());
        root.addContent(invoiceId);
        org.jdom.Element invoiceNumber = new org.jdom.Element("InvoiceNo");
        String invoiceno = tfinvoicenumber.getText().trim();
        invoiceNumber.setText(invoiceno);
        root.addContent(invoiceNumber);
        org.jdom.Element vendorname = new org.jdom.Element("Vendor");
        vendorname.setText(tfvendor.getText().trim());
        root.addContent(vendorname);
        org.jdom.Element invoiceamount = new org.jdom.Element("InvoiceAmount");
        invoiceamount.setText(tfinvoiceamount.getText().trim());
        root.addContent(invoiceamount);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            String success = root1.getChildText("Success");
            if (success.equals("Y")) {
                refreshScreen();
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("TaskSuccessful"));
                int printCopies = new Integer(root1.getChildText("PrintCopies").toString()).intValue();
                if (printCopies > 0) {
                    String formid = utility.getTestedString(root1.getChildText("FormId"));
                    if (!formid.equals("") && !formid.equals("0")) {
                        String[] formId = new String[1];
                        formId[0] = root1.getChildText("FormId");
                        newgen.presentation.component.PrintComponentDialog.getInstance().setData(new Integer(newGenMain.getLibraryID()), formId);
                        newgen.presentation.component.PrintComponentDialog.getInstance().setModal(true);
                        newgen.presentation.component.PrintComponentDialog.getInstance().show();
                    }
                }
            } else if (success.equals("D")) {
                newGenMain.showInformationMessage(resourceBundle.getString("AlreadyAdvancePaymentProcessed"));
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("AdvancePaymentProcessTransactionfailed"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhilepostingdata"));
        }
    }

    public void AdvancePayment() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "11");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element invoiceId = new org.jdom.Element("InvoiceId");
        Integer invoiceid = new Integer(defTbModel1.getValueAt(table1.getSelectedRow(), 0).toString());
        invoiceId.setText(invoiceid.toString());
        root.addContent(invoiceId);
        org.jdom.Element invoiceNumber = new org.jdom.Element("InvoiceNo");
        String invoiceno = tfinvoicenumber.getText().trim();
        invoiceNumber.setText(invoiceno);
        root.addContent(invoiceNumber);
        org.jdom.Element payslipcontent = new org.jdom.Element("PayslipContent");
        payslipcontent.setText(reconcilationLetter);
        root.addContent(payslipcontent);
        org.jdom.Element refno1 = new org.jdom.Element("Refno");
        refno1.setText(refno.toString());
        root.addContent(refno1);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        String success = root1.getChildText("Success");
        if (root1 != null) {
            if (success.equals("Y")) {
                refreshScreen();
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("TaskSuccessful"));
            } else if (success.equals("D")) {
                newGenMain.showInformationMessage(resourceBundle.getString("AlreadyAdvancePaymentProcessed"));
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("AdvancePaymentProcessTransactionfailed"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhilepostingdata"));
        }
    }

    public void getAccessionDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "2");
        org.jdom.Element libraryId = new org.jdom.Element("LibraryId");
        libraryId.setText(newGenMain.getLibraryID());
        root.addContent(libraryId);
        org.jdom.Element invoiceId = new org.jdom.Element("InvoiceId");
        Integer invoiceid = new Integer(tfinvoicenumber.getText().trim());
        invoiceId.setText(invoiceid.toString());
        root.addContent(invoiceId);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is Client xmlStr:" + xmlStr);
        xmlStr = servletConnector.getInstance().sendRequest("PaymentFunctionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        Object[] objLocal1 = new Object[0];
        objLocal1 = root1.getChildren("AccessionDetails").toArray();
        if (root1 != null) {
            if (objLocal1.length > 0) {
                for (int k = defTbModel2.getRowCount(); k > 0; k--) {
                    removeRow = true;
                    defTbModel2.removeRow(k - 1);
                    removeRow = false;
                }
                for (int i = 0; i < objLocal1.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) objLocal1[i];
                    Object[][] row = new Object[1][1];
                    defTbModel2.addRow(row);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("RequestId")), defTbModel2.getRowCount() - 1, 0);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("Title")), defTbModel2.getRowCount() - 1, 1);
                    defTbModel2.setValueAt(utility.getTestedString(newGenMain.getMaterialName(element.getChildText("MaterialTypeId"))), defTbModel2.getRowCount() - 1, 2);
                    String accessionnumber = "";
                    try {
                        accessionnumber = utility.getTestedString(element.getChildText("AccessionNo"));
                    } catch (NumberFormatException ex) {
                        System.out.println("NumberFormatException while not accessioned");
                    } catch (NullPointerException ex) {
                        System.out.println("NullPointerException while not accessioned");
                    }
                    defTbModel2.setValueAt(accessionnumber, defTbModel2.getRowCount() - 1, 3);
                    Integer holdinglibrary = new Integer(0);
                    try {
                        holdinglibrary = new Integer(element.getChildText("HoldingLibrary"));
                    } catch (NumberFormatException ex) {
                        System.out.println("NumberFormatException while not accessioned");
                    } catch (NullPointerException ex) {
                        System.out.println("NullPointerException while not accessioned");
                    }
                    if (holdinglibrary.intValue() == 0) {
                        defTbModel2.setValueAt(holdinglibrary, defTbModel2.getRowCount() - 1, 4);
                    } else {
                        defTbModel2.setValueAt(utility.getTestedString(newGenMain.getLibraryName(holdinglibrary.toString())), defTbModel2.getRowCount() - 1, 4);
                    }
                    Integer orderedcopies = new Integer(element.getChildText("OrderedCopies"));
                    defTbModel2.setValueAt(new Integer(element.getChildText("OrderedCopies")), defTbModel2.getRowCount() - 1, 5);
                    Integer receivedcopies = new Integer(element.getChildText("ReceivedCopies"));
                    defTbModel2.setValueAt(new Integer(element.getChildText("ReceivedCopies")), defTbModel2.getRowCount() - 1, 6);
                    Double price = new Double(element.getChildText("Price"));
                    double orderamt = price.doubleValue();
                    String currencycode = utility.getTestedString(element.getChildText("CurrencyCode"));
                    double price2 = new Double(orderamt).doubleValue();
                    java.util.Hashtable ht = new java.util.Hashtable();
                    ht.put("LibraryID", newGenMain.getLibraryID());
                    ht.put("CurrencyCode", currencycode);
                    ht = newGenXMLGenerator.parseXMLDocument(servletConnector.sendRequest("Utility", newGenXMLGenerator.buildXMLDocument("8", ht)));
                    double conversionRate = 0.0d;
                    try {
                        conversionRate = new Double(ht.get("ConversionRate").toString()).doubleValue();
                    } catch (NumberFormatException ex) {
                        System.out.println("NumberFormatException while calculating conversion rate");
                    }
                    double orderamt1 = new Double(price2 * conversionRate).doubleValue();
                    int ordercopies = orderedcopies.intValue();
                    double orderedamount = orderamt1 * ordercopies;
                    defTbModel2.setValueAt(new Double(orderamt1), defTbModel2.getRowCount() - 1, 7);
                    Double invoiceprice = new Double(element.getChildText("InvoiceAmount"));
                    double invoiceamt = invoiceprice.doubleValue();
                    double invoiceprice2 = new Double(invoiceamt).doubleValue();
                    defTbModel2.setValueAt(new Double(invoiceprice2), defTbModel2.getRowCount() - 1, 8);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("Oneormoreitemsnotattobereceived\nPaymentcannotbeprocessed"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretrievingdata"));
        }
    }

    public void reloadLocales() {
        jDialog1.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchbyInvoiceNo"));
        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Searchby"));
        rbInvoiceno.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceNo"));
        rbOrderno.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderno"));
        rbVendor.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceNo"));
        bInvoiceno1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        jLabel10.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderno"));
        bOrderno1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        jLabel11.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        bVendor1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceNumber"));
        bInvoiceno.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceDate"));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceAmount"));
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderno"));
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderdate"));
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CurrencyCode"));
        jPanel2.setBorder(new javax.swing.border.TitledBorder(null, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Itemsintheinvoice"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 0, 51)));
        jPanel3.setBorder(new javax.swing.border.TitledBorder(null, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetDetails"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 0, 51)));
        this.defTbModel1 = new javax.swing.table.DefaultTableModel(new Object[] { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceNumber"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceDate"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceAmount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CurrencyCode"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNumber"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderDate"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AdvancePayment") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table1.setModel(defTbModel1);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setMaxWidth(0);
        table1.getColumnModel().getColumn(3).setPreferredWidth(0);
        table1.getColumnModel().getColumn(3).setMinWidth(0);
        table1.getColumnModel().getColumn(3).setMaxWidth(0);
        table1.getColumnModel().getColumn(4).setPreferredWidth(0);
        table1.getColumnModel().getColumn(4).setMinWidth(0);
        table1.getColumnModel().getColumn(4).setMaxWidth(0);
        table1.getColumnModel().getColumn(7).setPreferredWidth(0);
        table1.getColumnModel().getColumn(7).setMinWidth(0);
        table1.getColumnModel().getColumn(7).setMaxWidth(0);
        table1.getColumnModel().getColumn(8).setPreferredWidth(0);
        table1.getColumnModel().getColumn(8).setMinWidth(0);
        table1.getColumnModel().getColumn(8).setMaxWidth(0);
        this.defTbModel2 = new javax.swing.table.DefaultTableModel(new Object[] { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("RequestId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title/Author"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalPresentationForm"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AccessionNo"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("HoldingLibrary"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderedNoOfCopies"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoicedNoOfCopies"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderedAmount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceAmount") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table2.setModel(defTbModel2);
        table2.getColumnModel().getColumn(0).setPreferredWidth(0);
        table2.getColumnModel().getColumn(0).setMinWidth(0);
        table2.getColumnModel().getColumn(0).setMaxWidth(0);
        this.defTbModel3 = new javax.swing.table.DefaultTableModel(new Object[] { newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderedAmount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceAmount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BalanceAmount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CommittedAmount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ExpenditureAmount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetLibraryId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetTaId"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderedcopies"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Receivedcopies"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Price") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table3.setModel(defTbModel3);
        table3.getColumnModel().getColumn(7).setPreferredWidth(0);
        table3.getColumnModel().getColumn(7).setMinWidth(0);
        table3.getColumnModel().getColumn(7).setMaxWidth(0);
        table3.getColumnModel().getColumn(8).setPreferredWidth(0);
        table3.getColumnModel().getColumn(8).setMinWidth(0);
        table3.getColumnModel().getColumn(8).setMaxWidth(0);
        table3.getColumnModel().getColumn(9).setPreferredWidth(0);
        table3.getColumnModel().getColumn(9).setMinWidth(0);
        table3.getColumnModel().getColumn(9).setMaxWidth(0);
        table3.getColumnModel().getColumn(10).setPreferredWidth(0);
        table3.getColumnModel().getColumn(10).setMinWidth(0);
        table3.getColumnModel().getColumn(10).setMaxWidth(0);
        table3.getColumnModel().getColumn(11).setPreferredWidth(0);
        table3.getColumnModel().getColumn(11).setMinWidth(0);
        table3.getColumnModel().getColumn(11).setMaxWidth(0);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(jDialog1);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jDialog1 = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        rbInvoiceno = new javax.swing.JRadioButton();
        rbOrderno = new javax.swing.JRadioButton();
        rbVendor = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        tfinvoiceno1 = new newgen.presentation.UnicodeTextField();
        bInvoiceno1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        tforderno1 = new newgen.presentation.UnicodeTextField();
        bOrderno1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cbVendor = new javax.swing.JComboBox();
        bVendor1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        bOk = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bHelpCsh = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfinvoicenumber = new newgen.presentation.UnicodeTextField();
        bInvoiceno = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tfinvoicedate = new newgen.presentation.UnicodeTextField();
        jLabel3 = new javax.swing.JLabel();
        tfinvoiceamount = new newgen.presentation.UnicodeTextField();
        jLabel4 = new javax.swing.JLabel();
        tfvendor = new newgen.presentation.UnicodeTextField();
        jLabel5 = new javax.swing.JLabel();
        tforderno = new newgen.presentation.UnicodeTextField();
        jLabel6 = new javax.swing.JLabel();
        tforderdate = new newgen.presentation.UnicodeTextField();
        jLabel7 = new javax.swing.JLabel();
        tfcurrencycode = new newgen.presentation.UnicodeTextField();
        lAdvacepay = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table3 = new javax.swing.JTable();
        jDialog1.getContentPane().setLayout(new javax.swing.BoxLayout(jDialog1.getContentPane(), javax.swing.BoxLayout.Y_AXIS));
        jDialog1.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchbyInvoiceNo"));
        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Searchby"));
        jPanel4.add(jLabel8);
        rbInvoiceno.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceNo"));
        rbInvoiceno.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbInvoicenoActionPerformed(evt);
            }
        });
        jPanel4.add(rbInvoiceno);
        rbOrderno.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderno"));
        rbOrderno.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOrdernoActionPerformed(evt);
            }
        });
        jPanel4.add(rbOrderno);
        rbVendor.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        rbVendor.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbVendorActionPerformed(evt);
            }
        });
        jPanel4.add(rbVendor);
        jDialog1.getContentPane().add(jPanel4);
        jPanel5.setLayout(new java.awt.CardLayout());
        jPanel8.setLayout(new java.awt.GridBagLayout());
        jLabel9.setForeground(new java.awt.Color(255, 0, 51));
        jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceNo"));
        jPanel8.add(jLabel9, new java.awt.GridBagConstraints());
        tfinvoiceno1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfinvoiceno1ActionPerformed(evt);
            }
        });
        jPanel8.add(tfinvoiceno1, new java.awt.GridBagConstraints());
        bInvoiceno1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bInvoiceno1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bInvoiceno1.setPreferredSize(new java.awt.Dimension(47, 20));
        bInvoiceno1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInvoiceno1ActionPerformed(evt);
            }
        });
        jPanel8.add(bInvoiceno1, new java.awt.GridBagConstraints());
        jPanel5.add(jPanel8, "pInvoceNumber");
        jPanel9.setLayout(new java.awt.GridBagLayout());
        jLabel10.setForeground(new java.awt.Color(255, 0, 51));
        jLabel10.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderno"));
        jPanel9.add(jLabel10, new java.awt.GridBagConstraints());
        tforderno1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tforderno1ActionPerformed(evt);
            }
        });
        jPanel9.add(tforderno1, new java.awt.GridBagConstraints());
        bOrderno1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bOrderno1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bOrderno1.setPreferredSize(new java.awt.Dimension(47, 20));
        bOrderno1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOrderno1ActionPerformed(evt);
            }
        });
        jPanel9.add(bOrderno1, new java.awt.GridBagConstraints());
        jPanel5.add(jPanel9, "pOrderNumber");
        jPanel10.setLayout(new java.awt.GridBagLayout());
        jLabel11.setForeground(new java.awt.Color(255, 0, 51));
        jLabel11.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        jPanel10.add(jLabel11, new java.awt.GridBagConstraints());
        cbVendor.setPreferredSize(new java.awt.Dimension(167, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(cbVendor, gridBagConstraints);
        bVendor1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bVendor1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bVendor1.setPreferredSize(new java.awt.Dimension(47, 20));
        bVendor1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bVendor1ActionPerformed(evt);
            }
        });
        jPanel10.add(bVendor1, new java.awt.GridBagConstraints());
        jPanel5.add(jPanel10, "pVendor");
        jDialog1.getContentPane().add(jPanel5);
        jPanel6.setLayout(new java.awt.BorderLayout());
        table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        table1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table1.setRowHeight(20);
        jScrollPane3.setViewportView(table1);
        jPanel6.add(jScrollPane3, java.awt.BorderLayout.CENTER);
        jDialog1.getContentPane().add(jPanel6);
        jPanel7.setBorder(new javax.swing.border.EtchedBorder());
        bOk.setMnemonic('o');
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });
        jPanel7.add(bOk);
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bHelp.setMnemonic('h');
        jPanel7.add(bHelp);
        bHelpCsh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/helpcsh.gif")));
        jPanel7.add(bHelpCsh);
        bCancel.setMnemonic('c');
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });
        jPanel7.add(bCancel);
        jDialog1.getContentPane().add(jPanel7);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(1871, 150));
        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel1, gridBagConstraints);
        tfinvoicenumber.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfinvoicenumber, gridBagConstraints);
        bInvoiceno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bInvoiceno.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bInvoiceno.setPreferredSize(new java.awt.Dimension(47, 20));
        bInvoiceno.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInvoicenoActionPerformed(evt);
            }
        });
        jPanel1.add(bInvoiceno, new java.awt.GridBagConstraints());
        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel1.add(tfinvoicedate, gridBagConstraints);
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("InvoiceAmount"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        jPanel1.add(tfinvoiceamount, gridBagConstraints);
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel1.add(tfvendor, gridBagConstraints);
        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderno"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel1.add(tforderno, gridBagConstraints);
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orderdate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel1.add(tforderdate, gridBagConstraints);
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CurrencyCode"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        jPanel1.add(tfcurrencycode, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        jPanel1.add(lAdvacepay, gridBagConstraints);
        add(jPanel1);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBorder(new javax.swing.border.TitledBorder(null, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Itemsintheinvoice"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 0, 51)));
        jPanel2.setPreferredSize(new java.awt.Dimension(463, 350));
        table2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        table2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table2.setRowHeight(20);
        jScrollPane1.setViewportView(table2);
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel2);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.setBorder(new javax.swing.border.TitledBorder(null, newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetDetails"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 0, 51)));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(453, 350));
        table3.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        table3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table3.setRowHeight(20);
        jScrollPane2.setViewportView(table3);
        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        add(jPanel3);
    }

    private void tforderno1ActionPerformed(java.awt.event.ActionEvent evt) {
        bOrderno1.doClick();
    }

    private void tfinvoiceno1ActionPerformed(java.awt.event.ActionEvent evt) {
        bInvoiceno1.doClick();
    }

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {
        if (table1.getSelectedRow() != -1) {
            tfinvoicenumber.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 1).toString());
            tfinvoicedate.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 2).toString());
            tfinvoiceamount.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 3).toString());
            tfcurrencycode.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 4).toString());
            tfvendor.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 5).toString());
            tforderno.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 6).toString());
            tforderdate.setText(defTbModel1.getValueAt(table1.getSelectedRow(), 7).toString());
            String advPay = defTbModel1.getValueAt(table1.getSelectedRow(), 8).toString();
            getAccessionLibraryDetails();
            if (advPay.equals("Y")) {
                lAdvacepay.setVisible(true);
                lAdvacepay.setForeground(java.awt.Color.BLUE);
                lAdvacepay.setText("Already Advance Paid for this Invoice Number");
                getPaymentDetails();
            } else {
                lAdvacepay.setVisible(false);
                getPaymentDetails();
            }
            jDialog1.dispose();
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table1.grabFocus();
        }
    }

    private void bVendor1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (cbVendor.getSelectedIndex() > -1) {
            refreshScreen4();
            getVendorDetails();
        } else {
            System.out.println("Select the Vendor Name");
        }
    }

    private void bOrderno1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (tforderno1.getText().trim().length() > 0) {
            refreshScreen4();
            getOrderNoDetails();
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("EnterOrderNumber"));
            tforderno1.grabFocus();
        }
    }

    private void bInvoiceno1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (tfinvoiceno1.getText().trim().length() > 0) {
            refreshScreen4();
            getDetails();
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("EnterInvoiceNumber"));
            tfinvoiceno1.grabFocus();
        }
    }

    private void bInvoicenoActionPerformed(java.awt.event.ActionEvent evt) {
        refreshScreen1();
        utility.getVendors(cbVendor);
        jDialog1.setSize(500, 300);
        jDialog1.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(500, 300));
        jDialog1.show();
        tfinvoiceno1.grabFocus();
    }

    private void rbVendorActionPerformed(java.awt.event.ActionEvent evt) {
        rbVendor.setSelected(true);
        rbOrderno.setSelected(false);
        rbInvoiceno.setSelected(false);
        ((java.awt.CardLayout) this.jPanel5.getLayout()).show(jPanel5, "pVendor");
        refreshScreen4();
    }

    private void rbOrdernoActionPerformed(java.awt.event.ActionEvent evt) {
        rbOrderno.setSelected(true);
        rbInvoiceno.setSelected(false);
        rbVendor.setSelected(false);
        tforderno1.setText("");
        ((java.awt.CardLayout) this.jPanel5.getLayout()).show(jPanel5, "pOrderNumber");
        tforderno1.grabFocus();
        refreshScreen4();
    }

    private void rbInvoicenoActionPerformed(java.awt.event.ActionEvent evt) {
        rbInvoiceno.setSelected(true);
        rbOrderno.setSelected(false);
        rbVendor.setSelected(false);
        tfinvoiceno1.setText("");
        ((java.awt.CardLayout) this.jPanel5.getLayout()).show(jPanel5, "pInvoceNumber");
        tfinvoiceno1.grabFocus();
        refreshScreen4();
    }

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {
        refreshScreen1();
        jDialog1.dispose();
    }

    private javax.swing.JButton bCancel;

    private javax.swing.JButton bHelp;

    private javax.swing.JButton bHelpCsh;

    private javax.swing.JButton bInvoiceno;

    private javax.swing.JButton bInvoiceno1;

    private javax.swing.JButton bOk;

    private javax.swing.JButton bOrderno1;

    private javax.swing.JButton bVendor1;

    private javax.swing.JComboBox cbVendor;

    private javax.swing.JDialog jDialog1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JLabel lAdvacepay;

    private javax.swing.JRadioButton rbInvoiceno;

    private javax.swing.JRadioButton rbOrderno;

    private javax.swing.JRadioButton rbVendor;

    private javax.swing.JTable table1;

    private javax.swing.JTable table2;

    private javax.swing.JTable table3;

    private newgen.presentation.UnicodeTextField tfcurrencycode;

    private newgen.presentation.UnicodeTextField tfinvoiceamount;

    private newgen.presentation.UnicodeTextField tfinvoicedate;

    private newgen.presentation.UnicodeTextField tfinvoiceno1;

    private newgen.presentation.UnicodeTextField tfinvoicenumber;

    private newgen.presentation.UnicodeTextField tforderdate;

    private newgen.presentation.UnicodeTextField tforderno;

    private newgen.presentation.UnicodeTextField tforderno1;

    private newgen.presentation.UnicodeTextField tfvendor;

    String xmlStr1;

    String reconcilationLetter;

    String refno = "";
}
