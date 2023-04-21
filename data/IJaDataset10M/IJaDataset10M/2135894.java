package org.eaasyst.eaa.data.impl;

import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.data.DataAccessBean;
import org.eaasyst.eaa.data.DataAccessBeanFactoryBase;
import org.eaasyst.eaa.data.XmlDatabase;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>Implements the required methods for the EaasyStreet user
 * profile data access bean factory component.</p>
 *
 * @version 2.2.1
 * @author Jeff Chilton
 */
public class UserProfileDabFactory extends DataAccessBeanFactoryBase {

    private static final String DATABASE_KEY = Constants.CTX_SECURITY_USER_DATABASE;

    /**
	 * <p>Constructs a new "UserProfileDabFactory" object.</p>
	 *
	 * @since Eaasy Street 2.0
	 */
    public UserProfileDabFactory() {
        setClassName(StringUtils.computeClassName(getClass()));
        if (EaasyStreet.getProperty(Constants.CFG_SEC_DATA_CONN) != null) {
            DataAccessBean dab = (DataAccessBean) EaasyStreet.getInstance(EaasyStreet.getProperty(Constants.CFG_SEC_DATA_CONN));
            if (dab != null) {
                setAccessBean("logon", dab);
                setAccessBean("list", dab);
                setAccessBean("browse", dab);
                setAccessBean("edit", dab);
                setAccessBean("delete", dab);
                setAccessBean("reset", dab);
                setAccessBean("get", dab);
                setAccessBean("byGroup", dab);
                setAccessBean("updateLastLogon", dab);
                setAccessBean("updateSecurityQuestions", dab);
                setAccessBean("updateForcedApplications", dab);
                setAccessBean("getUserIdsAndNames", dab);
                setAccessBean("all", dab);
            } else {
                EaasyStreet.logError("Unable to instantiate requested User Profile data connector.");
            }
        } else {
            XmlDatabase database = (XmlDatabase) EaasyStreet.getContextAttribute(DATABASE_KEY);
            if (database != null) {
                DataAccessBean dab = new XmlUserProfileDab();
                setAccessBean("logon", dab);
                setAccessBean("list", dab);
                setAccessBean("browse", dab);
                setAccessBean("edit", dab);
                setAccessBean("delete", dab);
                setAccessBean("reset", dab);
                setAccessBean("get", dab);
                setAccessBean("updateLastLogon", dab);
                setAccessBean("updateSecurityQuestions", dab);
                setAccessBean("updateForcedApplications", dab);
                setAccessBean("all", dab);
            } else {
                setStandardSqlUsed(true);
                setSqlDataSource(null);
                setSqlStatement("logon", "select * from users where userId = {key}");
                setSqlStatement("list", "select * from users where userId = {key}");
                setSqlStatement("browse", "select * from users where userId = {key}");
                setSqlStatement("edit", "select * from users where userId = {key}");
                setSqlStatement("delete", "select * from users where userId = {key}");
                setSqlStatement("reset", "select * from users where userId = {key}");
                setSqlStatement("all", "select * from users");
            }
        }
    }
}
