package org.blueoxygen.papaje.invite;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.papaje.entity.Email;
import org.blueoxygen.util.StringUtils;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.persistence.PersistenceAware;
import org.blueoxygen.cimande.persistence.PersistenceManager;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;
import com.opensymphony.xwork2.ActionSupport;

public class FriendRequest extends ActionSupport implements SessionCredentialsAware {

    private SessionCredentials sess;

    private PersistenceManager manager;

    private String id = "";

    Email mail = new Email();

    LogInformation logInfo;

    private List<Email> ers = new ArrayList<Email>();

    public String execute() {
        id = new StringUtils().decodeBase64(getId());
        List<Email> emails = getManager().getList("FROM " + Email.class.getName() + " e WHERE e.user_id='" + id + "'", null, null);
        if (!emails.isEmpty()) {
            setMail(emails.get(0));
        }
        if (mail.getLogInformation() == null) {
            logInfo = new LogInformation();
        } else {
            logInfo = mail.getLogInformation();
        }
        logInfo.setActiveFlag(1);
        logInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        mail.setLogInformation(logInfo);
        getManager().save(mail);
        return SUCCESS;
    }

    /**
	 * @return the mail
	 */
    public Email getMail() {
        return mail;
    }

    /**
	 * @param mail the mail to set
	 */
    public void setMail(Email mail) {
        this.mail = mail;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
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
	 * @param manager the manager to set
	 */
    public void setManager(PersistenceManager manager) {
        this.manager = manager;
    }

    /**
	 * @param sess the sess to set
	 */
    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sess = sessionCredentials;
    }
}
