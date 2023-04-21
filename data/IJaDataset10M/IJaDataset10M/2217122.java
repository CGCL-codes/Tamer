package ces.sf.oa.insend.form;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import ces.arch.form.ListForm;
import ces.oa.util.BusinessUtil;

public class ViewListForm extends ListForm {

    private String infoType;

    private String sendTime;

    private String exceptPid = "-1";

    private String title;

    private String orgId;

    private String issue = "1";

    private String fileDraftType;

    private Date insendDatebegin;

    private Date insendDateend;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        orgId = BusinessUtil.getUserOrgId(BusinessUtil.getCurrentUserId(request));
        query = "<query id=\"0\">" + "<target pojo=\"InsendPojo\" as=\"o\"/>" + "<compare target=\"o.infotype\" name=\"infoType\" operator=\"EQ\"/>" + "<compare target=\"o.orgId\" name=\"orgId\" operator=\"EQ\"/>" + "<compare target=\"o.issue\" name=\"issue\" operator=\"EQ\"/>" + "<compare target=\"o.fileDraftType\" name=\"fileDraftType\" operator=\"EQ\"/>" + "<compare target=\"o.sendFileTime\" name=\"insendDatebegin\" operator=\"GE\"/>" + "<compare target=\"o.sendFileTime\" name=\"insendDateend\" operator=\"LE\"/>" + "<compare target=\"o.fileName\" name=\"title\" operator=\"LK\"/>" + "<compare target=\"o.processInstanceId\" name=\"exceptPid\" operator=\"NE\"/>" + "</query>";
        orderBy = "id";
        orderType = "desc";
    }

    public String getFileDraftType() {
        return fileDraftType;
    }

    public void setFileDraftType(String fileDraftType) {
        this.fileDraftType = fileDraftType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getExceptPid() {
        return exceptPid;
    }

    public void setExceptPid(String exceptPid) {
        this.exceptPid = exceptPid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getInsendDatebegin() {
        return insendDatebegin;
    }

    public void setInsendDatebegin(Date insendDatebegin) {
        this.insendDatebegin = insendDatebegin;
    }

    public Date getInsendDateend() {
        return insendDateend;
    }

    public void setInsendDateend(Date insendDateend) {
        this.insendDateend = insendDateend;
    }
}
