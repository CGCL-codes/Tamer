package org.jcvi.vics.web.gwt.blast.client.submit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import org.jcvi.vics.model.common.UserDataNodeVO;
import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.tasks.blast.BlastTask;
import org.jcvi.vics.model.user_data.Node;
import org.jcvi.vics.web.gwt.blast.client.BlastService;
import org.jcvi.vics.web.gwt.blast.client.BlastServiceAsync;
import org.jcvi.vics.web.gwt.common.client.SystemWebTracker;
import org.jcvi.vics.web.gwt.common.client.jobs.JobSubmissionListener;
import org.jcvi.vics.web.gwt.common.client.service.DataService;
import org.jcvi.vics.web.gwt.common.client.service.DataServiceAsync;
import org.jcvi.vics.web.gwt.common.client.service.log.Logger;
import org.jcvi.vics.web.gwt.common.client.util.BlastData;
import java.util.Set;

/**
 * @author Michael Press
 */
public class SubmitBlastJob {

    private static Logger _logger = Logger.getLogger("org.jcvi.vics.web.gwt.blast.client.submit.SubmitBlastJob");

    private static DataServiceAsync _dataservice = (DataServiceAsync) GWT.create(DataService.class);

    private static BlastServiceAsync _blastService = (BlastServiceAsync) GWT.create(BlastService.class);

    static {
        ((ServiceDefTarget) _dataservice).setServiceEntryPoint("data.srv");
        ((ServiceDefTarget) _blastService).setServiceEntryPoint("blast.srv");
    }

    private BlastData _blastData;

    private JobSubmissionListener _listener;

    private AsyncCallback _uploadListener;

    public SubmitBlastJob(BlastData blastData, JobSubmissionListener jobSubmissionListener, AsyncCallback uploadListener) {
        _blastData = blastData;
        _listener = jobSubmissionListener;
        _uploadListener = uploadListener;
    }

    public void runJob() {
        Set<String> previousQuerySet = _blastData.getQuerySequenceDataNodeMap().keySet();
        if (previousQuerySet != null && previousQuerySet.size() > 0) runBlastJob(previousQuerySet.iterator().next()); else uploadQuerySequence();
    }

    private void uploadQuerySequence() {
        _dataservice.saveUserDefinedFastaNode(_blastData.getMostRecentlySpecifiedQuerySequenceName(), _blastData.getUserReferenceFASTA(), Node.VISIBILITY_PRIVATE, new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                _logger.error("Failed in attempt to save the user data node. Exception:" + throwable.getMessage());
                notifyFailure(throwable);
            }

            public void onSuccess(Object object) {
                if (object == null) notifyFailure(null); else {
                    _logger.debug("Successfully called the service to save the user data node. NodeId=" + object.toString());
                    UserDataNodeVO newVO = (UserDataNodeVO) object;
                    notifyUploadSuccess(newVO);
                    runBlastJob(newVO.getDatabaseObjectId());
                }
            }
        });
    }

    private void runBlastJob(String queryNodeId) {
        try {
            Task blastTask = _blastData.getBlastTask();
            if (null == blastTask || null == blastTask.getParameterKeySet()) {
                _logger.error("Blast Task in getData is null or the parameters are null. Returning from syncTask.");
                return;
            }
            if (null == _blastData.getSubjectSequenceDataNodeMap() || null == queryNodeId) {
                _logger.error("Blast subject dbs or query dbs are null. Returning from syncTask.");
                return;
            }
            blastTask.setParameter(BlastTask.PARAM_query, queryNodeId);
            blastTask.setParameter(BlastTask.PARAM_subjectDatabases, Task.csvStringFromList(_blastData.getSubjectDatasetIdsList()));
            _logger.debug("Saved the query Node, now running Blast Job");
            _blastService.runBlastJob(blastTask, new AsyncCallback() {

                public void onFailure(Throwable throwable) {
                    SystemWebTracker.trackActivity("SubmitBlastJobFailure");
                    notifyFailure(throwable);
                }

                public void onSuccess(Object object) {
                    SystemWebTracker.trackActivity("SubmitBlastJobSuccessful");
                    String jobNumber = (String) object;
                    _blastData.setJobNumber((String) object);
                    notifySuccess(jobNumber);
                }
            });
        } catch (Throwable throwable) {
            _logger.error("Error in setupBlastJob():" + throwable.getMessage(), throwable);
            notifyFailure(throwable);
        }
    }

    private void notifyFailure(Throwable throwable) {
        SystemWebTracker.trackActivity("SubmitBlastJobFailure");
        if (_listener != null) _listener.onFailure(throwable);
    }

    private void notifySuccess(String jobId) {
        SystemWebTracker.trackActivity("SubmitBlastJobSuccessful");
        if (_listener != null) _listener.onSuccess(jobId);
    }

    private void notifyUploadSuccess(UserDataNodeVO node) {
        if (_uploadListener != null) _uploadListener.onSuccess(node);
    }
}
