package issrg.editor2.configurations;

import issrg.utils.gui.AbstractConfigComponent;
import issrg.utils.gui.xml.NodeItemList;
import issrg.utils.gui.xml.XMLChangeEvent;
import issrg.utils.gui.xml.XMLChangeListener;
import issrg.utils.gui.xml.XMLEditor;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author  bn29
 */
public class CoordParameterPanel extends NodeItemList implements XMLChangeListener, ActionListener, ItemListener {

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField4;

    private javax.swing.JTextField jTextField5;

    private javax.swing.JComboBox jCombType;

    private javax.swing.JList jList1;

    private AbstractConfigComponent xmlED;

    DefaultListModel model = new DefaultListModel();

    String oldNodeVar = null;

    String newNodeVar = null;

    /** Creates new form NewJPanel */
    public CoordParameterPanel(AbstractConfigComponent editor) {
        super(editor);
        this.xmlED = editor;
        xmlED.addXMLChangeListener(this);
        this.setLayout(new BorderLayout());
        this.add(getContentPanel(), BorderLayout.CENTER);
        refreshView();
    }

    public JPanel getContentPanel() {
        JPanel contentPanel = new javax.swing.JPanel(new GridBagLayout());
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField1.setColumns(3);
        jTextField2 = new javax.swing.JTextField();
        jTextField2.setColumns(3);
        jTextField3 = new javax.swing.JTextField();
        jTextField3.setColumns(3);
        jTextField4 = new javax.swing.JTextField();
        jTextField4.setColumns(3);
        jTextField5 = new javax.swing.JTextField();
        jTextField5.setColumns(3);
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jLabel1.setText("Coordination parameter name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 0);
        contentPanel.add(jLabel1, gridBagConstraints);
        jTextField1.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 169;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 0);
        contentPanel.add(jTextField1, gridBagConstraints);
        jLabel6.setText("DataType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 0);
        contentPanel.add(jLabel6, gridBagConstraints);
        jCombType = new javax.swing.JComboBox();
        jCombType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Integer", "Double", "String" }));
        jCombType.addActionListener(this);
        jCombType.setActionCommand("COMBO_OP_SELECTED");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 0);
        contentPanel.add(jCombType, gridBagConstraints);
        jPanel1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Including these information:"));
        jTextField2.setText("");
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 199;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 10);
        jPanel1.add(jTextField2, gridBagConstraints);
        jLabel2.setText("Subject Attributes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);
        jLabel3.setText("Action Attributes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 10, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);
        jLabel4.setText("Resource Attributes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 10, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);
        jLabel5.setText("Environment Attributes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 10, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);
        jTextField3.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 199;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 0, 10);
        jPanel1.add(jTextField3, gridBagConstraints);
        jTextField4.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 199;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 0, 10);
        jPanel1.add(jTextField4, gridBagConstraints);
        jTextField5.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 199;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 21, 10);
        jPanel1.add(jTextField5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 57;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 20, 0, 18);
        contentPanel.add(jPanel1, gridBagConstraints);
        jButton1.setText("Create");
        jButton1.addActionListener(this);
        jButton1.setActionCommand("Create");
        jButton1.setMnemonic(KeyEvent.VK_P);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 120, 17, 0);
        contentPanel.add(jButton1, gridBagConstraints);
        jButton3.setText("Modify");
        jButton3.addActionListener(this);
        jButton3.setActionCommand("Modify");
        jButton3.setMnemonic(KeyEvent.VK_P);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 17, 0);
        contentPanel.add(jButton3, gridBagConstraints);
        jButton2.setText("Delete");
        jButton2.addActionListener(this);
        jButton2.setActionCommand("Delete");
        jButton2.setMnemonic(KeyEvent.VK_P);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 17, 0);
        contentPanel.add(jButton2, gridBagConstraints);
        jList1 = new javax.swing.JList();
        model = new DefaultListModel();
        jList1.setModel(model);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListValueChanged(evt);
            }
        });
        jList1.setSize(20, 20);
        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setAutoscrolls(true);
        scrollPanel.setViewportView(jList1);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 17, 0);
        contentPanel.add(scrollPanel, gridBagConstraints);
        return contentPanel;
    }

    void jListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting() == false) {
            String var = (String) jList1.getSelectedValue();
            if ((var == null) && (model.isEmpty() == false)) {
                var = (String) model.getElementAt(model.getSize() - 1);
            }
            fillFieldswithText(var);
        }
    }

    public void XMLChanged(XMLChangeEvent ev) {
        refreshView();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Create")) {
            if (jTextField1.getText().equals("")) {
                JOptionPane.showMessageDialog(xmlED, "Parameter has no name", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String var = getVar();
            int i = existInList(var);
            if (i == -1) {
                model.addElement(var);
                addItem();
            }
        }
        if (e.getActionCommand().equals("Modify")) {
            if (jTextField1.getText().equals("")) {
                JOptionPane.showMessageDialog(xmlED, "Parameter has no name", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int j = jList1.getSelectedIndex();
            if (j != -1) {
                newNodeVar = getVar();
                oldNodeVar = (String) model.getElementAt(j);
                model.setElementAt(newNodeVar, j);
                replaceItem();
                fillFieldswithText(newNodeVar);
            } else JOptionPane.showMessageDialog(xmlED, "No parameter selected from the list", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        if (e.getActionCommand().equals("Delete")) {
            if (model.isEmpty() == false) {
                deleteItem();
            }
        }
    }

    private String getVar() {
        String var;
        String name = jTextField1.getText();
        var = name + "[";
        String subjectvar = parseParamters(jTextField2.getText(), "(S)");
        if (subjectvar != null) var = var + subjectvar;
        String actionvar = parseParamters(jTextField3.getText(), "(A)");
        if (actionvar != null) var = var + "," + actionvar;
        String resourcevar = parseParamters(jTextField4.getText(), "(R)");
        if (resourcevar != null) var = var + "," + resourcevar;
        String envvar = parseParamters(jTextField5.getText(), "(E)");
        if (envvar != null) var = var + "," + envvar;
        var = var + "]";
        String type = (String) jCombType.getSelectedItem();
        var = var + ";" + type;
        return var;
    }

    String parseParamters(String allAtts, String extension) {
        String result = null;
        if (allAtts.equals("")) {
            return result;
        } else {
            String[] mulAtts = allAtts.split(",", 0);
            if (mulAtts != null) {
                result = mulAtts[0] + extension;
                if (mulAtts.length > 1) result = result + ",";
                for (int i = 1; i < mulAtts.length; i++) {
                    result = result + mulAtts[i] + extension;
                    if (i != mulAtts.length - 1) result = result + ",";
                }
            }
            return result;
        }
    }

    public int existInList(String var) {
        int i = 0;
        for (i = 0; i < model.getSize(); i++) {
            if (var.equals(model.getElementAt(i))) return i;
        }
        return -1;
    }

    void fillFieldswithText(String value) {
        if (value != null) {
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("");
            jCombType.setSelectedItem("");
            String[] parts = value.split("\\[", 0);
            if (parts != null) {
                jTextField1.setText(parts[0].substring(0, parts[0].length()));
                String[] argsType = parts[1].split(";", 0);
                String[] args = argsType[0].substring(0, argsType[0].length() - 1).split(",", 0);
                if ((args != null) && (args.length != 0)) {
                    if (!args[0].equals("")) {
                        for (int i = 0; i < args.length; i++) {
                            String tmpString = args[i].substring(args[i].length() - 3, args[i].length());
                            if (tmpString.equals("(S)")) {
                                if (jTextField2.getText().equals("")) jTextField2.setText(args[i].substring(0, args[i].length() - 3)); else jTextField2.setText(jTextField2.getText() + "," + args[i].substring(0, args[i].length() - 3));
                            }
                            if (tmpString.equals("(A)")) {
                                if (jTextField3.getText().equals("")) jTextField3.setText(args[i].substring(0, args[i].length() - 3)); else jTextField3.setText(jTextField3.getText() + "," + args[i].substring(0, args[i].length() - 3));
                            }
                            if (tmpString.equals("(R)")) {
                                if (jTextField4.getText().equals("")) jTextField4.setText(args[i].substring(0, args[i].length() - 3)); else jTextField4.setText(jTextField4.getText() + "," + args[i].substring(0, args[i].length() - 3));
                            }
                            if (tmpString.equals("(E)")) {
                                if (jTextField5.getText().equals("")) jTextField5.setText(args[i].substring(0, args[i].length() - 3)); else jTextField5.setText(jTextField5.getText() + "," + args[i].substring(0, args[i].length() - 3));
                            }
                        }
                    }
                    jCombType.setSelectedItem(argsType[1]);
                } else {
                    jTextField1.setText("");
                    jTextField2.setText("");
                    jTextField3.setText("");
                    jTextField4.setText("");
                    jTextField5.setText("");
                    jCombType.setSelectedItem("");
                }
            }
        } else {
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("");
            jCombType.setSelectedItem("");
        }
    }

    public void refreshView() {
        NodeList coordList = xmlED.DOM.getElementsByTagName("CoordinationParameters");
        if (coordList != null) {
            Node root = coordList.item(0);
            if (root != null) {
                NodeList nlist = ((Element) root).getElementsByTagName("Variable");
                model.clear();
                for (int i = 0; i < nlist.getLength(); i++) {
                    model.addElement(((Element) nlist.item(i)).getAttribute("Name") + ";" + ((Element) nlist.item(i)).getAttribute("DataType"));
                }
            }
        }
    }

    public void addItem() {
        if (model.isEmpty()) {
            return;
        }
        Node root = xmlED.DOM.getElementsByTagName("CoordinationParameters").item(0);
        int size = model.getSize();
        for (int i = 0; i < size; i++) {
            int j = -1;
            String selection = (String) model.getElementAt(i);
            String name = selection.split(";", 0)[0];
            String type = selection.split(";", 0)[1];
            if (selection != null) {
                Element child1 = xmlED.DOM.createElement("Variable");
                child1.setAttribute("Name", name);
                child1.setAttribute("DataType", type);
                Element exChild1 = (Element) isInPolicy(child1);
                if (exChild1 == null) {
                    xmlED.addItem(child1, (Element) getParentNode());
                }
            }
        }
        refreshView();
    }

    /**
     * This method can return true, if the variable exists
     * already. If it does not exist it will return false.
     * This is used as a measure so that there will be no double entries.
     *
     * @param childToCheck   The element variable to check
     *                       
     *
     * @return   a boolean exists or not in the
     *           'pe.cfg' file.
     */
    public Node isInPolicy(Element childToCheck) {
        NodeList nlist = null;
        if (getParentNode() != null) {
            nlist = ((Element) getParentNode()).getElementsByTagName("Variable");
            if (nlist != null) {
                for (int i = 0; i < nlist.getLength(); i++) {
                    Element child = (Element) nlist.item(i);
                    boolean comparename = childToCheck.getAttribute("Name").equals(child.getAttribute("Name"));
                    String childType = child.getAttribute("DataType");
                    String childToCheckType = childToCheck.getAttribute("DataType");
                    boolean compareType = childToCheckType.equals(childType);
                    if (comparename && compareType) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    /**
     * The root node of the Current Document.
     *
     * @return   a node with the root Element of the 'pe.cfg' XML file.
     */
    public Node getParentNode() {
        return xmlED.DOM.getElementsByTagName("CoordinationParameters").item(0);
    }

    public void replaceItem() {
        Element elem = xmlED.DOM.createElement("Variable");
        String[] nameType = oldNodeVar.split(";", 0);
        if ((nameType != null) && (nameType.length > 1)) {
            elem.setAttribute("Name", nameType[0]);
            elem.setAttribute("DataType", nameType[1]);
            Node exNode = isInPolicy(elem);
            if (exNode != null) {
                String values[] = { newNodeVar.split(";", 0)[0], newNodeVar.split(";", 0)[1] };
                String atts[] = { "Name", "DataType" };
                xmlED.setAttributeValue((Element) exNode, atts, values);
            }
            refreshView();
        }
    }

    public void deleteItem() {
        int j = jList1.getSelectedIndex();
        if (j != -1) {
            int i = jList1.getSelectedIndex();
            Element child1 = (Element) xmlED.DOM.createElement("Variable");
            String[] nameType = ((String) model.getElementAt(i)).split(";", 0);
            if ((nameType != null) && (nameType.length > 1)) {
                child1.setAttribute("Name", nameType[0]);
                child1.setAttribute("DataType", nameType[1]);
                Element selectedNode = (Element) isInPolicy(child1);
                model.removeElementAt(i);
                xmlED.deleteItem(selectedNode, (Element) getParentNode());
            }
        }
        refreshView();
    }

    public void itemStateChanged(ItemEvent e) {
    }
}
