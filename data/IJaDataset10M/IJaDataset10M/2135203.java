package de.sonivis.tool.core.metricsystem;

import java.io.Serializable;
import java.util.Date;
import de.sonivis.tool.core.ModelManager;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.eventhandling.INetworkFilter;

/**
 * Superclass for arbitrary wiki metrics which can be calculated for an extracted information space
 * coming from a wiki-connector.
 * <p>
 * It represents an {@link AbstractMetric} in the SONIVIS metric model.
 * </p>
 * 
 * @author Benedikt
 * @author Andreas Erber
 * @param <ValueType>
 *            Type of this metric value
 * 
 */
public abstract class WikiMetric<ValueType extends Serializable> extends AbstractMetric<ValueType, InfoSpace> {

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractMetric#AbstractMetric(String)
	 */
    public WikiMetric(final String name) {
        super(name);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractMetric#precalculate()
	 */
    @Override
    public final void precalculate() throws AbstractMetricException {
        final InfoSpace currentWiki = ModelManager.getInstance().getCurrentInfoSpace();
        final Date startDate = ModelManager.getInstance().getStartDate();
        final Date endDate = ModelManager.getInstance().getEndDate();
        if (currentWiki != null) {
            getValueFor(currentWiki, startDate, endDate, null);
        }
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractMetric#getMeasurableType()
	 */
    @Override
    public final Class<InfoSpace> getMeasurableType() {
        return InfoSpace.class;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractMetric#calculateValueFor()
	 */
    @Override
    protected ValueType calculateValueFor(final InfoSpace measuredInfoSpace, Date startDate, Date endDate, INetworkFilter filter) throws AbstractMetricException {
        throw (new MethodNotImplementedException(this));
    }
}
