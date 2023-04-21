package orcajo.azada.core;

import orcajo.azada.core.olap.Setting;
import orcajo.azada.core.preferences.PreferenceConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.olap4j.AllocationPolicy;
import org.olap4j.query.QueryDimension.HierarchizeMode;

public class PropertyChangeListener implements IPropertyChangeListener {

    private Setting setting;

    public PropertyChangeListener() {
        super();
        setting = Setting.getInstance();
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getProperty().equals(PreferenceConstants.P_HIERARCHIZE_MODE)) {
            if (event.getNewValue() != null) {
                String sValue = (String) event.getNewValue();
                HierarchizeMode hm = null;
                HierarchizeMode[] modes = HierarchizeMode.values();
                for (int i = 0; i < modes.length; i++) {
                    if (sValue.equals(modes[i].name())) {
                        hm = modes[i];
                    }
                }
                setting.setHierarchizeMode(hm);
            }
        }
        if (event.getProperty().equals(PreferenceConstants.P_RESTORE_QUERY)) {
            if (event.getNewValue() != null) {
                setting.setRestoreLastQuery(getValue(event.getNewValue()));
            }
        }
        if (event.getProperty().equals(PreferenceConstants.P_BREAK_LINES)) {
            if (event.getNewValue() != null) {
                setting.setBreakLines(getValue(event.getNewValue()));
            }
        }
        if (event.getProperty().equals(PreferenceConstants.P_REMOVE_PARENTHESIS)) {
            if (event.getNewValue() != null) {
                setting.setRemoveParenthesis(getValue(event.getNewValue()));
            }
        }
        if (event.getProperty().equals(PreferenceConstants.P_ALLOCATION_POLICY)) {
            if (event.getNewValue() != null) {
                String sValue = (String) event.getNewValue();
                AllocationPolicy ap = AllocationPolicy.EQUAL_INCREMENT;
                AllocationPolicy[] policies = AllocationPolicy.values();
                for (int i = 0; i < policies.length; i++) {
                    if (sValue.equals(policies[i].name())) {
                        ap = policies[i];
                    }
                }
                setting.setAllocationPolicy(ap);
            }
        }
    }

    boolean getValue(Object value) {
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }
}
