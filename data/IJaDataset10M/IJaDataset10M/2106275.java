package educate.sis.registration;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import lebah.db.DataHelper;
import lebah.db.SQLRenderer;
import org.apache.velocity.Template;

public class SISLogModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String submit = getParam("command");
        String template_name = prepareTemplate(submit, session);
        Template template = engine.getTemplate(template_name);
        return template;
    }

    String prepareTemplate(String submit, HttpSession session) throws Exception {
        String template_name = "vtl/sis/register/sis_log.vm";
        Vector logList = getLogList();
        context.put("logList", logList);
        return template_name;
    }

    Vector getLogList() throws Exception {
        return new DataHelper() {

            public String doSQL() {
                SQLRenderer r = new SQLRenderer();
                r.add("log_date");
                r.add("ip_address");
                r.add("username");
                r.add("description");
                return r.getSQLSelect("sis_log");
            }

            public Object createObject(ResultSet rs) throws Exception {
                Hashtable h = new Hashtable();
                h.put("log_date", rs.getDate("log_date"));
                h.put("ip_address", rs.getString("ip_address"));
                h.put("username", rs.getString("username"));
                h.put("description", rs.getString("description"));
                return h;
            }
        }.getObjectList();
    }
}
