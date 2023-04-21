package org.robcash.apps.messagepopup.view.components;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.robcash.apps.messagepopup.vo.Schedule;
import org.robcash.apps.messagepopup.vo.Subscription;

/**
 *
 * @author Rob
 */
public class SubscriptionTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    @Override
    protected void setValue(Object value) {
        if (value instanceof Subscription) {
            Subscription subscription = (Subscription) value;
            setText(subscription.getName());
            setToolTipText(subscription.getMessageStore().getLocation().toString());
        } else if (value instanceof Schedule) {
            Schedule schedule = (Schedule) value;
            setText(schedule.getName());
            setToolTipText(schedule.getDescription());
        } else {
            setText(value.toString());
        }
    }
}
