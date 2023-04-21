package fr.umlv.jee.hibou.web.accueil;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.opensymphony.xwork2.ActionSupport;
import fr.umlv.jee.hibou.web.session.UserSession;
import fr.umlv.jee.hibou.web.utils.XmlHelper;
import fr.umlv.jee.hibou.wsclient.HibouWS;
import fr.umlv.jee.hibou.wsclient.HibouWSService;
import fr.umlv.jee.hibou.wsclient.UserBean;

/**
 * this class manage users authentification
 * @author matt, alex, nak, micka
 *
 */
public class Authentification extends ActionSupport {

    private static final long serialVersionUID = 4826122538932817676L;

    private final HibouWS hibouWSPort = new HibouWSService().getHibouWSPort();

    private static final String LOGIN_SUCCESS = "login_success";

    private static final String TECHNICAL_ERROR = "technical_error";

    private static final String LOGOUT_SUCCESS = "logout_success";

    /**
	 * This method carries out a control of validation to authorize an authentification. 
	 * The tests carried out are: 
	 * - the existence of the account user 
	 * - the state of activity of the account 
	 * - validity of the password 
	 * If the successful authentification, the action login is called. 
	 * Otherwise an error message is turned over to the user. 
	 * 
	 */
    public String checkLogin() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("Authentification_checkLogin" + " " + username + " " + password);
            if (!hibouWSPort.accountExists(username)) {
                System.out.println("utilisateur " + username + " n'existe pas !!!");
                row.setAttribute("result", "username_error");
                doc.getDocumentElement().appendChild(row);
            } else if (!hibouWSPort.isAccountActivated(username)) {
                System.out.println("utilisateur " + username + " n'a pas �t� activ� !");
                row.setAttribute("result", "activation_error");
                doc.getDocumentElement().appendChild(row);
            } else if (!hibouWSPort.isPasswordCorrect(username, password)) {
                System.out.println("mauvais mot de passe pour l'utilisateur " + username);
                row.setAttribute("result", "password_error");
                doc.getDocumentElement().appendChild(row);
            } else {
                System.out.println("RESULT OK");
                row.setAttribute("result", "ok");
                doc.getDocumentElement().appendChild(row);
            }
            System.out.println(XmlHelper.toString(doc));
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
        } catch (Exception e) {
            row.setAttribute("result", "error");
            doc.getDocumentElement().appendChild(row);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            try {
                System.out.println(XmlHelper.toString(doc));
                response.getWriter().write(XmlHelper.toString(doc));
                response.flushBuffer();
                return null;
            } catch (IOException e1) {
                return TECHNICAL_ERROR;
            }
        }
        return null;
    }

    /**
	 * This method is called if the authentification successful. 
	 * The user context is load in session. 
	 * @return LOGIN_SUCCESS or TECHNICAL_ERROR if the recovery of the user data fail. 
	 */
    public String login() {
        try {
            System.out.println("Authentification_login" + " " + username + " " + password);
            UserBean userBean = hibouWSPort.getUserInformations(username);
            UserSession user = new UserSession(userBean);
            System.out.println("email " + userBean.getEmail());
            System.out.println("nickname " + userBean.getNickname());
            System.out.println("country " + userBean.getCountry());
            System.out.println("sex " + userBean.getSex());
            ServletActionContext.getRequest().getSession().setAttribute("navigationContext", user);
            return LOGIN_SUCCESS;
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
    }

    /**
	 * This method disconnect one user
	 * @return
	 * @throws Exception
	 */
    public String logout() throws Exception {
        ServletActionContext.getRequest().getSession().setAttribute("navigationContext", null);
        return LOGOUT_SUCCESS;
    }

    private String username;

    private String password;

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }
}
