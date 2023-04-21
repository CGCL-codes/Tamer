package org.progeeks.parser.proto.ast;

import java.util.*;
import org.progeeks.util.log.*;
import org.progeeks.parser.proto.*;

/**
 *  A rule that processes a single rule repeatedly until it fails.
 *  The result is then returned based on the status of the iterator.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class RepeatRule implements IteratorRule {

    static Log log = Log.getLog(RepeatRule.class);

    private IteratorRule rule;

    private boolean atLeastOne;

    public RepeatRule(IteratorRule rule, boolean atLeastOne) {
        this.rule = rule;
        this.atLeastOne = atLeastOne;
    }

    public RepeatRule(IteratorRule rule) {
        this(rule, false);
    }

    /**
     *  Processes a rule repeatedly as long as it continues
     *  to return MATCHES.  If it returns INCOMPLETE then this
     *  method will return INCOMPLETE.  If it returns FAILED
     *  then we check the count to determine whether or not to
     *  return INCOMPLETE or MATCHES.  INCOMPLETE is returned
     *  if we reached the end of the iterator before running out
     *  of elements that matched or if there was supposed to be
     *  at least one element and it wasn't found.
     */
    public int matches(ListIterator it) {
        int count = 0;
        while (it.hasNext()) {
            if (log.isDebugEnabled()) log.debug("Rule:" + rule);
            int index = it.nextIndex();
            int result = rule.matches(it);
            switch(result) {
                case MATCHES:
                    log.debug("-MATCHES");
                    count++;
                    break;
                case INCOMPLETE:
                    log.debug("-INCOMPLETE");
                    return (INCOMPLETE);
                case FAILED:
                    log.debug("-FAILED... rolling back");
                    while (index != it.nextIndex()) {
                        if (log.isDebugEnabled()) log.debug("   index:" + index + "   nextIndex:" + it.nextIndex());
                        it.previous();
                    }
                    if (log.isDebugEnabled()) log.debug("Found " + count + " matches.  Need at least one? " + atLeastOne);
                    if (atLeastOne && count == 0) {
                        log.debug("Return FAILED");
                        return (FAILED);
                    }
                    log.debug("Return MATCHES");
                    return (MATCHES);
            }
        }
        log.debug("Return INCOMPLETE");
        return (INCOMPLETE);
    }

    public String toString() {
        return ("RepeatRule[" + rule + "]");
    }
}
