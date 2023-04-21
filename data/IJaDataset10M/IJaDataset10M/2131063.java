package org.slizardo.beobachter.gui.dialogs.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.slizardo.beobachter.beans.Rule;
import org.slizardo.beobachter.resources.languages.Translator;

public class RulesTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -3824360112995729445L;

    private String columns[] = new String[] { Translator.t("Pattern"), Translator.t("Colors"), Translator.t("Ignore_case") };

    private List<Rule> rules;

    public RulesTableModel() {
        rules = new ArrayList<Rule>();
    }

    public String getColumnName(int index) {
        return columns[index];
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Object getValueAt(int row, int column) {
        Rule rule = (Rule) rules.get(row);
        switch(column) {
            case 0:
                return rule.getPattern();
            case 1:
                return new Color[] { rule.getBackgroundColor(), rule.getForegroundColor() };
            case 2:
                return new Boolean(rule.isIgnoreCase());
            default:
                return null;
        }
    }

    public int getRowCount() {
        return rules.size();
    }

    public void addRule(Object o) {
        if (o instanceof Rule) {
            Rule rule = (Rule) o;
            rules.add(rule);
            fireTableRowsInserted(rules.size() - 1, rules.size() - 1);
        }
    }

    public void removeRule(int index) {
        rules.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void clear() {
        rules.clear();
        fireTableDataChanged();
    }

    public List<Rule> getRules() {
        return rules;
    }
}
