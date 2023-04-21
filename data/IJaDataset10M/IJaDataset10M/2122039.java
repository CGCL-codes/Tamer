package orm.impl;

import org.orm.*;
import org.hibernate.Query;
import java.util.List;
import orm.*;

public class Tan_subsectorDAOImpl implements orm.dao.Tan_subsectorDAO {

    public Tan_subsector loadTan_subsectorByORMID(int sub_id) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return loadTan_subsectorByORMID(session, sub_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector getTan_subsectorByORMID(int sub_id) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return getTan_subsectorByORMID(session, sub_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector loadTan_subsectorByORMID(int sub_id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return loadTan_subsectorByORMID(session, sub_id, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector getTan_subsectorByORMID(int sub_id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return getTan_subsectorByORMID(session, sub_id, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector loadTan_subsectorByORMID(PersistentSession session, int sub_id) throws PersistentException {
        try {
            return (Tan_subsector) session.load(orm.Tan_subsector.class, new Integer(sub_id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector getTan_subsectorByORMID(PersistentSession session, int sub_id) throws PersistentException {
        try {
            return (Tan_subsector) session.get(orm.Tan_subsector.class, new Integer(sub_id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector loadTan_subsectorByORMID(PersistentSession session, int sub_id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Tan_subsector) session.load(orm.Tan_subsector.class, new Integer(sub_id), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector getTan_subsectorByORMID(PersistentSession session, int sub_id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Tan_subsector) session.get(orm.Tan_subsector.class, new Integer(sub_id), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector[] listTan_subsectorByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return listTan_subsectorByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector[] listTan_subsectorByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return listTan_subsectorByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector[] listTan_subsectorByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tan_subsector as Tan_subsector");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            List list = query.list();
            return (Tan_subsector[]) list.toArray(new Tan_subsector[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector[] listTan_subsectorByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tan_subsector as Tan_subsector");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            query.setLockMode("this", lockMode);
            List list = query.list();
            return (Tan_subsector[]) list.toArray(new Tan_subsector[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector loadTan_subsectorByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return loadTan_subsectorByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector loadTan_subsectorByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return loadTan_subsectorByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector loadTan_subsectorByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        Tan_subsector[] tan_subsectors = listTan_subsectorByQuery(session, condition, orderBy);
        if (tan_subsectors != null && tan_subsectors.length > 0) return tan_subsectors[0]; else return null;
    }

    public Tan_subsector loadTan_subsectorByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        Tan_subsector[] tan_subsectors = listTan_subsectorByQuery(session, condition, orderBy, lockMode);
        if (tan_subsectors != null && tan_subsectors.length > 0) return tan_subsectors[0]; else return null;
    }

    public static java.util.Iterator iterateTan_subsectorByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return iterateTan_subsectorByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator iterateTan_subsectorByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = orm.AnotacionPersistentManager.instance().getSession();
            return iterateTan_subsectorByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator iterateTan_subsectorByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tan_subsector as Tan_subsector");
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

    public static java.util.Iterator iterateTan_subsectorByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From orm.Tan_subsector as Tan_subsector");
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

    public Tan_subsector createTan_subsector() {
        return new orm.Tan_subsector();
    }

    public boolean save(orm.Tan_subsector tan_subsector) throws PersistentException {
        try {
            orm.AnotacionPersistentManager.instance().saveObject(tan_subsector);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean delete(orm.Tan_subsector tan_subsector) throws PersistentException {
        try {
            orm.AnotacionPersistentManager.instance().deleteObject(tan_subsector);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate(orm.Tan_subsector tan_subsector) throws PersistentException {
        try {
            if (tan_subsector.getTan_cursocur() != null) {
                tan_subsector.getTan_cursocur().tan_subsector.remove(tan_subsector);
            }
            orm.Tan_anotacion[] lTan_anotacions = tan_subsector.tan_anotacion.toArray();
            for (int i = 0; i < lTan_anotacions.length; i++) {
                lTan_anotacions[i].setTan_subsectorsub(null);
            }
            return delete(tan_subsector);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate(orm.Tan_subsector tan_subsector, org.orm.PersistentSession session) throws PersistentException {
        try {
            if (tan_subsector.getTan_cursocur() != null) {
                tan_subsector.getTan_cursocur().tan_subsector.remove(tan_subsector);
            }
            orm.Tan_anotacion[] lTan_anotacions = tan_subsector.tan_anotacion.toArray();
            for (int i = 0; i < lTan_anotacions.length; i++) {
                lTan_anotacions[i].setTan_subsectorsub(null);
            }
            try {
                session.delete(tan_subsector);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean refresh(orm.Tan_subsector tan_subsector) throws PersistentException {
        try {
            orm.AnotacionPersistentManager.instance().getSession().refresh(tan_subsector);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean evict(orm.Tan_subsector tan_subsector) throws PersistentException {
        try {
            orm.AnotacionPersistentManager.instance().getSession().evict(tan_subsector);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public Tan_subsector loadTan_subsectorByCriteria(Tan_subsectorCriteria tan_subsectorCriteria) {
        Tan_subsector[] tan_subsectors = listTan_subsectorByCriteria(tan_subsectorCriteria);
        if (tan_subsectors == null || tan_subsectors.length == 0) {
            return null;
        }
        return tan_subsectors[0];
    }

    public Tan_subsector[] listTan_subsectorByCriteria(Tan_subsectorCriteria tan_subsectorCriteria) {
        return tan_subsectorCriteria.listTan_subsector();
    }
}
