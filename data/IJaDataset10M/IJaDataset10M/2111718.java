package fr.umlv.jee.hibou.web.administrator;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.opensymphony.xwork2.ActionSupport;
import fr.umlv.jee.hibou.web.session.UserSession;
import fr.umlv.jee.hibou.web.utils.XmlHelper;
import fr.umlv.jee.hibou.wsclient.HibouWS;
import fr.umlv.jee.hibou.wsclient.HibouWSService;
import fr.umlv.jee.hibou.wsclient.ProjectBean;
import fr.umlv.jee.hibou.wsclient.ProjectListBean;

/**
 * This class provides actions for administrator page
 * @author micka, alex, nak, matt
 *
 */
public class AdministratorPage extends ActionSupport {

    private static final long serialVersionUID = 8425184058303151839L;

    private final HibouWS hibouWSPort = new HibouWSService().getHibouWSPort();

    private List<ProjectBean> projectsList;

    private String rssTitle;

    private String rssContent;

    private String projectName;

    private int projectIndex;

    private String addRemoveAdminName;

    private String username;

    private String removeUserName;

    private String removeProjectName;

    private static final String RSS_SUCCESS = "rss_success";

    private static final String RSS_ERROR = "rss_error";

    private static final String TECHNICAL_ERROR = "technical_error";

    private static final String ADMIN_ERROR = "admin_error";

