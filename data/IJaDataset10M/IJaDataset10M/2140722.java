package org.archive.modules.deciderules;

import org.archive.modules.ProcessorURI;
import org.archive.state.KeyManager;

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

    static {
        KeyManager.addKeys(HopsPathMatchesRegExpDecideRule.class);
    }
}
