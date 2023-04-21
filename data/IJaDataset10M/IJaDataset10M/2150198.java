package eu.coaxion.gnumedj.postgre.v14;

import org.orm.*;
import org.hibernate.Query;
import java.util.List;

/**
 * links test result evaluation norms to tests
 */
public class Lnk_tst2norm {

    public Lnk_tst2norm() {
    }

    public static Lnk_tst2norm loadLnk_tst2normByORMID(int id) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadLnk_tst2normByORMID(session, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm getLnk_tst2normByORMID(int id) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return getLnk_tst2normByORMID(session, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm loadLnk_tst2normByORMID(int id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadLnk_tst2normByORMID(session, id, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm getLnk_tst2normByORMID(int id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return getLnk_tst2normByORMID(session, id, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm loadLnk_tst2normByORMID(PersistentSession session, int id) throws PersistentException {
        try {
            return (Lnk_tst2norm) session.load(eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm.class, new Integer(id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm getLnk_tst2normByORMID(PersistentSession session, int id) throws PersistentException {
        try {
            return (Lnk_tst2norm) session.get(eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm.class, new Integer(id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm loadLnk_tst2normByORMID(PersistentSession session, int id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Lnk_tst2norm) session.load(eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm.class, new Integer(id), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm getLnk_tst2normByORMID(PersistentSession session, int id, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Lnk_tst2norm) session.get(eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm.class, new Integer(id), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm[] listLnk_tst2normByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return listLnk_tst2normByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm[] listLnk_tst2normByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return listLnk_tst2normByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm[] listLnk_tst2normByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm as Lnk_tst2norm");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            List list = query.list();
            return (Lnk_tst2norm[]) list.toArray(new Lnk_tst2norm[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm[] listLnk_tst2normByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm as Lnk_tst2norm");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            query.setLockMode("this", lockMode);
            List list = query.list();
            return (Lnk_tst2norm[]) list.toArray(new Lnk_tst2norm[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm loadLnk_tst2normByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadLnk_tst2normByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm loadLnk_tst2normByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadLnk_tst2normByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Lnk_tst2norm loadLnk_tst2normByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        Lnk_tst2norm[] lnk_tst2norms = listLnk_tst2normByQuery(session, condition, orderBy);
        if (lnk_tst2norms != null && lnk_tst2norms.length > 0) return lnk_tst2norms[0]; else return null;
    }

    public static Lnk_tst2norm loadLnk_tst2normByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        Lnk_tst2norm[] lnk_tst2norms = listLnk_tst2normByQuery(session, condition, orderBy, lockMode);
        if (lnk_tst2norms != null && lnk_tst2norms.length > 0) return lnk_tst2norms[0]; else return null;
    }

    public static java.util.Iterator<Lnk_tst2norm> iterateLnk_tst2normByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return iterateLnk_tst2normByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator<Lnk_tst2norm> iterateLnk_tst2normByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return iterateLnk_tst2normByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator<Lnk_tst2norm> iterateLnk_tst2normByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm as Lnk_tst2norm");
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

    public static java.util.Iterator<Lnk_tst2norm> iterateLnk_tst2normByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm as Lnk_tst2norm");
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

    public static Lnk_tst2norm loadLnk_tst2normByCriteria(Lnk_tst2normCriteria lnk_tst2normCriteria) {
        Lnk_tst2norm[] lnk_tst2norms = listLnk_tst2normByCriteria(lnk_tst2normCriteria);
        if (lnk_tst2norms == null || lnk_tst2norms.length == 0) {
            return null;
        }
        return lnk_tst2norms[0];
    }

    public static Lnk_tst2norm[] listLnk_tst2normByCriteria(Lnk_tst2normCriteria lnk_tst2normCriteria) {
        return lnk_tst2normCriteria.listLnk_tst2norm();
    }

    public static Lnk_tst2norm createLnk_tst2norm() {
        return new eu.coaxion.gnumedj.postgre.v14.Lnk_tst2norm();
    }

    public boolean save() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().saveObject(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean delete() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().deleteObject(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean refresh() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession().refresh(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean evict() throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession().evict(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate() throws PersistentException {
        try {
            if (getId_test() != null) {
                getId_test().getLnk_tst2norm().remove(this);
            }
            return delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate(org.orm.PersistentSession session) throws PersistentException {
        try {
            if (getId_test() != null) {
                getId_test().getLnk_tst2norm().remove(this);
            }
            try {
                session.delete(this);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    private int pk_audit;

    private int row_version;

    private java.sql.Timestamp modified_when;

    private String modified_by;

    private int id;

    private eu.coaxion.gnumedj.postgre.v14.Test_type id_test;

    private int id_norm;

    public void setPk_audit(int value) {
        this.pk_audit = value;
    }

    public int getPk_audit() {
        return pk_audit;
    }

    public void setRow_version(int value) {
        this.row_version = value;
    }

    public int getRow_version() {
        return row_version;
    }

    public void setModified_when(java.sql.Timestamp value) {
        this.modified_when = value;
    }

    public java.sql.Timestamp getModified_when() {
        return modified_when;
    }

    public void setModified_by(String value) {
        this.modified_by = value;
    }

    public String getModified_by() {
        return modified_by;
    }

    private void setId(int value) {
        this.id = value;
    }

    public int getId() {
        return id;
    }

    public int getORMID() {
        return getId();
    }

    /**
	 * the norm to apply to the linked test
	 */
    public void setId_norm(int value) {
        this.id_norm = value;
    }

    /**
	 * the norm to apply to the linked test
	 */
    public int getId_norm() {
        return id_norm;
    }

    public void setId_test(eu.coaxion.gnumedj.postgre.v14.Test_type value) {
        this.id_test = value;
    }

    public eu.coaxion.gnumedj.postgre.v14.Test_type getId_test() {
        return id_test;
    }

    public String toString() {
        return String.valueOf(getId());
    }
}
