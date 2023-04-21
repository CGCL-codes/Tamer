package oopex.hibernate3.nat.relationships;

import java.util.Collection;
import oopex.hibernate3.nat.relationships.model.Order;
import oopex.hibernate3.nat.relationships.model.OrderItem;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneToManyDependentMain {

    private static final Logger LOGGER = LoggerFactory.getLogger("oopex.sample");

    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try {
            LOGGER.info("*** insert ***");
            insert(sessionFactory);
            LOGGER.info("*** query ***");
            query(sessionFactory);
            LOGGER.info("*** update ***");
            update(sessionFactory);
            LOGGER.info("*** query ***");
            query(sessionFactory);
            LOGGER.info("*** delete ***");
            delete(sessionFactory);
        } finally {
            sessionFactory.close();
            LOGGER.info("*** finished ***");
        }
    }

    private static void insert(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Order order = new Order();
            order.setClient("Henry Ford");
            session.save(order);
            OrderItem item1 = new OrderItem();
            item1.setId(1001);
            item1.setOrderId(order.getId());
            item1.setArticle("wheel");
            item1.setQuantity(100);
            item1.setPrice(10);
            item1.setOrder(order);
            order.getItems().add(item1);
            OrderItem item2 = new OrderItem();
            item2.setId(1002);
            item2.setOrderId(order.getId());
            item2.setArticle("front window");
            item2.setQuantity(30);
            item2.setPrice(70);
            item2.setOrder(order);
            order.getItems().add(item2);
            session.getTransaction().commit();
        } finally {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    private static void query(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("from Order");
            Collection<Order> list = (Collection<Order>) query.list();
            for (Order order : list) {
                LOGGER.info("Found: " + order);
                for (OrderItem item : order.getItems()) {
                    LOGGER.info("  with item: " + item);
                }
            }
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    private static void update(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Order");
            Collection<Order> list = (Collection<Order>) query.list();
            for (Order order : list) {
                for (OrderItem item : order.getItems()) {
                    item.setPrice(item.getPrice() * 2);
                }
            }
            session.getTransaction().commit();
        } finally {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            session.close();
        }
    }

    private static void delete(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM OrderItem");
            query.executeUpdate();
            query = session.createQuery("DELETE FROM Order");
            query.executeUpdate();
            session.getTransaction().commit();
        } finally {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            session.close();
        }
    }
}
