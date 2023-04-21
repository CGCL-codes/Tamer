package struts.optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import struts.easy.ActionMappingExtended;

public abstract class ExtAction extends Action {

    public ActionHelper helper = ActionHelper.getInstance();

    ;

    public final ActionForward execute(ActionMapping actionmapping, ActionForm arg1, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("Rollen:" + actionmapping.getRoles());
        ActionForward forward = null;
        ActionForward helperforward = null;
        helperforward = helper.startExt((ActionMappingExtended) actionmapping, request, response);
        if (helperforward != null) {
            helper.endExt();
            return helperforward;
        }
        forward = doexecute(actionmapping, arg1, request, response);
        helperforward = helper.endExt();
        if (helperforward != null) forward = helperforward;
        return forward;
    }

    public abstract ActionForward doexecute(ActionMapping actionmapping, ActionForm arg1, HttpServletRequest request, HttpServletResponse arg3) throws Exception;
}
