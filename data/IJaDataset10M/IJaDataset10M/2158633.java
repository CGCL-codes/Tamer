package ces.research.oa.document.tel.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ces.arch.bo.IBo;
import ces.arch.form.EditForm;
import ces.research.oa.entity.TelPojo;

public class TelForm extends EditForm {

    private String workitemId;

    private String activity;

    private String startFlow;

    private List postilList;

    private List leaderPostilList;

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        this.bean = new TelPojo();
        if (arg1.getParameter("bean.id") != null && !"0".equals(arg1.getParameter("bean.id"))) {
            IBo bo = (IBo) (WebApplicationContextUtils.getRequiredWebApplicationContext(arg1.getSession().getServletContext()).getBean("RawBaseBo"));
            bean = (bo).get(bean.getClass(), new Long(arg1.getParameter("bean.id")));
        }
    }

    public String getActivity() {
        return activity;
    }

    public String getWorkitemId() {
        return workitemId;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    public String getStartFlow() {
        return startFlow;
    }

    public void setStartFlow(String startFlow) {
        this.startFlow = startFlow;
    }

    public List getPostilList() {
        return postilList;
    }

    public void setPostilList(List postilList) {
        this.postilList = postilList;
    }

    public List getLeaderPostilList() {
        return leaderPostilList;
    }

    public void setLeaderPostilList(List leaderPostilList) {
        this.leaderPostilList = leaderPostilList;
    }
}
