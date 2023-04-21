package net.lukemurphey.nsia.web.views;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.lukemurphey.nsia.web.ViewFailedException;
import net.lukemurphey.nsia.web.templates.TemplateLoader;

public class DashboardPreLoginPanel {

    public static String getPanel(HttpServletRequest request, Map<String, Object> data) throws ViewFailedException {
        return TemplateLoader.renderToString("DashboardPreLogin.ftl", data);
    }
}
