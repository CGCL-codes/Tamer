package edu.harvard.iq.safe.saasystem.ejb;

import edu.harvard.iq.safe.saasystem.entities.AuSubjectOptions;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Akio Sone
 */
@Stateless
public class AuSubjectOptionsFacade extends AbstractFacade<AuSubjectOptions> {

    @PersistenceContext(unitName = "edu.harvard.iq.safe_safearchiveauditsystem-ejb_ejb_1.1-PU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    Set<String> subjectSet = null;

    Map<String, Long> subjectToIdTable = new LinkedHashMap<String, Long>();

    public AuSubjectOptionsFacade() {
        super(AuSubjectOptions.class);
    }

    public Set<String> getSubjectSet() {
        List<AuSubjectOptions> allRows = this.findAll();
        Set<String> subjectSet = new TreeSet<String>();
        for (AuSubjectOptions row : allRows) {
            subjectSet.add(row.getSubject());
            subjectToIdTable.put(row.getSubject(), row.getId());
        }
        return subjectSet;
    }

    public Map<String, Long> getSubjectToIdTable() {
        getSubjectSet();
        return subjectToIdTable;
    }

    public void deleteBySubjectList(List<String> subjects) {
        em.createNamedQuery("AuSubjectOptions.deleteBySubjects", AuSubjectOptions.class).setParameter("subjects", subjects).executeUpdate();
    }

    public void deleteBySubjectSet(Set<String> subjects) {
        em.createNamedQuery("AuSubjectOptions.deleteBySubjects", AuSubjectOptions.class).setParameter("subjects", subjects).executeUpdate();
    }
}
