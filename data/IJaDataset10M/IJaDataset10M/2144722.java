package edu.upmc.opi.caBIG.caTIES.server.dispatcher;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.xpath.XPath;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Constants;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_JDomUtils;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.ActivityLogImpl;

public class CaTIES_ActivityLogger {

    private static final Logger logger = Logger.getLogger(CaTIES_ActivityLogger.class);

    public CaTIES_ActivityLogger() {
        ;
    }

    public void logDispatcherActivity(org.jdom.Document dispatcherConversationDocument, Properties properties) {
        SessionFactory sessionFactory = null;
        Session session = null;
        try {
            ActivityLogImpl activityLog = buildActivityFromHeaderInformation(dispatcherConversationDocument);
            String hibernateConfigurationFileName = properties.getProperty(CaTIES_Constants.PROPERTY_KEY_ACTIVITY_LOGGER_HIBERNATE_CFG);
            URL hibernateConfigurationFileURL = getClass().getResource(hibernateConfigurationFileName);
            Configuration configuration = new Configuration();
            configuration.configure(hibernateConfigurationFileURL);
            configuration.setProperty("hibernate.c3p0.min_size", "1");
            configuration.setProperty("hibernate.c3p0.max_size", "5");
            sessionFactory = configuration.buildSessionFactory();
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(activityLog);
            tx.commit();
            session.close();
            sessionFactory.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            forceSessionClose(session);
            forceSessionFactoryClose(sessionFactory);
        }
    }

    private void forceSessionClose(Session session) {
        try {
            if (session != null) {
                session.close();
            }
        } catch (Exception x) {
        }
    }

    private void forceSessionFactoryClose(SessionFactory sessionFactory) {
        try {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        } catch (Exception x) {
        }
    }

    @SuppressWarnings("unchecked")
    public ActivityLogImpl buildActivityFromHeaderInformation(org.jdom.Document doc) {
        ActivityLogImpl activityLog = null;
        String xpathQueryString = "";
        xpathQueryString += "//CaTIESUser";
        XPath xpath;
        try {
            xpath = XPath.newInstance(xpathQueryString);
            List<Element> results = xpath.selectNodes(doc);
            if (results.size() > 0) {
                Element userElement = results.get(0);
                activityLog = new ActivityLogImpl();
                Date timeNow = new Date();
                activityLog.setStartTime(timeNow);
                activityLog.setEndTime(timeNow);
                activityLog.setLastName((String) nullSafeGet(userElement.getAttributeValue("lastname")));
                activityLog.setFirstName((String) nullSafeGet(userElement.getAttributeValue("firstname")));
                activityLog.setOrganization((String) nullSafeGet(userElement.getAttributeValue("organization")));
                activityLog.setProtocol((String) nullSafeGet(userElement.getAttributeValue("protocol")));
                activityLog.setRole((String) nullSafeGet(userElement.getAttributeValue("role")));
                activityLog.setActivityText(CaTIES_JDomUtils.convertDocumentToString(doc, Format.getPrettyFormat()));
                logger.debug("Finished building activity log ==>\n" + activityLog);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return activityLog;
    }

    private Object nullSafeGet(Object requestObject) {
        return (requestObject == null) ? "" : requestObject;
    }
}
