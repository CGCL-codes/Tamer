package hibernate.test;

import org.hibernate.Session;
import hibernate.classes.*;
import hibernate.util.HibernateUtil;

public class Loader {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();
        Serie serie = (Serie) s.load(Serie.class, 1);
        System.out.println("Buscando serie: " + serie.getTitle());
        System.out.println("Buscando description: " + serie.getDescription());
        System.out.println("Buscando email: " + serie.getEmail());
        System.out.println("Buscando url: " + serie.getUrl());
        System.out.println("Buscando levels: " + serie.getLevels());
        s.getTransaction().commit();
    }
}
