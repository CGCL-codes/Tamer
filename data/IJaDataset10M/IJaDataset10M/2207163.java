package org.actioncenters.core.contribution.data.extentions.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.actioncenters.core.contribution.data.IContribution;
import org.actioncenters.core.contribution.data.extentions.IPopulationRule;
import org.actioncenters.core.contribution.data.impl.Contribution;

/**
 * Population rule value object.
 *
 * @author dkjeldgaard
 */
public class PopulationRule extends Contribution implements IPopulationRule {

    /** Auto-generated UID. */
    private static final long serialVersionUID = 5726474095983026576L;

    /** List of drop event configurations. */
    private List<IContribution> dropEvents = new ArrayList<IContribution>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IContribution> getDropEvents() {
        return dropEvents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getValueMap() {
        Map<String, Object> returnValue = super.getValueMap();
        addProperty(returnValue, DROP_EVENTS, getValueList(getDropEvents()));
        return returnValue;
    }
}
