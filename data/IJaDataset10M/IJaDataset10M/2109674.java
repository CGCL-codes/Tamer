package br.com.rasoft.academix.persistence.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe que representa uma infra-estrutura de c�digo para funcionamento do
 * Hibernate. Nesta classe ser� criado um objeto SessionFactory a partir do qual
 * ser�o criadas v�rias Session's.
 * 
 * @author Adriano G. Ferreira
 * 
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    /**
	 * Obtem um sessionFactory
	 * @return SessionFactory
	 */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
	 * Obtem uma sessão
	 * @return Session
	 */
    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
