package ces.research.oa.document.inspect.form;

import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import ces.arch.form.ListForm;

public class DesktopListQueryForm extends ListForm {

    private String title;

    private Date endTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        query = "<query id=\"0\">" + "<target pojo=\"InspectPojo\" as=\"o\"/>" + "<compare target=\"o.title\" name=\"title\" operator=\"LK\"/>" + "<compare target=\"o.endTime\" name=\"endTime\" operator=\"EQ\"/>" + "</query>";
    }
}
