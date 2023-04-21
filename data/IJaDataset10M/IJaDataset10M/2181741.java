package org.s3b.description.book;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openrdf.model.Graph;

/**
 * This class allows to couple list of books and related RDF Graph into one dynamic object
 * 
 * @author skruk
 *
 */
public class BooksGraphProxy implements BooksGraph {

    /**
	 * Graph associated with this object
	 */
    protected volatile SoftReference<Graph> graph = null;

    /**
	 * Books associated with this object
	 */
    protected List<BookInterface> lstBooks = null;

    /**
	 * Temporary mapping to array of books
	 */
    private WeakReference<BookInterface[]> tmpaBooks = null;

    /**
	 * 
	 */
    public BooksGraphProxy(Graph _graph) {
        this.graph = new SoftReference<Graph>(_graph);
        this.listBooks();
    }

    /**
	 * @param _books
	 */
    public BooksGraphProxy(BookInterface... _books) {
        this.lstBooks = Arrays.asList(_books);
    }

    /**
	 * @param strings
	 */
    public BooksGraphProxy(String... strings) {
        this.lstBooks = new ArrayList<BookInterface>();
        for (String uri : strings) {
            this.lstBooks.add(BookFactory.loadBook(uri));
        }
    }

    /** 
	 * This method has been overriden. 
	 * @see org.s3b.description.book.BooksGraph#getGraph()
	 */
    public Graph getGraph() {
        synchronized (this) {
            Graph _graph = null;
            if (this.graph != null) {
                _graph = this.graph.get();
            }
            if (_graph == null) {
                _graph = BookFactory.getGraph(this.getBooks());
                this.graph = new SoftReference<Graph>(_graph);
            }
            return _graph;
        }
    }

    /**
	 * Internal operation to ensure that we always get the non-null list of books
	 * @return non-null list of books
	 */
    protected List<BookInterface> listBooks() {
        synchronized (this) {
            if (this.lstBooks == null) {
                this.lstBooks = BookFactory.loadBooks(this.graph.get());
            }
            return this.lstBooks;
        }
    }

    /** 
	 * This method has been overriden. 
	 * @see org.s3b.description.book.BooksGraph#getBooks()
	 */
    public BookInterface[] getBooks() {
        BookInterface[] result = null;
        synchronized (this) {
            if (this.tmpaBooks != null) {
                result = this.tmpaBooks.get();
            }
            if (result == null) {
                result = this.listBooks().toArray(new BookInterface[this.listBooks().size()]);
                this.tmpaBooks = new WeakReference<BookInterface[]>(result);
            }
        }
        return result;
    }

    /** 
	 * This method has been overriden. 
	 * @see org.s3b.description.book.BooksGraph#addBook(org.s3b.description.book.BookInterface)
	 */
    public void addBook(BookInterface _book) {
        synchronized (this) {
            this.listBooks().add(_book);
            this.graph = null;
            this.tmpaBooks = null;
        }
    }

    /** 
	 * This method has been overriden. 
	 * @see org.s3b.description.book.BooksGraph#removeBook(org.s3b.description.book.BookInterface)
	 */
    public void removeBook(BookInterface _book) {
        synchronized (this) {
            if (this.listBooks().remove(_book)) {
                this.graph = null;
                this.tmpaBooks = null;
            }
        }
    }

    /** 
	 * This method has been overriden. 
	 * @see org.s3b.description.book.BooksGraph#removeBook(int)
	 */
    public void removeBook(int indx) {
        synchronized (this) {
            if (this.listBooks().remove(indx) != null) {
                this.graph = null;
                this.tmpaBooks = null;
            }
        }
    }
}
