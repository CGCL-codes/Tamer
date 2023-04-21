package edu.scripps.fl.hibernate;

import java.net.URL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import edu.scripps.fl.curves.Curve;

public class HibernateUtil {

    private static final String KEY = "Curves";

    public static AnnotationConfiguration getAnnotationConfiguration(URL url) {
        AnnotationConfiguration annot = new AnnotationConfiguration();
        annot.configure(url);
        annot.addAnnotatedClass(Curve.class);
        return annot;
    }

    public static Session getSession() {
        return HibernateStaticService.getHibernateSession(KEY);
    }

    public static SessionFactory getSessionFactory() {
        return HibernateStaticService.getHibernateSessionFactory(KEY);
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateStaticService.setHibernateSessionFactory(KEY, sessionFactory);
    }

    public static void build(URL url) {
        AnnotationConfiguration config = getAnnotationConfiguration(url);
        HibernateUtil.setSessionFactory(config.buildSessionFactory());
    }
}
