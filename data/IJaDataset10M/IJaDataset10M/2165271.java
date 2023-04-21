package org.ouobpo.tools.amazonchecker.repository;

import static org.ouobpo.tools.amazonchecker.Constants.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.ouobpo.tools.amazonchecker.domain.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;

/**
 * TODO: トランザクション管理をリポジトリの外に移す
 * 
 * @author SATO, Tadayosi
 * @version $Id: BookRepository.java 46 2008-12-29 14:41:23Z tadayosi $
 */
public class BookRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRepository.class);

    private ObjectServer fObjectServer;

    public BookRepository() {
        Db4o.configure().exceptionsOnNotStorable(true);
        Db4o.configure().objectClass(BigDecimal.class).storeTransientFields(true);
        fObjectServer = Db4o.openServer(DB_DIRECTORY + "/" + DB_FILE, 0);
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                close();
            }
        });
    }

    public void save(Book book) {
        ObjectContainer db = fObjectServer.openClient();
        try {
            Book existent = doFind(db, book.getAsin());
            if (existent == null) {
                db.set(book);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("{} を永続化", book);
                }
            } else {
                existent.setTitle(book.getTitle());
                existent.setActive(book.isActive());
                existent.setListPriceHistory(book.getListPriceHistory());
                existent.setUsedPriceHistory(book.getUsedPriceHistory());
                db.set(existent);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("{} を更新", existent);
                }
            }
        } finally {
            db.close();
        }
    }

    public List<Book> find(Book criteria) {
        ObjectContainer db = fObjectServer.openClient();
        try {
            ObjectSet<Book> result = db.get(criteria);
            List<Book> books = new ArrayList<Book>();
            while (result.hasNext()) {
                books.add(result.next());
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("{} 件の書籍を読み込み", books.size());
            }
            return books;
        } finally {
            db.close();
        }
    }

    public Book find(String asin) {
        ObjectContainer db = fObjectServer.openClient();
        try {
            return doFind(db, asin);
        } finally {
            db.close();
        }
    }

    private Book doFind(ObjectContainer db, String asin) {
        Book criteria = new Book();
        criteria.setAsin(asin);
        ObjectSet<Book> result = db.get(criteria);
        if (result.hasNext()) {
            return result.next();
        }
        return null;
    }

    private void close() {
        if (fObjectServer != null) {
            LOGGER.info("DBを停止します");
            fObjectServer.close();
            fObjectServer = null;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
