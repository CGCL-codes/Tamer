package org.dcm4chee.web.dao.ae;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.dcm4chee.archive.entity.AE;
import org.dcm4chee.usr.dao.UserAccess;
import org.dcm4chee.usr.util.JNDIUtils;
import org.jboss.annotation.ejb.LocalBinding;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Jan 5, 2008
 */
@Stateless
@LocalBinding(jndiBinding = AEHomeLocal.JNDI_NAME)
public class AEHomeBean implements AEHomeLocal {

    @PersistenceContext(unitName = "dcm4chee-arc")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<String> listAETitles() {
        return em.createQuery("SELECT ae.title FROM AE ae ORDER BY ae.title").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<AE> findAll(String filter) {
        String filterQuery = filter == null ? "" : "<NONE>".equals(filter) ? "WHERE aeGroup IS NULL" : "WHERE aeGroup = :filter";
        Query query = em.createQuery("FROM AE ae " + filterQuery + " ORDER BY ae.title, ae.aeGroup");
        if (filter != null && !"<NONE>".equals(filter)) query.setParameter("filter", filter);
        List<AE> l = query.getResultList();
        em.clear();
        return l;
    }

    public AE findByTitle(String title) {
        Query q = em.createNamedQuery("AE.findByTitle");
        q.setParameter("title", title);
        return (AE) q.getSingleResult();
    }

    public AE updateOrCreateAET(AE ae) {
        if (ae.getPk() == -1) {
            em.persist(ae);
            return null;
        } else {
            AE oldAE = em.find(AE.class, ae.getPk());
            String oldAETitle = oldAE.getTitle();
            em.merge(ae);
            ((UserAccess) JNDIUtils.lookup(UserAccess.JNDI_NAME)).updateAETInAETGroups(oldAETitle, ae.getTitle());
            return oldAE;
        }
    }

    public void removeAET(long pk) {
        AE ae = em.find(AE.class, pk);
        em.remove(ae);
        ((UserAccess) JNDIUtils.lookup(UserAccess.JNDI_NAME)).removeAETFromAETGroups(ae.getTitle());
    }

    @SuppressWarnings("unchecked")
    public List<String> listAeTypes() {
        return em.createQuery("SELECT DISTINCT ae.aeGroup FROM AE ae ORDER BY ae.aeGroup").getResultList();
    }
}
