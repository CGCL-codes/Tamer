package dao;

import bean.ProposalBean;
import bean.QuestionBean;
import bean.UsuarioBean;
import bean.VoteBean;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.DataAccessLayerException;
import util.HibernateFactory;

/**
 *
 * @author galluzzo
 */
public class VoteDao extends AbstractDao {

    public VoteDao() {
        super();
    }

    public void create(VoteBean vote) throws DataAccessLayerException {
        super.saveOrUpdate(vote);
    }

    /**
     * Delete a detached Event from the database.
     * @param event
     */
    public void delete(VoteBean vote) throws DataAccessLayerException {
        super.delete(vote);
    }

    /**
     * Find an Event by its primary key.
     * @param id
     * @return
     */
    public VoteBean find(Integer id) throws DataAccessLayerException {
        return (VoteBean) super.find(VoteBean.class, id);
    }

    /**
     * Updates the state of a detached Event.
     *
     * @param event
     */
    public void update(VoteBean vote) throws DataAccessLayerException {
        super.saveOrUpdate(vote);
    }

    /**
     * Finds all Events in the database.
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<VoteBean> findAll() throws DataAccessLayerException {
        return super.findAll(VoteBean.class);
    }

    public VoteBean findByQuestionAndUser(QuestionBean qbean, UsuarioBean voter) {
        HibernateFactory.buildIfNeeded();
        Session session = null;
        VoteBean vote = null;
        Transaction tx = null;
        try {
            session = HibernateFactory.openSession();
            tx = session.beginTransaction();
            Query query = session.createQuery("SELECT v FROM VoteBean v WHERE" + " v.voter.id =:voter" + " AND v.question.id = :question");
            query.setInteger("voter", voter.getId());
            query.setInteger("question", qbean.getId());
            vote = (VoteBean) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
        return vote;
    }

    public VoteBean findByProposalAndUser(ProposalBean proposal, UsuarioBean voter) {
        HibernateFactory.buildIfNeeded();
        Session session = null;
        VoteBean vote = null;
        Transaction tx = null;
        try {
            session = HibernateFactory.openSession();
            tx = session.beginTransaction();
            Query query = session.createQuery("SELECT v FROM VoteBean v WHERE" + " v.voter.id =:voter" + " AND v.proposal.id = :proposal");
            query.setInteger("voter", voter.getId());
            query.setInteger("proposal", proposal.getId());
            vote = (VoteBean) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
        return vote;
    }
}
