package de.fzi.harmonia.basematcher;

import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyRange;
import de.fzi.harmonia.basematcher.helper.DomainRangeDistanceHelper;
import de.fzi.kadmos.api.Correspondence;

/**
 * This base matcher computes a distance for two properties by inspecting
 * the distances between their domain and range classes. Complex Domain/Range
 * classes are not supported yet. DataProperties are also not supported yet.
 * 
 * @author bock
 *
 */
public class PropertyByDRClassMatcher extends AbstractPropertyBaseMatcher {

    private Log logger = LogFactory.getLog(PropertyByDRClassMatcher.class);

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends OWLEntity> double computeDistance(T entity1, T entity2) {
        double result = 1.d;
        Set<OWLClassExpression> domains1 = ((OWLProperty<?, ?>) entity1).getDomains(alignment.getOntology1());
        Set<OWLClassExpression> domains2 = ((OWLProperty<?, ?>) entity2).getDomains(alignment.getOntology2());
        Set<OWLPropertyRange> ranges1 = ((OWLProperty<OWLPropertyRange, ?>) entity1).getRanges(alignment.getOntology1());
        Set<OWLPropertyRange> ranges2 = ((OWLProperty<OWLPropertyRange, ?>) entity2).getRanges(alignment.getOntology2());
        int maxDomainMatch = Math.min(domains1.size(), domains2.size());
        int maxRangeMatch = Math.min(ranges1.size(), ranges2.size());
        double sumDomainDist = 0.;
        double sumRangeDist = 0.;
        int nbDomainMatch = 0;
        int nbRangeMatch = 0;
        if (maxDomainMatch != 0) {
            for (OWLClassExpression dom1 : domains1) if (dom1 instanceof OWLClass) if (alignment.containsEntity1((OWLClass) dom1)) for (Correspondence<? extends OWLEntity> domCorr : getCorrespondencesOfEntity((OWLClass) dom1)) {
                if (domains2.contains(domCorr.getEntity2())) {
                    sumDomainDist += (1. - domCorr.getConfidence());
                    nbDomainMatch++;
                }
            }
        }
        if (maxRangeMatch != 0) {
            for (OWLObject range1 : ranges1) {
                if (range1 instanceof OWLClassExpression) {
                    if (range1 instanceof OWLClass) {
                        if (alignment.containsEntity1((OWLClass) range1)) {
                            for (Correspondence<? extends OWLEntity> rangeCorr : getCorrespondencesOfEntity((OWLClass) range1)) {
                                if (ranges2.contains(rangeCorr.getEntity2())) {
                                    sumRangeDist += (1. - rangeCorr.getConfidence());
                                    nbRangeMatch++;
                                }
                            }
                        }
                    }
                } else if (range1 instanceof OWLDatatype) {
                    if (ranges2.contains(range1)) {
                        sumRangeDist += 0.;
                        nbRangeMatch++;
                    }
                }
            }
        }
        result = DomainRangeDistanceHelper.computeDomainRangeDistances(maxDomainMatch, nbDomainMatch, sumDomainDist, maxRangeMatch, nbRangeMatch, sumRangeDist);
        if (result == -1.) result = 1.d;
        if (logger.isDebugEnabled()) logger.debug("Property by Domain/Range class distance between " + entity1.toString() + " and " + entity2.toString() + " is " + result);
        return result;
    }
}
