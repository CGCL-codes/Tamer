package testhibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;

public class FirstExample {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Session session = null;
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("OK");
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            System.out.println("Inserting Record:");
            User user = new User();
            user.setUserid(38);
            user.setUsername("zhangshan4");
            user.setPassword("password14");
            session.saveOrUpdate(user);
            session.persist(user);
            tx.commit();
            System.out.println("Done!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }
}
