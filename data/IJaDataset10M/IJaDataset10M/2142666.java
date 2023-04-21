package ch.articlefox.newsdesk.foxes;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import ch.articlefox.db.TUsers;
import ch.articlefox.db.logic.Users;
import ch.articlefox.newsdesk.NewsDeskConstants;
import com.orelias.infoaccess.Config;
import com.orelias.infoaccess.InfoAccess;
import com.orelias.infoaccess.InfoBean;
import com.orelias.infoaccess.sql.SqlInfoAccess;

/**
 * The configuration for an Articlefox instances managed by this news desk.
 * 
 * @author Lukas Blunschi
 * 
 */
public class Fox {

    private final Properties props;

    private String name;

    private String dbConnUrl;

    private String webBaseUrl;

    private Map<String, Integer> usernameIdMap;

    private InfoAccess infoaccess;

    public Fox(String configPath) throws Exception {
        this.props = new Properties();
        props.load(new FileInputStream(configPath + NewsDeskConstants.FN_AFOXINFOACCESSBASE));
    }

    protected void validate() throws Exception {
        boolean allSet = name != null && dbConnUrl != null && webBaseUrl != null;
        if (!allSet) {
            throw new Exception("incomplete Articlefox instance configuration: " + this);
        }
    }

    public InfoAccess getInfoAccess() {
        return infoaccess;
    }

    /**
	 * Get user by its username in the given articlefox database.
	 * 
	 * @param username
	 * @param infoaccess
	 * @return user or null if user does not exist in the given articlefox
	 *         database.
	 */
    public InfoBean getUserByUsername(String username, InfoAccess infoaccess) {
        if (usernameIdMap == null) {
            usernameIdMap = Users.getUsernameIdMap(dbConnUrl);
        }
        Integer userId = usernameIdMap.get(username);
        if (userId == null) {
            usernameIdMap = Users.getUsernameIdMap(dbConnUrl);
            userId = usernameIdMap.get(username);
        }
        if (userId == null) {
            return null;
        } else {
            return infoaccess.get(TUsers.TBL_NAME, userId, true);
        }
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDbConnUrl(String dbConnUrl) {
        this.dbConnUrl = dbConnUrl;
        props.setProperty(Config.CONN_URL, dbConnUrl);
        infoaccess = new SqlInfoAccess(props);
    }

    protected void setWebBaseUrl(String webBaseUrl) {
        this.webBaseUrl = webBaseUrl;
    }

    public String getName() {
        return name;
    }

    public String getDbConnUrl() {
        return dbConnUrl;
    }

    public String getWebBaseUrl() {
        return webBaseUrl;
    }

    @Override
    public String toString() {
        return name;
    }
}
