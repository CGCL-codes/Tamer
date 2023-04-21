package de.sonivis.tool.mediawikiconnector.metrics.wiki;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.CorePlugin;
import de.sonivis.tool.core.CoreTooling;
import de.sonivis.tool.core.datamodel.IWiki;
import de.sonivis.tool.core.metricsystem.AbstractMetricException;
import de.sonivis.tool.core.metricsystem.MetricCalculationException;
import de.sonivis.tool.core.metricsystem.MetricCalculationSQLException;
import de.sonivis.tool.core.metricsystem.MetricCategory;
import de.sonivis.tool.core.metricsystem.MetricNotDefinedException;
import de.sonivis.tool.core.metricsystem.NoMeasurableGivenException;
import de.sonivis.tool.core.metricsystem.WikiMetric;
import de.sonivis.tool.mediawikiconnector.MediaWikiConnectorPreferences;
import de.sonivis.tool.mediawikiconnector.MediaWikiUtil;

/**
 * Metric: Number of links for all pages
 * 
 * @author Benedikt, Anne
 */
public class TotalPageLinkCountMetric extends WikiMetric<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TotalPageLinkCountMetric.class.getName());

    public static final String NAME = "Total Page Link Count";

    public TotalPageLinkCountMetric() {
        super(NAME);
        super.addToCategory(MetricCategory.WIKI_METRICS);
        super.setDescription("Number of links for all pages");
    }

    @Override
    public final Integer calculateValueFor(final IWiki measuredWiki) throws AbstractMetricException {
        if (measuredWiki == null) {
            throw new NoMeasurableGivenException(this);
        }
        final Connection connection = MediaWikiUtil.getInstance().getMysqlConnection();
        if (connection != null) {
            if (CorePlugin.getDefault().getPreferenceStore().getBoolean(MediaWikiConnectorPreferences.MEDIAWIKI_STORED_PROCEDURE)) {
                final String sql = "Select count(distinct p.page_to) FROM revision_links p;";
                try {
                    final long startTime = System.nanoTime();
                    final Statement statement = connection.createStatement();
                    final ResultSet resultSet = statement.executeQuery(sql);
                    resultSet.first();
                    final Integer result = resultSet.getInt(1);
                    statement.close();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(this.getName() + " calculated in " + ((System.nanoTime() - startTime) * CoreTooling.NANO_TIME_FACTOR) + CoreTooling.NANO_TIME_IDENTIFIER);
                    }
                    return result;
                } catch (final SQLException e) {
                    throw new MetricCalculationSQLException(this, e);
                }
            } else {
                throw new MetricNotDefinedException(this, " without activated stored procedure.");
            }
        } else {
            throw new MetricCalculationException(this, "No DB connection.");
        }
    }

    @Override
    public final Class<Integer> getValueType() {
        return int.class;
    }
}
