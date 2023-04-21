package ces.research.oa.document.meetingcontrol.form;

import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import ces.arch.form.ListForm;
import ces.research.oa.entity.MeetingControlPojo;

;

public class DesktopListQueryForm extends ListForm {

    private Date comeDate;

    private String dept;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        query = "<query id=\"0\">" + "<target pojo=\"MeetingControlPojo\" as=\"o\"/>" + "</query>";
    }

    public Date getComeDate() {
        return comeDate;
    }

    public void setComeDate(Date comeDate) {
        this.comeDate = comeDate;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
