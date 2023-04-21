package cn.ac.ntarl.umt.actions.user;

import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.database.DAOFactory;
import cn.ac.ntarl.umt.database.ExecuteHelper;
import cn.ac.ntarl.umt.database.user.UserDAO;

public class DBListUserGroups extends ExecuteHelper {

    @Override
    public void checkPermission() throws CLBException {
    }

    public DBListUserGroups(int userid) {
        this.userid = userid;
    }

    @Override
    public Object performWithResult() throws CLBException {
        UserDAO dbuser = DAOFactory.getInstance().getUserDAO();
        return dbuser.getUserGroups(userid);
    }

    private static final long serialVersionUID = 4682831630515321420L;

    private int userid;
}
