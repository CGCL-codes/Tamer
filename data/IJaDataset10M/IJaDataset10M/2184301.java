package cn.ac.ntarl.umt.actions.request;

import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.database.DAOFactory;
import cn.ac.ntarl.umt.database.ExecuteHelper;
import cn.ac.ntarl.umt.request.RequestDAO;

public class DBReadRequests extends ExecuteHelper {

    public DBReadRequests(int groupid, String type) {
        this.groupid = Integer.toString(groupid);
        this.type = type;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void checkPermission() throws CLBException {
    }

    @Override
    public Object performWithResult() throws CLBException {
        RequestDAO rd = DAOFactory.getInstance().getRequestDAO();
        return rd.getRequest(type, groupid);
    }

    private String groupid;

    private String type;
}
