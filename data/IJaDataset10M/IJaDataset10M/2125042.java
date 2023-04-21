package eof.techProcessing;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.prefs.Preferences;
import javax.swing.table.DefaultTableModel;
import newgenlib.marccomponent.conversion.Converter;
import org.apache.poi.hssf.record.DatRecord;
import org.hibernate.Session;
import reports.utility.CataloguePrinter;
import reports.utility.Utility;
import reports.utility.datamodel.technicalprocessing.CATALOGUERECORD;
import reports.utility.datamodel.technicalprocessing.CATALOGUERECORD_MANAGER;
import reports.utility.datamodel.technicalprocessing.CAT_VOLUME;
import reports.utility.datamodel.technicalprocessing.CAT_VOLUME_KEY;
import reports.utility.datamodel.technicalprocessing.CAT_VOLUME_MANAGER;
import reports.utility.datamodel.technicalprocessing.SEARCHABLE_CATALOGUERECORD;
import reports.utility.datamodel.technicalprocessing.SEARCHABLE_CATALOGUERECORD_KEY;
import reports.utility.datamodel.technicalprocessing.SEARCHABLE_CATALOGUERECORD_MANAGER;
import reports.utility.datamodel.technicalprocessing.DOCUMENT;
import reports.utility.datamodel.technicalprocessing.DOCUMENT_KEY;
import tools.HibernateUtil;
import tools.StringProcessor;

/**
 *
 * @author  Administrator
 */
public class CatalogueCardPrinting extends javax.swing.JInternalFrame {

