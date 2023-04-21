package name.huliqing.qblog.web.blog;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIData;
import name.huliqing.qblog.Messenger;
import name.huliqing.qblog.entity.CategoryEn;
import name.huliqing.qblog.service.CategorySe;
import name.huliqing.qblog.web.BaseWe;
import name.huliqing.qfaces.model.PageModel;
import name.huliqing.qfaces.model.PageParam;

/**
 * @author huliqing
 */
@ManagedBean
@RequestScoped
public class CategoryWe extends BaseWe {

    private CategoryEn category;

    private UIData uiCategorys;

    public CategoryWe() {
        super();
    }

    public CategoryEn getCategory() {
        if (category == null) {
            category = new CategoryEn();
        }
        return category;
    }

    public void setCategory(CategoryEn category) {
        this.category = category;
    }

    public UIData getUiCategorys() {
        return uiCategorys;
    }

    public void setUiCategorys(UIData uiCategorys) {
        this.uiCategorys = uiCategorys;
    }

    public PageModel<CategoryEn> loadData(PageParam pp) {
        if (pp.getSortField() == null) {
            pp.setSortField("sort");
            pp.setAsc(Boolean.TRUE);
        }
        return CategorySe.findAll(pp);
    }

    public void save() {
        if (CategorySe.save(category)) {
            Messenger.sendInfo("新建分类成功, Category=" + category.getName());
            category = null;
        }
    }

    public void delete() {
        CategoryEn ce = (CategoryEn) uiCategorys.getRowData();
        if (CategorySe.delete(ce.getName())) {
            Messenger.sendInfo("删除分类成功， Category=" + ce.getName());
        }
    }

    public void updateAll() {
        List<CategoryEn> categorys = (List<CategoryEn>) uiCategorys.getValue();
        if (categorys != null && !categorys.isEmpty()) {
            for (CategoryEn ce : categorys) {
                CategorySe.update(ce);
            }
            Messenger.sendInfo("批量更新成功.");
        }
    }
}
