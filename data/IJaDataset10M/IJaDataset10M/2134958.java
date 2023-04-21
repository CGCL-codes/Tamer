package gemini.castor.ui.client.page.content.store.catalog.categorycatalog;

import gemini.basic.model.ProductType;
import gemini.castor.ui.client.page.widget.categorylist.CategoryListEvent;
import gemini.castor.ui.client.page.widget.categorylist.CategoryListHandler;
import gemini.castor.ui.client.page.widget.categorylist.CategoryListWidget;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class CategoryCatalogView extends Composite implements CategoryCatalogPresenter.Display {

    @UiTemplate("CategoryCatalogView.ui.xml")
    interface MyUiBinder extends UiBinder<Panel, CategoryCatalogView> {
    }

    private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

    private ProductType category;

    public CategoryCatalogView() {
        initWidget(binder.createAndBindUi(this));
    }

    @UiField
    HTML htmCategoryName;

    @UiField
    Image imgCategory;

    @UiField
    Button btnViewProducts;

    @UiField
    HTML htmDescription;

    @UiField
    CategoryListWidget cwgCategoryList;

    private List<ProductType> productTypes;

    @Override
    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    @Override
    public void loadData() {
        category = productTypes.get(0);
        changeCategory();
        cwgCategoryList.loadData(productTypes);
        cwgCategoryList.addCategoryListHandler(new CategoryListHandler() {

            @Override
            public void onSelectedCategory(CategoryListEvent categoryListEvent) {
                category = categoryListEvent.getSelectedCategory();
                changeCategory();
            }
        });
    }

    @Override
    public ProductType getCategory() {
        return category;
    }

    public void changeCategory() {
        htmDescription.setHTML(category.getDescription());
        imgCategory.setUrl(category.getImageUrl());
        htmCategoryName.setHTML("<h3>" + category.getName() + "</h3>");
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HasClickHandlers getViewProductsClickHandlers() {
        return btnViewProducts;
    }
}
