package newgen.presentation.circulation;

/**
 *
 * @author  vasu praveen
 */
public class ReservationPlacing extends javax.swing.JPanel {

    private static ReservationPlacing instance = null;

    private javax.swing.table.DefaultTableModel defTbModel = null;

    private newgen.presentation.NewGenMain newGenMain = null;

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private newgen.presentation.component.ServletConnector servletConnector = null;

    private newgen.presentation.component.Utility utility = null;

    private int volSerID = 0;

    private boolean serial = false;

    /** Creates new form CheckoutToBinder */
    private ReservationPlacing() {
        newGenMain = newgen.presentation.NewGenMain.getAppletInstance();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        servletConnector = newgen.presentation.component.ServletConnector.getInstance();
        utility = newgen.presentation.component.Utility.getInstance();
        initComponents();
        initComps();
        pl10.add(pl12);
        pl10.add(pl13, java.awt.BorderLayout.LINE_END);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        tPatronID.grabFocus();
    }

    private void initComps() {
    }

    protected void updateDatabase() {
        if (validateScreen()) {
            org.jdom.Element root = new org.jdom.Element("OperationID");
            root.setAttribute("no", "1");
            org.jdom.Element element = new org.jdom.Element("LibraryID");
            element.setText(newGenMain.getLibraryID());
            root.addContent(element);
            element = new org.jdom.Element("MaterialType");
            element.setText("" + cMatType.getSelectedItem());
            root.addContent(element);
            element = new org.jdom.Element("PatronID");
            element.setText(tPatronID.getText());
            root.addContent(element);
            if (serial) {
                element = new org.jdom.Element("Serial");
                element.setText("Y");
                root.addContent(element);
                element = new org.jdom.Element("SerialID");
                element.setText("" + volSerID);
                root.addContent(element);
            } else {
                element = new org.jdom.Element("Serial");
                element.setText("N");
                root.addContent(element);
                element = new org.jdom.Element("VolumeID");
                element.setText("" + volSerID);
                root.addContent(element);
            }
            utility.addLoginDetailsToTheRootElement(root);
            String xmlStr = newGenXMLGenerator.buildXMLDocument(root);
            java.util.Hashtable htReturn = newGenXMLGenerator.parseXMLDocument(servletConnector.sendRequest("ReservationPlacing", xmlStr));
            if (htReturn != null && htReturn.size() > 0) {
                if (utility.getTestedString("" + htReturn.get("Eligible")).equals("NotDefined")) {
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Privelegeforpatroncategorynotyetdefined"));
                    tPatronID.grabFocus();
                } else if (utility.getTestedString("" + htReturn.get("Eligible")).equals("MaximumReservations")) {
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("CannotReserveWillExceedMaximumNoOfReservations"));
                    bSearchCatalogue.grabFocus();
                } else if (utility.getTestedString("" + htReturn.get("Eligible")).equals("N")) {
                    newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Categoryofpatronisnotpermittedtoborroowthisdocument"));
                    bSearchCatalogue.grabFocus();
                } else if (utility.getTestedString("" + htReturn.get("Eligible")).equals("Y")) {
                    if (utility.getTestedString("" + htReturn.get("DocumentAvailable")).equals("Y")) {
                        newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Documentavailableinlibrary\nCannotreserveadocumentthatisavailable"));
                        bSearchCatalogue.grabFocus();
                    } else if (utility.getTestedString("" + htReturn.get("DocumentAvailable")).equals("I")) {
                        newGenMain.showInformationMessage(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoHoldingsAvailable"));
                        bSearchCatalogue.grabFocus();
                    } else if (utility.getTestedString("" + htReturn.get("ReservationExists")).equals("Y")) {
                        newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Reservationalreadydoneforthisdocument"));
                        bSearchCatalogue.grabFocus();
                    } else if (utility.getTestedString("" + htReturn.get("ReservationQueueFull")).equals("1")) {
                        newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Nodocumentsexistsmatchingyoursearch"));
                        bSearchCatalogue.grabFocus();
                    } else if (utility.getTestedString("" + htReturn.get("ReservationQueueFull")).equals("2")) {
                        newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Reservationqueueisfull"));
                        bSearchCatalogue.grabFocus();
                    } else if (utility.getTestedString("" + htReturn.get("ReservationQueueFull")).equals("3")) {
                        if (utility.getTestedString("" + htReturn.get("HoldsSimilarDocument")).equals("true")) {
                            newGenMain.showInformationMessage(newGenMain.getMyResource().getString("CannotReserveAsThePatronHoldsTheSameDocument"));
                        } else {
                            if (utility.getTestedString("" + htReturn.get("Success")).equals("Y")) {
                                refreshScreen();
                                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Reservationplacedsuccessfully\nQueuenumber:") + htReturn.get("QueueNumber"));
                            } else {
                                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Problemoccuredwhilepostingtodatabase"));
                            }
                        }
                    }
                }
            } else {
                newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Problemoccuredwhilepostingtodatabase"));
            }
        }
    }

