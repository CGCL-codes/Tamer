package org.archive.crawler.deciderules;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;

/**
 * Rule REJECTs any CrawlURIs whose total number of path-segments (as
 * indicated by the count of '/' characters not including the first '//')
 * is over a given threshold.
 *
 * @author gojomo
 */
public class TooManyPathSegmentsDecideRule extends PredicatedDecideRule {

    private static final long serialVersionUID = 147079100367815075L;

    public static final String ATTR_MAX_PATH_DEPTH = "max-path-depth";

    /**
     * Default maximum value.
     * Default access so available to unit test.
     */
    static final Integer DEFAULT_MAX_PATH_DEPTH = new Integer(20);

    /**
     * Usual constructor. 
     * @param name Name of this DecideRule.
     */
    public TooManyPathSegmentsDecideRule(String name) {
        super(name);
        setDescription("TooManyPathSegmentsDecideRule. REJECTs URIs with " + "more total path-segments (as indicated by '/' characters) " + "than the configured '" + ATTR_MAX_PATH_DEPTH + "'.");
        Type type = addElementToDefinition(new SimpleType(ATTR_DECISION, "Decision to be applied", REJECT, ALLOWED_TYPES));
        type.setTransient(true);
        addElementToDefinition(new SimpleType(ATTR_MAX_PATH_DEPTH, "Number of" + " path segments beyond which this rule will reject URIs.", DEFAULT_MAX_PATH_DEPTH));
    }

    /**
     * Evaluate whether given object is over the threshold number of
     * path-segments.
     * 
     * @param object
     * @return true if the path-segments is exceeded
     */
    protected boolean evaluate(Object object) {
        boolean result = false;
        CandidateURI curi = null;
        try {
            curi = (CandidateURI) object;
        } catch (ClassCastException e) {
            return result;
        }
        String uri = curi.toString();
        int count = 0;
        int threshold = getThresholdSegments(object);
        for (int i = 0; i < uri.length(); i++) {
            if (uri.charAt(i) == '/') {
                count++;
            }
            if (count > threshold) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * @param obj
     * @return path-segments cutoff threshold
     */
    private int getThresholdSegments(Object obj) {
        return ((Integer) getUncheckedAttribute(obj, ATTR_MAX_PATH_DEPTH)).intValue() + 2;
    }
}
