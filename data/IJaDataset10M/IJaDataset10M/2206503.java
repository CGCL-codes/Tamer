package com.dcivision.workflow.taglib;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.PermissionManager;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.WebUtil;
import com.dcivision.workflow.bean.MtmWfProgressWfProgress;
import com.dcivision.workflow.bean.MtmWorkflowProgressUserRecord;
import com.dcivision.workflow.bean.MtmWorkflowStepUserActor;
import com.dcivision.workflow.bean.WorkflowProgress;
import com.dcivision.workflow.bean.WorkflowStep;
import com.dcivision.workflow.core.WorkflowProgressManager;
import com.dcivision.workflow.dao.MtmWfProgressWfProgressDAObject;
import com.dcivision.workflow.dao.MtmWorkflowProgressUserRecordDAObject;
import com.dcivision.workflow.dao.MtmWorkflowStepUserActorDAObject;
import com.dcivision.workflow.dao.WorkflowProgressDAObject;
import com.dcivision.workflow.dao.WorkflowStepDAObject;

/**
 * @author           Lun Au
 * @company          DCIVision Limited
 * @creation date    2005/1/3
 * @version          $Revision: 1.17.2.2 $
 */
public class WorkflowStepOwnerTag extends WorkflowPermissionTag {

    public static final String REVISION = "$Revision: 1.17.2.2 $";

    protected Log log = LogFactory.getLog(this.getClass().getName());

    protected SessionContainer sessionContainer;

    protected String contextPath;

    private Integer stepID;

    private List userActorList;

    private Integer filterBy;

    private boolean showEmail;

    private boolean nobr = true;

    private Integer progressID;

    public void setProgressID(Integer progressID) {
        this.progressID = progressID;
    }

    public Integer getProgressID() {
        return this.progressID;
    }

    public WorkflowStepOwnerTag() {
        super();
    }

    /**
   * @return Returns the stepID.
   */
    public Integer getStepID() {
        return stepID;
    }

    /**
   * @param stepID The stepID to set.
   */
    public void setStepID(Integer stepID) {
        this.stepID = stepID;
    }

    /**
   * @return Returns the userActorList.
   */
    public List getUserActorList() {
        return userActorList;
    }

    /**
   * @param userActorList The userActorList to set.
   */
    public void setUserActorList(List userActorList) {
        this.userActorList = userActorList;
    }

    /**
   * @return Returns the filterBy.
   */
    public Integer getFilterBy() {
        return filterBy;
    }

    /**
   * @param filterBy The filterBy to set.
   */
    public void setFilterBy(Integer filterBy) {
        this.filterBy = filterBy;
    }

    /**
   * @return Returns the showEmail.
   */
    public boolean isShowEmail() {
        return showEmail;
    }

    /**
   * @param showEmail The showEmail to set.
   */
    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    /**
   * @return Returns the nobr.
   */
    public boolean isNobr() {
        return nobr;
    }

    /**
   * @param nobr The nobr to set.
   */
    public void setNobr(boolean nobr) {
        this.nobr = nobr;
    }