    private boolean validateScreen() {
        if (tName.getText().trim().length() == 0) {
            if (tPatronID.getText().trim().length() > 0) {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("FillvalidpatronID"));
            } else {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("FillpatronID"));
            }
            tPatronID.grabFocus();
            return false;
        } else {
            return true;
        }
    }

    protected void refreshScreen() {
        tPatronID.setText("");
        tName.setText("");
        cMatType.removeAllItems();
        tTitle.setText("");
        tAuthor.setText("");
        tPublisher.setText("");
        tSeries.setText("");
        tISBN.setText("");
        tEdition.setText("");
        tPatronID.grabFocus();
    }

    private void getPatronDetails() {
        java.util.Hashtable ht = utility.getPatronDetails(newGenMain.getLibraryID(), tPatronID.getText().trim());
        if (ht.get("PatronName") != null) {
            tName.setText("" + ht.get("PatronName"));
            tPatronID.setText(utility.getTestedString(ht.get("PatronId")));
            bSearchCatalogue.grabFocus();
        } else {
            tName.setText("");
            newGenMain.showInformationMessage(newGenMain.getMyResource().getString("Norecordfound-matchingthepatronID"));
            tPatronID.setText("");
            tPatronID.grabFocus();
        }
    }

    private void getMaterialTypes(java.util.Vector vector) {
        System.out.println("vector : " + vector);
        cMatType.setModel(new javax.swing.DefaultComboBoxModel(new java.util.TreeSet(vector).toArray()));
    }

    public void reloadLocales() {
        lPatronID.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PatronID"));
        lName.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Name"));
        bSearchCatalogue.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Searchcatalogue"));
        lTitle.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title"));
        lAuthor.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Author"));
        lPublisher.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publisher"));
        lEdition.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edition"));
        lISBN.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ISBN"));
        lSeries.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Series"));
        lMatType.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Materialtype"));
        lVolume.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Volume"));
        bGo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bSearchCatalogue.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchCatalogue"));
        tPatronID.grabFocus();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pl10 = new javax.swing.JPanel();
        pl12 = new javax.swing.JPanel();
        lPatronID = new javax.swing.JLabel();
        tPatronID = new newgen.presentation.UnicodeTextField();
        lName = new javax.swing.JLabel();
        tName = new newgen.presentation.UnicodeTextField();
        bGo = new javax.swing.JButton();
        pl13 = new javax.swing.JPanel();
        bSearchCatalogue = new javax.swing.JButton();
        pl11 = new javax.swing.JPanel();
        lTitle = new javax.swing.JLabel();
        tTitle = new newgen.presentation.UnicodeTextField();
        lAuthor = new javax.swing.JLabel();
        tAuthor = new newgen.presentation.UnicodeTextField();
        lPublisher = new javax.swing.JLabel();
        tPublisher = new newgen.presentation.UnicodeTextField();
        lEdition = new javax.swing.JLabel();
        tEdition = new newgen.presentation.UnicodeTextField();
        lISBN = new javax.swing.JLabel();
        tISBN = new newgen.presentation.UnicodeTextField();
        lSeries = new javax.swing.JLabel();
        tSeries = new newgen.presentation.UnicodeTextField();
        lMatType = new javax.swing.JLabel();
        cMatType = new javax.swing.JComboBox();
        lVolume = new javax.swing.JLabel();
        tVolume = new newgen.presentation.UnicodeTextField();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        pl10.setLayout(new java.awt.BorderLayout());
        pl12.setLayout(new java.awt.GridBagLayout());
        pl12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lPatronID.setForeground(new java.awt.Color(170, 0, 0));
        lPatronID.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PatronID"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lPatronID, gridBagConstraints);
        tPatronID.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tPatronIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl12.add(tPatronID, gridBagConstraints);
        lName.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Name"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl12.add(lName, gridBagConstraints);
        tName.setColumns(30);
        tName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl12.add(tName, gridBagConstraints);
        bGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/go.jpg")));
        bGo.setMnemonic('g');
        bGo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Go"));
        bGo.setPreferredSize(new java.awt.Dimension(50, 21));
        bGo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pl12.add(bGo, gridBagConstraints);
        pl10.add(pl12, java.awt.BorderLayout.CENTER);
        pl13.setLayout(new java.awt.GridBagLayout());
        pl13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        pl13.add(bSearchCatalogue, gridBagConstraints);
        pl10.add(pl13, java.awt.BorderLayout.EAST);
        add(pl10);
        pl11.setLayout(new java.awt.GridBagLayout());
        pl11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lTitle.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Title"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lTitle, gridBagConstraints);
        tTitle.setColumns(25);
        tTitle.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(tTitle, gridBagConstraints);
        lAuthor.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Author"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lAuthor, gridBagConstraints);
        tAuthor.setColumns(25);
        tAuthor.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(tAuthor, gridBagConstraints);
        lPublisher.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Publisher"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lPublisher, gridBagConstraints);
        tPublisher.setColumns(25);
        tPublisher.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(tPublisher, gridBagConstraints);
        lEdition.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edition"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lEdition, gridBagConstraints);
        tEdition.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(tEdition, gridBagConstraints);
        lISBN.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ISBN"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lISBN, gridBagConstraints);
        tISBN.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(tISBN, gridBagConstraints);
        lSeries.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Series"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lSeries, gridBagConstraints);
        tSeries.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(tSeries, gridBagConstraints);
        lMatType.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Materialtype"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lMatType, gridBagConstraints);
        cMatType.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(cMatType, gridBagConstraints);
        lVolume.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Volume"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        pl11.add(lVolume, gridBagConstraints);
        tVolume.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl11.add(tVolume, gridBagConstraints);
        add(pl11);
    }

    private void bGoActionPerformed(java.awt.event.ActionEvent evt) {
        if (tPatronID.getText().trim().length() > 0) {
            getPatronDetails();
        } else {
            newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("FillpatronID"));
            tPatronID.grabFocus();
        }
    }

    private void bSearchCatalogueActionPerformed(java.awt.event.ActionEvent evt) {
        if (tName.getText().trim().length() > 0) {
            newgen.presentation.cataloguing.SearchCatalogueDialog searchCatalogue = new newgen.presentation.cataloguing.SearchCatalogueDialog(newgen.presentation.cataloguing.SearchCatalogueDialog.HOLDINGS_SELECTION_MODE);
            System.out.println("ret code from scd : " + searchCatalogue.getRetcode());
            if (searchCatalogue.getRetcode() == 0) {
                System.out.println("scd vol ser id : " + searchCatalogue.getVolumeSerialId());
                System.out.println("scd is serial : " + searchCatalogue.isSerial());
                volSerID = Integer.parseInt(searchCatalogue.getVolumeSerialId());
                serial = searchCatalogue.isSerial();
                java.util.Hashtable htbasic = searchCatalogue.getHtbasic();
                System.out.println("htBasic: " + htbasic);
                tTitle.setText(htbasic.get("Title").toString());
                tAuthor.setText(htbasic.get("Author").toString());
                tPublisher.setText(htbasic.get("Publisher").toString());
                tSeries.setText(htbasic.get("Series").toString());
                tISBN.setText(htbasic.get("ISBN/ISSN").toString());
                tEdition.setText(htbasic.get("Edition").toString());
                tVolume.setText(searchCatalogue.getVolumeDetails());
                getMaterialTypes(searchCatalogue.getVMaterialTypes());
            } else {
                return;
            }
        } else {
            if (tPatronID.getText().trim().length() > 0) {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("EntervalidpatronID"));
                tPatronID.grabFocus();
            } else {
                newGenMain.showInsufficientDataDialog(newGenMain.getMyResource().getString("EnterpatronID"));
                tPatronID.grabFocus();
            }
        }
    }

    private void bEditActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void tPatronIDActionPerformed(java.awt.event.ActionEvent evt) {
        bGo.doClick();
    }

    public static ReservationPlacing getInstance() {
        instance = new ReservationPlacing();
        return instance;
    }

    private javax.swing.JButton bGo;

    private javax.swing.JButton bSearchCatalogue;

    private javax.swing.JComboBox cMatType;

    private javax.swing.JLabel lAuthor;

    private javax.swing.JLabel lEdition;

    private javax.swing.JLabel lISBN;

    private javax.swing.JLabel lMatType;

    private javax.swing.JLabel lName;

    private javax.swing.JLabel lPatronID;

    private javax.swing.JLabel lPublisher;

    private javax.swing.JLabel lSeries;

    private javax.swing.JLabel lTitle;

    private javax.swing.JLabel lVolume;

    private javax.swing.JPanel pl10;

    private javax.swing.JPanel pl11;

    private javax.swing.JPanel pl12;

    private javax.swing.JPanel pl13;

    private newgen.presentation.UnicodeTextField tAuthor;

    private newgen.presentation.UnicodeTextField tEdition;

    private newgen.presentation.UnicodeTextField tISBN;

    private newgen.presentation.UnicodeTextField tName;

    private newgen.presentation.UnicodeTextField tPatronID;

    private newgen.presentation.UnicodeTextField tPublisher;

    private newgen.presentation.UnicodeTextField tSeries;

    private newgen.presentation.UnicodeTextField tTitle;

    private newgen.presentation.UnicodeTextField tVolume;
}
