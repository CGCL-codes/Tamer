package wicket.wicketbook.book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import wicket.Component;
import wicket.extensions.markup.html.repeater.data.DataView;
import wicket.extensions.markup.html.repeater.data.IDataProvider;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.navigation.paging.PagingNavigator;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.PropertyModel;
import wicket.quickstart.QuickStartApplication;
import wicket.quickstart.SecuredBasePage;

public class ViewBooks extends SecuredBasePage {

    public List getBookCategories() {
        QuickStartApplication application = (QuickStartApplication) getApplication();
        return application.getBookDao().getSupportedCategories();
    }

    public ViewBooks() {
        final Form form = new Form("bookForm");
        final BookDataProvider dataProvider = new BookDataProvider();
        final DataView books = new BookDataView("books", dataProvider);
        DropDownChoice categories = new CategoryDropDownChoice("categories", new PropertyModel(dataProvider, "category"), getBookCategories(), books);
        categories.setNullValid(false);
        form.add(categories);
        books.setItemsPerPage(10);
        form.add(books);
        form.add(new Button("addToCart") {

            public void onSubmit() {
                System.out.println("Need to implement add to cart!!");
            }
        });
        form.add(new PagingNavigator("navigator", books));
        add(form);
    }

    class BookDataView extends DataView {

        public BookDataView(String id, IDataProvider dataProvider) {
            super(id, dataProvider);
        }

        protected void populateItem(final Item item) {
            Book book = (Book) item.getModelObject();
            item.setModel(new CompoundPropertyModel(book));
            item.add(new Label("title"));
            item.add(new Label("author"));
            item.add(new Label("publisher"));
            item.add(new Label("price"));
            item.add(new MyCheckBox("selected", new CheckBoxModel(book.getId())));
        }
    }

    class MyCheckBox extends CheckBox {

        public MyCheckBox(String id, IModel model) {
            super(id, model);
        }

        protected boolean wantOnSelectionChangedNotifications() {
            return true;
        }
    }

    class CategoryDropDownChoice extends DropDownChoice {

        DataView bookDataView;

        public CategoryDropDownChoice(String id, IModel model, List displayData, DataView bookDataView) {
            super(id, model, displayData);
            this.bookDataView = bookDataView;
        }

        public boolean wantOnSelectionChangedNotifications() {
            return true;
        }

        public void onSelectionChanged(java.lang.Object newSelection) {
            getForm().process();
            bookDataView.setCurrentPage(0);
        }
    }

    private List booksMarkedForCheckout = new ArrayList();

    private class CheckBoxModel implements IModel, Serializable {

        private final Integer bookId;

        public CheckBoxModel(int bookId) {
            this.bookId = new Integer(bookId);
        }

        public IModel getNestedModel() {
            return null;
        }

        public Object getObject(Component component) {
            return isBookAlreadyMarkedForCheckout();
        }

        private Boolean isBookAlreadyMarkedForCheckout() {
            if (booksMarkedForCheckout.contains(bookId)) return Boolean.TRUE; else return Boolean.FALSE;
        }

        public void setObject(Component component, Object object) {
            boolean selected = ((Boolean) object).booleanValue();
            boolean previouslySelected = isBookAlreadyMarkedForCheckout().booleanValue();
            if (selected) {
                if (!previouslySelected) {
                    booksMarkedForCheckout.add(bookId);
                }
            } else {
                if (previouslySelected) {
                    booksMarkedForCheckout.remove(bookId);
                }
            }
        }

        public void detach() {
        }
    }
}
