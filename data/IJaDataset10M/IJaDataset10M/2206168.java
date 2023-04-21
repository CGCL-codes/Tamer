package de.sonivis.tool.metrics.network;

import java.util.Date;
import de.sonivis.tool.core.datamodel.Graph;
import de.sonivis.tool.core.eventhandling.INetworkFilter;
import de.sonivis.tool.core.gnu.r.AbstractRException;
import de.sonivis.tool.core.gnu.r.RManager;
import de.sonivis.tool.core.metricsystem.AbstractMetric;
import de.sonivis.tool.core.metricsystem.AbstractMetricException;
import de.sonivis.tool.core.metricsystem.MetricCalculationException;
import de.sonivis.tool.core.metricsystem.MetricCategory;
import de.sonivis.tool.core.metricsystem.NetworkMetric;
import de.sonivis.tool.core.metricsystem.NoMeasurableGivenException;

/**
 * Calculates network's diameter (maximal geodetic between two nodes in a network).
 * 
 * @author Benedikt <br>
 *         Andreas Erber
 * @see de.sonivis.tool.core.metricsystem.AbstractMetric
 * @version $Date: 2010-04-07 15:28:53 -0400 (Wed, 07 Apr 2010) $ <br>
 *          $Rev: 1626 $
 */
public class DiameterMetric extends NetworkMetric<Double> {

    /**
	 * This metric's name.
	 */
    public static final String NAME = "Diameter";

    /**
	 * Default constructor.
	 */
    public DiameterMetric() {
        super(NAME);
        super.addToCategory(MetricCategory.NETWORK_METRICS);
        super.setDescription("Calculates network's diameter (maximal geodetic between two nodes in a network).");
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see NetworkMetric#calculateValueFor(Graph, Date, Date, INetworkFilter)
	 */
    @Override
    protected final Double calculateValueFor(final Graph measuredNetwork, final Date startDate, final Date endDate, final INetworkFilter filter) throws AbstractMetricException {
        if (measuredNetwork == null) {
            throw new NoMeasurableGivenException(this);
        }
        Double result = Double.NaN;
        try {
            result = RManager.getInstance().getMetricFromR(measuredNetwork, "diameter(" + RManager.R_GRAPH + ")");
        } catch (final AbstractRException e) {
            throw new MetricCalculationException(this, e);
        }
        if (result == null) {
            throw new MetricCalculationException(this, "R returned null.");
        }
        this.setValueFor(measuredNetwork, result);
        return result;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractMetric#getValueType()
	 */
    @Override
    public final Class<Double> getValueType() {
        return double.class;
    }
}
