package ca.ubc.icapture.genapha.actions;

import ca.ubc.icapture.genapha.forms.MoveGeneSnpForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import icapture.SQLMgr;

public class GetSetLookUps extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ActionForward forward = new ActionForward();
        forward = mapping.findForward("admin");
        MoveGeneSnpForm mgsForm = (MoveGeneSnpForm) form;
        try {
            mgsForm.setGeneSetLookUp(SQLMgr.getAllGeneSetLookUp());
            mgsForm.setSnpSetLookUp(SQLMgr.getAllSnpSetLookUp());
        } catch (Exception e) {
            System.out.println("could not get snp or gene set");
        }
        return forward;
    }
}
