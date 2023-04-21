package net.sf.wgfa.struts.action.selection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.wgfa.exceptions.WgfaMissingRequestAttributeException;
import net.sf.wgfa.selection.ResourceFilter;
import net.sf.wgfa.selection.ResourceSelection;
import net.sf.wgfa.selection.ResourceSelector;
import net.sf.wgfa.struts.form.selection.SelectionForm;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Base action for actions of the resource selection system. It provides the
 * configuration object from the session context to any extending class and
 * JSPs in the request context.
 *
 * @author tobias
 */
public abstract class SelectionAction extends Action {

    /**
     * Where to store the configuration object in the request context
     */
    protected static final String CONFIG = "selection";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id;
        if (form != null && form instanceof SelectionForm) {
            id = ((SelectionForm) form).getId();
        } else if (request.getParameter("id") != null) {
            id = request.getParameter("id");
        } else {
            throw new WgfaMissingRequestAttributeException("id");
        }
        ResourceSelector rs = ResourceSelector.getInstance(request.getSession(), id);
        if (rs == null) {
            ResourceSelection fullSelection = new ResourceFilter(null, null);
            rs = ResourceSelector.createInstance(request.getSession(), id, fullSelection, true);
        }
        request.setAttribute(CONFIG, rs);
        request.setAttribute("id", id);
        return doSelectionAction(mapping, form, request, response, rs);
    }

    public abstract ActionForward doSelectionAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ResourceSelector rs) throws Exception;
}
