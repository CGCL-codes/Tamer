package dao.impl;

import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import bean.FacturaBean;
import dao.FacturaDAO;

public class FacturaDAOImpl implements FacturaDAO {

    private static FacturaDAOImpl instancia = null;

    @SuppressWarnings("unused")
    private static SessionFactory sf = null;

    private FacturaDAOImpl() {
    }

    public static FacturaDAOImpl getInstancia() {
        if (instancia == null) {
            sf = HibernateUtil.getSessionFactory();
            instancia = new FacturaDAOImpl();
        }
        return instancia;
    }

    @Override
    public void insertarFactura(FacturaBean factura) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.merge(factura);
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
}
