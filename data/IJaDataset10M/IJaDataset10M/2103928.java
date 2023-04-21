package actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logic.account.AccountDetails;
import logic.account.ContentSpecification;
import logic.account.FolderContentSpecification;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Displays first page of current mail folder contents.
 * @author marcin
 */
public class ViewFolder extends CommonFolderView {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession session = request.getSession();
            AccountDetails details = (AccountDetails) session.getAttribute("accountDetails");
            String folderName = request.getParameter("folderName");
            long folderId = Long.parseLong((String) request.getParameter("folderId"));
            FolderContentSpecification spec = new FolderContentSpecification(details.id, folderId);
            session.setAttribute("folderStart", new Integer(0));
            spec.setSortOrder(ContentSpecification.ORDERBYDATE);
            spec.setOrderReversed(false);
            session.setAttribute("contentSpec", spec);
            session.setAttribute("folderActiveId", new Long(folderId));
            session.setAttribute("folderActive", folderName);
            return resetFolderContentList(request, mapping);
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findForward("notConnected");
        }
    }
}
