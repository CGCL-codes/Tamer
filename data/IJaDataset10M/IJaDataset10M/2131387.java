package newgen.presentation.holdings;

/**
 *
 * @author  Administrator
 */
public class HoldingTypeDecisionDialog extends javax.swing.JDialog {

    /** Creates new form HoldingTypeDecisionDialog */
    public HoldingTypeDecisionDialog() {
        super();
        initComponents();
        this.setModal(true);
        this.setSize(405, 226);
        this.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(405, 226));
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        partTypeDecissionDisp = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        radSingle = new javax.swing.JRadioButton();
        radMultiwithInd = new javax.swing.JRadioButton();
        radMultipart = new javax.swing.JRadioButton();
        radSerialItem = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        bnOk = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("HoldingTypeDecissionDialog"));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        partTypeDecissionDisp.setFont(new java.awt.Font("Dialog", 1, 14));
        partTypeDecissionDisp.setForeground(new java.awt.Color(51, 51, 255));
        partTypeDecissionDisp.setAlignmentX(5.0F);
        jPanel4.add(partTypeDecissionDisp);
        jPanel1.add(jPanel4);
        jPanel3.setLayout(new java.awt.GridBagLayout());
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonGroup1.add(radSingle);
        radSingle.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SinglePartItem"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(radSingle, gridBagConstraints);
        buttonGroup1.add(radMultiwithInd);
        radMultiwithInd.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("MultiPartItem(withIndividualTitlesforeachpart)"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel3.add(radMultiwithInd, gridBagConstraints);
        buttonGroup1.add(radMultipart);
        radMultipart.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("MultiPartItem"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(radMultipart, gridBagConstraints);
        buttonGroup1.add(radSerialItem);
        radSerialItem.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SerialItem"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(radSerialItem, gridBagConstraints);
        jPanel1.add(jPanel3);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bnOk.setMnemonic('o');
        bnOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnOkActionPerformed(evt);
            }
        });
        jPanel2.add(bnOk);
        bnCancel.setMnemonic('c');
        bnCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(bnCancel);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        this.dispose();
    }

    public String getSingleItemVolumeXML() {
        String xmlStr = "";
        try {
            org.jdom.Element root = new org.jdom.Element("OperationId");
            root.setAttribute("no", "17");
            org.jdom.Element ele = new org.jdom.Element("CatalogueRecordId");
            ele.setText(this.cataloguerecordid.trim());
            root.addContent(ele);
            ele = new org.jdom.Element("OwnLibId");
            ele.setText(this.ownlibid.trim());
            root.addContent(ele);
            ele = new org.jdom.Element("entryId");
            ele.setText(newgen.presentation.NewGenMain.getAppletInstance().getEntryID());
            root.addContent(ele);
            ele = new org.jdom.Element("entryLibId");
            ele.setText(newgen.presentation.NewGenMain.getAppletInstance().getLibraryID());
            root.addContent(ele);
            org.jdom.Document doc = new org.jdom.Document(root);
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            xmlStr = out.outputString(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlStr;
    }

    private void bnOkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int flag = -1;
            if (radSingle.isSelected() || radMultiwithInd.isSelected()) {
                newgen.presentation.holdings.HoldingsDialog hold = new newgen.presentation.holdings.HoldingsDialog(false);
                hold.setHtClassificationNumbers(getHtClassificationNumbers());
                hold.setCatIdOwnId(this.cataloguerecordid.trim(), this.ownlibid.trim());
                String xmlSingle = this.getSingleItemVolumeXML();
                xmlSingle = newgen.presentation.component.ServletConnector.getInstance().sendRequest("HoldingsServlet", xmlSingle);
                org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                org.jdom.Document docsingle = sax.build(new java.io.StringReader(xmlSingle.trim()));
                org.jdom.Element rootsng = docsingle.getRootElement();
                if (rootsng.getChildText("status").equals("success")) {
                    String volid = rootsng.getChildText("VolumeId").trim();
                    hold.setSingleVolumeId(volid, this.cataloguerecordid, this.ownlibid);
                    hold.setMaterialTypeId(this.getMaterialTypeId());
                    hold.setLibraryName(this.getLibraryName());
                    hold.setSize(565, 265);
                    hold.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(565, 265));
                    hold.show();
                    this.setVisible(false);
                    this.dispose();
                } else if (rootsng.getChildText("status").equals("error")) {
                    new javax.swing.JOptionPane().showMessageDialog(this, "Please Contact Vendor", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else if (radSerialItem.isSelected() || radMultipart.isSelected()) {
                newgen.presentation.holdings.HoldingsDialog hold = new newgen.presentation.holdings.HoldingsDialog(true);
                hold.setHtClassificationNumbers(getHtClassificationNumbers());
                if (getHtInitialEnumeration() != null) {
                    hold.setHtInitialEnumeration(getHtInitialEnumeration());
                }
                hold.setCatIdOwnId(this.cataloguerecordid.trim(), this.ownlibid.trim());
                hold.setCaptionData(this.getCaptionData());
                hold.setMaterialTypeId(this.getMaterialTypeId());
                hold.setLibraryName(this.getLibraryName());
                hold.setSize(600, 500);
                hold.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(700, 500));
                hold.show();
            } else {
                newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage(this.statusDisp.trim());
            }
            this.setVisible(false);
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    /** Getter for property cataloguerecordid.
     * @return Value of property cataloguerecordid.
     *
     */
    public java.lang.String getCataloguerecordid() {
        return cataloguerecordid;
    }

    /** Setter for property cataloguerecordid.
     * @param cataloguerecordid New value of property cataloguerecordid.
     *
     */
    public void setCataloguerecordid(java.lang.String cataloguerecordid) {
        this.cataloguerecordid = cataloguerecordid;
    }

    /** Getter for property ownlibid.
     * @return Value of property ownlibid.
     *
     */
    public java.lang.String getOwnlibid() {
        return ownlibid;
    }

    /** Setter for property ownlibid.
     * @param ownlibid New value of property ownlibid.
     *
     */
    public void setOwnlibid(java.lang.String ownlibid) {
        this.ownlibid = ownlibid;
    }

    public String getItemTypeDecissionXML() {
        String xmlStr = "";
        try {
            org.jdom.Element root = new org.jdom.Element("OperationId");
            root.setAttribute("no", "18");
            org.jdom.Element ele = new org.jdom.Element("catid");
            ele.setText(this.cataloguerecordid);
            root.addContent(ele);
            ele = new org.jdom.Element("ownLibid");
            ele.setText(this.ownlibid);
            root.addContent(ele);
            org.jdom.Document doc = new org.jdom.Document(root);
            xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlStr;
    }

    public void setCatOwnLib(String catId, String ownLib) {
        cataloguerecordid = catId;
        ownlibid = ownLib;
    }

    public String getCaptionData() {
        String xml = "";
        try {
            org.jdom.Element root = new org.jdom.Element("OperationId");
            root.setAttribute("no", "1");
            org.jdom.Element catId = new org.jdom.Element("CatId");
            catId.setText(this.cataloguerecordid.trim());
            org.jdom.Element ownlibId = new org.jdom.Element("OwnLibId");
            ownlibId.setText(this.ownlibid.trim());
            root.addContent(catId);
            root.addContent(ownlibId);
            xml = (new org.jdom.output.XMLOutputter().outputString(new org.jdom.Document(root)));
            xml = newgen.presentation.component.ServletConnector.getInstance().sendRequest("HoldingsServlet", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    /** Getter for property libraryName.
     * @return Value of property libraryName.
     *
     */
    public java.lang.String getLibraryName() {
        return libraryName;
    }

    /** Setter for property libraryName.
     * @param libraryName New value of property libraryName.
     *
     */
    public void setLibraryName(java.lang.String libraryName) {
        this.libraryName = libraryName;
    }

    /** Getter for property bibliographicLevel.
     * @return Value of property bibliographicLevel.
     *
     */
    public java.lang.String getBibliographicLevel() {
        return bibliographicLevel;
    }

    /** Setter for property bibliographicLevel.
     * @param bibliographicLevel New value of property bibliographicLevel.
     *
     */
    public void setBibliographicLevel(java.lang.String bibliographicLevel) {
        if (bibliographicLevel.trim().equals("Serial") || bibliographicLevel.trim().startsWith("Serial")) {
            radSerialItem.setSelected(true);
        }
        this.bibliographicLevel = bibliographicLevel;
    }

    /** Getter for property materialTypeId.
     * @return Value of property materialTypeId.
     *
     */
    public java.lang.String getMaterialTypeId() {
        return materialTypeId;
    }

    /** Setter for property materialTypeId.
     * @param materialTypeId New value of property materialTypeId.
     *
     */
    public void setMaterialTypeId(java.lang.String materialTypeId) {
        this.materialTypeId = materialTypeId;
    }

    /** Getter for property itemType.
     * @return Value of property itemType.
     *
     */
    public java.lang.String getItemType() {
        return itemType;
    }

    /** Setter for property itemType.
     * @param itemType New value of property itemType.
     *
     */
    public void setItemType(java.lang.String itemType) {
        this.itemType = itemType;
    }

    public void setCatalogueDetails(String catalogueRecordId, String ownerLibraryId, String libraryName) {
        this.cataloguerecordid = catalogueRecordId;
        this.ownlibid = ownerLibraryId;
        this.libraryName = libraryName;
        if (multiVolume) {
            radMultipart.setSelected(true);
            partTypeDecissionDisp.setText("System identified the document as Multi-Volume, please click on OK to proceed");
        }
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("CatalogueRecordId", catalogueRecordId);
        ht.put("OwnerLibraryId", ownerLibraryId);
        newgen.presentation.component.NewGenXMLGenerator xmlgen = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        String xmlreq = xmlgen.buildXMLDocument("19", ht);
        System.out.println("xmlreq = " + xmlreq);
        String xmlres = newgen.presentation.component.ServletConnector.getInstance().sendRequest("HoldingsServlet", xmlreq);
        System.out.println("xmlres = " + xmlres);
        java.util.Hashtable htres = xmlgen.parseXMLDocument(xmlres);
        if (htres.get("HoldingsType").toString().equals("SerialItem") || htres.get("HoldingsType").toString().equals("MultiPartItem")) {
            String captionData = getCaptionData();
            newgen.presentation.holdings.HoldingsDialog hold = new newgen.presentation.holdings.HoldingsDialog(true);
            hold.setHtClassificationNumbers(getHtClassificationNumbers());
            hold.setCatIdOwnId(this.cataloguerecordid.trim(), this.ownlibid.trim());
            hold.setCaptionData(captionData);
            hold.setLibraryName(this.getLibraryName());
            hold.setSize(700, 550);
            hold.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(700, 550));
            if (getHtInitialEnumeration() != null) {
                hold.setHtInitialEnumeration(getHtInitialEnumeration());
            }
            hold.show();
        } else if (htres.get("HoldingsType").toString().equals("SinglePartItem")) {
            newgen.presentation.holdings.HoldingsDialog hold = new newgen.presentation.holdings.HoldingsDialog(false);
            hold.setHtClassificationNumbers(getHtClassificationNumbers());
            hold.setCatIdOwnId(this.cataloguerecordid.trim(), this.ownlibid.trim());
            hold.setCaptionData(this.getCaptionData());
            String xmlSingle = this.getSingleItemVolumeXML();
            xmlSingle = newgen.presentation.component.ServletConnector.getInstance().sendRequest("HoldingsServlet", xmlSingle);
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document docsingle = null;
            try {
                docsingle = sax.build(new java.io.StringReader(xmlSingle.trim()));
            } catch (Exception exp) {
            }
            org.jdom.Element rootsng = docsingle.getRootElement();
            if (rootsng.getChildText("status").equals("success")) {
                String volid = rootsng.getChildText("VolumeId").trim();
                hold.setSingleVolumeId(volid, this.cataloguerecordid, this.ownlibid);
                hold.setLibraryName(this.getLibraryName());
                hold.setSize(565, 265);
                hold.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(565, 265));
                hold.show();
                this.setVisible(false);
                this.dispose();
            } else if (rootsng.getChildText("status").equals("error")) {
                new javax.swing.JOptionPane().showMessageDialog(this, "Please Contact Vendor", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } else if (htres.get("HoldingsType").toString().equals("UnableToDetermine")) {
            radSingle.setSelected(true);
            show();
        }
    }

    /** Getter for property multiVolume.
     * @return Value of property multiVolume.
     *
     */
    public boolean isMultiVolume() {
        return multiVolume;
    }

    /** Setter for property multiVolume.
     * @param multiVolume New value of property multiVolume.
     *
     */
    public void setMultiVolume(boolean multiVolume) {
        this.multiVolume = multiVolume;
    }

    /** Getter for property htInitialEnumeration.
     * @return Value of property htInitialEnumeration.
     *
     */
    public java.util.Hashtable getHtInitialEnumeration() {
        return htInitialEnumeration;
    }

    /** Setter for property htInitialEnumeration.
     * @param htInitialEnumeration New value of property htInitialEnumeration.
     *
     */
    public void setHtInitialEnumeration(java.util.Hashtable htInitialEnumeration) {
        this.htInitialEnumeration = htInitialEnumeration;
    }

    /** Getter for property htClassificationNumbers.
     * @return Value of property htClassificationNumbers.
     *
     */
    public java.util.Hashtable getHtClassificationNumbers() {
        return htClassificationNumbers;
    }

    /** Setter for property htClassificationNumbers.
     * @param htClassificationNumbers New value of property htClassificationNumbers.
     *
     */
    public void setHtClassificationNumbers(java.util.Hashtable htClassificationNumbers) {
        this.htClassificationNumbers = htClassificationNumbers;
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnOk;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JLabel partTypeDecissionDisp;

    private javax.swing.JRadioButton radMultipart;

    private javax.swing.JRadioButton radMultiwithInd;

    private javax.swing.JRadioButton radSerialItem;

    private javax.swing.JRadioButton radSingle;

    private String cataloguerecordid = "";

    private String ownlibid = "";

    private String materialTypeId = "", libraryName = "", bibliographicLevel = "";

    private String itemType = "";

    private String statusDisp = "";

    private boolean multiVolume = false;

    private java.util.Hashtable htInitialEnumeration;

    private java.util.Hashtable htClassificationNumbers;
}
