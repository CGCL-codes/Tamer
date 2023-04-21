package cn.ac.ntarl.umt.actions.user;

import java.util.Vector;
import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.database.DAOFactory;
import cn.ac.ntarl.umt.database.ExecuteHelper;
import cn.ac.ntarl.umt.database.user.UserDAO;
import cn.vlabs.simpleAuth.AuthInfo;
import cn.vlabs.simpleAuth.Principal;

public class DBSimpleAuthLogIn extends ExecuteHelper {

    private static final long serialVersionUID = 1L;

    @Override
    public void checkPermission() throws CLBException {
    }

    @Override
    public Object performWithResult() throws CLBException {
        UserDAO gd = DAOFactory.getInstance().getUserDAO();
        Vector<Principal> principals = gd.UserLogin(authinfo);
        return principals;
    }

    public DBSimpleAuthLogIn(AuthInfo authinfo) {
        this.authinfo = authinfo;
    }

    private AuthInfo authinfo;
}
