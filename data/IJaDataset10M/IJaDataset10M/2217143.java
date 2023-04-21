package business.manager.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.transaction.UserTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import business.domain.Conference;
import business.domain.Paper;
import business.domain.actors.User;
import business.manager.interfaces.AssignationManager;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AssignationManagerBean implements AssignationManager {

    @PersistenceContext
    EntityManager em;

    @Resource
    private UserTransaction ut;

    @Transient
    protected final Log logger = LogFactory.getLog(getClass());

    @SuppressWarnings("finally")
    public boolean assignPaperToMember(String memberLogin, long paperId) {
        try {
            logger.debug("AssignationManagerBean : assignPaperToMember : starting transaction  ");
            ut.begin();
            User member = em.find(User.class, memberLogin);
            Paper paper = em.find(Paper.class, paperId);
            member.addPaperToReview(paper);
            paper.addPcMember(member);
            em.flush();
            ut.commit();
            logger.info("AssignationManagerBean : assignPaperToMember : transaction complete with success");
            return true;
        } catch (Exception e) {
            logger.error("AssignationManagerBean : assignPaperToMember : transaction failed, rollbacking now");
            try {
                ut.rollback();
                logger.info("AssignationManagerBean : assignPaperToMember : rollback done");
            } catch (Exception e1) {
                logger.error("AssignationManagerBean : assignPaperToMember :rollback failed");
            } finally {
                return false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Paper> getAllAssignedPapers(String login, long conferenceId) {
        logger.debug("UserManagerBean : getAllAssignedPapers");
        User user = em.find(User.class, login);
        if (user == null) return null;
        Conference conf = em.find(Conference.class, conferenceId);
        if (conf == null) return null;
        Query query = em.createQuery("select d from User u join u.delegatedPapers d where u.login=:aLogin and d.conference=:aConf");
        query.setParameter("aLogin", login);
        query.setParameter("aConf", conf);
        List<Paper> papers = new ArrayList<Paper>();
        papers = (List<Paper>) query.getResultList();
        query = em.createQuery("select p from Paper p join p.pcMembersReviewers r where r.login=:aLogin and p.conference=:aConf");
        query.setParameter("aLogin", login);
        query.setParameter("aConf", conf);
        List<Paper> papersToReview = (List<Paper>) query.getResultList();
        papers.addAll(papersToReview);
        return papers;
    }

    @SuppressWarnings({ "unchecked" })
    public List<Paper> getMemberPapersByConference(long conferenceId, String login) {
        logger.debug("AssignationManagerBean : getMemberPapersByConference ");
        User user = em.find(User.class, login);
        Conference conf = em.find(Conference.class, conferenceId);
        if (user == null || conf == null) return null;
        Query query = em.createQuery("select p from Paper p join p.pcMembersReviewers pcmember where p.conference.id=:confId and pcmember.login=:login");
        query.setParameter("confId", conferenceId);
        query.setParameter("login", login);
        List<Paper> papers = query.getResultList();
        logger.info("AssignationManagerBean : getMemberPapersByConference : transaction complete with success");
        return papers;
    }

    @SuppressWarnings("finally")
    public long getNbPapersAssignedToPCMember(long conferenceId, String memberLogin) {
        Conference conf = em.find(Conference.class, conferenceId);
        User user = em.find(User.class, memberLogin);
        if (conf == null || user == null) return -1;
        logger.debug("AssignationManagerBean : getNbPapersAssignedToPCMember : starting transaction");
        Query query = em.createQuery("select count(p) from Paper p join p.pcMembersReviewers pcmember where p.conference.id=:confId and pcmember.login=:login");
        query.setParameter("confId", conferenceId);
        query.setParameter("login", memberLogin);
        Long nbPapers = (Long) query.getSingleResult();
        Query queryDelegatedPapers = em.createQuery("select count(d) from User u join u.delegatedPapers d where u.login=:aLogin and d.conference=:aConf");
        queryDelegatedPapers.setParameter("aLogin", memberLogin);
        queryDelegatedPapers.setParameter("aConf", conf);
        Long nbDelegatedPapers = (Long) queryDelegatedPapers.getSingleResult();
        return nbPapers + nbDelegatedPapers;
    }

    @SuppressWarnings("finally")
    public List<Paper> getPaperByMemberForAllConference(String memberLogin) {
        logger.debug("AssignationManagerBean : getPaperByMemberForAllConference ");
        Set<Paper> papers = null;
        User member = em.find(User.class, memberLogin);
        if (member == null) return null;
        papers = member.getPapersToReview();
        return new ArrayList<Paper>(papers);
    }

    public boolean unassignPaperFromMember(String memberLogin, long paperId) {
        logger.debug("AssignationManagerBean : unassignPaperFromMember : transaction beggining");
        try {
            ut.begin();
            User member = em.find(User.class, memberLogin);
            Paper paper = em.find(Paper.class, paperId);
            boolean res = member.removePaperToReview(paper);
            res &= paper.removePcMember(member);
            if (!res) {
                if (member.removeFromDelegatedPapers(paper)) {
                    User delegate = member.getDelegateForPaper(paper);
                    res = delegate.removeFromReceivedPapers(paper);
                    res &= paper.removeDelegate(delegate);
                    res &= paper.removeDeleguer(member);
                    member.removeDelegate(paper);
                    delegate.removeDeleger(paper);
                }
            }
            ut.commit();
            logger.info("AssignationManagerBean : unassignPaperFromMember : transaction complete with success");
            return res;
        } catch (Exception e) {
            logger.error("AssignationManagerBean : unassignPaperFromMember : transaction failed, rollbacking now");
            try {
                ut.rollback();
                logger.info("AssignationManagerBean : unassignPaperFromMember : rollback done");
            } catch (Exception e1) {
                logger.error("AssignationManagerBean : unassignPaperFromMember : rollback failed");
            }
        }
        return false;
    }
}
