package de.sonivis.tool.mediawikiconnector.metrics.node;

import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.CoreTooling;
import de.sonivis.tool.core.datamodel.ENetworkType;
import de.sonivis.tool.core.datamodel.INetwork;
import de.sonivis.tool.core.datamodel.INode;
import de.sonivis.tool.core.metricsystem.AbstractMetricException;
import de.sonivis.tool.core.metricsystem.MetricCalculationException;
import de.sonivis.tool.core.metricsystem.MetricCalculationSQLException;
import de.sonivis.tool.core.metricsystem.MetricCategory;
import de.sonivis.tool.core.metricsystem.MetricNotDefinedException;
import de.sonivis.tool.core.metricsystem.NoMeasurableGivenException;
import de.sonivis.tool.core.metricsystem.NodeMetric;
import de.sonivis.tool.mediawikiconnector.MetricRevDiffHelper;

/**
 * Metric: Number of minor edits for a node (Page)
 * @author Benedikt
 */
public class MinorRevisionCountMetric extends NodeMetric<Integer> {

    public static final String NAME = "Minor Revision Count";

    private static final Logger LOGGER = LoggerFactory.getLogger(MinorRevisionCountMetric.class.getName());

    public MinorRevisionCountMetric() {
        super(NAME);
        super.addToCategory(MetricCategory.NODE_METRICS);
        super.setDescription("Number of minor edits for a node (Page)");
    }

    @Override
    protected final Integer calculateValueFor(final INode measuredEntity) throws AbstractMetricException {
        if (measuredEntity == null) {
            throw new NoMeasurableGivenException(this);
        }
        final Integer result = calculateValuesForAllNodes(measuredEntity.getNetwork()).get(measuredEntity);
        if (result == null) {
            throw new MetricCalculationException(this);
        }
        return result;
    }

    @Override
    protected final Map<INode, Integer> calculateValuesForAllNodes(final INetwork network) throws AbstractMetricException {
        if (network == null) {
            throw new NoMeasurableGivenException(this);
        }
        if (!network.getFilter().getNetworkType().equals(ENetworkType.WIKI_LINK)) {
            throw new MetricNotDefinedException(this, network.getFilter().getNetworkType().toString());
        }
        try {
            final long startTime = System.nanoTime();
            final Map<INode, Integer> result = MetricRevDiffHelper.getInstance().getNodeMinorEditCount(network);
            if (result == null) {
                throw new MetricCalculationException(this, "MetricRevDiffHelper returned null.");
            }
            for (final INode node : network.getNodes()) {
                if (result.get(node) != null) {
                    this.setValueFor(node, result.get(node));
                }
            }
            setValueForAll(network, result);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(this.getName() + " added on Nodes in " + ((System.nanoTime() - startTime) * CoreTooling.NANO_TIME_FACTOR) + CoreTooling.NANO_TIME_IDENTIFIER);
            }
            return result;
        } catch (final SQLException e) {
            throw new MetricCalculationSQLException(this, e);
        }
    }

    @Override
    public final Class<Integer> getValueType() {
        return int.class;
    }
}
