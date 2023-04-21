package cn.ac.ntarl.umt.actions.app;

import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.app.AppDAO;
import cn.ac.ntarl.umt.app.Application;
import cn.ac.ntarl.umt.database.DAOFactory;
import cn.ac.ntarl.umt.database.ExecuteHelper;

public class DBCreateApp extends ExecuteHelper {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public DBCreateApp(Application app) {
        this.app = app;
    }

    public void checkPermission() throws CLBException {
    }

    public Object performWithResult() throws CLBException {
        AppDAO ad = DAOFactory.getInstance().getAppDAO();
        return Integer.valueOf(ad.createApp(app));
    }

    private Application app;
}
