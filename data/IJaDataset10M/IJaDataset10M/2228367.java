package name.huliqing.qblog.web.blog;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import name.huliqing.qblog.QBlog;
import name.huliqing.qblog.web.BaseWe;
import name.huliqing.qblog.entity.ArticleEn;
import name.huliqing.qblog.entity.PageEn;
import name.huliqing.qblog.enums.ArticleSecurity;
import name.huliqing.qblog.service.ArticleSe;
import name.huliqing.qblog.service.CategorySe;
import name.huliqing.qblog.service.PageSe;
import name.huliqing.qfaces.model.PageModel;
import name.huliqing.qfaces.model.PageParam;

public abstract class ArticleListWe extends BaseWe {

    protected Long pageId;

    protected UIData uiArticles;

    public ArticleListWe() {
        super();
        Long tempPageId = QBlog.getPageId();
        if (tempPageId != null) {
            pageId = tempPageId;
        }
    }

    public Long getPageId() {
        if (pageId == null) {
            List<PageEn> pes = PageSe.findAllEnabled();
            if (pes != null && !pes.isEmpty()) {
                pageId = pes.get(0).getPageId();
            }
        }
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public UIData getUiArticles() {
        return uiArticles;
    }

    public void setUiArticles(UIData uiArticles) {
        this.uiArticles = uiArticles;
    }

    public List<SelectItem> getArticleTypes() {
        return ArticleSecurity.generateItems();
    }

    public List<SelectItem> getCategorys() {
        return CategorySe.findAllAsSelectItem();
    }

    abstract PageModel<ArticleEn> loadData(PageParam pp);

    public void delete(ActionEvent ae) {
        ArticleEn articleDel = (ArticleEn) this.uiArticles.getRowData();
        ArticleSe.delete(articleDel.getArticleId());
    }
}
