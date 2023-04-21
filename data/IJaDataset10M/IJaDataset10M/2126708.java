package logtrap.action;

import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import common.action.Action;
import common.action.ActionForward;
import innerbus.logtrap.common.*;

public class LogTrapStartAction implements Action {

    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String runProject = request.getParameter("selectedList");
        String[] projects = runProject.split("\\#");
        ProjectListXmlParser.getInstance().changeRun(stringToArray(projects), true);
        int kind = Integer.parseInt(request.getParameter("kind"));
        int flag = Integer.parseInt(request.getParameter("flag"));
        Map hmProject = ProjectListXmlParser.getInstance().parse(kind);
        request.setAttribute("project", hmProject);
        request.setAttribute("isReload", "true");
        request.setAttribute("isFiles", "true");
        request.setAttribute("isStart", request.getParameter("start"));
        ActionForward forward = new ActionForward();
        if (kind == ProjectHandler.SYSLOGD) {
            if (flag == 0) forward.setPath("syslogdForm.jsp"); else {
                request.setAttribute("max", 0 + "");
                forward.setPath("syslogdList.jsp");
            }
        } else if (kind == ProjectHandler.FTP) {
            if (flag == 0) forward.setPath("../lt/ftpForm.jsp"); else {
                request.setAttribute("max", 0 + "");
                forward.setPath("ftpList.jsp");
            }
        } else forward.setPath("leaForm.jsp");
        return forward;
    }

    public ArrayList stringToArray(String[] str) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return list;
    }
}
