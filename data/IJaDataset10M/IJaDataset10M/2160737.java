package educate.sis.struct;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import lebah.db.Db;
import lebah.db.SQLRenderer;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SetupIntakeModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/struct/setup_intake.vm";
        String submit = getParam("command");
        Vector intakeList = getIntakeList();
        context.put("intakeList", intakeList);
        Template template = engine.getTemplate(template_name);
        return template;
    }

    private Vector getIntakeList() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("intake_id");
            r.add("intake_name");
            r.add("intake_begin");
            sql = r.getSQLSelect("intake");
            ResultSet rs = stmt.executeQuery(sql);
            Vector list = new Vector();
            while (rs.next()) {
                Hashtable h = new Hashtable();
                h.put("id", rs.getString("intake_id"));
                h.put("name", rs.getString("intake_name"));
                h.put("date_begin", rs.getString("intake_begin"));
                list.add(h);
            }
            return list;
        } finally {
            if (db != null) db.close();
        }
    }
}
