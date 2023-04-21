package newgen.presentation.acquisitions.acqAdv;

/**
 *
 * @author  N VASU PRAVEEN
 */
public class AccessioningPanel extends javax.swing.JPanel {

    private static AccessioningPanel instance = null;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.Utility utility = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private javax.swing.table.DefaultTableModel defTbModel = null;

    private java.util.ResourceBundle resourceBundle = null;

    private boolean removeRow = false;

    private java.util.Hashtable htLocation;

    public static AccessioningPanel getInstance() {
        instance = new AccessioningPanel();
        return instance;
    }

    /** Creates new form AccessioningPanel */
    public AccessioningPanel() {
        initComponents();
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        resourceBundle = newGenMain.getMyResource();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        ((java.awt.CardLayout) this.cardPanel.getLayout()).show(cardPanel, "firmOrder");
        newgen.presentation.component.Utility.getInstance().getMaterialTypes(cbPresentationform);
        cbPresentationform.setSelectedIndex(6);
        loadAllLibrariesLocations();
        String[] cols = { "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title/Author"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Volume"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publisher"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edition"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ISBN"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor/Donor"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Acquisitionmode"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Price"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CurrencyCode"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Discount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Misccharges"), "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Library"), "", "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AcqMode") };
        this.defTbModel = new javax.swing.table.DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table1.setModel(defTbModel);
        table1.getTableHeader().setSize(new java.awt.Dimension(100, table1.getHeight()));
        table1.getTableHeader().setReorderingAllowed(false);
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
        table1.getColumnModel().getColumn(1).setPreferredWidth(147);
        table1.getColumnModel().getColumn(2).setPreferredWidth(109);
        table1.getColumnModel().getColumn(3).setPreferredWidth(109);
        table1.getColumnModel().getColumn(4).setPreferredWidth(107);
        table1.getColumnModel().getColumn(5).setPreferredWidth(75);
        table1.getColumnModel().getColumn(6).setPreferredWidth(75);
        table1.getColumnModel().getColumn(7).setPreferredWidth(75);
        table1.getColumnModel().getColumn(8).setPreferredWidth(75);
        table1.getColumnModel().getColumn(9).setPreferredWidth(75);
        table1.getColumnModel().getColumn(10).setPreferredWidth(75);
        table1.getColumnModel().getColumn(11).setPreferredWidth(75);
        table1.getColumnModel().getColumn(12).setMinWidth(0);
        table1.getColumnModel().getColumn(12).setPreferredWidth(0);
        table1.getColumnModel().getColumn(13).setMinWidth(0);
        table1.getColumnModel().getColumn(13).setPreferredWidth(0);
        table1.getColumnModel().getColumn(14).setMinWidth(0);
        table1.getColumnModel().getColumn(14).setPreferredWidth(0);
        table1.getColumnModel().getColumn(15).setMinWidth(0);
        table1.getColumnModel().getColumn(15).setPreferredWidth(0);
        table1.getColumnModel().getColumn(16).setMinWidth(0);
        table1.getColumnModel().getColumn(16).setPreferredWidth(0);
        table1.getColumnModel().getColumn(17).setMinWidth(0);
        table1.getColumnModel().getColumn(17).setPreferredWidth(0);
        table1.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table1.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent lEvt) {
                if (!lEvt.getValueIsAdjusting() && !removeRow) {
                    if (table1.getSelectedRow() != -1) {
                        if (defTbModel.getValueAt(table1.getSelectedRow(), 15).toString().equals("")) {
                            lbMessage.setText("");
                            java.util.Hashtable ht = new java.util.Hashtable();
                            ht.put("Id", defTbModel.getValueAt(table1.getSelectedRow(), 0).toString());
                            ht.put("LibraryId", newGenMain.getLibraryID());
                            ht.put("HoldingsLibraryId", defTbModel.getValueAt(table1.getSelectedRow(), 13).toString());
                            loadLibraryLocationsInCombobox(defTbModel.getValueAt(table1.getSelectedRow(), 13).toString());
                            ht.put("AcquisitionMode", defTbModel.getValueAt(table1.getSelectedRow(), 8).toString());
                            ht.put("VolumeId", defTbModel.getValueAt(table1.getSelectedRow(), 15).toString());
                            if (defTbModel.getValueAt(table1.getSelectedRow(), 15).toString().equals("")) {
                            } else {
                                String xmlres = servletConnector.sendRequest("AccessioningServlet", newGenXMLGenerator.buildXMLDocument("6", ht));
                                java.util.Hashtable htres = newGenXMLGenerator.parseXMLDocument(xmlres);
                                String classno = utility.getTestedString(htres.get("CLASSNO"));
                                String bookno = utility.getTestedString(htres.get("BOOKNO"));
                                tfClassfication.setText(classno);
                                tfBooknumber.setText(bookno);
                                tfCallno.setText(classno + " " + bookno);
                            }
                            tfHoldinglibrary.setText(defTbModel.getValueAt(table1.getSelectedRow(), 14).toString());
                            String typeid = defTbModel.getValueAt(table1.getSelectedRow(), 16).toString();
                            String typename = newGenMain.getMaterialName(typeid);
                            cbPresentationform.setSelectedItem(typename);
                        } else {
                            lbMessage.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ThisItemIsAlreadyCataloguedAfterAccessioningItMayBeSentToRacks"));
                            java.util.Hashtable ht = new java.util.Hashtable();
                            loadLibraryLocationsInCombobox(defTbModel.getValueAt(table1.getSelectedRow(), 13).toString());
                            ht.put("LibraryId", defTbModel.getValueAt(table1.getSelectedRow(), 13).toString());
                            ht.put("VolumeId", defTbModel.getValueAt(table1.getSelectedRow(), 15).toString());
                            String xmlres = servletConnector.sendRequest("AccessioningServlet", newGenXMLGenerator.buildXMLDocument("6", ht));
                            java.util.Hashtable htres = newGenXMLGenerator.parseXMLDocument(xmlres);
                            String classno = utility.getTestedString(htres.get("ClassificationNumber"));
                            String bookno = utility.getTestedString(htres.get("BookNumber"));
                            tfClassfication.setText(classno);
                            tfBooknumber.setText(bookno);
                            tfCallno.setText(classno + " " + bookno);
                            tfHoldinglibrary.setText(defTbModel.getValueAt(table1.getSelectedRow(), 14).toString());
                            String typeid = defTbModel.getValueAt(table1.getSelectedRow(), 16).toString();
                            String typename = newGenMain.getMaterialName(typeid);
                            cbPresentationform.setSelectedItem(typename);
                        }
                    }
                }
            }
        });
        tfHoldinglibrary.setEditable(false);
        tfCallno.setEditable(false);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        onRefreshClick();
    }

    public void refreshScreen1() {
        for (int i = defTbModel.getRowCount(); i > 0; i--) {
            if (table1.getSelectedRow() != -1) {
                removeRow = true;
                defTbModel.removeRow(i - 1);
                removeRow = false;
            }
        }
    }

    public void updateDatabase() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            if (!tfBarcode.getText().trim().equals("")) {
                java.util.Hashtable htreq = new java.util.Hashtable();
                htreq.put("LibraryId", newGenMain.getLibraryID());
                htreq.put("Id", defTbModel.getValueAt(selectedRow, 0).toString());
                htreq.put("HoldingsLibraryId", defTbModel.getValueAt(selectedRow, 13).toString());
                htreq.put("AcquisitionMode", defTbModel.getValueAt(selectedRow, 17).toString());
                htreq.put("VolumeId", defTbModel.getValueAt(selectedRow, 15).toString());
                htreq.put("AccessionNumber", tfBarcode.getText());
                htreq.put("BarcodeNumber", tfAccessionnumber.getText());
                htreq.put("ClassNo", tfClassfication.getText());
                htreq.put("BookNo", tfBooknumber.getText());
                htreq.put("MaterialTypeId", newGenMain.getMaterialId(cbPresentationform.getSelectedItem().toString()));
                htreq.put("LocationId", getLocationId(cbLocation.getSelectedItem().toString(), defTbModel.getValueAt(selectedRow, 13).toString()));
                htreq.put("EntryId", newGenMain.getEntryID());
                htreq.put("EntryLibraryId", newGenMain.getLibraryID());
                String xmlres = servletConnector.sendRequest("AccessioningServlet", newGenXMLGenerator.buildXMLDocument("8", htreq));
                System.out.println(xmlres);
                java.util.Hashtable htres = newGenXMLGenerator.parseXMLDocument(xmlres);
                String status = htres.get("STATUS").toString();
                String emailDispatch = utility.getTestedString(htres.get("EMAIL_DISPATCHED"));
                if (status.equals("SUCCESS")) {
                    newGenMain.showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("TaskSuccessful"));
                    if (emailDispatch.equals("YES")) {
                        newGenMain.showInformationMessage("<html>This book has been reserved by patron(s).<br>" + "Please retain the document at the Circulation desk.<br>" + "Mail has been dispatched to the patron(s).</html>");
                    }
                    onRefreshClick();
                } else {
                    String reason = utility.getTestedString(htres.get("RESAON"));
                    System.out.println("-------------------REASON = " + reason);
                    if (reason.equals("AVAILABLE")) newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BarcodeExistsPleaseUseADifferentBarcode")); else newGenMain.showErrorMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ERROR") + " " + newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Reason") + ": " + reason);
                    onRefreshClick();
                }
            } else {
                newGenMain.showInsufficientDataDialog(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("EnterBarcode"));
            }
        }
    }

    public void refreshScreen2() {
        tfBooknumber.setText("");
        tfBarcode.setText("");
        tfClassfication.setText("");
        tfAccessionnumber.setText("");
        tfCallno.setText("");
        tfHoldinglibrary.setEditable(false);
        tfCallno.setEditable(false);
        cbPresentationform.setSelectedIndex(6);
        cbLocation.setSelectedIndex(0);
        if (defTbModel.getRowCount() > 0) {
            table1.grabFocus();
            table1.setRowSelectionInterval(0, 0);
        }
    }

    public void refreshScreen() {
        refreshScreen1();
        tfBooknumber.setText("");
        tfBarcode.setText("");
        tfClassfication.setText("");
        tfAccessionnumber.setText("");
        tfCallno.setText("");
        tfHoldinglibrary.setEditable(false);
        tfCallno.setEditable(false);
        cbPresentationform.setSelectedIndex(6);
    }

    public void getLocationDetails() {
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryID", newGenMain.getLibraryID());
        org.jdom.Element root = newGenXMLGenerator.getRootElement(servletConnector.sendRequest("AccessioningServlet", newGenXMLGenerator.buildXMLDocument("1", ht)));
        if (root != null) {
            java.util.ArrayList arrLocation = new java.util.ArrayList();
            Object[] object = new Object[0];
            try {
                object = root.getChildren("LocationDetails").toArray();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            htLocation = new java.util.Hashtable();
            if (object.length > 0) {
                for (int i = 0; i < object.length; i++) {
                    org.jdom.Element element = (org.jdom.Element) object[i];
                    arrLocation.add(element.getChildText("Location").toString());
                    java.util.Collections.sort(arrLocation);
                    String libid = element.getChildText("LibraryId");
                    String locid = element.getChildText("LocationId");
                    String loc = element.getChildText("Location");
                }
                Object[] objectLocation = arrLocation.toArray();
                for (int k = 0; k < objectLocation.length; k++) {
                    cbLocation.addItem(objectLocation[k]);
                }
            } else {
                newGenMain.showInformationMessage(resourceBundle.getString("NoLocationRecords"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Problemoccuredwhilepostingdata"));
        }
    }

    public void getInvoiceDetails() {
        defTbModel.setRowCount(0);
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryId", newGenMain.getLibraryID());
        String xmlres = servletConnector.sendRequest("AccessioningServlet", newGenXMLGenerator.buildXMLDocument("5", ht));
        System.out.println(xmlres);
        org.jdom.Element rootele = newGenXMLGenerator.getRootElement(xmlres);
        java.util.List listcopies = rootele.getChildren("Copy");
        java.util.Vector vecrows = new java.util.Vector();
        for (int i = 0; i < listcopies.size(); i++) {
            org.jdom.Element elecopy = (org.jdom.Element) listcopies.get(i);
            Object[] r = new Object[18];
            r[0] = utility.getTestedString(elecopy.getChildTextTrim("Id"));
            r[1] = utility.getTestedString(elecopy.getChildTextTrim("TitleAuthor"));
            r[2] = utility.getTestedString(utility.getVolumeInfoDisplay(elecopy.getChildTextTrim("VOLUMENO")));
            r[3] = utility.getTestedString(elecopy.getChildTextTrim("Publisher"));
            r[4] = utility.getTestedString(elecopy.getChildTextTrim("Edition"));
            r[5] = utility.getTestedString(elecopy.getChildTextTrim("ISBN"));
            r[6] = utility.getTestedString(elecopy.getChildTextTrim("OrderNo"));
            String acqmode = utility.getTestedString(elecopy.getChildTextTrim("AcquisitionMode"));
            r[17] = acqmode;
            if (acqmode.equals("FirmOrder")) {
                r[7] = utility.getTestedString(elecopy.getChildTextTrim("VendorName"));
                r[8] = newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FirmOrder");
                r[13] = utility.getTestedString(elecopy.getChildTextTrim("RequestLibraryId"));
                r[14] = newGenMain.getLibraryName(r[13].toString());
            } else if (acqmode.equals("Gift")) {
                r[7] = utility.getTestedString(elecopy.getChildTextTrim("Donor"));
                r[8] = newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Gift");
                r[13] = newGenMain.getLibraryID();
                r[14] = newGenMain.getLibraryName(r[13].toString());
            }
            r[9] = utility.getTestedString(elecopy.getChildTextTrim("Price"));
            r[10] = utility.getTestedString(elecopy.getChildTextTrim("CurrencyCode"));
            r[11] = utility.getTestedString(elecopy.getChildTextTrim("Discount"));
            r[12] = utility.getTestedString(elecopy.getChildTextTrim("Miscellaneous"));
            r[15] = utility.getTestedString(elecopy.getChildTextTrim("VOLUMEID"));
            r[16] = utility.getTestedString(elecopy.getChildTextTrim("MATERIALTYPEID"));
            defTbModel.addRow(r);
        }
    }

    public void getVolumeIdDetails() {
        if (table1.getSelectedRow() != -1) {
            String volumeid = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 12).toString());
            if (!volumeid.equals("")) {
                String xmlStr;
                org.jdom.Element root = new org.jdom.Element("OperationId");
                root.setAttribute("no", "4");
                utility.addLoginDetailsToTheRootElement(root);
                org.jdom.Element copyId = new org.jdom.Element("CopyId");
                String copyID = defTbModel.getValueAt(table1.getSelectedRow(), 8).toString();
                copyId.setText(copyID);
                root.addContent(copyId);
                org.jdom.Element library = new org.jdom.Element("LibraryId");
                String libraryName = defTbModel.getValueAt(table1.getSelectedRow(), 9).toString();
                library.setText(newGenMain.getLibraryId(libraryName));
                root.addContent(library);
                org.jdom.Element volumeId = new org.jdom.Element("VolumeId");
                volumeId.setText(volumeid);
                root.addContent(volumeId);
                org.jdom.Document doc = new org.jdom.Document(root);
                xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
                xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("AccessioningServlet", xmlStr);
                org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
                Object[] object = new Object[0];
                try {
                    object = root1.getChildren("VolumeDetails").toArray();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (object.length > 0) {
                    for (int i = 0; i < object.length; i++) {
                        org.jdom.Element element = (org.jdom.Element) object[i];
                        String bookno = utility.getTestedString(element.getChildText("BookNumber"));
                        tfBooknumber.setText(bookno);
                        String classificationno = utility.getTestedString(element.getChildText("ClassificationNumber"));
                        tfClassfication.setText(classificationno);
                        String callno = utility.getTestedString(element.getChildText("CallNo"));
                        tfCallno.setText(callno);
                    }
                } else {
                    tfBooknumber.setText("");
                    tfClassfication.setText("");
                    tfCallno.setText("");
                }
            } else {
                tfBooknumber.setText("");
                tfClassfication.setText("");
                tfCallno.setText("");
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table1.grabFocus();
        }
    }

    public void getDetails() {
        if (table1.getSelectedRow() != -1) {
            if (tfBarcode.getText().trim().length() > 0 && tfAccessionnumber.getText().trim().length() > 0) {
                String xmlStr;
                org.jdom.Element root = new org.jdom.Element("OperationId");
                root.setAttribute("no", "3");
                utility.addLoginDetailsToTheRootElement(root);
                org.jdom.Element copyId = new org.jdom.Element("CopyId");
                String copyID = defTbModel.getValueAt(table1.getSelectedRow(), 8).toString();
                copyId.setText(copyID);
                root.addContent(copyId);
                org.jdom.Element library = new org.jdom.Element("LibraryId");
                String libraryName = defTbModel.getValueAt(table1.getSelectedRow(), 9).toString();
                library.setText(newGenMain.getLibraryId(libraryName));
                root.addContent(library);
                org.jdom.Element barcode = new org.jdom.Element("Barcode");
                barcode.setText(tfBarcode.getText().trim());
                root.addContent(barcode);
                org.jdom.Element accesiionnumber = new org.jdom.Element("AccessionNo");
                accesiionnumber.setText(tfAccessionnumber.getText().trim());
                root.addContent(accesiionnumber);
                org.jdom.Element bookno = new org.jdom.Element("BookNo");
                bookno.setText(tfBooknumber.getText().trim());
                root.addContent(bookno);
                org.jdom.Element classificationno = new org.jdom.Element("ClassificationNo");
                classificationno.setText(tfClassfication.getText().trim());
                root.addContent(classificationno);
                org.jdom.Element callno = new org.jdom.Element("CallNo");
                callno.setText(tfCallno.getText());
                root.addContent(callno);
                org.jdom.Element materialtype = new org.jdom.Element("MaterialType");
                materialtype.setText(newGenMain.getMaterialId(cbPresentationform.getSelectedItem().toString()));
                root.addContent(materialtype);
                org.jdom.Element location = new org.jdom.Element("Location");
                location.setText(cbLocation.getSelectedItem().toString());
                root.addContent(location);
                org.jdom.Element volumeId = new org.jdom.Element("VolumeId");
                String volumeid = defTbModel.getValueAt(table1.getSelectedRow(), 12).toString();
                if (volumeid.equals("")) {
                    volumeid = "0";
                    volumeId.setText(volumeid);
                } else {
                    volumeId.setText(volumeid);
                }
                root.addContent(volumeId);
                org.jdom.Element volume = new org.jdom.Element("VolumeNo");
                String volumeno = "";
                volumeno = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 10).toString());
                volume.setText(volumeno);
                root.addContent(volume);
                org.jdom.Element partsub = new org.jdom.Element("PartSubDivision");
                String partsubId = "";
                partsubId = utility.getTestedString(defTbModel.getValueAt(table1.getSelectedRow(), 11).toString());
                partsub.setText(partsubId);
                root.addContent(partsub);
                org.jdom.Element requestId = new org.jdom.Element("RequestId");
                String requestID = defTbModel.getValueAt(table1.getSelectedRow(), 13).toString();
                requestId.setText(requestID);
                root.addContent(requestId);
                org.jdom.Element acqMode = new org.jdom.Element("AcqMode");
                String acqMode1 = defTbModel.getValueAt(table1.getSelectedRow(), 6).toString();
                acqMode.setText(acqMode1);
                root.addContent(acqMode);
                org.jdom.Document doc = new org.jdom.Document(root);
                xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
                xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("AccessioningServlet", xmlStr);
                org.jdom.Element root1 = newGenXMLGenerator.getRootElement(xmlStr);
                if (root1.getChildText("Success").equals("D")) {
                    tfBarcode.setText("");
                    tfAccessionnumber.setText("");
                    newGenMain.showInformationMessage(resourceBundle.getString("Accessionnoandbarcodeshouldbeuniquew.r.tlibrary\nTheenteredaccessionnoandbarcodealreadyexist"));
                } else if (root1.getChildText("Success").equals("Y")) {
                    refreshScreen();
                    int printCopies = 0;
                    newGenMain.showInformationMessage(resourceBundle.getString("TaskSuccessful"));
                    String printcop = utility.getTestedString(root1.getChildText("PrintCopies"));
                    if (!printcop.equals("") && !printcop.equals("0")) {
                        printCopies = new Integer(printcop).intValue();
                    }
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
                    newGenMain.showErrorMessage(resourceBundle.getString("AccessionTransaction(s)failed"));
                }
            } else {
                newGenMain.showInformationMessage("Enterthebarcodeandaccessionnumber");
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table1.grabFocus();
        }
    }

    public int getCount() {
        int i;
        i = table1.getRowCount();
        System.out.println("table row count:" + i);
        return i;
    }

    public void reloadLocales() {
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("HoldingsLibrary(Acquiredfor)"));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("itemBarcode"));
        bValidate.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Validate"));
        bAccessionNo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NextvalidAccessionNo"));
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AccessionNumber"));
        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ClassificationNumber"));
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BookNo"));
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CallNumber"));
        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalPresentationForm"));
        jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Location"));
        String[] cols = { "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title/Author"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Volume"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publisher"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edition"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ISBN"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Vendor/Donor"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Acquisitionmode"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Price"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CurrencyCode"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Discount"), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Misccharges"), "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Library"), "", "", newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AcqMode") };
        this.defTbModel.setColumnIdentifiers(cols);
        table1.setModel(defTbModel);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
        table1.getColumnModel().getColumn(1).setPreferredWidth(147);
        table1.getColumnModel().getColumn(2).setPreferredWidth(109);
        table1.getColumnModel().getColumn(3).setPreferredWidth(109);
        table1.getColumnModel().getColumn(4).setPreferredWidth(107);
        table1.getColumnModel().getColumn(5).setPreferredWidth(75);
        table1.getColumnModel().getColumn(6).setPreferredWidth(75);
        table1.getColumnModel().getColumn(7).setPreferredWidth(75);
        table1.getColumnModel().getColumn(8).setPreferredWidth(75);
        table1.getColumnModel().getColumn(9).setPreferredWidth(75);
        table1.getColumnModel().getColumn(10).setPreferredWidth(75);
        table1.getColumnModel().getColumn(11).setPreferredWidth(75);
        table1.getColumnModel().getColumn(12).setMinWidth(0);
        table1.getColumnModel().getColumn(12).setPreferredWidth(0);
        table1.getColumnModel().getColumn(13).setMinWidth(0);
        table1.getColumnModel().getColumn(13).setPreferredWidth(0);
        table1.getColumnModel().getColumn(14).setMinWidth(0);
        table1.getColumnModel().getColumn(14).setPreferredWidth(0);
        table1.getColumnModel().getColumn(15).setMinWidth(0);
        table1.getColumnModel().getColumn(15).setPreferredWidth(0);
        table1.getColumnModel().getColumn(16).setMinWidth(0);
        table1.getColumnModel().getColumn(16).setPreferredWidth(0);
        table1.getColumnModel().getColumn(17).setMinWidth(0);
        table1.getColumnModel().getColumn(17).setPreferredWidth(0);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        cardPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lbMessage = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfBarcode = new newgen.presentation.UnicodeTextField();
        bValidate = new javax.swing.JButton();
        bAccessionNo = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tfAccessionnumber = new newgen.presentation.UnicodeTextField();
        jLabel5 = new javax.swing.JLabel();
        tfClassfication = new newgen.presentation.UnicodeTextField();
        jLabel6 = new javax.swing.JLabel();
        tfBooknumber = new newgen.presentation.UnicodeTextField();
        jLabel7 = new javax.swing.JLabel();
        tfCallno = new newgen.presentation.UnicodeTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cbPresentationform = new javax.swing.JComboBox();
        cbLocation = new javax.swing.JComboBox();
        tfHoldinglibrary = new newgen.presentation.UnicodeTextField();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        cardPanel.setLayout(new java.awt.CardLayout());
        cardPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cardPanel.setPreferredSize(new java.awt.Dimension(457, 350));
        jPanel5.setLayout(new java.awt.BorderLayout());
        table1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        table1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(table1);
        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        cardPanel.add(jPanel5, "firmOrder");
        add(cardPanel);
        lbMessage.setFont(new java.awt.Font("Dialog", 3, 14));
        lbMessage.setForeground(new java.awt.Color(0, 0, 170));
        jPanel2.add(lbMessage);
        add(jPanel2);
        jPanel4.setLayout(new java.awt.GridBagLayout());
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setPreferredSize(new java.awt.Dimension(622, 165));
        jLabel2.setForeground(new java.awt.Color(170, 0, 0));
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("HoldingsLibrary(Acquiredfor)"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel2, gridBagConstraints);
        jLabel3.setForeground(new java.awt.Color(170, 0, 0));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("itemBarcode"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel3, gridBagConstraints);
        tfBarcode.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfBarcodeActionPerformed(evt);
            }
        });
        tfBarcode.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfBarcodeCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel4.add(tfBarcode, gridBagConstraints);
        bValidate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/validate.gif")));
        bValidate.setMnemonic('v');
        bValidate.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Validate"));
        bValidate.setPreferredSize(new java.awt.Dimension(40, 20));
        bValidate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bValidateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel4.add(bValidate, gridBagConstraints);
        bAccessionNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/pickup.gif")));
        bAccessionNo.setMnemonic('n');
        bAccessionNo.setToolTipText("Next valid Accession No");
        bAccessionNo.setPreferredSize(new java.awt.Dimension(40, 20));
        bAccessionNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAccessionNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel4.add(bAccessionNo, gridBagConstraints);
        jLabel4.setForeground(new java.awt.Color(170, 0, 0));
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("AccessionNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(tfAccessionnumber, gridBagConstraints);
        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ClassificationNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel5, gridBagConstraints);
        tfClassfication.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfClassficationCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel4.add(tfClassfication, gridBagConstraints);
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BookNo"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel6, gridBagConstraints);
        tfBooknumber.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfBooknumberCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel4.add(tfBooknumber, gridBagConstraints);
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CallNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(tfCallno, gridBagConstraints);
        jLabel8.setForeground(new java.awt.Color(170, 0, 0));
        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalPresentationForm"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel8, gridBagConstraints);
        jLabel9.setForeground(new java.awt.Color(170, 0, 0));
        jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Location"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel4.add(jLabel9, gridBagConstraints);
        cbPresentationform.setPreferredSize(new java.awt.Dimension(167, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(cbPresentationform, gridBagConstraints);
        cbLocation.setPreferredSize(new java.awt.Dimension(176, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(cbLocation, gridBagConstraints);
        tfHoldinglibrary.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(tfHoldinglibrary, gridBagConstraints);
        add(jPanel4);
    }

    private void bAccessionNoActionPerformed(java.awt.event.ActionEvent evt) {
        if (tfHoldinglibrary.getText().length() > 0) {
            newgen.presentation.administration.PickUpAccessionNumber pic = new newgen.presentation.administration.PickUpAccessionNumber();
            pic.show();
            if (pic.getReturnCode() == 0) {
                tfBarcode.setText(pic.getBarcode());
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table1.grabFocus();
        }
    }

    private void tfBarcodeActionPerformed(java.awt.event.ActionEvent evt) {
        bValidate.doClick();
    }

    private void bValidateActionPerformed(java.awt.event.ActionEvent evt) {
        if (tfHoldinglibrary.getText().length() > 0) {
            String libIdSelected = newgen.presentation.NewGenMain.getAppletInstance().getLibraryId(tfHoldinglibrary.getText().toString());
            String barcode = tfBarcode.getText().trim();
            int x = newgen.presentation.component.Utility.getInstance().validateBarcode(barcode, libIdSelected);
            if (x == 0) {
                newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BarcodeExistsPleaseUseADifferentBarcode"));
                tfBarcode.requestFocus();
                tfBarcode.selectAll();
            } else if (x == 1) {
                newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BarcodeEnteredCanBeUsed"));
            } else if (x == 2) {
                newgen.presentation.NewGenMain.getAppletInstance().showErrorMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ERROR"));
            }
        } else {
            newGenMain.showInformationMessage(resourceBundle.getString("Selectarecordfromtable"));
            table1.grabFocus();
        }
    }

    private void tfBarcodeCaretUpdate(javax.swing.event.CaretEvent evt) {
        tfAccessionnumber.setText(tfBarcode.getText());
    }

    private void tfBooknumberCaretUpdate(javax.swing.event.CaretEvent evt) {
        tfCallno.setText(tfClassfication.getText() + " " + tfBooknumber.getText());
    }

    private void tfClassficationCaretUpdate(javax.swing.event.CaretEvent evt) {
        tfCallno.setText(tfClassfication.getText() + " " + tfBooknumber.getText());
    }

    public void onRefreshClick() {
        refreshScreen1();
        getInvoiceDetails();
        tfBooknumber.setText("");
        tfBarcode.setText("");
        tfClassfication.setText("");
        tfAccessionnumber.setText("");
        tfCallno.setText("");
        tfBarcode.requestFocus();
        if (table1.getRowCount() > 0) {
            table1.setRowSelectionInterval(0, 0);
        }
    }

    public void loadAllLibrariesLocations() {
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

    public void loadLibraryLocationsInCombobox(String libraryId) {
        java.util.Hashtable htloc = (java.util.Hashtable) htLocations.get(libraryId);
        cbLocation.removeAllItems();
        Object[] col = htloc.values().toArray();
        for (int i = 0; i < col.length; i++) {
            cbLocation.addItem(col[i].toString());
        }
    }

    public String getLocationId(String locname, String libraryId) {
        java.util.Hashtable htloc = (java.util.Hashtable) htLocations.get(libraryId);
        java.util.Enumeration enumx = htloc.keys();
        String locid = "";
        while (enumx.hasMoreElements()) {
            String key = enumx.nextElement().toString();
            String loca = htloc.get(key).toString();
            if (loca.equals(locname)) {
                locid = key;
                break;
            }
        }
        return locid;
    }

    private javax.swing.JButton bAccessionNo;

    private javax.swing.JButton bValidate;

    private javax.swing.JPanel cardPanel;

    private javax.swing.JComboBox cbLocation;

    private javax.swing.JComboBox cbPresentationform;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lbMessage;

    private javax.swing.JTable table1;

    private newgen.presentation.UnicodeTextField tfAccessionnumber;

    private newgen.presentation.UnicodeTextField tfBarcode;

    private newgen.presentation.UnicodeTextField tfBooknumber;

    private newgen.presentation.UnicodeTextField tfCallno;

    private newgen.presentation.UnicodeTextField tfClassfication;

    private newgen.presentation.UnicodeTextField tfHoldinglibrary;

    private java.util.Hashtable htLocations;
}
