package org.archive.processors.deciderules;

import java.util.regex.Pattern;
import org.archive.processors.ProcessorURI;
import org.archive.state.Key;

/**
 * Rule applies configured decision to any ProcessorURIs whose String URI
 * matches the supplied regexp.
 *
 * @author gojomo
 */
public class MatchesRegExpDecideRule extends PredicatedAcceptDecideRule {

    private static final long serialVersionUID = 2L;

    public static final Key<Pattern> REGEXP = Key.make(Pattern.compile("."));

    /**
     * Usual constructor. 
     */
    public MatchesRegExpDecideRule() {
    }

    /**
     * Evaluate whether given object's string version
     * matches configured regexp
     * 
     * @param object
     * @return true if regexp is matched
     */
    @Override
    protected boolean evaluate(ProcessorURI uri) {
        Pattern p = getPattern(uri);
        return p.matcher(getString(uri)).matches();
    }

    protected Pattern getPattern(ProcessorURI uri) {
        return uri.get(this, REGEXP);
    }

    protected String getString(ProcessorURI uri) {
        return uri.toString();
    }
}
