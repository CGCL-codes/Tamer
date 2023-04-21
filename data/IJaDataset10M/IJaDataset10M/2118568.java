package www.user.show;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import business.db.exceptions.DatabaseException;
import business.management.Newses;
import www.DbAction;

/** 
 * This class prepares the bean for the WEB-INF/user/show/news.jsp
 * @author Christian Templin
 */
public class ShowArchiveAction extends DbAction {

    /**
   * This method prepares a bean with the following data:
   * - news: a vector filled with news objects from the database
   * - pageLinks : this is used to create the navigation bar with the current page and entires per page
   * 
   * @author Christian Templin
   * @date 20.07.2006
   * 
   * @param pMapping : Available action-forward mapping to select from. These are defined in the struts-config.xml and are action-specific.
   * @param pForm : A populated instance of the form bean that is associated with this action. Depending on the request, it may not contain all or only default values.
   * @param pRequest : A HTTPServletRequest object containing all relevant information to the request.
   * @param pResponse : A HTTPServletResponse object that result information can be written to.
   * @return ActionForward : Unless an exception is thrown the method forwards with "success" as defined in the struts-config.xml.
   * @throws DatabaseException : This exception is thrown when the database call fails.
   */
    protected ActionForward dbExecute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws DatabaseException {
        Newses news = database.acquireNewsByRange(fCurrentPage - 1, fItemsPerPage);
        fMaxPages = (int) Math.ceil((float) (database.countNews()) / (float) fItemsPerPage);
        pRequest.setAttribute("pageLinks", createPageLinks());
        pRequest.setAttribute("news", news);
        return (pMapping.findForward("success"));
    }
}
