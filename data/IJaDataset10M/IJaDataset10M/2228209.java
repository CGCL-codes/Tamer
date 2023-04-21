package pl.model.service;

import java.io.File;
import java.util.List;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.bridge.StringBridge;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;
import pl.common.PaddedIntegerBridge;
import pl.model.AbstractTest;
import pl.model.domain.Author;
import pl.model.domain.Book;

public class LuceneTest extends AbstractTest {

    public static final int DB_SIZE = 50;

    protected StringBridge rankBridge = (StringBridge) new PaddedIntegerBridge();

    public LuceneTest() {
        super();
    }

    public void testTestDao() {
        this.searchService.testDao();
    }

    @Rollback(false)
    public void testInsertSampleData() {
        String tags = Tag.ADULTS + " " + Tag.BRUTAL + " " + Tag.FIGHT + " " + Tag.THRILLER;
        String description = "Ogoreczek, ogoreczek brutalna ksiazka dla doroslych, zakazana dla dzieci do lat 18. " + "Ogoreczek, Przeznaczona dla doroslych czytelnikow. Opowiada brutalna historie morterstwa." + "Morderstwo bardzo brutalne, ogoreczek, ogoreczek";
        Book book = new Book("Poszukiwany pierniczek", tags, 11, description);
        bookDao.persist(book);
        RandomObjectFactory factory = new RandomObjectFactory();
        for (int i = 0; i < DB_SIZE; i++) {
            book = factory.getPlainBook();
            bookDao.persist(book);
        }
        for (int i = 0; i < DB_SIZE; i++) {
            book = factory.getPlainBookWithAuthor();
            Author author = authorDao.find(book.getAuthor().getName(), book.getAuthor().getSurname());
            if (author == null) {
                try {
                    bookDao.persist(book);
                } catch (DataAccessException e) {
                    log.info("blad przy zapisie ksiazki: " + book);
                    e.printStackTrace();
                }
            } else {
                log.info("already exists book with author " + author);
            }
        }
        book = new Book("Pierniczek Zemsta pierniczek smoka", Tag.FIGHT + "", 11, "Ksiazka dla mlodziezy, ogoreczek");
        bookDao.persist(book);
    }

    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Test
    public void testLuceneBookByTitle() {
        try {
            FullTextEntityManager fem = Search.createFullTextEntityManager(super.sharedEntityManager);
            QueryParser parser = new QueryParser("title", new StopAnalyzer());
            Query query = parser.parse("rank:noword");
            assertEquals(0, fem.createFullTextQuery(query).getResultList().size());
            query = parser.parse("title:Drzewko");
            assertEquals("Drzewko don't go with parser", 1, fem.createFullTextQuery(query).getResultList().size());
            query = new TermQuery(new Term("title", "Drzewko"));
            List<Book> books = fem.createFullTextQuery(query, Book.class).getResultList();
            assertEquals("by Title Drzewko ", 0, books.size());
            query = new TermQuery(new Term("title", "pierniczek"));
            books = fem.createFullTextQuery(query, Book.class).getResultList();
            for (Book book : books) {
                log.info("znalazl " + book);
            }
            assertEquals("getResultList", 2, books.size());
            query = new TermQuery(new Term("rank", rankBridge.objectToString(11)));
            books = fem.createFullTextQuery(query, Book.class).getResultList();
            assertTrue("sam wstawilem 2 z rank 11", books.size() >= 2);
            for (Book book : books) {
                log.info("znalazl " + book);
                assertEquals(11, book.getRank().intValue());
            }
            fem.clear();
        } catch (ParseException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testLuceneBookByDescription() {
        try {
            FullTextEntityManager fem = Search.createFullTextEntityManager(super.sharedEntityManager);
            Query query = new TermQuery(new Term("description", "ogoreczek"));
            List<Book> books = fem.createFullTextQuery(query, Book.class).getResultList();
            for (Book book : books) {
                log.info("znalazl " + book);
            }
            assertEquals("getResultList", 2, books.size());
            fem.clear();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testLuceneBookByTags() {
        try {
            FullTextEntityManager fem = Search.createFullTextEntityManager(super.sharedEntityManager);
            QueryParser parser = new QueryParser("tags", new StopAnalyzer());
            Query query = parser.parse("+poezja liryka -horror");
            List<Book> books = fem.createFullTextQuery(query, Book.class).getResultList();
            for (Book book : books) {
                log.info("znalazl (+liryka +poezja):" + book.getTags());
                assertTrue("nie zawiera poezji", book.getTags().contains("poezja"));
            }
            fem.clear();
        } catch (ParseException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testLuceneBookById() {
        FullTextEntityManager fem = Search.createFullTextEntityManager(super.sharedEntityManager);
        List<Book> books = bookDao.getAll();
        for (Book book : books) {
            Query query = new TermQuery(new Term("id", book.getId() + ""));
            Book b = (Book) fem.createFullTextQuery(query, Book.class).getSingleResult();
            assertEquals(book.getTitle(), b.getTitle());
            assertEquals(book.getDescription(), b.getDescription());
            assertEquals(book.getTags(), b.getTags());
        }
    }

    @Test
    public void testLuceneBookByAuthorName() {
        FullTextEntityManager fem = Search.createFullTextEntityManager(super.sharedEntityManager);
        QueryParser parser = new QueryParser("author", new StandardAnalyzer());
        List<Author> authors = authorDao.getAll();
        for (Author author : authors) {
            log.info("debuging author " + author);
            Query query = new TermQuery(new Term("author.name", author.getName()));
            List<Book> books = fem.createFullTextQuery(query, Book.class).getResultList();
            assertTrue(books.size() >= 1);
            for (Book book : books) {
                log.info("book " + book.getTitle() + " author: " + book.getAuthor());
                assertEquals(book.getAuthor().getName(), author.getName());
            }
        }
    }

    @Test
    @Rollback(false)
    public void testClearIndexesAndDatabase() {
        if (1 == 1) return;
        clearDatabase();
        File f = new File("/tmp/lucene/indexes");
        deleteDir(f);
        f.mkdir();
    }

    protected enum Tag {

        HORROR, COMEDY, DRAMAT, THRILLER, EROTIC, BRUTAL, FIGHT, VIOLENCE, LOVE, CHILDREN, ADULTS, YOUTH
    }
}
