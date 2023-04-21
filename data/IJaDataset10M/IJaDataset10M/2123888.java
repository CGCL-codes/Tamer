package org.jcvi.vics.compute.service.recruitment;

import org.jcvi.vics.model.common.SystemConfigurationProperties;
import org.jcvi.vics.model.tasks.recruitment.RecruitmentViewerFilterDataTask;
import org.jcvi.vics.model.tasks.recruitment.RecruitmentViewerTask;
import org.jcvi.vics.model.user_data.User;
import org.jcvi.vics.model.user_data.genome.GenomeProjectFileNode;
import org.jcvi.vics.model.vo.ParameterException;
import org.jcvi.vics.shared.processors.recruitment.RecruitmentDataHelper;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Jun 14, 2007
 * Time: 10:11:38 AM
 */
public class FrvNonGridDataOnlyService extends FrvNonGridServiceBase {

    protected void recruit() throws IOException, ParameterException, InterruptedException {
        logger.debug("\nFrvNonGridDataOnlyService recruit started");
        String basePath = SystemConfigurationProperties.getString("Perl.ModuleBase") + SystemConfigurationProperties.getString("RecruitmentViewer.PerlBaseDir");
        String sampleInfoName = SystemConfigurationProperties.getString("RecruitmentViewer.SampleFile.Name");
        String pathToAnnotationFile = null;
        if (User.SYSTEM_USER_LOGIN.equalsIgnoreCase(dataFileNode.getOwner())) {
            GenomeProjectFileNode gpNode = (GenomeProjectFileNode) computeDAO.getNodeById(Long.valueOf(task.getParameter(RecruitmentViewerTask.GENOME_PROJECT_NODE_ID)));
            pathToAnnotationFile = gpNode.getDirectoryPath() + File.separator + task.getParameter(RecruitmentViewerFilterDataTask.GENBANK_FILE_NAME);
        }
        RecruitmentDataHelper helper = new RecruitmentDataHelper(dataFileNode.getDirectoryPath(), resultFileNode.getDirectoryPath(), pathToAnnotationFile, basePath + File.separator + sampleInfoName, task.getSampleListAsCommaSeparatedString(), Integer.toString(task.getPercentIdMin()), Integer.toString(task.getPercentIdMax()), Double.toString(task.getReferenceBegin()), Double.toString(task.getReferenceEnd()), task.getMateBits(), task.getAnnotationFilterString(), task.getMateSpanPoint(), task.getColorizationType());
        helper.regenerateDataOnly();
        saveNumRecruitedReads();
        logger.debug("\nFrvNonGridDataOnlyService recruit() complete");
    }
}
