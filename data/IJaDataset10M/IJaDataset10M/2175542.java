package ces.research.oa.document.scope.action;

import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.ListAction;
import ces.arch.bo.BusinessException;
import ces.arch.form.BaseForm;
import ces.arch.form.ListForm;
import ces.arch.query.ListQury;
import ces.arch.query.QueryParser;
import ces.platform.system.dbaccess.User;
import ces.research.oa.document.scope.form.ScopeListForm;
import ces.research.oa.document.util.OAConstants;

public class ScopeListAction extends ListAction {

    public ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws BusinessException {
        User user = (User) request.getSession().getAttribute(OAConstants.ATTRIBUTE_USER);
        if (user == null) {
            return mapping.findForward(OAConstants.FORWARD_LOGIN);
        }
        String doAction = null;
        ScopeListForm listForm = (ScopeListForm) form;
        doAction = listForm.getDoAction();
        List result = null;
        if ("list".equals(doAction)) {
            QueryParser qp = new QueryParser((ListForm) form);
            ListQury query = qp.parse();
            query.setCursor(query.getCursor() - 1);
            query.setOrderby("id");
            query.setNumPerPage(((ListForm) form).getNumPerPage());
            result = getBo().find(query);
            listForm.setTotalNum(query.getTotalNum());
        } else if ("jsonlist".equals(doAction)) {
            result = getBo().getDao().getHibernateTemplate().find("from ScopePojo where reg_user_id='" + listForm.getRegUserId() + "'");
            String data = JSONArray.fromObject(result).toString();
            response.setContentType("text/html;charset=GBK");
            try {
                PrintWriter pw = response.getWriter();
                pw.write(data);
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapping.findForward(null);
        }
        request.setAttribute("result", result);
        request.setAttribute("ScopeListForm", listForm);
        return mapping.findForward("success");
    }
}
