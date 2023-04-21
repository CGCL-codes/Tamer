package data.dao;

import java.util.List;
import org.hibernate.Session;
import util.HibernateUtil;
import data.entities.User;
import data.entities.UserLogin;

public class UserDAO implements IUserDAO {

    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<User> users = null;
        try {
            users = session.createQuery("from User").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void saveUser(User user) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public User getUser(String login) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        User user = null;
        try {
            user = (User) session.createQuery("from User as user where user.login =?").setString(0, login).uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return user;
    }

    @Override
    public UserLogin getUserLogin(String login) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        UserLogin user = null;
        try {
            user = (UserLogin) session.createQuery("from UserLogin as user where user.login =?").setString(0, login).uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUserLogin(String login) {
        UserLogin userLogin = getUserLogin(login);
        if (userLogin != null) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                session.delete(userLogin);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }

    public void setUserLogin(UserLogin userLogin) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            session.save(userLogin);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public UserLogin createUserLogin(String login) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        UserLogin userLogin = new UserLogin(login);
        try {
            session.save(userLogin);
            session.getTransaction().commit();
            return userLogin;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }
}
