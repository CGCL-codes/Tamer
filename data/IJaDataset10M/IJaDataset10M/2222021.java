package newgen.presentation.acquisitions.acqAdv;

import newgen.presentation.component.MonthsSeasonsAutoComplete;

/**
 *
 * @author  Administrator
 */
public class StartEndVolumeEnumChronPanel extends javax.swing.JPanel {

    Object[] items = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", "Spring", "Summer", "Autumn", "Winter" };

    /** Creates new form StartEndVolumeEnumChronPanel */
    public StartEndVolumeEnumChronPanel() {
        initComponents();
        setScreenData();
        getCurrencyCodes();
        this.tfEnumFirst.grabFocus();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    public void reloadLocales() {
        try {
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Enumeration")));
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Chronology")));
            jLabel11.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterVolumeIssuePartdata"));
            jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FirstLevel"));
            jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SecondLevel"));
            jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ThirdLevel"));
            jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FourthLevel"));
            jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FifthLevel"));
            jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SixthLevel"));
            jLabel12.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterYearMonthDayDate"));
            jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FirstLevel"));
            jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SecondLevel"));
            jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ThirdLevel"));
            jLabel10.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FourthLevel"));
            lPrice1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Pricepercopy"));
            lNoOfCopies.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Noofcopies"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScreenData() {
        cmbEnumFirst.addItem("vol");
        cmbEnumFirst.addItem("iss");
        cmbEnumFirst.addItem("part");
        cmbEnumFirst.addItem(" ");
        cmbEnumSecond.addItem(" ");
        cmbEnumSecond.addItem("vol");
        cmbEnumSecond.addItem("part");
        cmbEnumSecond.addItem("iss");
        cmbEnumThird.addItem(" ");
        cmbEnumThird.addItem("part");
        cmbEnumThird.addItem("vol");
        cmbEnumThird.addItem("iss");
        cmbEnumFourth.addItem(" ");
        cmbEnumFourth.addItem("vol");
        cmbEnumFourth.addItem("iss");
        cmbEnumFourth.addItem("part");
        cmbEnumFifth.addItem(" ");
        cmbEnumFifth.addItem("vol");
        cmbEnumFifth.addItem("iss");
        cmbEnumFifth.addItem("part");
        cmbEnumSixth.addItem(" ");
        cmbEnumSixth.addItem("vol");
        cmbEnumSixth.addItem("iss");
        cmbEnumSixth.addItem("part");
        cmbChronFirst.addItem("  ");
        cmbChronFirst.addItem("Year");
        cmbChronSecond.addItem(" ");
        cmbChronSecond.addItem("Month");
        cmbChronSecond.addItem("Season");
        cmbChronThird.addItem(" ");
        cmbChronThird.addItem("Day");
        cmbChronFourth.addItem(" ");
        cmbChronFourth.addItem("Time of a day");
    }

    private void getCurrencyCodes() {
        cCurrency1.removeAllItems();
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryID", newgen.presentation.NewGenMain.getAppletInstance().getLibraryID());
        String xmlStr = newgen.presentation.component.ServletConnector.getInstance().sendRequest("Utility", newgen.presentation.component.NewGenXMLGenerator.getInstance().buildXMLDocument("2", ht));
        org.jdom.Element root = newgen.presentation.component.NewGenXMLGenerator.getInstance().getRootElement(xmlStr);
        Object[] object = root.getChildren("CurrencyCode").toArray();
        for (int i = 0; i < object.length; i++) {
            cCurrency1.addItem(((org.jdom.Element) object[i]).getText());
        }
    }

    public void setDataToStartAndEndVol(String xml, String price, String copies, String currency) {
        try {
            this.tfEnumFirst.grabFocus();
            if (xml != null && !xml.trim().equals("")) {
                org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = sax.build(new java.io.StringReader(xml.trim()));
                org.jdom.Element root = doc.getRootElement();
                java.util.List lst = root.getChildren("Enumeration");
                java.util.List lstChron = root.getChildren("Chronology");
                for (int i = 0; i < lst.size(); i++) {
                    org.jdom.Element ele = (org.jdom.Element) lst.get(i);
                    String code = "", val = "", name = "";
                    code = ele.getAttributeValue("code");
                    name = ele.getAttributeValue("name");
                    val = ele.getText();
                    if (code.trim().equals("a")) {
                        cmbEnumFirst.setSelectedItem(name);
                        tfEnumFirst.setText(val);
                    } else if (code.trim().equals("b")) {
                        cmbEnumSecond.setSelectedItem(name);
                        tfEnumSecond.setText(val);
                    } else if (code.trim().equals("c")) {
                        cmbEnumThird.setSelectedItem(name);
                        tfEnumThird.setText(val);
                    } else if (code.trim().equals("d")) {
                        cmbEnumFourth.setSelectedItem(name);
                        tfEnumFourth.setText(val);
                    } else if (code.trim().equals("e")) {
                        cmbEnumFifth.setSelectedItem(name);
                        tfEnumFifth.setText(val);
                    } else if (code.trim().equals("f")) {
                        cmbEnumSixth.setSelectedItem(name);
                        tfEnumSixth.setText(val);
                    }
                }
                for (int i = 0; i < lstChron.size(); i++) {
                    org.jdom.Element ele = (org.jdom.Element) lstChron.get(i);
                    String code = "", val = "", name = "";
                    code = ele.getAttributeValue("code");
                    name = ele.getAttributeValue("name");
                    val = ele.getText();
                    if (code.trim().equals("i")) {
                        cmbChronFirst.setSelectedItem(name);
                        tfChronFirst.setText(val);
                    } else if (code.trim().equals("j")) {
                        cmbChronSecond.setSelectedItem(name);
                        cbChronSecond.setSelectedItem(newgen.presentation.component.EnumChronoStringConverter.getInstance().getMonthSeasonDisplay(val));
                    } else if (code.trim().equals("k")) {
                        cmbChronThird.setSelectedItem(name);
                        tfChronThird.setText(val);
                    } else if (code.trim().equals("l")) {
                        cmbChronFourth.setSelectedItem(name);
                        tfChronFourth.setText(val);
                    }
                }
                tPrice1.setValue(new Double(price));
                tCopies.setValue(new Integer(copies));
                cCurrency1.setSelectedItem(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validation() {
        boolean valid = false;
        tCopies.setFocusable(true);
        System.out.println("tCopies.getValue() " + tCopies.getValue());
        if (tCopies.getValue().toString().equals("0")) {
            tCopies.setValue(new Integer(1));
        }
        if (!tfEnumFirst.getText().equals("") && !cmbEnumFirst.getSelectedItem().toString().trim().equals("")) {
            valid = true;
        } else if (!tfChronFirst.getText().equals("") && !cmbChronFirst.getSelectedItem().toString().trim().equals("")) {
            valid = true;
        } else {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("Please Enter Appropriate data");
            valid = false;
            tfEnumFirst.grabFocus();
        }
        if (tCopies.getValue().toString().equals("0")) {
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("Please Enter noofcopies");
            valid = false;
        }
        return valid;
    }

    public void cancelValues() {
        tfEnumFirst.setText("");
        tfEnumSecond.setText("");
        tfEnumThird.setText("");
        tfEnumFourth.setText("");
        tfEnumFifth.setText("");
        tfEnumSixth.setText("");
        tfChronFirst.setText("");
        tfChronThird.setText("");
        tfChronFourth.setText("");
        cbChronSecond.setSelectedItem("");
        tCopies.setValue(new Integer(1));
        tPrice1.setValue(new Double(0));
    }

    public void cancelCaptions() {
        cmbEnumSecond.setSelectedItem("");
        cmbEnumThird.setSelectedItem("");
        cmbEnumFourth.setSelectedItem("");
        cmbEnumFifth.setSelectedItem("");
        cmbEnumSixth.setSelectedItem("");
        cmbChronFirst.setSelectedItem("");
        cmbChronSecond.setSelectedItem("");
        cmbChronThird.setSelectedItem("");
        cmbChronFourth.setSelectedItem("");
    }

    public String getXML() {
        String xmlStr = "";
        try {
            if (validation()) {
                boolean valid = false, validchro = false;
                ;
                org.jdom.Element root = new org.jdom.Element("EnumerationDef");
                org.jdom.Element ele = new org.jdom.Element("Enumeration");
                ele.setAttribute("code", "a");
                ele.setAttribute("name", cmbEnumFirst.getSelectedItem().toString().trim());
                ele.setText(tfEnumFirst.getText());
                if (!tfEnumFirst.getText().equals("") && !cmbEnumFirst.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                    valid = true;
                }
                ele = new org.jdom.Element("Enumeration");
                ele.setAttribute("code", "b");
                ele.setAttribute("name", cmbEnumSecond.getSelectedItem().toString().trim());
                ele.setText(tfEnumSecond.getText());
                if (valid && !tfEnumSecond.getText().equals("") && !cmbEnumSecond.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    valid = false;
                }
                ele = new org.jdom.Element("Enumeration");
                ele.setAttribute("code", "c");
                ele.setAttribute("name", cmbEnumThird.getSelectedItem().toString().trim());
                ele.setText(tfEnumThird.getText());
                if (valid && !tfEnumThird.getText().equals("") && !cmbEnumThird.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    valid = false;
                }
                ele = new org.jdom.Element("Enumeration");
                ele.setAttribute("code", "d");
                ele.setAttribute("name", cmbEnumFourth.getSelectedItem().toString().trim());
                ele.setText(tfEnumFourth.getText());
                if (valid && !tfEnumFourth.getText().equals("") && !cmbEnumFourth.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    valid = false;
                }
                ele = new org.jdom.Element("Enumeration");
                ele.setAttribute("code", "e");
                ele.setAttribute("name", cmbEnumFifth.getSelectedItem().toString().trim());
                ele.setText(tfEnumFifth.getText());
                if (valid && !tfEnumFifth.getText().equals("") && !cmbEnumFifth.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    valid = false;
                }
                ele = new org.jdom.Element("Enumeration");
                ele.setAttribute("code", "f");
                ele.setAttribute("name", cmbEnumSixth.getSelectedItem().toString().trim());
                ele.setText(tfEnumSixth.getText());
                if (valid && !tfEnumSixth.getText().equals("") && !cmbEnumSixth.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    valid = false;
                }
                ele = new org.jdom.Element("Chronology");
                ele.setAttribute("code", "i");
                ele.setAttribute("name", cmbChronFirst.getSelectedItem().toString().trim());
                ele.setText(tfChronFirst.getText());
                if (!tfChronFirst.getText().equals("") && !cmbChronFirst.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                    validchro = true;
                }
                org.jdom.Element subfld = new org.jdom.Element("subfield");
                ele = new org.jdom.Element("Chronology");
                ele.setAttribute("code", "j");
                ele.setAttribute("name", cmbChronSecond.getSelectedItem().toString().trim());
                ele.setText(newgen.presentation.component.EnumChronoStringConverter.getInstance().getMonthSeasonDatabaseString(cbChronSecond.getSelectedItem().toString()));
                if (!cmbChronSecond.getSelectedItem().toString().trim().equals("") && !cbChronSecond.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    validchro = false;
                }
                ele = new org.jdom.Element("Chronology");
                ele.setAttribute("code", "k");
                ele.setAttribute("name", cmbChronThird.getSelectedItem().toString().trim());
                ele.setText(tfChronThird.getText());
                if (!tfChronThird.getText().trim().equals("") && !cmbChronThird.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    validchro = false;
                }
                ele = new org.jdom.Element("Chronology");
                ele.setAttribute("code", "l");
                ele.setAttribute("name", cmbChronFourth.getSelectedItem().toString().trim());
                ele.setText(tfChronFourth.getText());
                if (!tfChronFourth.getText().trim().equals("") && !cmbChronFourth.getSelectedItem().toString().trim().equals("")) {
                    root.addContent(ele);
                } else {
                    validchro = false;
                }
                org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
                org.jdom.Document doc = new org.jdom.Document(root);
                xmlStr = out.outputString(doc);
                this.volDisplayString = util.Utility.getInstance().getEnumChronoDisplayString(xmlStr);
                if (cCurrency1.getSelectedItem() != null) {
                    this.currencyStr = cCurrency1.getSelectedItem().toString();
                }
                System.out.println("tPrice1 " + tPrice1.getValue());
                if (tPrice1.getValue() != null) {
                    this.priceStr = tPrice1.getValue().toString();
                }
                tCopies.grabFocus();
                if (tCopies.getValue() != null) {
                    this.noofCopiesStr = tCopies.getValue().toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("in xml ..." + xmlStr);
        return xmlStr;
    }

    public void setEnumXMLScreen(String xml) {
        try {
            tfEnumFirst.grabFocus();
            System.out.println("xml in setEnum Screen..." + xml);
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = sax.build(new java.io.StringReader(xml));
            org.jdom.Element root = doc.getRootElement();
            java.util.List lstCap = root.getChildren("Caption");
            for (int i = 0; i < lstCap.size(); i++) {
                String code = "", name = "", val = "";
                org.jdom.Element ele = (org.jdom.Element) lstCap.get(i);
                code = ele.getAttributeValue("code");
                name = ele.getAttributeValue("name");
                val = ele.getAttributeValue("val");
                if (code != null && code.trim().equals("a")) {
                    cmbEnumFirst.setSelectedItem(name);
                    tfEnumFirst.setText(val);
                } else if (code != null && code.trim().equals("b")) {
                    cmbEnumSecond.setSelectedItem(name);
                    tfEnumSecond.setText(val);
                } else if (code != null && code.trim().equals("c")) {
                    cmbEnumThird.setSelectedItem(name);
                    tfEnumThird.setText(val);
                } else if (code != null && code.trim().equals("d")) {
                    cmbEnumFourth.setSelectedItem(name);
                    tfEnumFourth.setText(val);
                } else if (code != null && code.trim().equals("e")) {
                    cmbEnumFifth.setSelectedItem(name);
                    tfEnumFifth.setText(val);
                } else if (code != null && code.trim().equals("f")) {
                    cmbEnumSixth.setSelectedItem(name);
                    tfEnumSixth.setText(val);
                }
            }
            org.jdom.Element eleChron = root.getChild("Chronology");
            java.util.List lstChron = eleChron.getChildren();
            System.out.println("Chronology size is.." + lstChron.size());
            for (int j = 0; j < lstChron.size(); j++) {
                String code = "", name = "", val = "";
                System.out.println("chronology  " + code + "   " + name + "   " + val);
                org.jdom.Element ele = (org.jdom.Element) lstChron.get(j);
                code = ele.getAttributeValue("code");
                name = ele.getAttributeValue("name");
                val = ele.getAttributeValue("val");
                System.out.println("chronology  " + code + "   " + name + "   " + val);
                if (code != null && code.trim().equals("i")) {
                    cmbChronFirst.setSelectedItem(name);
                    tfChronFirst.setText(val);
                } else if (code != null && code.trim().equals("j")) {
                    cmbChronSecond.setSelectedItem(name);
                    cbChronSecond.setText(val);
                } else if (code != null && code.trim().equals("k")) {
                    cmbChronThird.setSelectedItem(name);
                    tfChronThird.setText(val);
                } else if (code != null && code.trim().equals("l")) {
                    cmbChronFourth.setSelectedItem(name);
                    tfChronFourth.setText(val);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEnumXMLScreen() {
        String numbers = "";
        if (cmbEnumFirst.getSelectedItem() != null && !cmbEnumFirst.getSelectedItem().toString().trim().equals("")) numbers += cmbEnumFirst.getSelectedItem().toString().trim() + ": ";
        if (tfEnumFirst.getText() != null && !tfEnumFirst.getText().trim().equals("")) numbers += tfEnumFirst.getText().trim();
        if (cmbEnumSecond.getSelectedItem() != null && !cmbEnumSecond.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbEnumSecond.getSelectedItem().toString().trim() + ": ";
        if (tfEnumSecond.getText() != null && !tfEnumSecond.getText().trim().equals("")) numbers += tfEnumSecond.getText().trim();
        if (cmbEnumThird.getSelectedItem() != null && !cmbEnumThird.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbEnumThird.getSelectedItem().toString().trim() + ": ";
        if (tfEnumThird.getText() != null && !tfEnumThird.getText().trim().equals("")) numbers += tfEnumThird.getText().trim();
        if (cmbEnumFourth.getSelectedItem() != null && !cmbEnumFourth.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbEnumFourth.getSelectedItem().toString().trim() + ": ";
        if (tfEnumFourth.getText() != null && !tfEnumFourth.getText().trim().equals("")) numbers += tfEnumFourth.getText().trim();
        if (cmbEnumFifth.getSelectedItem() != null && !cmbEnumFifth.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbEnumFifth.getSelectedItem().toString().trim() + ": ";
        if (tfEnumFifth.getText() != null && !tfEnumFifth.getText().trim().equals("")) numbers += tfEnumFifth.getText().trim();
        if (cmbEnumSixth.getSelectedItem() != null && !cmbEnumSixth.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbEnumSixth.getSelectedItem().toString().trim() + ": ";
        if (tfEnumSixth.getText() != null && !tfEnumSixth.getText().trim().equals("")) numbers += tfEnumSixth.getText().trim();
        if (cmbChronFirst.getSelectedItem() != null && !cmbChronFirst.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbChronFirst.getSelectedItem().toString().trim() + ": ";
        if (tfChronFirst.getText() != null && !tfChronFirst.getText().trim().equals("")) {
            numbers += tfChronFirst.getText().trim();
            year[i] = tfChronFirst.getText().trim();
            i++;
        }
        if (cmbChronSecond.getSelectedItem() != null && !cmbChronSecond.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbChronSecond.getSelectedItem().toString().trim() + ": ";
        if (cbChronSecond.getText() != null && !cbChronSecond.getText().trim().equals("")) numbers += cbChronSecond.getText().trim().trim();
        if (cmbChronThird.getSelectedItem() != null && !cmbChronThird.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbChronThird.getSelectedItem().toString().trim() + ": ";
        if (tfChronThird.getText() != null && !tfChronThird.getText().trim().equals("")) numbers += tfChronThird.getText().trim();
        if (cmbChronFourth.getSelectedItem() != null && !cmbChronFourth.getSelectedItem().toString().trim().equals("")) numbers += ", " + cmbChronFourth.getSelectedItem().toString().trim() + ": ";
        if (tfChronFourth.getText() != null && !tfChronFourth.getText().trim().equals("")) numbers += tfChronFourth.getText().trim();
        if (i == 2) {
            i = 0;
            System.out.println("year range is " + year[0] + " to " + year[1]);
        } else System.out.println("i is " + i);
        return numbers;
    }

    public void cancelFields() {
        tfEnumFirst.setText("");
        tfEnumSecond.setText("");
        tfEnumThird.setText("");
        tfEnumFourth.setText("");
        tfEnumFifth.setText("");
        tfEnumSixth.setText("");
        tfChronFirst.setText("");
        cbChronSecond.setText("");
        tfChronThird.setText("");
        tfChronFourth.setText("");
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbEnumFirst = new javax.swing.JComboBox();
        tfEnumFirst = new newgen.presentation.UnicodeTextField();
        jLabel2 = new javax.swing.JLabel();
        cmbEnumSecond = new javax.swing.JComboBox();
        tfEnumSecond = new newgen.presentation.UnicodeTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbEnumThird = new javax.swing.JComboBox();
        tfEnumThird = new newgen.presentation.UnicodeTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbEnumFourth = new javax.swing.JComboBox();
        tfEnumFourth = new newgen.presentation.UnicodeTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbEnumFifth = new javax.swing.JComboBox();
        tfEnumFifth = new newgen.presentation.UnicodeTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbEnumSixth = new javax.swing.JComboBox();
        tfEnumSixth = new newgen.presentation.UnicodeTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbChronFirst = new javax.swing.JComboBox();
        tfChronFirst = new newgen.presentation.UnicodeTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbChronSecond = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cmbChronThird = new javax.swing.JComboBox();
        tfChronThird = new newgen.presentation.UnicodeTextField();
        jLabel10 = new javax.swing.JLabel();
        cmbChronFourth = new javax.swing.JComboBox();
        tfChronFourth = new newgen.presentation.UnicodeTextField();
        cbChronSecond = new MonthsSeasonsAutoComplete(items);
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lPrice1 = new javax.swing.JLabel();
        lNoOfCopies = new javax.swing.JLabel();
        cCurrency1 = new javax.swing.JComboBox();
        tPrice1 = new newgen.presentation.component.DoubleTextField();
        tCopies = new newgen.presentation.component.NTextField();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Enumeration")));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FirstLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel1, gridBagConstraints);
        cmbEnumFirst.setEditable(true);
        cmbEnumFirst.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(cmbEnumFirst, gridBagConstraints);
        tfEnumFirst.setMinimumSize(new java.awt.Dimension(169, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfEnumFirst, gridBagConstraints);
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SecondLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel2, gridBagConstraints);
        cmbEnumSecond.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(cmbEnumSecond, gridBagConstraints);
        tfEnumSecond.setMinimumSize(new java.awt.Dimension(219, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfEnumSecond, gridBagConstraints);
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ThirdLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel3, gridBagConstraints);
        cmbEnumThird.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(cmbEnumThird, gridBagConstraints);
        tfEnumThird.setMinimumSize(new java.awt.Dimension(219, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfEnumThird, gridBagConstraints);
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FourthLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel4, gridBagConstraints);
        cmbEnumFourth.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(cmbEnumFourth, gridBagConstraints);
        tfEnumFourth.setMinimumSize(new java.awt.Dimension(219, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfEnumFourth, gridBagConstraints);
        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FifthLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel5, gridBagConstraints);
        cmbEnumFifth.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(cmbEnumFifth, gridBagConstraints);
        tfEnumFifth.setMinimumSize(new java.awt.Dimension(219, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfEnumFifth, gridBagConstraints);
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SixthLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel6, gridBagConstraints);
        cmbEnumSixth.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(cmbEnumSixth, gridBagConstraints);
        tfEnumSixth.setMinimumSize(new java.awt.Dimension(219, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(tfEnumSixth, gridBagConstraints);
        jLabel11.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel11.setForeground(new java.awt.Color(0, 0, 175));
        jLabel11.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterVolumeIssuePartdata"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jLabel11, gridBagConstraints);
        add(jPanel1);
        jPanel2.setLayout(new java.awt.GridBagLayout());
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Chronology")));
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FirstLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel2.add(jLabel7, gridBagConstraints);
        cmbChronFirst.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbChronFirst.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbChronFirstActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(cmbChronFirst, gridBagConstraints);
        tfChronFirst.setMinimumSize(new java.awt.Dimension(169, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(tfChronFirst, gridBagConstraints);
        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SecondLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel2.add(jLabel8, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(cmbChronSecond, gridBagConstraints);
        jLabel9.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ThirdLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel2.add(jLabel9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(cmbChronThird, gridBagConstraints);
        tfChronThird.setMinimumSize(new java.awt.Dimension(169, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(tfChronThird, gridBagConstraints);
        jLabel10.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("FourthLevel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel2.add(jLabel10, gridBagConstraints);
        cmbChronFourth.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(cmbChronFourth, gridBagConstraints);
        tfChronFourth.setMinimumSize(new java.awt.Dimension(169, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(tfChronFourth, gridBagConstraints);
        cbChronSecond.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", "Spring", "Summer", "Autumn", "Winter" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(cbChronSecond, gridBagConstraints);
        jLabel12.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel12.setForeground(new java.awt.Color(0, 0, 175));
        jLabel12.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PleaseEnterYearMonthDayDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel12, gridBagConstraints);
        add(jPanel2);
        jPanel3.setLayout(new java.awt.GridBagLayout());
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setPreferredSize(new java.awt.Dimension(337, 100));
        lPrice1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Pricepercopy"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel3.add(lPrice1, gridBagConstraints);
        lNoOfCopies.setForeground(new java.awt.Color(255, 0, 51));
        lNoOfCopies.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Noofcopies"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(lNoOfCopies, gridBagConstraints);
        cCurrency1.setAutoscrolls(true);
        cCurrency1.setMinimumSize(new java.awt.Dimension(31, 19));
        cCurrency1.setPreferredSize(new java.awt.Dimension(82, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel3.add(cCurrency1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel3.add(tPrice1, gridBagConstraints);
        tCopies.setText("1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(tCopies, gridBagConstraints);
        add(jPanel3);
    }

    private void cmbChronFirstActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /** Getter for property chronTopLevel.
     * @return Value of property chronTopLevel.
     *
     */
    public java.lang.String getChronTopLevel() {
        return chronTopLevel;
    }

    /** Setter for property chronTopLevel.
     * @param chronTopLevel New value of property chronTopLevel.
     *
     */
    public void setChronTopLevel(java.lang.String chronTopLevel) {
        this.chronTopLevel = chronTopLevel;
    }

    /** Getter for property volDisplayString.
     * @return Value of property volDisplayString.
     *
     */
    public java.lang.String getVolDisplayString() {
        return volDisplayString;
    }

    /** Setter for property volDisplayString.
     * @param volDisplayString New value of property volDisplayString.
     *
     */
    public void setVolDisplayString(java.lang.String volDisplayString) {
        this.volDisplayString = volDisplayString;
    }

    /** Getter for property priceStr.
     * @return Value of property priceStr.
     *
     */
    public java.lang.String getPriceStr() {
        return priceStr;
    }

    /** Setter for property priceStr.
     * @param priceStr New value of property priceStr.
     *
     */
    public void setPriceStr(java.lang.String priceStr) {
        this.priceStr = priceStr;
    }

    /** Getter for property currencyStr.
     * @return Value of property currencyStr.
     *
     */
    public java.lang.String getCurrencyStr() {
        return currencyStr;
    }

    /** Setter for property currencyStr.
     * @param currencyStr New value of property currencyStr.
     *
     */
    public void setCurrencyStr(java.lang.String currencyStr) {
        this.currencyStr = currencyStr;
    }

    /** Getter for property noofCopiesStr.
     * @return Value of property noofCopiesStr.
     *
     */
    public java.lang.String getNoofCopiesStr() {
        return noofCopiesStr;
    }

    /** Setter for property noofCopiesStr.
     * @param noofCopiesStr New value of property noofCopiesStr.
     *
     */
    public void setNoofCopiesStr(java.lang.String noofCopiesStr) {
        this.noofCopiesStr = noofCopiesStr;
    }

    public void disablePanel3() {
        jPanel3.setVisible(false);
    }

    private javax.swing.JComboBox cCurrency1;

    private newgen.presentation.component.MonthsSeasonsAutoComplete cbChronSecond;

    private javax.swing.JComboBox cmbChronFirst;

    private javax.swing.JComboBox cmbChronFourth;

    private javax.swing.JComboBox cmbChronSecond;

    private javax.swing.JComboBox cmbChronThird;

    private javax.swing.JComboBox cmbEnumFifth;

    private javax.swing.JComboBox cmbEnumFirst;

    private javax.swing.JComboBox cmbEnumFourth;

    private javax.swing.JComboBox cmbEnumSecond;

    private javax.swing.JComboBox cmbEnumSixth;

    private javax.swing.JComboBox cmbEnumThird;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JLabel lNoOfCopies;

    private javax.swing.JLabel lPrice1;

    private newgen.presentation.component.NTextField tCopies;

    private newgen.presentation.component.DoubleTextField tPrice1;

    private newgen.presentation.UnicodeTextField tfChronFirst;

    private newgen.presentation.UnicodeTextField tfChronFourth;

    private newgen.presentation.UnicodeTextField tfChronThird;

    private newgen.presentation.UnicodeTextField tfEnumFifth;

    private newgen.presentation.UnicodeTextField tfEnumFirst;

    private newgen.presentation.UnicodeTextField tfEnumFourth;

    private newgen.presentation.UnicodeTextField tfEnumSecond;

    private newgen.presentation.UnicodeTextField tfEnumSixth;

    private newgen.presentation.UnicodeTextField tfEnumThird;

    String[] year = new String[2];

    int i = 0;

    String chronTopLevel = "", volDisplayString = "", priceStr = "", currencyStr = "", noofCopiesStr = "";
}
