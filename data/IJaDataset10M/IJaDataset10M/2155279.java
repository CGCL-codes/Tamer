package wilos.hibernate.gen;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wilos.model.spem2.task.Step;

/**
 * Dao object for domain model class Step.
 * 
 * @see wilos.model.spem2.task.Step
 * <p/>Remark: This class is auto-generated.
 */
public class StepDao extends HibernateDaoSupport {

    /**
     * Return the Step which have the id _id.
     * 
     * @param _id 
     *            The id of the Step.
     * @return The corresponding Step.
     */
    public Step getStep(String _id) {
        if (!_id.equals("")) return (Step) this.getHibernateTemplate().get(Step.class, _id);
        return null;
    }

    /**
     * Return the list of all Steps
     * 
     * @return The list of all Steps
     */
    public List<Step> getAllSteps() {
        List<Step> list = new ArrayList<Step>();
        for (Object obj : this.getHibernateTemplate().loadAll(Step.class)) {
            Step elt = (Step) obj;
            list.add(elt);
        }
        return list;
    }

    /**
     * Save or update the Step _elt.
     * 
     * @param _elt 
     *            The Step to save or update.
     */
    public void saveOrUpdateStep(Step _elt) {
        if (_elt != null) this.getHibernateTemplate().saveOrUpdate(_elt);
    }

    /**
     * Delete the Step _elt.
     * 
     * @param _elt 
     *            The _elt to delete.
     */
    public void deleteStep(Step _elt) {
        this.getHibernateTemplate().delete(_elt);
    }
}
