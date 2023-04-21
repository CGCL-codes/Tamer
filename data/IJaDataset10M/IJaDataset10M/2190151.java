package ces.research.oa.document.basecourse.form;

import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import ces.arch.form.ListForm;
import ces.research.oa.entity.WorkWelcomePojo;

;

public class DesktopListQueryForm extends ListForm {

    private Date comeDate;

    private Date leaveDate;

    private String fileNumber;

    private String title;

    private String sendUnit;

    private String overUnit;

    private String signUser;

    private String secret;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        query = "<query id=\"0\">" + "<target pojo=\"BasecoursePojo\" as=\"o\"/>" + "<compare target=\"o.signDate\" name=\"comeDate\" operator=\"GE\"/>" + "<compare target=\"o.signDate\" name=\"leaveDate\" operator=\"LE\"/>" + "<compare target=\"o.fileNumber\" name=\"fileNumber\" operator=\"LK\"/>" + "<compare target=\"o.title\" name=\"title\" operator=\"LK\"/>" + "<compare target=\"o.sendUnit\" name=\"sendUnit\" operator=\"LK\"/>" + "<compare target=\"o.overUnit\" name=\"overUnit\" operator=\"EQ\"/>" + "<compare target=\"o.signUser\" name=\"signUser\" operator=\"LK\"/>" + "<compare target=\"o.secret\" name=\"secret\" operator=\"EQ\"/>" + "</query>";
    }

    public Date getComeDate() {
        return comeDate;
    }

    public void setComeDate(Date comeDate) {
        this.comeDate = comeDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSendUnit() {
        return sendUnit;
    }

    public void setSendUnit(String sendUnit) {
        this.sendUnit = sendUnit;
    }

    public String getOverUnit() {
        return overUnit;
    }

    public void setOverUnit(String overUnit) {
        this.overUnit = overUnit;
    }

    public String getSignUser() {
        return signUser;
    }

    public void setSignUser(String signUser) {
        this.signUser = signUser;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
