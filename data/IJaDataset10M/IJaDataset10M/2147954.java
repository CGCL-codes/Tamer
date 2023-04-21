package net.narusas.cafelibrary.ui.bookshelf;

import java.util.LinkedList;
import java.util.List;
import net.narusas.cafelibrary.Book;
import net.narusas.cafelibrary.BookList;
import net.narusas.cafelibrary.BookListListener;
import net.narusas.cafelibrary.SerialBooks;

public class BookShelfModel implements BookListListener {

    private List<BookList> serialBooks;

    private BookList list;

    List<BookShelfModelListener> listeners = new LinkedList<BookShelfModelListener>();

    public BookShelfModel(BookList bList) {
        list = bList;
        if (bList == null) {
            return;
        }
        bList.addListener(this);
        update(bList);
    }

    private void update(BookList bList) {
        this.list = bList;
        SerialBooks worker = new SerialBooks(bList);
        serialBooks = worker.getSerialBooks();
        notifyToListeners();
    }

    private void notifyToListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            BookShelfModelListener listener = listeners.get(i);
            listener.bookListChanged();
        }
    }

    public int size() {
        return serialBooks == null ? 0 : serialBooks.size();
    }

    public BookList getBookList(int index) {
        return serialBooks.get(index);
    }

    public void bookAdded(BookList list, Book book) {
        update(list);
    }

    public void bookRemoved(BookList list, Book book) {
        update(list);
    }

    public void nameChanged(BookList list, String newName) {
        update(list);
    }

    public void remove(Book selectedBook) {
        if (list == null) {
            return;
        }
        list.remove(selectedBook);
        update(list);
    }

    public void addListener(BookShelfModelListener bookShelfModelListener) {
        listeners.add(bookShelfModelListener);
    }
}
