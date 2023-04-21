package fr.umlv.jee.hibou.web.personnalpage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ListEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.opensymphony.xwork2.ActionSupport;
import fr.umlv.jee.hibou.web.session.UserSession;
import fr.umlv.jee.hibou.web.utils.XmlHelper;
import fr.umlv.jee.hibou.wsclient.HibouWS;
import fr.umlv.jee.hibou.wsclient.HibouWSService;

/**
 * This class makes it possible the user to modify his personal information 
 * @author micka, matt, nak, alex
 *
 */
public class PreferencesPage extends ActionSupport {

    private static final long serialVersionUID = -4803338009108899249L;

    private static final String VALIDATION_SUCCESS = "validation_success";

    private static final String TECHNICAL_ERROR = "technical_error";

    private static final List<String> sexList = new ArrayList<String>();

    static {
        sexList.add("M");
        sexList.add("F");
    }

    private final List<ListEntry> countryList = new ArrayList<ListEntry>();

    private final HibouWS hibouWSPort = new HibouWSService().getHibouWSPort();

    private String username;

    private String oldNickname;

    private String newNickname;

    private String sex;

    private String newSex;

    private String country;

    private String newCountry;

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;

    /**
	 * Init the page
	 */
    @Override
    public String execute() throws Exception {
        init();
        UserSession user = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
        username = user.getUser().getEmail();
        newNickname = oldNickname = user.getUser().getNickname();
        return SUCCESS;
    }

    private void init() {
        countryList.add(new ListEntry("france", getText("france"), false));
        countryList.add(new ListEntry("usa", getText("usa"), false));
        countryList.add(new ListEntry("greatBritain", getText("greatBritain"), false));
        countryList.add(new ListEntry("spain", getText("spain"), false));
        countryList.add(new ListEntry("germany", getText("germany"), false));
        countryList.add(new ListEntry("switzerland", getText("switzerland"), false));
        countryList.add(new ListEntry("italy", getText("italy"), false));
        countryList.add(new ListEntry("belgium", getText("belgium"), false));
        countryList.add(new ListEntry("holland", getText("holland"), false));
        countryList.add(new ListEntry("japan", getText("japan"), false));
        countryList.add(new ListEntry("russia", getText("russia"), false));
        countryList.add(new ListEntry("china", getText("china"), false));
        countryList.add(new ListEntry("other", getText("other"), false));
    }

    /**
	 * This method save the modification in database
	 * @return success or error
	 */
    public String changeInformations() {
        try {
            UserSession user = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
            username = user.getUser().getEmail();
            System.out.println("APPEL PREFERENCE CHANGE INFO");
            System.out.println("username : " + username);
            System.out.println("nouveau pseudo : " + newNickname);
            System.out.println("nouveau sexe : " + newSex);
            System.out.println("nouveau pays : " + newCountry);
            if (hibouWSPort.changeInformations(username, newNickname, newSex, newCountry) == false) {
                System.out.println("change info error");
                return TECHNICAL_ERROR;
            }
            System.out.println("change info ok");
            user.getUser().setNickname(newNickname);
            user.getUser().setSex(newSex);
            user.getUser().setCountry(newCountry);
            return VALIDATION_SUCCESS;
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
    }

    /**
	 * This ajax method check if the old password is correct or not.
	 * If the old password is incorrect, method return error success otherwise.
	 * @return success or error
	 */
    public String checkPassword() {
        Document doc = XmlHelper.newDocument("ROWS");
        Element row = doc.createElement("ROW");
        try {
            System.out.println("PREFERENCES PAGE --> checkPassword()");
            UserSession user = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
            username = user.getUser().getEmail();
            System.out.println("username : " + username);
            System.out.println("old password : " + oldPassword);
            if (!hibouWSPort.isPasswordCorrect(username, oldPassword)) {
                System.out.println("mauvais mot de passe !!");
                row.setAttribute("result", "password_error");
                doc.getDocumentElement().appendChild(row);
            } else {
                row.setAttribute("result", "ok");
                doc.getDocumentElement().appendChild(row);
            }
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/xml");
            response.getWriter().write(XmlHelper.toString(doc));
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            row.setAttribute("result", "error");
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
	 * This method save the new password in the database
	 * @return success or error
	 */
    public String changePassword() {
        try {
            UserSession user = (UserSession) ServletActionContext.getRequest().getSession().getAttribute("navigationContext");
            username = user.getUser().getEmail();
            System.out.println("APPEL PREFERENCE CHANGE PASSWORD");
            System.out.println("username : " + username);
            System.out.println("old password : " + oldPassword);
            System.out.println("new password : " + newPassword);
            System.out.println("confirm new password : " + confirmNewPassword);
            if (hibouWSPort.changePassword(username, newPassword) == false) {
                System.out.println("change password error");
                return TECHNICAL_ERROR;
            }
            System.out.println("change password ok");
            return VALIDATION_SUCCESS;
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
    }

    /**
	 * 
	 * @return the country list
	 */
    public List<ListEntry> getCountryList() {
        return countryList;
    }

    /**
	 * 
	 * @return the sex list
	 */
    public List<String> getSexList() {
        return sexList;
    }

    /**
	 * @return the country
	 */
    public String getCountry() {
        return country;
    }

    /**
	 * @param country the country to set
	 */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
	 * @return the newCountry
	 */
    public String getNewCountry() {
        return newCountry;
    }

    /**
	 * @param newCountry the newCountry to set
	 */
    public void setNewCountry(String newCountry) {
        this.newCountry = newCountry;
    }

    /**
	 * @return the newNickname
	 */
    public String getNewNickname() {
        return newNickname;
    }

    /**
	 * @param newNickname the newNickname to set
	 */
    public void setNewNickname(String newNickname) {
        this.newNickname = newNickname;
    }

    /**
	 * @return the newSex
	 */
    public String getNewSex() {
        return newSex;
    }

    /**
	 * @param newSex the newSex to set
	 */
    public void setNewSex(String newSex) {
        this.newSex = newSex;
    }

    /**
	 * @return the oldNickname
	 */
    public String getOldNickname() {
        return oldNickname;
    }

    /**
	 * @param oldNickname the oldNickname to set
	 */
    public void setOldNickname(String oldNickname) {
        this.oldNickname = oldNickname;
    }

    /**
	 * @return the sex
	 */
    public String getSex() {
        return sex;
    }

    /**
	 * @param sex the sex to set
	 */
    public void setSex(String sex) {
        this.sex = sex;
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

    /**
	 * @return the confirmNewPassword
	 */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
	 * @param confirmNewPassword the confirmNewPassword to set
	 */
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    /**
	 * @return the newPassword
	 */
    public String getNewPassword() {
        return newPassword;
    }

    /**
	 * @param newPassword the newPassword to set
	 */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
	 * @return the oldPassword
	 */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
	 * @param oldPassword the oldPassword to set
	 */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
