package org.progeeks.parser.proto.ast;

import java.util.*;
import org.progeeks.util.log.*;
import org.progeeks.parser.proto.*;

/**
 *  A rule that returns true if a sequence of sub-rules returns true.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class SequenceRule implements IteratorRule {

    static Log log = Log.getLog(SequenceRule.class);

    private IteratorRule[] rules;

    public SequenceRule(IteratorRule[] rules) {
        this.rules = rules;
    }

    public SequenceRule(List rules) {
        this.rules = new IteratorRule[rules.size()];
        this.rules = (IteratorRule[]) rules.toArray(this.rules);
    }

    /**
     *  Returns COMPLETE for a String that matches
     *  the rule's regular expression.  FAILED for anything
     *  else.  The token will be swallowed either way and it
     *  is up to the caller to revert if necessary.
     */
    public int matches(ListIterator it) {
        int i;
        for (i = 0; i < rules.length; i++) {
            if (log.isDebugEnabled()) log.debug("Rule:" + rules[i]);
            if (!it.hasNext()) {
                log.debug("-INCOMPLETE... ran out of elements.");
                return (INCOMPLETE);
            }
            int result = rules[i].matches(it);
            switch(result) {
                case MATCHES:
                    log.debug("-MATCHES");
                    break;
                case INCOMPLETE:
                    log.debug("-INCOMPLETE");
                    return (INCOMPLETE);
                case FAILED:
                    log.debug("-FAILED");
                    return (FAILED);
            }
        }
        log.debug("return MATCHES");
        return (MATCHES);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("SequenceRule[");
        for (int i = 0; i < rules.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(rules[i]);
        }
        sb.append("]");
        return (sb.toString());
    }
}
