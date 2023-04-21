package be.edev.icerp.service.stock.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import be.edev.icerp.model.stock.Categorie;
import be.edev.icerp.service.stock.CategorieService;

@Transactional
public class CategorieServiceImpl implements CategorieService {

    private EntityManager em;

    private Log log = LogFactory.getLog(CategorieServiceImpl.class);

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List<Categorie> findAll() {
        Query query = getEntityManager().createQuery("select c FROM Categorie c");
        return query.getResultList();
    }

    public void remove(int id) {
        Categorie categorie = find(id);
        if (categorie != null) {
            em.remove(categorie);
        }
    }

    private EntityManager getEntityManager() {
        return em;
    }

    public Categorie find(int id) {
        return em.find(Categorie.class, id);
    }

    public void save(Categorie categorie) {
        if (categorie != null) {
            if (categorie.getId() == null) {
                em.persist(categorie);
            } else {
                em.merge(categorie);
            }
        } else {
            this.log.debug("on passe une catégorie null!");
        }
    }
}
