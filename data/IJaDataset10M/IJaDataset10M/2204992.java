package fr.helmet.session.stateless.eao.note;

import fr.helmet.entity.note.Note;
import fr.helmet.session.stateless.eao.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vlaugier
 */
@Stateless
public class NoteFacade extends AbstractFacade<Note> implements NoteFacadeLocal {

    @PersistenceContext(unitName = "sample-project")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public NoteFacade() {
        super(Note.class);
    }
}
