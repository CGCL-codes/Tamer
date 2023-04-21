package br.com.cqipac.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import br.com.cqipac.to.Funcionario;
import br.com.cqipac.to.NaoConformidade;

/**
 * Home object for domain model class NaoConformidade.
 * 
 * @see br.com.cqipac.to.NaoConformidade
 * @author Hibernate Tools
 */
@Stateless
public class NaoConformidadeDao implements RemoteDao {

    private static final Log log = LogFactory.getLog(NaoConformidadeDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Object transientInstance) throws Exception {
        log.debug("persisting NaoConformidade instance");
        entityManager.persist((NaoConformidade) transientInstance);
        log.debug("persist successful");
    }

    public void remove(Object persistentInstance) throws Exception {
        log.debug("removing NaoConformidade instance");
        entityManager.remove((NaoConformidade) persistentInstance);
        log.debug("remove successful");
    }

    public NaoConformidade merge(Object detachedInstance) throws Exception {
        log.debug("merging NaoConformidade instance");
        NaoConformidade result = entityManager.merge((NaoConformidade) detachedInstance);
        log.debug("merge successful");
        return result;
    }

    public NaoConformidade findById(Integer id) throws Exception {
        log.debug("getting NaoConformidade instance with id: " + id);
        NaoConformidade instance = entityManager.find(NaoConformidade.class, id);
        log.debug("get successful");
        return instance;
    }

    public List<NaoConformidade> list() throws Exception {
        log.debug("getting NaoConformidade instances list");
        List<NaoConformidade> instance = entityManager.createQuery("from NaoConformidade naoConformidade").getResultList();
        log.debug("get successful");
        return instance;
    }

    @Override
    public Object find(Object searchingIsntance, Integer intTipoBusca) throws Exception {
        return null;
    }

    @Override
    public List<Funcionario> list(Object searchingInstance, Integer intTipoBusca) throws Exception {
        return null;
    }
}
