package educate.sis.exam;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class GradingSchemeModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/exam/grading_scheme.vm";
        String submit = getParam("command");
        String scheme_id = getParam("scheme_id");
        if ("addScheme".equals(submit)) {
            template_name = "vtl/sis/exam/grading_scheme_edit.vm";
            String scheme_name = getParam("scheme_name");
            scheme_id = GradingSchemeData.addScheme(scheme_name);
            Hashtable info = GradingSchemeData.getSchemeInfo(scheme_id);
            context.put("schemeInfo", info);
            submit = "grade";
        } else if ("changeName".equals(submit)) {
            template_name = "vtl/sis/exam/grading_scheme_edit.vm";
            String scheme_name = getParam("scheme_name");
            GradingSchemeData.updateSchemeName(scheme_id, scheme_name);
            Hashtable info = GradingSchemeData.getSchemeInfo(scheme_id);
            context.put("schemeInfo", info);
            submit = "grade";
        } else if ("editScheme".equals(submit)) {
            submit = "grade";
        } else if ("deleteScheme".equals(submit)) {
            GradingSchemeData.deleteScheme(scheme_id);
        } else if ("addGrade".equals(submit)) {
            String achievement = getParam("achievement");
            String display = getParam("display");
            float high_mark = !"".equals(getParam("high_mark")) ? Float.parseFloat(getParam("high_mark")) : 0.0f;
            float low_mark = !"".equals(getParam("low_mark")) ? Float.parseFloat(getParam("low_mark")) : 0.0f;
            float point = !"".equals(getParam("point")) ? Float.parseFloat(getParam("point")) : 0.0f;
            String id = getParam("grade_id");
            Grade grade = new Grade();
            grade.id = id;
            grade.high = high_mark;
            grade.low = low_mark;
            grade.point = point;
            grade.display = display;
            grade.achievement = achievement;
            GradingSchemeData.addGrade(scheme_id, grade);
            submit = "grade";
        } else if ("deleteGrade".equals(submit)) {
            String grade_id = getParam("grade_id");
            GradingSchemeData.deleteGrade(grade_id);
            submit = "grade";
        }
        if ("grade".equals(submit)) {
            template_name = "vtl/sis/exam/grading_scheme_edit.vm";
            Hashtable info = GradingSchemeData.getSchemeInfo(scheme_id);
            context.put("schemeInfo", info);
        }
        Vector schemeList = GradingSchemeData.getSchemeList();
        context.put("schemeList", schemeList);
        Template template = engine.getTemplate(template_name);
        return template;
    }
}
