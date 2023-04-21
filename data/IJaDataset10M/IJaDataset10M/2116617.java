package eu.coaxion.gnumedj.postgre.v14;

import org.orm.*;
import org.hibernate.Query;
import java.util.List;

/**
 * collects data on hospitalisations of patients, reasons are linked via a link table
 */
public class Hospital_stay {

    public Hospital_stay() {
    }

    public static Hospital_stay loadHospital_stayByORMID(int pk) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadHospital_stayByORMID(session, pk);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay getHospital_stayByORMID(int pk) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return getHospital_stayByORMID(session, pk);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay loadHospital_stayByORMID(int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadHospital_stayByORMID(session, pk, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay getHospital_stayByORMID(int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return getHospital_stayByORMID(session, pk, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay loadHospital_stayByORMID(PersistentSession session, int pk) throws PersistentException {
        try {
            return (Hospital_stay) session.load(eu.coaxion.gnumedj.postgre.v14.Hospital_stay.class, new Integer(pk));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay getHospital_stayByORMID(PersistentSession session, int pk) throws PersistentException {
        try {
            return (Hospital_stay) session.get(eu.coaxion.gnumedj.postgre.v14.Hospital_stay.class, new Integer(pk));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay loadHospital_stayByORMID(PersistentSession session, int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Hospital_stay) session.load(eu.coaxion.gnumedj.postgre.v14.Hospital_stay.class, new Integer(pk), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay getHospital_stayByORMID(PersistentSession session, int pk, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            return (Hospital_stay) session.get(eu.coaxion.gnumedj.postgre.v14.Hospital_stay.class, new Integer(pk), lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay[] listHospital_stayByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return listHospital_stayByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay[] listHospital_stayByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return listHospital_stayByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay[] listHospital_stayByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Hospital_stay as Hospital_stay");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            List list = query.list();
            return (Hospital_stay[]) list.toArray(new Hospital_stay[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay[] listHospital_stayByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Hospital_stay as Hospital_stay");
        if (condition != null) sb.append(" Where ").append(condition);
        if (orderBy != null) sb.append(" Order By ").append(orderBy);
        try {
            Query query = session.createQuery(sb.toString());
            query.setLockMode("this", lockMode);
            List list = query.list();
            return (Hospital_stay[]) list.toArray(new Hospital_stay[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay loadHospital_stayByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadHospital_stayByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay loadHospital_stayByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return loadHospital_stayByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static Hospital_stay loadHospital_stayByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        Hospital_stay[] hospital_stays = listHospital_stayByQuery(session, condition, orderBy);
        if (hospital_stays != null && hospital_stays.length > 0) return hospital_stays[0]; else return null;
    }

    public static Hospital_stay loadHospital_stayByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        Hospital_stay[] hospital_stays = listHospital_stayByQuery(session, condition, orderBy, lockMode);
        if (hospital_stays != null && hospital_stays.length > 0) return hospital_stays[0]; else return null;
    }

    public static java.util.Iterator<Hospital_stay> iterateHospital_stayByQuery(String condition, String orderBy) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return iterateHospital_stayByQuery(session, condition, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator<Hospital_stay> iterateHospital_stayByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        try {
            PersistentSession session = eu.coaxion.gnumedj.postgre.v14.UntitledPersistentManager.instance().getSession();
            return iterateHospital_stayByQuery(session, condition, orderBy, lockMode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public static java.util.Iterator<Hospital_stay> iterateHospital_stayByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Hospital_stay as Hospital_stay");
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

    public static java.util.Iterator<Hospital_stay> iterateHospital_stayByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException {
        StringBuffer sb = new StringBuffer("From eu.coaxion.gnumedj.postgre.v14.Hospital_stay as Hospital_stay");
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

    public static Hospital_stay loadHospital_stayByCriteria(Hospital_stayCriteria hospital_stayCriteria) {
        Hospital_stay[] hospital_stays = listHospital_stayByCriteria(hospital_stayCriteria);
        if (hospital_stays == null || hospital_stays.length == 0) {
            return null;
        }
        return hospital_stays[0];
    }

    public static Hospital_stay[] listHospital_stayByCriteria(Hospital_stayCriteria hospital_stayCriteria) {
        return hospital_stayCriteria.listHospital_stay();
    }

    public static Hospital_stay createHospital_stay() {
        return new eu.coaxion.gnumedj.postgre.v14.Hospital_stay();
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
            eu.coaxion.gnumedj.postgre.v14.Procedure[] lProcedures = (eu.coaxion.gnumedj.postgre.v14.Procedure[]) getProcedure().toArray(new eu.coaxion.gnumedj.postgre.v14.Procedure[getProcedure().size()]);
            for (int i = 0; i < lProcedures.length; i++) {
                lProcedures[i].setFk_hospital_stay(null);
            }
            return delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
    }

    public boolean deleteAndDissociate(org.orm.PersistentSession session) throws PersistentException {
        try {
            eu.coaxion.gnumedj.postgre.v14.Procedure[] lProcedures = (eu.coaxion.gnumedj.postgre.v14.Procedure[]) getProcedure().toArray(new eu.coaxion.gnumedj.postgre.v14.Procedure[getProcedure().size()]);
            for (int i = 0; i < lProcedures.length; i++) {
                lProcedures[i].setFk_hospital_stay(null);
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

    private int pk_item;

    private java.sql.Timestamp clin_when;

    private int fk_encounter;

    private int fk_episode;

    private String narrative;

    private String soap_cat;

    private int pk;

    private java.sql.Timestamp discharge;

    private java.util.Set<eu.coaxion.gnumedj.postgre.v14.Procedure> procedure = new java.util.HashSet<eu.coaxion.gnumedj.postgre.v14.Procedure>();

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

    public void setPk_item(int value) {
        this.pk_item = value;
    }

    public int getPk_item() {
        return pk_item;
    }

    /**
	 * to be used as when the patient was admitted
	 */
    public void setClin_when(java.sql.Timestamp value) {
        this.clin_when = value;
    }

    /**
	 * to be used as when the patient was admitted
	 */
    public java.sql.Timestamp getClin_when() {
        return clin_when;
    }

    public void setFk_encounter(int value) {
        this.fk_encounter = value;
    }

    public int getFk_encounter() {
        return fk_encounter;
    }

    public void setFk_episode(int value) {
        this.fk_episode = value;
    }

    public int getFk_episode() {
        return fk_episode;
    }

    /**
	 * the hospital to which the patient was admitted
	 */
    public void setNarrative(String value) {
        this.narrative = value;
    }

    /**
	 * the hospital to which the patient was admitted
	 */
    public String getNarrative() {
        return narrative;
    }

    public void setSoap_cat(String value) {
        this.soap_cat = value;
    }

    public String getSoap_cat() {
        return soap_cat;
    }

    private void setPk(int value) {
        this.pk = value;
    }

    public int getPk() {
        return pk;
    }

    public int getORMID() {
        return getPk();
    }

    /**
	 * when was the patient discharged
	 */
    public void setDischarge(java.sql.Timestamp value) {
        this.discharge = value;
    }

    /**
	 * when was the patient discharged
	 */
    public java.sql.Timestamp getDischarge() {
        return discharge;
    }

    public void setProcedure(java.util.Set<eu.coaxion.gnumedj.postgre.v14.Procedure> value) {
        this.procedure = value;
    }

    public java.util.Set<eu.coaxion.gnumedj.postgre.v14.Procedure> getProcedure() {
        return procedure;
    }

    public String toString() {
        return String.valueOf(getPk());
    }
}
