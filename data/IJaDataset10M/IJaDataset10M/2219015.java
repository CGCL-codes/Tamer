package whiteboard.course;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.util.ServletContextAware;
import whiteboard.HomePage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * Action to delete course from DB and homepage instance.
 * 
 * @author John
 * 
 */
public class DeleteCourse extends ActionSupport implements ServletContextAware {

    private ServletContext servletContext;

    private String deleteCourse;

    private HomePage homePage;

    public String execute() {
        homePage = getHomePage();
        String courseToBeDeleted = getDeleteCourse();
        if (homePage.getAccountType().compareTo("administrator") == 0) {
            DataSource dbcp = (DataSource) servletContext.getAttribute("dbpool");
            removeCourseFromDB(courseToBeDeleted, homePage.getUser(), dbcp);
            if (homePage.removeCourse(courseToBeDeleted)) {
                return SUCCESS;
            }
        }
        validate();
        return INPUT;
    }

    /**
	 * Delete the entry of the course with courseid courseid, who is taught by
	 * adminLogin, and all related info: notes, assignments, annoucements,
	 * enrolled students, etc...
	 * 
	 * @param courseid
	 * @param user
	 */
    public static boolean removeCourseFromDB(String courseid, String adminLogin, DataSource dbcp) {
        Connection con = null;
        boolean wasError = false;
        try {
            con = dbcp.getConnection();
            PreparedStatement prepStatement;
            String sql = "DELETE FROM enrolled WHERE courseid = ? AND admin = ?";
            prepStatement = con.prepareStatement(sql);
            prepStatement.setString(1, courseid);
            prepStatement.setString(2, adminLogin);
            prepStatement.executeUpdate();
            sql = "DELETE FROM courses WHERE courseid = ? AND admin = ?";
            prepStatement = con.prepareStatement(sql);
            prepStatement.setString(1, courseid);
            prepStatement.setString(2, adminLogin);
            prepStatement.executeUpdate();
            prepStatement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            wasError = true;
        } finally {
            if (con != null) try {
                con.close();
            } catch (Exception e) {
            }
        }
        return wasError;
    }

    private HomePage getHomePage() {
        Map<String, Object> attibutes = ActionContext.getContext().getSession();
        return ((HomePage) attibutes.get("homePage"));
    }

    public void setServletContext(ServletContext arg0) {
        this.servletContext = arg0;
    }

    public void setDeleteCourse(String deleteCourse) {
        this.deleteCourse = deleteCourse;
    }

    public String getDeleteCourse() {
        return deleteCourse;
    }

    public void validate() {
        homePage = getHomePage();
        if (homePage == null) System.out.println("homepage is null");
        if (homePage.getAccountType().compareTo("administrator") != 0) {
            System.out.println("homepage <><><>");
            addFieldError("deleteCourse", getText("You cannot perform that because you are not admin."));
        }
    }
}
