package net.sourceforge.configured.examples.ui.controller.products.impl;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.configured.annotation.ServiceProvider;
import net.sourceforge.configured.examples.domain.entities.product.Category;
import net.sourceforge.configured.examples.domain.entities.product.Product;
import net.sourceforge.configured.examples.domain.exception.ServiceException;
import net.sourceforge.configured.examples.domain.service.CategoryService;
import net.sourceforge.configured.examples.domain.service.ProductService;
import net.sourceforge.configured.examples.ui.controller.AbstractUseCaseController;
import net.sourceforge.configured.examples.ui.controller.products.ProductHomepageController;
import net.sourceforge.configured.examples.ui.controller.products.model.BrowseCategoriesModel;
import net.sourceforge.configured.examples.ui.controller.products.model.BrowseProductsModel;
import net.sourceforge.configured.examples.ui.controller.products.model.CreateProductCategorySuggestionsModel;
import net.sourceforge.configured.examples.ui.exception.FieldValidationException;
import net.sourceforge.configured.examples.ui.transformer.CategoryTransformer;
import net.sourceforge.configured.examples.ui.transformer.ProductTransformer;
import net.sourceforge.configured.examples.ui.vo.CategoryVO;
import net.sourceforge.configured.examples.ui.vo.ProductVO;
import net.sourceforge.configured.examples.utils.list.PagedList;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("productHomepageController")
@Transactional(rollbackFor = { ServiceException.class })
@ServiceProvider(service = ProductHomepageController.class)
public class ProductHomepageControllerImpl extends AbstractUseCaseController implements ProductHomepageController {

    protected ProductTransformer productTransformer;

    protected ProductService productService;

    protected CategoryService categoryService;

    protected CategoryTransformer categoryTransformer;

    @Override
    public BrowseProductsModel browseProductsAction(int pageNumber) {
        PagedList<Product> products = productService.getProducts();
        List<Product> page = products.getPage(pageNumber);
        BrowseProductsModel ret = new BrowseProductsModel();
        List<ProductVO> prods = new ArrayList<ProductVO>();
        if (page != null) {
            for (Product product : page) {
                prods.add(productTransformer.transform(product));
            }
        }
        ret.setProducts(prods);
        ret.setCurrentPage(pageNumber);
        ret.setMaxPages(products.getPageCount());
        return ret;
    }

    @Override
    public BrowseCategoriesModel browseCategoriesAction(int pageNumber) {
        PagedList<Category> pagedList = categoryService.getCategories();
        List<Category> categories = pagedList.getPage(pageNumber);
        BrowseCategoriesModel ret = new BrowseCategoriesModel();
        List<CategoryVO> cats = new ArrayList<CategoryVO>();
        for (Category cat : categories) {
            cats.add(categoryTransformer.transform(cat));
        }
        ret.setCategories(cats);
        ret.setCurrentPage(pageNumber);
        ret.setMaxPages(pagedList.getPageCount());
        return ret;
    }

    @Override
    public CreateProductCategorySuggestionsModel suggestCategoriesAction(String userInput) {
        CreateProductCategorySuggestionsModel ret = new CreateProductCategorySuggestionsModel();
        List<Category> suggesionsDomain = getBestSuggestionsDomain(userInput);
        List<CategoryVO> suggestions = new ArrayList<CategoryVO>();
        for (Category category : suggesionsDomain) {
            suggestions.add(categoryTransformer.transform(category));
        }
        ret.setSuggestions(suggestions);
        return ret;
    }

    @Override
    public void createCategoryAction(CategoryVO category) throws FieldValidationException, ServiceException {
        categoryService.createOrUpdateCategory(categoryTransformer.transform(category));
    }

    @Override
    public void createProductAction(ProductVO product) throws FieldValidationException, ServiceException {
        productService.createOrUpdateProduct(productTransformer.transform(product));
    }

    protected List<Category> getBestSuggestionsDomain(String userInput) {
        List<Category> suggestionsDomain = null;
        while (suggestionsDomain == null || suggestionsDomain.isEmpty() && !StringUtils.isBlank(userInput)) {
            suggestionsDomain = categoryService.getCategoriesLike(userInput).getPage(1);
            if (userInput.length() > 0) {
                userInput = userInput.substring(0, userInput.length() - 1);
            }
        }
        if (suggestionsDomain == null || suggestionsDomain.isEmpty()) {
            suggestionsDomain = categoryService.getCategories().getPage(1);
        }
        return suggestionsDomain;
    }

    public ProductTransformer getProductTransformer() {
        return productTransformer;
    }

    public void setProductTransformer(ProductTransformer productTransformer) {
        this.productTransformer = productTransformer;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryTransformer getCategoryTransformer() {
        return categoryTransformer;
    }

    public void setCategoryTransformer(CategoryTransformer categoryTransformer) {
        this.categoryTransformer = categoryTransformer;
    }
}