    /**
	 * This method init the page
	 */
    @Override
    public String execute() {
        try {
            UserSession usr = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
            username = usr.getUser().getEmail();
            if (!hibouWSPort.isAdmin(username)) {
                return ADMIN_ERROR;
            }
            ProjectListBean listBean = hibouWSPort.getUnvalidatedProjects();
            projectsList = listBean.getList();
            return SUCCESS;
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
    }

    /**
	 * This ajax method adds a new administrator. 
	 * @return the result of the action success or error
	 */
    public String addAdmin() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("addAdmin " + addRemoveAdminName);
            UserSession usr = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
            username = usr.getUser().getEmail();
            if (username.equals(addRemoveAdminName)) {
                System.out.println("impossible d'entrer votre propre nom");
                row.setAttribute("result", "yourself");
                doc.getDocumentElement().appendChild(row);
            } else if (hibouWSPort.accountExists(addRemoveAdminName)) {
                if (!hibouWSPort.isAccountActivated(addRemoveAdminName)) {
                    System.out.println(addRemoveAdminName + " n'a pas activ� son compte");
                    row.setAttribute("result", "not_activated");
                    doc.getDocumentElement().appendChild(row);
                } else if (hibouWSPort.isAdmin(addRemoveAdminName)) {
                    System.out.println(addRemoveAdminName + " est deja un administrateur");
                    row.setAttribute("result", "is_admin");
                    doc.getDocumentElement().appendChild(row);
                } else {
                    System.out.println(addRemoveAdminName + " n'est pas un administrateur");
                    if (hibouWSPort.changeAdminStatus(addRemoveAdminName, true)) {
                        System.out.println(addRemoveAdminName + " est devenu un administrateur");
                        row.setAttribute("result", "ok");
                        doc.getDocumentElement().appendChild(row);
                    } else {
                        System.out.println("probleme lors de l'ajout de " + addRemoveAdminName);
                        row.setAttribute("result", "error");
                        doc.getDocumentElement().appendChild(row);
                    }
                }
            } else {
                System.out.println(addRemoveAdminName + " n'existe pas !");
                row.setAttribute("result", "unknown_user");
                doc.getDocumentElement().appendChild(row);
            }
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            row.setAttribute("result", "technical_error");
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            try {
                response.getWriter().write(XmlHelper.toString(doc));
                response.flushBuffer();
                return null;
            } catch (IOException e1) {
                return TECHNICAL_ERROR;
            }
        }
    }

    /**
	 * This ajax method remove an administrator.
	 * If there is only one administrator, that could not be removed . 
	 * @return the result of the action success or error
	 */
    public String removeAdmin() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("removeAdmin " + addRemoveAdminName);
            UserSession usr = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
            username = usr.getUser().getEmail();
            if (username.equals(addRemoveAdminName)) {
                System.out.println("impossible d'entrer votre propre nom");
                row.setAttribute("result", "yourself");
                doc.getDocumentElement().appendChild(row);
            } else if (hibouWSPort.accountExists(addRemoveAdminName)) {
                if (hibouWSPort.isAdmin(addRemoveAdminName)) {
                    System.out.println(addRemoveAdminName + " est un administrateur");
                    if (hibouWSPort.changeAdminStatus(addRemoveAdminName, false)) {
                        System.out.println(addRemoveAdminName + " n'est plus administrateur");
                        row.setAttribute("result", "ok");
                        doc.getDocumentElement().appendChild(row);
                    } else {
                        System.out.println("probleme lors de la suppression de " + addRemoveAdminName);
                        row.setAttribute("result", "error");
                        doc.getDocumentElement().appendChild(row);
                    }
                } else {
                    System.out.println(addRemoveAdminName + " n'est pas un administrateur");
                    row.setAttribute("result", "is_not_admin");
                    doc.getDocumentElement().appendChild(row);
                }
            } else {
                System.out.println(addRemoveAdminName + " n'existe pas !");
                row.setAttribute("result", "unknown_user");
                doc.getDocumentElement().appendChild(row);
            }
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            row.setAttribute("result", "technical_error");
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            try {
                response.getWriter().write(XmlHelper.toString(doc));
                response.flushBuffer();
                return null;
            } catch (IOException e1) {
                return TECHNICAL_ERROR;
            }
        }
    }

    /**
	 * This ajax method delete one user
	 * @return success or error
	 */
    public String deleteUser() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("deleteUser " + removeUserName);
            UserSession usr = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
            username = usr.getUser().getEmail();
            if (username.equals(removeUserName)) {
                System.out.println("impossible d'entrer votre propre nom");
                row.setAttribute("result", "yourself");
                doc.getDocumentElement().appendChild(row);
            } else if (hibouWSPort.accountExists(removeUserName)) {
                if (hibouWSPort.dropAccount(removeUserName)) {
                    System.out.println(removeUserName + " a �t� supprim�");
                    row.setAttribute("result", "ok");
                    doc.getDocumentElement().appendChild(row);
                } else {
                    System.out.println("probleme lors de la suppression de " + removeUserName);
                    row.setAttribute("result", "error");
                    doc.getDocumentElement().appendChild(row);
                }
            } else {
                System.out.println(removeUserName + " n'existe pas !");
                row.setAttribute("result", "unknown_user");
                doc.getDocumentElement().appendChild(row);
            }
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            row.setAttribute("result", "technical_error");
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            try {
                response.getWriter().write(XmlHelper.toString(doc));
                response.flushBuffer();
                return null;
            } catch (IOException e1) {
                return TECHNICAL_ERROR;
            }
        }
    }

    /**
	 * This ajax method remove one project
	 * @return success or error
	 */
    public String deleteProject() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("deleteProject " + removeProjectName);
            if (hibouWSPort.projectExists(removeProjectName)) {
                if (hibouWSPort.setProjectAsDeleted(removeProjectName)) {
                    System.out.println(removeProjectName + " est programm� pour suppression");
                    row.setAttribute("result", "ok");
                    doc.getDocumentElement().appendChild(row);
                } else {
                    System.out.println("probleme lors de la suppression de " + removeProjectName);
                    row.setAttribute("result", "error");
                    doc.getDocumentElement().appendChild(row);
                }
            } else {
                System.out.println(removeProjectName + " n'existe pas !");
                row.setAttribute("result", "unknown_project");
                doc.getDocumentElement().appendChild(row);
            }
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            row.setAttribute("result", "technical_error");
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            try {
                response.getWriter().write(XmlHelper.toString(doc));
                response.flushBuffer();
                return null;
            } catch (IOException e1) {
                return TECHNICAL_ERROR;
            }
        }
    }

    /**
	 * This ajax method validate one project create by a user
	 * @return sucess or error
	 */
    public String validateProject() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("Valider " + projectName + "...");
            if (hibouWSPort.validateProject(projectName) == false) {
                row.setAttribute("result", "error");
            }
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            row.setAttribute("result", "technical_error");
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            try {
                response.getWriter().write(XmlHelper.toString(doc));
                response.flushBuffer();
                return null;
            } catch (IOException e1) {
                return TECHNICAL_ERROR;
            }
        }
    }

    /**
	 * This ajax method reject one project create by a user
	 * @return success or error
	 */
    public String rejectProject() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("Rejeter " + projectName + "...");
            if (hibouWSPort.deleteProject(projectName) == false) {
                row.setAttribute("result", "error");
            }
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            row.setAttribute("result", "technical_error");
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            try {
                response.getWriter().write(XmlHelper.toString(doc));
                response.flushBuffer();
                return null;
            } catch (IOException e1) {
                return TECHNICAL_ERROR;
            }
        }
    }

    /**
	 * Action for rss news editing
	 * @return Returns the result as a string
	 */
    public String RSSNews() {
        try {
            long date = GregorianCalendar.getInstance().getTimeInMillis();
            if (hibouWSPort.addRSSItem(rssTitle, rssContent, date) == false) {
                return RSS_ERROR;
            }
            return RSS_SUCCESS;
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
    }

    public String getAddRemoveAdminName() {
        return addRemoveAdminName;
    }

    public void setAddRemoveAdminName(String addRemoveAdminName) {
        this.addRemoveAdminName = addRemoveAdminName;
    }

    /**
	 * @return the projectIndex
	 */
    public int getProjectIndex() {
        return projectIndex;
    }

    /**
	 * @param projectIndex the projectIndex to set
	 */
    public void setProjectIndex(int projectIndex) {
        this.projectIndex = projectIndex;
    }

    /**
	 * @return the projectName
	 */
    public String getProjectName() {
        return projectName;
    }

    /**
	 * @param projectName the projectName to set
	 */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
	 * @param projectsList the projectsList to set
	 */
    public void setProjectsList(List<ProjectBean> projectsList) {
        this.projectsList = projectsList;
    }

    /**
	 * @return the userProjectsList
	 */
    public List<ProjectBean> getProjectsList() {
        return projectsList;
    }

    /**
	 * @return the rssContent
	 */
    public String getRssContent() {
        return rssContent;
    }

    /**
	 * @param rssContent the rssContent to set
	 */
    public void setRssContent(String rssContent) {
        this.rssContent = rssContent;
    }

    /**
	 * @return the rssTitle
	 */
    public String getRssTitle() {
        return rssTitle;
    }

    /**
	 * @param rssTitle the rssTitle to set
	 */
    public void setRssTitle(String rssTitle) {
        this.rssTitle = rssTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return the removeProjectName
	 */
    public String getRemoveProjectName() {
        return removeProjectName;
    }

    /**
	 * @param removeProjectName the removeProjectName to set
	 */
    public void setRemoveProjectName(String removeProjectName) {
        this.removeProjectName = removeProjectName;
    }

    /**
	 * @return the removeUserName
	 */
    public String getRemoveUserName() {
        return removeUserName;
    }

    /**
	 * @param removeUserName the removeUserName to set
	 */
    public void setRemoveUserName(String removeUserName) {
        this.removeUserName = removeUserName;
    }
}
