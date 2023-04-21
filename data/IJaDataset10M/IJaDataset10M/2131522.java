package jogovelha.banco;

import jogovelha.dados.Jogada;
import jogovelha.dados.Jogo;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author manchini
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    private static final AnnotationConfiguration cfg;

    static {
        try {
            cfg = new AnnotationConfiguration();
            getCfg().addAnnotatedClass(Jogada.class);
            getCfg().addAnnotatedClass(Jogo.class);
            sessionFactory = getCfg().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @return the cfg
     */
    public static AnnotationConfiguration getCfg() {
        return cfg;
    }
}
