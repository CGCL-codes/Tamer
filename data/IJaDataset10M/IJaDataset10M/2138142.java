package net.narusas.cafelibrary.ui;

import net.narusas.cafelibrary.Book;
import net.narusas.cafelibrary.BookList;
import net.narusas.cafelibrary.Library;

public class Controller {

    private Library lib;

    private MainFrame f;

    private BookListController blc;

    private BookTableController btc;

    private BookDetailController bdc;

    public void setFrame(final MainFrame f) {
        this.f = f;
    }

    public void setLibrary(Library lib) {
        this.lib = lib;
    }

    public void setup(BookListController blc, BookTableController btc, BookDetailController bdc) {
        this.blc = blc;
        this.btc = btc;
        this.bdc = bdc;
        bind();
        blc.setFirstList();
        btc.bookListSelected(lib);
        bdc.bookSelected(lib.get(0));
    }

    private void bind() {
        blc.setListener(new BookListSectionListener() {

            public void bookListSelected(BookList bookList) {
                if (bookList == null) {
                    return;
                }
                btc.bookListSelected(bookList);
                bdc.bookSelected(bookList.get(0));
            }
        });
        btc.setListener(new BookTableSectionListener() {

            public void addBook() {
                bdc.addBook();
            }

            public void bookSelected(Book book) {
                bdc.bookSelected(book);
            }
        });
    }
}
