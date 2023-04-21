package mecca.sis.payment;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mecca.portal.action.RequestUtil;
import mecca.sis.registration.SessionData;
import mecca.sis.struct.ProgramData;
import org.apache.velocity.VelocityContext;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */
public class FeeStructureAction implements mecca.portal.action.ActionTemplate {

    public boolean doAction(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        GeneralRequest.doInit(req, res, context);
        Vector programList = ProgramData.getList();
        context.put("programList", programList);
        String program_code = RequestUtil.getParam(req, "program_list");
        context.put("program_code", program_code);
        String programCode = RequestUtil.getParam(req, "program_list");
        if (!"".equals(programCode)) {
            Vector schemes = ProgramData.getPeriodStructureList2(programCode);
            context.put("periodSchemes", schemes);
            if (schemes.size() == 1) {
                String period_scheme = RequestUtil.getParam(req, "period_scheme");
                if ("".equals(period_scheme)) {
                    Hashtable h = (Hashtable) schemes.elementAt(0);
                    period_scheme = (String) h.get("period_scheme");
                }
                context.put("period_scheme", period_scheme);
                Vector intakeList = SessionData.getIntakeBatch(period_scheme);
                context.put("intakeList", intakeList);
            }
        }
        return true;
    }
}
