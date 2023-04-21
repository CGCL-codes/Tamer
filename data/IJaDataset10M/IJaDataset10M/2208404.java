package dao.impl;

import hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import bean.CondicionCompraBean;
import dao.CondicionCompraDAO;
import exceptions.BuscarObjetoException;

public class CondicionCompraDAOImpl implements CondicionCompraDAO {

    private static CondicionCompraDAOImpl instancia = null;

    private static SessionFactory sf = null;

    private CondicionCompraDAOImpl() {
    }

    public static CondicionCompraDAOImpl getInstancia() {
        if (instancia == null) {
            sf = HibernateUtil.getSessionFactory();
            instancia = new CondicionCompraDAOImpl();
        }
        return instancia;
    }

    public CondicionCompraBean findByID(Integer id) throws BuscarObjetoException {
        try {
            CondicionCompraBean condicion = new CondicionCompraBean();
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.load(condicion, id);
            tx.commit();
            session.close();
            return condicion;
        } catch (Exception e) {
            throw new BuscarObjetoException("No existe la Condici�n de Compra con ID: " + id);
        }
    }

    public void grabarCondicionCompra(CondicionCompraBean cond) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(cond);
        session.flush();
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    public List<CondicionCompraBean> getAll() {
        List<CondicionCompraBean> condicionesCompra;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        condicionesCompra = session.createQuery("from CondicionCompraBean").list();
        tx.commit();
        session.close();
        return condicionesCompra;
    }

    @Override
    public List<CondicionCompraBean> getCondicionesXProveedor(int idProveedor) {
        List<CondicionCompraBean> condicionesCompra = new ArrayList<CondicionCompraBean>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query consulta = session.createQuery("from CondicionCompraBean cc where cc.proveedor.idProveedor = :idProveedor");
        consulta.setInteger("idProveedor", idProveedor);
        condicionesCompra = consulta.list();
        tx.commit();
        session.close();
        return condicionesCompra;
    }
}
