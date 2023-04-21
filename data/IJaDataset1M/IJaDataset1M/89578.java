package icescrum2.dao;

import icescrum2.dao.model.IRelease;
import icescrum2.dao.model.IStory;
import icescrum2.dao.model.impl.Release;
import icescrum2.service.beans.ExceptionManager;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 
 * La classe ReleaseDao contr�le des demandes � la base de donn�es
 * 
 */
public class ReleaseDao extends HibernateDaoSupport {

    public boolean checkUniqueName(IRelease _r, String idPb) {
        String nb = "0";
        Session sessHQL = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
        String query = "select count(*) from Product p join " + " p.releases r where p.idProduct=? " + "and r.name=? and not idRelease=?";
        nb = sessHQL.createQuery(query).setString(0, idPb).setString(1, _r.getName()).setInteger(2, _r.getIdRelease()).uniqueResult().toString();
        if (nb.equals("0")) {
            return true;
        }
        return false;
    }

    /**
	 * Sauvegarde la release alex
	 * 
	 * @param _release
	 */
    public boolean saveRelease(IRelease _release, String idPb) {
        if (this.checkUniqueName(_release, idPb)) {
            try {
                this.getHibernateTemplate().save(_release);
            } catch (DataIntegrityViolationException _e) {
                ExceptionManager.getInstance().manageDataIntegrityViolationException(this.getClass().getName(), "saveRelease", _e);
                return false;
            } catch (ConstraintViolationException _ex) {
                ExceptionManager.getInstance().manageConstraintViolationException(this.getClass().getName(), "saveRelease", _ex);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Mise � jour de la Release alex
	 * 
	 * @param _release
	 */
    public boolean updateRelease(final IRelease _release, String idPb) {
        if (this.checkUniqueName(_release, idPb)) {
            try {
                this.getHibernateTemplate().update(_release);
                return true;
            } catch (DataIntegrityViolationException _e) {
                ExceptionManager.getInstance().manageDataIntegrityViolationException(this.getClass().getName(), "updateRelease", _e);
                return false;
            } catch (ConstraintViolationException _ex) {
                ExceptionManager.getInstance().manageConstraintViolationException(this.getClass().getName(), "updateRelease", _ex);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
	 * R�cup�re la Release � partir de la cl� pass�e en param�tre alex
	 * 
	 * @param cle
	 * @return Release
	 */
    @SuppressWarnings("unchecked")
    public IRelease getRelease(String cle) {
        List<Release> loadAll = new ArrayList<Release>();
        loadAll = (List<Release>) this.getHibernateTemplate().find("from Release re where re.idRelease=" + cle);
        if (loadAll.size() == 1) {
            return loadAll.get(0);
        } else {
            return null;
        }
    }

    public void deleteRelease(IRelease _release) {
        try {
            this.getHibernateTemplate().delete(_release);
        } catch (DataAccessException _e) {
            ExceptionManager.getInstance().manageDataAccessException(this.getClass().getName(), "deleteRelease", _e);
        }
    }

    public Double pointsForOneRelease(IRelease r, boolean separateNewItems) {
        Session sessHQL = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
        String queryPbi = "select sum(pbi.estimatedPoints) from Product p join " + "p.releases rel join rel.sprints s " + "join s.productBacklogItems pbi where" + " pbi.estimatedPoints != " + IStory.IMPOSSIBLE_POINT + " and pbi.estimatedPoints != " + IStory.SPRINT_POINT + " and rel = ? ";
        Long l;
        if (!separateNewItems) {
            l = (Long) sessHQL.createQuery(queryPbi).setEntity(0, r).uniqueResult();
        } else {
            queryPbi += " and pbi.insertedOnActiveRelease = ?";
            l = (Long) sessHQL.createQuery(queryPbi).setEntity(0, r).setBoolean(1, false).uniqueResult();
        }
        if (l != null) return new Double(l);
        return null;
    }
}
