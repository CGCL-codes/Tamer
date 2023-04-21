package org.lds.wilmington.christiana.preparedness.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.lds.wilmington.christiana.preparedness.data.Families;
import org.lds.wilmington.christiana.preparedness.data.Family;
import org.lds.wilmington.christiana.preparedness.data.Summary;
import org.lds.wilmington.christiana.preparedness.data.Zone;
import org.lds.wilmington.christiana.preparedness.gui.layout.FormConstraints;
import org.lds.wilmington.christiana.preparedness.gui.layout.FormLayout;

/**
 * @author Jay Askren
 */
public class QueryPanel extends JPanel {

    private static final Pattern _gridPattern = Pattern.compile("([a-zA-Z]*)([0-9]*)");

    private ResourceBundle _resourceBundle = null;

    private JScrollPane _resourcePanel = null;

    private Map _countLabel = null;

    private Families _families = null;

    private JList _familyList = null;

    private JTabbedPane _tabbedPane = null;

    private JPanel _gridPanel = null;

    private Map _gridMap = new HashMap();

    private int _numLetters = Summary.GRID_LETTERS.length;

    private int _numNumbers = Summary.GRID_NUMBERS.length;

    private InputPanel _parentPanel = null;

    private JLabel _numFamiliesLabel = new JLabel();

    private Color _highlightColor = Color.white;

    private Color _defaultColor = _numFamiliesLabel.getBackground();

    private Integer _zero = new Integer(0);

    private Vector _zoneVector = null;

    private JComboBox _zoneComboBox = null;

    public QueryPanel(JList familyList, InputPanel parentPanel) {
        _parentPanel = parentPanel;
        _resourceBundle = ResourceBundle.getBundle("org.lds.wilmington.christiana." + "preparedness.gui.GuiResourceBundle");
        _familyList = familyList;
        initializeGridValues();
        initialize();
    }

    public void initializeGridValues() {
    }

    public void initialize() {
        _tabbedPane = new JTabbedPane();
        setLayout(new BorderLayout());
        Font font = new Font("Times Roman", Font.BOLD, 14);
        JPanel chooseZonePanel = new JPanel();
        chooseZonePanel.setLayout(new FormLayout(5));
        _zoneVector = new Vector();
        _zoneVector.add(Zone.ALL_ZONES_NAME);
        _zoneComboBox = new JComboBox(_zoneVector);
        _zoneComboBox.setEditable(false);
        _zoneComboBox.setEnabled(true);
        _zoneComboBox.addItemListener(new ZoneComboBoxListener());
        _numFamiliesLabel = new JLabel("0");
        chooseZonePanel.add(new JLabel("Number of Families:  "), new FormConstraints(FormConstraints.LABEL_LEFT));
        chooseZonePanel.add(_numFamiliesLabel, new FormConstraints(FormConstraints.TEXT_FIELD));
        chooseZonePanel.add(new JLabel("Summary for: "), new FormConstraints(FormConstraints.LABEL_LEFT));
        chooseZonePanel.add(_zoneComboBox, new FormConstraints(FormConstraints.TEXT_FIELD));
        add(chooseZonePanel, BorderLayout.NORTH);
        add(_tabbedPane, BorderLayout.CENTER);
        _tabbedPane.add(getResourcePanel(), "Skills/Resources");
        _tabbedPane.add(getGridPanel(), "Map Grid");
    }

    public JScrollPane getResourcePanel() {
        if (_resourcePanel == null) {
            _countLabel = new HashMap();
            JPanel mainPanel = new JPanel();
            _resourcePanel = new JScrollPane(mainPanel);
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            JPanel panel = null;
            List resourceKeys = Summary.getResourceKeys();
            int numPerColumn = resourceKeys.size() / 3 + 1;
            if (resourceKeys.size() % 3 == 0) {
                numPerColumn = resourceKeys.size() / 3;
            }
            for (int i = 0; i < resourceKeys.size(); i++) {
                if (i % numPerColumn == 0) {
                    panel = new JPanel();
                    mainPanel.add(panel);
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    panel.setVisible(true);
                } else {
                }
                String key = (String) resourceKeys.get(i);
                JCheckBox checkBox = new JCheckBox(_resourceBundle.getString(key) + " (0)");
                _countLabel.put(key, checkBox);
                checkBox.addActionListener(new CheckBoxActionListener());
                checkBox.setName(key);
                panel.add(checkBox, new FormConstraints(FormConstraints.CHECK_BOX));
            }
        }
        return _resourcePanel;
    }

