package scaffoldhunter.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import scaffoldhunter.db.DBController;
import scaffoldhunter.db.DBException;
import scaffoldhunter.db.DBTree;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * This class constructs a dialog that guides the user through a process in which he can
 * define a filter to minimze the number of visible scaffolds.
 * @author Anke Arndt, Arbia Ben Ahmed, Michael Rex, Vanessa Bembenek
 */
public class FilterDialog extends BaseDialog {

    /** Constant to show that the overview panel is visible. */
    private static final int OVERVIEW_PANEL = 0;

    /** Constant to show that the filter panel is visible. */
    private static final int FILTER_PANEL = 1;

    /** Constant to show that the filter panel is visible. */
    private static final int SUBSEARCH_PANEL = 2;

    /** Stores one of the constants <code>OVERVIEW_PANEL</code>, <code>FILTER_PANEL</code>. */
    private int panelCount;

    /** Stores the type of filtering that should be executed. */
    private FilterType filterType;

    /** <code>true</code> if <code>filterType</code> equals <code>FilterType.iterative_filtering</code>
	 * and the filter rules that have been added must be adjusted, <code>false</code> otherwise */
    private boolean adjustmentMessage;

    /** Stores the parent of this dialog. */
    private MainFrame parent;

    /** Stores the miliseconds that should be waited until the number of visible scaffolds
	 * will be updated. */
    private int spinnerDelay;

    /** The Timer that updates the number of visible scaffolds. */
    private Timer spinnerTimer;

    /** This list contains instances of the classes <code>Detail</code> and <code>FilterRule</code>. 
	 * It stores all settings the user sets in the process of defining a filter. */
    private ArrayList<Detail> definedRules;

    /** This list stores all filter rules the user defined. It will be passed to the 
	 * <code>DBController</code> of - if <code>filterType</code> equals 
	 * <code>FilterType.iterative_filtering</code> - to the current <code>VCanvas</code>. */
    private ArrayList<FilterRule> newFilter;

    /** This list stores all filter rules that are active on the current <code>VCanvas</code>. */
    private ArrayList<FilterRule> activeFilter;

    /** This list stores all available scaffold properties including its data. */
    private ArrayList<ScaffoldProperty> properties;

    /** This list stores the checkbox for every scaffold property. */
    private ArrayList<JCheckBox> checkBoxes;

    /** Stores the initial dimension of the panel to assure a constant size of the dialog. */
    private Dimension initialDimension;

    /** The <code>GridBagConstraints</code> is needed for a consistent appearance of the 
	 * <code>pureFilterPanel</code>. */
    private GridBagConstraints c;

    /** Stores the layout constaints needed for a consistens appearance of every filter rule. */
    private FormLayout ruleLayout;

    /** Popupmenu for filter management. */
    private JPopupMenu popupmenu;

    /** This panel holds the checkboxes. */
    private JPanel overviewPanel;

    /** This panel holds all components that are needed to define a filter rule. */
    private JPanel pureFilterPanel;

    /** This panel holds a <code>JScrollPane</code> in which the <code>pureFilterPanel</code> is
	 * embedded. It is needed to assure that the dialog is resizable. */
    private JPanel scrollFilterPanel;

    /** Holds the panel for defining substructures */
    private SubstructureSearchPanel subsearchPanel;

    /** This label holds the number of visible scaffolds depending on the defined filter rules. */
    private JLabel numberVisibleScaffoldsLabel;

    /** This label holds the number of all scaffolds for which the chosen properties are defined
	 * or - if <code>filterType</code> equals <code>FilterType.iterative_filtering</code> - the
	 * number of all scaffolds that meet the active filter. */
    private JLabel numberAvailableScaffoldsLabel;

    /** Stores the number of visible scaffolds depending on the defined filter rules. */
    private int numberVisibleScaffolds;

    /** Stores the number of all scaffolds for which the chosen properties are defined or - 
	 * if <code>filterType</code> equals <code>FilterType.iterative_filtering</code> - the
	 * number of all scaffolds that meet the active filter. */
    private int numberAvailableScaffolds;

    /** Stores the default button of this dialog. */
    private JButton filterForward;

    /** Shows the manual. */
    private Action helpAction;

    /** Disposes this dialog and returns to the <code>MainFrame</code> or - if <code>filterType</code> 
	 * equals <code>FilterType.standard_filtering</code> - quits the application. */
    private Action cancelAction;

    /** Shows a contextmenu to call the <code>FilterManagerDialog</code> or to store the defined 
	 * filter. */
    private Action manageAction;

    /** Opens the <code>FilterManagerDialog</code>. */
    private Action manageDialogAction;

    /** Stores the defined filter. */
    private Action storeFilterAction;

    /** Takes a step backward. */
    private Action backwardAction;

    /** Takes a step forward. */
    private Action forwardAction;

    /**
	 * Constructs the dialog for initial filtering.
	 * 
	 * @param filterType The type of filtering that should be executed. In this case either
	 * <code>FilterType.standard_filtering</code> or <code>FilterType.restart_filtering</code>.
	 */
    FilterDialog(FilterType filterType) {
        super();
        this.filterType = filterType;
        spinnerDelay = 500;
        spinnerTimer = new Timer(spinnerDelay, new SpinnerAction());
        spinnerTimer.setRepeats(false);
        initI18N();
        initActions();
        initGUI();
    }

    /**
	 * Constructs the dialog for iterative filtering.
	 * 
	 * @param parent The <code>MainFrame</code> from which the dialog is displayed.
	 */
    FilterDialog(MainFrame parent) {
        super(parent, true);
        this.parent = parent;
        filterType = FilterType.iterative_filtering;
        spinnerDelay = 500;
        spinnerTimer = new Timer(spinnerDelay, new SpinnerAction());
        spinnerTimer.setRepeats(false);
        initI18N();
        initActions();
        initGUI();
    }

    /**
	 * Initializes the actions of this dialog.
	 */
    private void initActions() {
        helpAction = new HelpAction(this);
        cancelAction = new CancelAction(this);
        manageAction = new ManageAction();
        manageDialogAction = new ManageDialogAction(this);
        storeFilterAction = new StoreFilterAction(this);
        backwardAction = new BackwardAction(this);
        forwardAction = new ForwardAction(this);
        storeFilterAction.setEnabled(false);
        backwardAction.setEnabled(false);
    }

