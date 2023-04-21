package org.archive.processors.deciderules;

import org.archive.processors.ProcessorURI;

/**
 * Rule applies configured decision to any CrawlURIs whose 'hops-path'
 * (string like "LLXE" etc.) matches the supplied regexp.
 *
 * @author gojomo
 */
public class HopsPathMatchesRegExpDecideRule extends MatchesRegExpDecideRule {

    private static final long serialVersionUID = 3L;

    /**
     * Usual constructor. 
     * @param name
     */
    public HopsPathMatchesRegExpDecideRule() {
    }

    @Override
    protected String getString(ProcessorURI uri) {
        return uri.getPathFromSeed();
    }
}
