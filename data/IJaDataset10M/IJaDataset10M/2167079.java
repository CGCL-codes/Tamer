package net.sf.jttslite.model;

import java.util.Collection;
import javax.inject.Inject;
import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jdo.JdoCallback;
import org.springframework.orm.jdo.support.JdoDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object per i {@link ActionTemplate modelli di azioni}.
 * 
 * @author Davide Cavestro
 * 
 */
@Repository("actionTemplateDAO")
public class ActionTemplateDAO extends JdoDaoSupport {

    @Inject
    public ActionTemplateDAO(final PersistenceManagerFactory pmf) {
        setPersistenceManagerFactory(pmf);
    }

    /**
	 * Carica l'azione individuata tramite l'ID specificato.
	 * 
	 * @param id
	 *            l'ID dell''azione.
	 */
    public ActionTemplate loadTemplate(final long id) throws DataAccessException {
        final ActionTemplate template = getJdoTemplate().getObjectById(ActionTemplate.class, Long.valueOf(id));
        if (template == null) {
            throw new RuntimeException("Template " + id + " not found");
        }
        return getPersistenceManager().detachCopy(template);
    }

    /**
	 * Aggiorna lo stato persistente di un modello già persistente.
	 * 
	 * @param template
	 *            il modello di azione da salvare.
	 */
    public void storeTemplate(final ActionTemplate template) throws DataAccessException {
        getJdoTemplate().makePersistent(template);
    }

    /**
	 * Elimina un modello persistente.
	 * 
	 * @param template
	 *            il modello da elimare.
	 */
    public void deleteTemplate(final ActionTemplate template) throws DataAccessException {
        if (template == null || template.getId() == null) {
            throw new RuntimeException("Template is not persistent");
        } else {
            getPersistenceManager().deletePersistent(template);
        }
    }

    /**
	 * Aggiunge un nuovo modello, rendendolo persistente.
	 * 
	 * @param template
	 *            il modello da aggiungere.
	 * @throws DataAccessException
	 *             in caso di errori JDO
	 */
    public void insertTemplate(final ActionTemplate template) throws DataAccessException {
        if (template == null) {
            throw new NullPointerException("Template is null");
        }
        getJdoTemplate().execute(new JdoCallback<Object>() {

            @Override
            public Object doInJdo(final PersistenceManager pm) throws JDOException {
                pm.makePersistent(template);
                return null;
            }
        });
    }

    /**
	 * Restituisce tutti i template delle action.
	 * 
	 * @return tutti i progetti.
	 * @throws DataAccessException
	 *             in caso di errori JDO
	 */
    public Collection<ActionTemplate> getAllActionTemplates() throws DataAccessException {
        getPersistenceManager().getFetchPlan().addGroup("detach_actiontemplate_time");
        getPersistenceManager().getFetchPlan().setMaxFetchDepth(2);
        return getPersistenceManager().detachCopyAll(getJdoTemplate().find(ActionTemplate.class));
    }
}
