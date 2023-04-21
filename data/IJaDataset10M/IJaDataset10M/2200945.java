package de.sonivis.tool.mediawikiconnector.metrics.infospaceitem;

import java.util.Date;
import java.util.Map;
import de.sonivis.tool.core.datamodel.Actor;
import de.sonivis.tool.core.datamodel.Node;
import de.sonivis.tool.core.eventhandling.INetworkFilter;
import de.sonivis.tool.core.metricsystem.AbstractMetricException;
import de.sonivis.tool.core.metricsystem.InfoSpaceItemMetric;
import de.sonivis.tool.core.metricsystem.MetricCalculationException;
import de.sonivis.tool.core.metricsystem.MetricCategory;
import de.sonivis.tool.core.metricsystem.MetricNotImplementedException;
import de.sonivis.tool.core.metricsystem.NoMeasurableGivenException;

/**
 * Metric: Number of added external links
 * 
 * @author Benedikt
 * @version $Date$ <br>
 *          $Rev$
 */
public class AddedExternalLinkCount extends InfoSpaceItemMetric<Integer> {

    public static final String NAME = "Added External Link Count";

    public AddedExternalLinkCount() {
        super(NAME);
        super.addToCategory(MetricCategory.INFOSPACEITEM_METRICS);
        super.setDescription("Number of external links added by an author");
    }

    protected final Integer calculateValueFor(final Actor measuredEntity, Date startDate, Date endDate, INetworkFilter filter) throws AbstractMetricException {
        if (measuredEntity == null) {
            throw new NoMeasurableGivenException(this);
        }
        final Integer result = calculateValuesForAll(measuredEntity, null, null, null).get(measuredEntity);
        if (result == null) {
            throw new MetricCalculationException(this);
        }
        return result;
    }

    protected final Map<Node, Integer> calculateValuesForAll(final Actor actor, Date startDate, Date endDate, INetworkFilter filter) throws AbstractMetricException {
        throw new MetricNotImplementedException(this);
    }

    @Override
    public final Class<Integer> getValueType() {
        return int.class;
    }
}
