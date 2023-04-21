package org.jcrexplorer.login;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcrexplorer.BackingBean;
import org.jcrexplorer.Constants;
import org.jcrexplorer.ContentBean;

public class LoginBean extends BackingBean {

    private Log logger = LogFactory.getLog(this.getClass());

    private ContentBean contentBean;

    private String password;

    private String username;

    private String jndiName = "java:jcr/local";

    public ContentBean getContentBean() {
        return contentBean;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    /**
	 * @jsfActionHandler
	 */
    public String login() {
        try {
            InitialContext context = new InitialContext();
            Repository repository = (Repository) context.lookup(jndiName);
            logger.info("Found repository, retrieving session and loggin in...");
            SimpleCredentials credentials = new SimpleCredentials(username, password.toCharArray());
            Session session = repository.login(credentials);
            contentBean.startSession(session);
            contentBean.setCredentials(credentials);
            logger.info("Logged in, session active, let's go...");
            return Constants.OUTCOME_SUCCESS;
        } catch (Exception e) {
            addErrorMessage(e);
            return Constants.OUTCOME_FAILURE;
        }
    }

    public void setContentBean(ContentBean contentBean) {
        this.contentBean = contentBean;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String context) {
        this.jndiName = context;
    }
}
