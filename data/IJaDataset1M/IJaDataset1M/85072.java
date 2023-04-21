package org.broadleafcommerce.core.catalog.service;

import org.broadleafcommerce.core.catalog.dao.CategoryDao;
import org.broadleafcommerce.core.catalog.dao.ProductDao;
import org.broadleafcommerce.core.catalog.dao.SkuDao;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.type.ProductType;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("blCatalogService")
public class CatalogServiceImpl implements CatalogService {

    @Resource(name = "blCategoryDao")
    protected CategoryDao categoryDao;

    @Resource(name = "blProductDao")
    protected ProductDao productDao;

    @Resource(name = "blSkuDao")
    protected SkuDao skuDao;

    public Product findProductById(Long productId) {
        return productDao.readProductById(productId);
    }

    public List<Product> findProductsByName(String searchName) {
        return productDao.readProductsByName(searchName);
    }

    public List<Product> findActiveProductsByCategory(Category category, Date currentDate) {
        return productDao.readActiveProductsByCategory(category.getId(), currentDate);
    }

    public Product saveProduct(Product product) {
        return productDao.save(product);
    }

    public Category findCategoryById(Long categoryId) {
        return categoryDao.readCategoryById(categoryId);
    }

    @Deprecated
    public Category findCategoryByName(String categoryName) {
        return categoryDao.readCategoryByName(categoryName);
    }

    @Override
    public List<Category> findCategoriesByName(String categoryName) {
        return categoryDao.readCategoriesByName(categoryName);
    }

    public Category saveCategory(Category category) {
        return categoryDao.save(category);
    }

    public void removeCategory(Category category) {
        categoryDao.delete(category);
    }

    public List<Category> findAllCategories() {
        return categoryDao.readAllCategories();
    }

    @Override
    public List<Category> findAllSubCategories(Category category) {
        return categoryDao.readAllSubCategories(category);
    }

    @Override
    public List<Category> findActiveSubCategoriesByCategory(Category category) {
        return categoryDao.readActiveSubCategoriesByCategory(category);
    }

    public List<Product> findAllProducts() {
        return categoryDao.readAllProducts();
    }

    public List<Sku> findAllSkus() {
        return skuDao.readAllSkus();
    }

    public Sku findSkuById(Long skuId) {
        return skuDao.readSkuById(skuId);
    }

    public Sku saveSku(Sku sku) {
        return skuDao.save(sku);
    }

    public List<Sku> findSkusByIds(List<Long> ids) {
        return skuDao.readSkusById(ids);
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setSkuDao(SkuDao skuDao) {
        this.skuDao = skuDao;
    }

    public List<Product> findProductsForCategory(Category category) {
        return productDao.readProductsByCategory(category.getId());
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public Map<String, List<Long>> getChildCategoryURLMapByCategoryId(Long categoryId) {
        Category category = findCategoryById(categoryId);
        if (category != null) {
            return category.getChildCategoryURLMap();
        }
        return null;
    }

    public Category createCategory() {
        return categoryDao.create();
    }

    public Sku createSku() {
        return skuDao.create();
    }

    public Product createProduct(ProductType productType) {
        return productDao.create(productType);
    }
}
