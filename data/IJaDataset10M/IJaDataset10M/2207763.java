package org.acs.elated.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acs.elated.commons.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * Adds a new datastream to an item
 */
public class AddDataStreamAction extends Action {

    static Logger logger = Logger.getLogger(AddDataStreamAction.class);

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("Entering AddDatastreamAction");
        checkSession(request);
        ActionErrors errors = new ActionErrors();
        AddDataStreamActionForm form = (AddDataStreamActionForm) actionForm;
        String itemPID = (String) form.getItemPID();
        String collectionPID = form.getCollectionPID();
        request.setAttribute("collectionPID", collectionPID);
        request.setAttribute("itemPID", itemPID);
        request.setAttribute("tabbedContext", form.getTabbedContext());
        try {
            if (!StringUtils.isEmpty(form.getUrl())) {
                String dsid = AddDatastreamHelper.addDatastreamFromUrl(request, form.getUrl(), form.getLabel(), itemPID);
            } else {
                String dsid = AddDatastreamHelper.addDatastreamFromFile(request, form.getUploadFile(), form.getLabel(), itemPID);
            }
        } catch (Exception e) {
            errors.add(e.getMessage(), new ActionMessage("add.datastream.failure", e.getMessage()));
            this.saveErrors(request, errors);
            return actionMapping.findForward("failure");
        }
        return actionMapping.findForward("success");
    }
}
