package org.acs.elated.ui.itembinding;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acs.elated.app.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

/**
 * @author ACS Tech Center
 */
public class GetMechanismBindingsAction extends Action {

    private static Logger logger = (Logger) Logger.getLogger(GetMechanismBindingsAction.class);

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("entering execute");
        GetMechanismBindingsForm form = (GetMechanismBindingsForm) actionForm;
        ActionErrors errors = new ActionErrors();
        BindingMgr bindingMgr = new BindingMgr();
        AppMgr fedoraManager = (AppMgr) request.getSession().getAttribute("fedoraManager");
        request.setAttribute("collectionPID", form.getCollectionPID());
        request.setAttribute("itemPID", form.getItemPID());
        request.setAttribute("tabbedContext", form.getTabbedContext());
        try {
            logger.info("collectionPID is " + form.getCollectionPID());
            logger.info("definitionPID is " + form.getDefinitionPID());
            logger.info("mechanismPID is " + form.getMechanismPID());
            List bindingRules = bindingMgr.getBindingRules(form.getMechanismPID());
            List dStreamList = fedoraManager.getItemMgr().getItem(form.getItemPID()).getDatastreams();
            if (validateRule(bindingRules, dStreamList)) {
                request.setAttribute("definitionPID", form.getDefinitionPID());
                request.setAttribute("mechanismPID", form.getMechanismPID());
                request.setAttribute("bindingRules", bindingRules);
                logger.debug("exiting execute");
                return actionMapping.findForward("success");
            } else {
                errors.add("getBindingMechanismError", new ActionMessage("getBindingMechanism.validation.failure"));
                this.saveErrors(request, errors);
                return actionMapping.findForward("failure");
            }
        } catch (Exception e) {
            logger.error("error", e);
            errors.add("getBindingMechanismError", new ActionMessage("getBindingMechanism.failure"));
            this.saveErrors(request, errors);
            return actionMapping.findForward("failure");
        }
    }

    private boolean validateRule(List bindingRules, List dStreams) {
        logger.debug("validateRule(List bindingRules=" + bindingRules + ", List dStreams=" + dStreams + ") - start");
        boolean[] flag = new boolean[bindingRules.size()];
        for (int i = 0; i < bindingRules.size(); i++) {
            flag[i] = false;
            BindMap bMap = (BindMap) bindingRules.get(i);
            String mime = bMap.getMime().split("/")[1];
            if (!containsMime(dStreams, mime)) {
                logger.debug("validateRule() - end - return value=" + false);
                return false;
            }
        }
        logger.debug("validateRule() - end - return value=" + true);
        return true;
    }

    private boolean containsMime(List dStreams, String mime) {
        logger.debug("containsMime(List dStreams=" + dStreams + ", String mime=" + mime + ") - start");
        for (int i = 0; i < dStreams.size(); i++) {
            DStream ds = (DStream) dStreams.get(i);
            if (ds.getMimeType().indexOf(mime) != -1) {
                logger.debug("containsMime() - end - return value=" + true);
                return true;
            }
        }
        logger.debug("containsMime() - end - return value=" + false);
        return false;
    }
}
