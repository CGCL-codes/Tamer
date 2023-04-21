package net.sf.jabs.ui.component;

import net.sf.jabs.data.project.ProjectDAO;
import net.sf.jabs.data.report.ReportDAO;
import net.sf.jabs.data.scheduling.SchedulingDAO;
import net.sf.jabs.infr.services.ProjectSchedulingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;

public abstract class ProjectDelete extends BaseComponent {

    public abstract long getProject();

    public abstract boolean getAllowDelete();

    public abstract void setAllowDelete(boolean allow);

    public abstract void setLinkCount(long linkCount);

    @InjectObject("service:jabs.services.ProjectDAO")
    public abstract ProjectDAO getProjectDAO();

    @InjectObject("service:jabs.services.SchedulingDAO")
    public abstract SchedulingDAO getSchedulingDAO();

    @InjectObject("service:jabs.services.ReportDAO")
    public abstract ReportDAO getReportDAO();

    @InjectObject("service:jabs.services.ProjectSchedulingService")
    public abstract ProjectSchedulingService getProjectSchedulingService();

    private static Log _log = LogFactory.getLog(ProjectDelete.class);

    /**
     * Run the delete process. This runs a job that performs the delete
     * @param cycle
     */
    public void doDelete(IRequestCycle cycle) {
        Object[] parameters = cycle.getListenerParameters();
        Long project = (Long) parameters[0];
        if (_log.isDebugEnabled()) _log.debug("Deleting project");
        net.sf.jabs.data.project.ProjectDelete projectDelete = new net.sf.jabs.data.project.ProjectDelete();
        projectDelete.setProjectId(project);
        projectDelete.start();
    }

    /**
     * Cancels the delete process. 
     * @param cycle
     */
    public void doCancel(IRequestCycle cycle) {
        if (_log.isDebugEnabled()) _log.debug("Canceling delete");
    }

    /**
     * Checks if any projects link to this one. Returns a message
     * with the reference count.
     * @return
     */
    public String getLinkCheck() {
        long linkCount = getProjectDAO().getProjectLinkCount(getProject());
        setLinkCount(linkCount);
        return linkCount + " project(s) linking to this one";
    }

    /**
     * Returns a message showing the number of reports for this project.
     * @return
     */
    public String getReportCheck() {
        long linkCount = getReportDAO().getProjectReportCount(getProject());
        return linkCount + " saved reports";
    }

    /**
     * Returns a message showing the number of scheduled jobs for this
     * project
     * @return
     */
    public String getScheduleCheck() {
        long scheduleCount = getSchedulingDAO().getProjectSchedule(getProject()).size();
        return scheduleCount + " scheduled jobs";
    }
}
