package org.dcm4chee.web.dao.trash;

import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.annotation.ejb.LocalBinding;

/**
 * @author Robert David <robert.david@agfa.com>
 * @version $Revision$ $Date$
 * @since May 31, 2011
 */
@Stateless
@LocalBinding(jndiBinding = TrashCleanerLocal.JNDI_NAME)
public class TrashCleanerBean implements TrashCleanerLocal {

    @PersistenceContext(unitName = "dcm4chee-arc")
    private EntityManager em;

    public void clearTrash(Date limit) {
        em.createQuery("UPDATE PrivateFile p SET p.instance = NULL " + "WHERE p.instance.id IN " + "(SELECT pi FROM PrivateInstance pi WHERE pi.createdTime < :limit)").setParameter("limit", limit).executeUpdate();
        em.createQuery("DELETE FROM PrivateInstance pi WHERE pi.createdTime < :limit").setParameter("limit", limit).executeUpdate();
        em.createQuery("DELETE FROM PrivateSeries ps WHERE SIZE(ps.instances) = 0").executeUpdate();
        em.createQuery("DELETE FROM PrivateStudy ps WHERE SIZE(ps.series) = 0").executeUpdate();
        em.createQuery("DELETE FROM PrivatePatient pp WHERE SIZE(pp.studies) = 0").executeUpdate();
    }
}
