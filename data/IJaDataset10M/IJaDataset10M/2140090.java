package org.blueoxygen.cimande.registration;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.struts2.ServletActionContext;
import org.blueoxygen.cimande.commons.CimandeAction;
import org.blueoxygen.cimande.commons.LogInformation;
import org.blueoxygen.cimande.persistence.PersistenceManager;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.User;
import org.blueoxygen.util.PropertyLooker;
import org.blueoxygen.util.StringUtils;

public class AutentifikasiEmail extends CimandeAction {

    private String id = "";

    private PersistenceManager manager;

    private SessionCredentials sess;

    private LogInformation loginfo;

    private User user;

    private int e = 1;

    private static final int ENCRIPTED = 1;

    private static int OWNER_EDIT = 3;

    public String execute() {
        if (e == ENCRIPTED) {
            id = new StringUtils().decodeBase64(getId());
        }
        user = (User) getManager().getById(User.class, getId());
        loginfo = user.getLogInformation();
        loginfo.setActiveFlag(1);
        loginfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        if (e == OWNER_EDIT) {
            loginfo.setLastUpdateBy(sess.getCurrentUser().getId());
        }
        user.setLogInformation(loginfo);
        getManager().save(user);
        HtmlEmail mail = new HtmlEmail();
        mail.setHostName(get("email.smtp.server"));
        mail.setSSL(true);
        mail.setSmtpPort(Integer.parseInt(get("email.smtp.port")));
        if (!"".equalsIgnoreCase(get("email.smtp.username"))) {
            mail.setAuthentication(get("email.smtp.username"), get("email.smtp.password"));
        }
        try {
            URL url = new URL(get("application.logo.url"));
            String cid = mail.embed(url, "JobMerv Logo");
            mail.addTo(user.getEmail(), user.getName().getFirst() + " " + user.getName().getLast());
            mail.setFrom(PropertyLooker.get("application.activation.from.email"), PropertyLooker.get("application.activation.from.name"));
            mail.setSubject("Account has been activated");
            mail.setHtmlMsg("<html><img src=\"cid:" + cid + "\">" + "<br><hr><br>" + "<br>You are registered in Mervpolis " + "Your account has been activated. You can log in now with your account.<br>" + "<br>To start using Mervpolis, click <a href=\"" + ServletActionContext.getRequest().getContextPath() + "/backend/user/index.action\">here</a>." + "<br><br>Best regards,<br>Cimande Admin</html>");
            mail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        return SUCCESS;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the manager
	 */
    public PersistenceManager getManager() {
        return manager;
    }

    /**
	 * @param manager
	 *            the manager to set
	 */
    public void setManager(PersistenceManager manager) {
        this.manager = manager;
    }

    /**
	 * @param sess
	 *            the sess to set
	 */
    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sess = sessionCredentials;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }
}
