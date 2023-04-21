package pl.nni.msz.db.interfaces.dbi;

import java.util.Collection;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.nni.msz.db.SessionFactory;
import pl.nni.msz.db.Simulations;
import pl.nni.msz.db.Users;

public class GetAll {

    public Collection users() {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from Users user order by user.login asc");
            tx.commit();
            Collection users = q.list();
            return users;
        } catch (ObjectNotFoundException onfe) {
            return null;
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Collection economyT(Simulations simulation) {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from Economies economy where (economy.simulations = :id) order by economy.t asc");
            q.setInteger("id", simulation.getId());
            tx.commit();
            Collection economy = q.list();
            return economy;
        } catch (ObjectNotFoundException onfe) {
            return null;
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Collection agents(Simulations simulation) {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from Agents agent where (agent.simulations = :id) order by agent.id asc");
            q.setInteger("id", simulation.getId());
            tx.commit();
            Collection agents = q.list();
            return agents;
        } catch (ObjectNotFoundException onfe) {
            return null;
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Collection companiesNoSamples(Simulations simulation) {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Query q = session.createQuery("from Companies company where (company.sample = false) and (company.simulations = :id) and (company.sample = false)");
            q.setInteger("id", simulation.getId());
            Collection companies = q.list();
            return companies;
        } catch (ObjectNotFoundException onfe) {
            return null;
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Collection agentsNoSamples(Simulations simulation) {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from Agents agent where (agent.simulations = :id and agent.sample = false) order by agent.id asc");
            q.setInteger("id", simulation.getId());
            tx.commit();
            Collection agents = q.list();
            return agents;
        } catch (ObjectNotFoundException onfe) {
            return null;
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Collection simulationsUser(Users user) {
        Session session = null;
        try {
            session = SessionFactory.currentSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from Simulations simulation where (simulation.users = :user) order by simulation.id asc");
            q.setString("user", user.getLogin());
            tx.commit();
            Collection simulations = q.list();
            return simulations;
        } catch (ObjectNotFoundException onfe) {
            return null;
        } catch (HibernateException e) {
            System.err.println("Hibernate Exception" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
