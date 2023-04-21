package pe.lamborgini.dao;

import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import pe.lamborgini.domain.mapping.HeladosEntregadoRecibido;
import pe.lamborgini.util.AppUtil;

/**
 *
 * @author Cesardl
 */
public class HeladosEntregadoRecibidoDAO {

    public HeladosEntregadoRecibido getHeladosEntregadoRecibido(int id_heladero) {
        Session session = AppUtil.getSessionFactory().openSession();
        Transaction tx = null;
        HeladosEntregadoRecibido her = null;
        try {
            tx = session.beginTransaction();
            Criteria c = session.createCriteria(HeladosEntregadoRecibido.class).add(Restrictions.eq("heladero.idHeladero", id_heladero)).add(Restrictions.eq("fecha", new Date()));
            her = (HeladosEntregadoRecibido) c.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            System.err.println("ERROR: HeladosEntregadoRecibidoDAO.getHeladosEntregadoRecibido " + e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return her;
    }

    public void insertHeladosEntregadoRecibido(HeladosEntregadoRecibido her) {
        Session session = AppUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(her);
            tx.commit();
        } catch (Exception e) {
            System.err.println("Error: HeladosEntregadoRecibidoDAO.insertHeladosEntregadoRecibido " + e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }
}
