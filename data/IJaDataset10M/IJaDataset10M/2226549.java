package educate.lcms.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lebah.portal.action.RequestUtil;
import org.apache.velocity.VelocityContext;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class AddModuleAction implements lebah.portal.action.ActionTemplate {

    public boolean doAction(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        HttpSession session = req.getSession();
        String content_id = RequestUtil.getParam(req, "this_content_id");
        context.put("content_id", content_id);
        String chapter_id = RequestUtil.getParam(req, "chapter_id");
        context.put("chapter_id", chapter_id);
        return true;
    }
}
