package org.hip.vif.core.bom.impl;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.hip.kernel.bom.QueryStatement;
import org.hip.kernel.bom.SingleValueQueryStatement;
import org.hip.kernel.bom.impl.ExtDBQueryStatement;
import org.hip.kernel.bom.impl.ExtSingleValueQueryStatement;
import org.hip.kernel.persistency.ConnectionSetting;
import org.hip.kernel.persistency.ConnectionSettingImpl;
import org.hip.vif.core.service.PreferencesHandler;
import org.hip.vif.core.util.ExternalObjectDefUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Home for <code>Member</code> objects retrieved from an external member database.
 * 
 * @author Luthiger
 */
public class ExtMemberHomeImpl extends MemberHomeImpl {

    private static final Logger LOG = LoggerFactory.getLogger(ExtMemberHomeImpl.class);

    private static final String MEMBER_CLASS_NAME = "org.hip.vif.core.bom.impl.ExtMemberImpl";

    static final String OBJECT_DEF_FILE = "MEMBEROBJECTDEF.xml";

    private QueryStatement statement = null;

    /**
	 * Returns the Member class name
	 *
	 * @return java.lang.String
	 */
    public String getObjectClassName() {
        return MEMBER_CLASS_NAME;
    }

    /**
	 * Reads the content of the file <code>$TOMCAT_HOME/webapps/vifapp/WEB-INF/conf/MEMBEROBJECTDEF.xml</code>.
	 */
    protected String getObjectDefString() {
        File lObjectDefFile = ExternalObjectDefUtil.getObjectDefFile(OBJECT_DEF_FILE);
        if (!lObjectDefFile.exists()) {
            return "";
        }
        return ExternalObjectDefUtil.readObjectDef(lObjectDefFile);
    }

    /**
	 * Overrides super implementation to return specialised QueryStatement.
	 */
    public QueryStatement createQueryStatement() {
        if (statement == null) {
            statement = createStatement();
        }
        return statement;
    }

    private QueryStatement createStatement() {
        return new ExtDBQueryStatement(this, createSetting());
    }

    /**
	 * Overrides super implementation to return specialised QueryStatement.
	 */
    protected SingleValueQueryStatement createSingleValueQueryStatement() {
        return new ExtSingleValueQueryStatement(createSetting());
    }

    private ConnectionSetting createSetting() {
        try {
            PreferencesHandler lPreferences = PreferencesHandler.INSTANCE;
            String lDriver = lPreferences.get(PreferencesHandler.KEY_DBX_DRIVER);
            String lUrl = lPreferences.get(PreferencesHandler.KEY_DBX_CONNECTION);
            String lUserId = lPreferences.get(PreferencesHandler.KEY_DBX_USER);
            String lPassword = lPreferences.get(PreferencesHandler.KEY_DBX_PW);
            String lJndi = lPreferences.get(PreferencesHandler.KEY_DBX_JNDI);
            return new ConnectionSettingImpl("ExtMember", lDriver, lUrl, lUserId, lPassword, lJndi);
        } catch (IOException exc) {
            LOG.error("Error encountered while setting the connection for the external DB!", exc);
        }
        return null;
    }
}
