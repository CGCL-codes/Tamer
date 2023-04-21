package org.archive.modules.deciderules;

import org.archive.modules.ProcessorURI;
import org.archive.state.Key;
import org.archive.state.KeyManager;

/**
 * Rule REJECTs any CrawlURIs whose total number of path-segments (as
 * indicated by the count of '/' characters not including the first '//')
 * is over a given threshold.
 *
 * @author gojomo
 */
public class TooManyPathSegmentsDecideRule extends PredicatedRejectDecideRule {

    private static final long serialVersionUID = 3L;

    /**
     * Number of path segments beyond which this rule will reject URIs.
     */
    public static final Key<Integer> MAX_PATH_DEPTH = Key.make(20);

    static {
        KeyManager.addKeys(TooManyPathSegmentsDecideRule.class);
    }

    /**
     * Usual constructor. 
     */
    public TooManyPathSegmentsDecideRule() {
    }

    /**
     * Evaluate whether given object is over the threshold number of
     * path-segments.
     * 
     * @param object
     * @return true if the path-segments is exceeded
     */
    @Override
    protected boolean evaluate(ProcessorURI curi) {
        String uri = curi.toString();
        int count = 0;
        int threshold = curi.get(this, MAX_PATH_DEPTH);
        for (int i = 0; i < uri.length(); i++) {
            if (uri.charAt(i) == '/') {
                count++;
            }
            if (count > threshold) {
                return true;
            }
        }
        return false;
    }
}
