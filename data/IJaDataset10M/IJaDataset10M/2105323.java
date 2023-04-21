package com.example.pagingcomponent;

import java.util.ArrayList;
import java.util.List;
import org.vaadin.pagingcomponent.ComponentsManager;
import org.vaadin.pagingcomponent.PagingComponent;
import org.vaadin.pagingcomponent.PagingComponent.ChangePageEvent;
import org.vaadin.pagingcomponent.PagingComponent.PagingComponentListener;
import org.vaadin.pagingcomponent.RangeDisplayer;
import org.vaadin.pagingcomponent.button.ButtonPageNavigator;
import org.vaadin.pagingcomponent.customizer.adaptator.impl.CustomizerAdaptator;
import org.vaadin.pagingcomponent.customizer.style.ButtonsStyleCustomizer;
import com.vaadin.Application;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import dao.ProductDao;
import entities.Product;

@SuppressWarnings("serial")
public class PagingComponentApplication extends Application {

    @Override
    public void init() {
        setTheme("myTheme");
        Window mainWindow = new Window("PagingComponent Application");
        VerticalLayout verticalContent = new VerticalLayout();
        verticalContent.addComponent(integerExemple());
        verticalContent.addComponent(daoExample1());
        verticalContent.addComponent(daoExample2());
        verticalContent.addComponent(styleExempleWithPagingComponentMethods());
        verticalContent.addComponent(styleExempleWithButtonsStyleCustomizer());
        verticalContent.addComponent(styleExempleCustomizerAdaptator());
        verticalContent.addComponent(rangeDisplayerExemple());
        verticalContent.addComponent(test1());
        mainWindow.setContent(verticalContent);
        setMainWindow(mainWindow);
    }

