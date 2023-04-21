package whiteboard;

import java.util.Map;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.util.ServletContextAware;
import whiteboard.course.Course;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * This action remembers what document the user selected and what action he
 * wants to perform. For example, he way want to delete document "x".
 * 
 * We store or "remember" these user choices by storing the values in the
 * selected course (the current course page the user is on).
 * 
 * NOTE: Deletion is handled here because there is no need to go to a form.
 * 
 * @author John
 * 
 */
public class RememberDocumentAction extends ActionSupport implements ServletContextAware {

    private ServletContext servletContext;

    private String documentAction;

    private String whatDocument;

    private HomePage homePage;

    private String p;

    public String execute() {
        DataSource dbcp = (DataSource) servletContext.getAttribute("dbpool");
        homePage = getHomePage();
        String action = this.getDocumentAction();
        String whatDocument = getWhatDocument();
        Course selectedCourse = homePage.getSelectedCourse();
        selectedCourse.setAction(action);
        selectedCourse.setWhatDocument(whatDocument);
        String typeOfDoc = DocumentAction.findTypeOfDoc(action);
        selectedCourse.setSelectedDocType(typeOfDoc);
        if (action.compareTo("DeleteHomework") == 0) {
            selectedCourse.removeHomework(whatDocument);
            typeOfDoc = DocumentAction.findTypeOfDoc(action);
            selectedCourse.removeDocFromDB(typeOfDoc, whatDocument, dbcp);
            return NONE;
        }
        if (action.compareTo("DeleteNote") == 0) {
            selectedCourse.removeNote(whatDocument);
            typeOfDoc = DocumentAction.findTypeOfDoc(action);
            selectedCourse.removeDocFromDB(typeOfDoc, whatDocument, dbcp);
            return NONE;
        }
        if (action.compareTo("DeleteAnnouncement") == 0) {
            selectedCourse.removeAnnouncement(whatDocument);
            typeOfDoc = DocumentAction.findTypeOfDoc(action);
            selectedCourse.removeDocFromDB(typeOfDoc, whatDocument, dbcp);
            return NONE;
        }
        if (action.startsWith("View")) {
            return INPUT;
        }
        return SUCCESS;
    }

    private HomePage getHomePage() {
        Map<String, Object> attibutes = ActionContext.getContext().getSession();
        return ((HomePage) attibutes.get("homePage"));
    }

    public void setServletContext(ServletContext arg0) {
        this.servletContext = arg0;
    }

    public void validate() {
        homePage = getHomePage();
        if (homePage == null) System.out.println("homepage is null");
    }

    public void setDocumentAction(String documentAction) {
        this.documentAction = documentAction;
    }

    public String getDocumentAction() {
        return documentAction;
    }

    public void setWhatDocument(String whatDocument) {
        this.whatDocument = whatDocument;
    }

    public String getWhatDocument() {
        return whatDocument;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getP() {
        return p;
    }
}
