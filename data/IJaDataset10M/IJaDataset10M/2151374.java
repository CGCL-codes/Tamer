package com.dcivision.workflow.taglib;

import java.sql.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.WebUtil;
import com.dcivision.workflow.bean.WorkflowProgress;
import com.dcivision.workflow.bean.WorkflowStep;
import com.dcivision.workflow.dao.WorkflowProgressDAObject;

/**
 * @author           Lun Au
 * @company          DCIVision Limited
 * @creation date    2005/1/3
 * @version          $Revision: 1.11.2.3 $
 */
public class WorkflowStepInfoTag extends WorkflowStepOwnerTag {

    public static final String REVISION = "$Revision: 1.11.2.3 $";

    protected Log log = LogFactory.getLog(this.getClass().getName());

    private Integer progressID;

    private String styleClass;

    private String functionCode = null;

    private String navMode = null;

    private String delegateUserRecordID = null;

    public WorkflowStepInfoTag() {
        super();
    }

    /**
   * @return Returns the progressID.
   */
    public Integer getProgressID() {
        return progressID;
    }

    /**
   * @param progressID The progressID to set.
   */
    public void setProgressID(Integer progressID) {
        this.progressID = progressID;
    }

    /**
   * @return Returns the styleClass.
   */
    public String getStyleClass() {
        return styleClass;
    }

    /**
   * @param styleClass The styleClass to set.
   */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getDelegateUserRecordID() {
        return delegateUserRecordID;
    }

    public void setDelegateUserRecordID(String delegateUserRecordID) {
        this.delegateUserRecordID = delegateUserRecordID;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getNavMode() {
        return navMode;
    }

    public void setNavMode(String navMode) {
        this.navMode = navMode;
    }

    public void release() {
        super.release();
        progressID = null;
        this.setFunctionCode(null);
        this.setNavMode(null);
        this.setDelegateUserRecordID(null);
    }

    protected boolean validateTag() {
        return (this.getWorkflowProgress() != null || progressID != null);
    }

    protected String getContent() throws ApplicationException {
        StringBuffer sb = new StringBuffer();
        if (WorkflowStep.END_STEP_SEQ_NO.equals(this.getWorkflowProgress().getStepSeq())) {
            sb.append("<img src=\"" + WebUtil.getImage(pageContext.getRequest(), "IconInfinity.gif") + "\" align=\"absmiddle\" border=\"0\">");
        } else {
            sb.append(this.getWorkflowProgress().getStepSeq()).append(". ");
        }
        this.setRight("R");
        this.setObjectType(GlobalConstant.WORKFLOW_OWNER_PERMISSION);
        this.setObjectID(this.getWorkflowProgress().getWorkflowStepID());
        if (this.checkPermission()) {
            sb.append("<a ");
            sb.append("href=\"#\" ");
            if (!Utility.isEmpty(styleClass)) {
                sb.append("class=\"" + styleClass + "\" ");
            }
            if (SystemFunctionConstant.WORKFLOW_TASK.equals(this.getFunctionCode())) {
                sb.append("onClick=\"return OpEditProcess(\'" + this.getWorkflowProgress().getTrackID() + "\', \'" + this.getWorkflowProgress().getID() + "\', \'" + this.getWorkflowProgress().getWorkflowRecordID() + "\', \'" + this.getWorkflowProgress().getWorkflowStepID() + "\', \'" + delegateUserRecordID + "\', \'" + this.getWorkflowProgress().getStatus() + "\', \'" + navMode + "\')\" ");
            } else if (SystemFunctionConstant.WORKFLOW_TRACK.equals(this.getFunctionCode())) {
                sb.append("onClick=\"return OpViewTrack(\'" + this.getWorkflowProgress().getTrackID() + "\', \'" + this.getWorkflowProgress().getID() + "\', \'" + this.getWorkflowProgress().getWorkflowRecordID() + "\')\" ");
            } else {
                sb.append("onclick=\"return OpenCenteredPopup('" + contextPath + "/workflow/MaintWorkflowProgress.do?navMode=V&trackID=" + (this.getWorkflowProgress()).getTrackID() + "&workflowStepID=" + (this.getWorkflowProgress()).getWorkflowStepID() + "','DCIVisionProgressView',630, 300, 'scrollbars=yes')\" ");
            }
            sb.append("onmouseover=\"return getPopupInfo('");
            sb.append(TextUtility.escapeJSString(getActorNameDescription()));
            sb.append(TextUtility.escapeJSString(getFilterByDescription()));
            sb.append("');\" ");
            sb.append("onmouseout=\"return nd();\"");
            sb.append(">");
            sb.append(this.getWorkflowProgress().getStepName());
            sb.append("</a>");
        } else {
            sb.append(this.getWorkflowProgress().getStepName());
        }
        return sb.toString();
    }

    protected String getActorNameDescription() throws ApplicationException {
        StringBuffer sbActor = new StringBuffer();
        sbActor.append("<b><u style=\'font-size:11px\'>");
        sbActor.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.actor"));
        sbActor.append(":</u></b><br>");
        sbActor.append(super.getActorNameDescription());
        log.debug(sbActor.toString());
        return sbActor.toString();
    }

    protected String getFilterByDescription() throws ApplicationException {
        if (Utility.isEmpty(super.getFilterBy())) {
            return "";
        }
        StringBuffer sbFilter = new StringBuffer();
        sbFilter.append("<br><b><u style=\'font-size:11px\'>");
        sbFilter.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "workflow.label.filter_by"));
        sbFilter.append(":</u></b><br>");
        sbFilter.append(super.getFilterByDescription());
        log.debug(sbFilter.toString());
        return sbFilter.toString();
    }

    protected void init() throws ApplicationException {
        if (!validateTag()) {
            throw new IllegalArgumentException();
        }
        if (this.getWorkflowProgress() == null) {
            Connection conn = (Connection) pageContext.getRequest().getAttribute(GlobalConstant.DB_KEY);
            WorkflowProgressDAObject progressDAO = new WorkflowProgressDAObject(sessionContainer, conn);
            if (this.getWorkflowProgress() == null) {
                log.debug("Getting progress object of id=" + progressID);
                this.setWorkflowProgress((WorkflowProgress) progressDAO.getObjectByID(progressID));
            }
        } else if (this.getWorkflowProgress().getUserActorList() != null) {
            super.setUserActorList(this.getWorkflowProgress().getUserActorList());
            super.setFilterBy(null);
        }
        super.setStepID(this.getWorkflowProgress().getWorkflowStepID());
        super.init();
    }

    public boolean checkPermission() throws ApplicationException {
        boolean permission = false;
        permission = super.checkPermission();
        if (!permission && !Utility.isEmpty(this.delegateUserRecordID) && !"null".equals(this.delegateUserRecordID)) {
            this.setUserRecordID(new Integer(this.delegateUserRecordID));
            permission = super.checkPermission();
        }
        return permission;
    }
}
