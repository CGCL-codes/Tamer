package gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
import org.w3c.dom.traversal.NodeIterator;
import com.sun.org.apache.xpath.internal.XPathAPI;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;

/**
 * �zenet szekvencia szerkeszt� alkalmaz�s �zenetv�z szerkezt� panelje
 * 
 * @author Dojcs�k S�ndor
 */
public class MessageEditorPanel extends MessagesPanel {

    private GridBagConstraints gbc0;

    private GridBagConstraints gbc1;

    private JButton btNew;

    private JButton btSave;

    private JButton btSaveas;

    private JButton btfDelete;

    private String prevItem = "";

    private File file;

    private JToggleButton tbtChecksum;

    private JToggleButton tbtLength;

    private JPanel checksumMenu;

    private JPanel lengthMenu;

    private JComboBox cbChksumField;

    protected JComboBox cbChksumFields;

    private JComboBox cbPseudoHeader;

    private JComboBox cbLengthField;

    private JComboBox cbLengthFields;

    public MessageEditorPanel(File mDir, NetSpotter own) {
        this.own = own;
        messageContainerPanel = own.meContainerPanel;
        gbc0 = new GridBagConstraints();
        gbc0.gridx = 0;
        gbc0.gridy = 0;
        gbc0.weightx = 1;
        gbc0.anchor = GridBagConstraints.WEST;
        gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 1;
        gbc1.weightx = 1;
        gbc1.anchor = GridBagConstraints.WEST;
        this.mDir = mDir;
        mefpn = new JPanel();
        mefpn.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel mefp = new JPanel();
        mefp.setBorder(BorderFactory.createEtchedBorder());
        mefpn.add(mefp);
        mefp.add(btNew = new JButton("New Message"));
        btNew.addActionListener(this);
        mefp.add(new JLabel("  Extends:"));
        mefp.add(cbLXload = new JComboBox());
        cbLXload.setPreferredSize(new Dimension(150, 22));
        cbLXload.addActionListener(this);
        cbLXload.setEnabled(false);
        refreshLoad(cbLXload);
        mefp.add(new JLabel("  Load:"));
        mefp.add(cbload = new JComboBox());
        cbload.addActionListener(this);
        refreshLoad(cbload);
        mefp.add(btSave = new JButton("Save"));
        btSave.setEnabled(false);
        btSave.addActionListener(this);
        mefp.add(btSaveas = new JButton("Save as"));
        btSaveas.setEnabled(false);
        btSaveas.addActionListener(this);
        mefp.add(btfDelete = new JButton("Delete"));
        btfDelete.setEnabled(false);
        btfDelete.addActionListener(this);
        mcpn = new JPanel();
        mcpn.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel field = new JPanel();
        field.setBorder(BorderFactory.createEtchedBorder());
        field.add(new JLabel("Fields:"));
        field.add(btAdd);
        btAdd.setEnabled(false);
        btAdd.addActionListener(this);
        field.add(btDelete);
        btDelete.setEnabled(false);
        btDelete.addActionListener(this);
        btCloseBody = new JButton("Close Body");
        lMessageName = new JLabel();
        JPanel specField = new JPanel();
        specField.setBorder(BorderFactory.createEtchedBorder());
        specField.add(tbtChecksum = new JToggleButton("CheckSum"));
        specField.add(tbtLength = new JToggleButton("Length"));
        tbtChecksum.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(tbtChecksum);
        bg.add(tbtLength);
        tbtChecksum.addActionListener(this);
        tbtLength.addActionListener(this);
        checksumMenu = new JPanel();
        checksumMenu.setBorder(BorderFactory.createEtchedBorder());
        checksumMenu.add(new JLabel("CheckSum Field:"));
        checksumMenu.add(cbChksumField = new JComboBox());
        cbChksumField.setPreferredSize(new Dimension(65, 22));
        cbChksumField.addItem("None");
        cbChksumField.setEnabled(false);
        cbChksumField.addItemListener(this);
        checksumMenu.add(new JLabel(" Fields:"));
        checksumMenu.add(cbChksumFields = new JComboBox());
        cbChksumFields.setEnabled(false);
        cbChksumFields.addItem("None");
        cbChksumFields.addItem("All Fields");
        cbChksumFields.addItem("All Fields and Body");
        cbChksumFields.addItem("Custom");
        cbChksumFields.addItemListener(this);
        checksumMenu.add(new JLabel(" PseudoHeader:"));
        checksumMenu.add(cbPseudoHeader = new JComboBox());
        cbPseudoHeader.setEnabled(false);
        cbPseudoHeader.addItem("None");
        cbPseudoHeader.addItem("IPv4");
        cbPseudoHeader.addItem("IPv6");
        cbPseudoHeader.addItemListener(this);
        lengthMenu = new JPanel();
        lengthMenu.setBorder(BorderFactory.createEtchedBorder());
        lengthMenu.add(new JLabel("Length Field:"));
        lengthMenu.add(cbLengthField = new JComboBox());
        cbLengthField.setPreferredSize(new Dimension(65, 22));
        cbLengthField.addItem("None");
        cbLengthField.setEnabled(false);
        cbLengthField.addItemListener(this);
        lengthMenu.add(new JLabel(" Fields:"));
        lengthMenu.add(cbLengthFields = new JComboBox());
        cbLengthFields.setEnabled(false);
        cbLengthFields.addItem("None");
        cbLengthFields.addItem("All Fields");
        cbLengthFields.addItem("All Fields and Body");
        cbLengthFields.addItem("Body");
        cbLengthFields.addItemListener(this);
        mcpn.add(field);
        mcpn.add(specField);
        mcpn.add(checksumMenu);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btNew) {
            New();
        } else if (e.getSource() == cbload) {
            if (cbload.getSelectedItem().equals("None")) {
                cbload.removeActionListener(this);
                cbload.setSelectedItem(messageName);
                cbload.addActionListener(this);
            } else Load();
        } else if (e.getSource() == cbLXload) {
            if (!cbLXload.getSelectedItem().equals("None")) {
                try {
                    file = new File(mDir + File.separator + cbLXload.getSelectedItem().toString() + ".xml");
                    bodyMPage = null;
                    Document doc = readFromFile(file);
                    readFromDocument(doc, true);
                    setTree(this);
                } catch (FileTypeException f) {
                }
            }
        } else if (e.getSource() == btSave) {
            Save();
        } else if (e.getSource() == btSaveas) {
            String messageName;
            do {
                messageName = JOptionPane.showInputDialog(own.meContainerPanel, "Message Name:", "", JOptionPane.QUESTION_MESSAGE);
            } while (wasName(messageName));
            if (messageName != null) {
                this.messageName = messageName;
                if (!own.isLockedMessage(messageType)) messageType = messageName;
                Save();
            }
        } else if (e.getSource() == btfDelete) {
            Delete();
        } else if (e.getSource() == btAdd) {
            Add();
        } else if (e.getSource() == btDelete) {
            saved = false;
            remove(selected - 1);
            for (int i = selected - 1; i < getComponentCount() - 1; i++) ((Field) getComponent(i)).setFcount(i + 1);
            updateUI();
            btDelete.setEnabled(false);
            selected = 0;
            setcbChkField();
            setcbLengthField();
            setPreferredSize(new Dimension(getPreferredSize().width, 73 * getComponentCount() + 5));
            setTree(this);
        } else if (e.getSource() == cbbTypeData) {
            saved = false;
            String Type = cbbTypeData.getSelectedItem().toString();
            if (Type.equals("text")) {
                tfbLength.setText("0");
                tfbLength.setEditable(false);
            } else if (Type.equals("MACaddress")) {
                tfbLength.setText("6");
                tfbLength.setEditable(false);
            } else if (Type.equals("ip4address")) {
                tfbLength.setText("4");
                tfbLength.setEditable(false);
            } else if (Type.equals("ip6address")) {
                tfbLength.setText("16");
                tfbLength.setEditable(false);
            } else if (prevItem.equals("text") || prevItem.equals("MACaddress") || prevItem.equals("ip4address") || prevItem.equals("ip6address")) {
                tfbLength.setText("");
                tfbLength.setEditable(true);
            }
            prevItem = cbbTypeData.getSelectedItem().toString();
        } else if (e.getSource().equals(tbtChecksum)) {
            mcpn.remove(lengthMenu);
            mcpn.add(checksumMenu);
            mcpn.updateUI();
        } else if (e.getSource().equals(tbtLength)) {
            mcpn.remove(checksumMenu);
            mcpn.add(lengthMenu);
            mcpn.updateUI();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        super.itemStateChanged(e);
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getSource().equals(cbChksumField)) {
                if (chksField != 0) {
                    Field field = (Field) getComponent(chksField - 1);
                    field.tfValue.setEditable(true);
                    field.tfValue.setText("");
                }
                String cf = cbChksumField.getSelectedItem().toString();
                if (cf.equals("None")) chksField = 0; else {
                    chksField = Integer.parseInt(cf.substring(0, cf.indexOf('.')));
                    ((Field) getComponent(chksField - 1)).tfValue.setEditable(false);
                }
                setcbLengthField();
                if (!cbChksumField.getSelectedItem().equals("None")) {
                    cbChksumFields.setEnabled(true);
                    cbPseudoHeader.setEnabled(true);
                    for (int i = 0; i < getComponentCount() - 1; i++) {
                        ((Field) getComponent(i)).chbChkSum.setEnabled(true);
                    }
                    chbCHKSum.setEnabled(true);
                } else {
                    for (int i = 0; i < getComponentCount() - 1; i++) {
                        ((Field) getComponent(i)).chbChkSum.setEnabled(false);
                        ((Field) getComponent(i)).chbChkSum.setSelected(false);
                        ((Field) getComponent(i)).chksum = false;
                    }
                    chbCHKSum.setEnabled(false);
                    chbCHKSum.setSelected(false);
                    cbChksumFields.setSelectedIndex(0);
                    cbChksumFields.setEnabled(false);
                    cbPseudoHeader.setSelectedIndex(0);
                    cbPseudoHeader.setEnabled(false);
                }
            } else if (e.getSource().equals(cbChksumFields)) {
                chksFields = cbChksumFields.getSelectedItem().toString();
                if (cbChksumFields.getSelectedIndex() != 3) setCHKSum(cbChksumFields.getSelectedIndex());
            } else if (e.getSource().equals(cbPseudoHeader)) pseudoHeader = cbPseudoHeader.getSelectedItem().toString(); else if (e.getSource().equals(cbLengthField)) {
                if (lengthField != 0) {
                    Field field = (Field) getComponent(lengthField - 1);
                    field.tfValue.setEditable(true);
                    field.tfValue.setText("");
                }
                String lf = cbLengthField.getSelectedItem().toString();
                if (lf.equals("None")) lengthField = 0; else {
                    lengthField = Integer.parseInt(lf.substring(0, lf.indexOf('.')));
                    System.out.println("itt");
                    ((Field) getComponent(lengthField - 1)).tfValue.setEditable(false);
                }
                setcbChkField();
                if (!cbLengthField.getSelectedItem().equals("None")) {
                    cbLengthFields.setEnabled(true);
                } else {
                    cbLengthFields.setSelectedIndex(0);
                    cbLengthFields.setEnabled(false);
                }
            } else if (e.getSource().equals(cbLengthFields)) lengthFields = cbLengthFields.getSelectedItem().toString();
            Body.updateUI();
            fieldChanged();
        } else if (e.getSource().equals(chbCHKSum)) {
            cbChksumFields.setSelectedIndex(3);
        }
    }

    private void setCHKSum(int index) {
        boolean field = true;
        boolean body = true;
        switch(index) {
            case 0:
                field = false;
                body = false;
                break;
            case 1:
                field = true;
                body = false;
                break;
            case 2:
                field = true;
                body = true;
                break;
        }
        for (int i = 0; i < getComponentCount() - 1; i++) {
            MessageEditorField MEfield = (MessageEditorField) getComponent(i);
            MEfield.chbChkSum.removeItemListener(MEfield);
            MEfield.chbChkSum.setSelected(field);
            MEfield.chksum = field;
            MEfield.chbChkSum.addItemListener(MEfield);
        }
        chbCHKSum.removeItemListener(this);
        chbCHKSum.setSelected(body);
        chbCHKSum.addItemListener(this);
    }

    protected void setcbChkField() {
        Object selectedCHKField = cbChksumField.getSelectedItem();
        cbChksumField.removeItemListener(this);
        cbChksumField.removeAllItems();
        cbChksumField.addItem("None");
        for (int i = 0; i < getComponentCount() - 1; i++) {
            MessageEditorField field = (MessageEditorField) getComponent(i);
            if (lengthField != i + 1) if (field.cbTypeField.getSelectedItem().equals("bit") || field.cbTypeField.getSelectedItem().equals("byte")) cbChksumField.addItem(i + 1 + ". Field");
        }
        cbChksumField.setSelectedItem(selectedCHKField);
        cbChksumField.addItemListener(this);
        if (cbChksumField.getSelectedIndex() == 0) {
            for (int i = 0; i < getComponentCount() - 1; i++) {
                ((Field) getComponent(i)).chbChkSum.setEnabled(false);
            }
            chbCHKSum.setEnabled(false);
            cbChksumFields.setEnabled(false);
            cbPseudoHeader.setEnabled(false);
        }
    }

    protected void setcbLengthField() {
        Object selectedLengthField = cbLengthField.getSelectedItem();
        cbLengthField.removeItemListener(this);
        cbLengthField.removeAllItems();
        cbLengthField.addItem("None");
        for (int i = 0; i < getComponentCount() - 1; i++) {
            MessageEditorField field = (MessageEditorField) getComponent(i);
            if (chksField != i + 1) if (field.cbTypeField.getSelectedItem().equals("bit") || field.cbTypeField.getSelectedItem().equals("byte")) cbLengthField.addItem(i + 1 + ". Field");
        }
        cbLengthField.setSelectedItem(selectedLengthField);
        cbLengthField.addItemListener(this);
        if (cbLengthField.getSelectedIndex() == 0) cbLengthFields.setEnabled(false);
    }

    private boolean wasName(String messageName) {
        if (messageName == null) return false;
        if (messageName.equals("")) return true;
        for (int i = 0; i < cbload.getItemCount(); i++) if (cbload.getItemAt(i).toString().toLowerCase().equals(messageName.toLowerCase())) {
            JOptionPane.showMessageDialog(own.meContainerPanel, "Existing Message", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private void New() {
        int option = JOptionPane.NO_OPTION;
        if (!saved) {
            option = JOptionPane.showConfirmDialog(own.meContainerPanel, "Save " + messageName + "?", "", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                Save();
            } else if (option == JOptionPane.NO_OPTION) {
                if (!new File(mDir + File.separator + messageName + ".xml").exists()) {
                    cbload.removeActionListener(this);
                    cbload.removeItem(messageName);
                    cbload.addActionListener(this);
                }
            }
        }
        if (option != JOptionPane.CANCEL_OPTION) {
            String messageName = JOptionPane.showInputDialog(own.meContainerPanel, "Message Name:", "", JOptionPane.QUESTION_MESSAGE);
            while (wasName(messageName)) {
                messageName = JOptionPane.showInputDialog(own.meContainerPanel, "Message Name:", "", JOptionPane.QUESTION_MESSAGE);
            }
            if (messageName != null) {
                cbChksumField.removeItemListener(this);
                cbChksumField.removeAllItems();
                cbChksumField.addItem("None");
                chksField = 0;
                cbChksumField.addItemListener(this);
                cbChksumFields.setSelectedIndex(0);
                chksFields = "None";
                cbChksumFields.setEnabled(false);
                cbPseudoHeader.setSelectedIndex(0);
                pseudoHeader = "None";
                cbPseudoHeader.setEnabled(false);
                cbLengthField.removeItemListener(this);
                cbLengthField.removeAllItems();
                cbLengthField.addItem("None");
                lengthField = 0;
                cbLengthField.addItemListener(this);
                cbLengthFields.setSelectedIndex(0);
                lengthFields = "None";
                cbLengthFields.setEnabled(false);
                selected = 0;
                this.messageName = messageName;
                messageType = messageName;
                btfDelete.setEnabled(false);
                btDelete.setEnabled(false);
                removeAll();
                Body.removeAll();
                addBody();
                setDataVisible(true);
                JScrollBar sbv = own.meContainerPanel.getVerticalScrollBar();
                sbv.setValue(sbv.getMinimum());
                JScrollBar sbh = own.meContainerPanel.getHorizontalScrollBar();
                sbh.setValue(sbh.getMinimum());
                repaint();
                saved = false;
                btSave.setEnabled(true);
                btAdd.setEnabled(true);
                cbLXload.setEnabled(true);
                btSaveas.setEnabled(true);
                tfbName.setEditable(true);
                cbbTypeData.setEnabled(true);
                tfbName.requestFocus();
                setTree(this);
                refreshLoad(cbLXload);
                cbload.setSelectedIndex(0);
                cbLXload.setSelectedIndex(0);
                cbChksumField.setEnabled(true);
                cbLengthField.setEnabled(true);
                fieldChanged();
                setPreferredSize(new Dimension(getPreferredSize().width, 73 * getComponentCount() + 5));
            }
        }
    }

    private void Load() {
        try {
            int option = JOptionPane.NO_OPTION;
            if (!saved) {
                option = JOptionPane.showConfirmDialog(own.meContainerPanel, "Save " + messageName + "?", "", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == JOptionPane.YES_OPTION) Save();
            }
            if (option == JOptionPane.CANCEL_OPTION) {
                cbload.removeActionListener(this);
                cbload.setSelectedItem(messageName);
                cbload.addActionListener(this);
            }
            if (option != JOptionPane.CANCEL_OPTION) {
                if (!messageName.trim().equals("None") && !new File(mDir + File.separator + messageName + ".xml").exists()) cbload.removeItem(messageName);
                file = new File(mDir + File.separator + cbload.getSelectedItem().toString() + ".xml");
                bodyMPage = null;
                refreshLoad(cbLXload);
                Document doc = readFromFile(file);
                readFromDocument(doc, false);
                cbLXload.removeActionListener(this);
                cbLXload.removeItem(messageName);
                cbLXload.addActionListener(this);
                saved = true;
                tbtChecksum.doClick();
                setTree(this);
                JScrollBar sb = own.meContainerPanel.getVerticalScrollBar();
                sb.setValue(sb.getMinimum());
                JScrollBar sbh = own.meContainerPanel.getHorizontalScrollBar();
                sbh.setValue(sbh.getMinimum());
            } else {
                cbload.removeActionListener(this);
                cbload.setSelectedItem("None");
                cbload.setSelectedItem(messageName);
                cbload.addActionListener(this);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileTypeException f) {
        }
    }

    protected void setTree(TreeSelectionListener tsl) {
        tree = new JTree(createTree());
        own.messageEditorTreePanel.setViewportView(tree);
        tree.addTreeSelectionListener(tsl);
    }

    private void Save() {
        Document document = null;
        try {
            document = createDocument(3);
            if (!new File(mDir + File.separator + messageName + ".xml").exists()) {
                cbload.removeActionListener(this);
                cbload.addItem(messageName);
                cbload.setSelectedItem(messageName);
                cbload.addActionListener(this);
            }
            writeToFile(document, new File(mDir + File.separator + messageName + ".xml"), "message skeleton");
            saved = true;
            btSave.setEnabled(true);
            if (!(messageType.equals("IPv4") || messageType.equals("IPv6"))) btfDelete.setEnabled(true);
            for (int i = 0; i < own.messagePanels.size(); i++) own.messagePanels.get(i).updateLoad();
            updateExtends(messageName);
        } catch (MissingException m) {
            if (!m.getMessage().equals("Missing message")) JOptionPane.showMessageDialog(this, m.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateExtends(String messageName) {
        try {
            File[] messages = mDir.listFiles();
            for (int j = 0; j < messages.length; j++) {
                Document doc = readFromFile(messages[j]);
                String extend = XPathAPI.eval(doc, "/message/extends/text()").toString();
                String childName = XPathAPI.eval(doc, "/message/messageName").toString();
                if (extend.equals(messageName)) {
                    Document child = readFromFile(messages[j]);
                    NodeIterator nlChildField = XPathAPI.selectNodeIterator(child, "/message/fields/field");
                    NodeIterator nloverride = XPathAPI.selectNodeIterator(child, "//override/text()");
                    Node nChildField;
                    Node nOver;
                    Document parent = readFromFile(new File(mDir + File.separator + messageName + ".xml"));
                    NodeIterator nlParentField = XPathAPI.selectNodeIterator(parent, "/message/fields/field");
                    Node nParentField;
                    while ((nParentField = nlParentField.nextNode()) != null) {
                        nChildField = nlChildField.nextNode();
                        nOver = nloverride.nextNode();
                        String over = nOver.getNodeValue();
                        if (over.equals("1") || over.equals("3")) {
                            Element importedDoc = (Element) nParentField;
                            Node imported = child.importNode(importedDoc, true);
                            for (int i = 0; i < 4; i++) nChildField.removeChild(nChildField.getFirstChild());
                            Node override = nChildField.getFirstChild();
                            for (int i = 0; i < 4; i++) nChildField.insertBefore(imported.getFirstChild(), override);
                        }
                    }
                    Node nChildBody = XPathAPI.selectSingleNode(child, "/message/body/data");
                    Node nParentBody = XPathAPI.selectSingleNode(parent, "/message/body/data");
                    String over = XPathAPI.eval(child, "/message/body/data/override/text()").toString();
                    if (over.equals("1")) {
                        Element importedDoc = (Element) nParentBody;
                        Node imported = child.importNode(importedDoc, true);
                        for (int i = 0; i < 4; i++) nChildBody.removeChild(nChildBody.getFirstChild());
                        Node override = nChildBody.getFirstChild();
                        for (int i = 0; i < 4; i++) nChildBody.insertBefore(imported.getFirstChild(), override);
                    }
                    child.getDocumentElement().removeChild(XPathAPI.selectSingleNode(child, "//fileType"));
                    writeToFile(child, messages[j], "message skeleton");
                    updateExtends(childName);
                }
            }
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void Delete() {
        if (JOptionPane.showConfirmDialog(own.meContainerPanel, "Delete " + cbload.getSelectedItem() + "?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            removeAll();
            own.messageEditorTreePanel.removeAll();
            saved = true;
            new File(mDir + File.separator + cbload.getSelectedItem().toString() + ".xml").delete();
            cbload.removeActionListener(this);
            cbload.removeItem(cbload.getSelectedItem());
            cbload.addActionListener(this);
            btSave.setEnabled(false);
            btSaveas.setEnabled(false);
            btfDelete.setEnabled(false);
            btAdd.setEnabled(false);
            btDelete.setEnabled(false);
            btCloseBody.setEnabled(false);
            selected = 0;
            cbload.setSelectedIndex(0);
            cbLXload.setSelectedIndex(0);
            cbLXload.setEnabled(false);
            for (int i = 0; i < own.messagePanels.size(); i++) own.messagePanels.get(i).updateLoad();
            messageName = "";
            messageType = "";
            cbChksumField.setSelectedIndex(0);
            cbLengthField.setSelectedIndex(0);
            own.messageEditorTreePanel.removeAll();
            repaint();
        }
    }

    private void Add() {
        saved = false;
        JTextField tfName;
        tfName = new JTextField(15);
        JTextField tfLength;
        tfLength = new JTextField(3);
        JComboBox cbTypeField;
        cbTypeField = new JComboBox();
        cbTypeField.addItem("bit");
        cbTypeField.addItem("byte");
        cbTypeField.addItem("text");
        cbTypeField.addItem("MACaddress");
        cbTypeField.addItem("ip4address");
        cbTypeField.addItem("ip6address");
        cbTypeField.setSelectedItem("byte");
        JTextField tfDefValue;
        tfDefValue = new JTextField(31);
        tfName.addFocusListener(this);
        cbTypeField.addItemListener(this);
        cbTypeField.addFocusListener(this);
        boolean chk = false;
        if (cbChksumFields.getSelectedIndex() == 1 || cbChksumFields.getSelectedIndex() == 2) chk = true;
        add(new MessageEditorField(this, tfName, cbTypeField, tfLength, tfDefValue, 0, chk, true), selected);
        setPreferredSize(new Dimension(getPreferredSize().width, 73 * getComponentCount() + 5));
        if (getComponentCount() == 2) {
            ((Field) getComponent(selected)).selectedBorder();
            selected = 1;
            Body.setBorder(BorderFactory.createEtchedBorder());
        }
        for (int i = 0; i < getComponentCount() - 1; i++) ((Field) getComponent(i)).setFcount(i + 1);
        setcbChkField();
        setcbLengthField();
        setTree(this);
        updateUI();
    }

    protected void readFromDocument(Document document, boolean Extends) throws FileTypeException {
        super.readFromDocument(document, Extends);
        if (!Extends) {
            try {
                cbLXload.removeActionListener(this);
                String extend = XPathAPI.eval(document, "//extends/text()").toString();
                if (extend.equals("None")) cbLXload.setSelectedIndex(0); else cbLXload.setSelectedItem(extend);
                cbLXload.addActionListener(this);
            } catch (TransformerException t) {
                t.printStackTrace();
            }
        }
        cbChksumField.removeItemListener(this);
        cbChksumField.addItem(chksField + ". Field");
        cbChksumField.setSelectedItem(chksField + ". Field");
        cbLengthField.removeItemListener(this);
        cbLengthField.addItem(lengthField + ". Field");
        cbLengthField.setSelectedItem(lengthField + ". Field");
        setcbChkField();
        setcbLengthField();
        tfbName.setEditable(true);
        cbbTypeData.setEnabled(true);
        tfbLength.setEditable(true);
        btSave.setEnabled(true);
        btSaveas.setEnabled(true);
        btfDelete.setEnabled(true);
        cbChksumFields.removeItemListener(this);
        cbPseudoHeader.removeItemListener(this);
        cbChksumField.setSelectedItem(chksField + ". Field");
        if (!cbChksumField.getSelectedItem().equals("None")) {
            cbChksumFields.setEnabled(true);
            cbPseudoHeader.setEnabled(true);
            if (!own.isLockedMessage(messageType) && !Extends) for (int i = 0; i < getComponentCount() - 1; i++) {
                MessageEditorField field = (MessageEditorField) getComponent(i);
                field.chbChkSum.setEnabled(true);
                field.chbChkSum.setSelected(field.chksum);
            }
            chbCHKSum.setEnabled(true);
        }
        cbChksumFields.setSelectedItem(chksFields);
        cbPseudoHeader.setSelectedItem(pseudoHeader);
        cbChksumFields.addItemListener(this);
        cbPseudoHeader.addItemListener(this);
        cbLengthFields.removeItemListener(this);
        cbLengthField.setSelectedItem(lengthField + ". Field");
        if (!cbLengthField.getSelectedItem().equals("None")) cbLengthFields.setEnabled(true);
        cbLengthFields.setSelectedItem(lengthFields);
        cbLengthFields.addItemListener(this);
        if (own.isLockedMessage(messageType) && !Extends) {
            cbChksumField.setEnabled(false);
            cbChksumFields.setEnabled(false);
            cbLengthField.setEnabled(false);
            cbLengthFields.setEnabled(false);
            cbPseudoHeader.setEnabled(false);
            cbLXload.setEnabled(false);
            btfDelete.setEnabled(false);
            if (messageType.equals("IPv4") || messageType.equals("IPv6")) btSaveas.setEnabled(false);
        } else {
            cbChksumField.setEnabled(true);
            cbLengthField.setEnabled(true);
            cbLXload.setEnabled(true);
        }
        prevItem = cbbTypeData.getSelectedItem().toString();
        String type = cbbTypeData.getSelectedItem().toString();
        if (type.equals("MACaddress") || type.equals("ip4address") || type.equals("ip6address") || cbbTypeData.getSelectedItem().equals("text")) tfbLength.setEditable(false);
    }

    protected void addField(String Name, String TypeField, String Length, String Value, int override, boolean chksum) {
        JTextField tfName;
        tfName = new JTextField(15);
        JTextField tfLength;
        tfLength = new JTextField(3);
        JTextField tfValue = new JTextField(31);
        JComboBox cbValue;
        JComboBox cbTypeField;
        cbTypeField = new JComboBox();
        cbTypeField.addItem("bit");
        cbTypeField.addItem("byte");
        cbTypeField.addItem("text");
        cbTypeField.addItem("MACaddress");
        cbTypeField.addItem("ip4address");
        cbTypeField.addItem("ip6address");
        tfName.setText(Name);
        cbTypeField.setSelectedItem(TypeField);
        tfLength.setText(Length);
        if ((TypeField.equals("MACaddress") || TypeField.equals("ip4address") || TypeField.equals("ip6address")) && !feedBack) {
            tfLength.setEditable(false);
            cbValue = new JComboBox();
            if (TypeField.equals("ip4address")) ipAlias("ip4address", cbValue); else if (TypeField.equals("ip6address")) ipAlias("ip6address", cbValue); else ipAlias("MACaddress", cbValue);
            try {
                String address = "MAC";
                if (TypeField.equals("ip4address")) address = "IPv4"; else if (TypeField.equals("ip6address")) address = "IPv6";
                Document addresses = readFromFile(new File("addresses.xml"));
                cbValue.setSelectedItem(XPathAPI.eval(addresses, "/addresses/" + address + "/field[addr='" + Value + "']/alias").toString());
            } catch (TransformerException te) {
                te.printStackTrace();
            }
            add(new MessageEditorField(this, tfName, cbTypeField, tfLength, cbValue, override, chksum, false), getComponentCount() - 1);
        } else {
            tfValue.setText(Value);
            add(new MessageEditorField(this, tfName, cbTypeField, tfLength, tfValue, override, chksum, false), getComponentCount() - 1);
        }
        if (own.isLockedMessage(messageType) && override != 1) {
            tfName.setEditable(false);
            cbTypeField.setEnabled(false);
            tfLength.setEditable(false);
        }
        if (cbTypeField.getSelectedItem().equals("text")) tfLength.setEditable(false);
        setPreferredSize(new Dimension(getPreferredSize().width, 73 * getComponentCount() + 5));
    }

    protected Document createDocument(int Value) throws MissingException {
        Document document = super.createDocument(Value);
        Element message = document.getDocumentElement();
        Element extend = (Element) document.createElement("extends");
        message.appendChild(extend);
        extend.appendChild(document.createTextNode(cbLXload.getSelectedItem().toString().trim()));
        return document;
    }

    protected void fieldChanged() {
        super.fieldChanged();
        updateChksum();
    }

    protected void createBody() {
        body = new DefaultMutableTreeNode(getComponentCount() + ". " + tfbName.getText());
        String Type = cbbTypeData.getSelectedItem().toString();
        body.add(new DefaultMutableTreeNode("Type: " + Type));
        body.add(new DefaultMutableTreeNode("Length: " + tfbLength.getText()));
        String Value;
        if (Type.equals("MACaddress") || Type.equals("ip4address") || Type.equals("ip6address")) Value = cbbValue.getSelectedItem().toString(); else Value = tfbValue.getText();
        DefaultMutableTreeNode value = new DefaultMutableTreeNode("Value: " + Value);
        body.add(value);
    }

    public void valueChanged(TreeSelectionEvent tse) {
        setFocus(tse);
    }
}
