package com.qasystems.qstudio.java.gui.ruleconfiguration;

import com.qasystems.qstudio.java.gui.Rule;
import com.qasystems.qstudio.java.gui.RuleConfiguration;
import com.qasystems.qstudio.java.gui.RuleSet;
import com.qasystems.swing.table.Control;
import com.qasystems.swing.table.Model;
import com.qasystems.swing.table.View;
import java.util.Iterator;
import java.util.Vector;

/**
 * Control showing the rule configurations
 */
public class RuleConfigurationControl extends Control {

    private boolean isTableEditable = false;

    private RuleSet ruleSet = null;

    private Vector filters = new Vector();

    /**
   * Default constructor
   *
   * @param model the model
   * @param view the view
   */
    public RuleConfigurationControl(Model model, View view) {
        super(model, view);
        getTableModel().setTableEditable(false);
    }

    /**
   * Default constructor
   *
   * @param model the model
   * @param view the view
   * @param editable true when it is possible to change
   *                 the 'selected' attribute
   *                 of a rule configuration, false otherwise
   */
    public RuleConfigurationControl(Model model, View view, boolean editable) {
        this(model, view);
        isTableEditable = editable;
        getTableModel().setTableEditable(isTableEditable);
    }

    /**
   * Add a filter for the data.
   *
   * @param filter the filter
   */
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    /**
   * Set the ruleset, this is necessary if new rules are added to
   * the ruleset.
   *
   * @param r the ruleset
   */
    public synchronized void setRuleSet(RuleSet r) {
        ruleSet = r;
    }

    /**
   * @return the ruleset
   */
    public RuleSet getRuleSet() {
        return (ruleSet);
    }

    /**
   * Filter the date(rules) before making the data available.
   *
   * @param rules the rules.
   * @return the rules which conform all filters.
   */
    private Rule[] filterRules(Rule[] rules) {
        Iterator f = filters.iterator();
        while (f.hasNext()) {
            final Filter filter = (Filter) f.next();
            for (int i = 0; i < rules.length; i++) {
                if ((rules[i] != null) && (filter != null) && !filter.select(rules[i])) {
                    rules[i] = null;
                }
            }
        }
        final Vector filteredRules = new Vector();
        for (int i = 0; i < rules.length; i++) {
            if (rules[i] != null) {
                filteredRules.add(rules[i]);
            }
        }
        return ((Rule[]) filteredRules.toArray(new Rule[filteredRules.size()]));
    }

    /**
   * apply changes made in the JTable on the RuleConfiguration.
   * - in this case the 'column' variable in not needed
   *   since whe know that there is only one column that
   *   can be changed.
   *
   * @param row the row
   * @param col the column
   * @param value the changed value
   */
    protected void processChange(int row, int col, Object value) {
        if (getTableModel().isObjectAvailable()) {
            final RuleConfiguration ruleConfig = (RuleConfiguration) getTableModel().getObject(row);
            final boolean selected = ((value != null) && value instanceof Boolean) ? ((Boolean) value).booleanValue() : false;
            if (ruleConfig != null) {
                ruleConfig.setSelected(selected);
                ruleConfig.signalChange();
            }
        }
    }

    /**
   * Load the rules into the model, if directly called from the
   * outside this
   *
   * @param rules the rules
   */
    public void loadData(Rule[] rules) {
        if (rules != null) {
            final Rule[] filteredRules = filterRules(rules);
            setContainsData(true);
            getTableModel().loadData(filteredRules);
            getTableSorter().reallocateIndexes();
            sortColumn(getTableSorter().getCurrentSortedColumn());
            setRowSelection(0);
        } else {
            reset();
        }
    }

    /**
   * remove old data and load new rules into the model
   */
    public void reloadData() {
        unloadData();
        loadData(ruleSet.getRules());
    }

    /**
   * Gets the value of the cell at the given row in the ID column.
   *
   * @param row the zero-based index of the row
   * @return the Check ID
   */
    public int getIDValue(int row) {
        return (Integer.parseInt(getTableSorter().getValueAt(row, 1).toString()));
    }

    /**
   * Returns <i>class_name</i>@<i>object_hashcode</i>.
   *
   * @return the string
   */
    public String toString() {
        return (getClass().getName() + "@" + Integer.toHexString(hashCode()));
    }

    /**
   * NEEDED ???
   *
   * @param ruleId a value of type 'int'
   * @return a value of type 'Rule'
   */
    public Rule getRule(int ruleId) {
        Rule rule = null;
        final Model model = getTableModel();
        if (model instanceof RuleConfigurationModel) {
            rule = ((RuleConfigurationModel) model).getRule(ruleId);
        }
        return (rule);
    }

    /**
   * retrieve the rule configuration from sorted tablemodel
   *
   * @param row a value of type 'int'
   * @return a value of type 'RuleConfiguration'
   */
    public RuleConfiguration getRuleConfiguration(int row) {
        RuleConfiguration config = null;
        final int sortedRowNumber = getTableSorter().getIndexedRowNumber(row);
        if (sortedRowNumber != -1) {
            config = (RuleConfiguration) getTableModel().getObject(sortedRowNumber);
        }
        return (config);
    }
}
