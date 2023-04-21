package edu.ucla.mbi.curator.actions.curator.feature;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Mar 23, 2006
 * Time: 12:45:29 PM
 */
public class AddExperimentalFeature extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        new AddFeatureToParticipant().execute(mapping, form, request, response);
        request.setAttribute("featureType", "experimental");
        return mapping.findForward("success");
    }
}
