package net.sf.gham.core.entity.player.category;

import java.util.Comparator;
import net.sf.gham.core.dao.Category;

/**
 * @author fabio
 *
 */
public class CategoryComparator implements Comparator<Category> {

    public int compare(Category o1, Category o2) {
        int r = o1.getViewOrder() - o2.getViewOrder();
        if (r == 0) {
            r = o1.getName().compareToIgnoreCase(o2.getName());
        }
        return r;
    }
}
