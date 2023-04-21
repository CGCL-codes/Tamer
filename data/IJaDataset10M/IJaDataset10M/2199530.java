package org.eaasyst.tables.apps;

import javax.servlet.http.HttpServletRequest;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.apps.ApplicationBase;
import org.eaasyst.eaa.data.OptionListSourceFactory;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.transients.UserMessage;
import org.eaasyst.eaa.utils.StringUtils;
import org.eaasyst.tables.data.impl.TableOptionListSourceFactory;

/**
 * <p>This application reloads the table-based option list source
 * objects.</p>
 *
 * @version 2.1.4
 * @author Jeff Chilton, Peter Ruan
 */
public class TableOptionsReload extends ApplicationBase {

    /**
	 * <p>Constructs a new "TableOptionsReload" object.</p>
	 *
	 * @since EaasyStreet 2.1.4
	 */
    public TableOptionsReload() {
        className = StringUtils.computeClassName(getClass());
    }

    /**
	 * <p>Called by the <code>Controller</code> whenever the application is
	 * invoked through either navigation or an application request.</p>
	 *
	 * @param req the <code>HttpServletRequest</code> object
	 * @since Eaasy Street 2.1.4
	 */
    public void initialize(HttpServletRequest req) {
        EaasyStreet.logTrace("[In] TableOptionsReload.initialize()");
        EaasyStreet.removeContextAttribute("table.data.cache.map");
        OptionListSourceFactory factory = new TableOptionListSourceFactory();
        factory.reload();
        EaasyStreet.setUserMessage(req, new UserMessage("message.information.reloaded"));
        req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
        EaasyStreet.logTrace("[Out] TableOptionsReload.initialize()");
    }
}
