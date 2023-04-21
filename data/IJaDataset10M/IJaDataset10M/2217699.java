package com.plus.subtrack.work;

import com.plus.subtrack.model.Model;
import com.plus.subtrack.session.SessionAction;
import com.plus.subtrack.session.SubTrackSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * User: Mark Elam
 * Date: 12-Oct-2005
 * Time: 21:45:42
 */
public class WorkDetailsAction extends SessionAction {

    protected static Logger _logger = Logger.getLogger(WorkDetailsAction.class.getName());

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model, SubTrackSession subTrackSession) {
        Collection genres = model.getGenres();
        httpServletRequest.setAttribute("Genres", genres);
        Collection sizes = model.getSizes();
        httpServletRequest.setAttribute("Sizes", sizes);
        Integer id = null;
        String strId = httpServletRequest.getParameter("WorkId");
        if (strId == null) {
            id = subTrackSession.getCurrentWorkId();
        } else {
            id = new Integer(Integer.parseInt(strId));
        }
        _logger.debug("Work Id is:" + strId);
        subTrackSession.setCurrentWorkId(id);
        actionForm = model.getWorkActionForm(id);
        httpServletRequest.setAttribute(actionMapping.getAttribute(), actionForm);
        return null;
    }
}