    /** Creates new form CatalogueCardPrinting */
    public CatalogueCardPrinting() {
        initComponents();
        tfFrom.setValue(new Date());
        tfTo.setValue(new Date());
        String[] cols = { "", "Title", "Main Author", "Publisher", "Edition", "Selected", "" };
        dtmCat = new DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int row, int column) {
                if (column == 5) return true; else return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        tableCats.setModel(dtmCat);
        setSize(600, 500);
        show();
        tableCats.getColumnModel().getColumn(0).setMinWidth(0);
        tableCats.getColumnModel().getColumn(0).setPreferredWidth(0);
        tableCats.getColumnModel().getColumn(6).setMinWidth(0);
        tableCats.getColumnModel().getColumn(6).setPreferredWidth(0);
        tableCats.getColumnModel().getColumn(1).setPreferredWidth(193);
        tableCats.getColumnModel().getColumn(2).setPreferredWidth(165);
        tableCats.getColumnModel().getColumn(3).setPreferredWidth(75);
        tableCats.getColumnModel().getColumn(4).setPreferredWidth(75);
        tableCats.getColumnModel().getColumn(5).setPreferredWidth(52);
        tableCats.setFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.PLAIN, 12));
        spClassificationNo.setValue(new Integer(1));
        spMainEntry.setValue(new Integer(1));
        spOtherAuthors.setValue(new Integer(1));
        spSeries.setValue(new Integer(1));
        spSubject.setValue(new Integer(1));
        spTitle.setValue(new Integer(1));
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        bnPrint = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        bnClose = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfFrom = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        tfTo = new javax.swing.JFormattedTextField();
        bnGoDate = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        accessionno = new javax.swing.JTextField();
        bnAccessionNo = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCats = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        chbMainEntry = new javax.swing.JCheckBox();
        spMainEntry = new javax.swing.JSpinner();
        chbTitle = new javax.swing.JCheckBox();
        spTitle = new javax.swing.JSpinner();
        chbSubject = new javax.swing.JCheckBox();
        spSubject = new javax.swing.JSpinner();
        chbClassNo = new javax.swing.JCheckBox();
        spClassificationNo = new javax.swing.JSpinner();
        chbSeries = new javax.swing.JCheckBox();
        spSeries = new javax.swing.JSpinner();
        chbOtherAuthors = new javax.swing.JCheckBox();
        spOtherAuthors = new javax.swing.JSpinner();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Print Catalogue cards");
        bnPrint.setText("Print");
        bnPrint.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnPrintActionPerformed(evt);
            }
        });
        jPanel1.add(bnPrint);
        bnCancel.setText("Cancel");
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(bnCancel);
        bnClose.setText("Close");
        bnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(bnClose);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jPanel3.setLayout(new java.awt.GridBagLayout());
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 100));
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Date wise");
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton1, new java.awt.GridBagConstraints());
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Accession number");
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton2, new java.awt.GridBagConstraints());
        jPanel2.add(jPanel3);
        jPanel5.setLayout(new java.awt.CardLayout());
        jPanel6.setLayout(new java.awt.GridBagLayout());
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setText("From:");
        jPanel6.add(jLabel1, new java.awt.GridBagConstraints());
        tfFrom.setColumns(15);
        jPanel6.add(tfFrom, new java.awt.GridBagConstraints());
        jLabel2.setText("To:");
        jPanel6.add(jLabel2, new java.awt.GridBagConstraints());
        tfTo.setColumns(15);
        jPanel6.add(tfTo, new java.awt.GridBagConstraints());
        bnGoDate.setText("Go");
        bnGoDate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnGoDateActionPerformed(evt);
            }
        });
        jPanel6.add(bnGoDate, new java.awt.GridBagConstraints());
        jPanel5.add(jPanel6, "date");
        jLabel3.setText("Accession number");
        jPanel8.add(jLabel3);
        accessionno.setColumns(20);
        accessionno.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accessionnoActionPerformed(evt);
            }
        });
        jPanel8.add(accessionno);
        bnAccessionNo.setText("Go");
        bnAccessionNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnAccessionNoActionPerformed(evt);
            }
        });
        jPanel8.add(bnAccessionNo);
        jPanel5.add(jPanel8, "accession number");
        jPanel2.add(jPanel5);
        jPanel4.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setFont(new java.awt.Font("Arial Unicode MS", 0, 12));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(454, 300));
        tableCats.setFont(new java.awt.Font("Arial Unicode MS", 0, 12));
        tableCats.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tableCats);
        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel2.add(jPanel4);
        jPanel7.setLayout(new java.awt.GridBagLayout());
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Select type of Cards and number of copies"));
        jPanel7.setPreferredSize(new java.awt.Dimension(318, 100));
        chbMainEntry.setSelected(true);
        chbMainEntry.setText("Main Entry");
        chbMainEntry.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chbMainEntry.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(chbMainEntry, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(spMainEntry, gridBagConstraints);
        chbTitle.setSelected(true);
        chbTitle.setText("Title");
        chbTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chbTitle.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(chbTitle, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(spTitle, gridBagConstraints);
        chbSubject.setSelected(true);
        chbSubject.setText("Subject");
        chbSubject.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chbSubject.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(chbSubject, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(spSubject, gridBagConstraints);
        chbClassNo.setSelected(true);
        chbClassNo.setText("Classification no.");
        chbClassNo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chbClassNo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(chbClassNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(spClassificationNo, gridBagConstraints);
        chbSeries.setSelected(true);
        chbSeries.setText("Series");
        chbSeries.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chbSeries.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(chbSeries, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(spSeries, gridBagConstraints);
        chbOtherAuthors.setSelected(true);
        chbOtherAuthors.setText("Other authors");
        chbOtherAuthors.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chbOtherAuthors.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(chbOtherAuthors, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(spOtherAuthors, gridBagConstraints);
        jPanel2.add(jPanel7);
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void accessionnoActionPerformed(java.awt.event.ActionEvent evt) {
        bnAccessionNo.doClick();
        accessionno.setText("");
        accessionno.setFocusable(true);
    }

    public String getPrice(String libId, String accNo) {
        try {
            java.sql.Connection con = reports.utility.database.ConnectionPoolFactory.getInstance().getConnectionPool().getConnection();
            java.sql.Statement stmt = con.createStatement();
            String query = "select price, currency_code, discount, miscellaneous from acq_request_am_copy where accession_library_id = " + libId + " and accession_no = '" + accNo + "'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            String cost = "";
            while (rs.next()) {
                double price = rs.getDouble(1);
                String currencyCode = rs.getString(2);
                cost = price + " " + currencyCode;
            }
            rs.close();
            stmt.close();
            con.close();
            return cost;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String[] getDefaultCustomDetials(String customDet) {
        String[] stx = new String[6];
        System.out.println("customDet=" + customDet);
        if (customDet != null && !customDet.trim().equals("") && customDet.startsWith("<")) {
            System.out.println("customDet=" + customDet);
            org.jdom.Document doc = null;
            try {
                doc = new org.jdom.input.SAXBuilder().build(new java.io.StringReader(customDet));
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            if (doc != null) {
                List li = doc.getRootElement().getChildren("Field");
                for (int i = 0; i < li.size(); i++) {
                    org.jdom.Element ele = (org.jdom.Element) li.get(i);
                    String type = ele.getAttributeValue("NAME");
                    String typeDis = ele.getAttributeValue("DISPLAYNAME");
                    if (type != null) {
                        if (type.equals("Invoice No")) {
                            stx[1] = ele.getText();
                        } else if (type.equals("Invoice Date")) {
                            stx[2] = ele.getText();
                        } else if (type.equals("Acquisition Mode")) {
                            stx[3] = ele.getText();
                        } else if (type.equals("Actual Price")) {
                            stx[4] = ele.getText();
                        } else if (type.equals("Vendor Name")) {
                            stx[0] = ele.getText();
                        } else {
                            if (stx[5] != null && !stx[5].equals("")) {
                                stx[5] += "; " + typeDis + ": " + ele.getText();
                            } else {
                                stx[5] = typeDis + ": " + ele.getText();
                            }
                        }
                    }
                }
            }
        }
        return stx;
    }

    private void bnAccessionNoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int status = 0;
            Session session = HibernateUtil.getSessionFactory().openSession();
            StringProcessor sp = StringProcessor.getInstance();
            String accNo = accessionno.getText();
            String libId = reports.utility.StaticValues.getInstance().getLoginLibraryId();
            Converter conv = new Converter();
            Hashtable ht = new Hashtable();
            java.util.List list = session.createQuery("from DOCUMENT as d where d.primaryKey.library_id=" + libId + " and d.barcode like '" + accNo + "'").list();
            System.out.println("the size of the list=" + list.size());
            for (int i = 0; i < list.size(); i++) {
                DOCUMENT doc = (DOCUMENT) list.get(i);
                Integer volId = doc.getVolume_id();
                if (volId != null) {
                    CAT_VOLUME_KEY catKey = new CAT_VOLUME_KEY();
                    catKey.setVolume_id(volId);
                    CAT_VOLUME_MANAGER catVolManager = new CAT_VOLUME_MANAGER();
                    CAT_VOLUME cat = catVolManager.load(session, catKey);
                    Integer catRecId = cat.getCataloguerecordid();
                    Integer ownerLibId = cat.getOwner_library_id();
                    SEARCHABLE_CATALOGUERECORD_KEY searchKey = new SEARCHABLE_CATALOGUERECORD_KEY();
                    searchKey.setCataloguerecordid(catRecId);
                    searchKey.setOwner_library_id(ownerLibId);
                    SEARCHABLE_CATALOGUERECORD searchCat = (SEARCHABLE_CATALOGUERECORD) session.load(SEARCHABLE_CATALOGUERECORD.class, searchKey);
                    for (int j = 0; j < dtmCat.getRowCount(); j++) {
                        String str1 = (String) dtmCat.getValueAt(j, 0);
                        String str2 = catRecId + "_" + ownerLibId;
                        if (str1.equals(str2)) status = 1;
                    }
                    if (status == 0) {
                        String isorec = searchCat.getWholecataloguerecord();
                        ht = conv.getDetails(isorec);
                        Object[] r = new Object[7];
                        r[0] = catRecId + "_" + ownerLibId;
                        r[1] = sp.verifyStringTrim(ht.get("TITLE"));
                        System.out.println("title = " + ht.get("TITLE"));
                        r[2] = sp.verifyStringTrim(ht.get("AUTHOR"));
                        r[3] = sp.verifyStringTrim(ht.get("PUBLISHER"));
                        r[4] = sp.verifyStringTrim(ht.get("EDITION"));
                        r[5] = new Boolean(true);
                        Hashtable htRow = new Hashtable();
                        if (!sp.verifyStringTrim(ht.get("AUTHOR")).equals("")) {
                            htRow.put("MAIN_ENTRY", sp.verifyStringTrim(ht.get("AUTHOR")));
                        }
                        if (!sp.verifyStringTrim(ht.get("TITLE_SOR")).equals("")) {
                            htRow.put("Title_SOR", sp.verifyStringTrim(ht.get("TITLE_SOR")));
                        }
                        if (!sp.verifyStringTrim(ht.get("TITLE")).equals("")) {
                            htRow.put("TITLE", sp.verifyStringTrim(ht.get("TITLE")));
                        }
                        if (!sp.verifyStringTrim(ht.get("PARALLEL_TITLE_STR")).equals("")) {
                            htRow.put("parallel_title", sp.verifyStringTrim(ht.get("PARALLEL_TITLE_STR")));
                        }
                        if (!sp.verifyStringTrim(ht.get("EDITION")).equals("")) {
                            htRow.put("edition", sp.verifyStringTrim(ht.get("EDITION")));
                        }
                        if (!sp.verifyStringTrim(ht.get("PUBLISHER")).equals("")) {
                            htRow.put("publisher", sp.verifyStringTrim(ht.get("PUBLISHER")));
                        }
                        if (!sp.verifyStringTrim(ht.get("PHYSICAL_DESCRIPTION")).equals("")) {
                            htRow.put("PHYSICAL_DESCRIPTION", sp.verifyStringTrim(ht.get("PHYSICAL_DESCRIPTION")));
                        }
                        htRow.put("subjects", ht.get("SUBJECTS"));
                        htRow.put("addedEntries", ht.get("ADDED_ENTRIES"));
                        htRow.put("series", ht.get("SERIES"));
                        String price = "";
                        price = getPrice(libId, accNo);
                        if (price == null || price.trim().equals("")) {
                            String custom[] = getDefaultCustomDetials(doc.getCustom());
                            price = custom[4];
                        }
                        if (price != null) htRow.put("price", price); else htRow.put("price", "");
                        String volume = "";
                        if (cat.getEnum_chrono() != null) volume = util.Utility.getInstance().getEnumChronoDisplayString(cat.getEnum_chrono());
                        if (volume != null) htRow.put("volume", volume); else htRow.put("volume", "");
                        System.out.println("the price and the volume id's are price = " + price + " and the volume is = " + volume);
                        CATALOGUERECORD_MANAGER catman = new CATALOGUERECORD_MANAGER();
                        Vector vecacccla = catman.getAccessioNosAndClassNo(session, searchCat.getPrimaryKey().getCataloguerecordid().toString(), searchCat.getPrimaryKey().getOwner_library_id().toString(), "1");
                        Vector vecacc = (Vector) vecacccla.elementAt(0);
                        String classno = vecacccla.elementAt(1).toString();
                        String bookno = vecacccla.elementAt(2).toString();
                        htRow.put("accessionNo", vecacc);
                        htRow.put("classNumber", classno);
                        htRow.put("bookNumber", bookno);
                        r[6] = htRow;
                        dtmCat.addRow(r);
                    } else javax.swing.JOptionPane.showMessageDialog(this, "The catalogue record for this accession no was already added to the table", "Information message", javax.swing.JOptionPane.OK_OPTION);
                } else javax.swing.JOptionPane.showMessageDialog(this, "There is no catalogue record for this accession no", "Information message", javax.swing.JOptionPane.OK_OPTION);
            }
            if (list.size() == 0) javax.swing.JOptionPane.showMessageDialog(this, "There is no catalogue record for this accession no", "Information message", javax.swing.JOptionPane.OK_OPTION);
            session.close();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "There is a problem in performing this operation", "Error message", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        ((java.awt.CardLayout) jPanel5.getLayout()).show(jPanel5, "date");
    }

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        ((java.awt.CardLayout) jPanel5.getLayout()).show(jPanel5, "accession number");
    }

    private void bnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        String[] cols = { "", "Title", "Main Author", "Publisher", "Edition", "Selected", "" };
        dtmCat = new DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int row, int column) {
                if (column == 5) return true; else return false;
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        tableCats.setModel(dtmCat);
        setSize(600, 500);
        show();
        tableCats.getColumnModel().getColumn(0).setMinWidth(0);
        tableCats.getColumnModel().getColumn(0).setPreferredWidth(0);
        tableCats.getColumnModel().getColumn(6).setMinWidth(0);
        tableCats.getColumnModel().getColumn(6).setPreferredWidth(0);
        tableCats.getColumnModel().getColumn(1).setPreferredWidth(193);
        tableCats.getColumnModel().getColumn(2).setPreferredWidth(165);
        tableCats.getColumnModel().getColumn(3).setPreferredWidth(75);
        tableCats.getColumnModel().getColumn(4).setPreferredWidth(75);
        tableCats.getColumnModel().getColumn(5).setPreferredWidth(52);
    }

    private void bnPrintActionPerformed(java.awt.event.ActionEvent evt) {
        CataloguePrinter catp = new CataloguePrinter(chbMainEntry.isSelected(), chbOtherAuthors.isSelected(), chbSeries.isSelected(), chbTitle.isSelected(), chbSubject.isSelected(), chbClassNo.isSelected());
        for (int i = 0; i < dtmCat.getRowCount(); i++) {
            Boolean selected = (Boolean) dtmCat.getValueAt(i, 5);
            int noMain = ((Integer) spMainEntry.getValue()).intValue();
            int noOther = ((Integer) spOtherAuthors.getValue()).intValue();
            int noSeries = ((Integer) spSeries.getValue()).intValue();
            int noTitle = ((Integer) spTitle.getValue()).intValue();
            int noSubject = ((Integer) spSubject.getValue()).intValue();
            int noClass = ((Integer) spClassificationNo.getValue()).intValue();
            if (selected.booleanValue() == true) catp.execute((Hashtable) dtmCat.getValueAt(i, 6), noMain, noOther, noSeries, noTitle, noSubject, noClass);
        }
        try {
            String os = System.getProperty("os.name");
            System.out.println("Os name Identified");
            if (os.toUpperCase().indexOf("WINDOWS") != -1) {
                Runtime.getRuntime().exec("print c:\\PrintCat.txt");
            } else if (os.toUpperCase().indexOf("LINUX") != -1) {
                Runtime.getRuntime().exec("print \\usr\\PrintCat.txt");
            } else {
                Runtime.getRuntime().exec("print c:\\PrintCat.txt");
            }
            System.out.println("printing is completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bnGoDateActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            dtmCat.setRowCount(0);
            StringProcessor sp = StringProcessor.getInstance();
            Utility utility = Utility.getInstance();
            CATALOGUERECORD_MANAGER catman = new CATALOGUERECORD_MANAGER();
            SEARCHABLE_CATALOGUERECORD_MANAGER scman = new SEARCHABLE_CATALOGUERECORD_MANAGER();
            Converter conv = new Converter();
            Session session = HibernateUtil.getSessionFactory().openSession();
            String fromdate = utility.getDatabaseDatFormat((Date) tfFrom.getValue());
            String todate = utility.getDatabaseDatFormat((Date) tfTo.getValue());
            List list = catman.getCatalogueRecordsOfMonographsBetweenDates(session, fromdate, todate);
            for (int i = 0; i < list.size(); i++) {
                CATALOGUERECORD catrec = (CATALOGUERECORD) list.get(i);
                SEARCHABLE_CATALOGUERECORD_KEY sckey = new SEARCHABLE_CATALOGUERECORD_KEY();
                sckey.setCataloguerecordid(catrec.getPrimaryKey().getCataloguerecordid());
                sckey.setOwner_library_id(catrec.getPrimaryKey().getOwner_library_id());
                SEARCHABLE_CATALOGUERECORD saercat = scman.load(session, sckey);
                String isorec = saercat.getWholecataloguerecord();
                Hashtable ht = conv.getDetails(isorec);
                Object[] r = new Object[7];
                r[0] = catrec.getPrimaryKey().getCataloguerecordid() + "_" + catrec.getPrimaryKey().getOwner_library_id();
                System.out.println("title asd" + ht.get("TITLE") + "  ... " + ht.get("AUTHOR"));
                r[1] = sp.verifyStringTrim(ht.get("TITLE"));
                r[2] = sp.verifyStringTrim(ht.get("AUTHOR"));
                r[3] = sp.verifyStringTrim(ht.get("PUBLISHER"));
                r[4] = sp.verifyStringTrim(ht.get("EDITION"));
                r[5] = new Boolean(true);
                Hashtable htRow = new Hashtable();
                if (!sp.verifyStringTrim(ht.get("AUTHOR")).equals("")) {
                    htRow.put("MAIN_ENTRY", sp.verifyStringTrim(ht.get("AUTHOR")));
                }
                if (!sp.verifyStringTrim(ht.get("TITLE_SOR")).equals("")) {
                    htRow.put("Title_SOR", sp.verifyStringTrim(ht.get("TITLE_SOR")));
                }
                if (!sp.verifyStringTrim(ht.get("TITLE")).equals("")) {
                    htRow.put("TITLE", sp.verifyStringTrim(ht.get("TITLE")));
                }
                if (!sp.verifyStringTrim(ht.get("PARALLEL_TITLE_STR")).equals("")) {
                    htRow.put("parallel_title", sp.verifyStringTrim(ht.get("PARALLEL_TITLE_STR")));
                }
                if (!sp.verifyStringTrim(ht.get("EDITION")).equals("")) {
                    htRow.put("edition", sp.verifyStringTrim(ht.get("EDITION")));
                }
                if (!sp.verifyStringTrim(ht.get("PUBLISHER")).equals("")) {
                    htRow.put("publisher", sp.verifyStringTrim(ht.get("PUBLISHER")));
                }
                if (!sp.verifyStringTrim(ht.get("PHYSICAL_DESCRIPTION")).equals("")) {
                    htRow.put("PHYSICAL_DESCRIPTION", sp.verifyStringTrim(ht.get("PHYSICAL_DESCRIPTION")));
                }
                htRow.put("subjects", ht.get("SUBJECTS"));
                htRow.put("addedEntries", ht.get("ADDED_ENTRIES"));
                htRow.put("series", ht.get("SERIES"));
                Vector vecacccla = catman.getAccessioNosAndClassNo(session, catrec.getPrimaryKey().getCataloguerecordid().toString(), catrec.getPrimaryKey().getOwner_library_id().toString(), "1");
                Vector vecacc = (Vector) vecacccla.elementAt(0);
                String classno = vecacccla.elementAt(1).toString();
                String bookno = vecacccla.elementAt(2).toString();
                htRow.put("accessionNo", vecacc);
                htRow.put("classNumber", classno);
                htRow.put("bookNumber", bookno);
                String price = "";
                Enumeration en = vecacc.elements();
                String libId = reports.utility.StaticValues.getInstance().getLoginLibraryId();
                String accessionNo = "";
                if (en.hasMoreElements()) {
                    accessionNo = en.nextElement().toString();
                    price = getPrice(libId, accessionNo);
                }
                if (price == null || price.trim().equals("")) {
                    java.util.List list1 = session.createQuery("from DOCUMENT as d where d.primaryKey.library_id=" + libId + " and d.barcode like '" + accessionNo + "'").list();
                    for (int j = 0; j < list1.size(); j++) {
                        DOCUMENT doc = (DOCUMENT) list1.get(j);
                        String custom[] = getDefaultCustomDetials(doc.getCustom());
                        price = custom[4];
                    }
                }
                if (price != null) htRow.put("price", price); else htRow.put("price", "");
                String volume = "";
                Integer volId = null;
                java.util.List list1 = session.createQuery("from DOCUMENT as d where d.primaryKey.library_id=" + libId + " and d.barcode like '" + accessionNo + "'").list();
                for (int j = 0; j < list1.size(); j++) {
                    DOCUMENT doc = (DOCUMENT) list1.get(j);
                    volId = doc.getVolume_id();
                }
                if (volId != null) {
                    CAT_VOLUME_KEY catKey = new CAT_VOLUME_KEY();
                    catKey.setVolume_id(volId);
                    CAT_VOLUME_MANAGER catVolManager = new CAT_VOLUME_MANAGER();
                    CAT_VOLUME cat = catVolManager.load(session, catKey);
                    if (cat.getEnum_chrono() != null) volume = util.Utility.getInstance().getEnumChronoDisplayString(cat.getEnum_chrono());
                }
                if (volume != null) htRow.put("volume", volume); else htRow.put("volume", "");
                System.out.println("the price and the volume id's are price = " + price + " and the volume is = " + volume);
                r[6] = htRow;
                dtmCat.addRow(r);
            }
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private javax.swing.JTextField accessionno;

    private javax.swing.JButton bnAccessionNo;

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnClose;

    private javax.swing.JButton bnGoDate;

    private javax.swing.JButton bnPrint;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JCheckBox chbClassNo;

    private javax.swing.JCheckBox chbMainEntry;

    private javax.swing.JCheckBox chbOtherAuthors;

    private javax.swing.JCheckBox chbSeries;

    private javax.swing.JCheckBox chbSubject;

    private javax.swing.JCheckBox chbTitle;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSpinner spClassificationNo;

    private javax.swing.JSpinner spMainEntry;

    private javax.swing.JSpinner spOtherAuthors;

    private javax.swing.JSpinner spSeries;

    private javax.swing.JSpinner spSubject;

    private javax.swing.JSpinner spTitle;

    private javax.swing.JTable tableCats;

    private javax.swing.JFormattedTextField tfFrom;

    private javax.swing.JFormattedTextField tfTo;

    private DefaultTableModel dtmCat;
}
