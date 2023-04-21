package org.jcvi.vics.web.gwt.common.client.panel.user.jobs;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import org.jcvi.vics.model.tasks.Event;
import org.jcvi.vics.shared.tasks.JobInfo;
import org.jcvi.vics.web.gwt.common.client.jobs.JobSelectionListener;
import org.jcvi.vics.web.gwt.common.client.ui.imagebundles.ImageBundleFactory;
import org.jcvi.vics.web.gwt.common.client.util.UrlBuilder;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.MenuBar;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.MenuBarWithRightAlignedDropdowns;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.MenuItem;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Jul 28, 2008
 * Time: 11:38:01 AM
 */
public class UserFRVPipelineJobResultsPanel extends GeneralJobResultsPanel {

    public static final String TASK_FRV = "UserBlastFrvTask";

    public UserFRVPipelineJobResultsPanel(JobSelectionListener jobSelectionListener, JobSelectionListener reRunJobListener, String[] rowsPerPageOptions, int defaultRowsPerPage) {
        super(TASK_FRV, jobSelectionListener, reRunJobListener, rowsPerPageOptions, defaultRowsPerPage, "UserFRVPipelineJobResults");
    }

    protected Widget getJobMenu(final JobInfo job) {
        final MenuBar menu = new MenuBarWithRightAlignedDropdowns();
        menu.setAutoOpen(false);
        MenuBar dropDown = new MenuBarWithRightAlignedDropdowns(true);
        MenuItem gotoItem = new MenuItem("Go To Fragment Recruitment Viewer", true, new Command() {

            public void execute() {
                String url = UrlBuilder.getFrvUrl() + "?taskId=" + job.getJobId();
                Window.open(url, "_self", "");
            }
        });
        dropDown.addItem(gotoItem);
        MenuItem jobItem = new MenuItem("Job&nbsp;" + ImageBundleFactory.getControlImageBundle().getArrowDownEnabledImage().getHTML(), true, dropDown);
        jobItem.setStyleName("tableTopLevelMenuItem");
        menu.addItem(jobItem);
        menu.setVisible(job.getStatus().equals(Event.COMPLETED_EVENT));
        return menu;
    }
}
