package hibernate.hello;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import junit.framework.TestCase;
import hibernate.hello.*;

public class FirstExample extends TestCase {

    public void createRecords() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        for (int i = 0; i < 3; i++) {
            Message message = new Message("Hello World " + i);
            session.save(message);
        }
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }

    public void doProcess() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Message> messages = new ArrayList<Message>(session.createQuery("from Message").list());
        for (Message m : messages) {
            if (m.getId() == 1) {
                m.setMessage("nahh nahh");
                session.update(m);
            }
            System.out.println("message: " + m.getId() + " -- " + m.getMessage());
        }
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }

    public void doProcess2() {
        SessionFactory sessions = new Configuration().addClass(hibernate.hello.Message.class).setProperties(System.getProperties()).buildSessionFactory();
        sessions.getCurrentSession().beginTransaction();
        List<Message> messages = new ArrayList<Message>(sessions.getCurrentSession().createQuery("from Message").list());
        for (Message m : messages) {
            if (m.getId() == 1) {
                m.setMessage("nahh nahh");
                sessions.getCurrentSession().update(m);
            }
            System.out.println("message: " + m.getId() + " -- " + m.getMessage());
        }
        sessions.getCurrentSession().getTransaction().commit();
        sessions.close();
    }

    public void doProcess3() {
        Configuration cfg = new Configuration();
        cfg.addResource("hello/Message.hbm.xml");
        cfg.setProperties(System.getProperties());
        SessionFactory sessions = cfg.buildSessionFactory();
        sessions.getCurrentSession().beginTransaction();
        List<Message> messages = new ArrayList<Message>(sessions.getCurrentSession().createQuery("from Message").list());
        for (Message m : messages) {
            if (m.getId() == 1) {
                m.setMessage("nahh nahh");
                sessions.getCurrentSession().update(m);
            }
            System.out.println("message: " + m.getId() + " -- " + m.getMessage());
        }
        sessions.getCurrentSession().getTransaction().commit();
        sessions.close();
    }
}
