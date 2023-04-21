package tr.view.actions.screens.filters;

import java.beans.PropertyChangeListener;
import java.util.Vector;
import java.util.logging.Logger;
import tr.model.criteria.Value;
import tr.util.Utils;
import tr.view.filters.ChoiceAll;
import tr.view.filters.ValueAll;
import tr.view.filters.ValueMultiple;

/**
 * MatcherEditor the matches the energy criterion.
 *
 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public abstract class FilterCriterion extends FilterChoice implements PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger("tr.view.actions");

    /** Constructs a new instance. */
    public FilterCriterion() {
    }

    protected boolean canExcludeNulls() {
        return true;
    }

    public String[] getSerialValues() {
        if (combo == null) {
            return null;
        }
        Value value = (Value) combo.getSelectedItem();
        if (value == null) {
            return null;
        }
        if (value instanceof ValueAll) {
            return new String[] { ChoiceAll.ID };
        }
        if (value instanceof ValueMultiple) {
            ValueMultiple m = (ValueMultiple) value;
            if (m.getChosen() == null || m.getChosen().size() == 0) {
                return null;
            }
            String[] values = new String[m.getChosen().size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = String.valueOf(((Value) m.getChosen().get(i)).getID());
            }
            return values;
        }
        return new String[] { String.valueOf(value.getID()) };
    }

    public void setSerialValues(String[] values) {
        if (combo == null || combo.getItemCount() == 0) return;
        combo.stopChangeEvents();
        if (values == null || values.length == 0) {
            combo.setSelectedItem(null);
        } else if (values.length == 1) {
            if (values[0].equals(ChoiceAll.ID)) {
                combo.setSelectedIndex(0);
            } else {
                combo.setSelectedItem(getValue(values[0]));
            }
        } else if (values.length > 1) {
            combo.setSelectedIndex(1);
            ValueMultiple m = (ValueMultiple) combo.getItemAt(1);
            m.setChosen(new Vector<Value>());
            for (String id : values) {
                m.getChosen().add(getValue(id));
            }
        }
        combo.startChangeEvents();
    }

    protected abstract Value getValue(String id);

    public boolean equals(Object object) {
        if (!(object instanceof FilterCriterion)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Value thisValue = (Value) combo.getSelectedItem();
        Value thatValue = (Value) ((FilterCriterion) object).combo.getSelectedItem();
        return Utils.equal(thisValue, thatValue);
    }
}