    /** Simple example with integers stored by the PagingComponent*/
    private VerticalLayout integerExemple() {
        List<Integer> item = new ArrayList<Integer>();
        for (int i = 1; i <= 300; i++) {
            item.add(new Integer(i));
        }
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        final PagingComponent<Integer> pagingComponent = new PagingComponent<Integer>(10, item, new PagingComponentListener<Integer>() {

            @Override
            public void displayPage(ChangePageEvent<Integer> event) {
                itemsArea.removeAllComponents();
                for (Integer item : event.getPageRange().getItemsList()) {
                    itemsArea.addComponent(new Label(String.valueOf(item)));
                }
            }
        });
        mainLayout.addComponent(new Label("<h1>Simple example with integers<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        return mainLayout;
    }

    /** Product example with Dao 1 , with the primary keys (Long id) held by the PagingComponent. */
    private VerticalLayout daoExample1() {
        final ProductDao productDao = new ProductDao();
        List<Long> productIds = productDao.getProductIds();
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        final PagingComponent<Long> pagingComponent = new PagingComponent<Long>(10, productIds, new PagingComponentListener<Long>() {

            @Override
            public void displayPage(ChangePageEvent<Long> event) {
                itemsArea.removeAllComponents();
                int indexStart = event.getPageRange().getIndexPageStart();
                int indexEnd = event.getPageRange().getIndexPageEnd();
                List<Product> productList = productDao.getProductsFromRange(indexStart, indexEnd);
                for (Product product : productList) {
                    itemsArea.addComponent(new Label(product.toString()));
                }
            }
        });
        mainLayout.addComponent(new Label("<h1>Example with Dao 1<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        return mainLayout;
    }

    /** Product example with Dao 2, with the product held by the PagingComponent. */
    private VerticalLayout daoExample2() {
        final ProductDao productDao = new ProductDao();
        final List<Product> productList = productDao.getAllProducts();
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        final PagingComponent<Product> pagingComponent = new PagingComponent<Product>(10, productList, new PagingComponentListener<Product>() {

            @Override
            public void displayPage(ChangePageEvent<Product> event) {
                itemsArea.removeAllComponents();
                for (Product product : event.getPageRange().getItemsList()) {
                    itemsArea.addComponent(new Label(product.toString()));
                }
            }
        });
        mainLayout.addComponent(new Label("<h1>Example with Dao 2<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        return mainLayout;
    }

    /** Example that demonstrates how to manipulate PagingComponent styles (deprecated)*/
    @SuppressWarnings("deprecation")
    private VerticalLayout styleExempleWithPagingComponentMethods() {
        List<Integer> item = new ArrayList<Integer>();
        for (int i = 1; i <= 300; i++) {
            item.add(new Integer(i));
        }
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        final PagingComponent<Integer> pagingComponent = new PagingComponent<Integer>(10, item, new PagingComponentListener<Integer>() {

            @Override
            public void displayPage(ChangePageEvent<Integer> event) {
                itemsArea.removeAllComponents();
                for (Integer item : event.getPageRange().getItemsList()) {
                    itemsArea.addComponent(new Label(String.valueOf(item)));
                }
            }
        });
        pagingComponent.setStyleNameForAllButtons(BaseTheme.BUTTON_LINK);
        pagingComponent.addStyleNameForAllButtons("styleRed");
        pagingComponent.addStyleNameCurrentButtonState("styleBold");
        mainLayout.addComponent(new Label("<h1>Example PagingComponent Styles<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        return mainLayout;
    }

    /** Example that demonstrates how to manipulate PagingComponent styles with the {@link ButtonsStyleCustomizer}*/
    private VerticalLayout styleExempleWithButtonsStyleCustomizer() {
        List<Integer> item = new ArrayList<Integer>();
        for (int i = 1; i <= 300; i++) {
            item.add(new Integer(i));
        }
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        ButtonsStyleCustomizer styler = new ButtonsStyleCustomizer() {

            @Override
            public void styleButtonFirst(Button button, ComponentsManager manager) {
                button.setVisible(!manager.isFirstPage());
            }

            @Override
            public void styleButtonLast(Button button, ComponentsManager manager) {
                button.setVisible(!manager.isLastPage());
            }

            @Override
            public void styleButtonPrevious(Button button, ComponentsManager manager) {
                button.setVisible(!manager.isFirstPage());
            }

            @Override
            public void styleButtonNext(Button button, ComponentsManager manager) {
                button.setVisible(!manager.isLastPage());
            }

            @Override
            public void styleButtonPageNormal(ButtonPageNavigator button, int pageNumber) {
                button.setPage(pageNumber);
                button.removeStyleName("styleRed");
            }

            @Override
            public void styleButtonPageCurrentPage(ButtonPageNavigator button, int pageNumber) {
                button.setPage(pageNumber);
                button.addStyleName("styleRed");
                button.focus();
            }

            @Override
            public void styleFirstSeparator(AbstractComponent separator, ComponentsManager manager) {
                separator.setVisible(!manager.isFirstPage());
            }

            @Override
            public void styleLastSeparator(AbstractComponent separator, ComponentsManager manager) {
                separator.setVisible(!manager.isLastPage());
            }
        };
        final PagingComponent<Integer> pagingComponent = new PagingComponent<Integer>(10, 10, styler, item, new PagingComponentListener<Integer>() {

            @Override
            public void displayPage(ChangePageEvent<Integer> event) {
                itemsArea.removeAllComponents();
                for (Integer item : event.getPageRange().getItemsList()) {
                    itemsArea.addComponent(new Label(String.valueOf(item)));
                }
            }
        });
        mainLayout.addComponent(new Label("<h1>Example PagingComponent Styles with the ButtonsStyleCustomizer<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        return mainLayout;
    }

    /** Example that demonstrates how to manipulate PagingComponent styles with the {@link CustomizerAdaptator}*/
    private VerticalLayout styleExempleCustomizerAdaptator() {
        List<Integer> item = new ArrayList<Integer>();
        for (int i = 1; i <= 300; i++) {
            item.add(new Integer(i));
        }
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        CustomizerAdaptator adaptator = new CustomizerAdaptator() {

            @Override
            public Button createButtonFirst() {
                Button button = new Button("<<");
                button.setStyleName(BaseTheme.BUTTON_LINK);
                return button;
            }

            @Override
            public Button createButtonLast() {
                Button button = new Button(">>");
                button.setStyleName(BaseTheme.BUTTON_LINK);
                return button;
            }

            @Override
            public Button createButtonNext() {
                Button button = new Button(">");
                button.setStyleName(BaseTheme.BUTTON_LINK);
                return button;
            }

            @Override
            public Button createButtonPrevious() {
                Button button = new Button("<");
                button.setStyleName(BaseTheme.BUTTON_LINK);
                return button;
            }

            @Override
            public void styleButtonPageCurrentPage(ButtonPageNavigator button, int pageNumber) {
                button.setPage(pageNumber, "[" + pageNumber + "]");
                button.addStyleName("styleRed");
                button.focus();
            }

            @Override
            public void styleButtonPageNormal(ButtonPageNavigator button, int pageNumber) {
                button.setPage(pageNumber);
                button.removeStyleName("styleRed");
            }

            @Override
            public AbstractComponent createFirstSeparator() {
                return null;
            }

            @Override
            public AbstractComponent createLastSeparator() {
                return null;
            }
        };
        final PagingComponent<Integer> pagingComponent = new PagingComponent<Integer>(10, 10, adaptator, item, new PagingComponentListener<Integer>() {

            @Override
            public void displayPage(ChangePageEvent<Integer> event) {
                itemsArea.removeAllComponents();
                for (Integer item : event.getPageRange().getItemsList()) {
                    itemsArea.addComponent(new Label(String.valueOf(item)));
                }
            }
        });
        mainLayout.addComponent(new Label("<h1>Example PagingComponent Styles with the CustomizerAdaptator<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        return mainLayout;
    }

    /** Example with RangeDisplayer, i.e. display "Product 20-40 currently displayed". */
    private VerticalLayout rangeDisplayerExemple() {
        List<Integer> item = new ArrayList<Integer>();
        for (int i = 1; i <= 300; i++) {
            item.add(new Integer(i));
        }
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        final PagingComponent<Integer> pagingComponent = new PagingComponent<Integer>(10, item, new PagingComponentListener<Integer>() {

            @Override
            public void displayPage(ChangePageEvent<Integer> event) {
                itemsArea.removeAllComponents();
                for (Integer item : event.getPageRange().getItemsList()) {
                    itemsArea.addComponent(new Label(String.valueOf(item)));
                }
            }
        });
        RangeDisplayer<Integer> rd = new RangeDisplayer<Integer>(pagingComponent);
        rd.setValue("results : ");
        mainLayout.addComponent(new Label("<h1>Example with RangeDisplayer<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        mainLayout.addComponent(rd);
        return mainLayout;
    }

    private VerticalLayout test1() {
        List<Integer> item = new ArrayList<Integer>();
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        final PagingComponent<Integer> pagingComponent = new PagingComponent<Integer>(10, item, new PagingComponentListener<Integer>() {

            @Override
            public void displayPage(ChangePageEvent<Integer> event) {
                itemsArea.removeAllComponents();
                for (Integer item : event.getPageRange().getItemsList()) {
                    itemsArea.addComponent(new Label(String.valueOf(item)));
                }
            }
        });
        mainLayout.addComponent(new Label("<h1>Simple example with integers<h1>", Label.CONTENT_XHTML));
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
        return mainLayout;
    }
}
