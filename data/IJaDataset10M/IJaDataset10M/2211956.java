package org.gruposp2p.aularest.model.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import org.gruposp2p.aularest.model.Coursegroup;
import org.gruposp2p.aularest.model.Itemcalificable;
import org.gruposp2p.aularest.model.Itemcalificabletype;
import org.gruposp2p.aularest.model.Subject;
import org.gruposp2p.aularest.model.Competence;
import java.util.ArrayList;
import java.util.Collection;
import org.gruposp2p.aularest.model.Score;
import org.gruposp2p.aularest.model.controller.exceptions.IllegalOrphanException;
import org.gruposp2p.aularest.model.controller.exceptions.NonexistentEntityException;
import org.gruposp2p.aularest.model.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author jj
 */
public class ItemcalificableJpaController {

    public ItemcalificableJpaController() {
        emf = Persistence.createEntityManagerFactory("AulaRest_PU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Itemcalificable itemcalificable) throws PreexistingEntityException, Exception {
        if (itemcalificable.getCompetenceCollection() == null) {
            itemcalificable.setCompetenceCollection(new ArrayList<Competence>());
        }
        if (itemcalificable.getScoreCollection() == null) {
            itemcalificable.setScoreCollection(new ArrayList<Score>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Coursegroup coursegroupId = itemcalificable.getCoursegroupId();
            if (coursegroupId != null) {
                coursegroupId = em.getReference(coursegroupId.getClass(), coursegroupId.getId());
                itemcalificable.setCoursegroupId(coursegroupId);
            }
            Itemcalificabletype itemcalificabletypeId = itemcalificable.getItemcalificabletypeId();
            if (itemcalificabletypeId != null) {
                itemcalificabletypeId = em.getReference(itemcalificabletypeId.getClass(), itemcalificabletypeId.getId());
                itemcalificable.setItemcalificabletypeId(itemcalificabletypeId);
            }
            Subject subjectId = itemcalificable.getSubjectId();
            if (subjectId != null) {
                subjectId = em.getReference(subjectId.getClass(), subjectId.getId());
                itemcalificable.setSubjectId(subjectId);
            }
            Collection<Competence> attachedCompetenceCollection = new ArrayList<Competence>();
            for (Competence competenceCollectionCompetenceToAttach : itemcalificable.getCompetenceCollection()) {
                competenceCollectionCompetenceToAttach = em.getReference(competenceCollectionCompetenceToAttach.getClass(), competenceCollectionCompetenceToAttach.getId());
                attachedCompetenceCollection.add(competenceCollectionCompetenceToAttach);
            }
            itemcalificable.setCompetenceCollection(attachedCompetenceCollection);
            Collection<Score> attachedScoreCollection = new ArrayList<Score>();
            for (Score scoreCollectionScoreToAttach : itemcalificable.getScoreCollection()) {
                scoreCollectionScoreToAttach = em.getReference(scoreCollectionScoreToAttach.getClass(), scoreCollectionScoreToAttach.getId());
                attachedScoreCollection.add(scoreCollectionScoreToAttach);
            }
            itemcalificable.setScoreCollection(attachedScoreCollection);
            em.persist(itemcalificable);
            if (coursegroupId != null) {
                coursegroupId.getItemcalificableCollection().add(itemcalificable);
                coursegroupId = em.merge(coursegroupId);
            }
            if (itemcalificabletypeId != null) {
                itemcalificabletypeId.getItemcalificableCollection().add(itemcalificable);
                itemcalificabletypeId = em.merge(itemcalificabletypeId);
            }
            if (subjectId != null) {
                subjectId.getItemcalificableCollection().add(itemcalificable);
                subjectId = em.merge(subjectId);
            }
            for (Competence competenceCollectionCompetence : itemcalificable.getCompetenceCollection()) {
                competenceCollectionCompetence.getItemcalificableCollection().add(itemcalificable);
                competenceCollectionCompetence = em.merge(competenceCollectionCompetence);
            }
            for (Score scoreCollectionScore : itemcalificable.getScoreCollection()) {
                Itemcalificable oldItemcalificableIdOfScoreCollectionScore = scoreCollectionScore.getItemcalificableId();
                scoreCollectionScore.setItemcalificableId(itemcalificable);
                scoreCollectionScore = em.merge(scoreCollectionScore);
                if (oldItemcalificableIdOfScoreCollectionScore != null) {
                    oldItemcalificableIdOfScoreCollectionScore.getScoreCollection().remove(scoreCollectionScore);
                    oldItemcalificableIdOfScoreCollectionScore = em.merge(oldItemcalificableIdOfScoreCollectionScore);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItemcalificable(itemcalificable.getId()) != null) {
                throw new PreexistingEntityException("Itemcalificable " + itemcalificable + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Itemcalificable itemcalificable) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Itemcalificable persistentItemcalificable = em.find(Itemcalificable.class, itemcalificable.getId());
            Coursegroup coursegroupIdOld = persistentItemcalificable.getCoursegroupId();
            Coursegroup coursegroupIdNew = itemcalificable.getCoursegroupId();
            Itemcalificabletype itemcalificabletypeIdOld = persistentItemcalificable.getItemcalificabletypeId();
            Itemcalificabletype itemcalificabletypeIdNew = itemcalificable.getItemcalificabletypeId();
            Subject subjectIdOld = persistentItemcalificable.getSubjectId();
            Subject subjectIdNew = itemcalificable.getSubjectId();
            Collection<Competence> competenceCollectionOld = persistentItemcalificable.getCompetenceCollection();
            Collection<Competence> competenceCollectionNew = itemcalificable.getCompetenceCollection();
            Collection<Score> scoreCollectionOld = persistentItemcalificable.getScoreCollection();
            Collection<Score> scoreCollectionNew = itemcalificable.getScoreCollection();
            List<String> illegalOrphanMessages = null;
            for (Score scoreCollectionOldScore : scoreCollectionOld) {
                if (!scoreCollectionNew.contains(scoreCollectionOldScore)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Score " + scoreCollectionOldScore + " since its itemcalificableId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (coursegroupIdNew != null) {
                coursegroupIdNew = em.getReference(coursegroupIdNew.getClass(), coursegroupIdNew.getId());
                itemcalificable.setCoursegroupId(coursegroupIdNew);
            }
            if (itemcalificabletypeIdNew != null) {
                itemcalificabletypeIdNew = em.getReference(itemcalificabletypeIdNew.getClass(), itemcalificabletypeIdNew.getId());
                itemcalificable.setItemcalificabletypeId(itemcalificabletypeIdNew);
            }
            if (subjectIdNew != null) {
                subjectIdNew = em.getReference(subjectIdNew.getClass(), subjectIdNew.getId());
                itemcalificable.setSubjectId(subjectIdNew);
            }
            Collection<Competence> attachedCompetenceCollectionNew = new ArrayList<Competence>();
            for (Competence competenceCollectionNewCompetenceToAttach : competenceCollectionNew) {
                competenceCollectionNewCompetenceToAttach = em.getReference(competenceCollectionNewCompetenceToAttach.getClass(), competenceCollectionNewCompetenceToAttach.getId());
                attachedCompetenceCollectionNew.add(competenceCollectionNewCompetenceToAttach);
            }
            competenceCollectionNew = attachedCompetenceCollectionNew;
            itemcalificable.setCompetenceCollection(competenceCollectionNew);
            Collection<Score> attachedScoreCollectionNew = new ArrayList<Score>();
            for (Score scoreCollectionNewScoreToAttach : scoreCollectionNew) {
                scoreCollectionNewScoreToAttach = em.getReference(scoreCollectionNewScoreToAttach.getClass(), scoreCollectionNewScoreToAttach.getId());
                attachedScoreCollectionNew.add(scoreCollectionNewScoreToAttach);
            }
            scoreCollectionNew = attachedScoreCollectionNew;
            itemcalificable.setScoreCollection(scoreCollectionNew);
            itemcalificable = em.merge(itemcalificable);
            if (coursegroupIdOld != null && !coursegroupIdOld.equals(coursegroupIdNew)) {
                coursegroupIdOld.getItemcalificableCollection().remove(itemcalificable);
                coursegroupIdOld = em.merge(coursegroupIdOld);
            }
            if (coursegroupIdNew != null && !coursegroupIdNew.equals(coursegroupIdOld)) {
                coursegroupIdNew.getItemcalificableCollection().add(itemcalificable);
                coursegroupIdNew = em.merge(coursegroupIdNew);
            }
            if (itemcalificabletypeIdOld != null && !itemcalificabletypeIdOld.equals(itemcalificabletypeIdNew)) {
                itemcalificabletypeIdOld.getItemcalificableCollection().remove(itemcalificable);
                itemcalificabletypeIdOld = em.merge(itemcalificabletypeIdOld);
            }
            if (itemcalificabletypeIdNew != null && !itemcalificabletypeIdNew.equals(itemcalificabletypeIdOld)) {
                itemcalificabletypeIdNew.getItemcalificableCollection().add(itemcalificable);
                itemcalificabletypeIdNew = em.merge(itemcalificabletypeIdNew);
            }
            if (subjectIdOld != null && !subjectIdOld.equals(subjectIdNew)) {
                subjectIdOld.getItemcalificableCollection().remove(itemcalificable);
                subjectIdOld = em.merge(subjectIdOld);
            }
            if (subjectIdNew != null && !subjectIdNew.equals(subjectIdOld)) {
                subjectIdNew.getItemcalificableCollection().add(itemcalificable);
                subjectIdNew = em.merge(subjectIdNew);
            }
            for (Competence competenceCollectionOldCompetence : competenceCollectionOld) {
                if (!competenceCollectionNew.contains(competenceCollectionOldCompetence)) {
                    competenceCollectionOldCompetence.getItemcalificableCollection().remove(itemcalificable);
                    competenceCollectionOldCompetence = em.merge(competenceCollectionOldCompetence);
                }
            }
            for (Competence competenceCollectionNewCompetence : competenceCollectionNew) {
                if (!competenceCollectionOld.contains(competenceCollectionNewCompetence)) {
                    competenceCollectionNewCompetence.getItemcalificableCollection().add(itemcalificable);
                    competenceCollectionNewCompetence = em.merge(competenceCollectionNewCompetence);
                }
            }
            for (Score scoreCollectionNewScore : scoreCollectionNew) {
                if (!scoreCollectionOld.contains(scoreCollectionNewScore)) {
                    Itemcalificable oldItemcalificableIdOfScoreCollectionNewScore = scoreCollectionNewScore.getItemcalificableId();
                    scoreCollectionNewScore.setItemcalificableId(itemcalificable);
                    scoreCollectionNewScore = em.merge(scoreCollectionNewScore);
                    if (oldItemcalificableIdOfScoreCollectionNewScore != null && !oldItemcalificableIdOfScoreCollectionNewScore.equals(itemcalificable)) {
                        oldItemcalificableIdOfScoreCollectionNewScore.getScoreCollection().remove(scoreCollectionNewScore);
                        oldItemcalificableIdOfScoreCollectionNewScore = em.merge(oldItemcalificableIdOfScoreCollectionNewScore);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = itemcalificable.getId();
                if (findItemcalificable(id) == null) {
                    throw new NonexistentEntityException("The itemcalificable with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Itemcalificable itemcalificable;
            try {
                itemcalificable = em.getReference(Itemcalificable.class, id);
                itemcalificable.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemcalificable with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Score> scoreCollectionOrphanCheck = itemcalificable.getScoreCollection();
            for (Score scoreCollectionOrphanCheckScore : scoreCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Itemcalificable (" + itemcalificable + ") cannot be destroyed since the Score " + scoreCollectionOrphanCheckScore + " in its scoreCollection field has a non-nullable itemcalificableId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Coursegroup coursegroupId = itemcalificable.getCoursegroupId();
            if (coursegroupId != null) {
                coursegroupId.getItemcalificableCollection().remove(itemcalificable);
                coursegroupId = em.merge(coursegroupId);
            }
            Itemcalificabletype itemcalificabletypeId = itemcalificable.getItemcalificabletypeId();
            if (itemcalificabletypeId != null) {
                itemcalificabletypeId.getItemcalificableCollection().remove(itemcalificable);
                itemcalificabletypeId = em.merge(itemcalificabletypeId);
            }
            Subject subjectId = itemcalificable.getSubjectId();
            if (subjectId != null) {
                subjectId.getItemcalificableCollection().remove(itemcalificable);
                subjectId = em.merge(subjectId);
            }
            Collection<Competence> competenceCollection = itemcalificable.getCompetenceCollection();
            for (Competence competenceCollectionCompetence : competenceCollection) {
                competenceCollectionCompetence.getItemcalificableCollection().remove(itemcalificable);
                competenceCollectionCompetence = em.merge(competenceCollectionCompetence);
            }
            em.remove(itemcalificable);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Itemcalificable> findItemcalificableEntities() {
        return findItemcalificableEntities(true, -1, -1);
    }

    public List<Itemcalificable> findItemcalificableEntities(int maxResults, int firstResult) {
        return findItemcalificableEntities(false, maxResults, firstResult);
    }

    private List<Itemcalificable> findItemcalificableEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Itemcalificable as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Itemcalificable findItemcalificable(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Itemcalificable.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemcalificableCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Itemcalificable as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
