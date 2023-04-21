package tr.view.actions.screens.filters;

import ca.odell.glazedlists.matchers.Matcher;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import org.openide.util.NbBundle;
import tr.model.action.Action;
import tr.swing.date.combo.DateItem;
import tr.util.DateUtils;
import tr.view.filters.FilterCombo;
import tr.view.filters.FilterComboDate;

/**
 * MatcherEditor the matches actions for a created from date.
 *
 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public class FilterDateCreatedFrom extends FilterDate implements PropertyChangeListener {

    public static final byte INDEX = 9;

    private static final DateItem[] dateItems = new DateItem[] { DateItem.DATE_CHOOSER, DateItem.TODAY, DateItem.YESTERDAY, DateItem.WEEKS_AGO_1, DateItem.WEEKS_AGO_2, DateItem.WEEKS_AGO_3, DateItem.WEEKS_AGO_4 };

    /** Constructs a new instance. */
    public FilterDateCreatedFrom() {
        initialise();
    }

    protected void initialise() {
        combo = new FilterComboDate(dateItems, false);
        combo.addValueChangeListener(this);
    }

    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-created-from");
    }

    public byte getIndex() {
        return INDEX;
    }

    public void propertyChange(PropertyChangeEvent e) {
        DateItem item = (DateItem) combo.getSelectedItem();
        if (item == null) {
            fireMatchAll();
        } else {
            fireChanged(new FromDateMatcher(combo.getDate(item), excludeNulls));
        }
    }

    private static class FromDateMatcher implements Matcher<Action> {

        private final Date matchDate;

        private final boolean excludeNulls;

        public FromDateMatcher(Date date, boolean excludeNulls) {
            this.matchDate = DateUtils.getStart(date);
            this.excludeNulls = excludeNulls;
        }

        public boolean matches(Action action) {
            if (matchDate == null) {
                return true;
            }
            Date date = action.getCreated();
            if (date == null) {
                return !excludeNulls;
            }
            return !date.before(matchDate);
        }
    }
}
