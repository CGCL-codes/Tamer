package uk.ac.ebi.intact.services.validator.jobs;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.ontology_manager.impl.local.OntologyLoaderException;
import uk.ac.ebi.intact.services.validator.context.ValidatorWebContent;
import uk.ac.ebi.intact.services.validator.context.ValidatorWebContext;
import uk.ac.ebi.intact.services.validator.context.ValidatorWebContextException;

/**
 * The job will check every 6 hours that the ontologyManager for PSI-MI and the one for PSI-PAR are up-to-date (all the loaded ontologies are up to date).
 * If an update has been done, it will create a new ValidatorWebContent which will re-load the ontologies in the background and then
 * will set the ValidatorWebContent of the current ValidatorWebContext with the new instance of the ValidatorWebContent.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>28-Jun-2010</pre>
 */
public class ValidatorContextUpdater implements Job {

    /**
     * Logging, logging!
     */
    private static final Log log = LogFactory.getLog(ValidatorContextUpdater.class);

    /**
     * Create a new ValidatorContextUpdater
     */
    public ValidatorContextUpdater() {
    }

    /**
     * Execute the job
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ValidatorWebContext validatorContext = ValidatorWebContext.getInstance();
        ValidatorWebContent validatorContent = validatorContext.getValidatorWebContent();
        try {
            boolean isPsiMiUpToDate = checkOntology(validatorContext, validatorContent.getPsiMiOntologyManager());
            boolean isPsiParUpToDate = checkOntology(validatorContext, validatorContent.getPsiParOntologyManager());
            ValidatorWebContent newWebContent = null;
            final long start = System.currentTimeMillis();
            if (isPsiMiUpToDate && !isPsiParUpToDate) {
                newWebContent = new ValidatorWebContent(validatorContent.getPsiMiOntologyManager(), validatorContent.getPsiMiCvMapping(), validatorContent.getPsiMiObjectRules());
            } else if (!isPsiMiUpToDate && isPsiParUpToDate) {
                newWebContent = new ValidatorWebContent(validatorContent.getPsiParOntologyManager(), validatorContent.getPsiParCvMapping());
            } else if (!isPsiMiUpToDate && !isPsiParUpToDate) {
                newWebContent = new ValidatorWebContent();
            }
            if (newWebContent != null) {
                if (newWebContent.getPsiMiOntologyManager() == null) {
                    String body = "A new update of the psi-mi ontology has been done recently and a problem occurred " + "when re-uploading the PSI-MI ontology." + " As the new ontology manager is null, we will keep the previous one and will not be able to use the new ontology. \n";
                    NullPointerException e = new NullPointerException(body);
                    validatorContext.sendEmail("Update of the psi-mi ontology failed", ExceptionUtils.getFullStackTrace(e));
                    throw e;
                } else if (newWebContent.getPsiParOntologyManager() == null) {
                    String body = "A new update of the psi-par ontology has been done recently and a problem occurred " + "when re-uploading the PSI-PAR ontology." + " As the new ontology manager is null, we will keep the previous one and will not be able to use the new ontology. \n";
                    NullPointerException e = new NullPointerException(body);
                    validatorContext.sendEmail("Update of the psi-par ontology failed", ExceptionUtils.getFullStackTrace(e));
                    throw e;
                } else {
                    validatorContext.setValidatorWebContent(newWebContent);
                    final long stop = System.currentTimeMillis();
                    log.trace("Time to update the ontology': " + (stop - start) + "ms");
                }
            }
        } catch (OntologyLoaderException e) {
            String body = "We couldn't get the last ontology update date. If a new update has been done " + "recently, we will not be able to load the new ontology terms. \n \n" + ExceptionUtils.getFullStackTrace(e);
            log.warn(body);
            validatorContext.sendEmail("Date of last ontology update not available", body);
        } catch (ValidatorWebContextException e) {
            String body = "An ontology update has been done recently but it was not possible to create a new instance of the ValidatorWebContent." + ExceptionUtils.getFullStackTrace(e);
            log.warn(body);
            validatorContext.sendEmail("Impossible to create a new ValidatorWebContent instance", body);
        }
    }

    /**
     *
     * @param validatorContext
     * @param ontologyManager
     * @return true if the ontology manager is up to date
     * @throws OntologyLoaderException
     */
    private boolean checkOntology(ValidatorWebContext validatorContext, OntologyManager ontologyManager) throws OntologyLoaderException {
        if (ontologyManager == null) {
            String body = "A problem occurred when uploading the ontology and " + "consequently the ontology manager is null. \n" + "The validator will not be able to work properly.";
            NullPointerException e = new NullPointerException(body);
            validatorContext.sendEmail("Ontology Manager is null", ExceptionUtils.getFullStackTrace(e));
            throw e;
        } else {
            return ontologyManager.isUpToDate();
        }
    }
}
