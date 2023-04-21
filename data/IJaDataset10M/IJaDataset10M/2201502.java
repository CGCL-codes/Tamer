package org.paradise.dms.web.action.systemusergrouptaskallocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.directwebremoting.annotations.RemoteProxy;
import org.paradise.dms.pojo.SystemUser;
import org.paradise.dms.pojo.SystemUserTask;
import org.paradise.dms.services.SystemUserGroupTaskAllocationService;
import org.paradise.dms.web.action.DMSBaseAction;
import org.paradise.dms.web.tools.Pager;
import org.paradise.dms.web.tools.PagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "systemUserGroupTaskAllocationAction")
public class SystemUserGroupTaskAllocationAction extends DMSBaseAction {

    private static Logger log = Logger.getLogger(SystemUserGroupTaskAllocationAction.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 1136804160395491564L;

    @Autowired
    @Qualifier("systemUserGroupTaskAllocationServiceImpl")
    SystemUserGroupTaskAllocationService systemUserGroupTaskAllocationService;

    private SystemUserTask systemusertask;

    private List<SystemUserTask> tasklist;

    private SystemUser systemUser;

    private int systemuserid;

    private int systemusergroupid;

    private int systemusertaskassignerid;

    private int systemusertaskid;

    private String systemuserstatus;

    private int taskstatusid = 0;

    @Autowired
    private PagerService pagerService;

    private Pager pager;

    protected String currentPage;

    protected String totalRows;

    protected String pagerMethod;

    public String getSystemuserstatus() {
        return systemuserstatus;
    }

    public void setSystemuserstatus(String systemuserstatus) {
        this.systemuserstatus = systemuserstatus;
    }

    public int getTaskstatusid() {
        return taskstatusid;
    }

    public void setTaskstatusid(int taskstatusid) {
        this.taskstatusid = taskstatusid;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    public String getPagerMethod() {
        return pagerMethod;
    }

    public void setPagerMethod(String pagerMethod) {
        this.pagerMethod = pagerMethod;
    }

    public int getSystemusertaskassignerid() {
        return systemusertaskassignerid;
    }

    public void setSystemusertaskassignerid(int systemusertaskassignerid) {
        this.systemusertaskassignerid = systemusertaskassignerid;
    }

    public int getSystemuserid() {
        return systemuserid;
    }

    public void setSystemuserid(int systemuserid) {
        this.systemuserid = systemuserid;
    }

    public int getSystemusergroupid() {
        return systemusergroupid;
    }

    public void setSystemusergroupid(int systemusergroupid) {
        this.systemusergroupid = systemusergroupid;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public SystemUserTask getSystemusertask() {
        return systemusertask;
    }

    public void setSystemusertask(SystemUserTask systemusertask) {
        this.systemusertask = systemusertask;
    }

    public List<SystemUserTask> getTasklist() {
        return tasklist;
    }

    public void setTasklist(List<SystemUserTask> tasklist) {
        this.tasklist = tasklist;
    }

    public int getSystemusertaskid() {
        return systemusertaskid;
    }

    public void setSystemusertaskid(int systemusertaskid) {
        this.systemusertaskid = systemusertaskid;
    }

    public String insertTask() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        this.getSystemusertask().setSystemusertaskassigner((String) session.getAttribute("systemusername"));
        this.getSystemusertask().setSystemusertaskassignerid(Integer.parseInt((String) session.getAttribute("systemuserid")));
        this.getSystemusertask().setSystemusertaskind("1");
        this.getSystemusertask().setSystemusertaskassigntime(new Date());
        if (this.getSystemusertask().getSystemusertaskaccepter().equals("请选择用户")) {
            this.getSystemusertask().setSystemusertaskaccepter("该组全体用户");
        }
        if (systemUserGroupTaskAllocationService.insertTasks(this.getSystemusertask())) {
            return SUCCESS;
        }
        return INPUT;
    }

    public String listSentTask() {
        try {
            int totalRow = systemUserGroupTaskAllocationService.getRowsForListSentTask(systemusertaskassignerid);
            pager = pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
            this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
            this.setTotalRows(String.valueOf(totalRow));
            tasklist = systemUserGroupTaskAllocationService.listSentTask(systemusertaskassignerid, pager.getPageSize(), pager.getStartRow());
            this.getRequest().setAttribute("systemusertaskassignerid", systemusertaskassignerid);
            return SUCCESS;
        } catch (Exception e) {
            return INPUT;
        }
    }

    @SuppressWarnings("unchecked")
    public String listReceivedTask() {
        try {
            int totalRow = systemUserGroupTaskAllocationService.getRowsForListReicivedTask(systemuserid, systemusergroupid, taskstatusid);
            pager = pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRow);
            this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
            this.setTotalRows(String.valueOf(totalRow));
            List list = new ArrayList();
            list = systemUserGroupTaskAllocationService.listSystemUserReceivedTask(systemuserid, systemusergroupid, taskstatusid, pager.getPageSize(), pager.getStartRow());
            this.getRequest().setAttribute("task", list);
            return SUCCESS;
        } catch (Exception e) {
            return INPUT;
        }
    }

    public String updateTask() {
        try {
            systemusertask.setSystemusertaskassigntime(new Date());
            systemusertask.setSystemusertaskind("1");
            systemUserGroupTaskAllocationService.updateTask(systemusertask, systemusertaskid);
            return SUCCESS;
        } catch (Exception e) {
            log.info("DMS_error:更新用户任务有误！");
            log.info("DMS_error:错误原因！" + e);
            return INPUT;
        }
    }

    public String selectTaskForUpdate() {
        try {
            tasklist = systemUserGroupTaskAllocationService.selectTaskForUpdate(systemusertaskid);
            this.getRequest().setAttribute("updatedTask", tasklist.get(0));
            return SUCCESS;
        } catch (Exception e) {
            log.info("DMS_error:选择用户任务有误！");
            log.info("DMS_error:错误原因！" + e);
            return INPUT;
        }
    }

    public String deleteTask() {
        try {
            if ("success".equals(systemUserGroupTaskAllocationService.deleteTask(systemusertaskid))) {
                return SUCCESS;
            } else return INPUT;
        } catch (Exception e) {
            log.info("DMS_error:删除用户任务有误！");
            log.info("DMS_error:错误原因！" + e);
            return INPUT;
        }
    }

    @SuppressWarnings("unchecked")
    public String readOneTaskByTaskId() {
        try {
            List list = new ArrayList();
            list = (List) systemUserGroupTaskAllocationService.listSystemUserReceivedTask(systemuserid, systemusergroupid, taskstatusid, 1, 0);
            this.getRequest().setAttribute("task", list);
            return SUCCESS;
        } catch (Exception e) {
            return INPUT;
        }
    }

    public String changeStatus(String status, String statusid) {
        try {
            String effectNum = systemUserGroupTaskAllocationService.changeStatus(status, Integer.parseInt(statusid));
            return effectNum;
        } catch (Exception e) {
            log.info("DMS_error:更改用户任务状态有误！");
            log.info("DMS_error:错误原因！" + e);
            return null;
        }
    }
}
