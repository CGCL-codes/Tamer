package com.softntic.meetmemanager.data.dao;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.classic.Session;
import org.hibernate.dialect.MySQL5Dialect;
import com.softntic.meetmemanager.data.bean.Conference;
import com.softntic.meetmemanager.data.bean.RegisteredUser;
import com.softntic.meetmemanager.data.bean.User;
import com.softntic.meetmemanager.data.bean.RegisteredUser.AccessLevel;
import com.softntic.meetmemanager.data.bean.User.Gender;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            AnnotationConfiguration cnf = new AnnotationConfiguration();
            cnf.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
            cnf.setProperty(Environment.URL, "jdbc:mysql://localhost/meetme");
            cnf.setProperty(Environment.USER, "meetme");
            cnf.setProperty(Environment.PASS, "meetme");
            cnf.setProperty(Environment.DIALECT, MySQL5Dialect.class.getName());
            cnf.setProperty(Environment.SHOW_SQL, "true");
            cnf.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            cnf.addAnnotatedClass(User.class);
            cnf.addAnnotatedClass(RegisteredUser.class);
            cnf.addAnnotatedClass(Conference.class);
            sessionFactory = cnf.buildSessionFactory();
            insertOneToManyExample();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static void insertOneToManyExample() {
        Session sess = getSession();
        User user;
        Set<User> users = new HashSet<User>();
        for (int i = 1; i < 10; i++) {
            user = new User();
            user.setLastName("user" + i);
            user.setGender(Gender.MISS);
            sess.save(user);
            users.add(user);
        }
        RegisteredUser ru = new RegisteredUser();
        ru.setLastName("Admin");
        ru.setPassword("admin");
        ru.setMail("admin");
        ru.setAccess(AccessLevel.ADMIN);
        sess.save(ru);
        sess.getTransaction().commit();
        sess = getSession();
        Conference conf2 = new Conference();
        conf2.setTitle("my Conf");
        conf2.setRoom(conf2.getRoom());
        conf2.setBooker(ru);
        conf2.setUsers(users);
        sess.save(conf2);
        Conference conf3 = new Conference();
        conf3.setTitle("Lalalalal");
        conf3.setRoom(conf3.getRoom());
        conf3.setBooker(ru);
        conf3.setUsers(users);
        sess.save(conf3);
        System.out.println(UserDao.getAllUsers(false).size());
        sess.getTransaction().commit();
    }

    public static Session getSession() {
        Session currentSession = getSessionFactory().getCurrentSession();
        if (!currentSession.isOpen()) {
            System.out.println("Session is not open!");
            currentSession = getSessionFactory().openSession();
        }
        if (!currentSession.getTransaction().isActive()) {
            currentSession.beginTransaction();
        }
        return currentSession;
    }
}
