package org.localstorm.mcc.web.gtd.actions;

import org.localstorm.mcc.web.gtd.GtdBaseActionBean;
import java.util.Collection;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import org.localstorm.mcc.ejb.gtd.FlightPlanManager;
import org.localstorm.mcc.ejb.gtd.entity.GTDList;
import org.localstorm.mcc.ejb.gtd.TaskManager;
import org.localstorm.mcc.ejb.gtd.entity.Task;
import org.localstorm.mcc.web.ReturnPageBean;
import org.localstorm.mcc.web.gtd.Views;
import org.localstorm.mcc.web.gtd.actions.wrap.TaskWrapper;
import org.localstorm.mcc.web.gtd.actions.wrap.WrapUtil;
import org.localstorm.tools.aop.runtime.Logged;

/**
 * @author Alexey Kuznetsov
 */
@UrlBinding("/actions/gtd/ctx/list/ViewList")
public class ListViewActionBean extends GtdBaseActionBean {

    @Validate(required = true)
    private int listId;

    private Collection<TaskWrapper> tasks;

    private Collection<TaskWrapper> awaitedTasks;

    private Collection<TaskWrapper> archiveTasks;

    private GTDList listResult;

    public Collection<TaskWrapper> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<TaskWrapper> tasks) {
        this.tasks = tasks;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int id) {
        this.listId = id;
    }

    public Collection<TaskWrapper> getArchiveTasks() {
        return archiveTasks;
    }

    public void setArchiveTasks(Collection<TaskWrapper> archiveTasks) {
        this.archiveTasks = archiveTasks;
    }

    public GTDList getListResult() {
        return listResult;
    }

    public void setListResult(GTDList listResult) {
        this.listResult = listResult;
    }

    public void setAwaitedTasks(Collection<TaskWrapper> tasks) {
        this.awaitedTasks = tasks;
    }

    public Collection<TaskWrapper> getAwaitedTasks() {
        return this.awaitedTasks;
    }

    @DefaultHandler
    @Logged
    public Resolution filling() throws Exception {
        GTDList list = getListManager().findById(getListId());
        super.setCurrent(list);
        this.setListResult(list);
        TaskManager tm = this.getTaskManager();
        FlightPlanManager fpm = this.getFlightPlanManager();
        Collection<Task> currentFp = fpm.getTasksFromFlightPlan(fpm.findByUser(this.getUser()));
        this.setTasks(WrapUtil.genWrappers(tm.findOpeartiveByList(list), currentFp));
        this.setAwaitedTasks(WrapUtil.genWrappers(tm.findAwaitedByList(list), currentFp));
        this.setArchiveTasks(WrapUtil.genWrappers(tm.findArchiveByList(list), currentFp));
        ReturnPageBean rpb = new ReturnPageBean(Pages.LIST_VIEW.toString());
        {
            rpb.setParam(IncomingParameters.LIST_ID, Integer.toString(listId));
        }
        super.setReturnPageBean(rpb);
        return new ForwardResolution(Views.VIEW_LIST);
    }

    public static interface IncomingParameters {

        public static final String LIST_ID = "listId";
    }
}
