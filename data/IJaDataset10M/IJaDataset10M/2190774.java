package org.azrul.epice.db4o.daoimpl.queryimpl;

import com.db4o.query.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.azrul.epice.domain.Item;

/**
 *
 * @author Azrul
 */
public class DB4OFilterGroup implements DB4OItemsFilter {

    private List<DB4OItemsFilter> filters = new ArrayList<DB4OItemsFilter>();

    private boolean useOR = false;

    public DB4OItemPredicate filter(final Item item, final String user) {
        Predicate predicate = new Predicate() {

            public boolean match(Object et) {
                return booleanFilter(item, user);
            }
        };
        DB4OItemPredicate itemPredicate = new DB4OItemPredicate();
        itemPredicate.set(predicate);
        return itemPredicate;
    }

    public boolean booleanFilter(Item item, String currentUser) {
        if (filters == null) {
            return true;
        }
        if (filters.isEmpty()) {
            return true;
        }
        boolean res = true;
        boolean firstFlag = true;
        for (DB4OItemsFilter _filter : filters) {
            DB4OItemsFilter filter = (DB4OItemsFilter) _filter;
            if (firstFlag == true) {
                res = filter.booleanFilter(item, currentUser);
                firstFlag = false;
            } else {
                if (useOR) {
                    res = res || filter.booleanFilter(item, currentUser);
                } else {
                    res = res && filter.booleanFilter(item, currentUser);
                }
            }
        }
        return res;
    }

    public String getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the filters
     */
    public List<DB4OItemsFilter> getFilters() {
        return filters;
    }

    /**
     * @param filters the filters to set
     */
    public void setFilters(List<DB4OItemsFilter> filters) {
        this.filters = filters;
    }

    /**
     * @return the useOR
     */
    public boolean getUseOR() {
        return useOR;
    }

    /**
     * @param useOR the useOR to set
     */
    public void setUseOR(boolean useOR) {
        this.useOR = useOR;
    }
}
