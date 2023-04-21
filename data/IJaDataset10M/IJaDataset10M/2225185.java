package ces.research.oa.document.meetingcontrol.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.ListAction;
import ces.arch.bo.BusinessException;
import ces.arch.form.BaseForm;
import ces.arch.query.ListQury;
import ces.arch.query.QueryParser;
import ces.platform.system.dbaccess.User;
import ces.research.oa.document.util.OAConstants;
import ces.research.oa.document.meetingcontrol.form.DesktopListQueryForm;

public class DesktopListAction extends ListAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws BusinessException {
        log.info("document.meetingcontrol.action.DesktopListAction:running");
        User user = (User) request.getSession().getAttribute(OAConstants.ATTRIBUTE_USER);
        String doAction = "";
        doAction = request.getParameter("doAction");
        if (null == doAction || "".equals(doAction)) {
            doAction = request.getParameter("doAction") == null ? "" : request.getParameter("doAction");
        }
        DesktopListQueryForm desForm = (DesktopListQueryForm) form;
        if ("query".equals(doAction)) {
            ListQury filter = new ListQury();
            filter.setCursor(desForm.getCurrentPage() - 1);
            filter.setNumPerPage(desForm.getNumPerPage());
            desForm.setOrderBy("sequence_num");
            desForm.setOrderType("desc");
            QueryParser parser = new QueryParser(desForm);
            filter = parser.parse();
            filter.setNumPerPage(desForm.getNumPerPage());
            List list = getBo().find(filter);
            desForm.setTotalNum(filter.getTotalNum());
            request.setAttribute("DesktopListQueryForm", desForm);
            request.setAttribute("dataList", list);
        } else if ("docListPrint".equals(doAction)) {
            ListQury filter = new ListQury();
            filter.setCursor(desForm.getCurrentPage() - 1);
            filter.setNumPerPage(desForm.getNumPerPage());
            desForm.setOrderBy("sequence_num");
            desForm.setOrderType("desc");
            QueryParser parser = new QueryParser(desForm);
            filter = parser.parse();
            filter.setNumPerPage(desForm.getNumPerPage());
            List list = getBo().find(filter);
            desForm.setTotalNum(filter.getTotalNum());
            request.setAttribute("DesktopListQueryForm", desForm);
            request.setAttribute("dataList", list);
            return mapping.findForward("docListPrint");
        } else {
        }
        return mapping.findForward("list");
    }
}
