package wdp.entities.ctrl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import wdp.entities.ctrl.exceptions.NonexistentEntityException;
import wdp.entities.ctrl.exceptions.PreexistingEntityException;
import wdp.entities.raw.Note;
import wdp.entities.raw.Worker;

/**
 *
 * @author robson
 */
public class NoteJpaController {

    public NoteJpaController() {
        emf = Persistence.createEntityManagerFactory("db.fdbPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Note note) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Worker idAuthor = note.getIdAuthor();
            if (idAuthor != null) {
                idAuthor = em.getReference(idAuthor.getClass(), idAuthor.getId());
                note.setIdAuthor(idAuthor);
            }
            em.persist(note);
            if (idAuthor != null) {
                idAuthor.getNoteCollection().add(note);
                idAuthor = em.merge(idAuthor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNote(note.getId()) != null) {
                throw new PreexistingEntityException("Note " + note + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Note note) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Note persistentNote = em.find(Note.class, note.getId());
            Worker idAuthorOld = persistentNote.getIdAuthor();
            Worker idAuthorNew = note.getIdAuthor();
            if (idAuthorNew != null) {
                idAuthorNew = em.getReference(idAuthorNew.getClass(), idAuthorNew.getId());
                note.setIdAuthor(idAuthorNew);
            }
            note = em.merge(note);
            if (idAuthorOld != null && !idAuthorOld.equals(idAuthorNew)) {
                idAuthorOld.getNoteCollection().remove(note);
                idAuthorOld = em.merge(idAuthorOld);
            }
            if (idAuthorNew != null && !idAuthorNew.equals(idAuthorOld)) {
                idAuthorNew.getNoteCollection().add(note);
                idAuthorNew = em.merge(idAuthorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = note.getId();
                if (findNote(id) == null) {
                    throw new NonexistentEntityException("The note with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Note note;
            try {
                note = em.getReference(Note.class, id);
                note.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The note with id " + id + " no longer exists.", enfe);
            }
            Worker idAuthor = note.getIdAuthor();
            if (idAuthor != null) {
                idAuthor.getNoteCollection().remove(note);
                idAuthor = em.merge(idAuthor);
            }
            em.remove(note);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Note> findNoteEntities() {
        return findNoteEntities(true, -1, -1);
    }

    public List<Note> findNoteEntities(int maxResults, int firstResult) {
        return findNoteEntities(false, maxResults, firstResult);
    }

    private List<Note> findNoteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Note as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Note findNote(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Note.class, id);
        } finally {
            em.close();
        }
    }

    public int getNoteCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Note as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
