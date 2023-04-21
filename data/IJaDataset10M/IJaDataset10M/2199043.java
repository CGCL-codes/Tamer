package squirrels.util;

import java.util.Map;
import squirrels.constant.ApplicationConstants;
import squirrels.constant.ResourceConstants;
import squirrels.constant.ResultConstants;
import squirrels.constant.SessionConstants;
import squirrels.constant.SqlIdConstants;
import squirrels.constant.TemplateConstants;
import squirrels.constant.ValueConstants;
import nuts.ext.ibatis.SqlMapClientFactory;
import nuts.ext.xwork2.util.ContextUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ActionUtils.
 */
public abstract class ActionUtils implements ResultConstants, SessionConstants, ApplicationConstants, SqlIdConstants, ResourceConstants, TemplateConstants, ValueConstants {

    /**
	 * @return session
	 */
    public static Map<String, Object> getSession() {
        return ContextUtils.getSession();
    }

    /**
	 * @return application
	 */
    public static Map<String, Object> getApplication() {
        return ContextUtils.getApplication();
    }

    /**
	 * @return the sqlMapClient
	 */
    public static SqlMapClient getSqlMapClient() {
        return SqlMapClientFactory.getSqlMapClient();
    }
}
