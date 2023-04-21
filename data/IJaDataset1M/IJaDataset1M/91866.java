package net.narusas.cafelibrary.apps;

import net.narusas.cafelibrary.Book;
import net.narusas.cafelibrary.BookFactory;
import net.narusas.cafelibrary.BookList;

public class BookDetailControllerTest extends ControllerTest {

    BookDetailViewController c;

    private MockBookDetailView view;

    private MockBooksListView booksView;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setupModel();
        c = new BookDetailViewControllerImpl(model);
        MockBookListsController bsc = new MockBookListsController();
        bsc.setView(new MockBookListsView());
        MockBooksListController lc = new MockBooksListController();
        booksView = new MockBooksListView();
        lc.setView(booksView);
        model.setup(bsc, lc, c);
        view = new MockBookDetailView();
        c.setView(view);
    }

    public void testSetBookEmpty() {
        c.setBook(null);
        assertEquals("showBookDetailView,clearInfos,setBook(null),", view.log);
    }

    public void testSetBook() {
        model.setBookList(bList1);
        assertEquals("showBookDetailView,clearInfos,setBook(Title 1),", view.log);
        assertEquals(book1, c.getSettedBook());
    }

    public void testSelectMethod() {
        c.setBook(book1);
        view.clearLog();
        c.bookAddRequested();
        assertEquals("showSelectAddingMethod,", view.log);
    }

    public void testAddBookByHand() {
        model.setBookList(bList1);
        assertEquals(3, bList1.getBookSize());
        c.bookAddRequested();
        view.clearLog();
        c.bookAddByHandRequested();
        assertEquals("showBookEdit,setBook(Title),", view.log);
        view.clearLog();
        c.setBook(book2);
        assertEquals("showBookDetailView,clearInfos,setBook(Title 2),", view.log);
        assertEquals(4, bList1.getBookSize());
    }

    public void testAddBookBySearch() {
        model.setBookFactory(new DummyBF());
        model.setBookList(bList1);
        assertEquals(3, bList1.getBookSize());
        c.bookAddRequested();
        view.clearLog();
        c.bookAddBySearchRequested("a");
        c.addSearchedBook(new Book("Title"));
        assertEquals("showSearchingBook,setSearch(a),setSearchingBookList(a),", view.log);
    }
}

class MockBookDetailView implements BookDetailView {

    String log = "";

    void clearLog() {
        log = "";
    }

    public void clearInfos() {
        log += "clearInfos,";
    }

    public void setBook(Book book) {
        log += "setBook(" + (book == null ? null : book.getTitle()) + "),";
    }

    public void showBookDetailView() {
        log += "showBookDetailView,";
    }

    public void showSelectAddingMethodView() {
        log += "showSelectAddingMethod,";
    }

    public void showBookEditView() {
        log += "showBookEdit,";
    }

    public void setSearch(String searchTarget) {
        log += "setSearch(" + searchTarget + "),";
    }

    public void setSearchingBookList(BookList searchingList) {
        log += "setSearchingBookList(" + searchingList.getName() + "),";
    }

    public void showSearchingBookView() {
        log += "showSearchingBook,";
    }

    public void setController(BookDetailViewController controller) {
    }

    public void setCallback(BookDetailViewCallback controller) {
    }
}

class DummyBF extends BookFactory {

    public DummyBF() {
        super("dummy");
    }

    @Override
    public BookList findBooks(String searchTarget) {
        return new DummySearchingBookList(searchTarget);
    }

    @Override
    public Book findSpecificBook(String searchTarget) {
        return null;
    }
}

class DummySearchingBookList extends BookList {

    public DummySearchingBookList(String name) {
        super(name);
    }
}
