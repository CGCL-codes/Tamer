package jreceiver.client.mgr.struts;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import jreceiver.client.common.struts.JRecEditAction;
import jreceiver.client.common.struts.JRecEditForm;
import jreceiver.common.JRecException;
import jreceiver.common.rec.driver.DriverBindingKey;
import jreceiver.common.rec.driver.DriverBindingRec;
import jreceiver.common.rec.driver.DriverBinding;
import jreceiver.common.rec.driver.Driver;
import jreceiver.common.rec.security.User;
import jreceiver.common.rpc.DriverBindings;
import jreceiver.common.rpc.Drivers;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rpc.RpcFactory;
import jreceiver.util.HelperServlet;

/**
 * Handle incoming requests on "driverbindEdit.do" to update a single record
 * in DriverBindings table
 * <P>
 * This is a wrapper around the business logic. Its purpose is to
 * translate the HttpServletRequest to the business logic, which
 * if significant, should be processed in a separate class.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.4 $ $Date: 2002/10/05 07:10:20 $
 */
public final class DriverBindingEditAction extends JRecEditAction {

    /**
    * load any special stuff in to the form, usually init'd from param list
    */
    public void onLoad(User user, JRecEditForm edit_form, HttpServletRequest req, ActionErrors errors) throws JRecException {
        DriverBindingEditForm form = (DriverBindingEditForm) edit_form;
        try {
            int drv_master_id = HelperServlet.getIntParam(req, "driverMasterId", 0);
            int drv_slave_id = HelperServlet.getIntParam(req, "driverSlaveId", 0);
            DriverBindingKey drvbind_key = new DriverBindingKey(drv_master_id, drv_slave_id);
            form.setDriverMasterId(drvbind_key.getDriverMasterId());
            form.setDriverSlaveId(drvbind_key.getDriverSlaveId());
            Drivers drv_rpc = RpcFactory.newDrivers(user);
            Vector keys = drv_rpc.getKeys(null, 0, Driver.NO_LIMIT);
            Vector masters = drv_rpc.getRecs(keys, null, null);
            Vector slaves = drv_rpc.getRecs(keys, null, null);
            form.setMasters(masters);
            form.setSlaves(slaves);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem loading data", e);
        }
    }

    /**
     * store any form contents persistently
     */
    public void onSave(User user, JRecEditForm edit_form, HttpSession session, ActionErrors errors) throws JRecException {
        DriverBindingEditForm form = (DriverBindingEditForm) edit_form;
        try {
            DriverBindingKey drvbind_key = new DriverBindingKey(form.getDriverMasterId(), form.getDriverSlaveId());
            DriverBindings drvbind_rpc = RpcFactory.newDriverBindings(user);
            DriverBinding rec = new DriverBindingRec(drvbind_key);
            drvbind_rpc.storeRec(rec);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem saving data", e);
        }
    }
}
