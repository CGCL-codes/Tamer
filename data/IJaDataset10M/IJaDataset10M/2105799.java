package org.azrul.epice.db4o.daoimpl.queryimpl;

import org.azrul.epice.dao.query.ItemsFilter;
import java.util.GregorianCalendar;
import org.azrul.epice.domain.Item;
import org.azrul.epice.domain.Person;

/**
 *
 * @author Azrul Hasni MADISA
 */
public class DB4OSentItemsNearDeadlineFilter implements ItemsFilter {

    private String type = null;

    @Override
    public String getType() {
        return type;
    }

    public DB4OSentItemsNearDeadlineFilter() {
        type = "SENT_ITEMS_NEAR_DEADLINE";
    }

    public boolean filter(Item item, Person user) {
        GregorianCalendar today = ((GregorianCalendar) GregorianCalendar.getInstance());
        GregorianCalendar deadline = ((GregorianCalendar) GregorianCalendar.getInstance());
        GregorianCalendar dateInXDays = ((GregorianCalendar) GregorianCalendar.getInstance());
        dateInXDays.add(GregorianCalendar.DAY_OF_YEAR, 2);
        if (item.getDeadLine() == null) {
            return false;
        }
        deadline.setTime(item.getDeadLine());
        return item.getFromUser().equals(user) && dateInXDays.after(deadline) && today.before(deadline) && (("SENT-UNCONFIRMED").equals(item.getStatus()) || ("SENT-NEGOTIATED").equals(item.getStatus()) || ("SENT-ACCEPTED").equals(item.getStatus()) || ("NEED-REDO").equals(item.getStatus()) || ("DELEGATED").equals(item.getStatus()) || ("NEED-REDO DELEGATED").equals(item.getStatus()));
    }
}