    /**
	 * Initializes the GUI of this dialog.
	 */
    private void initGUI() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                cancelAction.actionPerformed(null);
            }
        });
        setTitle(i18n("Filter.Title"));
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        subsearchPanel = new SubstructureSearchPanel(this, false);
        subsearchPanel.setSize(getWidth(), getHeight());
        definedRules = new ArrayList<Detail>();
        newFilter = new ArrayList<FilterRule>();
        if (filterType.equals(FilterType.iterative_filtering)) {
            Filter filter = GUIController.getInstance().getCurrentVCanvas().getFilter();
            ArrayList<FilterRule> canvasFilter = filter.getFilterRules();
            activeFilter = new ArrayList<FilterRule>();
            for (int i = 0; i < canvasFilter.size(); i++) {
                FilterRule filterRule = new FilterRule(canvasFilter.get(i));
                boolean add = true;
                for (int j = 0; j < activeFilter.size(); j++) {
                    FilterRule definedRule = activeFilter.get(j);
                    if (!areRulesEffective(filterRule, definedRule)) {
                        add = false;
                    }
                }
                if (add) {
                    activeFilter.add(filterRule);
                    addFilterRule(filterRule);
                }
            }
            subsearchPanel.setFilter(filter);
        } else if (filterType.equals(FilterType.standard_filtering) || filterType.equals(FilterType.restart_filtering)) {
            Filter initialFilter = GUIController.getInstance().getProfile().getInitialFilter();
            ArrayList<FilterRule> filterRules = initialFilter.getFilterRules();
            for (int i = 0; i < filterRules.size(); i++) {
                FilterRule filterRule = new FilterRule(filterRules.get(i));
                addFilterRule(filterRule);
            }
            subsearchPanel.setFilter(initialFilter);
        }
        adjustmentMessage = false;
        popupmenu = new JPopupMenu();
        popupmenu.add(manageDialogAction);
        popupmenu.add(storeFilterAction);
        panelCount = OVERVIEW_PANEL;
        initialDimension = new Dimension(750, 400);
        ruleLayout = new FormLayout("left:pref, 10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, fill:pref, 10dlu, center:pref, 10dlu, center:pref", "");
        createOverviewPanel();
        fillOverviewPanel();
        numberVisibleScaffoldsLabel = new JLabel();
        numberAvailableScaffoldsLabel = new JLabel();
        getContentPane().add(overviewPanel, BorderLayout.CENTER);
        getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        getRootPane().setDefaultButton(filterForward);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
	 * Creates a panel that shows an overview over all scaffold properties and provides
	 * checkboxes to select them.
	 */
    private void createOverviewPanel() {
        properties = GUIController.getInstance().getPropertiesNum();
        FormLayout layout = new FormLayout("pref, 10dlu, pref", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        checkBoxes = new ArrayList<JCheckBox>();
        for (int i = 0; i < properties.size(); i++) {
            JCheckBox check = new JCheckBox(properties.get(i).getPropertyTitle());
            check.setToolTipText(properties.get(i).getPropertyTooltip());
            checkBoxes.add(check);
            builder.append(check);
        }
        int widthDialog = (int) initialDimension.getWidth() - 50;
        int widthPanel = (int) builder.getPanel().getPreferredSize().getWidth();
        int width = Math.max(widthDialog, widthPanel);
        DefaultFormBuilder mainBuilder = new DefaultFormBuilder(new FormLayout(width + "PX", ""));
        mainBuilder.setDefaultDialogBorder();
        mainBuilder.appendSeparator(i18n("Filter.Overview"));
        mainBuilder.append(builder.getPanel());
        JScrollPane scrollPane = new JScrollPane(mainBuilder.getPanel());
        overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.add(scrollPane, BorderLayout.CENTER);
        overviewPanel.setPreferredSize(initialDimension);
    }

    /**
	 * Selects the checkboxes accordingly to the filter rules defined by the user.
	 * If <code>filterType</code> equals <code>FilterType.iterative_filtering</code>
	 * the checkboxes for properties for which a rule within the list of active rules
	 * exits must be disabled.
	 */
    private void fillOverviewPanel() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setSelected(false);
            for (int j = 0; j < definedRules.size(); j++) {
                if (checkBoxes.get(i).getText().equals(definedRules.get(j).getPropertyTitle())) {
                    checkBoxes.get(i).setSelected(true);
                }
            }
            if (filterType.equals(FilterType.iterative_filtering)) {
                for (int j = 0; j < activeFilter.size(); j++) {
                    if (checkBoxes.get(i).getText().equals(activeFilter.get(j).getPropertyTitle())) {
                        checkBoxes.get(i).setEnabled(false);
                    }
                }
            }
        }
    }

    /**
	 * Reads out the status of every checkbox. If a former selected checkbox is not selected
	 * any more the according rules must be deleted. If a checkbox is selected and there does
	 * not exist an according rule the selected property must be saved as a detail.
	 */
    private void readoutOverviewPanel() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox check = checkBoxes.get(i);
            boolean addRule = true;
            ArrayList<Detail> toDelete = new ArrayList<Detail>();
            for (int j = 0; j < definedRules.size(); j++) {
                if (check.getText().equals(definedRules.get(j).getPropertyTitle())) {
                    addRule = false;
                    if (!check.isSelected()) {
                        toDelete.add(definedRules.get(j));
                    }
                }
            }
            for (int j = 0; j < toDelete.size(); j++) {
                definedRules.remove(toDelete.get(j));
            }
            if (addRule && check.isSelected()) {
                Detail detail = new Detail();
                detail.setPropertyID(properties.get(i).getPropertyID());
                detail.setPropertyTitle(check.getText());
                addFilterRule(detail);
            }
        }
    }

    /**
	 * Creates the panel that holds all defined filter rules. This method must be called whenever
	 * the panel that holds the filter rules should be shown.
	 */
    private void createFilterPanel() {
        pureFilterPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        fillFilterPanel();
        int widthDialog = (int) initialDimension.getWidth() - 50;
        int widthPanel = (int) pureFilterPanel.getPreferredSize().getWidth();
        int width = Math.max(widthDialog, widthPanel);
        DefaultFormBuilder mainBuilder = new DefaultFormBuilder(new FormLayout(width + "PX", ""));
        mainBuilder.setDefaultDialogBorder();
        mainBuilder.appendSeparator(i18n("Filter.Definition"));
        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel(i18n("Filter.Dialog") + ":"));
        infoPanel.add(numberVisibleScaffoldsLabel);
        infoPanel.add(new JLabel("/"));
        infoPanel.add(numberAvailableScaffoldsLabel);
        fillNumberScaffoldsLabels(true);
        mainBuilder.append(infoPanel);
        mainBuilder.append(pureFilterPanel);
        JScrollPane scrollPane = new JScrollPane(mainBuilder.getPanel());
        scrollFilterPanel = new JPanel(new BorderLayout());
        scrollFilterPanel.add(scrollPane, BorderLayout.CENTER);
        scrollFilterPanel.setPreferredSize(initialDimension);
    }

    /**
	 * Fills the panel with all defined filter rules.
	 */
    private void fillFilterPanel() {
        pureFilterPanel.removeAll();
        ArrayList<Detail> toDelete = new ArrayList<Detail>();
        for (int i = 0; i < definedRules.size(); i++) {
            Detail detail = definedRules.get(i);
            JPanel rulePanel;
            if (filterType.equals(FilterType.iterative_filtering)) {
                boolean activeRule = false;
                if (detail instanceof FilterRule) {
                    FilterRule filterRule = (FilterRule) detail;
                    for (int j = 0; j < activeFilter.size(); j++) {
                        if (filterRule == activeFilter.get(j)) {
                            activeRule = true;
                        }
                    }
                }
                if (activeRule) {
                    rulePanel = createActiveRulePanel((FilterRule) detail);
                } else {
                    rulePanel = createRulePanel(detail);
                }
            } else {
                rulePanel = createRulePanel(detail);
            }
            if (rulePanel.getComponentCount() != 0) {
                c.weighty = 1.0;
                pureFilterPanel.add(rulePanel, c);
            } else {
                toDelete.add(detail);
            }
        }
        for (int i = 0; i < toDelete.size(); i++) {
            Detail detail = toDelete.get(i);
            definedRules.remove(detail);
            activeFilter.remove(detail);
        }
        c.weighty = 20.0;
        pureFilterPanel.add(new JLabel(), c);
    }

    /**
	 * Creates the panel that holds all the information about an active rule.
	 * 
	 * @param filterRule The <code>FilterRule</code> object that encapsulates the property the 
	 * rule is defined for.
	 * @return The <code>JPanel</code> that holds all information about the active rule.
	 */
    private JPanel createActiveRulePanel(FilterRule filterRule) {
        DefaultFormBuilder builder = new DefaultFormBuilder(ruleLayout);
        JLabel titleLabel = new JLabel();
        JLabel definedLabel = new JLabel();
        JLabel typeLabel = new JLabel();
        JLabel comparisonLabel = new JLabel();
        JSpinner valueSpinner = new JSpinner();
        Float[] range = new Float[2];
        if (!filterRule.isDefined()) {
            range = getRangeForSpinner(filterRule);
            if (filterRule.getValue() < range[0] || filterRule.getValue() > range[1]) {
                return builder.getPanel();
            }
        }
        titleLabel.setText("<html><b>" + filterRule.getPropertyTitle() + "</b></html>");
        builder.append(titleLabel);
        if (filterRule.isDefined()) {
            definedLabel.setText(i18n("Filter.Color.Defined"));
            builder.append(definedLabel);
            builder.append(typeLabel);
            builder.append(comparisonLabel);
            builder.append(new JLabel());
        } else {
            builder.append(definedLabel);
            if (filterRule.getType() != null) {
                typeLabel.setText(filterRule.getType());
            }
            builder.append(typeLabel);
            comparisonLabel.setText(filterRule.getComparisonOperator().toString());
            builder.append(comparisonLabel);
            valueSpinner.setModel(getSpinnerModel(filterRule.getValue(), range));
            builder.append(valueSpinner);
            valueSpinner.addChangeListener(new SpinnerAction());
        }
        JButton add = new JButton(new AddAction(this, builder.getPanel()));
        enterPressesWhenFocused(add);
        builder.append(add);
        builder.append(new JLabel());
        return builder.getPanel();
    }

    /**
	 * Creates the panel that holds all components to specify the assigned rule.
	 * 
	 * @param detail The <code>Detail</code> object that encapsulates the property 
	 * the rule should be defined for.
	 * @return The <code>JPanel</code> that holds all components to specify the assigned rule.
	 */
    private JPanel createRulePanel(Detail detail) {
        DefaultFormBuilder builder = new DefaultFormBuilder(ruleLayout);
        String[] valueTypes = GUIController.getInstance().getPropertyValues(detail.getPropertyID());
        if (!(detail instanceof FilterRule)) {
            if (valueTypes == null) {
                detail.setType(null);
            } else {
                detail.setType(valueTypes[0]);
            }
        }
        Float[] range = getRangeForSpinner(detail);
        if (filterType.equals(FilterType.iterative_filtering) && detail instanceof FilterRule) {
            FilterRule filterRule = (FilterRule) detail;
            if (!filterRule.isDefined()) {
                if (filterRule.getValue() < range[0] || filterRule.getValue() > range[1]) {
                    adjustmentMessage = true;
                    return builder.getPanel();
                }
            }
        }
        JLabel titleLabel = new JLabel();
        JCheckBox definedCheck = new JCheckBox();
        JComboBox typeCombo = new JComboBox();
        JLabel typeLabel = new JLabel();
        JComboBox comparisonCombo = new JComboBox();
        JSpinner valueSpinner = new JSpinner();
        titleLabel.setText("<html><b>" + detail.getPropertyTitle() + "</b></html>");
        builder.append(titleLabel);
        definedCheck.setSelected(false);
        builder.append(definedCheck);
        if (valueTypes == null) {
            builder.append(new JLabel());
        } else if (valueTypes.length == 1) {
            typeLabel.setText(valueTypes[0]);
            builder.append(typeLabel);
        } else {
            for (int i = 0; i < valueTypes.length; i++) {
                typeCombo.addItem(valueTypes[i]);
            }
            builder.append(typeCombo);
        }
        comparisonCombo.addItem(FilterComparisonOperator.lessThan);
        comparisonCombo.addItem(FilterComparisonOperator.equals);
        comparisonCombo.addItem(FilterComparisonOperator.greaterThan);
        builder.append(comparisonCombo);
        builder.append(valueSpinner);
        JButton add = new JButton(new AddAction(this, builder.getPanel()));
        enterPressesWhenFocused(add);
        builder.append(add);
        JButton delete = new JButton(new DeleteAction(this, builder.getPanel()));
        enterPressesWhenFocused(delete);
        builder.append(delete);
        if (detail instanceof FilterRule) {
            FilterRule filterRule = (FilterRule) detail;
            if (filterRule.isDefined()) {
                definedCheck.setSelected(true);
                typeCombo.setEnabled(false);
                typeLabel.setEnabled(false);
                comparisonCombo.setEnabled(false);
                valueSpinner.setEnabled(false);
                valueSpinner.setModel(getSpinnerModel(range[0], range));
            } else {
                if (valueTypes != null && valueTypes.length > 1) {
                    typeCombo.setSelectedItem(filterRule.getType());
                }
                comparisonCombo.setSelectedItem(filterRule.getComparisonOperator());
                valueSpinner.setModel(getSpinnerModel(filterRule.getValue(), range));
            }
        } else {
            valueSpinner.setModel(getSpinnerModel(range[0], range));
        }
        definedCheck.setAction(new DefinedCheckBoxAction(definedCheck, typeCombo, typeLabel, comparisonCombo, valueSpinner));
        typeCombo.setAction(new TypeComboBoxAction(detail, definedCheck, typeCombo, valueSpinner));
        comparisonCombo.setAction(new OperatorComboBoxAction(definedCheck));
        valueSpinner.addChangeListener(new SpinnerAction());
        return builder.getPanel();
    }

    /**
	 * Reads out every defined filter rule and stores it in an instance of the class
	 * <code>FilterRule</code>.
	 */
    private void readoutFilterPanel() {
        Component[] components = pureFilterPanel.getComponents();
        int count = components.length;
        if (count != 0) {
            count--;
        }
        for (int i = 0; i < count; i++) {
            JPanel panel = (JPanel) pureFilterPanel.getComponent(i);
            Detail detail = definedRules.get(i);
            FilterRule filterRule;
            if (detail instanceof FilterRule) {
                filterRule = (FilterRule) detail;
            } else {
                filterRule = new FilterRule();
                filterRule.setPropertyID(detail.getPropertyID());
                filterRule.setPropertyTitle(detail.getPropertyTitle());
                filterRule.setType(detail.getType());
                definedRules.remove(i);
                definedRules.add(i, (Detail) filterRule);
            }
            Component definedComponent = panel.getComponent(1);
            if (filterType.equals(FilterType.iterative_filtering) && definedComponent instanceof JLabel) {
                if (!filterRule.isDefined()) {
                    JSpinner valueSpinner = (JSpinner) panel.getComponent(4);
                    double value = (Double) valueSpinner.getValue();
                    filterRule.setValue((float) value);
                }
            } else {
                JCheckBox definedCheck = (JCheckBox) definedComponent;
                filterRule.setIsDefined(definedCheck.isSelected());
                if (!filterRule.isDefined()) {
                    Component typeComponent = panel.getComponent(2);
                    if (typeComponent instanceof JComboBox) {
                        JComboBox typeCombo = (JComboBox) typeComponent;
                        filterRule.setType((String) typeCombo.getSelectedItem());
                    }
                    JComboBox operatorCombo = (JComboBox) panel.getComponent(3);
                    filterRule.setComparisonOperator((FilterComparisonOperator) operatorCombo.getSelectedItem());
                    JSpinner valueSpinner = (JSpinner) panel.getComponent(4);
                    double value = (Double) valueSpinner.getValue();
                    filterRule.setValue((float) value);
                }
            }
        }
    }

    /**
	 * Calculates the minimum and maximum values that provide the bounds of the range of values
	 * to choose from for the assigned property.
	 * 
	 * @param detail The <code>Detail</code> object that encapsulates the property and value type.
	 * @return An array with minimum and maximum value.
	 */
    private Float[] getRangeForSpinner(Detail detail) {
        Float[] range = null;
        try {
            range = DBController.getInstance().getScaffoldMinMax(detail.getPropertyID(), detail.getType());
        } catch (DBException e) {
            boolean connected = false;
            while (!connected) {
                Object[] options = { i18n("Button.OK"), i18n("Button.Cancel") };
                int n = JOptionPane.showOptionDialog(this, i18n("Message.ConnectionProblems"), i18n("Message.Info"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == JOptionPane.YES_OPTION) {
                    connected = GUIController.getInstance().reconnect();
                    try {
                        range = DBController.getInstance().getScaffoldMinMax(detail.getPropertyID(), detail.getType());
                    } catch (DBException f) {
                        connected = false;
                    }
                } else {
                    try {
                        GUIController.getInstance().quitOnError();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, i18n("Message.FileNotFound"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, i18n("Message.IOException"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        if (filterType.equals(FilterType.iterative_filtering)) {
            ArrayList<FilterRule> activeRulesForProperty = new ArrayList<FilterRule>();
            Filter fd = GUIController.getInstance().getCurrentVCanvas().getFilter();
            ArrayList<FilterRule> canvasFilter = fd.getFilterRules();
            for (int i = 0; i < canvasFilter.size(); i++) {
                FilterRule filterRule = canvasFilter.get(i);
                if (!filterRule.isDefined()) {
                    if ((detail.getPropertyID().equals(filterRule.getPropertyID()) && detail.getType() == null) || (detail.getPropertyID().equals(filterRule.getPropertyID()) && detail.getType().equals(filterRule.getType()))) {
                        activeRulesForProperty.add(filterRule);
                    }
                }
            }
            if (activeRulesForProperty.size() > 0) {
                for (int i = 0; i < activeRulesForProperty.size(); i++) {
                    FilterRule filterRule = activeRulesForProperty.get(i);
                    if (filterRule.getComparisonOperator().equals(FilterComparisonOperator.equals)) {
                        range[0] = filterRule.getValue();
                        range[1] = filterRule.getValue();
                    } else if (filterRule.getComparisonOperator().equals(FilterComparisonOperator.greaterThan)) {
                        if (filterRule.getValue() > range[0] && filterRule.getValue() < range[1]) {
                            range[0] = filterRule.getValue();
                        }
                    } else if (filterRule.getComparisonOperator().equals(FilterComparisonOperator.lessThan)) {
                        if (filterRule.getValue() < range[1] && filterRule.getValue() > range[0]) {
                            range[1] = filterRule.getValue();
                        }
                    }
                }
            }
        }
        return range;
    }

    /**
	 * Creates the <code>SpinnerNumberModel</code> for a property with appropriate minimum and
	 * maximum values.
	 *  
	 * @param initalValue The current value of the model.
	 * @param range An array with minimum and maximum value. 
	 * @return The <code>SpinnerNumberModel</code> with appropriate values.
	 */
    private SpinnerNumberModel getSpinnerModel(Float initialValue, Float[] range) {
        Double init = new Double(initialValue.doubleValue());
        Double[] array = new Double[2];
        array[0] = new Double(range[0].doubleValue());
        array[1] = new Double(range[1].doubleValue());
        if ((init < array[0]) || (init > array[1])) {
            init = array[0];
        }
        if (range[1] - range[0] <= 0.5) {
            return new SpinnerNumberModel(init, array[0], array[1], new Double(0.01));
        } else if (range[1] - range[0] <= 1) {
            return new SpinnerNumberModel(init, array[0], array[1], new Double(0.1));
        } else {
            return new SpinnerNumberModel(init, array[0], array[1], new Double(1.0));
        }
    }

    /**
	 * Counts the number of visible and - if <code>bothNumbers</code> is <code>true</code> - 
	 * available scaffolds and sets it onto the according labels.
	 * 
	 * @param bothNumbers <code>true</code> if the number of visible and available scaffolds should
	 * be updated, <code>false</code> if just the number of visible scaffolds should be updated.
	 */
    private void fillNumberScaffoldsLabels(boolean bothNumbers) {
        readoutFilterPanel();
        newFilter.clear();
        for (int i = 0; i < definedRules.size(); i++) {
            newFilter.add((FilterRule) definedRules.get(i));
        }
        try {
            numberVisibleScaffolds = DBController.getInstance().countScaffolds(newFilter);
        } catch (DBException e) {
            boolean connected = false;
            while (!connected) {
                Object[] options = { i18n("Button.OK"), i18n("Button.Cancel") };
                int n = JOptionPane.showOptionDialog(this, i18n("Message.ConnectionProblems"), i18n("Message.Info"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == JOptionPane.YES_OPTION) {
                    connected = GUIController.getInstance().reconnect();
                    try {
                        numberVisibleScaffolds = DBController.getInstance().countScaffolds(newFilter);
                    } catch (DBException f) {
                        connected = false;
                    }
                } else {
                    try {
                        GUIController.getInstance().quitOnError();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, i18n("Message.FileNotFound"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, i18n("Message.IOException"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        if (bothNumbers) {
            if (filterType.equals(FilterType.iterative_filtering)) {
                try {
                    numberAvailableScaffolds = DBController.getInstance().countScaffolds(activeFilter);
                } catch (DBException e) {
                    boolean connected = false;
                    while (!connected) {
                        Object[] options = { i18n("Button.OK"), i18n("Button.Cancel") };
                        int n = JOptionPane.showOptionDialog(this, i18n("Message.ConnectionProblems"), i18n("Message.Info"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (n == JOptionPane.YES_OPTION) {
                            connected = GUIController.getInstance().reconnect();
                            try {
                                numberAvailableScaffolds = DBController.getInstance().countScaffolds(activeFilter);
                            } catch (DBException f) {
                                connected = false;
                            }
                        } else {
                            try {
                                GUIController.getInstance().quitOnError();
                            } catch (FileNotFoundException ex) {
                                JOptionPane.showMessageDialog(this, i18n("Message.FileNotFound"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(this, i18n("Message.IOException"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
                numberAvailableScaffoldsLabel.setText("<html><b>" + numberAvailableScaffolds + "</html></b>");
            } else {
                ArrayList<String> propertyIDs = new ArrayList<String>();
                for (int i = 0; i < definedRules.size(); i++) {
                    String id = definedRules.get(i).getPropertyID();
                    boolean exist = false;
                    for (int j = 0; j < propertyIDs.size(); j++) {
                        if (id.equals(propertyIDs.get(j))) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        propertyIDs.add(id);
                    }
                }
                if (propertyIDs.size() == 0) {
                    numberAvailableScaffolds = numberVisibleScaffolds;
                } else {
                    try {
                        numberAvailableScaffolds = DBController.getInstance().countScaffoldsWithProperties(propertyIDs);
                    } catch (DBException e) {
                        boolean connected = false;
                        while (!connected) {
                            Object[] options = { i18n("Button.OK"), i18n("Button.Cancel") };
                            int n = JOptionPane.showOptionDialog(this, i18n("Message.ConnectionProblems"), i18n("Message.Info"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            if (n == JOptionPane.YES_OPTION) {
                                connected = GUIController.getInstance().reconnect();
                                try {
                                    numberAvailableScaffolds = DBController.getInstance().countScaffoldsWithProperties(propertyIDs);
                                } catch (DBException f) {
                                    connected = false;
                                }
                            } else {
                                try {
                                    GUIController.getInstance().quitOnError();
                                } catch (FileNotFoundException ex) {
                                    JOptionPane.showMessageDialog(this, i18n("Message.FileNotFound"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(this, i18n("Message.IOException"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
                numberAvailableScaffoldsLabel.setText("<html><b>" + numberAvailableScaffolds + "</html></b>");
            }
        }
        if (numberVisibleScaffolds > GUIController.getInstance().getProfile().getMaxNumberScaffolds() || numberVisibleScaffolds > numberAvailableScaffolds) {
            numberVisibleScaffoldsLabel.setText("<html><b><font color=#FF0000>" + numberVisibleScaffolds + "</font></b></html>");
        } else {
            numberVisibleScaffoldsLabel.setText("<html><b>" + numberVisibleScaffolds + "</html></b>");
        }
        if (numberVisibleScaffolds == 0 || numberVisibleScaffolds > numberAvailableScaffolds) {
            forwardAction.setEnabled(false);
        } else {
            forwardAction.setEnabled(true);
        }
    }

    /**
	 * Creates the panel that holds the buttons.
	 * 
	 * @return The <code>JPanel</code> with all buttons.
	 */
    private JPanel getButtonPanel() {
        JButton filterHelp = new JButton(helpAction);
        enterPressesWhenFocused(filterHelp);
        JButton filterManagement = new JButton(manageAction);
        enterPressesWhenFocused(filterManagement);
        JButton filterCancel = new JButton(cancelAction);
        enterPressesWhenFocused(filterCancel);
        JButton filterBackward = new JButton(backwardAction);
        enterPressesWhenFocused(filterBackward);
        filterForward = new JButton(forwardAction);
        enterPressesWhenFocused(filterForward);
        filterForward.setHorizontalTextPosition(SwingConstants.LEFT);
        JPanel buttonPanel = ButtonBarFactory.buildHelpBar(filterHelp, filterCancel, filterManagement, filterBackward, filterForward);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        return buttonPanel;
    }

    /**
	 * Applies the defined filter rules. If <code>filterType</code> equals 
	 * <code>FilterType.iterative_filtering</code> the defined filter rules will be
	 * passed to the current <code>VCanvas</code>. Otherwise the defined filter rules
	 * will be passed to the <code>DBController</code>.
	 */
    private void applyFilter() {
        readoutFilterPanel();
        Filter filter = new Filter(newFilter, subsearchPanel.getSubstructure(), subsearchPanel.isSearchDisabled(), subsearchPanel.isScaffoldSearch(), subsearchPanel.isStructureSearch());
        if (filterType.equals(FilterType.iterative_filtering)) {
            if (filter.isSubsearchDisabled()) {
                GUIController.getInstance().getCurrentVCanvas().setFilter(filter);
                this.dispose();
            } else {
                Collection<Integer> domain = ((DBTree) GUIController.getInstance().getCurrentVCanvas().getDTree()).getScaffoldIDs();
                GUIController.getInstance().showFilterProgressDialog(this, filter, domain);
                FilterProgressDialog fpg = GUIController.getInstance().getFilterProgressDialog();
                if (fpg.getValue() == JOptionPane.YES_OPTION) {
                    GUIController.getInstance().getCurrentVCanvas().setFilter(filter);
                    this.dispose();
                }
            }
        } else {
            if (filter.isSubsearchDisabled()) {
                int setting = GUIController.getInstance().getProfile().getMaxNumberScaffolds();
                if (numberVisibleScaffolds > setting) {
                    Object[] options = { i18n("Quit.Dialog.Yes"), i18n("Quit.Dialog.No") };
                    int n = JOptionPane.showOptionDialog(this, String.format(i18n("Message.NumberOfScaffolds"), GUIController.getInstance().getProfile().getMaxNumberScaffolds(), numberVisibleScaffolds), i18n("Message.Info"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (n == JOptionPane.YES_OPTION) {
                        GUIController.getInstance().getProfile().setInitialFilter(filter);
                        this.dispose();
                        GUIController.getInstance().showMainFrame();
                    }
                } else {
                    GUIController.getInstance().getProfile().setInitialFilter(filter);
                    this.dispose();
                    GUIController.getInstance().showMainFrame();
                }
            } else {
                GUIController.getInstance().showFilterProgressDialog(this, filter);
                FilterProgressDialog fpg = GUIController.getInstance().getFilterProgressDialog();
                if (fpg.getValue() == JOptionPane.YES_OPTION) {
                    GUIController.getInstance().getProfile().setInitialFilter(filter);
                    this.dispose();
                    GUIController.getInstance().showMainFrame();
                }
            }
        }
    }

    /**
	 * Decides if the given rules are effective, i.e. if there are not equal and not conflicting.
	 * The rules are conflicting if they are defined for the same value but use conflicting
	 * comparison operators ("<" and ">" or "=" and ">" or "=" and "<").
	 * 
	 * @param filterRule The rule that should be testet for effectiveness..
	 * @param definedRule The reference rule that is already within the list of defined rules.
	 * @return <code>true>/code> if the <code>filterRule</code> is effective and can be added
	 * to the list of defined rules, <code>false</code> otherwise.
	 */
    private boolean areRulesEffective(FilterRule filterRule, FilterRule definedRule) {
        if (filterRule.getPropertyID().equals(definedRule.getPropertyID())) {
            if (filterRule.isDefined() && definedRule.isDefined()) {
                return false;
            } else if ((filterRule.isDefined() && !definedRule.isDefined()) || (!filterRule.isDefined() && definedRule.isDefined())) {
                return true;
            } else if ((filterRule.getType() == null) || (filterRule.getType() != null && filterRule.getType().equals(definedRule.getType()))) {
                if (filterRule.getComparisonOperator().equals(definedRule.getComparisonOperator())) {
                    if (filterRule.getValue() == definedRule.getValue()) {
                        return false;
                    } else {
                        return true;
                    }
                } else if (filterRule.getValue() == definedRule.getValue()) {
                    if ((filterRule.getComparisonOperator().equals(FilterComparisonOperator.lessThan) && definedRule.getComparisonOperator().equals(FilterComparisonOperator.greaterThan)) || (filterRule.getComparisonOperator().equals(FilterComparisonOperator.greaterThan) && definedRule.getComparisonOperator().equals(FilterComparisonOperator.lessThan))) {
                        return false;
                    } else if (definedRule.getComparisonOperator().equals(FilterComparisonOperator.equals) && (filterRule.getComparisonOperator().equals(FilterComparisonOperator.lessThan) || filterRule.getComparisonOperator().equals(FilterComparisonOperator.greaterThan))) {
                        return false;
                    } else if (filterRule.getComparisonOperator().equals(FilterComparisonOperator.equals) && (definedRule.getComparisonOperator().equals(FilterComparisonOperator.lessThan) || definedRule.getComparisonOperator().equals(FilterComparisonOperator.greaterThan))) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
	 * Shows a <code>JOptionPane</code> to inform the user that the filter rules that have been
	 * added must be adjusted.
	 */
    private void showAdjustmentMessage() {
        JOptionPane.showMessageDialog(this, i18n("Message.FilterAdjustment"), i18n("Message.Info"), JOptionPane.INFORMATION_MESSAGE);
        adjustmentMessage = false;
    }

    /**
	 * Loads the given list of filter rules into the dialog, i.e. already existing rules will
	 * be deleted.
	 * 
	 * @param filter The list of filter rules that should be loaded into the dialog.
	 */
    void loadFilterIntoDialog(ArrayList<FilterRule> filter) {
        definedRules.clear();
        for (int i = 0; i < filter.size(); i++) {
            FilterRule filterRule = new FilterRule(filter.get(i));
            addFilterRule(filterRule);
        }
        switch(panelCount) {
            case OVERVIEW_PANEL:
                fillOverviewPanel();
                this.validate();
                this.repaint();
                GUIController.getInstance().closeFilterManagerDialog();
                break;
            case FILTER_PANEL:
                this.remove(scrollFilterPanel);
                createFilterPanel();
                this.add(scrollFilterPanel);
                this.validate();
                this.repaint();
                GUIController.getInstance().closeFilterManagerDialog();
                break;
        }
    }

    /**
	 * Transfers the given list of filter rules into the dialog, i.e. the assigned rules
	 * will be added to the already existing rules.
	 * 
	 * @param filter The list of filter rules that should be transfered into the dialog.
	 */
    void transferFilterIntoDialog(ArrayList<FilterRule> filter) {
        if (panelCount == OVERVIEW_PANEL) {
            readoutOverviewPanel();
        }
        for (int i = 0; i < filter.size(); i++) {
            FilterRule filterRule = new FilterRule(filter.get(i));
            if (filterType.equals(FilterType.iterative_filtering)) {
                boolean add = true;
                for (int j = 0; j < activeFilter.size(); j++) {
                    FilterRule definedRule = activeFilter.get(j);
                    if (!areRulesEffective(filterRule, definedRule)) {
                        add = false;
                    }
                }
                if (add) {
                    addFilterRule(filterRule);
                } else {
                    adjustmentMessage = true;
                }
            } else {
                addFilterRule(filterRule);
            }
        }
        switch(panelCount) {
            case OVERVIEW_PANEL:
                fillOverviewPanel();
                this.validate();
                this.repaint();
                GUIController.getInstance().closeFilterManagerDialog();
                break;
            case FILTER_PANEL:
                this.remove(scrollFilterPanel);
                createFilterPanel();
                this.add(scrollFilterPanel);
                this.validate();
                this.repaint();
                GUIController.getInstance().closeFilterManagerDialog();
                if (adjustmentMessage) {
                    showAdjustmentMessage();
                }
                break;
        }
    }

    /**
	 * Adds a filter rule to the list of defined filters. If the rule
	 * is defined on a property not existing in the database the filter rule
	 * will be ignored.
	 * 
	 * @param filterRule
	 * @return true iff filterRule was added
	 */
    private boolean addFilterRule(Detail filterRule) {
        ScaffoldProperty property = new ScaffoldProperty(filterRule.getPropertyID(), filterRule.getPropertyTitle(), "");
        if (!GUIController.getInstance().getPropertiesNum().contains(property)) {
            return false;
        }
        definedRules.add(filterRule);
        return true;
    }

    private class DefinedCheckBoxAction extends AbstractAction {

        private JCheckBox check;

        private JComboBox typeComboBox;

        private JLabel typeLabel;

        private JComboBox comparisonComboBox;

        private JSpinner valueSpinner;

        public DefinedCheckBoxAction(JCheckBox check, JComboBox typeComboBox, JLabel typeLabel, JComboBox comparisonComboBox, JSpinner valueSpinner) {
            super(i18n("Filter.Color.Defined"));
            this.check = check;
            this.typeComboBox = typeComboBox;
            this.typeLabel = typeLabel;
            this.comparisonComboBox = comparisonComboBox;
            this.valueSpinner = valueSpinner;
        }

        public void actionPerformed(ActionEvent e) {
            if (check.isSelected()) {
                typeComboBox.setEnabled(false);
                typeLabel.setEnabled(false);
                comparisonComboBox.setEnabled(false);
                valueSpinner.setEnabled(false);
                fillNumberScaffoldsLabels(false);
            } else {
                typeComboBox.setEnabled(true);
                typeLabel.setEnabled(true);
                comparisonComboBox.setEnabled(true);
                valueSpinner.setEnabled(true);
                fillNumberScaffoldsLabels(false);
            }
        }
    }

    private class TypeComboBoxAction extends AbstractAction {

        private Detail detail;

        private JComboBox typeComboBox;

        private JSpinner valueSpinner;

        public TypeComboBoxAction(Detail detail, JCheckBox definedCheck, JComboBox typeComboBox, JSpinner valueSpinner) {
            super();
            this.detail = detail;
            this.typeComboBox = typeComboBox;
            this.valueSpinner = valueSpinner;
            if (definedCheck.isSelected()) {
                this.setEnabled(false);
            }
        }

        public void actionPerformed(ActionEvent e) {
            Detail newDetail = new Detail(detail);
            newDetail.setType((String) typeComboBox.getSelectedItem());
            Float[] range = getRangeForSpinner(newDetail);
            valueSpinner.setModel(getSpinnerModel(range[0], range));
            fillNumberScaffoldsLabels(false);
        }
    }

    private class OperatorComboBoxAction extends AbstractAction {

        public OperatorComboBoxAction(JCheckBox definedCheck) {
            if (definedCheck.isSelected()) {
                this.setEnabled(false);
            }
        }

        public void actionPerformed(ActionEvent e) {
            fillNumberScaffoldsLabels(false);
        }
    }

    private class SpinnerAction implements ChangeListener, ActionListener {

        public void stateChanged(ChangeEvent e) {
            if (spinnerTimer.isRunning()) {
                spinnerTimer.stop();
            }
            spinnerTimer.start();
        }

        public void actionPerformed(ActionEvent e) {
            fillNumberScaffoldsLabels(false);
        }
    }

    private class AddAction extends AbstractAction {

        private FilterDialog dialog;

        private JPanel rulePanel;

        public AddAction(FilterDialog dialog, JPanel rulePanel) {
            super();
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/16/plus.png")));
            putValue(Action.SHORT_DESCRIPTION, i18n("Filter.Add"));
            this.dialog = dialog;
            this.rulePanel = rulePanel;
        }

        public void actionPerformed(ActionEvent e) {
            int position = 0;
            for (int i = 0; i < pureFilterPanel.getComponentCount(); i++) {
                if (pureFilterPanel.getComponent(i) == rulePanel) {
                    position = i;
                }
            }
            Detail newDetail = new Detail(definedRules.get(position));
            definedRules.add(position + 1, newDetail);
            c.weighty = 1.0;
            pureFilterPanel.add(createRulePanel(newDetail), c, position + 1);
            fillNumberScaffoldsLabels(false);
            dialog.validate();
            dialog.repaint();
        }
    }

    private class DeleteAction extends AbstractAction {

        private FilterDialog dialog;

        private JPanel rulePanel;

        public DeleteAction(FilterDialog dialog, JPanel rulePanel) {
            super();
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/16/button_cancel.png")));
            putValue(Action.SHORT_DESCRIPTION, i18n("Filter.Delete"));
            this.dialog = dialog;
            this.rulePanel = rulePanel;
        }

        public void actionPerformed(ActionEvent e) {
            int position = 0;
            for (int i = 0; i < pureFilterPanel.getComponentCount(); i++) {
                if (pureFilterPanel.getComponent(i) == rulePanel) {
                    position = i;
                }
            }
            pureFilterPanel.remove(rulePanel);
            definedRules.remove(position);
            fillNumberScaffoldsLabels(true);
            dialog.validate();
            dialog.repaint();
        }
    }

    private class HelpAction extends AbstractAction {

        private FilterDialog dialog;

        public HelpAction(FilterDialog dialog) {
            super(i18n("Button.Help"));
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/help.png")));
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            GUIController.getInstance().showManualSection(dialog, ManualSection.filter);
        }
    }

    private class CancelAction extends AbstractAction {

        private FilterDialog dialog;

        public CancelAction(FilterDialog dialog) {
            super(i18n("Button.Cancel"));
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/button_cancel.png")));
            if (filterType.equals(FilterType.restart_filtering)) {
                putValue(Action.SHORT_DESCRIPTION, i18n("Filter.Restart.Description"));
            } else {
                if (!filterType.equals(FilterType.iterative_filtering)) {
                    putValue(Action.SHORT_DESCRIPTION, i18n("Filter.Cancel.Description"));
                }
            }
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            if (filterType.equals(FilterType.restart_filtering)) {
                dialog.dispose();
                GUIController.getInstance().setMainFrameVisible();
            } else {
                dialog.dispose();
                if (filterType.equals(FilterType.standard_filtering)) {
                    try {
                        GUIController.getInstance().quitApp();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(dialog, i18n("Message.FileNotFound"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dialog, i18n("Message.IOException"), i18n("Title.Error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private class ManageAction extends AbstractAction {

        public ManageAction() {
            super(i18n("Filter.Management.Title"));
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/filter.png")));
        }

        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            popupmenu.show(b, 0, b.getHeight());
        }
    }

    private class ManageDialogAction extends AbstractAction {

        private FilterDialog dialog;

        public ManageDialogAction(FilterDialog dialog) {
            super(i18n("Filter.ManageDialog"));
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            GUIController.getInstance().showFilterManagerDialog(dialog, filterType);
        }
    }

    private class StoreFilterAction extends AbstractAction {

        private FilterDialog dialog;

        public StoreFilterAction(FilterDialog dialog) {
            super(i18n("Filter.Store"));
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            String proposedName = "Filter";
            int counter = 1;
            boolean nameFound;
            do {
                nameFound = false;
                if (GUIController.getInstance().getProfile().getFilter(proposedName + counter) != null) {
                    counter++;
                    nameFound = true;
                }
            } while (nameFound);
            String name = JOptionPane.showInputDialog(dialog, i18n("Filter.StoreFilterMessage") + ":", proposedName + counter);
            if (name != null) {
                if (GUIController.getInstance().getProfile().getFilter(name) != null) {
                    int response = JOptionPane.showConfirmDialog(dialog, String.format(i18n("Message.FilterOverwrite"), name), i18n("Title.Warning"), JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                ArrayList<FilterRule> rules = new ArrayList<FilterRule>();
                for (int i = 0; i < newFilter.size(); i++) {
                    FilterRule f = new FilterRule(newFilter.get(i));
                    rules.add(f);
                }
                GUIController.getInstance().getProfile().setFilter(name, rules);
            }
        }
    }

    private class BackwardAction extends AbstractAction {

        private FilterDialog dialog;

        public BackwardAction(FilterDialog dialog) {
            super(i18n("Button.Backward"));
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/1leftarrow.png")));
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            switch(panelCount) {
                case SUBSEARCH_PANEL:
                    {
                        dialog.remove(subsearchPanel);
                        dialog.add(scrollFilterPanel);
                        panelCount = FILTER_PANEL;
                        forwardAction.setEnabled(true);
                        forwardAction.putValue(NAME, i18n("Button.Forward"));
                        forwardAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/1rightarrow.png")));
                        storeFilterAction.setEnabled(true);
                        manageAction.setEnabled(true);
                        dialog.validate();
                        dialog.repaint();
                        if (!definedRules.isEmpty()) {
                            break;
                        }
                    }
                case FILTER_PANEL:
                    {
                        readoutFilterPanel();
                        fillOverviewPanel();
                        dialog.remove(scrollFilterPanel);
                        dialog.add(overviewPanel);
                        panelCount = OVERVIEW_PANEL;
                        this.setEnabled(false);
                        forwardAction.setEnabled(true);
                        storeFilterAction.setEnabled(false);
                        dialog.validate();
                        dialog.repaint();
                        break;
                    }
            }
        }
    }

    private class ForwardAction extends AbstractAction {

        private FilterDialog dialog;

        public ForwardAction(FilterDialog dialog) {
            super(i18n("Button.Forward"));
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/1rightarrow.png")));
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            switch(panelCount) {
                case OVERVIEW_PANEL:
                    {
                        readoutOverviewPanel();
                        createFilterPanel();
                        dialog.remove(overviewPanel);
                        dialog.add(scrollFilterPanel);
                        panelCount = FILTER_PANEL;
                        filterForward.requestFocus();
                        backwardAction.setEnabled(true);
                        storeFilterAction.setEnabled(true);
                        dialog.validate();
                        dialog.repaint();
                        if (adjustmentMessage) {
                            showAdjustmentMessage();
                        }
                        if (!definedRules.isEmpty()) {
                            break;
                        }
                    }
                case FILTER_PANEL:
                    {
                        dialog.remove(scrollFilterPanel);
                        dialog.add(subsearchPanel);
                        panelCount = SUBSEARCH_PANEL;
                        filterForward.requestFocus();
                        this.putValue(NAME, i18n("Button.OK"));
                        this.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/scaffoldhunter/gui/images/button_ok.png")));
                        forwardAction.setEnabled(true);
                        backwardAction.setEnabled(true);
                        storeFilterAction.setEnabled(false);
                        manageAction.setEnabled(false);
                        dialog.validate();
                        dialog.repaint();
                        break;
                    }
                case SUBSEARCH_PANEL:
                    {
                        applyFilter();
                        break;
                    }
            }
        }
    }
}
