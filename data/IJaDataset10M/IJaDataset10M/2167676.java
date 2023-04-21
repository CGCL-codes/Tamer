package salto.test.fwk.mvc.ajax.refresh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import salto.fwk.mvc.ajax.util.AjaxUtil;
import salto.test.fwk.mvc.Constants;

public class OpenPopupCommand extends RefreshCommand {

    private String id;

    private boolean modal;

    private String left;

    private String top;

    public OpenPopupCommand() {
    }

    public void setContext(HttpServletRequest request) {
        id = ("".equals(request.getParameter("popupId"))) ? null : request.getParameter("popupId");
        modal = "Modale".equalsIgnoreCase(request.getParameter("typePopup"));
        left = request.getParameter("left");
        top = request.getParameter("top");
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        if (left != null && top != null) {
            AjaxUtil.addPopup(request, response, "Test popup " + id, null, Constants.TEST_JSP_PATH + "/refresh/testRefresh.jsp", 500, 300, Integer.parseInt(left), Integer.parseInt(top), modal, id, Constants.TEST_MODULE_NAME + "/ExecuteRefreshCommands.do", null);
        } else {
            AjaxUtil.addPopup(request, response, "Test popup " + id, null, Constants.TEST_JSP_PATH + "/refresh/testRefresh.jsp", 500, 300, modal, id, Constants.TEST_MODULE_NAME + "/ExecuteRefreshCommands.do", null);
        }
        if (id != null) RefreshUtils.registerPopup(request, id);
    }

    public String toString() {
        return "Open " + (modal ? "modal" : "modeless") + " popup " + id;
    }
}
