package com.ixora.jobs.ui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import com.ixora.jobs.JobDefinition;
import com.ixora.jobs.JobId;
import com.ixora.jobs.JobsComponent;
import com.ixora.jobs.services.JobEngineService;
import com.ixora.jobs.ui.messages.Msg;
import com.ixora.common.MessageRepository;
import com.ixora.common.ui.UIConfiguration;
import com.ixora.common.ui.UIExceptionMgr;
import com.ixora.common.ui.UIUtils;
import com.ixora.common.ui.jobs.UIWorkerJobDefault;

/**
 * ActionScheduleJob.
 */
final class ActionScheduleJob extends AbstractAction {

    private static final long serialVersionUID = 4480438870358323225L;

    /** Job engine */
    private JobEngineService jobEngine;

    /** Job manager viewer */
    private JobManagerViewer jobManagerViewer;

    /**
     * Constructor.
     * @param jv view
     * @param jes the job engine
     */
    public ActionScheduleJob(JobManagerViewer jv, JobEngineService jes) {
        super();
        this.jobManagerViewer = jv;
        this.jobEngine = jes;
        UIUtils.setUsabilityDtls(MessageRepository.get(JobsComponent.NAME, Msg.ACTIONS_SCHEDULE_JOB), this);
        ImageIcon icon = UIConfiguration.getIcon("schedule_job.gif");
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
        }
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        try {
            jobManagerViewer.createJobDefinition(new JobManagerViewer.Callback() {

                public void jobDefinition(final JobDefinition jdef) {
                    jobManagerViewer.getViewContainer().getAppWorker().runJob(new UIWorkerJobDefault(jobManagerViewer.getWindow(), Cursor.WAIT_CURSOR, MessageRepository.get(JobsComponent.NAME, Msg.TEXT_SCHEDULING_JOB)) {

                        public void work() throws Exception {
                            this.fResult = jobEngine.runJob(jdef);
                        }

                        public void finished(Throwable ex) {
                            if (ex == null) {
                                jobManagerViewer.refreshJobTable(((JobId[]) this.fResult)[0]);
                            }
                        }
                    });
                }
            }, null);
        } catch (Exception e) {
            UIExceptionMgr.userException(e);
        }
    }
}
