package com.centraview.projects.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import com.centraview.common.CVDal;

public class ProjectsHelperEJB implements SessionBean {

    private javax.ejb.SessionContext ctx;

    private String dataSource = "MySqlDS";

    public ProjectsHelperEJB() {
    }

    public void setSessionContext(SessionContext ctx) {
        this.ctx = ctx;
    }

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    /**
   * This method responsible to query database to get list of project 
   * status from projectstatus table
   * @return Vector containing list of available statuses
   */
    public Vector getProjectStatusList() {
        CVDal dl = new CVDal(this.dataSource);
        Collection col = null;
        try {
            dl.setSql("project.getprojectstatuslist");
            col = dl.executeQuery();
            dl.clearParameters();
        } finally {
            dl.destroy();
        }
        return fillProjectStatusList(col);
    }

    /**
  * This methos filles the projectstatus table details in StatusVO Object
  * and wraps up in Vector.
  *
  * @return Vector 
  */
    private Vector fillProjectStatusList(Collection col) {
        StatusVO statusVO = null;
        Vector list = null;
        if (col != null) {
            Iterator it = col.iterator();
            String description, title;
            int statusID;
            list = new Vector();
            while (it.hasNext()) {
                HashMap hm = (HashMap) it.next();
                statusID = ((Long) hm.get("StatusID")).intValue();
                title = (String) hm.get("Title");
                description = (String) hm.get("Description");
                statusVO = new StatusVO(statusID, title, description);
                list.add(statusVO);
            }
        }
        return list;
    }

    /**
  * This method responsible to query database to get list of project 
  * status from projectstatus table
  * 
  * @return Vector
  */
    public Vector getTaskStatusList() throws java.rmi.RemoteException {
        CVDal dl = new CVDal(this.dataSource);
        dl.setSql("project.getprojectstatuslist");
        Collection col = dl.executeQuery();
        dl.clearParameters();
        dl.destroy();
        return fillProjectStatusList(col);
    }

    /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
    public void setDataSource(String ds) {
        this.dataSource = ds;
    }
}
