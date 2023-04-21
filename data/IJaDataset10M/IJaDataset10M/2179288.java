package org.jcvi.vics.compute.service.export.processor;

import org.jcvi.vics.compute.access.FeatureDAO;
import org.jcvi.vics.compute.engine.data.MissingDataException;
import org.jcvi.vics.model.common.SortArgument;
import org.jcvi.vics.model.genomics.BaseSequenceEntity;
import org.jcvi.vics.model.genomics.BioSequence;
import org.jcvi.vics.model.tasks.export.ExportTask;
import org.jcvi.vics.model.user_data.export.ExportFileNode;
import org.jcvi.vics.shared.export.ExportWriterConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Jun 6, 2008
 * Time: 4:21:05 PM
 */
public class SequenceExportProcessor extends ExportProcessor {

    FeatureDAO featureDAO;

    public SequenceExportProcessor(ExportTask exportTask, ExportFileNode exportFileNode) throws IOException, MissingDataException {
        super(exportTask, exportFileNode);
        featureDAO = new FeatureDAO(_logger);
    }

    public void execute() throws Exception {
        if (exportTask.getExportFormatType().equals(ExportWriterConstants.EXPORT_TYPE_FASTA)) {
            int count = 0;
            for (String sequenceAcc : exportTask.getAccessionList()) {
                BaseSequenceEntity seqEntity = featureDAO.findBseByAcc(sequenceAcc);
                BioSequence bioSeq = seqEntity.getBioSequence();
                String cur_defline = seqEntity.getDescription();
                if (cur_defline == null || cur_defline.length() == 0) {
                    cur_defline = seqEntity.getCameraAcc();
                }
                if (!cur_defline.startsWith(">")) {
                    cur_defline = ">" + cur_defline;
                }
                List<String> fastaItems = new ArrayList<String>();
                fastaItems.add(cur_defline);
                fastaItems.add(bioSeq.getSequence());
                exportWriter.writeItem(fastaItems);
                count++;
            }
            _logger.debug("SequenceExportProcessor export count=" + count);
        } else {
            throw new Exception("Do not support format type=" + exportTask.getExportFormatType());
        }
    }

    public String getProcessorType() {
        return "Sequence";
    }

    protected List<SortArgument> getDataHeaders() {
        return null;
    }
}
