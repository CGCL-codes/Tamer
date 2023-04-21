package mecca.lcms.modules;

import java.util.Collection;
import javax.servlet.http.HttpSession;
import mecca.lcms.CourseData;
import mecca.lcms.CourseDataFactory;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class CourseModule extends mecca.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/lcms/course.vm";
        String errorMsg = "";
        CourseData db = CourseDataFactory.get();
        Collection courseList = db.getCourses();
        context.put("courseList", courseList);
        Template template = engine.getTemplate(template_name);
        return template;
    }
}
