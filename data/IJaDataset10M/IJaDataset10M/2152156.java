package lebah.webmail;

import java.util.Hashtable;
import javax.servlet.http.HttpSession;
import lebah.portal.velocity.VTemplate;
import org.apache.velocity.Template;

public class WebmailInModule extends VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/iframe/webmail/setup_webmail.vm";
        String userid = (String) session.getAttribute("_portal_login");
        String submit = getParam("command");
        if ("".equals(submit)) submit = "logonWebmail";
        if ("saveUser".equals(submit)) {
            String username = getParam("username");
            String password = getParam("password");
            WebmailData.saveUserLogin(userid, username, password);
        } else if ("logonWebmail".equals(submit)) {
            Hashtable h = WebmailData.getUsernameAndPassword(userid);
            if (h != null) {
                context.put("username", (String) h.get("username"));
                context.put("password", (String) h.get("password"));
                template_name = "vtl/iframe/webmail/logon_webmail.vm";
            } else {
                template_name = "vtl/iframe/webmail/setup_webmail.vm";
            }
        }
        Template template = engine.getTemplate(template_name);
        return template;
    }
}