    /**Get the panel that shows the map grid and allows the user to query for 
	 * families living in the specified grid..*/
    public JPanel getGridPanel() {
        if (_gridPanel == null) {
            _gridPanel = new JPanel();
            JPanel actualGridPanel = new JPanel();
            JPanel unassignedPanel = new JPanel();
            JCheckBox unassignedBox = new JCheckBox("Grid Not Assigned");
            unassignedPanel.add(unassignedBox);
            unassignedBox.setName(Summary.UNKNOWN_MAP_GRID);
            unassignedBox.addActionListener(new CheckBoxActionListener());
            _gridMap.put(unassignedBox.getName(), unassignedBox);
            JScrollPane scrollPanel = new JScrollPane(actualGridPanel);
            _gridPanel.setLayout(new BorderLayout());
            _gridPanel.add(scrollPanel, BorderLayout.CENTER);
            _gridPanel.add(unassignedPanel, BorderLayout.NORTH);
            actualGridPanel.setLayout(new GridLayout(_numNumbers + 1, Summary.GRID_LETTERS.length + 1));
            for (int numPos = 0; numPos <= _numNumbers; numPos++) {
                for (int letPos = 0; letPos <= _numLetters; letPos++) {
                    JPanel individualCountPanel = new JPanel();
                    individualCountPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                    individualCountPanel.setLayout(new BorderLayout());
                    actualGridPanel.add(individualCountPanel);
                    if (numPos == 0 && letPos == 0) {
                        JLabel label = new JLabel();
                        individualCountPanel.add(label, BorderLayout.CENTER);
                    } else if (letPos == 0) {
                        JLabel label = new JLabel();
                        label.setText(Summary.GRID_NUMBERS[numPos - 1]);
                        label.setHorizontalAlignment(JLabel.CENTER);
                        individualCountPanel.add(label, BorderLayout.CENTER);
                    } else if (numPos == 0) {
                        JLabel label = new JLabel();
                        label.setText(Summary.GRID_LETTERS[letPos - 1]);
                        label.setHorizontalAlignment(JLabel.CENTER);
                        individualCountPanel.add(label, BorderLayout.CENTER);
                    } else {
                        JCheckBox checkBox = new JCheckBox();
                        checkBox.addActionListener(new CheckBoxActionListener());
                        _gridMap.put(Summary.GRID_LETTERS[letPos - 1] + Summary.GRID_NUMBERS[numPos - 1], checkBox);
                        checkBox.setText("");
                        checkBox.setName(Summary.GRID_LETTERS[letPos - 1] + Summary.GRID_NUMBERS[numPos - 1]);
                        checkBox.setVisible(false);
                        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
                        individualCountPanel.add(checkBox, BorderLayout.CENTER);
                    }
                }
            }
        }
        return _gridPanel;
    }

