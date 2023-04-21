package nl.gridshore.samples.books.integration.jpa;

import nl.gridshore.samples.books.integration.BookDao;
import nl.gridshore.samples.books.domain.Book;
import nl.gridshore.samples.books.domain.Author;
import nl.gridshore.samples.books.common.vo.BookSearchRequest;
import javax.persistence.Query;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.hibernate.ejb.EntityManagerFactoryImpl;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Mar 19, 2008
 * Time: 7:51:08 AM
 * Data acces class using JPA technology
 */
public class BookDaoJpa extends BaseDaoJpa<Book> implements BookDao {

    private int maxRecords = 250;

    public BookDaoJpa() {
        super(Book.class, "Book");
    }

    public List<Book> loadByFilter(BookSearchRequest searchRequest) {
        String queryStr = "select b from Book as b where lower(title) like lower(:title) and isbn like :isbn ";
        Query query = getEntityManager().createQuery(queryStr).setParameter("title", '%' + searchRequest.getBookTitle() + '%').setParameter("isbn", '%' + searchRequest.getBookIsbn() + '%').setMaxResults(maxRecords);
        return query.getResultList();
    }

    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }
}
