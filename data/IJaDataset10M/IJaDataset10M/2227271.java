package uk.ac.ebi.intact.dataexchange.imex.idassigner.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Outputs event using a simple logger.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 2.1.1
 */
public class LoggingImexUpdateListener extends AbstractImexUpdateListener {

    private static final Log log = LogFactory.getLog(LoggingImexUpdateListener.class);

    @Override
    public void onProcessPublication(ImexUpdateEvent evt) {
        log.info("Process " + evt.getPublication().getShortLabel());
    }

    @Override
    public void onProcessImexPublication(ImexUpdateEvent evt) {
        log.info("onProcessImexPublication " + evt.getPublication().getShortLabel());
    }

    @Override
    public void onPublicationUpToDate(ImexUpdateEvent evt) {
        log.info("onPublicationUpToDate " + evt.getPublication().getShortLabel());
    }

    @Override
    public void onImexIdAssignedToPublication(ImexUpdateEvent iue) {
        log.info("NewImexIdAssigned " + iue.getPublication().getShortLabel() + ": " + iue.getMessage());
    }

    @Override
    public void onImexIdAssignedToInteraction(ImexUpdateEvent iue) {
        log.info("onImexIdAssignedToInteraction " + iue.getPublication().getShortLabel() + ": " + iue.getMessage());
    }

    @Override
    public void onImexIdMismatchFound(ImexUpdateEvent iue) {
        log.info("IMEx id mismatch " + iue.getPublication().getShortLabel() + ": " + iue.getMessage());
    }
}
