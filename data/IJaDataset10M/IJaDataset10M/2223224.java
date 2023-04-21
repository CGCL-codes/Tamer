package sirarq.bd;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import sirarq.dominio.Fotografia;
import sirarq.dominio.Yacimiento;

public class YacimientoDAO implements OperacionComunInterfaz<Yacimiento> {

    private SessionFactory sf = new Configuration().configure().buildSessionFactory();

    @Override
    public Integer insert(Yacimiento elemento) {
        if (sf != null) {
            System.out.println("Inserto!");
            Session s = sf.openSession();
            Transaction t = s.beginTransaction();
            Integer id = null;
            id = (Integer) s.save(elemento);
            t.commit();
            s.close();
            return id;
        }
        return null;
    }

    @Override
    public Yacimiento select(Integer id) {
        Yacimiento yacimiento = null;
        if (sf != null) {
            Session s = sf.openSession();
            yacimiento = (Yacimiento) s.get(Yacimiento.class, new Integer(id));
            s.close();
        }
        return yacimiento;
    }

    @Override
    public void delete(Integer id) {
        if (sf != null) {
            Session s = sf.openSession();
            Transaction t = s.beginTransaction();
            s.delete(this.select(id));
            t.commit();
            s.close();
        }
    }

    @Override
    public void update(Yacimiento elemento) {
        if (sf != null) {
            Session s = sf.openSession();
            Transaction t = s.beginTransaction();
            s.update(elemento);
            t.commit();
            s.close();
        }
    }

    @SuppressWarnings("rawtypes")
    public List<Yacimiento> list() {
        ArrayList<Yacimiento> yacimientos = null;
        if (sf != null) {
            Session s = sf.openSession();
            SQLQuery query = s.createSQLQuery("SELECT id FROM yacimiento");
            List yac = query.list();
            for (Object o : yac) {
                try {
                    if (yacimientos == null) yacimientos = new ArrayList<Yacimiento>(yac.size());
                    Yacimiento yacimiento = (Yacimiento) s.get(Yacimiento.class, (Integer) o);
                    yacimientos.add(yacimiento);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            s.close();
        }
        return yacimientos;
    }

    @Override
    public Integer count() {
        return null;
    }

    @Override
    public List<Yacimiento> find(Integer from, Integer to) {
        return null;
    }

    public void close_connection() {
        sf.close();
    }
}
