package de.creatronix.artist3k.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import de.creatronix.artist3k.controller.form.OrganizerListForm;
import de.creatronix.artist3k.model.ModelController;
import de.creatronix.artist3k.model.OrganizerManager;

public class OrganizerListAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        OrganizerListForm organizerListForm = (OrganizerListForm) form;
        OrganizerManager organizerManager = ModelController.getInstance().getOrganizerManager();
        organizerListForm.setOrganizers(organizerManager.getAllOrganizers());
        return mapping.findForward("showOrganizerList");
    }
}
