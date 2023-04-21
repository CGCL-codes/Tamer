package net.etherstorm.jopenrpg.swing.nodehandlers.newd20tool.d20tools;

import javax.swing.event.TableModelEvent;

public class HitPointTableModel extends BasicTableModel {

    public static final String PROPERTY_FEATDATA = "hitPointData";

    protected HitPointData _hitPointData;

    protected String[] COLUMN_NAMES = { "Type", "Amount" };

    protected String[] ROW_LABELS = { "Current hp", "Max hp", "Calculated Max", "Damage Taken", "Subdual Damage Taken", "Temporary Points" };

    public HitPointTableModel() {
    }

    public void dataChanged(Object o) {
        fireTableModelEvent(new TableModelEvent(this, 0, getRowCount() - 1));
    }

    public Class getColumnClass(int col) {
        switch(col) {
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            default:
                return String.class;
        }
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public String getColumnName(int col) {
        try {
            return COLUMN_NAMES[col];
        } catch (Exception ex) {
            return "err";
        }
    }

    public HitPointData getHitPointData() {
        if (_hitPointData == null) setHitPointData(new HitPointData());
        return _hitPointData;
    }

    public int getRowCount() {
        return ROW_LABELS.length;
    }

    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                return ROW_LABELS[row];
            case 1:
                switch(row) {
                    case 0:
                        return new Integer(getHitPointData().getCurrent());
                    case 1:
                        return new Integer(getHitPointData().getMax());
                    case 2:
                        return new Integer(getHitPointData().getHandler().getClassesData().getTotalHitPoints());
                    case 3:
                        return new Integer(getHitPointData().getDamage());
                    case 4:
                        return new Integer(getHitPointData().getSubdual());
                    case 5:
                        return new Integer(getHitPointData().getBonus());
                    default:
                        return null;
                }
            default:
                break;
        }
        return null;
    }

    public boolean isCellEditable(int row, int col) {
        return (col == 1 && row != 2);
    }

    public void setHitPointData(HitPointData val) {
        try {
            if (val.equals(_hitPointData)) return;
            if (_hitPointData != null) {
                unRegisterWithListeners(_hitPointData.getHandler().getAbilityData().getAbilities());
                _hitPointData.removePropertyChangeListener(this);
            }
        } catch (Exception ex) {
            return;
        }
        HitPointData oldval = _hitPointData;
        _hitPointData = val;
        registerWithListeners(_hitPointData.getHandler().getAbilityData().getAbilities());
        _hitPointData.addPropertyChangeListener(this);
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0) return;
        switch(row) {
            case 0:
                getHitPointData().setCurrent(((Integer) value).intValue());
                break;
            case 1:
                getHitPointData().setMax(((Integer) value).intValue());
                break;
            case 3:
                getHitPointData().setDamage(((Integer) value).intValue());
                break;
            case 4:
                getHitPointData().setSubdual(((Integer) value).intValue());
                break;
            case 5:
                getHitPointData().setBonus(((Integer) value).intValue());
                break;
            default:
                break;
        }
    }
}
