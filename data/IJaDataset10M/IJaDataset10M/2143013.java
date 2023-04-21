package integrationtier.hibernate;

import integrationtier.AbstractDAOFactory;
import integrationtier.ITitulDAO;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author lucky
 */
public class HibernateDAOFactory extends AbstractDAOFactory {

    private String db = "";

    private int port = 4040;

    private String user = "";

    private String pass = "";

    private SessionFactory sessionFactory;

    public HibernateDAOFactory() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public ITitulDAO getTitulDAO() {
        return new TitulDAO(sessionFactory.getCurrentSession());
    }
}
