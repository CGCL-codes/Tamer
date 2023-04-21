package org.openedc.core.domain.model.service.support.ee;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.openedc.core.domain.model.Institution;
import org.openedc.core.domain.model.service.InstitutionService;
import org.openedc.core.domain.model.service.InstitutionServiceImplementation;
import org.openedc.core.domain.model.service.QueryParam;
import org.openedc.core.domain.model.service.exceptions.NonexistentEntityException;
import org.openedc.core.domain.model.support.InstitutionEntity;

/**
 *
 * @author openedc
 */
@Named
@Stateless
public class EeInstitutionEntityService implements InstitutionService {

    @PersistenceContext(unitName = "StandaloneJpa2AppPU")
    private EntityManager em;

    private EeEntityService entityService = new EeEntityService() {

        @Override
        protected EntityManager getEntityManager() {
            return em;
        }

        @Override
        public Class getEntityType() {
            return InstitutionEntity.class;
        }
    };

    @Override
    public void create(Institution institution) throws Exception {
        entityService.create(institution);
    }

    @Override
    public void destroy(Long id) throws NonexistentEntityException {
        entityService.destroy(id);
    }

    @Override
    public void edit(Institution institution) throws NonexistentEntityException, Exception {
        entityService.edit(institution);
    }

    @Override
    public void store(Institution institution) throws Exception {
        entityService.store(institution);
    }

    @Override
    public Institution find(Long id) throws Exception {
        return (Institution) entityService.find(id);
    }

    @Override
    public List findAll() throws Exception {
        return entityService.findAll();
    }

    @Override
    public List findAll(int maxResults, int firstResult) throws Exception {
        return entityService.findAll(maxResults, firstResult);
    }

    @Override
    public int getCount() throws Exception {
        return entityService.getCount();
    }

    @Override
    public Institution getInstance() throws Exception {
        return (Institution) entityService.getInstance();
    }

    @Override
    public Institution findByNamedQuery(String namedQueryName, QueryParam[] params) throws Exception {
        return (Institution) entityService.findByNamedQuery(namedQueryName, params);
    }
}
