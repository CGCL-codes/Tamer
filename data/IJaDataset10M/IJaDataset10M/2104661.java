package uk.ac.ebi.pride.gui.task.impl;

import uk.ac.ebi.pride.data.Tuple;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.gui.component.table.TableDataRetriever;
import uk.ac.ebi.pride.gui.component.table.model.TableContentType;
import uk.ac.ebi.pride.gui.task.TaskAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 16/08/2011
 * Time: 16:24
 */
public class RetrieveQuantProteinTableTask extends TaskAdapter<Void, Tuple<TableContentType, List<Object>>> {

    private static final String DEFAULT_TASK_NAME = "Updating Protein Table";

    private static final String DEFAULT_TASK_DESC = "Updating Protein Table";

    private DataAccessController controller;

    private int referenceSampleIndex;

    public RetrieveQuantProteinTableTask(DataAccessController controller, int referenceSampleIndex) {
        this.setName(DEFAULT_TASK_NAME);
        this.setDescription(DEFAULT_TASK_DESC);
        this.controller = controller;
        this.referenceSampleIndex = referenceSampleIndex;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Collection<Comparable> identIds = controller.getIdentificationIds();
        List<Object> proteinQuantHeaders = TableDataRetriever.getProteinQuantTableHeaders(controller, referenceSampleIndex);
        publish(new Tuple<TableContentType, List<Object>>(TableContentType.PROTEIN_QUANTITATION_HEADER, proteinQuantHeaders));
        for (Comparable identId : identIds) {
            List<Object> allQuantContent = new ArrayList<Object>();
            List<Object> identContent = TableDataRetriever.getProteinTableRow(controller, identId);
            allQuantContent.addAll(identContent);
            List<Object> identQuantContent = TableDataRetriever.getProteinQuantTableRow(controller, identId, referenceSampleIndex);
            allQuantContent.addAll(identQuantContent);
            publish(new Tuple<TableContentType, List<Object>>(TableContentType.PROTEIN_QUANTITATION, allQuantContent));
        }
        checkInterruption();
        return null;
    }

    private void checkInterruption() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }
}
