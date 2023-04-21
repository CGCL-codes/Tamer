package org.javadm.library.association.apriory;

import java.util.*;
import org.javadm.library.association.depthproject.*;
import org.javadm.library.data.*;
import org.javadm.library.data.binary.*;

public class Group {

    private ArrayList<String> m_columns = new ArrayList<String>();

    private int m_appearances = 0;

    private BitVector m_columnsForCheck;

    private String m_text;

    private String m_prefixText;

    public Group(String column, ArrayList<ColumnInformation> informations) {
        m_columns.add(column);
        initialize(informations);
    }

    public Group(ArrayList<String> columns, ArrayList<ColumnInformation> informations) {
        m_columns.addAll(columns);
        initialize(informations);
    }

    public Group(Group group1, Group group2, ArrayList<ColumnInformation> informations) {
        addGroup(group1);
        addGroup(group2);
        initialize(informations);
    }

    private void initialize(ArrayList<ColumnInformation> informations) {
        Collections.sort(m_columns);
        m_text = m_columns.toString();
        String last = m_columns.remove(m_columns.size() - 1);
        m_prefixText = m_columns.toString();
        m_columns.add(last);
        m_columnsForCheck = new BitVector(informations.size());
        int currentBit = 0;
        for (ColumnInformation information : informations) {
            if (m_columns.contains(information.getName())) {
                m_columnsForCheck.setBit(currentBit, true);
            }
            currentBit++;
        }
    }

    private void addGroup(Group group) {
        for (String column : group.m_columns) {
            if (!m_columns.contains(column)) {
                m_columns.add(column);
            }
        }
    }

    public void checkAppear(BinaryRow row) {
        if (isFound(row)) {
            m_appearances++;
        }
    }

    public boolean isFound(BinaryRow row) {
        return row.check(m_columnsForCheck);
    }

    public int getAppearances() {
        return m_appearances;
    }

    @Override
    public String toString() {
        return m_text + " appears " + m_appearances;
    }

    public String getKey() {
        return m_text;
    }

    @Override
    public boolean equals(Object object) {
        return equals((Group) object);
    }

    public boolean equals(Group group) {
        return getKey().equals(group.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    public boolean isPrefixMatch(Group group) {
        return m_prefixText.equals(group.m_prefixText);
    }

    public ArrayList<Rule> getAllRules(BinaryTable table, Class<? extends Rule> ruleClass) throws Exception {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        for (int firstSize = 1; firstSize < m_columns.size(); firstSize++) {
            int secondSize = m_columns.size() - firstSize;
            recursiveGetGroup(table, rules, firstSize, secondSize, 0, new ArrayList<String>(), new ArrayList<String>(), ruleClass);
        }
        return rules;
    }

    private void recursiveGetGroup(BinaryTable table, ArrayList<Rule> rules, int sizeToAddToFirstGroup, int sizeToAddToSecondGroup, int currentIndex, ArrayList<String> firstGroup, ArrayList<String> secondGroup, Class<? extends Rule> ruleClass) throws Exception {
        if ((sizeToAddToFirstGroup == 0) && (sizeToAddToSecondGroup == 0)) {
            Rule rule = ruleClass.newInstance();
            rule.initialize(firstGroup, secondGroup, table);
            if (rule.isInteresting()) {
                rules.add(rule);
            }
            return;
        }
        if (currentIndex >= m_columns.size()) {
            return;
        }
        String currentItem = m_columns.get(currentIndex);
        int nextIndex = currentIndex + 1;
        ArrayList<String> newFirstGroup = new ArrayList<String>();
        newFirstGroup.addAll(firstGroup);
        newFirstGroup.add(currentItem);
        recursiveGetGroup(table, rules, sizeToAddToFirstGroup - 1, sizeToAddToSecondGroup, nextIndex, newFirstGroup, secondGroup, ruleClass);
        ArrayList<String> newSecondGroup = new ArrayList<String>();
        newSecondGroup.addAll(secondGroup);
        newSecondGroup.add(currentItem);
        recursiveGetGroup(table, rules, sizeToAddToFirstGroup, sizeToAddToSecondGroup - 1, nextIndex, firstGroup, newSecondGroup, ruleClass);
    }

    public int getSize() {
        return m_columns.size();
    }

    public ArrayList<String> getColumns() {
        return m_columns;
    }

    public boolean containItemPart(String part) {
        for (String item : m_columns) {
            if (item.indexOf(part) != -1) {
                return true;
            }
        }
        return false;
    }
}
