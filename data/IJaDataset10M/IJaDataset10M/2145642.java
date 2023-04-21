package pcgen.gui.generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.AbstractSpinnerModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import pcgen.gui.PCGenUIManager;
import pcgen.gui.facade.ClassFacade;
import pcgen.gui.facade.RaceFacade;
import pcgen.gui.facade.StatFacade;
import pcgen.gui.util.ComboSelectionBox;
import pcgen.gui.util.DefaultGenericComboBoxModel;
import pcgen.gui.util.GenericComboBoxModel;
import pcgen.gui.util.event.DocumentChangeAdapter;
import pcgen.gui.util.table.TableCellUtilities.SpinnerEditor;
import pcgen.gui.util.table.TableCellUtilities.SpinnerRenderer;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class CharacterBuildCustomizer extends JPanel implements Customizer {

    public static final String NAME_VALIDITY = "Name";

    public static final String ALIGNMENT_VALIDITY = "Alignment";

    public static final String GENDER_VALIDITY = "Gender";

    public static final String RACE_VALIDITY = "Race";

    public static final String STATS_VALIDITY = "Stats";

    public static final String CLASSES_VALIDITY = "Classes";

    private final TitledPanel namePanel;

    private final JTextField nameField;

    private final JComboBox namesetComboBox;

    private final TitledPanel alignmentPanel;

    private final JComboBox alignmentComboBox;

    private final TitledPanel genderPanel;

    private final JComboBox genderComboBox;

    private final TitledPanel racePanel;

    private final ComboSelectionBox raceSelectionBox;

    private final TitledPanel statPanel;

    private final ComboSelectionBox statSelectionBox;

    private final JButton statRollButton;

    private final StatPointsLabel statPointsLabel;

    private final StatSpinnerEditor statSpinnerEditor;

    private final StatTablePane statTablePane;

    private final TitledPanel classPanel;

    private final JCheckBox classGenerationCheckBox1;

    private final JCheckBox classGenerationCheckBox2;

    private final JCheckBox classGenerationCheckBox3;

    private final ComboSelectionBox classSelectionBox1;

    private final ComboSelectionBox classSelectionBox2;

    private final ComboSelectionBox classSelectionBox3;

    private final JComboBox levelComboBox1;

    private final JComboBox levelComboBox2;

    private final JComboBox levelComboBox3;

    private final Map<String, Boolean> validityMap;

    private MutableCharacterBuild build;

    public CharacterBuildCustomizer() {
        this.namePanel = new TitledPanel("Name");
        this.nameField = new JTextField();
        this.namesetComboBox = new JComboBox();
        this.alignmentPanel = new TitledPanel("Alignment");
        this.alignmentComboBox = new JComboBox();
        this.genderPanel = new TitledPanel("Gender");
        this.genderComboBox = new JComboBox();
        this.racePanel = new TitledPanel("Race");
        this.raceSelectionBox = new ComboSelectionBox();
        this.statPanel = new TitledPanel("Stats");
        this.statSelectionBox = new ComboSelectionBox();
        this.statRollButton = new JButton();
        this.statPointsLabel = new StatPointsLabel();
        this.statSpinnerEditor = new StatSpinnerEditor();
        this.statTablePane = new StatTablePane();
        this.classPanel = new TitledPanel("Classes");
        this.classGenerationCheckBox1 = new JCheckBox();
        this.classGenerationCheckBox2 = new JCheckBox();
        this.classGenerationCheckBox3 = new JCheckBox();
        this.classSelectionBox1 = new ComboSelectionBox();
        this.classSelectionBox2 = new ComboSelectionBox();
        this.classSelectionBox3 = new ComboSelectionBox();
        this.levelComboBox1 = new JComboBox();
        this.levelComboBox2 = new JComboBox();
        this.levelComboBox3 = new JComboBox();
        this.validityMap = new HashMap<String, Boolean>();
        initComponents();
    }

    public void setValidity(String prop, boolean valid) {
        boolean oldvalue = validityMap.get(prop);
        validityMap.put(prop, valid);
        firePropertyChange(prop, oldvalue, valid);
    }

    public boolean isCharacterValid() {
        return !validityMap.values().contains(Boolean.FALSE);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        {
            namePanel.setLayout(new GridBagLayout());
            {
                nameField.getDocument().addDocumentListener(new DocumentChangeAdapter() {

                    @Override
                    public void documentChanged(DocumentEvent e) {
                        String text = nameField.getText();
                        setValidity(NAME_VALIDITY, text != null && text.length() > 0);
                    }
                });
            }
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(4, 0, 4, 0);
            namePanel.add(nameField, gridBagConstraints);
            JButton nameButton;
            {
                nameButton = new JButton(new GenerateNameAction());
                nameButton.setFocusable(false);
            }
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 0.0;
            namePanel.add(nameButton, gridBagConstraints);
            gridBagConstraints.gridwidth = 1;
            namePanel.add(new JLabel("Name Set:"), gridBagConstraints);
            {
            }
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            namePanel.add(namesetComboBox, gridBagConstraints);
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        add(namePanel, gridBagConstraints);
        {
            alignmentPanel.setLayout(new BorderLayout());
            {
                alignmentComboBox.addActionListener(new ActionListener() {

                    @SuppressWarnings("unchecked")
                    public void actionPerformed(ActionEvent e) {
                        checkSelectionValidity();
                    }
                });
            }
            alignmentPanel.add(alignmentComboBox, BorderLayout.CENTER);
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(4, 4, 0, 4);
        add(alignmentPanel, gridBagConstraints);
        {
            genderPanel.setLayout(new BorderLayout());
            {
            }
            genderPanel.add(genderComboBox, BorderLayout.CENTER);
        }
        gridBagConstraints.insets = new Insets(0, 4, 4, 4);
        add(genderPanel, gridBagConstraints);
        {
            racePanel.setLayout(new BorderLayout());
            {
                raceSelectionBox.addItemListener(new ItemListener() {

                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            @SuppressWarnings("unchecked") InfoFacadeGenerator<RaceFacade> raceGenerator = (InfoFacadeGenerator<RaceFacade>) e.getItem();
                            StatTableModel model = statTablePane.getModel();
                            if (raceGenerator.isSingleton()) {
                                RaceFacade race = raceGenerator.getNext();
                                model.setRace(race);
                            } else {
                                model.setRace(null);
                            }
                            checkSelectionValidity();
                        }
                    }
                });
            }
            racePanel.add(raceSelectionBox, BorderLayout.CENTER);
        }
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        add(racePanel, gridBagConstraints);
        {
            statPanel.setLayout(new GridBagLayout());
            {
                statSelectionBox.addItemListener(new ItemListener() {

                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            @SuppressWarnings("unchecked") StatGenerationMethod statGenerator = (StatGenerationMethod) e.getItem();
                            StatTableModel model = statTablePane.getModel();
                            if (statGenerator instanceof PurchaseMethod) {
                                PurchaseMethod purchaseMode = (PurchaseMethod) statGenerator;
                                if (model.setPurchaseMode(purchaseMode)) {
                                    statPointsLabel.setPoints(purchaseMode.getPoints());
                                    statSpinnerEditor.setPurchaseMode(purchaseMode);
                                }
                                statTablePane.setUpperLeft(statPointsLabel);
                            } else {
                                model.setPurchaseMode(null);
                                statSpinnerEditor.setPurchaseMode(null);
                                statTablePane.setUpperLeft(statRollButton);
                                statPointsLabel.setPoints(0);
                            }
                        }
                    }
                });
            }
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 0, 2, 0);
            statPanel.add(statSelectionBox, gridBagConstraints);
            {
                statRollButton.setAction(new RollStatsAction());
            }
            {
                statTablePane.setStatSpinnerEditor(statSpinnerEditor);
            }
            statPanel.add(statTablePane, gridBagConstraints);
        }
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        add(statPanel, gridBagConstraints);
        {
            classPanel.setLayout(new GridBagLayout());
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = 2;
            classPanel.add(new JLabel(), gridBagConstraints);
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 4, 0, 0);
            classPanel.add(new JLabel("Class Generation"), gridBagConstraints);
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 0.5;
            classPanel.add(new JLabel("Level Generation"), gridBagConstraints);
            SelectClassRowAction rowAction;
            JLabel label;
            {
                label = new JLabel("1st Class:");
                rowAction = new SelectClassRowAction(classGenerationCheckBox1, label, classSelectionBox1, levelComboBox1);
                classGenerationCheckBox1.setAction(rowAction);
            }
            {
            }
            {
            }
            initClassSelectionRow(classGenerationCheckBox1, label, classSelectionBox1, levelComboBox1);
            {
                label = new JLabel("2nd Class:");
                SelectClassRowAction action = new SelectClassRowAction(classGenerationCheckBox2, label, classSelectionBox2, levelComboBox2);
                rowAction.setRowAction(action);
                rowAction = action;
                classGenerationCheckBox2.setAction(rowAction);
            }
            {
            }
            {
            }
            initClassSelectionRow(classGenerationCheckBox2, label, classSelectionBox2, levelComboBox2);
            {
                label = new JLabel("3rd Class:");
                SelectClassRowAction action = new SelectClassRowAction(classGenerationCheckBox3, label, classSelectionBox3, levelComboBox3);
                rowAction.setRowAction(action);
                rowAction = action;
                classGenerationCheckBox2.setAction(rowAction);
            }
            {
            }
            {
            }
            initClassSelectionRow(classGenerationCheckBox3, label, classSelectionBox3, levelComboBox3);
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        add(classPanel, gridBagConstraints);
        initListeners();
    }

    private void initClassSelectionRow(JCheckBox classGenerationBox, JLabel label, ComboSelectionBox classSelectionBox, JComboBox levelComboBox) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        classPanel.add(classGenerationBox, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        classPanel.add(label, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(2, 0, 2, 4);
        classPanel.add(classSelectionBox, gridBagConstraints);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new Insets(2, 0, 2, 2);
        classPanel.add(levelComboBox, gridBagConstraints);
    }

    private <T> GenericComboBoxModel<T> createComboBoxModel(List<T> data) {
        return new DefaultGenericComboBoxModel<T>(data);
    }

    private void initListeners() {
        addPropertyChangeListener(CharacterCreationManager.NAME_VALIDITY, namePanel);
        addPropertyChangeListener(CharacterCreationManager.ALIGNMENT_VALIDITY, alignmentPanel);
        addPropertyChangeListener(CharacterCreationManager.GENDER_VALIDITY, genderPanel);
        addPropertyChangeListener(CharacterCreationManager.RACE_VALIDITY, racePanel);
        addPropertyChangeListener(CharacterCreationManager.CLASSES_VALIDITY, classPanel);
    }

    public void setObject(Object bean) {
        CharacterBuild build = (CharacterBuild) bean;
        this.build = (MutableCharacterBuild) build;
    }

    private void initModels() {
    }

    private static boolean anyValid(Generator<Integer> alignmentGenerator, RaceFacade race) {
        for (Integer alignment : alignmentGenerator.getAll()) {
            {
                return true;
            }
        }
        return false;
    }

    private static boolean anyValid(Generator<Integer> alignmentGenerator, ClassFacade c) {
        for (Integer alignment : alignmentGenerator.getAll()) {
            if (c.isAcceptableAlignment(alignment)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isClassSelectionValid(ComboSelectionBox box, Generator<Integer> alignmentGenerator, RaceFacade race) {
        boolean accept = true;
        if (box.isEnabled()) {
            @SuppressWarnings("unchecked") InfoFacadeGenerator<ClassFacade> classGenerator = (InfoFacadeGenerator<ClassFacade>) box.getSelectedItem();
            if (classGenerator.isSingleton()) {
                ClassFacade c = classGenerator.getNext();
                accept = anyValid(alignmentGenerator, c);
                if (race != null) {
                    accept &= c.isAcceptableRace(race);
                }
            }
        }
        return accept;
    }

    @SuppressWarnings("unchecked")
    private void checkSelectionValidity() {
        Generator<Integer> alignmentGenerator = (Generator<Integer>) alignmentComboBox.getSelectedItem();
        InfoFacadeGenerator<RaceFacade> raceGenerator = (InfoFacadeGenerator<RaceFacade>) raceSelectionBox.getSelectedItem();
        RaceFacade race = null;
        if (raceGenerator.isSingleton()) {
            race = raceGenerator.getNext();
        }
        if (race != null) {
            setValidity(RACE_VALIDITY, anyValid(alignmentGenerator, race));
        } else {
            setValidity(RACE_VALIDITY, true);
        }
        boolean accept = isClassSelectionValid(classSelectionBox1, alignmentGenerator, race);
        accept &= isClassSelectionValid(classSelectionBox2, alignmentGenerator, race);
        accept &= isClassSelectionValid(classSelectionBox3, alignmentGenerator, race);
        setValidity(CLASSES_VALIDITY, accept);
    }

    private class GenerateNameAction extends AbstractAction {

        public GenerateNameAction() {
            super("Generate Name");
        }

        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked") Generator<String> nameGenerator = (Generator<String>) namesetComboBox.getSelectedItem();
            nameField.setText(nameGenerator.getNext());
        }
    }

    private class RollStatsAction extends AbstractAction {

        public RollStatsAction() {
            super("Roll");
        }

        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked") Generator<Integer> statGenerator = (Generator<Integer>) statSelectionBox.getSelectedItem();
            StatTableModel model = statTablePane.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(statGenerator.getNext(), i, 1);
            }
        }
    }

    private static class ExclusiveComboBoxModel extends DefaultComboBoxModel implements ListDataListener {

        private int excludedIndex = -1;

        private DefaultComboBoxModel excludedModel;

        public ExclusiveComboBoxModel(DefaultComboBoxModel excludedModel) {
            this.excludedModel = excludedModel;
            excludedModel.addListDataListener(this);
            for (int index = 0; index < excludedModel.getSize(); index++) {
                Object item = excludedModel.getElementAt(index);
                if (item == excludedModel.getSelectedItem()) {
                    excludedIndex = index;
                } else {
                    addElement(item);
                }
            }
        }

        public void intervalAdded(ListDataEvent e) {
            for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
                if (excludedIndex != -1 && excludedIndex < index) {
                    insertElementAt(excludedModel.getElementAt(index), index - 1);
                } else {
                    insertElementAt(excludedModel.getElementAt(index), index);
                    excludedIndex++;
                }
            }
        }

        public void intervalRemoved(ListDataEvent e) {
            for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
                if (excludedIndex != -1 && excludedIndex < e.getIndex0()) {
                    removeElementAt(e.getIndex0() - 1);
                } else {
                    removeElementAt(e.getIndex0());
                    if (excludedIndex != -1) {
                        if (excludedIndex == e.getIndex0()) {
                            excludedIndex = -1;
                        }
                        excludedIndex--;
                    }
                }
            }
        }

        public void contentsChanged(ListDataEvent e) {
            if (e.getIndex0() < 0) {
                if (excludedIndex != -1) {
                    insertElementAt(excludedModel.getElementAt(excludedIndex), excludedIndex);
                }
                excludedIndex = excludedModel.getIndexOf(excludedModel.getSelectedItem());
                if (excludedIndex != -1) {
                    removeElementAt(excludedIndex);
                }
            }
        }
    }

    private static class SelectClassRowAction extends AbstractAction {

        private JCheckBox actionBox;

        private JLabel label;

        private ComboSelectionBox selectionBox;

        private JComboBox comboBox;

        private SelectClassRowAction rowAction;

        public SelectClassRowAction(JCheckBox actionBox, JLabel label, ComboSelectionBox selectionBox, JComboBox comboBox) {
            this.actionBox = actionBox;
            this.label = label;
            this.selectionBox = selectionBox;
            this.comboBox = comboBox;
        }

        public void actionPerformed(ActionEvent e) {
            setEnabled(true);
        }

        @Override
        public void setEnabled(boolean newValue) {
            super.setEnabled(newValue);
            label.setEnabled(newValue);
            newValue &= actionBox.isSelected();
            selectionBox.setEnabled(newValue);
            comboBox.setEnabled(newValue);
            if (rowAction != null) {
                rowAction.setEnabled(newValue);
            }
        }

        public void setRowAction(SelectClassRowAction rowAction) {
            this.rowAction = rowAction;
            rowAction.setEnabled(false);
        }
    }

    private static class TitledPanel extends JPanel implements PropertyChangeListener {

        private final TitledBorder titleBorder;

        public TitledPanel(String title) {
            this.titleBorder = new TitledBorder(null, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 12));
            setBorder(this.titleBorder);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if ((Boolean) evt.getNewValue()) {
                titleBorder.setTitleColor(Color.BLACK);
            } else {
                titleBorder.setTitleColor(Color.RED);
            }
            repaint();
        }
    }

    private class StatPointsLabel extends JLabel {

        private int points;

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
            setValidity(STATS_VALIDITY, points == 0);
            repaint();
        }

        @Override
        public String getText() {
            return "Points: " + points;
        }
    }

    private class StatSpinnerModel extends AbstractSpinnerModel {

        private final PurchaseMethod purchaseMode;

        private Integer score;

        public StatSpinnerModel(PurchaseMethod purchaseMode, Integer score) {
            this.purchaseMode = purchaseMode;
            this.score = score;
        }

        public Object getValue() {
            return score;
        }

        public void setValue(Object value) {
            setValue((Integer) value);
        }

        private void setValue(Integer value) {
            if (value < purchaseMode.getMinScore() || value > purchaseMode.getMaxScore()) {
                throw new IllegalArgumentException();
            }
            int points = statPointsLabel.getPoints();
            int cost = purchaseMode.getScoreCost(value) - purchaseMode.getScoreCost(score);
            if (cost > points) {
                throw new IllegalArgumentException();
            }
            score = value;
            statPointsLabel.setPoints(points - cost);
            fireStateChanged();
        }

        public Object getNextValue() {
            if (score == purchaseMode.getMaxScore()) {
                return null;
            }
            int value = score + 1;
            int points = statPointsLabel.getPoints();
            int cost = purchaseMode.getScoreCost(value) - purchaseMode.getScoreCost(score);
            if (cost > points) {
                return null;
            }
            return value;
        }

        public Object getPreviousValue() {
            if (score == purchaseMode.getMinScore()) {
                return null;
            }
            return score - 1;
        }
    }

    private class StatSpinnerEditor extends SpinnerEditor {

        private final SpinnerNumberModel defaultModel = new SpinnerNumberModel(0, 0, null, 0);

        private PurchaseMethod purchaseMode;

        public void setPurchaseMode(PurchaseMethod purchaseMode) {
            this.purchaseMode = purchaseMode;
            if (purchaseMode == null) {
                spinner.setModel(defaultModel);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (purchaseMode != null) {
                spinner.setModel(new StatSpinnerModel(purchaseMode, (Integer) value));
                return spinner;
            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
    }

    private static class StatTableModel extends AbstractTableModel {

        private static final String[] columns = new String[] { "Base Score", "Racial Adj", "Total", "Mod", "Cost" };

        private final CharacterCreationManager manager;

        private final List<StatFacade> stats;

        private PurchaseMethod purchaseMode;

        private RaceFacade race;

        public StatTableModel(CharacterCreationManager manager) {
            this.manager = manager;
            this.stats = manager.getStats();
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return StatFacade.class;
                default:
                    return Integer.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return null;
                default:
                    return columns[column - 1];
            }
        }

        public PurchaseMethod getPurchaseMode() {
            return purchaseMode;
        }

        public boolean setPurchaseMode(PurchaseMethod purchaseMode) {
            if (this.purchaseMode == purchaseMode) {
                return false;
            }
            if (purchaseMode != null) {
                for (StatFacade stat : stats) {
                }
            }
            this.purchaseMode = purchaseMode;
            fireTableStructureChanged();
            return true;
        }

        public void setRace(RaceFacade race) {
            if (this.race != race) {
                this.race = race;
                fireTableDataChanged();
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                return true;
            }
            return false;
        }

        public int getRowCount() {
            return stats.size();
        }

        public int getColumnCount() {
            if (purchaseMode != null) {
                return 6;
            }
            return 5;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            StatFacade stat = stats.get(rowIndex);
            if (columnIndex == 0) {
                return stat;
            }
            int score = 0;
            if (columnIndex == 1) {
                return score;
            }
            if (columnIndex == 5) {
                if (purchaseMode.getMaxScore() == score) {
                    return null;
                }
                return purchaseMode.getScoreCost(score + 1) - purchaseMode.getScoreCost(score);
            }
            int adj = 0;
            if (race != null) {
                adj = race.getRacialAdj(stat);
            }
            if (columnIndex == 2) {
                return adj;
            }
            score += adj;
            if (columnIndex == 3) {
                return score;
            }
            return manager.getModForScore(score);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    private static class StatTablePane extends JScrollPane implements TableModelListener {

        private final JTable rowTable;

        private final JTable statTable;

        private StatSpinnerEditor editor;

        private StatTableModel model;

        public StatTablePane() {
            super(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.rowTable = new JTable();
            this.statTable = new JTable();
            initComponents();
        }

        private void initComponents() {
            rowTable.setPreferredScrollableViewportSize(new Dimension(75, 0));
            rowTable.setAutoCreateColumnsFromModel(false);
            rowTable.setFocusable(false);
            rowTable.addColumn(new TableColumn());
            ListSelectionModel selectionModel = rowTable.getSelectionModel();
            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setRowHeaderView(rowTable);
            statTable.setAutoCreateColumnsFromModel(false);
            statTable.setSelectionModel(selectionModel);
            setViewportView(statTable);
        }

        private void createDefaultColumnsFromModel() {
            TableColumnModel columnModel = statTable.getColumnModel();
            while (columnModel.getColumnCount() > 0) {
                columnModel.removeColumn(columnModel.getColumn(0));
            }
            TableColumn column = new TableColumn(1);
            column.setCellRenderer(new SpinnerRenderer());
            column.setCellEditor(editor);
            statTable.addColumn(column);
            for (int i = 2; i < model.getColumnCount(); i++) {
                column = new TableColumn(i);
                statTable.addColumn(column);
            }
        }

        public StatTableModel getModel() {
            return model;
        }

        public void setModel(StatTableModel model) {
            if (this.model != null) {
                this.model.removeTableModelListener(this);
            }
            this.model = model;
            if (this.model != null) {
                this.model.addTableModelListener(this);
            }
            rowTable.setModel(model);
            statTable.setModel(model);
            createDefaultColumnsFromModel();
        }

        public void setStatSpinnerEditor(StatSpinnerEditor editor) {
            this.editor = editor;
        }

        public void setUpperLeft(Component upperLeft) {
            setCorner(JScrollPane.UPPER_LEFT_CORNER, upperLeft);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            Component view = getViewport().getView();
            if (view != null) {
                Dimension size = view.getPreferredSize();
                view = getRowHeader();
                if (view != null) {
                    size.width += view.getPreferredSize().width;
                }
                view = getColumnHeader();
                if (view != null) {
                    size.height += view.getPreferredSize().height;
                }
                Insets insets = getInsets();
                size.width += insets.left + insets.right;
                size.height += insets.top + insets.bottom;
                return size;
            }
            return super.getPreferredSize();
        }

        public void tableChanged(TableModelEvent e) {
            if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                createDefaultColumnsFromModel();
            }
        }
    }
}
