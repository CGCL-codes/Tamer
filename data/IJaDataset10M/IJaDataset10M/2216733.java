package org.semanticweb.mmm.mr3.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import org.jgraph.graph.*;
import org.semanticweb.mmm.mr3.actions.*;
import org.semanticweb.mmm.mr3.data.*;
import org.semanticweb.mmm.mr3.data.MR3Constants.*;
import org.semanticweb.mmm.mr3.jgraph.*;
import org.semanticweb.mmm.mr3.util.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * 
 * @author takeshi morita
 * 
 */
public class RDFPropertyPanel extends JPanel implements ActionListener, ListSelectionListener {

    private JCheckBox isContainerBox;

    private JSpinner numSpinner;

    private JCheckBox propOnlyCheck;

    private JComboBox uriPrefixBox;

    private JTextField idField;

    private JTextField findIDField;

    private JLabel nsLabel;

    private JButton applyButton;

    private JButton cancelButton;

    private JButton jumpPropertyButton;

    private GraphCell edge;

    private JList localNameList;

    private Map<String, Set<String>> propMap;

    private Set<String> propNameSpaceSet;

    private IconCellRenderer renderer;

    private static Object[] NULL = new Object[0];

    private List<GraphCell> propList;

    private GraphManager gmanager;

    private EditRDFPropertyAction editRDFPropertyAction;

    private static final int FIELD_WIDTH = 50;

    private static final int FIELD_HEIGHT = 20;

