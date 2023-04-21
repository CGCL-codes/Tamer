package org.acs.elated.ui;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acs.elated.app.CollabCollectionMgr;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * ManageCollaborativeCollectionAction.java
 */
public class ManageCollaborativeCollectionAction extends Action {

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Logger logger = (Logger) Logger.getInstance(org.acs.elated.ui.ManageCollaborativeCollectionAction.class);
        logger.debug("entering ManageCollaborativeCollectionAction");
        checkSession(httpServletRequest);
        ManageCollaborativeCollectionBean mCCBean = (ManageCollaborativeCollectionBean) actionForm;
        CollabCollectionMgr dataBean = (CollabCollectionMgr) httpServletRequest.getSession().getAttribute("mCCDBean");
        String collectionPID = dataBean.getCollectionPID();
        String collectionTitle = mCCBean.getCollectionName();
        dataBean.setCollectionName(collectionTitle);
        httpServletRequest.setAttribute("beingEdited", "true");
        try {
            logger.debug("Entering the manageCollaborativeCollectionAction");
            logger.info("collectionName : " + mCCBean.getCollectionName());
            Enumeration en = httpServletRequest.getParameterNames();
            String buttonClicked = "";
            while (en.hasMoreElements()) {
                String str = (String) en.nextElement();
                if (str.startsWith("btn")) {
                    buttonClicked = str;
                    break;
                }
            }
            if (buttonClicked.equalsIgnoreCase("btnAdminAdd")) {
                logger.info("addAdmin");
                String[] userSelected = mCCBean.getUserPool();
                dataBean.addAdmin(userSelected);
            } else if (buttonClicked.equalsIgnoreCase("btnAdminRemove")) {
                logger.info("removeAdmin");
                String[] userSelected = mCCBean.getAdmins();
                dataBean.removeAdmin(userSelected);
            } else if (buttonClicked.equalsIgnoreCase("btnContAdd")) {
                logger.info("addCon");
                String[] userSelected = mCCBean.getUserPool();
                dataBean.addContrib(userSelected);
            } else if (buttonClicked.equalsIgnoreCase("btnContRemove")) {
                logger.info("removeCon");
                String[] userSelected = mCCBean.getContributors();
                dataBean.removeContrib(userSelected);
            } else {
                logger.info("not a button");
            }
        } catch (NullPointerException ex) {
            logger.info("no user selected", ex);
            ActionErrors errors = new ActionErrors();
            errors.add("Wrong Button Clicked", new ActionMessage("update.mcc.nullpointer"));
            this.saveErrors(httpServletRequest, errors);
        }
        httpServletRequest.getSession().setAttribute("mCCDBean", dataBean);
        httpServletRequest.setAttribute("collectionPID", collectionPID);
        httpServletRequest.setAttribute("collectionTitle", collectionTitle);
        return actionMapping.findForward("update");
    }
}
