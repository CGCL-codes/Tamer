package net.kano.joustsim.oscar.oscar.service.ssi;

import net.kano.joscar.snaccmd.ssi.SsiItem;
import net.kano.joscar.ssiitem.BuddyItem;
import net.kano.joscar.ssiitem.SsiItemObjectWithId;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class SsiTools {

    private SsiTools() {
    }

    public static List<SsiItem> getBuddiesToDelete(List<Buddy> ingroup) {
        List<SsiItem> items = new ArrayList<SsiItem>();
        for (Buddy buddy : ingroup) {
            if (!(buddy instanceof SimpleBuddy)) {
                throw new IllegalArgumentException("can't delete buddy " + buddy + " : wrong type");
            }
            SimpleBuddy simpleBuddy = (SimpleBuddy) buddy;
            BuddyItem item = simpleBuddy.getItem();
            SsiItem ssiItem = item.toSsiItem();
            items.add(ssiItem);
        }
        return items;
    }

    public static List<Integer> getIdsForItems(List<SsiItem> items) {
        List<Integer> ids = new ArrayList<Integer>();
        for (SsiItem ssiItem : items) ids.add(ssiItem.getId());
        return ids;
    }

    public static <E extends SsiItemObjectWithId> void removeItemsWithId(Collection<E> items, int id) {
        for (Iterator<E> it = items.iterator(); it.hasNext(); ) {
            SsiItemObjectWithId otherItem = it.next();
            if (otherItem.getId() == id) it.remove();
        }
    }

    public static boolean isOnlyBuddies(List<SsiItem> items) {
        for (SsiItem item : items) {
            if (item.getItemType() != SsiItem.TYPE_BUDDY) return false;
        }
        return true;
    }
}
