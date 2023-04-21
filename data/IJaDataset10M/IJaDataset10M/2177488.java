package www.user.submit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import business.bibtex.Publication;
import business.db.exceptions.DatabaseException;
import business.management.Favorite;
import business.management.User;
import www.DbAction;

public class SubmitFavoriteAction extends DbAction {

    /**
   * @author Gregor Poloczek
   * @date 11.06.2006
   */
    protected ActionForward dbExecute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws DatabaseException {
        String action = "none";
        int publicationKey = -1;
        int favoriteKey = -1;
        if (pRequest.getParameter("publication") != null) {
            publicationKey = Integer.parseInt(pRequest.getParameter("publication"));
        }
        if (pRequest.getParameter("favorite") != null) {
            favoriteKey = Integer.parseInt(pRequest.getParameter("favorite"));
        }
        User currentUser = (User) (pRequest.getSession().getAttribute("login"));
        if (currentUser == null) {
            return (pMapping.findForward("denied"));
        }
        Publication publication = new Publication();
        publication.setPrimaryKey(publicationKey);
        Favorite favorite = new Favorite();
        favorite.setPrimaryKey(favoriteKey);
        favorite.getPublication().setPrimaryKey(publicationKey);
        favorite.setOwner(currentUser);
        if (publicationKey != -1) {
            action = "submit";
        }
        if (favoriteKey != -1) {
            action = "delete";
        }
        if (action.equals("none")) {
        } else if (action.equals("submit")) {
            Favorite tempFavorite = database.acquireFavoriteByUserAndPublication(favorite.getOwner(), favorite.getPublication());
            if (tempFavorite == null) {
                database.updateFavorite(favorite);
            }
            pRequest.setAttribute("helpKey", new Integer(favorite.getPublication().getPrimaryKey()));
            return (pMapping.findForward("finished"));
        } else if (action.equals("delete")) {
            Favorite tempFavorite = database.acquireFavoriteByPrimaryKey(favoriteKey);
            if (tempFavorite != null) {
                if (tempFavorite.getOwner().equals(currentUser) || currentUser.getAdminRights()) {
                    database.deleteFavorite(favorite);
                } else {
                    return (pMapping.findForward("denied"));
                }
            }
            pRequest.setAttribute("helpKey", new Integer(tempFavorite.getPublication().getPrimaryKey()));
            return (pMapping.findForward("finished"));
        }
        return (pMapping.findForward("failure"));
    }
}
