package jreceiver.client.mgr.struts;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import jreceiver.client.common.RoleAuthBean;
import jreceiver.client.common.struts.JRecAction;
import jreceiver.client.common.struts.Messages;
import jreceiver.common.rec.driver.Driver;
import jreceiver.common.rec.driver.DriverRec;
import jreceiver.common.rec.security.User;
import jreceiver.common.rpc.Drivers;
import jreceiver.common.rpc.GeneralSettings;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rpc.RpcFactory;
import jreceiver.common.rpc.ScannerSettings;
import jreceiver.util.HelperServlet;

/**
 * Handle incoming requests on "config.do" to update the settings for
 * an app or driver or the server.
 * <P>
 * This is a wrapper around the business logic. Its purpose is to
 * translate the HttpServletRequest to the business logic, which
 * if significant, should be processed in a separate class.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.9 $ $Date: 2002/09/24 20:51:35 $
 */
public final class ConfigAction extends JRecAction {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (log.isDebugEnabled()) log.debug("execute\n" + HelperServlet.dumpSessionAttribs(req.getSession()) + "\n");
        if (isCancelled(req)) {
            log.debug("user hit cancel button");
            form.reset(mapping, req);
            return mapping.findForward(FORWARD_CANCEL);
        }
        ActionErrors errors = new ActionErrors();
        String forward_uri = load((ConfigForm) form, req, resp, errors);
        if (!errors.empty()) saveErrors(req, errors);
        return mapping.findForward(forward_uri);
    }

    /**
    * initialize the form fields with data from persistent storage
    */
    protected String load(ConfigForm form, HttpServletRequest req, HttpServletResponse resp, ActionErrors errors) {
        log.debug("load");
        try {
            Messages msgs = Messages.getInstance();
            User user = getUser(req);
            Drivers drv_rpc = RpcFactory.newDrivers(user);
            Vector drivers = drv_rpc.getRecsForSettings(null, null, 0, Driver.NO_LIMIT);
            HttpSession session = req.getSession();
            RoleAuthBean bean = (RoleAuthBean) session.getAttribute(RoleAuthBean.ROLE_AUTH_KEY);
            if (bean.isAuthorized(ScannerSettings.HANDLER_NAME, ScannerSettings.STORE_MAP)) drivers.add(0, new DriverRec(ScannerSettings.SCANNER_SETTINGS_ID, msgs.get(req.getLocale(), ScannerSettings.SCANNER_SETTINGS_DESC)));
            if (bean.isAuthorized(GeneralSettings.HANDLER_NAME, GeneralSettings.STORE_MAP)) drivers.add(0, new DriverRec(GeneralSettings.GENERAL_SETTING_ID, msgs.get(req.getLocale(), GeneralSettings.GENERAL_SETTING_DESC)));
            form.setDrivers(drivers);
        } catch (ServletException e) {
            log.warn("unable to load settings into form", e);
            reportLoadError(errors, e);
        } catch (RpcException e) {
            log.warn("unable to load settings into form", e);
            reportLoadError(errors, e);
        }
        return FORWARD_CONTINUE;
    }

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(ConfigAction.class);
}
