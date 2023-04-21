package br.org.sd.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import br.org.sd.model.Account;
import br.org.sd.model.Address;
import br.org.sd.model.AdminUser;
import br.org.sd.model.Bill;
import br.org.sd.model.CommonUser;
import br.org.sd.model.Council;
import br.org.sd.model.GraduationType;
import br.org.sd.model.LegalPerson;
import br.org.sd.model.Parcel;
import br.org.sd.model.Person;
import br.org.sd.model.Phone;
import br.org.sd.model.PhysicPerson;
import br.org.sd.model.ProdRefValue;
import br.org.sd.model.Product;
import br.org.sd.model.ProductType;
import br.org.sd.model.State;
import br.org.sd.model.Statistician;
import br.org.sd.model.StatisticianType;
import br.org.sd.model.University;
import br.org.sd.model.User;

public class HibernateUtil {

    private static ThreadLocal<Session> sessionTL = new ThreadLocal<Session>();

    private static ThreadLocal<Transaction> transactionTL = new ThreadLocal<Transaction>();

    private static AnnotationConfiguration cfg;

    private static SessionFactory factory;

    static {
        cfg = new AnnotationConfiguration();
        cfg.addAnnotatedClass(GraduationType.class);
        cfg.addAnnotatedClass(CommonUser.class);
        cfg.addAnnotatedClass(AdminUser.class);
        cfg.addAnnotatedClass(Address.class);
        cfg.addAnnotatedClass(Person.class);
        cfg.addAnnotatedClass(PhysicPerson.class);
        cfg.addAnnotatedClass(LegalPerson.class);
        cfg.addAnnotatedClass(Phone.class);
        cfg.addAnnotatedClass(Council.class);
        cfg.addAnnotatedClass(State.class);
        cfg.addAnnotatedClass(Statistician.class);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(StatisticianType.class);
        cfg.addAnnotatedClass(University.class);
        cfg.addAnnotatedClass(Phone.class);
        cfg.addAnnotatedClass(Product.class);
        cfg.addAnnotatedClass(ProductType.class);
        cfg.addAnnotatedClass(Parcel.class);
        cfg.addAnnotatedClass(Bill.class);
        cfg.addAnnotatedClass(ProdRefValue.class);
        cfg.addAnnotatedClass(Account.class);
        factory = cfg.buildSessionFactory();
    }

    public static Session getSession() {
        Session session = sessionTL.get();
        if (session == null || !session.isOpen()) {
            System.out.println("ABRINDO SESSAO DO HIBERNATE PARA " + Thread.currentThread().getId());
            System.out.println("COMECANDO TRANSACAO DO HIBERNATE PARA " + Thread.currentThread().getId());
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            sessionTL.set(session);
            transactionTL.set(transaction);
        }
        return session;
    }

    public static void close() {
        System.out.println(Thread.currentThread().getId());
        Session session = sessionTL.get();
        Transaction transaction = transactionTL.get();
        if (transaction != null) {
            System.out.println("COMITANDO TRANSACAO DO HIBERNATE PARA " + Thread.currentThread().getId());
            transaction.commit();
        }
        if (session != null && session.isOpen()) {
            System.out.println("FECHANDO SESSAO DO HIBERNATEPARA " + Thread.currentThread().getId());
            session.close();
        }
        sessionTL.set(null);
        transactionTL.set(null);
    }

    public static void rollback() {
        System.out.println("Rolling back transaction...");
    }

    public static void create() {
        new SchemaExport(cfg).create(true, true);
    }
}
