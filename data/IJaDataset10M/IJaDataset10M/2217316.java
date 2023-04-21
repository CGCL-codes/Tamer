package name.huliqing.qblog.service;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import name.huliqing.qblog.daocache.CategoryCache;
import name.huliqing.qblog.entity.CategoryEn;
import name.huliqing.qfaces.model.PageModel;
import name.huliqing.qfaces.model.PageParam;

/**
 *
 * @author huliqing
 */
public class CategorySe {

    public static final boolean save(CategoryEn categoryEn) {
        return CategoryCache.getInstance().save(categoryEn);
    }

    /**
     * 导入数据（不要使用save(CategoryEn）
     * @param categoryEn
     * @return
     */
    public static final boolean _import(CategoryEn categoryEn) {
        return CategoryCache.getInstance().save(categoryEn);
    }

    public static final boolean update(CategoryEn categoryEn) {
        return CategoryCache.getInstance().update(categoryEn);
    }

    public static final boolean delete(String name) {
        return CategoryCache.getInstance().delete(name);
    }

    public static final CategoryEn find(String name) {
        return CategoryCache.getInstance().find(name);
    }

    public static final List<CategoryEn> findAll() {
        return CategoryCache.getInstance().findAll();
    }

    public static final PageModel<CategoryEn> findAll(PageParam pp) {
        throw new UnsupportedOperationException("未实现的方法");
    }

    public static final List<SelectItem> findAllAsSelectItem() {
        List<CategoryEn> all = CategoryCache.getInstance().findAll();
        List<SelectItem> items = new ArrayList<SelectItem>();
        if (all != null && !all.isEmpty()) {
            for (CategoryEn ce : all) {
                items.add(new SelectItem(ce.getName(), ce.getName()));
            }
        }
        return items;
    }
}
