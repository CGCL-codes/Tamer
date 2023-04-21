package de.lichtflut.infra.html;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 * 
 * <p>
 * 	Created 07.08.2009
 * </p>
 *
 * @author Nils Bleisch
 */
public final class HtmlFilter {

    private Map<WellKnownElement, WellKnownElement> filterRules = new HashMap<WellKnownElement, WellKnownElement>();

    public void addFilterRule(WellKnownElement rule) {
        if (!isFilterRule(rule)) filterRules.put(rule, rule);
    }

    public void setFilterRules(Map<WellKnownElement, WellKnownElement> filterRules) {
        reset(filterRules);
    }

    public Map<WellKnownElement, WellKnownElement> getFilterRules() {
        return filterRules;
    }

    public void removeFilterRule(WellKnownElement rule) {
        filterRules.remove(rule);
    }

    public boolean isFilterRule(WellKnownElement elem) {
        return filterRules.containsKey(elem);
    }

    public void reset() {
        filterRules = new HashMap<WellKnownElement, WellKnownElement>();
    }

    public void reset(Map<WellKnownElement, WellKnownElement> filterRules) {
        reset();
        this.filterRules.putAll(filterRules);
    }

    public void invert() {
        for (WellKnownElement elem : WellKnownElement.values()) {
            if (!isFilterRule(elem)) filterRules.put(elem, elem); else removeFilterRule(elem);
        }
    }
}
