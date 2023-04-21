package educate.lcms.modules;

import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class LaunchPageModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/lcms/launch_page.vm";
        String errorMsg = "";
        Template template = engine.getTemplate(template_name);
        return template;
    }
}
