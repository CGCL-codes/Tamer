package lebah.app;

import java.util.Vector;
import javax.servlet.http.HttpSession;
import lebah.portal.velocity.VTemplate;
import org.apache.velocity.Template;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class RoleModule extends VTemplate {

    private String targetPage;

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        RoleProcessor processor = new RoleProcessor();
        String action = request.getParameter("form_action");
        if ((action == null) || (action.equals(""))) action = "none";
        if (action.equals("none")) {
            Vector list = processor.getRoles();
            context.put("roleList", list);
            targetPage = "vtl/admin/role.vm";
        } else if (action.equals("add_role")) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            processor.addRole(name, description);
            Vector list = processor.getRoles();
            context.put("roleList", list);
            targetPage = "vtl/admin/role.vm";
        } else if (action.equals("update_role")) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String oldName = request.getParameter("old_name");
            processor.updateRole(oldName, name, description);
            Vector list = processor.getRoles();
            context.put("roleList", list);
            targetPage = "vtl/admin/role.vm";
        } else if (action.equals("delete_role")) {
            String name = request.getParameter("old_name");
            processor.deleteRole(name);
            Vector list = processor.getRoles();
            context.put("roleList", list);
            targetPage = "vtl/admin/role.vm";
        }
        Template template = engine.getTemplate(targetPage);
        return template;
    }
}
