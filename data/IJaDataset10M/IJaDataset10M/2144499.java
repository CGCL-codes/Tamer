package leesoft.hub.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import action.Logic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import leesoft.data.databeans.*;
import java.util.logging.Logger;

public class BingliAnalyzerAction implements IAction {

    public Map process(SessionContext ctx) {
        Logic logic = new Logic();
        String start_date = ctx.getParameterString("start_date");
        String end_date = ctx.getParameterString("end_date");
        String selector1 = ctx.getParameterString("selector1");
        String selector2 = ctx.getParameterString("selector2");
        String selector3 = ctx.getParameterString("selector3");
        String selector4 = ctx.getParameterString("selector4");
        if (start_date.equals("")) start_date = null;
        if (end_date.equals("")) end_date = null;
        Logger.getAnonymousLogger().info("start_date:" + start_date);
        Logger.getAnonymousLogger().info("end_date:" + end_date);
        ArrayList resultList = logic.report1(start_date, end_date, selector1, selector2, selector3, selector4);
        HashMap<String, List> returnMap = new HashMap<String, List>();
        returnMap.put("items", resultList);
        return returnMap;
    }

    public void setRequest(HttpServletRequest request) {
    }

    public void setResponse(HttpServletResponse response) {
    }
}
