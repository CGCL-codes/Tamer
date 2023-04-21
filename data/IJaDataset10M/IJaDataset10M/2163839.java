package newgen.presentation.sm;

import org.jdom.*;
import newgen.presentation.*;
import org.jdom.output.*;
import org.jdom.input.*;

/**
 *
 * @author  Administrator
 */
public class ReOrderSubscriptionPanel extends javax.swing.JPanel {

    private javax.swing.table.DefaultTableModel dtmSubscription, dtmSubLib;

    private javax.swing.table.DefaultTableModel defTbModel = null;

    private javax.swing.table.DefaultTableModel defTbModel1 = null;

    private javax.swing.table.DefaultTableModel defTbModel2 = null;

    private javax.swing.table.DefaultTableModel defTbModel3 = null;

    private javax.swing.table.DefaultTableModel defTbModel4 = null;

    private javax.swing.table.DefaultTableModel defTbModelOrderNos = null;

    private javax.swing.DefaultListModel listModelPending = null;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private newgen.presentation.component.Utility utility = null;

    private java.util.ResourceBundle resourceBundle = null;

    private boolean removeRow = false;

    newgen.presentation.sm.SerialMasterDialog smDialog = null;

    /** Creates new form ReOrderSubscriptionOrderPanel */
    public ReOrderSubscriptionPanel() {
        initComponents();
        jPanel4.add(jScrollPane2);
        jPanel4.add(jPanel5, java.awt.BorderLayout.LINE_END);
        dialog.setModal(true);
        dialog.setSize(400, 350);
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        dialog.setLocation(newGenMain.getLocation(400, 350));
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        resourceBundle = newGenMain.getMyResource();
        this.defTbModel = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("SubscriptionId"), resourceBundle.getString("Title"), resourceBundle.getString("ISSN"), resourceBundle.getString("Publisher"), resourceBundle.getString("SubscriptionPeriod"), resourceBundle.getString("SubscriptionAmount"), resourceBundle.getString("VendorName") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table.setModel(defTbModel);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(6).setMinWidth(0);
        table.getColumnModel().getColumn(6).setMaxWidth(0);
        table.getColumnModel().getColumn(6).setPreferredWidth(0);
        table.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent lEvt) {
                if (!lEvt.getValueIsAdjusting() && !removeRow) {
                    if (table.getSelectedRow() != -1) {
                    }
                }
            }
        });
        this.defTbModel1 = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("SubscriptionId"), resourceBundle.getString("Title"), resourceBundle.getString("ISSN"), resourceBundle.getString("Publisher"), resourceBundle.getString("SubscriptionPeriod"), resourceBundle.getString("SubscriptionAmount") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table1.setModel(defTbModel1);
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setMaxWidth(0);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
        table1.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.defTbModel2 = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("OrderNo"), resourceBundle.getString("OrderNo"), resourceBundle.getString("VendorName"), resourceBundle.getString("BatchDate") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        table2.setModel(defTbModel2);
        table2.getColumnModel().getColumn(0).setMinWidth(0);
        table2.getColumnModel().getColumn(0).setMaxWidth(0);
        table2.getColumnModel().getColumn(0).setPreferredWidth(0);
        table2.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dialogOrderInfo.setSize(330, 180);
        dialogOrderInfo.setLocation(newGenMain.getLocation(330, 180));
        dialogOrderNos.setSize(290, 300);
        dialogOrderNos.setLocation(newGenMain.getLocation(290, 300));
        this.defTbModelOrderNos = new javax.swing.table.DefaultTableModel(new Object[] { resourceBundle.getString("OrderNo"), resourceBundle.getString("Batchdate") }, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        tabOrderNos.setModel(defTbModelOrderNos);
        tabOrderNos.getColumnModel().getColumn(0).setPreferredWidth(170);
        tabOrderNos.getColumnModel().getColumn(0).setMinWidth(170);
        tabOrderNos.getColumnModel().getColumn(0).setMaxWidth(170);
        tabOrderNos.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabOrderNos.getColumnModel().getColumn(1).setMinWidth(120);
        tabOrderNos.getColumnModel().getColumn(1).setMaxWidth(120);
        tabOrderNos.getColumnModel().getColumn(1).setCellRenderer(utility.getCenterAlignmentForTableColumn());
        refreshScreen();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialog);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogOrderInfo);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogOrderNos);
    }

    public void refreshScreen() {
        try {
            utility.getVendors(cbVendor);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        for (int i = defTbModel.getRowCount(); i > 0; i--) {
            defTbModel.removeRow(i - 1);
        }
        for (int j = defTbModel1.getRowCount(); j > 0; j--) {
            defTbModel1.removeRow(j - 1);
        }
        for (int k = defTbModel2.getRowCount(); k > 0; k--) {
            defTbModel2.removeRow(k - 1);
        }
        rbOrderNo.setSelected(true);
        rbVendor.setSelected(false);
        ((java.awt.CardLayout) this.jPanel8.getLayout()).show(jPanel8, "orderno");
        tfOrderNo.setText("");
        tfOrderNo1.setText("");
        tfOrderNo2.setText("");
        bNew.setSelected(false);
    }

    public void getArrivalDatePeriod() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "2");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.sendRequest("ReOrderSubscriptionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        String arrivalPeriod = utility.getTestedString(root1.getChildText("ArrivalPeriod"));
        if (!arrivalPeriod.equals("")) {
            int arrivalperiod = Integer.parseInt(arrivalPeriod);
            String arrivalDate = utility.getWorkingDayAfterTheseDays(arrivalperiod);
            tArrivalDate.setDate(arrivalDate);
        }
    }

    private boolean getOpenOrderNos() {
        boolean flag = false;
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryID", newGenMain.getLibraryID());
        ht.put("Vendor", cbVendor.getSelectedItem());
        java.util.Vector[] vector = newGenXMLGenerator.parseXMLDocument(new String[] { "OrderNo", "BatchDate" }, servletConnector.sendRequest("ReOrderSubscriptionServlet", newGenXMLGenerator.buildXMLDocument("1", ht)));
        for (int i = defTbModelOrderNos.getRowCount(); i > 0; i--) {
            defTbModelOrderNos.removeRow(i - 1);
        }
        if (vector[0].size() > 0) {
            flag = true;
            for (int i = 0; i < vector[0].size(); i++) {
                Object[][] row = new Object[1][1];
                defTbModelOrderNos.addRow(row);
                defTbModelOrderNos.setValueAt(vector[0].elementAt(i), defTbModelOrderNos.getRowCount() - 1, 0);
                defTbModelOrderNos.setValueAt(utility.getDate(vector[1].elementAt(i).toString()), defTbModelOrderNos.getRowCount() - 1, 1);
            }
        }
        return flag;
    }

    public void getReOrderNoDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "4");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element orderNo = new org.jdom.Element("OrderNo");
        orderNo.setText(tfOrderNo1.getText().trim());
        root.addContent(orderNo);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("ReOrderSubscriptionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            for (int i = defTbModel2.getRowCount(); i > 0; i--) {
                defTbModel2.removeRow(i - 1);
            }
            Object[] object = new Object[0];
            try {
                object = root1.getChildren("ReOrderNoDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (object.length > 0) {
                for (int i = 0; i < object.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) object[i];
                    Object[][] row = new Object[1][1];
                    defTbModel2.addRow(row);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("OrderNo")), defTbModel2.getRowCount() - 1, 0);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("OrderNoStr")), defTbModel2.getRowCount() - 1, 1);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("VendorName")), defTbModel2.getRowCount() - 1, 2);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("BatchDate")), defTbModel2.getRowCount() - 1, 3);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoOrderNodetails"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
    }

    public void getReOrderNoVendorDetails() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "5");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element vendor = new org.jdom.Element("VendorName");
        vendor.setText(cbVendor1.getSelectedItem().toString());
        root.addContent(vendor);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("ReOrderSubscriptionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            for (int i = defTbModel2.getRowCount(); i > 0; i--) {
                defTbModel2.removeRow(i - 1);
            }
            Object[] object = new Object[0];
            try {
                object = root1.getChildren("ReVendorDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (object.length > 0) {
                for (int i = 0; i < object.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) object[i];
                    Object[][] row = new Object[1][1];
                    defTbModel2.addRow(row);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("OrderNo")), defTbModel2.getRowCount() - 1, 0);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("OrderNoStr")), defTbModel2.getRowCount() - 1, 1);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("VendorName")), defTbModel2.getRowCount() - 1, 2);
                    defTbModel2.setValueAt(utility.getTestedString(element.getChildText("BatchDate")), defTbModel2.getRowCount() - 1, 3);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoVendordetails"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
    }

    public void getLineItemsInTheReOrder() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "6");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element orderno = new org.jdom.Element("OrderNo");
        orderno.setText(defTbModel2.getValueAt(table2.getSelectedRow(), 0).toString());
        root.addContent(orderno);
        org.jdom.Element vendor = new org.jdom.Element("VendorName");
        vendor.setText(defTbModel2.getValueAt(table2.getSelectedRow(), 2).toString());
        root.addContent(vendor);
        org.jdom.Element status = new org.jdom.Element("Status");
        String status1 = "A";
        status.setText(status1);
        root.addContent(status);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("ReOrderSubscriptionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            for (int i = defTbModel.getRowCount(); i > 0; i--) {
                defTbModel.removeRow(i - 1);
            }
            Object[] object = new Object[0];
            try {
                object = root1.getChildren("ReOrderSubscriptionDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (object.length > 0) {
                for (int i = 0; i < object.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) object[i];
                    Object[][] row = new Object[1][1];
                    defTbModel.addRow(row);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("SubScriptionId")), defTbModel.getRowCount() - 1, 0);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Title")), defTbModel.getRowCount() - 1, 1);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("ISSN")), defTbModel.getRowCount() - 1, 2);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("Publisher")), defTbModel.getRowCount() - 1, 3);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("SubscriptionPeriod")), defTbModel.getRowCount() - 1, 4);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("SubscriptionAmount")), defTbModel.getRowCount() - 1, 5);
                    defTbModel.setValueAt(utility.getTestedString(element.getChildText("VendorName")), defTbModel.getRowCount() - 1, 6);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoSubscriptiondata"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
    }

    public void getLineItemsInTheOrder() {
        String xmlStr;
        org.jdom.Element root = new org.jdom.Element("OperationId");
        root.setAttribute("no", "7");
        utility.addLoginDetailsToTheRootElement(root);
        org.jdom.Element orderno = new org.jdom.Element("OrderNo");
        orderno.setText(defTbModelOrderNos.getValueAt(tabOrderNos.getSelectedRow(), 0).toString());
        root.addContent(orderno);
        org.jdom.Element vendorname = new org.jdom.Element("VendorName");
        vendorname.setText(cbVendor.getSelectedItem().toString());
        root.addContent(vendorname);
        org.jdom.Document doc = new org.jdom.Document(root);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        xmlStr = servletConnector.getInstance().sendRequest("ReOrderSubscriptionServlet", xmlStr);
        org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
        if (root1 != null) {
            Object[] object = new Object[0];
            try {
                object = root1.getChildren("ReOrderSubscriptionDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (object.length > 0) {
                for (int i = 0; i < object.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) object[i];
                    Object[][] row = new Object[1][1];
                    defTbModel1.addRow(row);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubScriptionId")), defTbModel1.getRowCount() - 1, 0);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("Title")), defTbModel1.getRowCount() - 1, 1);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("ISSN")), defTbModel1.getRowCount() - 1, 2);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("Publisher")), defTbModel1.getRowCount() - 1, 3);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubscriptionPeriod")), defTbModel1.getRowCount() - 1, 4);
                    defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubscriptionAmount")), defTbModel1.getRowCount() - 1, 5);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoSubscriptiondata"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
        }
    }

    public void getReOrderDetails() {
        if (!tfOrderNo2.getText().trim().equals("")) {
            String vendorname1 = defTbModel.getValueAt(table.getSelectedRow(), 6).toString();
            String vendorname2 = cbVendor.getSelectedItem().toString();
            if (!vendorname2.equals(vendorname1)) {
                String xmlStr;
                org.jdom.Element root = new org.jdom.Element("OperationId");
                root.setAttribute("no", "8");
                utility.addLoginDetailsToTheRootElement(root);
                int tableselection[] = table.getSelectedRows();
                org.jdom.Element subscriptionId = new org.jdom.Element("SubscriptionId");
                subscriptionId.setText(defTbModel.getValueAt(table.getSelectedRow(), 0).toString());
                root.addContent(subscriptionId);
                org.jdom.Element vendor = new org.jdom.Element("Vendorname");
                vendor.setText(defTbModel.getValueAt(table.getSelectedRow(), 6).toString());
                root.addContent(vendor);
                org.jdom.Element orderNo = new org.jdom.Element("OrderNo");
                orderNo.setText(tfOrderNo2.getText().trim().toString());
                root.addContent(orderNo);
                org.jdom.Element orderno = new org.jdom.Element("Orderno");
                orderno.setText(tfOrderNo.getText().trim().toString());
                root.addContent(orderno);
                org.jdom.Element vendorname = new org.jdom.Element("VendorName");
                vendorname.setText(cbVendor.getSelectedItem().toString());
                root.addContent(vendorname);
                org.jdom.Document doc = new org.jdom.Document(root);
                org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
                out.setIndent(true);
                out.setNewlines(true);
                java.io.StringWriter sw = new java.io.StringWriter();
                try {
                    out.output(doc, sw);
                } catch (java.io.IOException ex) {
                    ex.printStackTrace();
                }
                xmlStr = sw.toString();
                System.out.println("This is client xmlStr:" + xmlStr);
                xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("ReOrderSubscriptionServlet", xmlStr);
                org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
                if (root1.getChildText("Success").equals("Y")) {
                    subScriptionDetails();
                    removeSelectedRowFromTable();
                    newGenMain.showInformationMessage(resourceBundle.getString("TaskSuccessful"));
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
                } else {
                    newGenMain.showErrorMessage(resourceBundle.getString("ItemfailedtoposttotheCurrentOrder"));
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewVendor"));
            }
        } else {
            if (defTbModel1.getRowCount() > 0) {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewOrderNoorSelectExistingOrderforVendor"));
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewOrderNoorSelectExistingOrderforVendor"));
            }
        }
    }

    public void closeLineItemFromTheOrder() {
        if (!tfOrderNo2.getText().trim().equals("")) {
            String xmlStr;
            org.jdom.Element root = new org.jdom.Element("OperationId");
            root.setAttribute("no", "9");
            utility.addLoginDetailsToTheRootElement(root);
            org.jdom.Element orderNo = new org.jdom.Element("OrderNo");
            orderNo.setText(tfOrderNo2.getText().trim().toString());
            root.addContent(orderNo);
            org.jdom.Element vendorname = new org.jdom.Element("VendorName");
            vendorname.setText(cbVendor.getSelectedItem().toString());
            root.addContent(vendorname);
            int tableselection = table1.getRowCount();
            System.out.println("Tbale count:" + tableselection);
            for (int j = 0; j < table1.getRowCount(); j++) {
                org.jdom.Element closeOrder = new org.jdom.Element("CloseOrder");
                org.jdom.Element element = new org.jdom.Element("SubscriptionID");
                element.setText(defTbModel1.getValueAt(j, 0).toString());
                closeOrder.addContent(element);
                root.addContent(closeOrder);
            }
            org.jdom.Document doc = new org.jdom.Document(root);
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setIndent(true);
            out.setNewlines(true);
            java.io.StringWriter sw = new java.io.StringWriter();
            try {
                out.output(doc, sw);
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
            xmlStr = sw.toString();
            System.out.println("This is client xmlStr:" + xmlStr);
            xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("ReOrderSubscriptionServlet", xmlStr);
            org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
            if (root1.getChildText("Success").equals("Y")) {
                for (int i = defTbModel1.getRowCount(); i > 0; i--) {
                    defTbModel1.removeRow(i - 1);
                }
                tfOrderNo2.setText("");
                newGenMain.showInformationMessage(resourceBundle.getString("TaskSuccessful"));
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
            } else {
                newGenMain.showErrorMessage(resourceBundle.getString("CurrentOrderClosurefailed"));
            }
        } else {
            if (defTbModel1.getRowCount() > 0) {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewOrderNoorSelectExistingOrderforVendor"));
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewOrderNoorSelectExistingOrderforVendor"));
            }
        }
    }

    private void removeSelectedRowFromTable() {
        removeRow = true;
        defTbModel.removeRow(table.getSelectedRow());
        removeRow = false;
    }

    public void subScriptionDetails() {
        if (!tfOrderNo2.getText().trim().equals("")) {
            String xmlStr;
            org.jdom.Element root = new org.jdom.Element("OperationId");
            root.setAttribute("no", "10");
            utility.addLoginDetailsToTheRootElement(root);
            org.jdom.Element subscriptionId = new org.jdom.Element("SubscriptionId");
            subscriptionId.setText(defTbModel.getValueAt(table.getSelectedRow(), 0).toString());
            root.addContent(subscriptionId);
            org.jdom.Element orderNo = new org.jdom.Element("OrderNo");
            orderNo.setText(tfOrderNo2.getText().trim().toString());
            root.addContent(orderNo);
            org.jdom.Element vendorname = new org.jdom.Element("VendorName");
            vendorname.setText(cbVendor.getSelectedItem().toString());
            root.addContent(vendorname);
            org.jdom.Document doc = new org.jdom.Document(root);
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            out.setIndent(true);
            out.setNewlines(true);
            java.io.StringWriter sw = new java.io.StringWriter();
            try {
                out.output(doc, sw);
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
            xmlStr = sw.toString();
            System.out.println("This is client xmlStr:" + xmlStr);
            xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("ReOrderSubscriptionServlet", xmlStr);
            org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
            if (root1 != null) {
                Object[] object = new Object[0];
                try {
                    object = root1.getChildren("SubscriptionDetails").toArray();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (object.length > 0) {
                    for (int i = 0; i < object.length; i++) {
                        org.jdom.Element element = (org.jdom.Element) object[i];
                        Object[][] row = new Object[1][1];
                        defTbModel1.addRow(row);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubScriptionId")), defTbModel1.getRowCount() - 1, 0);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("Title")), defTbModel1.getRowCount() - 1, 1);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("ISSN")), defTbModel1.getRowCount() - 1, 2);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("Publisher")), defTbModel1.getRowCount() - 1, 3);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubscriptionPeriod")), defTbModel1.getRowCount() - 1, 4);
                        defTbModel1.setValueAt(utility.getTestedString(element.getChildText("SubscriptionAmount")), defTbModel1.getRowCount() - 1, 5);
                    }
                } else {
                    newGenMain.showInformationMessage(resourceBundle.getString("NoSubscriptiondata"));
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhileretreingdata"));
            }
        } else {
            if (defTbModel1.getRowCount() > 0) {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewOrderNoorSelectExistingOrderforVendor"));
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewOrderNoorSelectExistingOrderforVendor"));
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
        dialog = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        rbOrderNo = new javax.swing.JRadioButton();
        rbVendor = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfOrderNo1 = new newgen.presentation.UnicodeTextField();
        bOrderNoGo = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cbVendor1 = new javax.swing.JComboBox();
        bVendorGo = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        bOk = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bHelpCsh = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        dialogOrderInfo = new javax.swing.JDialog();
        panel14 = new javax.swing.JPanel();
        lArrivalDate = new javax.swing.JLabel();
        tArrivalDate = new newgen.presentation.component.DateField();
        lBatchDate = new javax.swing.JLabel();
        tBatchDate = new newgen.presentation.component.DateField();
        panel15 = new javax.swing.JPanel();
        bOkOrderInfo = new javax.swing.JButton();
        bHelpOderInfo = new newgen.presentation.component.HelpButton();
        bCSHOrderInfo = new newgen.presentation.component.CSHButton();
        bCloseOrderInfo = new javax.swing.JButton();
        dialogOrderNos = new javax.swing.JDialog();
        panel4 = new javax.swing.JPanel();
        spOrderNos = new javax.swing.JScrollPane();
        tabOrderNos = new javax.swing.JTable();
        panel5 = new javax.swing.JPanel();
        bOkOrderNos = new javax.swing.JButton();
        bHelpOrderNos = new newgen.presentation.component.HelpButton();
        bCSHOrderNos = new newgen.presentation.component.CSHButton();
        bCloseOrderNos = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lOrderNo = new javax.swing.JLabel();
        tfOrderNo = new newgen.presentation.UnicodeTextField();
        bSearchOrders = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbVendor = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        tfOrderNo2 = new newgen.presentation.UnicodeTextField();
        bNew = new javax.swing.JToggleButton();
        bSelectExisting = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        bReOrder = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        bMaster = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        bCloseOrder = new javax.swing.JButton();
        dialog.getContentPane().setLayout(new javax.swing.BoxLayout(dialog.getContentPane(), javax.swing.BoxLayout.Y_AXIS));
        dialog.setModal(true);
        jPanel7.setLayout(new java.awt.GridBagLayout());
        jPanel7.setBorder(new javax.swing.border.EtchedBorder());
        rbOrderNo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"));
        rbOrderNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOrderNoActionPerformed(evt);
            }
        });
        jPanel7.add(rbOrderNo, new java.awt.GridBagConstraints());
        rbVendor.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        rbVendor.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbVendorActionPerformed(evt);
            }
        });
        jPanel7.add(rbVendor, new java.awt.GridBagConstraints());
        dialog.getContentPane().add(jPanel7);
        jPanel8.setLayout(new java.awt.CardLayout());
        jPanel11.setLayout(new java.awt.GridBagLayout());
        jPanel11.setBorder(new javax.swing.border.EtchedBorder());
        jLabel3.setForeground(new java.awt.Color(255, 0, 51));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"));
        jPanel11.add(jLabel3, new java.awt.GridBagConstraints());
        jPanel11.add(tfOrderNo1, new java.awt.GridBagConstraints());
        bOrderNoGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bOrderNoGo.setPreferredSize(new java.awt.Dimension(47, 20));
        bOrderNoGo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOrderNoGoActionPerformed(evt);
            }
        });
        jPanel11.add(bOrderNoGo, new java.awt.GridBagConstraints());
        jPanel8.add(jPanel11, "orderno");
        jPanel12.setLayout(new java.awt.GridBagLayout());
        jPanel12.setBorder(new javax.swing.border.EtchedBorder());
        jLabel4.setForeground(new java.awt.Color(255, 0, 51));
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        jPanel12.add(jLabel4, new java.awt.GridBagConstraints());
        cbVendor1.setPreferredSize(new java.awt.Dimension(167, 20));
        jPanel12.add(cbVendor1, new java.awt.GridBagConstraints());
        bVendorGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bVendorGo.setPreferredSize(new java.awt.Dimension(47, 20));
        bVendorGo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bVendorGoActionPerformed(evt);
            }
        });
        jPanel12.add(bVendorGo, new java.awt.GridBagConstraints());
        jPanel8.add(jPanel12, "vendorname");
        dialog.getContentPane().add(jPanel8);
        jPanel9.setLayout(new java.awt.BorderLayout());
        table2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jScrollPane1.setViewportView(table2);
        jPanel9.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        dialog.getContentPane().add(jPanel9);
        jPanel10.setBorder(new javax.swing.border.EtchedBorder());
        bOk.setMnemonic('o');
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });
        jPanel10.add(bOk);
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bHelp.setMnemonic('h');
        jPanel10.add(bHelp);
        bHelpCsh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/helpcsh.gif")));
        jPanel10.add(bHelpCsh);
        bCancel.setMnemonic('c');
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });
        jPanel10.add(bCancel);
        dialog.getContentPane().add(jPanel10);
        dialogOrderInfo.setModal(true);
        panel14.setLayout(new java.awt.GridBagLayout());
        panel14.setBorder(new javax.swing.border.EtchedBorder());
        lArrivalDate.setForeground(new java.awt.Color(255, 0, 51));
        lArrivalDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ArrivalDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panel14.add(lArrivalDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panel14.add(tArrivalDate, gridBagConstraints);
        lBatchDate.setForeground(new java.awt.Color(255, 0, 51));
        lBatchDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BatchDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panel14.add(lBatchDate, gridBagConstraints);
        tBatchDate.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tBatchDateCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panel14.add(tBatchDate, gridBagConstraints);
        dialogOrderInfo.getContentPane().add(panel14, java.awt.BorderLayout.CENTER);
        panel15.setBorder(new javax.swing.border.EtchedBorder());
        bOkOrderInfo.setMnemonic('o');
        bOkOrderInfo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderInfo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkOrderInfoActionPerformed(evt);
            }
        });
        panel15.add(bOkOrderInfo);
        bHelpOderInfo.setMnemonic('h');
        panel15.add(bHelpOderInfo);
        panel15.add(bCSHOrderInfo);
        bCloseOrderInfo.setMnemonic('e');
        bCloseOrderInfo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderInfo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseOrderInfoActionPerformed(evt);
            }
        });
        panel15.add(bCloseOrderInfo);
        dialogOrderInfo.getContentPane().add(panel15, java.awt.BorderLayout.SOUTH);
        dialogOrderNos.setModal(true);
        panel4.setLayout(new java.awt.BorderLayout());
        tabOrderNos.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        tabOrderNos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        spOrderNos.setViewportView(tabOrderNos);
        panel4.add(spOrderNos, java.awt.BorderLayout.CENTER);
        dialogOrderNos.getContentPane().add(panel4, java.awt.BorderLayout.CENTER);
        panel5.setBorder(new javax.swing.border.EtchedBorder());
        bOkOrderNos.setMnemonic('o');
        bOkOrderNos.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderNos.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkOrderNosActionPerformed(evt);
            }
        });
        panel5.add(bOkOrderNos);
        bHelpOrderNos.setMnemonic('h');
        panel5.add(bHelpOrderNos);
        panel5.add(bCSHOrderNos);
        bCloseOrderNos.setMnemonic('e');
        bCloseOrderNos.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderNos.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseOrderNosActionPerformed(evt);
            }
        });
        panel5.add(bCloseOrderNos);
        dialogOrderNos.getContentPane().add(panel5, java.awt.BorderLayout.SOUTH);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jPanel1.setMinimumSize(new java.awt.Dimension(233, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 40));
        lOrderNo.setForeground(new java.awt.Color(255, 0, 51));
        lOrderNo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(lOrderNo, gridBagConstraints);
        tfOrderNo.setEditable(false);
        tfOrderNo.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                tfOrderNoFocusLost(evt);
            }
        });
        jPanel1.add(tfOrderNo, new java.awt.GridBagConstraints());
        bSearchOrders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Find16.gif")));
        bSearchOrders.setMnemonic('s');
        bSearchOrders.setPreferredSize(new java.awt.Dimension(47, 20));
        bSearchOrders.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSearchOrdersActionPerformed(evt);
            }
        });
        jPanel1.add(bSearchOrders, new java.awt.GridBagConstraints());
        add(jPanel1);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jScrollPane3.setBorder(new javax.swing.border.TitledBorder(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SelectSubscriptionstoReOrder")));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(400, 100));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(400, 100));
        table.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jScrollPane3.setViewportView(table);
        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);
        add(jPanel3);
        jPanel2.setLayout(new java.awt.GridBagLayout());
        jPanel2.setBorder(new javax.swing.border.TitledBorder(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SelectVendortoReOrder")));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 60));
        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor"));
        jPanel2.add(jLabel1, new java.awt.GridBagConstraints());
        cbVendor.setPreferredSize(new java.awt.Dimension(167, 20));
        jPanel2.add(cbVendor, new java.awt.GridBagConstraints());
        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"));
        jPanel2.add(jLabel2, new java.awt.GridBagConstraints());
        tfOrderNo2.setEditable(false);
        jPanel2.add(tfOrderNo2, new java.awt.GridBagConstraints());
        bNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/New16.gif")));
        bNew.setMnemonic('n');
        bNew.setPreferredSize(new java.awt.Dimension(47, 20));
        bNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNewActionPerformed(evt);
            }
        });
        jPanel2.add(bNew, new java.awt.GridBagConstraints());
        bSelectExisting.setMnemonic('s');
        bSelectExisting.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SelectExistingOrderforVendor"));
        bSelectExisting.setPreferredSize(new java.awt.Dimension(217, 20));
        bSelectExisting.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSelectExistingActionPerformed(evt);
            }
        });
        jPanel2.add(bSelectExisting, new java.awt.GridBagConstraints());
        add(jPanel2);
        jPanel6.setLayout(new java.awt.GridBagLayout());
        jPanel6.setBorder(new javax.swing.border.EtchedBorder());
        bReOrder.setMnemonic('r');
        bReOrder.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ReOrderSubscription"));
        bReOrder.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bReOrderActionPerformed(evt);
            }
        });
        jPanel6.add(bReOrder, new java.awt.GridBagConstraints());
        add(jPanel6);
        jPanel4.setLayout(new java.awt.BorderLayout());
        jScrollPane2.setMinimumSize(new java.awt.Dimension(400, 100));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(500, 100));
        table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jScrollPane2.setViewportView(table1);
        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jPanel5.setLayout(new java.awt.GridBagLayout());
        jPanel5.setBorder(new javax.swing.border.EtchedBorder());
        bMaster.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/master.gif")));
        bMaster.setMnemonic('m');
        bMaster.setPreferredSize(new java.awt.Dimension(47, 26));
        bMaster.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMasterActionPerformed(evt);
            }
        });
        jPanel5.add(bMaster, new java.awt.GridBagConstraints());
        jPanel4.add(jPanel5, java.awt.BorderLayout.EAST);
        add(jPanel4);
        jPanel13.setLayout(new java.awt.GridBagLayout());
        jPanel13.setBorder(new javax.swing.border.EtchedBorder());
        bCloseOrder.setMnemonic('o');
        bCloseOrder.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CloseCurrentOrder"));
        bCloseOrder.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseOrderActionPerformed(evt);
            }
        });
        jPanel13.add(bCloseOrder, new java.awt.GridBagConstraints());
        add(jPanel13);
    }

    private void bCloseOrderActionPerformed(java.awt.event.ActionEvent evt) {
        if (tfOrderNo2.getText().trim().length() > 0) {
            closeLineItemFromTheOrder();
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("SelectNewOrderNoorSelectExistingOrderforVendor"));
            tfOrderNo2.grabFocus();
        }
    }

    private void tBatchDateCaretUpdate(javax.swing.event.CaretEvent evt) {
        getArrivalDatePeriod();
    }

    private void bOkOrderNosActionPerformed(java.awt.event.ActionEvent evt) {
        if (tabOrderNos.getSelectedRowCount() == 1) {
            tfOrderNo2.setText(defTbModelOrderNos.getValueAt(tabOrderNos.getSelectedRow(), 0).toString());
            for (int i = defTbModel1.getRowCount(); i > 0; i--) {
                defTbModel1.removeRow(i - 1);
            }
            getLineItemsInTheOrder();
            dialogOrderNos.dispose();
            bNew.setSelected(false);
        } else {
            if (tabOrderNos.getSelectedRowCount() > 1) {
                newGenMain.showInformationMessage(resourceBundle.getString("Selectonlyoneorder"));
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("Selectoneorder"));
            }
        }
    }

    private void bCloseOrderNosActionPerformed(java.awt.event.ActionEvent evt) {
        dialogOrderNos.dispose();
    }

    private void bOkOrderInfoActionPerformed(java.awt.event.ActionEvent evt) {
        java.util.Hashtable ht = new java.util.Hashtable();
        utility.addLoginDetailsToTheHashtable(ht);
        ht.put("BatchDate", tBatchDate.getDate());
        ht.put("ArrivalDate", tArrivalDate.getDate());
        ht.put("Vendor", cbVendor.getSelectedItem().toString());
        String xmlStr = utility.getTestedString(servletConnector.sendRequest("ReOrderSubscriptionServlet", newGenXMLGenerator.buildXMLDocument("3", ht)));
        if (xmlStr.length() > 0) {
            ht = newGenXMLGenerator.parseXMLDocument(xmlStr);
            if (ht != null && utility.getTestedString(ht.get("OrderNo")).length() > 0) {
                tfOrderNo2.setText(ht.get("OrderNo").toString());
            } else {
                newGenMain.showErrorMessage(resourceBundle.getString("Problemoccuredwhilefetchingorderno"));
            }
        } else {
            newGenMain.showErrorMessage(resourceBundle.getString("Problemoccuredwhilefetchingorderno"));
        }
        dialogOrderInfo.dispose();
    }

    private void bCloseOrderInfoActionPerformed(java.awt.event.ActionEvent evt) {
        dialogOrderInfo.dispose();
    }

    private void bReOrderActionPerformed(java.awt.event.ActionEvent evt) {
        if (table.getSelectedRow() != -1) {
            int selectedRow = this.table.getSelectedRow();
            if (selectedRow != -1) {
                String subscriptionid = this.defTbModel.getValueAt(selectedRow, 0).toString();
                boolean val = false;
                for (int i = 0; i < this.defTbModel1.getRowCount(); i++) {
                    if (this.defTbModel1.getValueAt(i, 0).toString().equals(subscriptionid)) {
                        val = true;
                        break;
                    }
                }
                if (!val) {
                    getReOrderDetails();
                } else {
                    newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(resourceBundle.getString("AlreadySubscriptionisadded"));
                    table.grabFocus();
                }
            }
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table.grabFocus();
        }
    }

    private void bSelectExistingActionPerformed(java.awt.event.ActionEvent evt) {
        if (cbVendor.getSelectedIndex() != -1) {
            String vendorname1 = defTbModel.getValueAt(table.getSelectedRow(), 6).toString();
            String vendorname2 = cbVendor.getSelectedItem().toString();
            if (!vendorname2.equals(vendorname1)) {
                if (getOpenOrderNos()) {
                    dialogOrderNos.show();
                } else {
                    for (int i = defTbModel1.getRowCount(); i > 0; i--) {
                        defTbModel1.removeRow(i - 1);
                    }
                    tfOrderNo2.setText("");
                    newGenMain.showInformationMessage(resourceBundle.getString("Noopenorders"));
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("SelectNewVendor"));
            }
        }
    }

    private void bNewActionPerformed(java.awt.event.ActionEvent evt) {
        if (tfOrderNo.getText().trim().length() > 0) {
            if (table.getSelectedRow() != -1) {
                if (cbVendor.getSelectedIndex() != -1) {
                    String vendorname1 = defTbModel.getValueAt(table.getSelectedRow(), 6).toString();
                    String vendorname2 = cbVendor.getSelectedItem().toString();
                    if (!vendorname2.equals(vendorname1)) {
                        if (bNew.isSelected()) {
                            for (int i = defTbModel1.getRowCount(); i > 0; i--) {
                                defTbModel1.removeRow(i - 1);
                            }
                            getArrivalDatePeriod();
                            dialogOrderInfo.show();
                        } else {
                            tfOrderNo2.setText("");
                            bSelectExisting.grabFocus();
                        }
                    } else {
                        newGenMain.showInformationMessage(resourceBundle.getString("SelectNewVendor"));
                    }
                } else {
                }
            } else {
                bNew.setSelected(false);
                newGenMain.showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
                table.grabFocus();
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("SelectExistingOrderNo"));
            tfOrderNo.grabFocus();
        }
    }

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {
        if (table2.getSelectedRow() != -1) {
            getLineItemsInTheReOrder();
            tfOrderNo.setText(defTbModel2.getValueAt(table2.getSelectedRow(), 1).toString());
            dialog.dispose();
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table2.grabFocus();
        }
    }

    private void bVendorGoActionPerformed(java.awt.event.ActionEvent evt) {
        if (cbVendor1.getSelectedIndex() != -1) {
            getReOrderNoVendorDetails();
        } else {
        }
    }

    private void bOrderNoGoActionPerformed(java.awt.event.ActionEvent evt) {
        if (tfOrderNo1.getText().trim().length() > 0) {
            getReOrderNoDetails();
        } else {
        }
    }

    private void rbVendorActionPerformed(java.awt.event.ActionEvent evt) {
        rbOrderNo.setSelected(false);
        rbVendor.setSelected(true);
        ((java.awt.CardLayout) this.jPanel8.getLayout()).show(jPanel8, "vendorname");
        for (int i = defTbModel2.getRowCount(); i > 0; i--) {
            defTbModel2.removeRow(i - 1);
        }
    }

    private void rbOrderNoActionPerformed(java.awt.event.ActionEvent evt) {
        rbOrderNo.setSelected(true);
        rbVendor.setSelected(false);
        tfOrderNo1.setText("");
        ((java.awt.CardLayout) this.jPanel8.getLayout()).show(jPanel8, "orderno");
        for (int i = defTbModel2.getRowCount(); i > 0; i--) {
            defTbModel2.removeRow(i - 1);
        }
        tfOrderNo1.grabFocus();
    }

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {
        dialog.dispose();
    }

    private void bMasterActionPerformed(java.awt.event.ActionEvent evt) {
        if (table1.getSelectedRow() != -1) {
            String subscriptionId = defTbModel1.getValueAt(table1.getSelectedRow(), 0).toString();
            ;
            SerialMasterDialog smd = SerialMasterDialog.getInstance();
            smd.setSubscription(subscriptionId, newGenMain.getLibraryID());
            smd.show();
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table1.grabFocus();
        }
    }

    private void bSearchOrdersActionPerformed(java.awt.event.ActionEvent evt) {
        rbOrderNo.setSelected(true);
        rbVendor.setSelected(false);
        ((java.awt.CardLayout) this.jPanel8.getLayout()).show(jPanel8, "orderno");
        tfOrderNo1.setText("");
        utility.getVendors(cbVendor1);
        for (int i = defTbModel2.getRowCount(); i > 0; i--) {
            defTbModel2.removeRow(i - 1);
        }
        tfOrderNo1.grabFocus();
        dialog.show();
    }

    private void tfOrderNoFocusLost(java.awt.event.FocusEvent evt) {
    }

    private newgen.presentation.component.CSHButton bCSHOrderInfo;

    private newgen.presentation.component.CSHButton bCSHOrderNos;

    private javax.swing.JButton bCancel;

    private javax.swing.JButton bCloseOrder;

    private javax.swing.JButton bCloseOrderInfo;

    private javax.swing.JButton bCloseOrderNos;

    private javax.swing.JButton bHelp;

    private javax.swing.JButton bHelpCsh;

    private newgen.presentation.component.HelpButton bHelpOderInfo;

    private newgen.presentation.component.HelpButton bHelpOrderNos;

    private javax.swing.JButton bMaster;

    private javax.swing.JToggleButton bNew;

    private javax.swing.JButton bOk;

    private javax.swing.JButton bOkOrderInfo;

    private javax.swing.JButton bOkOrderNos;

    private javax.swing.JButton bOrderNoGo;

    private javax.swing.JButton bReOrder;

    private javax.swing.JButton bSearchOrders;

    private javax.swing.JButton bSelectExisting;

    private javax.swing.JButton bVendorGo;

    private javax.swing.JComboBox cbVendor;

    private javax.swing.JComboBox cbVendor1;

    private javax.swing.JDialog dialog;

    private javax.swing.JDialog dialogOrderInfo;

    private javax.swing.JDialog dialogOrderNos;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel11;

    private javax.swing.JPanel jPanel12;

    private javax.swing.JPanel jPanel13;

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

    private javax.swing.JLabel lArrivalDate;

    private javax.swing.JLabel lBatchDate;

    private javax.swing.JLabel lOrderNo;

    private javax.swing.JPanel panel14;

    private javax.swing.JPanel panel15;

    private javax.swing.JPanel panel4;

    private javax.swing.JPanel panel5;

    private javax.swing.JRadioButton rbOrderNo;

    private javax.swing.JRadioButton rbVendor;

    private javax.swing.JScrollPane spOrderNos;

    private newgen.presentation.component.DateField tArrivalDate;

    private newgen.presentation.component.DateField tBatchDate;

    private javax.swing.JTable tabOrderNos;

    private javax.swing.JTable table;

    private javax.swing.JTable table1;

    private javax.swing.JTable table2;

    private newgen.presentation.UnicodeTextField tfOrderNo;

    private newgen.presentation.UnicodeTextField tfOrderNo1;

    private newgen.presentation.UnicodeTextField tfOrderNo2;

    java.util.Hashtable htVendor;

    private String batchDate = "";
}
