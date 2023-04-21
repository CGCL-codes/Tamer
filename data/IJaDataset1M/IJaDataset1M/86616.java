package de.uni_hamburg.golem.service;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import de.uni_hamburg.golem.control.BatchProcessor;
import de.uni_hamburg.golem.control.SecurityController;
import de.uni_hamburg.golem.model.GEnterprisePackage;
import de.uni_hamburg.golem.model.GMessage;
import de.uni_hamburg.golem.model.GPerson;

public class GolemPackageService {

    private Log log = LogFactory.getLog(this.getClass());

    private SecurityController security;

    private BatchProcessor batch;

    private void init() {
        if (security == null) {
            MessageContext mc = MessageContext.getCurrentContext();
            HttpServletRequest req = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
            ServletContext context = req.getSession().getServletContext();
            WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(context);
            this.security = (SecurityController) wc.getBean("golemSecurityController");
            this.batch = (BatchProcessor) wc.getBean("golemBatchController");
        }
    }

    private void init(ServletContext context) {
        if (security == null) {
            WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(context);
            this.security = (SecurityController) wc.getBean("golemSecurityController");
            this.batch = (BatchProcessor) wc.getBean("golemBatchController");
        }
    }

    /**
	 * @param userid
	 * @param password
	 * @param pkgXML
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
    public String invoke(String userid, String password, String pkgXML) {
        init();
        GEnterprisePackage msgs = new GEnterprisePackage();
        try {
            GPerson user = security.authenticate(userid, password);
            if (user != null && security.isSysAdmin(user)) {
                GEnterprisePackage pkg = GEnterprisePackage.read(pkgXML);
                GEnterprisePackage batchMsgs = batch.process(pkg);
                msgs.addAll(batchMsgs.getMessages());
            }
        } catch (Exception e) {
            msgs.addMessage(GMessage.CODE_ERROR, GMessage.SOURCE_GOLEM, GMessage.TARGET_RETURN, "", this.getClass() + " ERROR : " + e.toString());
        }
        try {
            return msgs.toXML();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * Fetch Person data.
	 * @param adminid
	 * @param password
	 * @param userid
	 * @return
	 */
    public String getPerson(ServletContext context, String adminid, String password, String userid) {
        init(context);
        GEnterprisePackage pkg = new GEnterprisePackage();
        try {
            GPerson admin = security.authenticate(adminid, password);
            if (admin != null && security.isSysAdmin(admin)) {
                GPerson user = security.getUser(userid);
                if (user != null) {
                    pkg.add(user);
                }
            }
        } catch (Exception e) {
            pkg.addMessage(GMessage.CODE_ERROR, GMessage.SOURCE_GOLEM, GMessage.TARGET_RETURN, "", this.getClass() + " ERROR : " + e.toString());
        }
        try {
            return pkg.toXML();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * @param userid
	 * @param password
	 * @param pkgXML
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
    public String invoke(ServletContext context, String userid, String password, String pkgXML) {
        init(context);
        GEnterprisePackage msgs = new GEnterprisePackage();
        try {
            GPerson user = security.authenticate(userid, password);
            if (user != null && security.isSysAdmin(user)) {
                GEnterprisePackage pkg = GEnterprisePackage.read(pkgXML);
                GEnterprisePackage batchMsgs = batch.process(pkg);
                msgs.addAll(batchMsgs.getMessages());
            } else {
                msgs.addMessage(GMessage.CODE_ERROR, GMessage.SOURCE_GOLEM, GMessage.TARGET_RETURN, "", "not authenticated/authorized");
            }
        } catch (Exception e) {
            msgs.addMessage(GMessage.CODE_ERROR, GMessage.SOURCE_GOLEM, GMessage.TARGET_RETURN, "", this.getClass() + " ERROR : " + e.toString());
        }
        try {
            return msgs.toXML();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    /**
	 * @return the security
	 */
    public SecurityController getSecurity() {
        return security;
    }

    /**
	 * @param security
	 *            the security to set
	 */
    public void setSecurity(SecurityController security) {
        this.security = security;
    }

    /**
	 * @return the batch
	 */
    public BatchProcessor getBatch() {
        return batch;
    }

    /**
	 * @param batch
	 *            the batch to set
	 */
    public void setBatch(BatchProcessor batch) {
        this.batch = batch;
    }
}
