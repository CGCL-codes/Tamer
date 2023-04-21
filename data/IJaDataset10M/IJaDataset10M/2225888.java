package org.decisiondeck.xmcda_oo.persist;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.apache.xmlbeans.XmlException;
import org.decisiondeck.xmcda_oo.aggregators.GroupOrderedAssignmentsA;
import org.decisiondeck.xmcda_oo.aggregators.GroupOrderedAssignmentsAPGroupCoalitions;
import org.decisiondeck.xmcda_oo.aggregators.SortingProblem;
import org.decisiondeck.xmcda_oo.aggregators.Thresholds;
import org.decisiondeck.xmcda_oo.structure.DecisionMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistSituations {

    private static final Logger s_logger = LoggerFactory.getLogger(PersistSituations.class);

    public void persist(final GroupOrderedAssignmentsAPGroupCoalitions data, String pathName) throws IOException, XmlException {
        final DocManip all = new DocManip();
        appendToDoc(all, data);
        all.saveTo(pathName);
    }

    public void appendToDoc(final DocManip doc, final GroupOrderedAssignmentsAPGroupCoalitions data) {
        final org.decisiondeck.xmcda_2_0_0.Alternatives xmlAlts = new PersistAlternatives().writeAlternatives(data.getAllAlternatives());
        doc.append(xmlAlts);
        final PersistCategories pCats = new PersistCategories();
        final org.decisiondeck.xmcda_2_0_0.Categories xmlCats = pCats.writeCategories(data.getCategories());
        doc.append(xmlCats);
        final org.decisiondeck.xmcda_2_0_0.CategoriesProfiles xmlCatsProfs = pCats.getWrittenXmlProfs();
        doc.append(xmlCatsProfs);
        final Iterator<DecisionMaker> dmIter = data.getDms().iterator();
        final org.decisiondeck.xmcda_2_0_0.Criteria xmlDataCrits;
        final PersistCriteria persistCriteria = new PersistCriteria();
        if (dmIter.hasNext()) {
            final DecisionMaker dm = dmIter.next();
            final Thresholds thresholds = data.getThresholds(dm);
            if (thresholds.getCriteria().isEmpty()) {
                s_logger.warn("No thresholds found for one of the decision makers, skipping thresholds.");
                xmlDataCrits = persistCriteria.writeCriteria(data.getCriteria());
            } else {
                s_logger.warn("Using only the thresholds of one of the decision makers.");
                xmlDataCrits = persistCriteria.writeCriteria(data.getCriteria(), thresholds.getPrefs(), thresholds.getIndiffs(), thresholds.getVetoes());
            }
        } else {
            xmlDataCrits = persistCriteria.writeCriteria(data.getCriteria());
        }
        doc.append(xmlDataCrits);
        final org.decisiondeck.xmcda_2_0_0.PerformanceTable xmlEvals = new PersistEvaluations().writeEvaluations(data.getAllEvaluations());
        doc.append(xmlEvals);
        final org.decisiondeck.xmcda_2_0_0.MethodParameters xmlDms = new PersistDecisionMakers().writeDecisionMakers(data.getDms());
        doc.append(xmlDms);
        final Collection<org.decisiondeck.xmcda_2_0_0.CriteriaSet> xmlCoals = new PersistCriteria().writeCoalitions(data.getAllCoalitions());
        for (org.decisiondeck.xmcda_2_0_0.CriteriaSet xmlCoal : xmlCoals) {
            doc.append(xmlCoal);
        }
        final Collection<org.decisiondeck.xmcda_2_0_0.AlternativesAffectations> xmlAllAssignments = new PersistAssignments().writeOrderedAssignments(data.getAllOrderedAssignments());
        for (org.decisiondeck.xmcda_2_0_0.AlternativesAffectations xmlAssignments : xmlAllAssignments) {
            doc.append(xmlAssignments);
        }
    }

    public void persist(GroupOrderedAssignmentsA data, String pathName) throws IOException, XmlException {
        final DocManip all = new DocManip();
        appendToDoc(all, data);
        all.saveTo(pathName);
    }

    public void appendToDoc(DocManip doc, SortingProblem data) {
        final org.decisiondeck.xmcda_2_0_0.Alternatives xmlAlts = new PersistAlternatives().writeAlternatives(data.getAllAlternatives());
        doc.append(xmlAlts);
        final PersistCategories pCats = new PersistCategories();
        final org.decisiondeck.xmcda_2_0_0.Categories xmlCats = pCats.writeCategories(data.getCategories());
        doc.append(xmlCats);
        final org.decisiondeck.xmcda_2_0_0.CategoriesProfiles xmlCatsProfs = pCats.getWrittenXmlProfs();
        doc.append(xmlCatsProfs);
        final org.decisiondeck.xmcda_2_0_0.Criteria xmlDataCrits = new PersistCriteria().writeCriteria(data.getCriteria(), data.getThresholds().getPrefs(), data.getThresholds().getIndiffs(), data.getThresholds().getVetoes());
        doc.append(xmlDataCrits);
        final org.decisiondeck.xmcda_2_0_0.PerformanceTable xmlEvals = new PersistEvaluations().writeEvaluations(data.getAllEvaluations());
        doc.append(xmlEvals);
        final org.decisiondeck.xmcda_2_0_0.CriteriaSet xmlDataCoals = new PersistCriteria().writeCoalitions(data.getCoalitions());
        doc.append(xmlDataCoals);
    }

    public void appendToDoc(DocManip doc, GroupOrderedAssignmentsA data) {
        final org.decisiondeck.xmcda_2_0_0.Alternatives xmlAlts = new PersistAlternatives().writeAlternatives(data.getAllAlternatives());
        doc.append(xmlAlts);
        final PersistCategories pCats = new PersistCategories();
        final org.decisiondeck.xmcda_2_0_0.Categories xmlCats = pCats.writeCategories(data.getCategories());
        doc.append(xmlCats);
        final org.decisiondeck.xmcda_2_0_0.CategoriesProfiles xmlCatsProfs = pCats.getWrittenXmlProfs();
        doc.append(xmlCatsProfs);
        final org.decisiondeck.xmcda_2_0_0.Criteria xmlDataCrits = new PersistCriteria().writeCriteria(data.getCriteria());
        doc.append(xmlDataCrits);
        final org.decisiondeck.xmcda_2_0_0.PerformanceTable xmlEvals = new PersistEvaluations().writeEvaluations(data.getAssignedEvaluations());
        doc.append(xmlEvals);
        final org.decisiondeck.xmcda_2_0_0.MethodParameters xmlDms = new PersistDecisionMakers().writeDecisionMakers(data.getDms());
        doc.append(xmlDms);
        final Collection<org.decisiondeck.xmcda_2_0_0.AlternativesAffectations> xmlAllAssignments = new PersistAssignments().writeOrderedAssignments(data.getAllOrderedAssignments());
        for (org.decisiondeck.xmcda_2_0_0.AlternativesAffectations xmlAssignments : xmlAllAssignments) {
            doc.append(xmlAssignments);
        }
    }
}
