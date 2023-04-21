package net.narusas.cafelibrary.apps;

import net.narusas.cafelibrary.BookList;
import net.narusas.cafelibrary.Borrower;

public class BookListsControllerTest extends ControllerTest {

    private BookListsViewCallback c;

    private BookListsView v;

    private MockBookListsView view;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setupModel();
        c = new BookListsViewControllerImpl(model);
        model.setup((BookListsViewControllerImpl) c, new MockBooksListController(), new MockBookDetailController());
        view = new MockBookListsView();
        c.setView(view);
    }

    public void testSelection() {
        c.bookListSelected(bList1);
        assertEquals("unhighlightBorrowers,selectBookList(BookList 1),setBookListVisible,", view.log);
    }

    public void testAddBookList() {
        assertEquals(2, lib.sizeOfBookLists());
        c.bookListAddRequested();
        assertEquals("askNewBookListName(newTitle),", view.log);
        assertEquals(3, lib.sizeOfBookLists());
        assertEquals("newTitle", lib.getBookList(2).getName());
    }

    public void testAddBorrower() {
        assertEquals(1, lib.sizeOfBorrowers());
        c.borrowerAddRequested();
        assertEquals("askNewBookListName(newTitle),", view.log);
        assertEquals(2, lib.sizeOfBorrowers());
        assertEquals("newTitle", lib.getBorrower(1).getName());
    }

    public void testDeleteBookList() {
        assertEquals(2, lib.sizeOfBookLists());
        model.setBookList(bList1);
        view.clearLog();
        c.deleteBookList();
        assertEquals("unhighlightBorrowers,selectBookList(BookList 2),setBookListVisible,", view.log);
    }

    public void testDeleteBorrower() {
        c.borrowerSelected(borrower);
        view.clearLog();
        c.deleteBorrower();
        assertEquals("unhighlightBookLists,selectBorrower(null),setBorrowerVisible,", view.log);
    }
}

class MockBookListsView implements BookListsView {

    String log = "";

    public void selectBookList(BookList bList) {
        log += "selectBookList(" + (bList == null ? null : bList.getName()) + "),";
    }

    public void setBookListVisible() {
        log += "setBookListVisible,";
    }

    public void unhighlightBorrowers() {
        log += "unhighlightBorrowers,";
    }

    public String askNewBookListName() {
        log += "askNewBookListName(newTitle),";
        return "newTitle";
    }

    void clearLog() {
        log = "";
    }

    public void selectBorrower(Borrower borrower) {
        log += "selectBorrower(" + (borrower == null ? null : borrower.getName()) + "),";
    }

    public void setBorrowerVisible() {
        log += "setBorrowerVisible,";
    }

    public void unhighlightBookLists() {
        log += "unhighlightBookLists,";
    }

    public void setController(BookListsViewController controller) {
    }

    public void setCallback(BookListsViewCallback controller) {
    }
}
