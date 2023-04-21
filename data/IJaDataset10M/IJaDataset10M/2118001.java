package issrg.policytester;

import issrg.pba.rbac.xmlpolicy.EscapedStringTokenizer;
import issrg.utils.clipboard.DefaultPopupMenu;
import issrg.utils.clipboard.PopupListener;
import issrg.utils.xml.CommaSeparatedListCreator;
import issrg.utils.gui.xml.NodeItemList;
import issrg.utils.gui.xml.NodeVector;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Christian Azzopardi
 */
public class ActionTab extends NodeItemList {

    private JLabel nickLabel;

    private JTextField nickName;

    private PTComponent ptc;

    private DefaultPopupMenu dpm;

    JPanel oldDisplay;

    ActionTable actionTable;

    ResourceBundle rb = ResourceBundle.getBundle("issrg/policytester/PTIFConditions_i18n");

    ResourceBundle rbl = ResourceBundle.getBundle("issrg/policytester/PTComponent_i18n");

    String enterNickCaption = rbl.getString("ActionTab_Enter_Nickname");

    String envParamCaption = rbl.getString("ActionTab_Parameter");

    String valueCaption = rbl.getString("ActionTab_Environment_Value");

    String valueTTipCaption = rbl.getString("ActionTab_Value_ToolTip");

    String availableCaption = rbl.getString("ActionTab_Border_Available_Actions");

    String addCaption = rbl.getString("ActionTab_Button_Add");

    String deleteCaption = rbl.getString("ActionTab_Button_Delete");

    String replaceCaption = rbl.getString("ActionTab_Button_Replace");

    String addTTCaption = rbl.getString("ActionTab_Button_Add_TTip");

    String deleteTTCaption = rbl.getString("ActionTab_Button_Delete_TTip");

    String replaceTTCaption = rbl.getString("ActionTab_Button_Replace_TTip");

    String errorHeader = rbl.getString("ErrorHeader");

    String errorMSG1 = rbl.getString("ActionTab_Error1");

    String errorMSG2 = rbl.getString("ActionTab_Error2");

    String errorMSG3 = rbl.getString("ActionTab_Error3");

    String errorMSG4 = rbl.getString("ActionTab_Error4");

    String modifyHereCaption = rbl.getString("Modify_Here");

    String clickHereCaption = rbl.getString("Click_Here");

    /** Creates a new instance of ActionTab */
    public ActionTab(PTComponent that) {
        super(that);
        ptc = that;
        this.setLayout(new BorderLayout());
        this.add(getContentPanel(), BorderLayout.CENTER);
        xmlED.addXMLChangeListener(this);
        replaceButton.setEnabled(false);
    }

