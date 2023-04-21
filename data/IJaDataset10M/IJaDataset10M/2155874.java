package org.decisiondeck.xmcda_oo.structure;

import java.util.Map;
import java.util.Set;
import org.decisiondeck.xmcda_oo.structure.Criterion.PreferenceDirection;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class CriteriaDirections {

    /**
     * A temporary method to help refactoring. Should separate the direction and the criterion objects!
     * 
     * @param criteria
     *            not <code>null</code>.
     * @return not <code>null</code>.
     */
    public static Map<Criterion, PreferenceDirection> getDirections(Set<Criterion> criteria) {
        final Map<Criterion, PreferenceDirection> directions = Maps.newHashMap();
        for (Criterion criterion : criteria) {
            directions.put(criterion, criterion.getPreferenceDirection());
        }
        return directions;
    }

    /**
     * A temporary method to help refactoring. Should separate the direction and the criterion objects!
     * 
     * @param criteria
     *            not <code>null</code>.
     * @return not <code>null</code>.
     */
    public static Map<Criterion, IOrderedInterval> getDirectionsAsScales(Set<Criterion> criteria) {
        final Map<Criterion, IOrderedInterval> directions = Maps.newHashMap();
        for (Criterion criterion : criteria) {
            directions.put(criterion, OrderedInterval.newDirection(criterion.getPreferenceDirection()));
        }
        return directions;
    }

    public static Map<Criterion, PreferenceDirection> getDirectionsFromScales(Map<Criterion, IOrderedInterval> scales) {
        return Maps.transformValues(scales, new Function<IOrderedInterval, PreferenceDirection>() {

            @Override
            public PreferenceDirection apply(IOrderedInterval input) {
                return input.getPreferenceDirection();
            }
        });
    }
}
