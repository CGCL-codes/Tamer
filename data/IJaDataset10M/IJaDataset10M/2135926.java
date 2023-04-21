package de.sonivis.tool.mediawikiconnector.metrics.infospaceitem;

import java.util.Date;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.eventhandling.INetworkFilter;
import de.sonivis.tool.core.metricsystem.AbstractMetricException;
import de.sonivis.tool.core.metricsystem.InfoSpaceItemMetric;
import de.sonivis.tool.core.metricsystem.MetricCategory;
import de.sonivis.tool.core.metricsystem.MetricNotImplementedException;

/**
 * Metric: Number of included external links
 * 
 * @author Benedikt
 * @version $Date$ <br>
 *          $Rev$
 */
public class IncludedExternalLinkCount extends InfoSpaceItemMetric<Integer> {

    public static final String NAME = "Included External Link Count";

    public IncludedExternalLinkCount() {
        super(NAME);
        super.addToCategory(MetricCategory.INFOSPACEITEM_METRICS);
        super.setDescription("Number of external links");
    }

    protected final Integer calculateValueFor(final ContentElement measuredEntity, Date startDate, Date endDate, INetworkFilter filter) throws AbstractMetricException {
        throw new MetricNotImplementedException(this);
    }

    @Override
    public final Class<Integer> getValueType() {
        return int.class;
    }
}
