package eu.vph.predict.vre.in_silico.business.integration.aggregator;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.vph.predict.vre.base.exception.VREBusinessException;
import eu.vph.predict.vre.in_silico.business.integration.AbstractIntegrationBean;
import eu.vph.predict.vre.in_silico.business.integration.message.value.MessageRunSimulationJob;
import eu.vph.predict.vre.in_silico.entity.simulation.AbstractSimulation;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationJob;

/**
 * Service activator to aggregate simulation jobs post job run.
 *
 * @author Geoff Williams
 */
public class AggregatorSimulationRunJobs extends AbstractIntegrationBean {

    private static final Log log = LogFactory.getLog(AggregatorSimulationRunJobs.class);

    /**
   * Aggregate the simulation jobs into the simulation.
   * 
   * @param messages Simulation job run messages.
   * @return The simulation being run.
   */
    public AbstractSimulation aggregateSimulationRunJobs(final List<MessageRunSimulationJob> messages) throws VREBusinessException {
        log.debug("~int.aggregateSimulationRunJobs(List<MessageRunSimulationJob> : Aggregating simulation jobs");
        final Long simulationId = messages.get(0).getSimulationJob().getSimulation().getId();
        for (final MessageRunSimulationJob message : messages) {
            final SimulationJob simulationJob = message.getSimulationJob();
            sendStatusMessage(simulationJob.getSimulation().getId(), simulationJob.getIdentifier(), "Aggregating simulation jobs (run)", null);
        }
        sendStatusMessage(simulationId, AbstractSimulation.PROGRESS_TARGET_OVERALL, "Aggregating simulation jobs (run)", null);
        final AbstractSimulation simulation = messages.get(0).getSimulationJob().getSimulation();
        simulation.getApplicationInterface().nullifyTransients();
        simulation.getSimulationEnvironment().nullifyTransients();
        return simulation;
    }
}
