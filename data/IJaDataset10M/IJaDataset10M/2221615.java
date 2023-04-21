package educate.lcms.modules;

import java.util.Collection;
import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;
import educate.lcms.Course;
import educate.lcms.CourseData;
import educate.lcms.CourseDataFactory;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SelectCourseModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/lcms/select_course.vm";
        String errorMsg = "";
        String command = getParam("command");
        if ("addnew".equals(command)) {
            String course_id = getParam("course_id");
            String course_title = getParam("course_title");
            CourseData data = CourseDataFactory.get();
            if (data.add(new Course(course_id, course_title))) {
            }
        } else if ("delete".equals(command)) {
            String course_id = getParam("course_id");
            CourseData data = CourseDataFactory.get();
            data.delete(course_id);
        }
        CourseData db = CourseDataFactory.get();
        Collection courseList = db.getCourses();
        context.put("courses", courseList);
        Template template = engine.getTemplate(template_name);
        return template;
    }
}
