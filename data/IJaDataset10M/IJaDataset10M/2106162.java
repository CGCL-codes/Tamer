package org.example.finance;

import org.example.CommonAction;
import org.example.ActionState;
import org.apache.struts.action.ActionForward;
import org.apache.log4j.Logger;
import net.wgen.op.logging.TraceKey;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: ProjectTimesheetAction.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public class ProjectTimesheetAction extends CommonAction {

    public ActionForward execute(ActionState actionState) throws Exception {
        Integer projectSid = actionState.getInteger("projectSid", new Integer(1));
        TraceKey traceKey = actionState.getTraceKey();
        ProjectTimesheetsOp timesheetsOp = new ProjectTimesheetsOp(traceKey);
        timesheetsOp.setProjectSid(projectSid);
        timesheetsOp.execute();
        Logger.getLogger(getClass()).debug(traceKey + " returned from Op, adding data to request");
        Map displayObj = new HashMap();
        displayObj.put("projectInfo", timesheetsOp.getProjectInfo());
        displayObj.put("timesheetData", timesheetsOp.getTimesheetResults());
        actionState.setOutParameter("display", displayObj);
        return actionState.forwardTo("success");
    }
}
