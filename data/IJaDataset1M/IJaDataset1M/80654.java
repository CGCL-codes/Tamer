package fr.devcoop.jee.tp8;

import fr.devcoop.jee.tp8.DAOException;
import fr.devcoop.jee.tp8.impl.BookDAOJPAImpl;
import fr.devcoop.jee.tp8.impl.DVDDAOJPAImpl;
import fr.devcoop.jee.tp8.impl.PersonDAOJPAImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author lfo
 */
public abstract class TestAbs {

    protected static EntityManager entityManager;

    protected static PersonDAOJPAImpl personDAO;

    protected static BookDAOJPAImpl bookDAO;

    protected static DVDDAOJPAImpl dvdDAO;

    @BeforeClass
    public static void init() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jee-tp8-pu");
        entityManager = emf.createEntityManager();
        personDAO = new PersonDAOJPAImpl(entityManager);
        bookDAO = new BookDAOJPAImpl(entityManager);
        dvdDAO = new DVDDAOJPAImpl(entityManager);
    }

    @AfterClass
    public static void close() throws DAOException {
        entityManager.close();
    }
}
