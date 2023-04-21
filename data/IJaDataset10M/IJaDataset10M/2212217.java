package educate.sis.payment;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lebah.portal.action.RequestUtil;
import lebah.util.DateTool;
import org.apache.velocity.VelocityContext;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */
public class StudentInvAction implements lebah.portal.action.ActionTemplate_deprecated {

    public void doAction(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        StudentInvAction.doInit(req, res, context);
    }

    public static void doInit(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        HttpSession session = req.getSession();
        String student_id = !"".equals(RequestUtil.getParam(req, "student_id")) ? RequestUtil.getParam(req, "student_id") : "";
        context.put("student_id", student_id);
        Hashtable billDate = session.getAttribute("billDate") != null ? (Hashtable) session.getAttribute("billDate") : new Hashtable();
        Hashtable studentInfo = session.getAttribute("studentInfo") != null ? (Hashtable) session.getAttribute("studentInfo") : new Hashtable();
        Hashtable dateTime = DateTool.getCurrentDateTime();
        context.put("dateTime", dateTime);
    }
}
