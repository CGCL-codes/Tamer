package org.jcvi.vics.web.gwt.frv.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.jcvi.vics.web.gwt.common.client.jobs.JobSelectionListener;
import org.jcvi.vics.web.gwt.common.client.service.StatusService;
import org.jcvi.vics.web.gwt.common.client.service.StatusServiceAsync;
import org.jcvi.vics.web.gwt.common.client.service.log.Logger;
import org.jcvi.vics.web.gwt.common.client.ui.link.ActionLink;
import org.jcvi.vics.web.gwt.common.client.util.SystemProps;
import org.jcvi.vics.web.gwt.frv.client.panels.FrvControlsPanel;

public class FrvPanel extends VerticalPanel {

    private static Logger _logger = Logger.getLogger("org.jcvi.vics.web.gwt.frv.client.FrvPanel");

    private HorizontalPanel _contentsPanel;

    private FrvControlsPanel _frvControlsPanel;

    private static StatusServiceAsync _statusservice = (StatusServiceAsync) GWT.create(StatusService.class);

    static {
        ((ServiceDefTarget) _statusservice).setServiceEntryPoint("status.srv");
    }

    public FrvPanel() {
        setWidth("100%");
    }

    public void setDefaultJob() {
        String defTaskId = SystemProps.getString("RecruitmentViewer.DefaultTaskId", null);
        if (defTaskId == null) _logger.error("Got null default FRV task ID from properties, starting with no default"); else getDefaultTask(defTaskId);
    }

    public void getDefaultTask(String taskId) {
        _statusservice.getRecruitmentTaskById(taskId, new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                _logger.error("Failed to retrieve default FRV task, starting with no default");
            }

            public void onSuccess(Object object) {
                if (object == null) {
                    _logger.error("Got null default FRV task, starting with no default");
                    setJob(null);
                } else {
                    _logger.info("Got default FRV task");
                    setJob((org.jcvi.vics.shared.tasks.RecruitableJobInfo) object);
                }
            }
        });
    }

    public void setUserTask(String userPipelineTaskId) {
        _statusservice.getRecruitmentFilterTaskByUserPipelineId(userPipelineTaskId, new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                _logger.error("Failed to retrieve user FRV task, starting with default");
                navFailureStartup();
            }

            public void onSuccess(Object object) {
                if (object == null) {
                    _logger.error("Got null user FRV task, starting with no default");
                    navFailureStartup();
                } else {
                    _logger.info("Got user FRV task");
                    setJob((org.jcvi.vics.shared.tasks.RecruitableJobInfo) object);
                }
            }

            private void navFailureStartup() {
                setDefaultJob();
            }
        });
    }

    public void setJob(org.jcvi.vics.shared.tasks.RecruitableJobInfo job) {
        setJob(job, null);
    }

    public void setJob(org.jcvi.vics.shared.tasks.RecruitableJobInfo job, ActionLink link) {
        realize(link);
        _frvControlsPanel.setJob(job);
    }

    private void realize(ActionLink link) {
        if (_contentsPanel == null) {
            _contentsPanel = new HorizontalPanel();
            add(_contentsPanel);
        }
        if (_frvControlsPanel == null) {
            _frvControlsPanel = new FrvControlsPanel("Controls", new JobChangedListener());
            if (link != null) _frvControlsPanel.addActionLink(link);
            _contentsPanel.add(_frvControlsPanel);
        }
    }

    /**
     * This is a hook for internal panels to notify this main panel that the job has been changed by the
     * user using the panel's controls.  We'll just re-init each panel with the new job
     */
    public class JobChangedListener implements JobSelectionListener {

        public void onSelect(org.jcvi.vics.shared.tasks.JobInfo job) {
            setJob((org.jcvi.vics.shared.tasks.RecruitableJobInfo) job);
        }

        /**
         * not applicable
         */
        public void onUnSelect() {
        }
    }
}
