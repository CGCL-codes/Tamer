package controllers.front.forum;

import controllers.BaseController;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import models.Course;
import models.User;
import persistence.dao.CourseDao;
import persistence.dao.UserDao;

@WebServlet(name = "ForumOverview", urlPatterns = { "/forum" })
public class ForumOverview extends BaseController {

    private UserDao userDao;

    private CourseDao courseDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initBase(request, response);
        userDao = new UserDao();
        User user = userDao.getUserById(userSession.getUserId());
        if (isLoggedIn()) {
            courseDao = new CourseDao();
            if (user.getClearance() == 4) {
                List<Course> courses = courseDao.getAllCourses();
                request.setAttribute("courses", courses);
            } else if (user.getClearance() > 1) {
                List<Course> courses = courseDao.getCoursesByTeacher(user);
                request.setAttribute("courses", courses);
            } else {
                List<Course> courses = courseDao.getCoursesByStudent(user);
                request.setAttribute("courses", courses);
            }
            loader.loadFrontContent("/forum/overview.jsp", "Forum");
        } else {
            loader.redirect("/home");
        }
    }
}
