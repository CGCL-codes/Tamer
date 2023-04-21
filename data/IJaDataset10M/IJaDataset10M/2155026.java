package org.posterita.struts.admin;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.posterita.core.TmkJSPEnv;
import org.posterita.businesslogic.CheckSequenceManager;
import org.posterita.exceptions.SequenceUpdateException;
import org.posterita.struts.core.BaseDispatchAction;

public class CheckSequenceAction extends BaseDispatchAction {

    public static final String CHECK_SEQUENCE = "checkSequence";

    public ActionForward checkSequence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        Properties ctx = TmkJSPEnv.getCtx(request);
        try {
            CheckSequenceManager.runProcess(ctx);
        } catch (SequenceUpdateException e) {
            postGlobalError("error.update.sequence", request);
            return mapping.getInputForward();
        }
        return mapping.findForward(CHECK_SEQUENCE);
    }
}
