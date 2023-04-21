package com.centraview.additionalmenu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Logger;
import com.centraview.common.CVDal;

/**
 * This is the EJB class for File The Logic for methods defined in Remote
 * interface is defined in this class
 * 
 * @author CentraView, LLC.
 */
public class AdditionalMenuEJB implements SessionBean {

    private static Logger logger = Logger.getLogger(AdditionalMenuEJB.class);

    protected SessionContext sessionContext;

    private String dataSource = "MySqlDS";

    /**
   * Set the associated session context. The container calls this method after
   * the instance creation. The enterprise Bean instance should store the
   * reference to the context object in an instance variable. This method is
   * called with no transaction context.
   * 
   * @param sessionContext The new sessionContext value
   */
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
   * called when instance is getting created
   *  
   */
    public void ejbCreate() {
    }

    /**
   * Called before object is getting removed
   *  
   */
    public void ejbRemove() {
    }

    /**
   * Called when bean is activated
   *  
   */
    public void ejbActivate() {
    }

    /**
   * Called when bean is passivated
   *  
   */
    public void ejbPassivate() {
    }

    /**
   * This method returns a vector of AdditionalMenuVO for the user
   * @param userID(int)
   * @param isOrdered(boolean)
   * @return Vector
   * @exception none
   */
    public Vector getAllAdditionalMenuItems(boolean isOrdered) {
        Collection col = null;
        Vector vec = new Vector();
        try {
            CVDal dl = new CVDal(dataSource);
            dl.setSql("additionalmenu.getallmenu");
            col = dl.executeQuery();
            dl.destroy();
            if (col != null) {
                Iterator it = col.iterator();
                HashMap hm = null;
                while (it.hasNext()) {
                    hm = (HashMap) it.next();
                    AdditionalMenuVO amVO = new AdditionalMenuVO();
                    int id = ((Number) hm.get("id")).intValue();
                    amVO.setMenuItemId(id);
                    amVO.setMenuItemName((String) hm.get("name"));
                    amVO.setModuleId(((Number) hm.get("moduleid")).intValue());
                    amVO.setModuleName((String) hm.get("modulename"));
                    amVO.setForwardResource((String) hm.get("forward"));
                    if (((Number) hm.get("new_win")).intValue() == 0) amVO.setIsNewWin(false); else amVO.setIsNewWin(true);
                    amVO.setWinProperty((String) hm.get("win_property"));
                    amVO.setParams((String) hm.get("params"));
                    vec.add(amVO);
                }
            }
        } catch (Exception e) {
            logger.error("[getAllAdditionalMenuItems] Exception thrown.", e);
        }
        return vec;
    }

    /**
   * @author Kevin McAllister <kevin@centraview.com>This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
    public void setDataSource(String ds) {
        this.dataSource = ds;
    }
}
