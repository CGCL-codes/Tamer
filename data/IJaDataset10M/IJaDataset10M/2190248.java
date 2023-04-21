package uk.ac.ebi.pride.gui.task.impl;

import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.Tuple;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.data.core.Modification;
import uk.ac.ebi.pride.gui.component.exception.ThrowableEntry;
import uk.ac.ebi.pride.gui.component.message.MessageType;
import uk.ac.ebi.pride.gui.component.report.SummaryReportMessage;
import uk.ac.ebi.pride.gui.component.table.TableDataRetriever;
import uk.ac.ebi.pride.gui.component.table.model.TableContentType;
import uk.ac.ebi.pride.gui.event.SummaryReportEvent;
import java.util.*;

/**
 * Scan experiment for all the data related to identification, peptide and quantitation
 * <p/>
 * User: rwang
 * Date: 14-Sep-2010
 * Time: 11:34:33
 */
public class ScanExperimentTask extends AbstractDataAccessTask<Void, Tuple<TableContentType, List<Object>>> {

    private static final Logger logger = LoggerFactory.getLogger(ScanExperimentTask.class);

    /**
     * the default task title
     */
    private static final String DEFAULT_TASK_TITLE = "loading experiment content";

    /**
     * the default task description
     */
    private static final String DEFAULT_TASK_DESCRIPTION = "Loading experiment content";

    /**
     * Retrieve a subset of identifications.
     *
     * @param controller DataAccessController
     */
    public ScanExperimentTask(DataAccessController controller) {
        super(controller);
        this.setName(DEFAULT_TASK_TITLE);
        this.setDescription(DEFAULT_TASK_DESCRIPTION);
    }

    @Override
    protected Void retrieve() throws Exception {
        try {
            Collection<Comparable> identIds = controller.getIdentificationIds();
            int missingSpectrumLinks = 0;
            boolean hasQuantData = controller.hasQuantData();
            Map<String, String> ptmMap = new HashMap<String, String>();
            if (hasQuantData) {
                List<Object> proteinQuantHeaders = TableDataRetriever.getProteinQuantTableHeaders(controller, -1);
                publish(new Tuple<TableContentType, List<Object>>(TableContentType.PROTEIN_QUANTITATION_HEADER, proteinQuantHeaders));
            }
            for (Comparable identId : identIds) {
                List<Object> identContent = TableDataRetriever.getProteinTableRow(controller, identId);
                publish(new Tuple<TableContentType, List<Object>>(TableContentType.PROTEIN, identContent));
                if (hasQuantData) {
                    List<Object> allQuantContent = new ArrayList<Object>();
                    allQuantContent.addAll(identContent);
                    List<Object> identQuantContent = TableDataRetriever.getProteinQuantTableRow(controller, identId, -1);
                    allQuantContent.addAll(identQuantContent);
                    publish(new Tuple<TableContentType, List<Object>>(TableContentType.PROTEIN_QUANTITATION, allQuantContent));
                }
                Collection<Comparable> ids = controller.getPeptideIds(identId);
                if (ids != null) {
                    for (Comparable peptideId : ids) {
                        List<Object> peptideContent = TableDataRetriever.getPeptideTableRow(controller, identId, peptideId);
                        publish(new Tuple<TableContentType, List<Object>>(TableContentType.PEPTIDE, peptideContent));
                        if (controller.getPeptideSpectrumId(identId, peptideId) == null) {
                            missingSpectrumLinks++;
                        }
                        Collection<Modification> mods = controller.getPTMs(identId, peptideId);
                        if (mods != null) {
                            for (Modification mod : mods) {
                                String accession = mod.getAccession();
                                String name = mod.getName();
                                if (!ptmMap.containsKey(accession)) {
                                    EventBus.publish(new SummaryReportEvent(this, controller, new SummaryReportMessage(SummaryReportMessage.Type.INFO, "PTM: " + accession, "PTM found: [" + accession + "]\t" + name)));
                                    ptmMap.put(mod.getAccession(), mod.getName());
                                }
                            }
                        }
                    }
                }
                checkInterruption();
            }
            if (missingSpectrumLinks > 0) {
                EventBus.publish(new SummaryReportEvent(this, controller, new SummaryReportMessage(SummaryReportMessage.Type.WARNING, "Missing spectra [" + missingSpectrumLinks + "]", "The number of peptides without spectrum links")));
            }
        } catch (DataAccessException dex) {
            String msg = "Failed to retrieve protein and peptide related data";
            logger.error(msg, dex);
            appContext.addThrowableEntry(new ThrowableEntry(MessageType.ERROR, msg, dex));
        } catch (InterruptedException ex) {
            logger.warn("Protein table and peptide table update has been cancelled");
        }
        return null;
    }

    private void checkInterruption() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }
}