    /**This is used for highlighting the currentFamily's resources and place on the map.*/
    public void setCurrentFamily(Object[] families) {
        if (true) {
            return;
        }
        Set gridNumbers = new HashSet();
        for (int i = 0; i < families.length; i++) {
            String gridNumber = ((Family) families[i]).getGridNumber();
            if (!Summary.hasGridKey(gridNumber)) {
                gridNumber = Summary.UNKNOWN_MAP_GRID;
            }
            gridNumbers.add(gridNumber);
        }
        Iterator iter = _gridMap.values().iterator();
        while (iter.hasNext()) {
            JCheckBox tempLabel = (JCheckBox) iter.next();
            (tempLabel).setBackground(_defaultColor);
            tempLabel.setOpaque(false);
        }
        iter = gridNumbers.iterator();
        while (iter.hasNext()) {
            JCheckBox gridLabel = (JCheckBox) _gridMap.get(iter.next());
            if (gridLabel != null) {
                gridLabel.setBackground(_highlightColor);
                gridLabel.setOpaque(true);
            }
        }
        Set resources = new HashSet();
        for (int i = 0; i < families.length; i++) {
            Family family = (Family) families[i];
            iter = family.getSkillsResources().keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Object resource = family.getSkillOrResource(key);
                if (resource != null && !resource.equals(_zero)) {
                    String resourceString = resource.toString();
                    if (!resourceString.equals("") && !resourceString.equals("false")) {
                        resources.add(key);
                    }
                }
            }
        }
        iter = _countLabel.values().iterator();
        while (iter.hasNext()) {
            JCheckBox tempLabel = (JCheckBox) iter.next();
            tempLabel.setBackground(_defaultColor);
            tempLabel.setOpaque(false);
        }
        iter = resources.iterator();
        while (iter.hasNext()) {
            Object resourceKey = iter.next();
            JCheckBox tempLabel = (JCheckBox) _countLabel.get(resourceKey);
            if (tempLabel != null) {
                tempLabel.setOpaque(true);
                tempLabel.setBackground(_highlightColor);
            }
        }
    }

    /**A file of families has just been loaded.  Display the summary for that family.*/
    public void setFamilies(Families fam) {
        _families = fam;
        displaySummary();
    }

    public void displaySummary() {
        if (_families != null) {
            Summary familySummary = _families.getSummary();
            _zoneVector.clear();
            _zoneVector.add(Zone.ALL_ZONES_NAME);
            for (int i = 0; i < _families.getZones().size(); i++) {
                _zoneVector.add(_families.getZones().get(i));
            }
            _zoneComboBox.revalidate();
            _zoneComboBox.repaint();
            _zoneComboBox.setSelectedIndex(0);
            Iterator iter = _countLabel.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                JCheckBox checkBox = (JCheckBox) _countLabel.get(key);
                Object objectValue = familySummary.getResources().get(key);
                if (objectValue != null) {
                    setTextField(key, objectValue, checkBox);
                    continue;
                }
                objectValue = familySummary.getSkills().get(key);
                if (objectValue != null) {
                    setTextField(key, objectValue, checkBox);
                    continue;
                }
                objectValue = familySummary.getNeeds().get(key);
                if (objectValue != null) {
                    setTextField(key, objectValue, checkBox);
                }
            }
            Map gridMap = familySummary.getGrid();
            Iterator keyIter = gridMap.keySet().iterator();
            while (keyIter.hasNext()) {
                Object key = keyIter.next();
                Integer numFamiliesInGrid = (Integer) gridMap.get(key);
                JCheckBox gridCheckBox = (JCheckBox) _gridMap.get(key);
                if (gridCheckBox != null) {
                    if (key == null || key.equals("")) {
                        System.out.println("Query Panel has blank or null key");
                    } else if (key.equals(Summary.UNKNOWN_MAP_GRID)) {
                        String unassignedText = "Grid Not Assigned (" + numFamiliesInGrid.toString() + ")";
                        gridCheckBox.setText(unassignedText);
                    } else {
                        gridCheckBox.setText(numFamiliesInGrid.toString());
                        if (numFamiliesInGrid == null || numFamiliesInGrid.equals(new Integer(0))) {
                            gridCheckBox.setVisible(false);
                        } else {
                            gridCheckBox.setVisible(true);
                        }
                    }
                }
            }
            displaySummaryImpl();
        }
    }

    public void setHighlightColor(Color color) {
        _highlightColor = color;
    }

    private void displaySummaryImpl() {
        if (_families != null) {
            Summary summary = _families.getSummary(_zoneComboBox.getSelectedItem().toString());
            _numFamiliesLabel.setText(Integer.toString(summary.getNumFamilies()));
            Iterator iter = _countLabel.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                JCheckBox label = (JCheckBox) _countLabel.get(key);
                Object objectValue = summary.getResources().get(key);
                if (objectValue != null) {
                    setTextField(key, objectValue, label);
                    continue;
                }
                objectValue = summary.getSkills().get(key);
                if (objectValue != null) {
                    setTextField(key, objectValue, label);
                    continue;
                }
                objectValue = summary.getNeeds().get(key);
                if (objectValue != null) {
                    setTextField(key, objectValue, label);
                }
            }
            Map gridMap = summary.getGrid();
            Iterator keyIter = gridMap.keySet().iterator();
            while (keyIter.hasNext()) {
                Object key = keyIter.next();
                Integer numFamiliesInGrid = (Integer) gridMap.get(key);
                JCheckBox gridLabel = (JCheckBox) _gridMap.get(key);
                if (gridLabel != null) {
                    if (key == null || key.equals("")) {
                        System.out.println("Summary has blank or null key");
                    } else if (key.equals(Summary.UNKNOWN_MAP_GRID)) {
                        String unassignedText = "Grid Not Assigned (" + numFamiliesInGrid.toString() + ")";
                        gridLabel.setText(unassignedText);
                    } else {
                        if (numFamiliesInGrid == null || numFamiliesInGrid.equals(new Integer(0))) {
                            gridLabel.setText("");
                        } else {
                            gridLabel.setText(numFamiliesInGrid.toString());
                            gridLabel.setVisible(true);
                        }
                    }
                }
            }
        }
    }

    private void setTextField(String key, Object objectValue, JCheckBox label) {
        if (objectValue instanceof Integer) {
            Integer value = (Integer) objectValue;
            if (value == null || "".equals(value)) {
                label.setText(_resourceBundle.getString(label.getName()) + " (0)");
            } else {
                label.setText(_resourceBundle.getString(label.getName()) + " (" + value.toString() + ")");
            }
        } else if (objectValue == null) {
            label.setText(_resourceBundle.getString(label.getName()) + " (0)");
        }
    }

    class CheckBoxActionListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            JCheckBox comp = (JCheckBox) event.getSource();
            try {
                FamilyListCellRenderer renderer = (FamilyListCellRenderer) _familyList.getCellRenderer();
                if (comp.isSelected()) {
                    renderer.addField(comp.getName());
                    comp.setBackground(renderer.getColor(comp.getName()));
                } else {
                    renderer.removeField(comp.getName());
                    comp.setBackground(comp.getParent().getBackground());
                }
                _parentPanel.resortFamilies();
                _familyList.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
            comp.getParent().repaint();
        }
    }

    class ZoneComboBoxListener implements ItemListener {

        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                displaySummaryImpl();
            }
        }
    }
}
