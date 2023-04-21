package org.vikamine.swing.subgroup.editors.tuningtable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.event.TableModelListener;
import org.vikamine.app.DMManager;
import org.vikamine.app.Resources;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.data.NominalAttribute;
import org.vikamine.kernel.data.Ontology;
import org.vikamine.kernel.data.Population;
import org.vikamine.kernel.data.Value;
import org.vikamine.kernel.statistics.StatisticComponent;
import org.vikamine.kernel.subgroup.SConstraints;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGDescription;
import org.vikamine.kernel.subgroup.selectors.DefaultSGSelector;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;
import org.vikamine.kernel.subgroup.target.SGTarget;
import org.vikamine.kernel.util.VKMUtil;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;
import org.vikamine.swing.subgroup.SGStatInfoSettings;
import org.vikamine.swing.subgroup.quality.QualityStatisticComponent;
import org.vikamine.swing.util.DecimalOrStringFormat;

public class TuningTableModel extends AbstractInteractionTableModel {

    private static final long serialVersionUID = -4321039064576847901L;

    public static final String UNDEFINED = "-";

    private static final String VALUE_YES = "x";

    private static final String VALUE_NO = "";

    public static final String NUMBER_COLUMN_HEAD = Resources.I18N.getString("vikamine.subgroupTuningTable.numberColumn");

    private static final DecimalOrStringFormat DECIMAL_FORMAT = new DecimalOrStringFormat("#.###");

    private List<SG> interactionSGs;

    private AssociatedTableHeads associatedTableHeads;

    private SGTarget target;

    private boolean autoSort;

    private boolean showAbbreviationHeads;

    private final List<StatisticComponent> statComponents;

    public List<StatisticComponent> getStatComponents() {
        return Collections.unmodifiableList(statComponents);
    }

    private Population definedPopulation;

    /**
     * The sort-order
     */
    private boolean ascending = true;

    private int sortColumn;

    private static final String TAHLE_PREFIX = "T";

    private boolean isAddAttribute = false;

    public TuningTableModel() {
        interactionSGs = new LinkedList<SG>();
        associatedTableHeads = new AssociatedTableHeads();
        sortColumn = -1;
        showAbbreviationHeads = true;
        autoSort = true;
        statComponents = new ArrayList<StatisticComponent>();
        computeStatComponents();
    }

    private void computeStatComponents() {
        SGStatInfoSettings settings = AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getTuningTableStatInfoSettings();
        List<StatisticComponent> allSCs = StatisticComponent.getAllValidComponents(getTarget());
        allSCs.add(0, QualityStatisticComponent.getInstance());
        statComponents.clear();
        for (StatisticComponent sc : allSCs) {
            if (settings.isEnabled(sc)) {
                statComponents.add(sc);
            }
        }
    }

    public int getRowCount() {
        return interactionSGs.size();
    }

    public int getColumnCount() {
        return 1 + Util.getSortedValuesOfAssociations(associatedTableHeads).size() + statComponents.size();
    }

