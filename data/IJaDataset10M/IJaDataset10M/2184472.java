package org.mobicents.servlet.sip.catalina.rules;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.sip.SipServletRequest;
import org.mobicents.servlet.sip.core.descriptor.MatchingRule;

/**
 * @author Thomas Leseney
 */
public class AndRule implements MatchingRule {

    private List<MatchingRule> criteria = new ArrayList<MatchingRule>();

    public AndRule() {
    }

    public void addCriterion(MatchingRule c) {
        criteria.add(c);
    }

    public boolean matches(SipServletRequest request) {
        for (MatchingRule rule : criteria) {
            if (!rule.matches(request)) return false;
        }
        return true;
    }

    public String getExpression() {
        StringBuffer sb = new StringBuffer("(");
        boolean first = true;
        for (MatchingRule rule : criteria) {
            if (first) {
                first = false;
            } else {
                sb.append(" and ");
            }
            sb.append(rule.getExpression());
        }
        sb.append(")");
        return sb.toString();
    }
}
