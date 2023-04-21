package net.narusas.cafelibrary.ui2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import net.narusas.cafelibrary.Book;
import net.narusas.cafelibrary.BookComparator;
import net.narusas.cafelibrary.BookList;
import net.narusas.cafelibrary.BookListListener;

public class SearchedBookListController implements BookListListener {

    private final SearchedBookListView view;

    private BookList list;

    private HashMap<Book, SearchedBook> searchedBooks;

    public SearchedBookListController(SearchedBookListView view) {
        this.view = view;
        searchedBooks = new HashMap<Book, SearchedBook>();
    }

    public void setBookList(BookList list) {
        System.out.println("setBookList:" + list);
        if (this.list != null) {
            list.removeListener(this);
        }
        this.list = list;
        clear();
        if (this.list == null) {
            return;
        }
        startWaiter();
        list.addListener(this);
    }

    private void startWaiter() {
    }

    private void clear() {
        view.getContentPanel().removeAll();
        view.getContentPanel().revalidate();
        searchedBooks.clear();
    }

    public void bookAdded(BookList list, Book addedBook) {
        System.out.println("bookAdded:" + addedBook);
        List<Book> books = new ArrayList<Book>(list.getCollection());
        if (books == null || books.size() == 0) {
            return;
        }
        view.getContentPanel().removeAll();
        view.getContentPanel().revalidate();
        Collections.sort(books, new BookComparator(BookComparator.SORT_COL.COL_TITLE, true));
        for (Book book : books) {
            SearchedBook searchedBook = match(book);
            view.getContentPanel().add(searchedBook);
        }
    }

    private SearchedBook match(Book book) {
        if (searchedBooks.containsKey(book)) {
            return searchedBooks.get(book);
        }
        SearchedBook result = new SearchedBook();
        result.setBook(book);
        searchedBooks.put(book, result);
        return result;
    }

    public void bookRemoved(BookList list, Book book) {
    }

    public void nameChanged(BookList list, String newName) {
    }
}
