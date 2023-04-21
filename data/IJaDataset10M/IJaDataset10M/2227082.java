package org.helium.transept.criteria;

import gnu.regexp.RE;
import org.w3c.dom.Element;
import org.helium.transept.framework.Criterion;
import org.helium.transept.http.Request;

public class HostCriterionFactory extends GenericRegexCriterionFactory {

    protected String getParamName() {
        return "host";
    }

    protected String getCriterionName() {
        return "name";
    }

    protected Criterion actuallyMakeCriterion(Element params, RE regex, boolean negated) {
        return new HostCriterion(regex, negated);
    }

    private class HostCriterion implements Criterion {

        private boolean negated = false;

        private RE hostRegex = null;

        public HostCriterion(RE h, boolean n) {
            hostRegex = h;
            negated = n;
        }

        public boolean match(Request req) {
            String host = req.getHost();
            System.out.println("Matching against host \"" + host + "\"");
            if (host == null) {
                return negated;
            } else {
                return (hostRegex.getMatch(host) != null) ^ negated;
            }
        }
    }
}
