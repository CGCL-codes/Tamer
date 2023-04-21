package pl.o2.asdluki.oszwo.dao.impl;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import pl.o2.asdluki.oszwo.dao.Dao;
import pl.o2.asdluki.oszwo.dao.UserDao;
import pl.o2.asdluki.oszwo.model.User;

public class UserDaoImpl extends Dao implements UserDao {

    public User getUser(String login) {
        Session session = getSession();
        session.beginTransaction();
        User user = null;
        try {
            user = (User) session.createQuery("from User" + " where login = ?").setString(0, login).uniqueResult();
        } catch (HibernateException e) {
            session.beginTransaction().rollback();
            throw e;
        }
        session.beginTransaction().commit();
        if (user != null) session.refresh(user);
        return user;
    }

    public User getUser(int id) {
        Session session = getSession();
        session.beginTransaction().begin();
        User user;
        try {
            user = (User) session.get(User.class, id);
        } catch (HibernateException e) {
            session.beginTransaction().rollback();
            throw e;
        }
        session.beginTransaction().commit();
        if (user != null) session.refresh(user);
        return user;
    }

    public User getUser(String login, String pass) {
        Session session = getSession();
        session.beginTransaction().begin();
        User user;
        try {
            user = (User) session.createQuery("from User" + " where login = ? AND pass =  ?").setString(0, login).setString(1, pass).uniqueResult();
        } catch (HibernateException e) {
            session.beginTransaction().rollback();
            throw e;
        }
        session.beginTransaction().commit();
        if (user != null) session.refresh(user);
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        Session session = getSession();
        List users;
        session.beginTransaction().begin();
        try {
            users = session.createQuery("from User").list();
        } catch (HibernateException e) {
            session.beginTransaction().rollback();
            throw e;
        }
        session.beginTransaction().commit();
        return users;
    }

    public void removeUser(User user) {
        Session session = getSession();
        session.beginTransaction();
        try {
            session.delete(user);
        } catch (HibernateException e) {
            session.beginTransaction().rollback();
            throw e;
        }
        session.beginTransaction().commit();
    }

    public void saveUser(User user) {
        Session session = getSession();
        session.beginTransaction();
        try {
            session.save(user);
            session.flush();
        } catch (HibernateException e) {
            session.beginTransaction().rollback();
            throw e;
        }
        session.beginTransaction().commit();
    }

    @Override
    public boolean checkMail(String mail) {
        return false;
    }
}
