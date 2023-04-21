package eu.vph.predict.vre.in_silico.business.manager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import eu.vph.predict.vre.base.entity.lookup.LookupValue;
import eu.vph.predict.vre.base.exception.VREBusinessException;
import eu.vph.predict.vre.in_silico.entity.simulation.AbstractSimulation;
import eu.vph.predict.vre.in_silico.entity.simulation.UserInput;
import eu.vph.predict.vre.in_silico.entity.simulation.SweepConfig;
import eu.vph.predict.vre.in_silico.service.SimulationService.RESULTS_TYPE;
import eu.vph.predict.vre.in_silico.service.SimulationService.RETRIEVAL;
import eu.vph.predict.vre.in_silico.value.authentication.AbstractAuthenticationToken;
import eu.vph.predict.vre.in_silico.value.compute.ResourceProviderVO;
import eu.vph.predict.vre.in_silico.value.configuration.AbstractConfiguration;

/**
 * Simulation management interface to business logic.
 *
 * @author Geoff Williams
 */
public interface SimulationManager {

    /** Component name for consistent reference */
    public static final String COMPONENT_SIMULATION_MANAGER = "componentSimulationManager";

    /**
   * Assign metadata links to simulation user input metadata.
   * 
   * @param simulation Simulation to update values.
   * @param metadataLinkValues New metadata links.
   * @param lvLinkIPC LookupValue representing IPC links.
   */
    public void assignUserInputMetadataLinks(AbstractSimulation simulation, Map<String, Collection<String>> metadataLinkValues, LookupValue lvLinkIPC);

    /**
   * Delete a simulation.
   * 
   * @param simulationId Persistence id of simulation to delete.
   * @throws VREBusinessException If business constraints not met.
   */
    public void deleteSimulation(final Long simulationId) throws VREBusinessException;

    /**
   * Generate the SimulationJobs which the Simulation defines and save the simulation.
   * 
   * @param simulation Simulation defining the input value and sweep config.
   * @throws VREBusinessException If business constraints not met.
   */
    public void generateAndSaveSimulationJobs(AbstractSimulation simulation) throws VREBusinessException;

    /**
   * Undertake tasks required post simulation job completion.
   * 
   * @param simulationId Persistence id of simulation.
   * @param jobIdentifier Job identifier (not persistence id) of simulation job.
   * @throws VREBusinessException If business constraints not met.
   */
    public void onJobComplete(Long simulationId, String jobIdentifier) throws VREBusinessException;

    /**
   * Update according to target having results uploaded.
   * 
   * @param simulationId Persistence id of simulation.
   * @param jobIdentifier Job identifier (not persistence id) of simulation job.
   * @throws VREBusinessException
   */
    public void onJobResultsUploaded(Long simulationId, String jobIdentifier) throws VREBusinessException;

    /**
   * Retrieve indicator to say if results are available.
   *
   * @param simulationId Persistence id of simulation.
   * @param target Target to perform action on (job identifier or simulation).
   * @param resultsType The type of results required.
   * @return Indicator to say if specified (or default) results available or not.
   */
    public boolean resultsAvailable(Long simulationId, String target, RESULTS_TYPE resultsType);

    /**
   * Retrieve a Simulation according to persistence id. 
   * 
   * @param simulationId Persistence id of the simulation.
   * @return Simulation with specified id (or null if not found).
   */
    public AbstractSimulation retrieveSimulation(Long simulationId);

    /**
   * Retrieve a collection of the Simulations in the database.
   *
   * @param retrieval Retrieval type, e.g. {@link RETRIEVAL.MOST_RECENT_FIRST}, or null if no order.
   * @return Simulations which the current user can see.
   */
    public List<AbstractSimulation> retrieveSimulations(RETRIEVAL retrieveBy);

    /**
   * Retrieve a collection of the Simulations in the database for the user, most recent first.
   *
   * @param vreUserId VREUser persistence id.
   * @param retrieveMax Maximum of simulations to retrieve.
   * @return Simulations (up to the max) belonging to the defined user.
   */
    public List<AbstractSimulation> retrieveSimulations(String vreUserId, int retrieveMax);

    /**
   * Run a simulation.
   * 
   * @param simulation The simulation to run.
   * @param resourceProviderVOs ResourceProvider value objects of the resources to run simulation on.
   * @param requiredAuthenticationTokens Authentication tokens required by the simulation.
   * @param requiredConfigurations Configurations required by the simulation.
   * @throws VREBusinessException If business constraints not met.
   */
    public void runSimulation(AbstractSimulation simulation, List<ResourceProviderVO> resourceProviderVOs, Set<AbstractAuthenticationToken> requiredAuthenticationTokens, Set<AbstractConfiguration> requiredConfigurations) throws VREBusinessException;

    /**
   * Assign and save the user-derived method parameter input data
   * 
   * @param simulation Simulation to update.
   * @param userInputs Raw input values (including sweeping values) as they where entered by the user.
   * @param sweepConfig Sweep configuration data.
   * @param resetState Actions the resetting of simulation state to having no prior input before 
   *                   saving new values, e.g. remove any existing UserInputs and SimulationJobs.
   * @return The saved simulation (complete with updated @Version property value!).
   * @throws VREBusinessException If business constraints not met.
   */
    public AbstractSimulation saveMethodParameterInput(final AbstractSimulation simulation, final List<UserInput> userInputs, final SweepConfig sweepConfig, final boolean resetState) throws VREBusinessException;

    /**
   * Save a simulation
   * 
   * @param simulation Simulation to save.
   * @throws VREBusinessException Business constraints not met.
   */
    public void saveSimulation(AbstractSimulation simulation) throws VREBusinessException;
}
