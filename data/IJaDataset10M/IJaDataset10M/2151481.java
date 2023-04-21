package com.oroad.stxx.chain.legacy;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.chain.Constants;
import org.apache.struts.config.ModuleConfig;
import com.oroad.stxx.chain.StxxConstants;
import com.oroad.stxx.plugin.RequestProcessorHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.web.WebContext;

/**
 * <p>Create (if necessary) and cache a form bean for this request.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2005/02/12 23:28:40 $
 */
public class Initialize implements Command {

    private String actionServletKey = Constants.ACTION_SERVLET_KEY;

    private String moduleConfigKey = Constants.MODULE_CONFIG_KEY;

    private String requestProcessorHelperKey = StxxConstants.REQUEST_PROCESSOR_HELPER_KEY;

    private static final Log log = LogFactory.getLog(Initialize.class);

    /**
     * <p>Return the context attribute key under which the
     * <code>RequestProcessorHelper</code> for the currently selected application
     * action is stored.</p>
     */
    public String getRequestProcessorHelperKey() {
        return (this.requestProcessorHelperKey);
    }

    /**
     * <p>Set the context attribute key under which the
     * <code>RequestProcessorHelper</code> for the currently selected application
     * action is stored.</p>
     *
     * @param requestProcessorHelperKey The new context attribute key
     */
    public void setRequestProcessorHelperKey(String requestProcessorHelperKey) {
        this.requestProcessorHelperKey = requestProcessorHelperKey;
    }

    /**
     * <p>Return the context attribute key under which the
     * <code>ActionServlet</code> for the currently selected application
     * action is stored.</p>
     */
    public String getActionServletKey() {
        return (this.actionServletKey);
    }

    /**
     * <p>Set the context attribute key under which the
     * <code>ActionServlet</code> for the currently selected application
     * action is stored.</p>
     *
     * @param actionServletKey The new context attribute key
     */
    public void setActionServletKey(String actionServletKey) {
        this.actionServletKey = actionServletKey;
    }

    /**
     * <p>Return the context attribute key under which the
     * <code>ModuleConfig</code> for the currently selected application
     * module is stored.</p>
     */
    public String getModuleConfigKey() {
        return (this.moduleConfigKey);
    }

    /**
     * <p>Set the context attribute key under which the
     * <code>ModuleConfig</code> for the currently selected application
     * module is stored.</p>
     *
     * @param moduleConfigKey The new context attribute key
     */
    public void setModuleConfigKey(String moduleConfigKey) {
        this.moduleConfigKey = moduleConfigKey;
    }

    /**
     * <p>Initialize Stxx.</p>
     *
     * @param context The <code>Context</code> for the current request
     *
     * @return <code>false</code> so that processing continues
     */
    public boolean execute(Context context) throws Exception {
        WebContext wcontext = (WebContext) context;
        ModuleConfig moduleConfig = (ModuleConfig) wcontext.get(getModuleConfigKey());
        String key = moduleConfig.getPrefix() + getRequestProcessorHelperKey();
        RequestProcessorHelper helper = (RequestProcessorHelper) wcontext.getApplicationScope().get(key);
        if (helper == null) {
            ActionServlet servlet = (ActionServlet) wcontext.get(getActionServletKey());
            helper = new RequestProcessorHelper(servlet, moduleConfig);
            wcontext.getApplicationScope().put(key, helper);
        }
        wcontext.put(getRequestProcessorHelperKey(), helper);
        return (false);
    }
}
