package org.lds.wilmington.christiana.preparedness.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.lds.wilmington.christiana.preparedness.data.Family;
import org.lds.wilmington.christiana.preparedness.gui.layout.FormConstraints;
import org.lds.wilmington.christiana.preparedness.gui.layout.FormLayout;

/**
 * @author Jay Askren
 * 
 * */
public class ResourcePanel extends JPanel {

    private ResourceBundle _resourceBundle = null;

    private JPanel _mainPanel = null;

    private JPanel _transportationPanel = null;

    private JPanel _toolsPanel = null;

    private JPanel _emergencyPanel = null;

    private JPanel _communicationsPanel = null;

    private JPanel _miscellaneousEquipment = null;

    private JPanel _specializedEquipment = null;

    private JPanel _beddingPanel = null;

    private JPanel _heavyEquipmentPanel = null;

    private JPanel _foodCookingPanel = null;

    private JTextArea _otherTextArea = null;

    private Map _checkboxHashMap = null;

    private Map _textFieldHashMap = null;

    private int _headingSize = 12;

    private Family _curentFamily = null;

    /**
	 * @param args
	 */
    public ResourcePanel() {
        super();
        _resourceBundle = ResourceBundle.getBundle("org.lds.wilmington.christiana.preparedness.gui.GuiResourceBundle");
        _checkboxHashMap = new TreeMap();
        _textFieldHashMap = new TreeMap();
        setLayout(new BorderLayout());
        add(getMainPanel(), BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        if (_mainPanel == null) {
            _mainPanel = new JPanel();
            _mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.X_AXIS));
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.add(getCommunicationsPanel());
            rightPanel.add(getToolsPanel());
            rightPanel.add(getHeavyEquipment());
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(getEmergencyPanel());
            leftPanel.add(getTransportationPanel());
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.add(getFoodCookingPanel());
            centerPanel.add(getSpecializedEquipmentPanel());
            centerPanel.add(getMiscellaneousEquipmentPanel());
            _mainPanel.add(leftPanel);
            _mainPanel.add(centerPanel);
            _mainPanel.add(rightPanel);
        }
        return _mainPanel;
    }

    public JPanel getEmergencyPanel() {
        if (_emergencyPanel == null) {
            _emergencyPanel = new JPanel();
            _emergencyPanel.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _emergencyPanel.add(scrollPane, BorderLayout.CENTER);
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.emergency"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List skillKeys = getKeys("resources.emergency.");
            for (int i = 0; i < skillKeys.size(); i++) {
                String key = (String) skillKeys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _emergencyPanel;
    }

    public JPanel getFoodCookingPanel() {
        if (_foodCookingPanel == null) {
            _foodCookingPanel = new JPanel();
            _foodCookingPanel.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _foodCookingPanel.add(scrollPane, BorderLayout.CENTER);
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.food.cooking"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List skillKeys = getKeys("resources.food.cooking.");
            JPanel foodStoragePanel = new JPanel();
            String foodStorageKey = "resources.food.cooking.food";
            JTextField foodField = new JTextField();
            foodField.setColumns(3);
            foodField.setName(foodStorageKey);
            _textFieldHashMap.put(foodField.getName(), foodField);
            JLabel foodLabel = new JLabel(_resourceBundle.getString(foodStorageKey));
            foodStoragePanel.add(foodLabel, new FormConstraints(FormConstraints.LABEL_LEFT));
            foodStoragePanel.add(foodField, new FormConstraints(FormConstraints.TEXT_FIELD));
            foodField.getDocument().addDocumentListener(new UpdateTextFieldListener(foodField));
            panel.add(foodStoragePanel, new FormConstraints(FormConstraints.LABEL_RIGHT, 1));
            skillKeys.remove(foodStorageKey);
            for (int i = 0; i < skillKeys.size(); i++) {
                String key = (String) skillKeys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _foodCookingPanel;
    }

    public JPanel getCommunicationsPanel() {
        if (_communicationsPanel == null) {
            _communicationsPanel = new JPanel();
            _communicationsPanel.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _communicationsPanel.add(scrollPane, BorderLayout.CENTER);
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.communication"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List keys = getKeys("resources.communication.");
            for (int i = 0; i < keys.size(); i++) {
                String key = (String) keys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _communicationsPanel;
    }

    public JPanel getTransportationPanel() {
        if (_transportationPanel == null) {
            _transportationPanel = new JPanel();
            _transportationPanel.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _transportationPanel.add(scrollPane, BorderLayout.CENTER);
            panel.setName("Skills Panel");
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.transportation"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List skillKeys = getKeys("resources.transportation.");
            for (int i = 0; i < skillKeys.size(); i++) {
                String key = (String) skillKeys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _transportationPanel;
    }

    public JPanel getHeavyEquipment() {
        if (_heavyEquipmentPanel == null) {
            _heavyEquipmentPanel = new JPanel();
            _heavyEquipmentPanel.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _heavyEquipmentPanel.add(scrollPane, BorderLayout.CENTER);
            panel.setName("Misecellaneous Panel");
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.heavy.equipment"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List skillKeys = getKeys("resources.heavy.equipment.");
            for (int i = 0; i < skillKeys.size(); i++) {
                String key = (String) skillKeys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _heavyEquipmentPanel;
    }

    public JPanel getToolsPanel() {
        if (_toolsPanel == null) {
            _toolsPanel = new JPanel();
            _toolsPanel.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _toolsPanel.add(scrollPane, BorderLayout.CENTER);
            panel.setName("Skills Panel");
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.basic.tools"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List keys = getKeys("resources.basic.tools.");
            for (int i = 0; i < keys.size(); i++) {
                String key = (String) keys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _toolsPanel;
    }

    public JPanel getSpecializedEquipmentPanel() {
        if (_specializedEquipment == null) {
            _specializedEquipment = new JPanel();
            _specializedEquipment.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _specializedEquipment.add(scrollPane, BorderLayout.CENTER);
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.specialized.tools"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List skillKeys = getKeys("resources.specialized.tools.");
            for (int i = 0; i < skillKeys.size(); i++) {
                String key = (String) skillKeys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _specializedEquipment;
    }

    public JPanel getMiscellaneousEquipmentPanel() {
        if (_miscellaneousEquipment == null) {
            _miscellaneousEquipment = new JPanel();
            _miscellaneousEquipment.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(panel);
            _miscellaneousEquipment.add(scrollPane, BorderLayout.CENTER);
            panel.setLayout(new FormLayout(1, 0, 0));
            Font font = new Font("Times Roman", Font.BOLD, _headingSize);
            panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), _resourceBundle.getString("title.resources.miscellaneous.equipment"), TitledBorder.CENTER, TitledBorder.BELOW_TOP, font));
            panel.setVisible(true);
            List skillKeys = getKeys("resources.miscellaneous.equipment.");
            for (int i = 0; i < skillKeys.size(); i++) {
                String key = (String) skillKeys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key));
                _checkboxHashMap.put(key, checkBox);
                checkBox.setName(key);
                checkBox.addActionListener(new UpdateBooleanFieldActionListener());
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _miscellaneousEquipment;
    }

    public void setEditable(boolean editable) {
        JPanel panel = getMainPanel();
        Iterator iter = getBooleanFields().iterator();
        while (iter.hasNext()) {
            Object temp = iter.next();
            try {
                JComponent field = (JComponent) temp;
                field.setEnabled(editable);
            } catch (Exception e) {
                System.out.println(temp.getClass().getName());
                e.printStackTrace();
            }
        }
        iter = getTextFields().iterator();
        while (iter.hasNext()) {
            JTextComponent field = (JTextComponent) iter.next();
            field.setEditable(editable);
        }
    }

    public Family getCurrentFamily() {
        return _curentFamily;
    }

    public List getKeys(String str) {
        List keys = new ArrayList();
        Enumeration enumertion = _resourceBundle.getKeys();
        while (enumertion.hasMoreElements()) {
            String key = (String) enumertion.nextElement();
            if (key.startsWith(str)) {
                keys.add(key);
            }
        }
        Collections.sort(keys);
        return keys;
    }

    public void setCurrentFamily(Family family) {
        _curentFamily = family;
        Set booleanFieldsToUpdate = getBooleanFields();
        Iterator iter = booleanFieldsToUpdate.iterator();
        while (iter.hasNext()) {
            setSelected((JCheckBox) iter.next(), family);
        }
        Set textFieldsToUpdate = getTextFields();
        iter = textFieldsToUpdate.iterator();
        while (iter.hasNext()) {
            setSelected((JTextField) iter.next(), family);
        }
    }

    /***/
    private Set getBooleanFields() {
        Set checkBoxes = new HashSet();
        Set keys = _checkboxHashMap.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            checkBoxes.add(_checkboxHashMap.get(key));
        }
        return checkBoxes;
    }

    /***/
    private Set getTextFields() {
        Set textFields = new HashSet();
        Set keys = _textFieldHashMap.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            textFields.add(_textFieldHashMap.get(key));
        }
        return textFields;
    }

    private void setSelected(JCheckBox checkBox, Family family) {
        try {
            checkBox.setSelected(Boolean.valueOf(family.getSkillOrResource(checkBox.getName())).booleanValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelected(JTextField field, Family family) {
        try {
            field.setText(family.getSkillOrResource(field.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UpdateBooleanFieldActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            if (getCurrentFamily() != null) {
                getCurrentFamily().setSkillOrResource(checkBox.getName(), Boolean.toString(checkBox.isSelected()));
                getCurrentFamily().changeDateAltered();
            }
        }
    }

    class UpdateTextFieldListener implements DocumentListener {

        JTextComponent _component = null;

        public UpdateTextFieldListener(JTextComponent comp) {
            _component = comp;
        }

        public void changedUpdate(DocumentEvent e) {
        }

        public void insertUpdate(DocumentEvent e) {
            setComponent();
        }

        public void removeUpdate(DocumentEvent e) {
            setComponent();
        }

        public void setComponent() {
            if (getCurrentFamily() != null) {
                getCurrentFamily().setSkillOrResource(_component.getName(), _component.getText());
                getCurrentFamily().changeDateAltered();
            }
        }
    }
}
