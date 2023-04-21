package org.mars_sim.msp.ui.standard;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/** The SearchWindow is a tool window that allows the user to search
 *  for individual units by name and category.
 */
public class SearchWindow extends ToolWindow implements ActionListener, ItemListener, MouseListener, DocumentListener {

    private MainDesktopPane desktop;

    private UIProxyManager proxyManager;

    private JComboBox searchForSelect;

    private JList unitList;

    private DefaultListModel unitListModel;

    private JTextField selectTextField;

    private JLabel statusLabel;

    private JCheckBox openWindowCheck;

    private JCheckBox centerMapCheck;

    private boolean lockUnitList;

    private boolean lockSearchText;

    private String[] unitCategoryNames;

    /** Constructs a SearchWindow object 
     *  @param desktop the desktop pane
     */
    public SearchWindow(MainDesktopPane desktop) {
        super("Search Tool");
        lockUnitList = false;
        lockSearchText = false;
        unitCategoryNames = new String[3];
        unitCategoryNames[0] = "Person";
        unitCategoryNames[1] = "Settlement";
        unitCategoryNames[2] = "Vehicle";
        addInternalFrameListener(new ViewFrameListener());
        this.desktop = desktop;
        proxyManager = desktop.getProxyManager();
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPane);
        JPanel searchForPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPane.add(searchForPane, "North");
        JLabel searchForLabel = new JLabel("Search for: ");
        searchForPane.add(searchForLabel);
        String[] categoryStrings = { "People", "Settlements", "Vehicles" };
        searchForSelect = new JComboBox(categoryStrings);
        searchForSelect.setSelectedIndex(0);
        searchForSelect.addItemListener(this);
        searchForPane.add(searchForSelect);
        JPanel selectUnitPane = new JPanel(new BorderLayout());
        mainPane.add(selectUnitPane, "Center");
        selectTextField = new JTextField();
        selectTextField.getDocument().addDocumentListener(this);
        selectUnitPane.add(selectTextField, "North");
        unitListModel = new DefaultListModel();
        Iterator peopleProxies = proxyManager.getOrderedPersonProxies();
        while (peopleProxies.hasNext()) {
            UnitUIProxy proxy = (UnitUIProxy) peopleProxies.next();
            unitListModel.addElement(proxy.getUnit().getName());
        }
        unitList = new JList(unitListModel);
        unitList.setSelectedIndex(0);
        unitList.addMouseListener(this);
        selectUnitPane.add(new JScrollPane(unitList), "Center");
        JPanel bottomPane = new JPanel(new BorderLayout());
        mainPane.add(bottomPane, "South");
        JPanel selectOptionsPane = new JPanel(new GridLayout(2, 1));
        bottomPane.add(selectOptionsPane, "North");
        openWindowCheck = new JCheckBox("Open Detail Window");
        openWindowCheck.setSelected(true);
        selectOptionsPane.add(openWindowCheck);
        centerMapCheck = new JCheckBox("Recenter Mars Navigator");
        selectOptionsPane.add(centerMapCheck);
        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setBorder(new EtchedBorder());
        bottomPane.add(statusLabel, "Center");
        JPanel searchButtonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPane.add(searchButtonPane, "South");
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchButtonPane.add(searchButton);
        pack();
    }

    /**
     * Search for named unit when button is pushed.
     * Retrieve info on all units of selected category.
     */
    private void search() {
        Iterator unitProxies = null;
        String category = (String) searchForSelect.getSelectedItem();
        if (category.equals("People")) unitProxies = proxyManager.getOrderedPersonProxies();
        if (category.equals("Settlements")) unitProxies = proxyManager.getOrderedSettlementProxies();
        if (category.equals("Vehicles")) unitProxies = proxyManager.getOrderedVehicleProxies();
        boolean foundUnit = false;
        while (unitProxies.hasNext()) {
            UnitUIProxy proxy = (UnitUIProxy) unitProxies.next();
            if (selectTextField.getText().equalsIgnoreCase(proxy.getUnit().getName())) {
                foundUnit = true;
                if (openWindowCheck.isSelected()) desktop.openUnitWindow(proxy);
                if (centerMapCheck.isSelected()) desktop.centerMapGlobe(proxy.getUnit().getCoordinates());
            }
        }
        String tempName = unitCategoryNames[searchForSelect.getSelectedIndex()];
        if (!foundUnit) statusLabel.setText(tempName + " Not Found");
        if (selectTextField.getText().equals("")) statusLabel.setText("Enter The Name of a " + tempName);
    }

    /** ItemListener method overridden */
    public void itemStateChanged(ItemEvent event) {
        unitListModel.clear();
        Iterator unitProxies = null;
        String category = (String) searchForSelect.getSelectedItem();
        if (category.equals("People")) unitProxies = proxyManager.getOrderedPersonProxies();
        if (category.equals("Settlements")) unitProxies = proxyManager.getOrderedSettlementProxies();
        if (category.equals("Vehicles")) unitProxies = proxyManager.getOrderedVehicleProxies();
        lockUnitList = true;
        while (unitProxies.hasNext()) {
            UnitUIProxy proxy = (UnitUIProxy) unitProxies.next();
            unitListModel.addElement(proxy.getUnit().getName());
        }
        unitList.setSelectedIndex(0);
        unitList.ensureIndexIsVisible(0);
        lockUnitList = false;
        statusLabel.setText(" ");
    }

    /** ActionListener method overridden */
    public void actionPerformed(ActionEvent event) {
        search();
    }

    public void mouseClicked(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
        if (event.getClickCount() == 2) {
            search();
            return;
        }
        if (!lockUnitList) {
            String selectedUnitName = (String) unitList.getSelectedValue();
            lockSearchText = true;
            if (!selectTextField.getText().equals(selectedUnitName)) {
                selectTextField.setText(selectedUnitName);
            }
            lockSearchText = false;
        }
    }

    public void changedUpdate(DocumentEvent event) {
    }

    public void insertUpdate(DocumentEvent event) {
        searchTextChange();
    }

    public void removeUpdate(DocumentEvent event) {
        searchTextChange();
    }

    /** Make selection in list depending on what unit names begin with the changed text. */
    private void searchTextChange() {
        if (!lockSearchText) {
            String searchText = selectTextField.getText().toLowerCase();
            int fitIndex = 0;
            boolean goodFit = false;
            for (int x = unitListModel.size() - 1; x > -1; x--) {
                String unitString = ((String) unitListModel.elementAt(x)).toLowerCase();
                if (unitString.startsWith(searchText)) {
                    fitIndex = x;
                    goodFit = true;
                }
            }
            if (goodFit) {
                lockUnitList = true;
                unitList.setSelectedIndex(fitIndex);
                unitList.ensureIndexIsVisible(fitIndex);
                lockUnitList = false;
            }
        }
        statusLabel.setText(" ");
    }
}
