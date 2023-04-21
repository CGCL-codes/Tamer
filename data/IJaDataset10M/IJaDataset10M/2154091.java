package org.jcvi.vics.compute.service.recruitment;

import org.jcvi.vics.compute.access.ComputeDAO;
import org.jcvi.vics.compute.engine.data.IProcessData;
import org.jcvi.vics.compute.service.common.ProcessDataConstants;
import org.jcvi.vics.compute.service.common.ProcessDataHelper;
import org.jcvi.vics.model.user_data.recruitment.RecruitmentResultFileNode;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Nov 21, 2007
 * Time: 10:49:15 AM
 */
public class FrvImageResubmissionService extends FrvImageService {

    protected void init(IProcessData processData) throws Exception {
        this.task = ProcessDataHelper.getTask(processData);
        RecruitmentResultFileNode resultNode = (RecruitmentResultFileNode) new ComputeDAO(logger).getResultNodeByTaskId(this.task.getObjectId());
        processData.putItem(ProcessDataConstants.RESULT_FILE_NODE_ID, resultNode.getObjectId());
        super.init(processData);
    }
}
