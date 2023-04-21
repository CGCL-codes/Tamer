package de.juwimm.cms.messaging.bean.mdp;

import java.security.PrivilegedAction;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import org.apache.log4j.Logger;
import de.juwimm.cms.authorization.SimpleCallbackHandler;
import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.remote.AuthorizationServiceSpring;
import de.juwimm.cms.beans.foreign.CqPropertiesBeanSpring;
import de.juwimm.cms.messaging.MessageConstants;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.remote.EditionServiceSpring;

/**
 * This is the DeployCreateQueueBean, a Message-driven Bean, which only processes a single Item out of the queue
 * at a time. This MDB sends the Data at the end to the liveserver for deploying purposes.
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: Juwi|MacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: DeployCreateQueueMessageListener.java 34 2009-02-20 08:52:25Z skulawik $
 *
 */
public class DeployCreateQueueMessageListener implements MessageListener {

    private static Logger log = Logger.getLogger(DeployCreateQueueMessageListener.class);

    private static int id = 0;

    private EditionServiceSpring editionService = null;

    private AuthorizationServiceSpring authorizationService = null;

    private UserHbmDao userHbmDao = null;

    private UserHbm user = null;

    private SiteHbm previousSite = null;

    private CqPropertiesBeanSpring cqPropertiesBeanSpring;

    public CqPropertiesBeanSpring getCqPropertiesBeanSpring() {
        return cqPropertiesBeanSpring;
    }

    public void setCqPropertiesBeanSpring(CqPropertiesBeanSpring cqPropertiesBeanSpring) {
        this.cqPropertiesBeanSpring = cqPropertiesBeanSpring;
    }

    public void setEditionServiceSpring(EditionServiceSpring editionService) {
        this.editionService = editionService;
    }

    public void setAuthorizationServiceSpring(AuthorizationServiceSpring authorizationService) {
        this.authorizationService = authorizationService;
    }

    public void setUserHbmDao(UserHbmDao userHbmDao) {
        this.userHbmDao = userHbmDao;
    }

    /**
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>deploy, siteRoot</i>
	 * @param message The Messageobject. Currently can contain following <i>StringProperties</i>:
	 * <ul><li><i>userName</i> - The username</li>
	 * <li><i>comment</i> - A Comment</li>
	 * <li><i>rootViewComponentId</i> - The rootViewComponentId of this Unit</li>
	 * <li><i>deploy</i> - Boolean if only create Edition or also deploy to the Liveserver</li>
	 * <li><i>showMessage</i> - The Message the User woll get on success</li>
	 * <li><i>liveServerIP</i> - The IP Address of the Liveserver</li></ul>
	 */
    public void onMessage(Message message) {
        log.debug("Started queue with Job: " + (++id));
        String messageType = "";
        try {
            messageType = message.getJMSType();
            LoginContext lc = new LoginContext("juwimm-cms-security-domain", new SimpleCallbackHandler(getCqPropertiesBeanSpring().getSystemUser(), getCqPropertiesBeanSpring().getSystemPasswd()));
            lc.login();
            Subject s = lc.getSubject();
            if (log.isDebugEnabled()) log.debug("Subject is: " + s.getPrincipals().toString());
            if (messageType.equalsIgnoreCase(MessageConstants.MESSAGE_TYPE_LIVE_DEPLOY)) {
                Subject.doAs(s, new CreateLiveEditionPrivilegedAction(message));
            } else if (messageType.equalsIgnoreCase(MessageConstants.MESSAGE_TYPE_FULL_IMPORT)) {
                Integer rootVcId = null;
                try {
                    rootVcId = new Integer(message.getStringProperty("rootVcId"));
                    if (rootVcId.intValue() == 0) rootVcId = null;
                } catch (Exception exe) {
                }
                Subject.doAs(s, new FullEditionImportPrivilegedAction(new Integer(message.getStringProperty("siteId")), message.getStringProperty("editionFileName"), rootVcId));
            }
            lc.logout();
        } catch (Exception exe) {
            log.error("Error occured in onMessage doing " + messageType + ": ", exe);
        }
        log.debug("Finished queue with Job: " + id);
    }

    /**
	 * 
	 */
    private class FullEditionImportPrivilegedAction<T> implements PrivilegedAction<T> {

        private Integer siteId;

        private String editionFileName;

        private Integer rootVcId = null;

        public FullEditionImportPrivilegedAction(Integer siteId, String editionFileName, Integer rootVcId) {
            this.siteId = siteId;
            this.editionFileName = editionFileName;
            this.rootVcId = rootVcId;
        }

        public T run() {
            try {
                loginUser(this.siteId);
                editionService.processFileImport(this.siteId, this.editionFileName, this.rootVcId);
                restoreUserState();
            } catch (Exception exe) {
                user = null;
                previousSite = null;
                log.error("Error occured in FullEditionImportPrivilegedAction: ", exe);
            }
            return null;
        }
    }

    /**
	 * 
	 */
    private class CreateLiveEditionPrivilegedAction<T> implements PrivilegedAction<T> {

        private String userName = "";

        private String comment = "";

        private Integer rootViewComponentId = null;

        private Integer siteId = null;

        private boolean deploy = false;

        private boolean showMessage = false;

        public CreateLiveEditionPrivilegedAction(Message message) {
            try {
                this.userName = message.getStringProperty("userName");
                this.comment = message.getStringProperty("comment");
                this.rootViewComponentId = new Integer(message.getStringProperty("rootViewComponentId"));
                this.deploy = message.getBooleanProperty("deploy");
                this.showMessage = Boolean.parseBoolean(message.getStringProperty("showMessage"));
                this.siteId = new Integer(message.getStringProperty("siteId"));
            } catch (Exception exe) {
                log.error("Error occured in CreateLiveEditionPrivilegedAction: ", exe);
            }
        }

        public T run() {
            try {
                loginUser(this.siteId);
                editionService.createLiveDeploy(this.userName, this.comment, this.rootViewComponentId, this.deploy, this.showMessage);
            } catch (Exception exe) {
                log.error("Error occured in CreateLiveEditionPrivilegedAction: ", exe);
            }
            log.debug("CreateLiveEditionPrivilegedAction end");
            return null;
        }
    }

    private void loginUser(Integer siteId) {
        try {
            this.user = this.userHbmDao.load(getCqPropertiesBeanSpring().getSystemUser());
            this.previousSite = this.user.getActiveSite();
            this.authorizationService.login(getCqPropertiesBeanSpring().getSystemUser(), getCqPropertiesBeanSpring().getSystemPasswd(), siteId);
        } catch (Exception e) {
            this.user = null;
            this.previousSite = null;
            log.error("Error loging in on site " + siteId + ": ", e);
        }
    }

    private void restoreUserState() {
        if (this.user != null && this.previousSite != null) {
            this.user.setActiveSite(this.previousSite);
            log.info("Logout User " + this.user.getUserId());
            this.user.setLoginDate(0L);
            this.userHbmDao.update(this.user);
            this.user = null;
            this.previousSite = null;
        }
    }
}
