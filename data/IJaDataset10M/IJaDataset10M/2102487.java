package org.archive.crawler.deciderules;

import org.archive.crawler.datamodel.CandidateURI;

/**
 * Rule applies the configured decision for any URI which has a 'via' 
 * (essentially, any URI that was a seed or some kinds of mid-crawl adds).
 *
 * @author gojomo
 */
public class HasViaDecideRule extends PredicatedDecideRule {

    private static final long serialVersionUID = 1670292311303097735L;

    /**
     * Usual constructor. 
     * @param name Name of this DecideRule.
     */
    public HasViaDecideRule(String name) {
        super(name);
        setDescription("HasViaDecideRule. Applies configured decision " + "to any URI that has a 'via'.");
    }

    /**
     * Evaluate whether given object is over the threshold number of
     * hops.
     * 
     * @param object
     * @return true if the mx-hops is exceeded
     */
    protected boolean evaluate(Object object) {
        try {
            CandidateURI curi = (CandidateURI) object;
            return curi.getVia() != null;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
