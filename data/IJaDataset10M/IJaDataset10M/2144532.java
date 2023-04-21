package tr.view.actions.screens.filters;

import java.util.logging.Logger;
import tr.swing.date.combo.DateItem;
import tr.swing.date.combo.DateType;
import tr.util.Utils;
import tr.view.filters.FilterCombo;
import tr.view.filters.FilterComboDate;

/**
 * Abstract base class for date matcher editors.
 *
 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public abstract class FilterDate extends ActionsFilter {

    private static final Logger LOG = Logger.getLogger("tr.view.actions");

    protected FilterComboDate combo;

    /** Constructs a new instance. */
    public FilterDate() {
        super();
    }

    protected boolean canExcludeNulls() {
        return true;
    }

    /** Gets the component. */
    public final FilterCombo getFilterCombo() {
        return combo;
    }

    public String[] getSerialValues() {
        if (combo == null) return null;
        String selected = "";
        DateItem item = (DateItem) combo.getSelectedItem();
        if (item != null && item.type != DateType.NONE) {
            selected = String.valueOf(item.value);
        }
        return new String[] { selected, Boolean.toString(excludeNulls) };
    }

    public void setSerialValues(String[] values) {
        if (combo == null) return;
        combo.stopChangeEvents();
        if (values == null || values.length < 1) {
            combo.setSelectedItem(null);
        } else {
            try {
                long value = Long.parseLong(values[0]);
                if (value < 1000) {
                    combo.setSelectedItem(combo.getDateItem(DateType.DAYS, value));
                } else {
                    combo.setSelectedItem(combo.getDateItem(DateType.MS, value));
                }
            } catch (Exception ex) {
                combo.setSelectedItem(null);
            }
            if (values.length > 1) {
                try {
                    excludeNulls = Boolean.parseBoolean(values[1]);
                } catch (Exception ex) {
                }
            }
        }
        combo.startChangeEvents();
    }

    public boolean equals(Object object) {
        if (!(object instanceof FilterDate)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        DateItem thisDateItem = (DateItem) combo.getSelectedItem();
        DateItem thatDateItem = (DateItem) ((FilterDate) object).combo.getSelectedItem();
        return Utils.equal(thisDateItem, thatDateItem);
    }
}
