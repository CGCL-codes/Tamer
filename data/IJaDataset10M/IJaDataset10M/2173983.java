package de.creatronix.artist3k.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import de.creatronix.artist3k.controller.form.StageActListForm;
import de.creatronix.artist3k.model.ModelController;
import de.creatronix.artist3k.model.StageActManager;

public class StageActListAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        StageActListForm stageActListForm = (StageActListForm) form;
        StageActManager stageActManager = ModelController.getInstance().getStageActManager();
        stageActListForm.setStageActs(stageActManager.getAllStageActs());
        return mapping.findForward("showStageActList");
    }
}
