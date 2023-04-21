package net.sourceforge.solexatools.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.hibernate3.HibernateTemplate;
import net.sourceforge.solexatools.Debug;
import net.sourceforge.solexatools.dao.ExperimentDAO;
import net.sourceforge.solexatools.model.Experiment;
import net.sourceforge.solexatools.model.Registration;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SQLQuery;
import org.hibernate.Query;
import java.sql.SQLException;

public class ExperimentDAOHibernate extends HibernateDaoSupport implements ExperimentDAO {

    public ExperimentDAOHibernate() {
        super();
    }

    public void insert(Experiment experiment) {
        this.getHibernateTemplate().save(experiment);
    }

    public void update(Experiment experiment) {
        this.getHibernateTemplate().update(experiment);
    }

    public void merge(Experiment nu) {
        Integer id = nu.getExperimentId();
        Experiment old = null;
        if (id != null) {
            old = findByID(id);
            if (old == null) {
                nu.setExperimentId(null);
            }
        }
        if (old == null) {
            this.getHibernateTemplate().save(nu);
        } else {
            Session ssn = this.getSession(false);
            Query sql = null;
            Transaction tx = ssn.getTransaction();
            if (tx == null || !tx.isActive()) tx = ssn.beginTransaction();
            {
                sql = ssn.createSQLQuery("update	real_experiment" + "	set	name			= :name," + "		description		= :description" + " " + "where experiment_id	= :experiment_id");
                sql.setParameter("name", nu.getName());
                sql.setParameter("description", nu.getDescription());
                sql.setParameter("experiment_id", nu.getExperimentId());
            }
            if (sql.executeUpdate() != 1) {
                tx.rollback();
            } else {
                tx.commit();
            }
            if (!tx.isActive()) tx.begin();
        }
    }

    public void refresh(Experiment experiment) {
        HibernateTemplate hibernate = this.getHibernateTemplate();
        hibernate.refresh(experiment);
    }

    public List<Experiment> list(Registration registration) {
        ArrayList<Experiment> experiments = new ArrayList<Experiment>();
        if (registration == null) return experiments;
        List expmts;
        if (registration.isTechnician() || registration.isLIMSAdmin()) {
            expmts = this.getHibernateTemplate().find("from Experiment as experiment order by experiment.name desc");
        } else {
            expmts = this.getHibernateTemplate().find("from Experiment as experiment where owner_id = ? order by experiment.name desc", registration.getRegistrationId());
        }
        for (Object experiment : expmts) {
            experiments.add((Experiment) experiment);
        }
        return experiments;
    }

    /**
	 * Finds an instance of Experiment in the database by the Experiment
	 * name.
	 *
	 * @param name	name of the Experiment
	 * @return Experiment or null if not found
	 */
    public Experiment findByName(String name) {
        String query = "from experiment as experiment where experiment.name = ?";
        Experiment experiment = null;
        Object[] parameters = { name };
        List list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            experiment = (Experiment) list.get(0);
        }
        return experiment;
    }

    /**
	 * Finds an instance of Experiment in the database by the Experiment
	 * ID.
	 *
	 * @param expID	ID of the Experiment
	 * @return Experiment or null if not found
	 */
    public Experiment findByID(Integer expID) {
        String query = "from Experiment as experiment where experiment.experimentId = ?";
        Experiment experiment = null;
        Object[] parameters = { expID };
        List list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            experiment = (Experiment) list.get(0);
        }
        return experiment;
    }
}
