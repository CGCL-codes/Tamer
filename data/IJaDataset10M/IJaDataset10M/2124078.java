package vobs.webapp;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.xmldb.api.modules.*;
import vobs.datamodel.*;
import vobs.dbaccess.*;
import wdc.settings.*;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

public final class DesignAction extends Action {

    private Logger log = Logger.getLogger(DesignAction.class);

    private static String userDB = Settings.get("vo_meta.userProfilesResource");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User voUser = (User) session.getAttribute("voUser");
        VO virtObs = (VO) session.getAttribute("vobean");
        if (null == virtObs) {
            return (mapping.findForward("logon"));
        }
        if (voUser == null) {
            log.error("Session is missing or has expired for client from " + request.getRemoteAddr());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.session.nouser"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        if (!voUser.isAnonymous()) {
            if (request.getParameterMap().containsKey("size")) {
                String size = request.getParameter("size");
                virtObs.setFontSize(Integer.parseInt(size));
                updateProfileParam("size", size, voUser.getProfileName());
            }
            if (request.getParameterMap().containsKey("color")) {
                String color = request.getParameter("color");
                virtObs.setAppColor(color);
                updateProfileParam("color", color, voUser.getProfileName());
            }
            if (request.getParameterMap().containsKey("language")) {
                String language = request.getParameter("language");
                updateProfileParam("language", language, voUser.getProfileName());
            }
        } else {
            log.error("User from " + request.getRemoteAddr() + " is not authorize to use this page.");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.voUser.authorize"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        if (null != request.getSession().getAttribute("currentPage")) {
            return new ActionForward((String) request.getSession().getAttribute("currentPage"));
        }
        return (mapping.findForward("success"));
    }

    private void updateProfileParam(String paramName, String paramValue, String profileName) throws XMLDBException {
        Collection col = CollectionsManager.getCollection(userDB, true);
        String xquery = "<xupdate:modifications version='1.0' xmlns:xupdate='http://www.xmldb.org/xupdate'>" + "  <xupdate:update select=\"document('" + userDB + "/" + profileName + "')/USER_PROFILE/USER_DESIGN/" + paramName.toUpperCase() + "\">" + paramValue + "</xupdate:update>" + "</xupdate:modifications>";
        XUpdateQueryService updateService = (XUpdateQueryService) col.getService("XUpdateQueryService", "1.0");
        long res = updateService.update(xquery);
    }
}
