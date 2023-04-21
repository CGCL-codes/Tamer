package cn.ac.ntarl.flexinvoke;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import cn.ac.ntarl.umt.actions.group.GroupFacade;
import cn.ac.ntarl.umt.actions.positions.PositionFacade;
import cn.ac.ntarl.umt.database.NotExist;
import cn.ac.ntarl.umt.security.Sessions;
import cn.ac.ntarl.umt.user.Group;
import cn.ac.ntarl.umt.utils.I18nUtil;

public class UpdatePositionDescServlet extends CommonInvokeServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8012558827236954199L;

    private static Logger log;

    static {
        log = Logger.getLogger(AddUserToGroupByNamesServlet.class);
    }

    protected void dealInvoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        I18nUtil iu = new I18nUtil("main.flex", I18nUtil.getFlexLocale(request));
        String groupName = clean(request.getParameter("groupName"));
        String posName = clean(request.getParameter("posName"));
        String desc = request.getParameter("description");
        if (groupName.equals("")) {
            noReason(response);
            return;
        }
        if (desc == null) {
            noReason(response);
            return;
        }
        try {
            Group g = GroupFacade.getGroup(groupName);
            if (g == null || (g.getGroupID() <= 0)) {
                fail(response, iu.getString("UpdateGroupDescServlet.q4"));
                return;
            }
            int iGroupID = g.getGroupID();
            if (!ignorePermission) {
                if (!(Sessions.IsCurrentPosition(request, 1, "�����Ա")) && !(Sessions.IsCurrentPosition(request, iGroupID, "�����Ա"))) {
                    noPermission(response);
                    return;
                }
            }
            PositionFacade.updatePositionLabel(g, posName, desc);
            succ(response);
        } catch (NotExist e) {
            fail(response, "at least one child group not exist");
            return;
        } catch (Exception e) {
            log.error("add  users  to group" + groupName + "  error with reason:" + e.getMessage());
            noReason(response);
            return;
        }
    }
}
