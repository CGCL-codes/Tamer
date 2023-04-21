package net.sourceforge.configured.examples.ui.controller.products.model;

import java.util.List;
import net.sourceforge.configured.examples.ui.vo.CategoryVO;

public class BrowseCategoriesModel {

    protected List<CategoryVO> categories;

    protected int currentPage;

    protected int maxPages;

    public List<CategoryVO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryVO> categories) {
        this.categories = categories;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }
}
