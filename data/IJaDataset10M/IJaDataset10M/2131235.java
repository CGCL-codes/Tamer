package uk.ac.ebi.intact.persistence.dao.impl;

import uk.ac.ebi.intact.model.ProteinImpl;
import uk.ac.ebi.intact.model.CvXrefQualifier;
import uk.ac.ebi.intact.model.CvTopic;
import uk.ac.ebi.intact.model.InteractorImpl;
import uk.ac.ebi.intact.persistence.dao.impl.InteractorDaoImpl;
import uk.ac.ebi.intact.persistence.dao.ProteinDao;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Order;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: ProteinDaoImpl.java 4988 2006-06-07 12:58:13Z baranda $
 * @since <pre>03-May-2006</pre>
 */
@SuppressWarnings({ "unchecked" })
public class ProteinDaoImpl extends InteractorDaoImpl<ProteinImpl> implements ProteinDao {

    private static Log log = LogFactory.getLog(ProteinDaoImpl.class);

    public ProteinDaoImpl(Session session) {
        super(ProteinImpl.class, session);
    }

    /**
     * Gets the AC of the identity Xref
     */
    public String getIdentityXrefByProteinAc(String proteinAc) {
        Criteria crit = getSession().createCriteria(ProteinImpl.class).add(Restrictions.idEq(proteinAc)).createAlias("xrefs", "xref").createAlias("xref.cvXrefQualifier", "qual").add(Restrictions.eq("qual.shortLabel", CvXrefQualifier.IDENTITY)).setProjection(Projections.property("xref.ac"));
        return (String) crit.uniqueResult();
    }

    /**
     * Gets the AC of the identity Xref
     */
    public String getUniprotAcByProteinAc(String proteinAc) {
        Criteria crit = getSession().createCriteria(ProteinImpl.class).add(Restrictions.idEq(proteinAc)).createAlias("xrefs", "xref").createAlias("xref.cvXrefQualifier", "qual").add(Restrictions.eq("qual.shortLabel", CvXrefQualifier.IDENTITY)).setProjection(Projections.property("xref.primaryId"));
        return (String) crit.uniqueResult();
    }

    /**
     * Obtains the template of the url to be used in search.
     * Uses the uniprot Xref and then get hold of its annotation 'search-url'
     */
    public String getUniprotUrlTemplateByProteinAc(String proteinAc) {
        if (proteinAc == null) {
            throw new NullPointerException("proteinAc");
        }
        Criteria crit = getSession().createCriteria(ProteinImpl.class).add(Restrictions.idEq(proteinAc)).createAlias("xrefs", "xref").createAlias("xref.cvXrefQualifier", "cvQual").createAlias("xref.cvDatabase", "cvDb").createAlias("cvDb.annotations", "annot").createAlias("annot.cvTopic", "cvTopic").add(Restrictions.eq("cvQual.shortLabel", CvXrefQualifier.IDENTITY)).add(Restrictions.eq("cvTopic.shortLabel", CvTopic.SEARCH_URL)).setProjection(Projections.property("annot.annotationText"));
        return (String) crit.uniqueResult();
    }

    public Map<String, Integer> getPartnersCountingInteractionsByProteinAc(String proteinAc) {
        if (proteinAc == null) {
            throw new NullPointerException("proteinAc");
        }
        Criteria crit = getSession().createCriteria(ProteinImpl.class).add(Restrictions.idEq(proteinAc)).createAlias("activeInstances", "comp").createAlias("comp.interaction", "int").createAlias("int.components", "intcomp").createAlias("intcomp.interactor", "prot").add(Restrictions.disjunction().add(Restrictions.ne("prot.ac", proteinAc)).add(Restrictions.eq("comp.stoichiometry", 2f))).setProjection(Projections.projectionList().add(Projections.countDistinct("int.ac")).add(Projections.groupProperty("prot.ac")));
        List<Object[]> queryResults = crit.list();
        Map<String, Integer> results = new HashMap<String, Integer>(queryResults.size());
        for (Object[] res : queryResults) {
            results.put((String) res[1], (Integer) res[0]);
        }
        return results;
    }

    public Integer countPartnersByProteinAc(String proteinAc) {
        return (Integer) partnersByProteinAcCriteria(proteinAc).setProjection(Projections.countDistinct("prot.ac")).uniqueResult();
    }

    public Map<String, List<String>> getPartnersWithInteractionAcsByProteinAc(String proteinAc) {
        Criteria crit = partnersByProteinAcCriteria(proteinAc).setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("prot.ac"))).add(Projections.property("int.ac"))).addOrder(Order.asc("prot.ac"));
        int countAcs = 0;
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        for (Object[] res : (List<Object[]>) crit.list()) {
            String partnerProtAc = (String) res[0];
            String interactionAc = (String) res[1];
            if (results.containsKey(partnerProtAc)) {
                results.get(partnerProtAc).add(interactionAc);
            } else {
                countAcs++;
                List<String> interactionAcList = new ArrayList<String>();
                interactionAcList.add(interactionAc);
                results.put(partnerProtAc, interactionAcList);
            }
        }
        return results;
    }

    private Criteria partnersByProteinAcCriteria(String proteinAc) {
        if (proteinAc == null) {
            throw new NullPointerException("proteinAc");
        }
        return getSession().createCriteria(InteractorImpl.class).add(Restrictions.idEq(proteinAc)).createAlias("activeInstances", "comp").createAlias("comp.interaction", "int").createAlias("int.components", "intcomp").createAlias("intcomp.interactor", "prot").add(Restrictions.disjunction().add(Restrictions.ne("prot.ac", proteinAc)).add(Restrictions.eq("comp.stoichiometry", 2f)));
    }
}
