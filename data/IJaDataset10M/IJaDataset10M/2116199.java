package eu.vph.predict.vre.in_silico.business.integration.service_activator;

import java.io.File;
import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.vph.predict.vre.base.exception.MessageKeys;
import eu.vph.predict.vre.base.exception.VRESystemException;
import eu.vph.predict.vre.in_silico.business.integration.AbstractIntegrationBean;
import eu.vph.predict.vre.in_silico.business.integration.message.value.MessageRunSimulation;
import eu.vph.predict.vre.in_silico.business.manager.ApplicationManager;
import eu.vph.predict.vre.in_silico.entity.application.AbstractSimulationEnvironment;
import eu.vph.predict.vre.in_silico.entity.simulation.AbstractSimulation;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationAPPreDiCT;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationChaste;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationQTPreDiCT;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationTorsadePreDiCT;
import eu.vph.predict.vre.in_silico.exception.InSilicoMessageKeys;
import eu.vph.predict.vre.in_silico.service.MessageService;

/**
 * Abstract functionality for loading parameter files into integration messages (for later
 * modification).
 *
 * @author Geoff Williams
 */
public abstract class AbstractParameterFileReader extends AbstractIntegrationBean {

    private ApplicationManager applicationManager;

    private static final Log log = LogFactory.getLog(AbstractParameterFileReader.class);

    /**
   * Retrieve the directory which forms the base of the simulation runs, e.g. /home/me/run_here/
   * 
   * @return Simulation run base directory path.
   */
    protected abstract String retrieveBaseDirectoryPath();

    /**
   * Load the parameter files into the message.
   * 
   * @param message Message without parameter files.
   * @return Message with any parameter files now included.
   */
    public MessageRunSimulation readParameterFiles(final MessageRunSimulation message) {
        log.debug("~int.readParameterFiles(MessageRunSimulation) : One-time parameter file reading for simulation");
        final AbstractSimulation simulation = message.getSimulation();
        final AbstractSimulationEnvironment simulationEnvironment = (AbstractSimulationEnvironment) simulation.getSimulationEnvironment();
        final Long simulationId = simulation.getId();
        if (!(simulation instanceof SimulationAPPreDiCT) && !(simulation instanceof SimulationTorsadePreDiCT) && !(simulation instanceof SimulationQTPreDiCT) && !(simulation instanceof SimulationChaste)) {
            log.error("~int.readParameterFiles(MessageRunSimulation) : Cannot handle [" + simulation.getName() + "]");
            throw new VRESystemException(InSilicoMessageKeys.SYSTEM_CONFIG_SIM_ENV);
        }
        final String[] parameterFileResourcePaths = applicationManager.retrieveApplicationParameterFileResourcePaths(simulationEnvironment);
        if (parameterFileResourcePaths.length == 0) {
            log.debug("~int.readParameterFiles(MessageRunSimulation) : No parameter files for simulation");
            return message;
        }
        sendStatusMessage(simulationId, AbstractSimulation.PROGRESS_TARGET_OVERALL, InSilicoMessageKeys.READ_PARAMETER_FILES, MessageService.CODE_INFO_INTERNAL);
        final String temporaryDirectory = retrieveTemporaryDirectoryPath(simulationId.toString(), retrieveBaseDirectoryPath());
        String[] statusMessage = new String[] { MessageKeys.READING_GENERIC, InSilicoMessageKeys.MESSAGE_ISGENERAL_FOLDER, temporaryDirectory };
        sendStatusMessage(simulationId, AbstractSimulation.PROGRESS_TARGET_OVERALL, statusMessage, null);
        for (final String parameterFileResourcePath : Arrays.asList(parameterFileResourcePaths)) {
            log.debug("~int.readParameterFiles(MessageRunSimulation) : Adding parameter file [" + parameterFileResourcePath + "]");
            final String fileLocation = temporaryDirectory.concat(parameterFileResourcePath);
            message.addParameterFile(parameterFileResourcePath, new File(fileLocation));
        }
        return message;
    }

    /**
   * Create/retrieve the temporary directory for name processing.
   * 
   * @param simulationId Simulation persistence identity.
   * @param baseDirectoryPath Base directory of simulation runs.
   * @return Temporary directory path, e.g. /home/me/run_dir/22/tmp/
   */
    protected abstract String retrieveTemporaryDirectoryPath(String simulationId, String baseDirectoryPath);

    /**
   * @return the applicationManager
   */
    protected ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    /**
   * @param applicationManager the applicationManager to set
   */
    public void setApplicationManager(final ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }
}