    public int doStartTag() throws JspException {
        try {
            init();
            pageContext.getOut().print(this.getContent());
        } catch (ApplicationException ae) {
            log.error(ae.getMsgCode(), ae);
        } catch (Exception e) {
            log.fatal(e.getMessage(), e);
        } finally {
            this.release();
        }
        return (SKIP_BODY);
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public void release() {
        super.release();
        stepID = null;
        progressID = null;
        userActorList = null;
        filterBy = null;
        showEmail = false;
        nobr = true;
    }

    protected boolean validateTag() {
        return (stepID != null || (userActorList != null && filterBy != null));
    }

    protected String getContent() throws ApplicationException {
        StringBuffer sb = new StringBuffer();
        sb.append(getActorNameDescription());
        if (!Utility.isEmpty(filterBy)) {
            sb.append("<br><b><font style=\'font-size:11px\'>");
            sb.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.filter_by"));
            sb.append(":</font></b>&nbsp;");
            sb.append(getFilterByDescription());
        }
        return sb.toString();
    }

    /**
   * According permission shortening show full permission content.
   * @param  permission shortening
   * @return full permission content
   * @author Bill
   */
    protected String showFullPermission(String permission) {
        if (PermissionManager.READ_PERMISSION.equals(permission)) {
            return "Read";
        } else if (PermissionManager.EXECUTE_PERMISSION.equals(permission)) {
            return "Execute";
        } else if ((PermissionManager.READ_PERMISSION + PermissionManager.EXECUTE_PERMISSION).equals(permission)) {
            return "Read/Execute";
        } else {
            return "";
        }
    }

    protected String getActorNameDescription() throws ApplicationException {
        List actorList = new ArrayList();
        StringBuffer sbActor = new StringBuffer();
        if (userActorList != null && userActorList.size() > 0) {
            for (int i = 0; i < userActorList.size(); i++) {
                MtmWorkflowStepUserActor userActor = (MtmWorkflowStepUserActor) userActorList.get(i);
                boolean isSpecialActor = false;
                String email = null;
                String iconName = null;
                String actorName = null;
                String permission = null;
                ActorNameAndType compareActor = null;
                if (MtmWorkflowStepUserActor.ACTOR_TYPE_USER.equals(userActor.getActorType())) {
                    iconName = "IconUser.gif";
                    actorName = UserInfoFactory.getUserFullName(userActor.getActorID());
                    email = UserInfoFactory.getUserEmailAddress(userActor.getActorID());
                    permission = "[" + showFullPermission(userActor.getPermission()) + "]";
                } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_GROUP.equals(userActor.getActorType())) {
                    iconName = "IconGroup.gif";
                    actorName = userActor.getActorName();
                    permission = "[" + showFullPermission(userActor.getPermission()) + "]";
                } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_ROLE.equals(userActor.getActorType())) {
                    iconName = "IconRole.gif";
                    actorName = userActor.getActorName();
                    permission = "[" + showFullPermission(userActor.getPermission()) + "]";
                } else {
                    isSpecialActor = true;
                    iconName = "IconUser.gif";
                    actorName = userActor.getActorName();
                    userActor.setActorType(MtmWorkflowStepUserActor.ACTOR_TYPE_USER);
                    permission = "[" + showFullPermission(userActor.getPermission()) + "]";
                }
                compareActor = new ActorNameAndType();
                compareActor.setActorType(userActor.getActorType());
                compareActor.setActorName(actorName);
                if (!actorList.contains(compareActor)) {
                    actorList.add(compareActor);
                } else {
                    continue;
                }
                if (nobr) {
                    sbActor.append("<nobr>");
                }
                if (showEmail && !Utility.isEmpty(email)) {
                    sbActor.append("<a href=\"mailto:");
                    sbActor.append(email);
                    sbActor.append("\">");
                }
                if (isSpecialActor) {
                    sbActor.append("<img src=\'" + WebUtil.getImage(pageContext.getRequest(), iconName) + "\' align=\'absmiddle\' border=\'0\'>[" + actorName + "]");
                } else {
                    sbActor.append("<img src=\'" + WebUtil.getImage(pageContext.getRequest(), iconName) + "\' align=\'absmiddle\' border=\'0\'>" + actorName);
                }
                if (!Utility.isEmpty(userActor.getPermission())) {
                    sbActor.append(permission);
                }
                if (showEmail && !Utility.isEmpty(email)) {
                    sbActor.append("<img src=\'" + WebUtil.getImage(pageContext.getRequest(), "IconEmail.gif") + "\' align=\'absmiddle\' border=\'0\' alt=\'" + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.send_email", userActor.getActorName()) + "\'>");
                    sbActor.append("</a>");
                }
                if (nobr) {
                    sbActor.append("</nobr>");
                }
                if (userActorList.size() > 0 && i < userActorList.size() - 1) {
                    sbActor.append("<br>");
                }
            }
        } else if (this.isAutomaticStep()) {
            sbActor.append("<img src=\'" + WebUtil.getImage(pageContext.getRequest(), "IconUser.gif") + "\' align=\'absmiddle\' border=\'0\'>[" + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "alert.label.notification_S") + "]");
        }
        log.debug(sbActor.toString());
        return sbActor.toString();
    }

    /**
   * Examine that if this stpe is automatic step. 
   * @author bill
   * @return boolean
   * @throws ApplicationException
   */
    private boolean isAutomaticStep() throws ApplicationException {
        Connection conn = (Connection) pageContext.getRequest().getAttribute(GlobalConstant.DB_KEY);
        WorkflowStepDAObject stepDAO = new WorkflowStepDAObject(sessionContainer, conn);
        WorkflowStep step = (WorkflowStep) stepDAO.getStepByID(this.stepID);
        if (WorkflowStep.ACTION_TYPE_SYSTEM_AUTO.equals(step.getActionType())) {
            return true;
        }
        return false;
    }

    /**
   * get the Special Actors name
   * @param userActor
   * @return
   * @throws ApplicationException
   */
    private String getSetupSpecialActorName(MtmWorkflowStepUserActor userActor) throws ApplicationException {
        String actorName = "";
        if (MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.report_to");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_SUBMITTER.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.submitter");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_EVERYONE.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.everyone");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_RUNTIME_ASSIGN.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.dynamic_assign");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_RECURSIVE_RUNTIME_ASSIGN.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.recurrsive_dynamic_assign");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_PREVIOUS_TASK_OWNER.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.previous_task_owner");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_PREVIOUS_TASK_ACTION_TAKER.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.previous_task_action_taker");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO_OF_PREVIOUS_TASK_ACTION_TAKER.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.report_to_of_previous_task");
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_ACTION_TAKER.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.step_n_action_taker", this.getStepSeqNo(userActor.getActorID()));
        } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO_OF_ACTION_TAKER.equals(userActor.getActorType())) {
            actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.step_n_action_taker_report_to", this.getStepSeqNo(userActor.getActorID()));
        }
        return actorName;
    }

    private String getProgressSpecialActorName(MtmWorkflowStepUserActor userActor) throws ApplicationException {
        String actorName = "";
        Connection conn = (Connection) pageContext.getRequest().getAttribute(GlobalConstant.DB_KEY);
        WorkflowProgressManager progressManager = new WorkflowProgressManager(sessionContainer, conn);
        WorkflowProgressDAObject progressDAO = new WorkflowProgressDAObject(sessionContainer, conn);
        WorkflowProgress progress = (WorkflowProgress) progressDAO.getObjectByID(progressID);
        try {
            if (MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO.equals(userActor.getActorType())) {
                List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), userActor.getActorID(), progress.getTrackID());
                if (!Utility.isEmpty(actorList)) {
                    actorName = UserInfoFactory.getUserFullName((Integer) actorList.get(0));
                } else {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.report_to");
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_SUBMITTER.equals(userActor.getActorType())) {
                List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), userActor.getActorID(), progress.getTrackID());
                if (!Utility.isEmpty(actorList)) {
                    actorName = UserInfoFactory.getUserFullName((Integer) actorList.get(0));
                } else {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.submitter");
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_EVERYONE.equals(userActor.getActorType())) {
                actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.everyone");
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_RUNTIME_ASSIGN.equals(userActor.getActorType())) {
                List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), userActor.getActorID(), progress.getTrackID());
                if (!Utility.isEmpty(actorList)) {
                    actorName = UserInfoFactory.getUserFullName((Integer) actorList.get(0));
                } else {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.dynamic_assign");
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_RECURSIVE_RUNTIME_ASSIGN.equals(userActor.getActorType())) {
                List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), userActor.getActorID(), progress.getTrackID());
                if (!Utility.isEmpty(actorList)) {
                    actorName = UserInfoFactory.getUserFullName((Integer) actorList.get(0));
                } else {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.recurrsive_dynamic_assign");
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_PREVIOUS_TASK_OWNER.equals(userActor.getActorType())) {
                MtmWfProgressWfProgressDAObject mtmProgressDAO = new MtmWfProgressWfProgressDAObject(sessionContainer, conn);
                List mtmList = mtmProgressDAO.getListByChildProgressID(progressID);
                if (progress.getWorkflowStepID().intValue() != stepID.intValue()) {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.previous_task_owner");
                    return actorName;
                }
                if (!Utility.isEmpty(mtmList)) {
                    MtmWfProgressWfProgress mtmprogress = (MtmWfProgressWfProgress) mtmList.get(0);
                    Integer parentProgressID = mtmprogress.getParentProgressID();
                    MtmWorkflowProgressUserRecordDAObject mtmUserDAO = new MtmWorkflowProgressUserRecordDAObject(sessionContainer, conn);
                    List mtmUserList = mtmUserDAO.getFinishedProgressListByWorkflowProgressID(parentProgressID);
                    if (!Utility.isEmpty(mtmUserList)) {
                        MtmWorkflowProgressUserRecord pUser = (MtmWorkflowProgressUserRecord) mtmUserList.get(0);
                        Integer id = pUser.getUserRecordID();
                        actorName = UserInfoFactory.getUserFullName(id);
                    }
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_PREVIOUS_TASK_ACTION_TAKER.equals(userActor.getActorType())) {
                MtmWfProgressWfProgressDAObject mtmProgressDAO = new MtmWfProgressWfProgressDAObject(sessionContainer, conn);
                List mtmList = mtmProgressDAO.getListByChildProgressID(progressID);
                if (progress.getWorkflowStepID().intValue() != stepID.intValue()) {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.previous_task_action_taker");
                    return actorName;
                }
                if (!Utility.isEmpty(mtmList)) {
                    MtmWfProgressWfProgress mtmprogress = (MtmWfProgressWfProgress) mtmList.get(0);
                    Integer parentProgressID = mtmprogress.getParentProgressID();
                    WorkflowProgress parentProgress = (WorkflowProgress) progressDAO.getObjectByID(parentProgressID);
                    List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), parentProgress.getWorkflowStepID(), progress.getTrackID());
                    if (!Utility.isEmpty(actorList)) {
                        for (int i = 0; i < actorList.size(); i++) {
                            String temp = UserInfoFactory.getUserFullName((Integer) actorList.get(i));
                            actorName += temp + ";";
                        }
                    }
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO_OF_PREVIOUS_TASK_ACTION_TAKER.equals(userActor.getActorType())) {
                MtmWfProgressWfProgressDAObject mtmProgressDAO = new MtmWfProgressWfProgressDAObject(sessionContainer, conn);
                List mtmList = mtmProgressDAO.getListByChildProgressID(progressID);
                if (progress.getWorkflowStepID().intValue() != stepID.intValue()) {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.report_to_of_previous_task");
                    return actorName;
                }
                if (!Utility.isEmpty(mtmList)) {
                    MtmWfProgressWfProgress mtmprogress = (MtmWfProgressWfProgress) mtmList.get(0);
                    Integer parentProgressID = mtmprogress.getParentProgressID();
                    WorkflowProgress parentProgress = (WorkflowProgress) progressDAO.getObjectByID(parentProgressID);
                    List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), parentProgress.getWorkflowStepID(), progress.getTrackID());
                    if (!Utility.isEmpty(actorList)) {
                        for (int i = 0; i < actorList.size(); i++) {
                            String temp = UserInfoFactory.getUserFullName((Integer) actorList.get(i));
                            actorName += temp + ";";
                        }
                    } else {
                        actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.report_to_of_previous_task");
                    }
                } else {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.report_to_of_previous_task");
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_ACTION_TAKER.equals(userActor.getActorType())) {
                List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), userActor.getActorID(), progress.getTrackID());
                if (!Utility.isEmpty(actorList)) {
                    for (int i = 0; i < actorList.size(); i++) {
                        String temp = UserInfoFactory.getUserFullName((Integer) actorList.get(i));
                        actorName += temp + ";";
                    }
                } else {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.step_n_action_taker", this.getStepSeqNo(userActor.getActorID()));
                }
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO_OF_ACTION_TAKER.equals(userActor.getActorType())) {
                List actorList = progressManager.getActorIDListByProgressIDAndRecordID(userActor.getActorType(), userActor.getActorID(), progress.getTrackID());
                if (!Utility.isEmpty(actorList)) {
                    for (int i = 0; i < actorList.size(); i++) {
                        String temp = UserInfoFactory.getUserFullName((Integer) actorList.get(i));
                        actorName += temp + ";";
                    }
                } else {
                    actorName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.step_n_action_taker_report_to", this.getStepSeqNo(userActor.getActorID()));
                }
            }
        } catch (Exception e) {
            log.info("getProgressSpecialActorName Error");
        }
        if (actorName.indexOf(";") != -1) {
            actorName = actorName.substring(0, actorName.lastIndexOf(";"));
        }
        return actorName;
    }

    protected String getFilterByDescription() throws ApplicationException {
        StringBuffer sbFilter = new StringBuffer();
        if (!Utility.isEmpty(getFilterBy())) {
            if (nobr) {
                sbFilter.append("<nobr>");
            }
            if ((new Integer(1)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_1"));
            } else if ((new Integer(2)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_2"));
            } else if ((new Integer(3)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_3"));
            } else if ((new Integer(4)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_4"));
            } else if ((new Integer(5)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_5"));
            } else if ((new Integer(6)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_6"));
            } else if ((new Integer(7)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_7"));
            } else if ((new Integer(8)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_8"));
            } else if ((new Integer(9)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_9"));
            } else if ((new Integer(10)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "staff.label.hierarchy_level_10"));
            } else if ((new Integer(-1)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "setup.label.subject_type_G"));
            } else if ((new Integer(-2)).equals(getFilterBy())) {
                sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "setup.label.subject_type_R"));
            }
            if (nobr) {
                sbFilter.append("</nobr>");
            }
        }
        log.debug(sbFilter.toString());
        return sbFilter.toString();
    }

    protected void init() throws ApplicationException {
        sessionContainer = (SessionContainer) this.pageContext.getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
        contextPath = (String) this.pageContext.getServletContext().getAttribute(GlobalConstant.CONTEXT_PATH_KEY);
        if (!validateTag()) {
            throw new IllegalArgumentException();
        }
        if (userActorList == null || filterBy == null) {
            Connection conn = (Connection) pageContext.getRequest().getAttribute(GlobalConstant.DB_KEY);
            MtmWorkflowStepUserActorDAObject userActorDAO = new MtmWorkflowStepUserActorDAObject(sessionContainer, conn);
            WorkflowStepDAObject stepDAO = new WorkflowStepDAObject(sessionContainer, conn);
            if (userActorList == null) {
                userActorList = userActorDAO.getListByWorkflowStepID(stepID);
            }
            userActorList = getTheActorNameForUserActorList(userActorList);
            if (filterBy == null) {
                filterBy = ((WorkflowStep) stepDAO.getObjectByID(stepID)).getFilterBy();
            }
        }
    }

    private List getTheActorNameForUserActorList(List userActorList) throws ApplicationException {
        List userActorWithNameList = new ArrayList();
        for (Iterator it = userActorList.iterator(); it.hasNext(); ) {
            MtmWorkflowStepUserActor userActor = (MtmWorkflowStepUserActor) it.next();
            if (MtmWorkflowStepUserActor.ACTOR_TYPE_USER.equals(userActor.getActorType())) {
                userActorWithNameList.add(userActor);
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_GROUP.equals(userActor.getActorType())) {
                userActorWithNameList.add(userActor);
            } else if (MtmWorkflowStepUserActor.ACTOR_TYPE_ROLE.equals(userActor.getActorType())) {
                userActorWithNameList.add(userActor);
            } else {
                String actorName = "";
                if (getProgressID() == null) {
                    actorName = getSetupSpecialActorName(userActor);
                } else {
                    actorName = getProgressSpecialActorName(userActor);
                }
                if (!Utility.isEmpty(actorName)) {
                    String[] tempActorNames = actorName.split(";");
                    for (int i = 0; i < tempActorNames.length; i++) {
                        MtmWorkflowStepUserActor tempUserActor = (MtmWorkflowStepUserActor) userActor.clone();
                        tempUserActor.setActorName(tempActorNames[i]);
                        userActorWithNameList.add(tempUserActor);
                    }
                }
            }
        }
        return userActorWithNameList;
    }

    /**
   * get the step's Seq No. by the workfow step ID.
   * @param workflowStepID
   * @return
   * @throws ApplicationException
   */
    protected String getStepSeqNo(Integer workflowStepID) throws ApplicationException {
        String stepSeqNo = "";
        sessionContainer = (SessionContainer) this.pageContext.getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
        Connection conn = (Connection) pageContext.getRequest().getAttribute(GlobalConstant.DB_KEY);
        WorkflowStepDAObject stepDAO = new WorkflowStepDAObject(sessionContainer, conn);
        WorkflowStep step = (WorkflowStep) stepDAO.getObjectByID(workflowStepID);
        if (step != null) {
            stepSeqNo = step.getStepSeq().toString();
        }
        return stepSeqNo;
    }

    /**
   * add by TC.tang by Bug-1693
   * @author TC.tang
   *
   */
    class ActorNameAndType {

        String actorName = null;

        String actorType = null;

        public String getActorName() {
            return actorName;
        }

        public void setActorName(String actorName) {
            this.actorName = actorName;
        }

        public String getActorType() {
            return actorType;
        }

        public void setActorType(String actorType) {
            this.actorType = actorType;
        }

        public boolean equals(Object obj) {
            return (obj != null && ((ActorNameAndType) obj).getActorName().equals(this.getActorName()) && ((ActorNameAndType) obj).getActorType().equals(this.getActorType()));
        }
    }
}