    public RDFPropertyPanel(GraphManager manager) {
        gmanager = manager;
        editRDFPropertyAction = new EditRDFPropertyAction(gmanager);
        isContainerBox = new JCheckBox(Translator.getString("IsContainer"));
        isContainerBox.addActionListener(new ContainerBoxAction());
        isContainerBox.setSelected(false);
        numSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        numSpinner.setEnabled(false);
        propOnlyCheck = new JCheckBox(Translator.getString("ShowPropertyPrefixOnly"));
        propOnlyCheck.addActionListener(this);
        propOnlyCheck.setSelected(true);
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
        containerPanel.add(propOnlyCheck);
        containerPanel.add(isContainerBox);
        containerPanel.add(numSpinner);
        uriPrefixBox = new JComboBox();
        uriPrefixBox.addActionListener(new ChangePrefixAction());
        JComponent uriPrefixBoxP = Utilities.createTitledPanel(uriPrefixBox, MR3Constants.PREFIX);
        idField = new JTextField();
        idField.addActionListener(this);
        JComponent idFieldP = Utilities.createTitledPanel(idField, "ID");
        jumpPropertyButton = new JButton(Translator.getString("Property"));
        jumpPropertyButton.addActionListener(this);
        JComponent jumpPropertyButtonP = Utilities.createTitledPanel(jumpPropertyButton, Translator.getString("Jump"));
        JPanel uriPanel = new JPanel();
        uriPanel.setLayout(new GridLayout(1, 3, 5, 5));
        uriPanel.add(uriPrefixBoxP);
        uriPanel.add(idFieldP);
        uriPanel.add(jumpPropertyButtonP);
        nsLabel = new JLabel();
        JComponent nsLabelP = Utilities.createTitledPanel(nsLabel, MR3Constants.NAME_SPACE);
        findIDField = new JTextField();
        findIDField.getDocument().addDocumentListener(new TypePropertyIDAction());
        JComponent findIDFieldP = Utilities.createTitledPanel(findIDField, "Find ID", FIELD_WIDTH, FIELD_HEIGHT);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(nsLabelP, BorderLayout.CENTER);
        panel.add(findIDFieldP, BorderLayout.EAST);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(containerPanel);
        mainPanel.add(uriPanel);
        mainPanel.add(panel);
        mainPanel.add(getSelectPropertyPanel());
        applyButton = new JButton(MR3Constants.APPLY);
        applyButton.setMnemonic('a');
        applyButton.addActionListener(this);
        cancelButton = new JButton(MR3Constants.CANCEL);
        cancelButton.setMnemonic('c');
        cancelButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        setLayout(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(49, 105, 198));
        ImageIcon icon = Utilities.getImageIcon(Translator.getString("RDFEditor.Icon"));
        JLabel titleLabel = new JLabel(Translator.getString("AttributeDialog.RDFPropertyAttribute.Text"), icon, SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(Utilities.createEastPanel(buttonPanel), BorderLayout.SOUTH);
    }

    class TypePropertyIDAction implements DocumentListener {

        private void selectLocalName() {
            selectNameSpaceList();
            ListModel listModel = localNameList.getModel();
            List<Object> list = new ArrayList<Object>();
            String idStr = findIDField.getText();
            for (int i = 0; i < listModel.getSize(); i++) {
                String elementStr = listModel.getElementAt(i).toString();
                if (elementStr.length() < idStr.length()) {
                    continue;
                }
                if (elementStr.substring(0, idStr.length()).matches(idStr)) {
                    list.add(listModel.getElementAt(i));
                }
            }
            localNameList.setListData(list.toArray());
            if (0 < list.size()) {
                localNameList.setSelectedIndex(0);
            }
        }

        public void changedUpdate(DocumentEvent e) {
            selectLocalName();
        }

        public void insertUpdate(DocumentEvent e) {
            selectLocalName();
        }

        public void removeUpdate(DocumentEvent e) {
            selectLocalName();
        }
    }

    private void setContainer(boolean t) {
        numSpinner.setEnabled(t);
        propOnlyCheck.setEnabled(!t);
        uriPrefixBox.setEnabled(!t);
        idField.setEnabled(!t);
        nsLabel.setEnabled(!t);
        findIDField.setEditable(!t);
        jumpPropertyButton.setEnabled(!t);
        localNameList.setEnabled(!t);
    }

    private boolean isContainer() {
        return isContainerBox.isSelected();
    }

    class ContainerBoxAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            setContainer(isContainer());
        }
    }

    class ChangePrefixAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            selectNameSpaceList();
        }
    }

    private JComponent getSelectPropertyPanel() {
        localNameList = new JList();
        localNameList.addListSelectionListener(this);
        JScrollPane localNameListScroll = new JScrollPane(localNameList);
        localNameListScroll.setBorder(BorderFactory.createTitledBorder(Translator.getString("Property") + " ID"));
        renderer = new IconCellRenderer();
        localNameList.setCellRenderer(renderer);
        return localNameListScroll;
    }

    private static final String NULL_LOCAL_NAME = "(Null)";

    private void setURIPrefixBoxModel() {
        if (propList != null && propOnlyCheck.isSelected()) {
            uriPrefixBox.setModel(new DefaultComboBoxModel(PrefixNSUtil.getPropPrefixes(propList).toArray()));
        } else {
            uriPrefixBox.setModel(new DefaultComboBoxModel(PrefixNSUtil.getPrefixes().toArray()));
        }
    }

    private void setPrefix() {
        setURIPrefixBoxModel();
        for (PrefixNSInfo prefNSInfo : GraphUtilities.getPrefixNSInfoSet()) {
            if (prefNSInfo.getNameSpace().equals(nsLabel.getText())) {
                uriPrefixBox.setSelectedItem(prefNSInfo.getPrefix());
                break;
            }
        }
    }

    private void selectNameSpaceList() {
        PrefixNSUtil.replacePrefix((String) uriPrefixBox.getSelectedItem(), nsLabel);
        if (propMap == null) {
            return;
        }
        if (!localNameList.isSelectionEmpty()) {
            localNameList.clearSelection();
        }
        String nameSpace = nsLabel.getText();
        Set<String> localNames = propMap.get(nameSpace);
        if (localNames == null) {
            localNameList.setListData(NULL);
            return;
        }
        Set<String> modifyLocalNames = new TreeSet<String>();
        for (String localName : localNames) {
            if (localName.length() == 0) {
                modifyLocalNames.add(NULL_LOCAL_NAME);
            } else {
                modifyLocalNames.add(localName);
            }
        }
        localNameList.setListData(modifyLocalNames.toArray());
        localNameList.setSelectedValue(idField.getText(), true);
    }

    private void setNSLabel(String str) {
        nsLabel.setText(str);
        nsLabel.setToolTipText(str);
    }

    private void selectLocalNameList() {
        if (localNameList.getSelectedValue() != null) {
            String ln = localNameList.getSelectedValue().toString();
            if (ln.equals(NULL_LOCAL_NAME)) {
                ln = "";
            }
            idField.setText(ln);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        try {
            if (e.getSource() == localNameList) {
                selectLocalNameList();
            }
        } catch (NullPointerException np) {
            np.printStackTrace();
        }
    }

    public void setValue(GraphCell c, PropertyInfo info) {
        edge = c;
        if (info == null) {
            setNSLabel(MR3Resource.Nil.getNameSpace());
            idField.setText(MR3Resource.Nil.getLocalName());
            editRDFPropertyAction.setURIString(getURI());
            editRDFPropertyAction.setEdge(edge);
            editRDFPropertyAction.editRDFProperty();
        } else {
            isContainerBox.setSelected(info.isContainer());
            setContainer(info.isContainer());
            if (info.isContainer()) {
                numSpinner.setValue(new Integer(info.getNum()));
            } else {
                if (info.getURIStr().equals(MR3Resource.Nil.getURI())) {
                    if (0 < propList.size()) {
                        GraphCell cell = propList.get(0);
                        RDFSInfo propInfo = (RDFSInfo) GraphConstants.getValue(cell.getAttributes());
                        setNSLabel(propInfo.getURI().getNameSpace());
                    } else {
                        setNSLabel(gmanager.getBaseURI());
                    }
                    idField.setText("");
                } else {
                    setNSLabel(info.getNameSpace());
                    idField.setText(info.getLocalName());
                }
            }
        }
        setPrefix();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                idField.requestFocus();
            }
        });
    }

    public void setPropertyList(List<GraphCell> plist) {
        propList = plist;
        propMap = new HashMap<String, Set<String>>();
        propNameSpaceSet = new HashSet<String>();
        for (GraphCell cell : propList) {
            RDFSInfo info = (RDFSInfo) GraphConstants.getValue(cell.getAttributes());
            Resource uri = info.getURI();
            propNameSpaceSet.add(uri.getNameSpace());
            Set<String> localNames = propMap.get(uri.getNameSpace());
            if (localNames == null) {
                localNames = new HashSet<String>();
                propMap.put(uri.getNameSpace(), localNames);
            }
            localNames.add(uri.getLocalName());
        }
        selectNameSpaceList();
    }

    private String getURI() {
        return nsLabel.getText() + idField.getText();
    }

    private void jumpRDFSProperty() {
        Resource uri = ResourceFactory.createResource(nsLabel.getText() + idField.getText());
        if (gmanager.isEmptyURI(uri.getURI())) {
            return;
        }
        RDFSInfoMap rdfsInfoMap = gmanager.getCurrentRDFSInfoMap();
        if (rdfsInfoMap.isPropertyCell(uri)) {
            Object propertyCell = rdfsInfoMap.getPropertyCell(uri);
            gmanager.selectPropertyCell(propertyCell);
        } else {
            JOptionPane.showMessageDialog(gmanager.getDesktopTabbedPane(), Translator.getString("Warning.Message3"), Translator.getString("Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setContainerMemberProperty() {
        Integer num = (Integer) numSpinner.getValue();
        Resource resource = ResourceFactory.createResource(RDF.getURI() + "_" + num.intValue());
        GraphCell propertyCell = gmanager.getPropertyCell(resource, false);
        RDFSInfo rdfsInfo = (RDFSInfo) GraphConstants.getValue(propertyCell.getAttributes());
        GraphConstants.setValue(edge.getAttributes(), rdfsInfo);
        gmanager.getCurrentRDFGraph().getGraphLayoutCache().editCell(edge, edge.getAttributes());
    }

    public void actionPerformed(ActionEvent e) {
        if (edge == null) {
            return;
        }
        if (e.getSource() == applyButton || e.getSource() == idField) {
            RDFSInfo beforeRDFSInfo = (RDFSInfo) GraphConstants.getValue(edge.getAttributes());
            String beforeProperty = beforeRDFSInfo.getURIStr();
            if (isContainer()) {
                setContainerMemberProperty();
            } else {
                editRDFPropertyAction.setURIString(getURI());
                editRDFPropertyAction.setEdge(edge);
                editRDFPropertyAction.editRDFProperty();
                findIDField.setText("");
                gmanager.selectRDFCell(edge);
            }
            RDFSInfo afterRDFSInfo = (RDFSInfo) GraphConstants.getValue(edge.getAttributes());
            String afterProperty = afterRDFSInfo.getURIStr();
            HistoryManager.saveHistory(HistoryType.EDIT_PROPERTY_WITH_DIAGLOG, beforeProperty, afterProperty);
        } else if (e.getSource() == propOnlyCheck) {
            setURIPrefixBoxModel();
            selectNameSpaceList();
        } else if (e.getSource() == jumpPropertyButton) {
            jumpRDFSProperty();
        } else if (e.getSource() == cancelButton) {
            gmanager.setVisibleAttrDialog(false);
        }
    }

    /**
     * �C���[�W�t�����X�g��`��
     */
    class IconCellRenderer extends JLabel implements ListCellRenderer {

        IconCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String str = value.toString();
            setText(str);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            return this;
        }
    }
}
