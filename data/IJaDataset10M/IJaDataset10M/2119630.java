package wilos.hibernate.misc.project;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AffectedtoDao extends HibernateDaoSupport {

    /**
     * Returns the WilosUser which have the specified ID
     * 
     * @param _id The wanted WilosUser's ID
     * @return The wanted WilosUser
     */
    public String getAffectedToByIdParticipant(String _id) {
        List affected = this.getHibernateTemplate().find("from Affectedto wu where wu.participant_id=?", _id);
        if (affected.size() > 0) {
            return "false";
        } else {
            return "true";
        }
    }
}
