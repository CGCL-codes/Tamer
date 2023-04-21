package edu.ucsd.cse135.gas.action.reviewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.ucsd.cse135.gas.logic.reviewer.ReviewerFunctions;
import edu.ucsd.cse135.gas.logic.support.Database;
import edu.ucsd.cse135.gas.resources.Constants;

public class GetApplicationsAction extends Action {

    public GetApplicationsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("applications", ReviewerFunctions.getApplications(request.getRemoteUser()));
        return mapping.findForward(Constants.SUCCESS);
    }
}
