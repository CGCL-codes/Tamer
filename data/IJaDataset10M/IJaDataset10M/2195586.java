package org.stuzonk.sd.actions;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNumeric;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.stuzonk.RevisionNumberMismatchException;
import org.stuzonk.RevisionNumberTooOldException;
import org.stuzonk.SZTreeConflict;
import org.stuzonk.SZTreeManager;
import org.stuzonk.SZTreeNode;
import org.stuzonk.sd.config.SZConstants;
import org.stuzonk.sd.tree.MySZTreeObject;

/**
 * Action che gestisce la notifica da parte dell'utente di
 * un nodo eliminato.
 * @author michele
 *
 */
public class NotifyDeleteAction extends Action {

    private static final Logger logger = Logger.getLogger(NotifyDeleteAction.class);

    @SuppressWarnings("unchecked")
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm arg1, HttpServletRequest request, HttpServletResponse arg3) throws Exception {
        ActionForward forward = mapping.findForward(SZConstants.FWD_FAILURE);
        logger.trace(SZConstants.LOG_STARTING);
        String rn_par = request.getParameter(SZConstants.REQ_REVISION_NUMBER);
        String id_par = request.getParameter(SZConstants.REQ_NODE_ID);
        if (checkParams(rn_par, id_par)) {
            long revisionNumber = Long.parseLong(rn_par);
            SZTreeManager<MySZTreeObject> manager = (SZTreeManager<MySZTreeObject>) request.getSession().getServletContext().getAttribute(SZConstants.APP_TREE_MANAGER);
            try {
                SZTreeNode<MySZTreeObject> removed = manager.removeNode(Long.parseLong(rn_par), Integer.parseInt(id_par));
                if (removed == null) {
                    forward = mapping.findForward(SZConstants.FWD_CONFLICT);
                    request.setAttribute(SZConstants.REQ_CONFLICT, new SZTreeConflict(SZConstants.CONFLICT_ERROR, "Unable To Remove"));
                } else {
                    forward = mapping.findForward(SZConstants.FWD_SUCCESS);
                    revisionNumber++;
                }
            } catch (RevisionNumberMismatchException e) {
                request.setAttribute(SZConstants.REQ_CONFLICT, new SZTreeConflict(SZConstants.CONFLICT_MISMATCH_ID, "Unable To Remove node, resync is needed"));
                forward = mapping.findForward(SZConstants.FWD_REVISION_MISMATCH);
                revisionNumber = manager.getRevisionNumber();
            } catch (RevisionNumberTooOldException e) {
                forward = mapping.findForward(SZConstants.FWD_REVISION_TOO_OLD);
                request.setAttribute(SZConstants.REQ_CONFLICT, new SZTreeConflict(SZConstants.CONFLICT_RESYNC_NEEDED, "Unable To Remove, total resync is needed"));
                revisionNumber = manager.getRevisionNumber();
            }
            request.setAttribute(SZConstants.REQ_REVISION_NUMBER, new Long(revisionNumber));
        }
        return forward;
    }

    private boolean checkParams(String rn_par, String id_par) {
        return isNotEmpty(id_par) && isNotEmpty(rn_par) && isNumeric(rn_par);
    }
}
