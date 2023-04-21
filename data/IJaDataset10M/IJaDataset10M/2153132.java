package test;

import java.util.Date;
import org.hibernate.Session;
import com.loveazure.util.HibernateUtil;

public class EventManager {

    public static void main(String[] args) {
        EventManager mgr = new EventManager();
        mgr.createAndStoreEvent("My Event", new Date());
        HibernateUtil.getSessionFactory().close();
    }

    private void createAndStoreEvent(String title, Date theDate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.getTransaction().commit();
    }
}
