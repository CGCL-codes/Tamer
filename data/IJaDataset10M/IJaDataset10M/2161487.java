package aportal.action.cataloguing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.ejb.*;
import java.io.*;
import java.net.*;
import javax.naming.*;

/**
 *
 * @author  Administrator
 */
public class FederatedSearchResultsAction extends Action {

    /** Creates a new instance of FederatedSearchResultsAction */
    public FederatedSearchResultsAction() {
    }

    public org.apache.struts.action.ActionForward execute(org.apache.struts.action.ActionMapping actionMapping, org.apache.struts.action.ActionForm actionForm, javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) throws java.lang.Exception {
        aportal.form.cataloguing.FederatedSearchResultsForm form = (aportal.form.cataloguing.FederatedSearchResultsForm) actionForm;
        java.util.Vector vec = (java.util.Vector) httpServletRequest.getSession(true).getAttribute("FEDERATED_SEARCH_RESULTS");
        System.out.println("vec size: " + vec.size());
        form.setFederatedSearchResults(vec);
        return actionMapping.getInputForward();
    }
}
