package net.sf.gham.plugins.panels.player.list.player.category.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sf.gham.core.dao.Category;
import net.sf.gham.core.dao.CategoryItem;
import net.sf.gham.core.entity.player.YouthPlayerMyTeam;
import net.sf.gham.core.entity.player.category.CategoryItemChangeListener;
import net.sf.gham.swing.filter.IObjectFilter;
import net.sf.gham.swing.filter.PopupMultipleFilter;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 *
 */
public class CategoryFilter extends PopupMultipleFilter<YouthPlayerMyTeam, IObjectFilter<YouthPlayerMyTeam>> implements CategoryItemChangeListener {

    private final Map<Integer, CategoryItemFilter> filtersMap;

    public CategoryFilter(final Category category) {
        super("category_" + category.getIdCategory());
        filtersMap = new HashMap<Integer, CategoryItemFilter>(category.getCategoryItems().size() + 1);
        for (CategoryItem item : category.getCategoryItems()) {
            addFilter(createFilter(item));
        }
        CategoryItem item = createNoCategory(category);
        CategoryItemFilter f = new CategoryItemFilter(item) {

            @Override
            protected boolean filterIfEnabled(YouthPlayerMyTeam p) {
                return p.isNotOfCategory(category.getIdCategory());
            }
        };
        addOtherFilter(f);
        filtersMap.put(item.getIdCategoryItem(), f);
        setLabel(Messages.getString(category.getName(), category.isTranslated()));
    }

    private CategoryItemFilter createFilter(CategoryItem item) {
        CategoryItemFilter f = new CategoryItemFilter(item);
        CategoryItemFilter oldFilter = filtersMap.get(item.getIdCategoryItem());
        if (oldFilter != null) {
            f.setSelected(oldFilter.isSelected(), false);
        }
        filtersMap.put(item.getIdCategoryItem(), f);
        return f;
    }

    private CategoryItem createNoCategory(Category category) {
        CategoryItem categoryItem = new CategoryItem("Others", "", true, Integer.MAX_VALUE, false);
        categoryItem.setIdCategoryItem(-1);
        return categoryItem;
    }

    public void categoryItemAdded(CategoryItem item) {
        if (!filtersMap.containsKey(item.getIdCategoryItem())) {
            addFilter(createFilter(item));
        }
    }

    public void categoryItemRemoved(CategoryItem item) {
        removeFilter(filtersMap.remove(item.getIdCategoryItem()));
    }

    public void categoryItemsOrderChanged(Collection<CategoryItem> items) {
        for (CategoryItem item : items) {
            CategoryItemFilter f = filtersMap.get(item.getIdCategoryItem());
            removeFilterFromView(f);
        }
        for (CategoryItem item : items) {
            addFilterInView(createFilter(item));
        }
    }
}
