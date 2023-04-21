package ispyb.server.data.ejb;

import ispyb.server.data.interfaces.AdminActivityLightValue;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Session facade for AdminActivity.
 * 
 * @ejb.bean 
 * 		name="AdminActivityFacade"
 * 		type="Stateless"
 * 		view-type="local"
 * 		local-jndi-name="ispyb/AdminActivityFacadeLocalHome"
 * 
 * @ejb.ejb-ref
 * 		ejb-name="AdminActivity"
 * 		view-type="local"
 * 		ref-name="ejb/AdminActivity"
 * 		@ejb.util generate="physical"
*/
public abstract class AdminActivityFacadeEJB implements javax.ejb.SessionBean {

    private javax.ejb.SessionContext _ctx;

    private ispyb.server.data.interfaces.AdminActivityLocalHome getLocalHome() throws javax.naming.NamingException {
        return ispyb.server.data.interfaces.AdminActivityUtil.getLocalHome();
    }

    /**
 * @ejb.interface-method
 * @ejb.facade-method
 * 
*/
    public java.util.Collection findAll() throws javax.ejb.FinderException, javax.naming.NamingException {
        java.util.Collection selected = getLocalHome().findAll();
        ArrayList retval = new ArrayList(selected.size());
        for (Iterator i = selected.iterator(); i.hasNext(); ) {
            retval.add(((ispyb.server.data.interfaces.AdminActivityLocal) i.next()).getAdminActivityValue());
        }
        return retval;
    }

    /**
 * @ejb.interface-method
 * @ejb.facade-method
 * 
*/
    public java.util.Collection findByUsername(java.lang.String username) throws javax.ejb.FinderException, javax.naming.NamingException {
        java.util.Collection selected = getLocalHome().findByUsername(username);
        ArrayList retval = new ArrayList(selected.size());
        for (Iterator i = selected.iterator(); i.hasNext(); ) {
            retval.add(((ispyb.server.data.interfaces.AdminActivityLocal) i.next()).getAdminActivityValue());
        }
        return retval;
    }

    /**
 * @ejb.interface-method
 * @ejb.facade-method
 * 
*/
    public java.util.Collection findByAction(java.lang.String action) throws javax.ejb.FinderException, javax.naming.NamingException {
        java.util.Collection selected = getLocalHome().findByAction(action);
        ArrayList retval = new ArrayList(selected.size());
        for (Iterator i = selected.iterator(); i.hasNext(); ) {
            retval.add(((ispyb.server.data.interfaces.AdminActivityLocal) i.next()).getAdminActivityValue());
        }
        return retval;
    }

    /**
 * @ejb.interface-method
 * @ejb.facade-method
 * 
*/
    public java.util.Collection findByComment(java.lang.String comment) throws javax.ejb.FinderException, javax.naming.NamingException {
        java.util.Collection selected = getLocalHome().findByComment(comment);
        ArrayList retval = new ArrayList(selected.size());
        for (Iterator i = selected.iterator(); i.hasNext(); ) {
            retval.add(((ispyb.server.data.interfaces.AdminActivityLocal) i.next()).getAdminActivityValue());
        }
        return retval;
    }

    /**
 * @ejb.interface-method
 * @ejb.facade-method
 * 
*/
    public java.util.Collection findByDateTime(java.sql.Timestamp dateTime) throws javax.ejb.FinderException, javax.naming.NamingException {
        java.util.Collection selected = getLocalHome().findByDateTime(dateTime);
        ArrayList retval = new ArrayList(selected.size());
        for (Iterator i = selected.iterator(); i.hasNext(); ) {
            retval.add(((ispyb.server.data.interfaces.AdminActivityLocal) i.next()).getAdminActivityValue());
        }
        return retval;
    }

    /**
 * @ejb.interface-method
 * @ejb.facade-method
 * 
*/
    public ispyb.server.data.interfaces.AdminActivityValue findByPrimaryKey(java.lang.Integer pk) throws javax.ejb.FinderException, javax.naming.NamingException {
        ispyb.server.data.interfaces.AdminActivityLocal selected = getLocalHome().findByPrimaryKey(pk);
        ispyb.server.data.interfaces.AdminActivityValue retval = selected.getAdminActivityValue();
        return retval;
    }

    /**
    * @ejb.interface-method
    * @ejb.facade-method invalidate="true"

    */
    public ispyb.server.data.interfaces.AdminActivityValue create(java.lang.String username) throws javax.ejb.CreateException, javax.naming.NamingException {
        return getLocalHome().create(username).getAdminActivityValue();
    }

    /**
    * @ejb.interface-method
    * @ejb.facade-method invalidate="true"

    */
    public ispyb.server.data.interfaces.AdminActivityValue create(java.lang.String username, java.lang.String action, java.lang.String comment, java.sql.Timestamp dateTime) throws javax.ejb.CreateException, javax.naming.NamingException {
        return getLocalHome().create(username, action, comment, dateTime).getAdminActivityValue();
    }

    /**
    * @ejb.interface-method
    * @ejb.facade-method invalidate="true"

    */
    public ispyb.server.data.interfaces.AdminActivityValue create(ispyb.server.data.interfaces.AdminActivityLightValue value) throws javax.ejb.CreateException, javax.naming.NamingException {
        return getLocalHome().create(value).getAdminActivityValue();
    }

    /**
    * @ejb.interface-method
    * @ejb.facade-method
    */
    public void update(AdminActivityLightValue newValue) throws javax.ejb.FinderException, javax.naming.NamingException {
        ispyb.server.data.interfaces.AdminActivityLocal selected = getLocalHome().findByPrimaryKey(newValue.getAdminActivityId());
        selected.setAdminActivityLightValue(newValue);
    }

    /**
	* @ejb.create-method
	* @ejb.permission unchecked="true"
	*/
    public void ejbCreate() throws javax.ejb.CreateException {
    }

    public void setSessionContext(javax.ejb.SessionContext ctx) {
        _ctx = ctx;
    }
}