    public JPanel getContentPanel() {
        JPanel actionTabPanel = new JPanel(new GridBagLayout());
        oldDisplay = super.getContentPanel();
        nickLabel = new JLabel(enterNickCaption);
        nickName = new JTextField();
        dpm = new DefaultPopupMenu(nickName);
        MouseListener popupListener = new PopupListener(dpm);
        nickName.addMouseListener(popupListener);
        JPanel topPanel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Parameters for the Action");
        JPanel midPanel = new JPanel(new GridBagLayout());
        midPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Action Table"));
        addComponent(nickLabel, topPanel, 1, 0, 1, 1, GridBagConstraints.LINE_START, 0, 0, GridBagConstraints.NONE);
        addComponent(nickName, topPanel, 1, 1, 0, 1, GridBagConstraints.LINE_START, 0.6, 0, GridBagConstraints.HORIZONTAL);
        addComponent(topPanel, actionTabPanel, 0, 0, 3, 1, GridBagConstraints.CENTER, 1, 0, GridBagConstraints.HORIZONTAL);
        setCaption("ADD_BUTTON", addCaption);
        setCaption("DELETE_BUTTON", deleteCaption);
        setCaption("REPLACE_BUTTON", replaceCaption);
        addButton.setToolTipText(addTTCaption);
        deleteButton.setToolTipText(deleteTTCaption);
        replaceButton.setToolTipText(replaceTTCaption);
        constraints.insets = new Insets(3, 0, 0, 0);
        actionTable = new ActionTable(null);
        addComponent(actionTable.getContentPanel(), actionTabPanel, 1, 0, 1, 0, GridBagConstraints.WEST, 0.5, 1, GridBagConstraints.BOTH);
        actionTable.addRow(0);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        addComponent(removeAddButton(), buttonPanel, 0, 0, 1, 1, GridBagConstraints.CENTER, 1, 0, GridBagConstraints.HORIZONTAL);
        addComponent(removeReplaceButton(), buttonPanel, 1, 0, 1, 1, GridBagConstraints.CENTER, 1, 0, GridBagConstraints.HORIZONTAL);
        addComponent(removeDeleteButton(), buttonPanel, 2, 0, 1, 1, GridBagConstraints.CENTER, 1, 0, GridBagConstraints.HORIZONTAL);
        addComponent(buttonPanel, actionTabPanel, 1, 1, 1, 1, GridBagConstraints.CENTER, 0, 0, GridBagConstraints.NONE);
        oldDisplay.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), availableCaption));
        addComponent(oldDisplay, actionTabPanel, 1, 2, 1, 1, GridBagConstraints.CENTER, 0.5, 1, GridBagConstraints.BOTH);
        return actionTabPanel;
    }

    public void addItem() {
        if (nickName.getText().equals("")) {
            JOptionPane.showMessageDialog(xmlED, errorMSG1, errorHeader, JOptionPane.ERROR_MESSAGE);
            nickName.grabFocus();
            return;
        }
        if (actionTable.addl.getSelectedItem().equals("")) {
            int response = JOptionPane.showInternalConfirmDialog(this, "Do you want to have an empty action?", "Confirm Closing Operation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == 0) {
                actionTable.noParams.setSelected(true);
                add();
            }
        } else {
            add();
        }
    }

    public void add() {
        Element actList = null;
        NodeList actionList = ((Element) getParentNode()).getElementsByTagName("ActionList");
        if (actionList.getLength() == 0) {
            actList = xmlED.DOM.createElement("ActionList");
        }
        NodeList presentActions = ((Element) getParentNode()).getElementsByTagName("ActionSpec");
        String actionsList[] = new String[presentActions.getLength()];
        for (int i = 0; i < presentActions.getLength(); i++) {
            actionsList[i] = ((Element) presentActions.item(i)).getAttribute("NickName");
        }
        for (int j = 0; j < actionsList.length; j++) {
            if ((actionsList[j].compareToIgnoreCase(nickName.getText())) == 0) {
                JOptionPane.showMessageDialog(xmlED, errorMSG2, errorHeader, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Element child = xmlED.DOM.createElement("ActionSpec");
        child.setAttribute("NickName", nickName.getText());
        if (actionTable.addl.getSelectedItem() != null) {
            child.setAttribute("Action", (String) actionTable.addl.getSelectedItem());
        } else {
            child.setAttribute("Action", nickName.getText());
            actionTable.addl.addItem(nickName.getText());
        }
        Vector params = new Vector();
        Vector values = new Vector();
        Vector types = new Vector();
        if (!actionTable.noParams.isSelected()) {
            for (int i = 0; i < actionTable.table.getRowCount(); i++) {
                String param = ((JTextField) actionTable.column1Store.get(i)).getText();
                String value = ((JTextField) actionTable.column4Store.get(i)).getText();
                String type = ((String) ((JComboBox) actionTable.column2Store.get(i)).getSelectedItem());
                if (param.equals("")) {
                    JOptionPane.showMessageDialog(xmlED, errorMSG3 + " " + (i + 1), errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (value.equals("") || value.equals(modifyHereCaption) || value.equals(clickHereCaption)) {
                    JOptionPane.showMessageDialog(xmlED, errorMSG4 + " " + (i + 1), errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((type).intern().equals(rb.getString("type1")) || (type).intern().equals(rb.getString("type2")) || (type).intern().equals(rb.getString("type7"))) value = getAbsoluteTime(value);
                params.add(param);
                values.add(value);
                types.add(type);
            }
            String param = new CommaSeparatedListCreator(params).getResult();
            String value = new CommaSeparatedListCreator(values).getResult();
            String type = new CommaSeparatedListCreator(types).getResult();
            child.setAttribute("Parameter", param);
            child.setAttribute("Value", value);
            child.setAttribute("Type", type);
        }
        if (actList == null) {
            xmlED.addItem(child, (Element) actionList.item(0));
        } else {
            actList.appendChild(child);
            xmlED.addItem(actList, (Element) getParentNode());
        }
        setSelectedNode(child);
    }

    public String getAbsoluteTime(String s) {
        if (s.indexOf(":") == -1) {
            return s + "T**:**:**";
        } else if (s.indexOf("-") == -1) {
            return "****-**-**T" + s;
        } else return s;
    }

    public void replaceItem() {
        if (nickName.getText().equals("")) {
            JOptionPane.showMessageDialog(xmlED, errorMSG1, errorHeader, JOptionPane.ERROR_MESSAGE);
            nickName.grabFocus();
            return;
        }
        NodeList actionList = ((Element) getParentNode()).getElementsByTagName("ActionList");
        NodeList presentActions = ((Element) getParentNode()).getElementsByTagName("ActionSpec");
        String actionsList[] = new String[presentActions.getLength()];
        for (int i = 0; i < presentActions.getLength(); i++) {
            actionsList[i] = ((Element) presentActions.item(i)).getAttribute("NickName");
        }
        Element child = xmlED.DOM.createElement("ActionSpec");
        child.setAttribute("NickName", nickName.getText());
        child.setAttribute("Action", (String) actionTable.addl.getSelectedItem());
        if (!actionTable.noParams.isSelected()) {
            Vector params = new Vector();
            Vector values = new Vector();
            Vector types = new Vector();
            for (int i = 0; i < actionTable.table.getRowCount(); i++) {
                String param = ((JTextField) actionTable.column1Store.get(i)).getText();
                String value = ((JTextField) actionTable.column4Store.get(i)).getText();
                String type = (String) ((ParameterTypesDropDownList) actionTable.column2Store.get(i)).getSelectedItem();
                if (param.equals("")) {
                    JOptionPane.showMessageDialog(xmlED, errorMSG3 + " " + (i + 1), errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (value.equals("") || value.equals(modifyHereCaption) || value.equals(clickHereCaption)) {
                    JOptionPane.showMessageDialog(xmlED, errorMSG4 + " " + (i + 1), errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((type).intern().equals(rb.getString("type1")) || (type).intern().equals(rb.getString("type2")) || (type).intern().equals(rb.getString("type7"))) value = getAbsoluteTime(value);
                params.add(param);
                values.add(value);
                types.add(type);
            }
            String param = new CommaSeparatedListCreator(params).getResult();
            String value = new CommaSeparatedListCreator(values).getResult();
            String type = new CommaSeparatedListCreator(types).getResult();
            child.setAttribute("Parameter", param);
            child.setAttribute("Value", value);
            child.setAttribute("Type", type);
        }
        xmlED.replaceNode((Element) actionList.item(0), (Element) getSelectedNode(), child);
        setSelectedNode(child);
        replaceButton.setEnabled(false);
    }

    public void deleteItem() {
        if (this.getSelectedNode() != null) {
            Element actionList = (Element) ((Element) getParentNode()).getElementsByTagName("ActionList").item(0);
            if (NodeVector.getChildElements(actionList).getLength() > 1) {
                xmlED.deleteItem((Element) this.getSelectedNode(), actionList);
            } else {
                xmlED.deleteItem(actionList, ((Element) getParentNode()));
                listData = new String[0];
                listBox.setListData(listData);
                actionTable.addl.setSelectedItem("");
            }
        }
    }

    public void deleteAllRows() {
        for (int i = actionTable.table.getRowCount() - 1; i >= 0; i--) {
            actionTable.deleteRow(i);
        }
    }

    public void itemSelected() {
        Element name;
        super.itemSelected();
        actionTable.noParams.setSelected(false);
        if (getSelectedNode() == null) {
            nickName.setText("");
            actionTable.addl.setSelectedIndex(-1);
            deleteAllRows();
            actionTable.addRow(0);
            replaceButton.setEnabled(false);
        } else {
            nickName.setText(((Element) this.getSelectedNode()).getAttribute("NickName"));
            actionTable.addl.setSelectedItem(((Element) this.getSelectedNode()).getAttribute("Action"));
            deleteAllRows();
            String params = ((Element) this.getSelectedNode()).getAttribute("Parameter");
            String values = ((Element) this.getSelectedNode()).getAttribute("Value");
            String types = ((Element) this.getSelectedNode()).getAttribute("Type");
            if (params.equals("") || values.equals("")) {
                actionTable.addRow(0);
                actionTable.noParams.setSelected(true);
            } else {
                java.util.Enumeration argum = new EscapedStringTokenizer(params, ',');
                Vector paramVector = new Vector();
                while (argum.hasMoreElements()) {
                    paramVector.add((String) argum.nextElement());
                }
                String[] param = new String[paramVector.size()];
                for (int i = 0; i < paramVector.size(); i++) {
                    param[i] = (String) paramVector.get(i);
                }
                argum = new EscapedStringTokenizer(values, ',');
                Vector valuesVector = new Vector();
                while (argum.hasMoreElements()) {
                    valuesVector.add((String) argum.nextElement());
                }
                String[] value = new String[valuesVector.size()];
                for (int i = 0; i < valuesVector.size(); i++) {
                    value[i] = (String) valuesVector.get(i);
                }
                argum = new EscapedStringTokenizer(types, ',');
                Vector typesVector = new Vector();
                while (argum.hasMoreElements()) {
                    typesVector.add((String) argum.nextElement());
                }
                String[] type = new String[typesVector.size()];
                for (int i = 0; i < typesVector.size(); i++) {
                    type[i] = (String) typesVector.get(i);
                }
                for (int i = 0; i < param.length; i++) {
                    actionTable.addRow(i);
                    actionTable.table.setEditingRow(i);
                    if (value[i].indexOf("****-**-**T") > -1) {
                        value[i] = value[i].substring(value[i].indexOf("T") + 1);
                        ((JComboBox) actionTable.column2Store.get(i)).setSelectedItem(rb.getString("type1"));
                    } else if (value[i].indexOf("T**:**:**") > -1) {
                        value[i] = value[i].substring(0, value[i].indexOf("T"));
                        ((JComboBox) actionTable.column2Store.get(i)).setSelectedItem(rb.getString("type2"));
                    } else if (value[i].indexOf('-') == 5 && value[i].indexOf('T') == 11 && value[i].indexOf(':') == 14) {
                        ((JComboBox) actionTable.column2Store.get(i)).setSelectedItem(rb.getString("type7"));
                    }
                    ((JTextField) actionTable.column1Store.get(i)).setText(param[i]);
                    ((JComboBox) actionTable.column2Store.get(i)).setSelectedItem(type[i]);
                    ((JTextField) actionTable.column4Store.get(i)).setText(value[i]);
                }
            }
            replaceButton.setEnabled(true);
        }
    }

    public void refreshView() {
        if (xmlED == null || xmlED.DOM == null) return;
        if (getParentNode() != null) {
            Element actionList = (Element) ((Element) getParentNode()).getElementsByTagName("ActionList").item(0);
            if (actionList == null) return;
            Node n;
            NodeList nlist = actionList.getElementsByTagName("ActionSpec");
            String subjects[] = new String[nlist.getLength()];
            for (int i = 0; i < nlist.getLength(); i++) {
                n = nlist.item(i);
                subjects[i] = (((Element) n).getAttribute("NickName"));
            }
            setNodeList(nlist, subjects);
        }
    }

    public Node getParentNode() {
        return xmlED.DOM.getElementsByTagName("Action").item(0);
    }
}
