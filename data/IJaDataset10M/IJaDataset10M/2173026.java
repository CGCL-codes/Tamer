package org.azrul.epice.jpa.daoimpl.queryimpl;

import java.util.GregorianCalendar;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.azrul.epice.domain.Item;

/**
 *
 * @author Azrul Hasni MADISA
 */
public class JPASentItemsNearDeadlineFilter implements JPAItemsFilter {

    private String type = null;

    public String getType() {
        return type;
    }

    public JPASentItemsNearDeadlineFilter() {
        type = "SENT_ITEMS_NEAR_DEADLINE";
    }

    public boolean filter(Item item, String user) {
        GregorianCalendar today = ((GregorianCalendar) GregorianCalendar.getInstance());
        GregorianCalendar deadline = ((GregorianCalendar) GregorianCalendar.getInstance());
        GregorianCalendar dateInXDays = ((GregorianCalendar) GregorianCalendar.getInstance());
        dateInXDays.add(GregorianCalendar.DAY_OF_YEAR, 2);
        if (item.getDeadLine() == null) {
            return false;
        }
        deadline.setTime(item.getDeadLine());
        return item.getSender().equals(user) && dateInXDays.after(deadline) && today.before(deadline) && ((Item.Status.UNCONFIRMED == item.getStatus()) || (Item.Status.NEGOTIATED == item.getStatus()) || (Item.Status.ACCEPTED == item.getStatus()) || (Item.Status.NEED_REDO == item.getStatus()) || (Item.Status.DELEGATED == item.getStatus()) || (Item.Status.NEED_REDO_DELEGATED == item.getStatus()));
    }

    public JPACriteria filter(String user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Predicate filter(String user, CriteriaBuilder cb, Root<Item> ritem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
