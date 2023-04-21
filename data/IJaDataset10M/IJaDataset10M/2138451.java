package edu.harvard.iq.safe.saasystem.ejb;

import edu.harvard.iq.safe.saasystem.entities.AuOwnerInstitutionOptions;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Akio Sone
 */
@Singleton
public class AuOwnerInstitutionOptionsFacade extends AbstractFacade<AuOwnerInstitutionOptions> {

    @PersistenceContext(unitName = "edu.harvard.iq.safe_safearchiveauditsystem-ejb_ejb_1.1-PU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    Set<String> instNameSet = null;

    Map<String, Long> instNameToIdTable = new LinkedHashMap<String, Long>();

    public AuOwnerInstitutionOptionsFacade() {
        super(AuOwnerInstitutionOptions.class);
    }

    public Set<String> getInstNameSet() {
        List<AuOwnerInstitutionOptions> allRows = this.findAll();
        Set<String> subjectSet = new TreeSet<String>();
        for (AuOwnerInstitutionOptions row : allRows) {
            subjectSet.add(row.getInstName());
            instNameToIdTable.put(row.getInstName(), row.getId());
        }
        return subjectSet;
    }

    public int getNumberOfOwnerInstitutions() {
        return this.findAll().size();
    }

    public Map<String, Long> getInstNameToIdTable() {
        getInstNameSet();
        return instNameToIdTable;
    }

    public void deleteByInstNameList(List<String> instNames) {
        em.createNamedQuery("AuOwnerInstitutionOptions.deleteByInstName", AuOwnerInstitutionOptions.class).setParameter("instNames", instNames).executeUpdate();
    }

    public void deleteByInstNameSet(Set<String> instNames) {
        em.createNamedQuery("AuOwnerInstitutionOptions.deleteByInstName", AuOwnerInstitutionOptions.class).setParameter("instNames", instNames).executeUpdate();
    }
}
