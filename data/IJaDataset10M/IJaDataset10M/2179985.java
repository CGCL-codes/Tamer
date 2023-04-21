package org.mobicents.servlet.sip.catalina.rules;

import org.mobicents.servlet.sip.core.descriptor.MatchingRule;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Thomas Leseney
 */
public class MatchingRuleParser {

    public static MatchingRule buildRule(Element e) {
        String name = e.getNodeName();
        if ("and".equals(name)) {
            AndRule and = new AndRule();
            NodeList list = e.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node n = list.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    and.addCriterion(buildRule((Element) n));
                }
            }
            return and;
        } else if ("equal".equals(name)) {
            String var = getString(e, "var");
            String value = getString(e, "value");
            boolean ignoreCase = "true".equalsIgnoreCase(e.getAttribute("ignore-case"));
            return new EqualsRule(var, value, ignoreCase);
        } else if ("subdomain-of".equals(name)) {
            String var = getString(e, "var");
            String value = getString(e, "value");
            return new SubdomainRule(var, value);
        } else if ("or".equals(name)) {
            OrRule or = new OrRule();
            NodeList list = e.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node n = list.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    or.addCriterion(buildRule((Element) n));
                }
            }
            return or;
        } else if ("not".equals(name)) {
            NotRule not = new NotRule();
            NodeList list = e.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node n = list.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    not.setCriterion(buildRule((Element) n));
                }
            }
            return not;
        } else if ("contains".equals(name)) {
            String var = getString(e, "var");
            String value = getString(e, "value");
            boolean ignoreCase = "true".equalsIgnoreCase(e.getAttribute("ignore-case"));
            return new ContainsRule(var, value, ignoreCase);
        } else if ("exists".equals(name)) {
            return new ExistsRule(getString(e, "var"));
        } else {
            throw new IllegalArgumentException("Unknown rule: " + name);
        }
    }

    public static String getString(Element e, String name) {
        NodeList list = e.getElementsByTagName(name);
        return ((Element) list.item(0)).getTextContent();
    }
}
