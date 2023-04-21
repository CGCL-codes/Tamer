package net.sourceforge.configured.examples.domain.service.impl;

import net.sourceforge.configured.annotation.ConfiguredByRules;
import net.sourceforge.configured.examples.domain.dao.product.CategoryDao;
import net.sourceforge.configured.examples.domain.dao.product.ProductDao;

@ConfiguredByRules(rulebase = "service-rules", inherit = true)
public class AbstractService {

    protected ProductDao productDao;

    protected CategoryDao categoryDao;

    private int pageSize;

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
