package welo.dao;

import java.util.List;
import org.apache.cayenne.query.SelectQuery;
import welo.dao.common.NonThreadBaseDao;
import welo.model.db.WebUser;

public class SetupDatabaseDao extends NonThreadBaseDao {

    /**
	 * Check the need to populate the database with schema 
	 * @return
	 */
    public boolean isWebUserExist() {
        List<WebUser> userlist = getObjectContext().performQuery(new SelectQuery(WebUser.class));
        return userlist.size() > 0;
    }
}
