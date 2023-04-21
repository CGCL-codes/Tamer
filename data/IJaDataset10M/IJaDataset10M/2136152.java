package orm.impl;

import org.orm.*;
import org.hibernate.Query;
import java.util.List;
import orm.*;

public class Tdg_anotadorDAOImpl implements orm.dao.Tdg_anotadorDAO {

    public Tdg_anotador loadTdg_anotadorByORMID(String an_rut) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return loadTdg_anotadorByORMID(session, an_rut);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador getTdg_anotadorByORMID(String an_rut) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return getTdg_anotadorByORMID(session, an_rut);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador loadTdg_anotadorByORMID(String an_rut, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return loadTdg_anotadorByORMID(session, an_rut, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador getTdg_anotadorByORMID(String an_rut, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return getTdg_anotadorByORMID(session, an_rut, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador loadTdg_anotadorByORMID(PersistentSession session, String an_rut) throws PersistentException {
        try {
            return (Tdg_anotador) session.load(orm.Tdg_anotador.class, an_rut);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador getTdg_anotadorByORMID(PersistentSession session, String an_rut) throws PersistentException {
        try {
            return (Tdg_anotador) session.get(orm.Tdg_anotador.class, an_rut);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador loadTdg_anotadorByORMID(PersistentSession session, String an_rut, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Tdg_anotador) session.load(orm.Tdg_anotador.class, an_rut, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador getTdg_anotadorByORMID(PersistentSession session, String an_rut, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Tdg_anotador) session.get(orm.Tdg_anotador.class, an_rut, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador[] listTdg_anotadorByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return listTdg_anotadorByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador[] listTdg_anotadorByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return listTdg_anotadorByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador[] listTdg_anotadorByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tdg_anotador as Tdg_anotador");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            List list = query.list();
            return (Tdg_anotador[]) list.toArray(new Tdg_anotador[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador[] listTdg_anotadorByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tdg_anotador as Tdg_anotador");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            query.setLockMode("this", lockMode);
            List list = query.list();
            return (Tdg_anotador[]) list.toArray(new Tdg_anotador[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador loadTdg_anotadorByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return loadTdg_anotadorByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador loadTdg_anotadorByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return loadTdg_anotadorByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador loadTdg_anotadorByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        Tdg_anotador[] tdg_anotadors = listTdg_anotadorByQuery(session, condition, orderBy);
        if (tdg_anotadors != null && tdg_anotadors.length > 0) return tdg_anotadors[0]; else return null;
    }

    public Tdg_anotador loadTdg_anotadorByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        Tdg_anotador[] tdg_anotadors = listTdg_anotadorByQuery(session, condition, orderBy, lockMode);
        if (tdg_anotadors != null && tdg_anotadors.length > 0) return tdg_anotadors[0]; else return null;
    }

    public static java.util.Iterator iterateTdg_anotadorByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return iterateTdg_anotadorByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator iterateTdg_anotadorByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm._18nov2PersistentManager.instance().getSession();
            return iterateTdg_anotadorByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator iterateTdg_anotadorByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tdg_anotador as Tdg_anotador");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            return query.iterate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator iterateTdg_anotadorByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tdg_anotador as Tdg_anotador");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            query.setLockMode("this", lockMode);
            return query.iterate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador createTdg_anotador() {
        return new orm.Tdg_anotador();
    }

    public boolean save(orm.Tdg_anotador tdg_anotador) throws PersistentException {
        try {
            orm._18nov2PersistentManager.instance().saveObject(tdg_anotador);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean delete(orm.Tdg_anotador tdg_anotador) throws PersistentException {
        try {
            orm._18nov2PersistentManager.instance().deleteObject(tdg_anotador);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate(orm.Tdg_anotador tdg_anotador) throws PersistentException {
        try {
            if (tdg_anotador.getAn_id_subsector() != null) {
                tdg_anotador.getAn_id_subsector().tdg_anotador.remove(tdg_anotador);
            }
            orm.Tdg_anotacion[] lTdg_anotacions = tdg_anotador.tdg_anotacion.toArray();
            for (int i = 0; i < lTdg_anotacions.length; i++) {
                lTdg_anotacions[i].setAt_rut_anotador(null);
            }
            return delete(tdg_anotador);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate(orm.Tdg_anotador tdg_anotador, org.orm.PersistentSession session) throws PersistentException {
        try {
            if (tdg_anotador.getAn_id_subsector() != null) {
                tdg_anotador.getAn_id_subsector().tdg_anotador.remove(tdg_anotador);
            }
            orm.Tdg_anotacion[] lTdg_anotacions = tdg_anotador.tdg_anotacion.toArray();
            for (int i = 0; i < lTdg_anotacions.length; i++) {
                lTdg_anotacions[i].setAt_rut_anotador(null);
            }
            try {
                session.delete(tdg_anotador);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean refresh(orm.Tdg_anotador tdg_anotador) throws PersistentException {
        try {
            orm._18nov2PersistentManager.instance().getSession().refresh(tdg_anotador);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean evict(orm.Tdg_anotador tdg_anotador) throws PersistentException {
        try {
            orm._18nov2PersistentManager.instance().getSession().evict(tdg_anotador);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tdg_anotador loadTdg_anotadorByCriteria(Tdg_anotadorCriteria tdg_anotadorCriteria) {
        Tdg_anotador[] tdg_anotadors = listTdg_anotadorByCriteria(tdg_anotadorCriteria);
        if (tdg_anotadors == null || tdg_anotadors.length == 0) {
            return null;
        }
        return tdg_anotadors[0];
    }

    public Tdg_anotador[] listTdg_anotadorByCriteria(Tdg_anotadorCriteria tdg_anotadorCriteria) {
        return tdg_anotadorCriteria.listTdg_anotador();
    }
}
