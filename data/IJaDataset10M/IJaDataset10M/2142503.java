package boogle.entities.managers;

import boogle.Boogle;
import boogle.entities.Author;
import boogle.entities.Book;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Class for managing Book entities
 */
public class BookManager {

    /**
     * Returns Book entities
     * @return Book entities
     */
    public static List<Book> getAll() {
        EntityManager em = Boogle.getInstance().getEmf().createEntityManager();
        try {
            Query query = em.createNamedQuery("Book.findAll");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Insert new Book entity to database
     * @param book Book entity
     * @throws java.lang.Exception
     */
    public static void insert(Book book) throws Exception {
        EntityManager em = Boogle.getInstance().getEmf().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(book);
            et.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Updates Book entity in database
     * @param book Book entity
     * @throws java.lang.Exception
     */
    public static void update(Book book) throws Exception {
        EntityManager em = Boogle.getInstance().getEmf().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<Author> authorCollection = new ArrayList<Author>();
            for (Author a : book.getAuthorCollection()) {
                Author authorToAdd = em.getReference(Author.class, a.getId());
                authorCollection.add(authorToAdd);
            }
            book.setAuthorCollection(authorCollection);
            em.merge(book);
            for (Author a : book.getAuthorCollection()) {
                if (!a.getBookCollection().contains(book)) a.getBookCollection().add(book);
                em.merge(a);
            }
            et.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Deletes Book entity from database
     * @param book Book entity
     * @throws java.lang.Exception
     */
    public static void delete(Book book) throws Exception {
        EntityManager em = Boogle.getInstance().getEmf().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Book b = em.getReference(Book.class, book.getId());
            et.begin();
            for (Author a : book.getAuthorCollection()) {
                a.getBookCollection().remove(book);
                em.merge(a);
            }
            b.setAuthorCollection(new ArrayList<Author>());
            em.merge(b);
            em.remove(b);
            et.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Returns filtered Book entities
     * @param filter filter
     * @return filtered Book entities
     */
    public static List<Book> getFiltered(String filter) {
        filter = filter.toLowerCase();
        List<Book> list = getAll();
        List<Book> newList = new ArrayList<Book>();
        for (Book b : list) {
            for (Author a : b.getAuthorCollection()) {
                if (a.getFirstName().toLowerCase().contains(filter) || a.getLastName().toLowerCase().contains(filter) || String.valueOf(a.getBirthYear()).toLowerCase().contains(filter)) {
                    if (!newList.contains(b)) newList.add(b);
                }
            }
            if (b.getName().toLowerCase().contains(filter) || b.getPublisher().toLowerCase().contains(filter) || b.getIsbn().toLowerCase().contains(filter) || b.getDescription().toLowerCase().contains(filter) || String.valueOf(b.getReleaseDate()).toLowerCase().contains(filter)) {
                if (!newList.contains(b)) newList.add(b);
            }
        }
        return newList;
    }
}
