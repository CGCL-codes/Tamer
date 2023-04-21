package ces.coffice.webmail.ui.action;

import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.coffice.webmail.util.GarbagerFilter;
import ces.platform.system.common.Translate;
import ces.coffice.webmail.util.Constant;
import java.io.UnsupportedEncodingException;

/**
 * <p>Title: </p>
 *
 * <p>Description: add address to garbageAddress</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: CES</p>
 *
 * @author chenlei
 * @version 1.0
 */
public class AddGbgMailAddressAction extends Action {

    /**
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws ServletException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, UnsupportedEncodingException {
        String strTarget = "success";
        String fileName = request.getParameter("garbageFile") == null ? "" : request.getParameter("garbageFile");
        String address = request.getParameter("sender") == null ? "" : Translate.translate(request.getParameter("sender"), Constant.PARAM_GET);
        String flag = request.getParameter("flag") == null ? "" : request.getParameter("flag");
        if (fileName.equals("") || address.equals("")) {
            request.setAttribute("messageKey", "webmail.mail.parameter.error");
            return mapping.findForward("mailalert");
        } else {
            try {
                GarbagerFilter.addGbgMailAddress(fileName, address);
                Vector rs = (Vector) GarbagerFilter.getGbgAddress(fileName);
                request.setAttribute("rs", rs);
                request.setAttribute("file", fileName);
                if ("true".equals(flag)) {
                    request.setAttribute("status", Constant.SUCCESS_UP_RELOAD);
                } else {
                    request.setAttribute("status", Constant.SUCCESS_TO_CLOSE);
                }
                request.setAttribute("messageKey", "���óɹ���");
                strTarget = "mailalert";
                return mapping.findForward(strTarget);
            } catch (Exception ex) {
                ex.printStackTrace();
                return mapping.findForward("mailalert");
            }
        }
    }
}