    public String getColumnName(int columnIndex) {
        int associatedTableHeadsSize = associatedTableHeads.size();
        if (columnIndex >= getColumnCount()) System.err.println("Index out of bounds: " + columnIndex + " (only " + getColumnCount() + " available)");
        if (columnIndex == 0) return NUMBER_COLUMN_HEAD; else if (columnIndex <= associatedTableHeadsSize) {
            if (isShowAbbreviationHeads()) return Util.getSortedValueAbbreviationsOfAssociationTableHeads(associatedTableHeads).get(columnIndex - 1);
            return Util.getSortedValuesOfAssociations(associatedTableHeads).get(columnIndex - 1).getDescription();
        } else {
            return statComponents.get(columnIndex - Util.getSortedValuesOfAssociations(associatedTableHeads).size() - 1).getDescription();
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public List getSelectedSGs(int[] selectedRows) {
        List sgs = new LinkedList();
        for (int row : selectedRows) {
            if (row >= 0 && row < interactionSGs.size()) {
                sgs.add(interactionSGs.get(row));
            }
        }
        return sgs;
    }

    public List<SG> getAllSgs() {
        return interactionSGs;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        List<Value> sortedValuesOfAsso = Util.getSortedValuesOfAssociations(associatedTableHeads);
        if (columnIndex == 0) return rowIndex + 1; else if (columnIndex <= sortedValuesOfAsso.size()) {
            int shiftColumnIndex = columnIndex - 1;
            Value associatedValue = sortedValuesOfAsso.get(shiftColumnIndex);
            SG sg = interactionSGs.get(rowIndex);
            if (sg == null) return VALUE_NO;
            List<Value> sgValues = getAllValuesContainedInSGDescription(sg);
            for (Iterator iterator = sgValues.iterator(); iterator.hasNext(); ) {
                Value currentValue = (Value) iterator.next();
                if (Util.valuesEqual(associatedValue, currentValue)) return VALUE_YES;
            }
            return VALUE_NO;
        } else {
            StatisticComponent statComponentForColumn = statComponents.get(columnIndex - (sortedValuesOfAsso.size() + 1));
            SG sg = interactionSGs.get(rowIndex);
            if (sg.getSGDescription().isEmpty()) return UNDEFINED;
            return DECIMAL_FORMAT.format(statComponentForColumn.getValue(sg.getStatistics()));
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    private List<Value> getAllValuesContainedInSGDescription(SG sg) {
        LinkedList<Value> allSortedValues = new LinkedList<Value>();
        for (Iterator iter = sg.getSGDescription().iterator(); iter.hasNext(); ) {
            SGNominalSelector selector = (SGNominalSelector) iter.next();
            allSortedValues.addAll(Util.getAllValuesForSelectorAttribute(selector));
        }
        return allSortedValues;
    }

    public void setInterestingSGs(List<SG> interestingSGs) {
        Iterator<SG> sgIterator = interestingSGs.iterator();
        if (sgIterator.hasNext()) {
            SG firstSG = sgIterator.next().deepCopy();
            target = firstSG.getTarget();
            interactionSGs = new LinkedList<SG>();
            sortColumn = -1;
            definedPopulation = null;
            Set<SGNominalSelector> sgSelectors = new HashSet<SGNominalSelector>();
            sgSelectors.addAll(Util.getSelectorsOfSG(firstSG));
            interactionSGs.add(firstSG);
            while (sgIterator.hasNext()) {
                SG sg = sgIterator.next().deepCopy();
                if (target.equals(sg.getTarget())) {
                    for (Iterator<SGNominalSelector> iter = Util.getSelectorsOfSG(sg).iterator(); iter.hasNext(); ) {
                        sgSelectors.add(iter.next());
                    }
                    interactionSGs.add(sg);
                }
            }
            List<SGNominalSelector> sortedSelectors = VKMUtil.asList(sgSelectors.iterator());
            Collections.sort(sortedSelectors, new Comparator<SGNominalSelector>() {

                public int compare(SGNominalSelector o1, SGNominalSelector o2) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
            });
            associatedTableHeads = createAssociatedTableHeads(sortedSelectors);
            fireTableStructureChanged(TuningTableModelEvent.EXTENDED_TYPE_COLUMN_INSERTED);
        }
    }

    public void addSGsToInterestingSGs(List<SG> subgroups) {
        if (subgroups == null || subgroups.isEmpty()) {
            return;
        }
        Iterator<SG> sgIterator = subgroups.iterator();
        if (sgIterator.hasNext()) {
            SG firstSG = sgIterator.next().deepCopy();
            if (target == null) {
                target = firstSG.getTarget();
            }
            if (interactionSGs == null) {
                interactionSGs = new LinkedList<SG>();
            }
            sortColumn = -1;
            Set<SGNominalSelector> sgSelectors = new HashSet<SGNominalSelector>();
            for (SG sg : interactionSGs) {
                sgSelectors.addAll(Util.getSelectorsOfSG(sg));
            }
            if (!interactionSGs.contains(firstSG)) {
                sgSelectors.addAll(Util.getSelectorsOfSG(firstSG));
                interactionSGs.add(firstSG);
            }
            while (sgIterator.hasNext()) {
                SG sg = sgIterator.next().deepCopy();
                if (!interactionSGs.contains(sg)) {
                    if (target.equals(sg.getTarget())) {
                        for (Iterator<SGNominalSelector> iter = Util.getSelectorsOfSG(sg).iterator(); iter.hasNext(); ) {
                            sgSelectors.add(iter.next());
                        }
                        interactionSGs.add(sg);
                    }
                }
            }
            List<SGNominalSelector> sortedSelectors = VKMUtil.asList(sgSelectors.iterator());
            Collections.sort(sortedSelectors, new Comparator<SGNominalSelector>() {

                public int compare(SGNominalSelector o1, SGNominalSelector o2) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
            });
            associatedTableHeads = createAssociatedTableHeads(sortedSelectors);
            fireTableStructureChanged(TuningTableModelEvent.EXTENDED_TYPE_COLUMN_INSERTED);
        }
    }

    public void addDMAttribute(NominalAttribute dmAttribute) throws Exception {
        LinkedList<Value> valuesList = new LinkedList<Value>();
        for (Iterator itera = dmAttribute.allValuesIterator(); itera.hasNext(); ) {
            Value value = (Value) itera.next();
            valuesList.add(value);
        }
        DefaultSGSelector newSGSelector = new DefaultSGSelector(dmAttribute, new HashSet(valuesList));
        LinkedList<SGNominalSelector> newSGSelectorList = getSelectorListFromAssociatedHeaders();
        newSGSelectorList.add(newSGSelector);
        isAddAttribute = true;
        associatedTableHeads = createAssociatedTableHeads(newSGSelectorList);
        if (sortColumn != -1) sortColumn += valuesList.size();
        fireTableStructureChanged(TuningTableModelEvent.EXTENDED_TYPE_COLUMN_INSERTED);
    }

    private AssociatedTableHeads createAssociatedTableHeads(List<SGNominalSelector> sortedSGSelectors) {
        AssociatedTableHeads assTableHeads = new AssociatedTableHeads();
        int mainHeadID = 1;
        int subHeadID = 1;
        List<SGNominalSelector> processedSelectors = new ArrayList<SGNominalSelector>();
        int tableCounter = AllSubgroupPluginController.getInstance().getTuningTableController().getIndexOfTableModel(this) + 1;
        int threshold = AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getMinAbsoluteFrequencyThreshold();
        boolean addTablePrefix = (AllSubgroupPluginController.getInstance().getTuningTableController().getTuningTablesCount() > 1);
        for (Iterator iter = sortedSGSelectors.iterator(); iter.hasNext(); ) {
            SGNominalSelector selector = (SGNominalSelector) iter.next();
            List<Value> values = new ArrayList<Value>();
            String tablePrefix = addTablePrefix ? TuningTableModel.TAHLE_PREFIX + "" + tableCounter + "_" : "";
            String attributeName = (mainHeadID < 10) ? "A0" + mainHeadID : "A" + mainHeadID + "";
            String selectorAbbreviation = tablePrefix + attributeName;
            LinkedList<String> valueAbbreviations = new LinkedList<String>();
            mainHeadID++;
            Attribute dmAttribute = selector.getAttribute();
            if (dmAttribute.isNumeric()) {
                Set<Value> values2 = selector.getValues();
                for (Iterator iterator = values2.iterator(); iterator.hasNext(); ) {
                    Value value = (Value) iterator.next();
                    if (getFrequencyOfValue(value) >= threshold || selector.getValues().contains(value)) {
                        values.add(value);
                        valueAbbreviations.add(tablePrefix + ((subHeadID < 10) ? "V0" + subHeadID : "V" + subHeadID + ""));
                        subHeadID++;
                    }
                }
            } else if (dmAttribute instanceof NominalAttribute) {
                NominalAttribute nom = (NominalAttribute) dmAttribute;
                List<Value> validAttributeValues = new ArrayList<Value>();
                if (!isAddAttribute) {
                    for (Iterator<? extends Value> valIter = nom.allValuesIterator(); valIter.hasNext(); ) {
                        validAttributeValues.add(valIter.next());
                    }
                } else {
                    validAttributeValues.addAll(AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getInitialValues(dmAttribute));
                }
                validAttributeValues.addAll(AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getUserDefinedValueGroups(dmAttribute));
                for (Iterator<Value> itera = validAttributeValues.iterator(); itera.hasNext(); ) {
                    Value value = itera.next();
                    if (getFrequencyOfValue(value) >= threshold || selector.getValues().contains(value)) {
                        values.add(value);
                        valueAbbreviations.add(tablePrefix + ((subHeadID < 10) ? "V0" + subHeadID : "V" + subHeadID + ""));
                        subHeadID++;
                    }
                }
            }
            SGNominalSelector clonedSelector = (SGNominalSelector) selector.clone();
            clonedSelector.removeAll(selector.getValues());
            if (!processedSelectors.contains(clonedSelector)) {
                processedSelectors.add(clonedSelector);
                AssociatedTableHead associatedTableHead = new AssociatedTableHead(clonedSelector, values, selectorAbbreviation, valueAbbreviations);
                assTableHeads.add(associatedTableHead);
            } else {
                AssociatedTableHead oldHead = assTableHeads.getAssociatedTableHeadToSelector(clonedSelector);
                List<Value> newValues = new ArrayList<Value>();
                List<String> newAbbreviations = new ArrayList<String>();
                newValues.addAll(oldHead.getValues());
                for (Value newValue : values) {
                    if (!oldHead.getValues().contains(newValue)) {
                        newValues.add(newValue);
                    }
                }
                for (int i = 1; i <= newValues.size(); i++) {
                    newAbbreviations.add(tablePrefix + ((i < 10) ? "V0" + i : "V" + i + ""));
                }
                assTableHeads.remove(oldHead);
                assTableHeads.add(new AssociatedTableHead(clonedSelector, newValues, selectorAbbreviation, newAbbreviations));
            }
        }
        isAddAttribute = false;
        return assTableHeads;
    }

    private int getFrequencyOfValue(Value value) {
        int frequency = 0;
        for (Iterator<DataRecord> iter = DMManager.getInstance().getOntology().getPopulation().dataset().iterator(); iter.hasNext(); ) {
            DataRecord record = iter.next();
            if (value.isValueContainedInInstance(record)) {
                frequency++;
            }
        }
        return frequency;
    }

    public void addRow() {
        if (definedPopulation == null) {
            return;
        }
        int row = interactionSGs.size();
        SG newSg = new SG(definedPopulation, getTarget(), new SGDescription());
        newSg.createStatistics(AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getSConstraints());
        interactionSGs.add(newSg);
        if (isAutoSort()) resort();
        fireTableRowsInserted(row, row);
    }

    public void copyAndAddRow(int selectedSG) {
        if (selectedSG == -1) return;
        SG sg = interactionSGs.get(selectedSG);
        if (sg != null) interactionSGs.add(sg.deepCopy()); else interactionSGs.add(null);
        int row = interactionSGs.size();
        if (isAutoSort()) resort();
        fireTableRowsInserted(row, row);
    }

    public void modifyInteractionSG(int row, int column) {
        if (column != -1) {
            if (column > 0 && column <= Util.getSortedValuesOfAssociations(associatedTableHeads).size()) {
                SG currentSG = interactionSGs.get(row);
                Ontology ontology = DMManager.getInstance().getOntology();
                if (currentSG == null) currentSG = new SG(ontology.getPopulation(), getTarget(), new SGDescription());
                SG newSG = updateByCreatingNewSG(currentSG, column - 1);
                interactionSGs.set(row, newSG);
                updateInteractionSGs();
                fireTableDataChanged();
            }
        }
    }

    private SG updateByCreatingNewSG(SG sg, int column) {
        Value val = Util.getSortedValuesOfAssociations(associatedTableHeads).get(column);
        return createSG(sg, val);
    }

    private SG createSG(SG sg, Value value) {
        SGDescription sgd = sg.getSGDescription();
        SGNominalSelector correspondingSelector = Util.getCorrespondingSGSelector(sgd, value.getAttribute());
        if (correspondingSelector != null) {
            SGNominalSelector newSelector = (SGNominalSelector) correspondingSelector.clone();
            sgd.remove(correspondingSelector);
            if (newSelector.getValues().contains(value)) {
                newSelector.removeValue(value);
                if (!isMaybeRedundant(newSelector)) sgd.add(newSelector);
            } else {
                newSelector.addValue(value);
                sgd.add(newSelector);
            }
        } else {
            SGNominalSelector clonedSelector = new DefaultSGSelector(value.getAttribute(), value);
            sg.getSGDescription().add(clonedSelector);
        }
        return sg;
    }

    public boolean isColumnRemovable(int col) {
        return col > 0 && col < (1 + Util.getSortedValuesOfAssociations(associatedTableHeads).size());
    }

    public boolean isSortable(int col) {
        return col >= (1 + Util.getSortedValuesOfAssociations(associatedTableHeads).size());
    }

    public boolean isAscending() {
        return ascending;
    }

    public int getSortColumn() {
        return sortColumn;
    }

    public void sortByColumn(int columnIndex, boolean newAscending) {
        this.ascending = newAscending;
        this.sortColumn = columnIndex;
        resort();
    }

    protected void resort() {
        class ValueToSG {

            double value;

            SG sg;

            public ValueToSG(double value, SG sg) {
                this.value = value;
                this.sg = sg;
            }

            public SG getSG() {
                return sg;
            }

            public double getValue() {
                return value;
            }
        }
        if (sortColumn != -1) {
            LinkedList<SG> interestingSGsPositions = new LinkedList<SG>();
            for (int i = 0; i < interactionSGs.size(); i++) {
                SG sg = interactionSGs.get(i);
                interestingSGsPositions.add(sg);
            }
            LinkedList sortedValueToSGs = new LinkedList();
            for (int i = 0; i < interactionSGs.size(); i++) {
                SG sg = interactionSGs.get(i);
                String value = (String) getValueAt(i, getSortColumn());
                double valueDouble;
                if (value.equals(UNDEFINED)) valueDouble = Double.NaN; else {
                    try {
                        NumberFormat nf = NumberFormat.getInstance();
                        valueDouble = nf.parse(value).doubleValue();
                    } catch (ParseException e) {
                        valueDouble = Double.NaN;
                    }
                }
                ValueToSG valueToItem = new ValueToSG(valueDouble, sg);
                sortedValueToSGs.add(valueToItem);
            }
            Comparator<ValueToSG> comparator = new Comparator<ValueToSG>() {

                public int compare(ValueToSG vTi_1, ValueToSG vTi_2) {
                    double d1 = vTi_1.getValue();
                    double d2 = vTi_2.getValue();
                    boolean d1_NaN = (new Double(d1)).equals(Double.NaN);
                    boolean d2_NaN = (new Double(d2)).equals(Double.NaN);
                    if (d1_NaN && d2_NaN) return 0; else if (d1_NaN) return -1; else if (d2_NaN) return 1;
                    if (d1 < d2) return -1; else if (d1 == d2) return 0; else return 1;
                }
            };
            if (!ascending) comparator = Collections.reverseOrder(comparator);
            Collections.sort(sortedValueToSGs, comparator);
            LinkedList<SG> sortedInteractionSGs = new LinkedList<SG>();
            for (Iterator iter = sortedValueToSGs.iterator(); iter.hasNext(); ) {
                ValueToSG valueToSg = (ValueToSG) iter.next();
                sortedInteractionSGs.add(valueToSg.getSG());
            }
            interactionSGs = sortedInteractionSGs;
            LinkedList<Integer> newSortedIndices = new LinkedList<Integer>();
            for (int i = 0; i < interestingSGsPositions.size(); i++) {
                SG sg = interestingSGsPositions.get(i);
                newSortedIndices.add(interactionSGs.indexOf(sg));
            }
            fireTableRowSelectionChanged(newSortedIndices);
        }
    }

    public AssociatedTableHeads getAssociatedTableHeads() {
        return associatedTableHeads;
    }

    public SGTarget getTarget() {
        return target;
    }

    public void removeRow(int index) {
        interactionSGs.remove(index);
        fireTableRowsDeleted(index, index);
        updateHeadersAfterRowRemoval();
    }

    public void removeRows(int[] rows) {
        int deletedRows = 0;
        for (int i : rows) {
            interactionSGs.remove(i - deletedRows++);
        }
        fireTableRowsDeleted(rows[0], rows[rows.length - 1]);
        updateHeadersAfterRowRemoval();
    }

    public boolean isShowAbbreviationHeads() {
        return showAbbreviationHeads;
    }

    public void setShowAbbreviationHeads(boolean abbreviationHeads) {
        this.showAbbreviationHeads = abbreviationHeads;
        fireTableStructureChanged(TuningTableModelEvent.EXTENDED_TYPE_COLUMN_UPDATED);
    }

    public void removeDMAttributeAtSelectedColumn(int columnIndex) {
        int associatedTableHeadsValueSize = Util.getSortedValuesOfAssociations(associatedTableHeads).size();
        int shiftColumIndes = columnIndex - 1;
        if (shiftColumIndes >= 0 && shiftColumIndes < associatedTableHeadsValueSize) {
            int tableHeadIndex = Util.getAssociatedTableHeadIndex(associatedTableHeads, shiftColumIndes);
            AssociatedTableHead assTableHead = associatedTableHeads.get(tableHeadIndex);
            associatedTableHeads.remove(tableHeadIndex);
            SGNominalSelector selector = assTableHead.getSelector();
            for (int i = 0; i < interactionSGs.size(); i++) {
                SG sg = interactionSGs.get(i);
                if (sg != null) {
                    SGNominalSelector selectorToRemove = null;
                    for (Iterator iterator = sg.getSGDescription().iterator(); iterator.hasNext(); ) {
                        SGNominalSelector sel = (SGNominalSelector) iterator.next();
                        if (sel.getAttribute().equals(selector.getAttribute())) {
                            selectorToRemove = sel;
                        }
                    }
                    if (selectorToRemove != null) sg.getSGDescription().remove(selectorToRemove);
                }
            }
            if (sortColumn != -1) sortColumn -= assTableHead.getValues().size();
            LinkedList<SGNominalSelector> newSGSelectorList = getSelectorListFromAssociatedHeaders();
            associatedTableHeads = createAssociatedTableHeads(newSGSelectorList);
            updateInteractionSGs();
            fireTableStructureChanged(TuningTableModelEvent.EXTENDED_TYPE_COLUMN_DELETED);
        } else try {
            throw new Exception("Attribute could not be removed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAutoSort() {
        return autoSort;
    }

    public void setAutoSort(boolean autoSort) {
        this.autoSort = autoSort;
        if (autoSort == true) resort();
    }

    public void updateInteractionSGs() {
        for (Iterator iter = interactionSGs.iterator(); iter.hasNext(); ) {
            SG sg = (SG) iter.next();
            if (sg != null) {
                sg.createStatistics(sg.getStatistics().getSConstraints());
                updateSGQuality(sg);
            }
        }
        if (autoSort == true) resort();
        fireTableDataChanged();
    }

    private void updateSGQuality(SG sg) {
        if (!(sg == null)) sg.updateQuality(AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().createQualityFunctionForTarget(sg.getTarget()));
    }

    public List<SG> getInteractionSGs() {
        return interactionSGs;
    }

    public boolean isMaybeRedundant(SGNominalSelector selector) {
        if (selector.getAttribute().isNumeric()) {
            if (selector.getValues().size() != 0) return false;
            return true;
        }
        return selector.isMaybeRedundant();
    }

    public void updateSGTarget(SGTarget target) {
        this.target = target;
        for (Iterator iter = interactionSGs.iterator(); iter.hasNext(); ) {
            SG sg = (SG) iter.next();
            if (sg != null) {
                sg.setTarget(target);
                SConstraints constraints = sg.getStatistics().getSConstraints();
                sg.createStatistics(constraints);
            }
        }
        updateInteractionSGs();
        updateInteractionSGSettings();
        fireTableDataChanged();
    }

    public void addSingleFactors() {
        AssociatedTableHeads associatedTableHeads = getAssociatedTableHeads();
        Ontology ontology = DMManager.getInstance().getOntology();
        int oldSize = interactionSGs.size();
        for (Iterator iter = associatedTableHeads.iterator(); iter.hasNext(); ) {
            AssociatedTableHead tableHead = (AssociatedTableHead) iter.next();
            SGNominalSelector selector = tableHead.getSelector();
            for (Iterator iterator = tableHead.getValues().iterator(); iterator.hasNext(); ) {
                Value value = (Value) iterator.next();
                SG sg = new SG(ontology.getPopulation(), getTarget(), new SGDescription());
                Attribute attrib = selector.getAttribute();
                DefaultSGSelector ds = new DefaultSGSelector(attrib, value);
                sg.getSGDescription().add(ds);
                sg.createStatistics(AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getSConstraints());
                interactionSGs.add(sg);
            }
        }
        setAutoSort(false);
        updateInteractionSGs();
        fireTableRowsInserted(oldSize, interactionSGs.size());
    }

    public void removeAllColumns() {
        for (int i = 0; i < getColumnCount(); i++) {
            if (isColumnRemovable(i)) {
                removeDMAttributeAtSelectedColumn(i);
            }
        }
        fireTableColumnDeleted();
    }

    public void clearTable() {
        int size = interactionSGs.size();
        interactionSGs = new LinkedList<SG>();
        fireTableRowsDeleted(0, size);
    }

    public void updateInteractionSGSettings() {
        computeStatComponents();
        sortColumn = -1;
        fireTableColumnDeleted();
    }

    public void retrievePopulation() {
        definedPopulation = DMManager.getInstance().getOntology().getPopulation();
    }

    public void updateHeaders() {
        recreateHeaders();
        fireTableStructureChanged(TuningTableModelEvent.EXTENDED_TYPE_COLUMN_UPDATED);
    }

    public void updateHeadersAfterRowRemoval() {
        recreateHeaders();
        fireTableStructureChanged(TuningTableModelEvent.EXTENDED_TYPE_COLUMN_DELETED);
    }

    private void recreateHeaders() {
        LinkedList<SGNominalSelector> newSGSelectorList = getSelectorListFromAssociatedHeaders();
        associatedTableHeads = createAssociatedTableHeads(newSGSelectorList);
    }

    private LinkedList<SGNominalSelector> getSelectorListFromAssociatedHeaders() {
        LinkedList<SGNominalSelector> newSGSelectorList = new LinkedList<SGNominalSelector>();
        for (Iterator iter = associatedTableHeads.iterator(); iter.hasNext(); ) {
            AssociatedTableHead assTableHead2 = (AssociatedTableHead) iter.next();
            SGNominalSelector sel = assTableHead2.getSelector();
            if (sel.getAttribute().isNumeric()) sel.addAll(assTableHead2.getValues());
            newSGSelectorList.add(assTableHead2.getSelector());
        }
        return newSGSelectorList;
    }

    public void clearTargetComboBoxTableModelListeners() {
        EventListener[] listeners = listenerList.getListeners(TableModelListener.class);
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof TargetComboBox) {
                removeTableModelListener((TableModelListener) listeners[i]);
            }
        }
    }
}
