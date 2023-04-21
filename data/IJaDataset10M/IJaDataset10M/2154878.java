package com.plus.subtrack.index;

import com.plus.subtrack.dataobjects.MarketSubDate;
import com.plus.subtrack.dataobjects.SubmissionState;
import com.plus.subtrack.dataobjects.ToDoView;
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
 * Time: 18:47:59
 */
public class IndexAction extends SessionAction {

    protected static Logger _logger = Logger.getLogger(IndexAction.class.getName());

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model, SubTrackSession subTrackSession) {
        Collection<ToDoView> toDoViews = model.getToDoViews();
        httpServletRequest.setAttribute("ToDoList", toDoViews);
        Collection<SubmissionState> submissionStates = model.getCurrentQueries();
        httpServletRequest.setAttribute("submissionstates", submissionStates);
        Collection<MarketSubDate> marketSubDates = model.getChangingSubDates();
        httpServletRequest.setAttribute("marketSubDates", marketSubDates);
        return null;
    }
}
