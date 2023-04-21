package wilos.hibernate.gen;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wilos.model.misc.wilosuser.Participant;

/**
 * Dao object for domain model class Participant.
 * 
 * @see wilos.model.misc.wilosuser.Participant
 * <p/>Remark: This class is auto-generated.
 */
public class ParticipantDao extends HibernateDaoSupport {

    /**
     * Return the Participant which have the id _id.
     * 
     * @param _id 
     *            The id of the Participant.
     * @return The corresponding Participant.
     */
    public Participant getParticipant(String _id) {
        if (!_id.equals("")) return (Participant) this.getHibernateTemplate().get(Participant.class, _id);
        return null;
    }

    /**
     * Return the Participant which have the login _login.
     * 
     * @param _login 
     *            The id of the Participant.
     * @return The corresponding Participant.
     */
    public Participant getParticipantByLogin(String _login) {
        List<?> list = this.getHibernateTemplate().find("from Participant elt where elt.login=?", _login);
        if (list.size() > 0) return (Participant) list.get(0);
        return null;
    }

    /**
     * Return the list of all Participants
     * 
     * @return The list of all Participants
     */
    public List<Participant> getAllParticipants() {
        List<Participant> list = new ArrayList<Participant>();
        for (Object obj : this.getHibernateTemplate().loadAll(Participant.class)) {
            Participant elt = (Participant) obj;
            list.add(elt);
        }
        return list;
    }

    /**
     * Save or update the Participant _elt.
     * 
     * @param _elt 
     *            The Participant to save or update.
     */
    public void saveOrUpdateParticipant(Participant _elt) {
        if (_elt != null) this.getHibernateTemplate().saveOrUpdate(_elt);
    }

    /**
     * Delete the Participant _elt.
     * 
     * @param _elt 
     *            The _elt to delete.
     */
    public void deleteParticipant(Participant _elt) {
        this.getHibernateTemplate().delete(_elt);
    }
}
