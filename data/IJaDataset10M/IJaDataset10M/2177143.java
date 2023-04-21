package org.specrunner.webdriver.actions;

import nu.xom.Node;
import org.openqa.selenium.WebDriver;
import org.specrunner.context.IContext;
import org.specrunner.plugins.PluginException;
import org.specrunner.result.IResultSet;
import org.specrunner.result.Status;
import org.specrunner.util.UtilLog;
import org.specrunner.util.string.IStringProvider;
import org.specrunner.webdriver.AbstractPluginUrlAware;

/**
 * Sets initial page of a given driver. Once set, relative references can be
 * made.
 * 
 * @author Thiago Santos
 * 
 */
public class PluginBrowserIn extends AbstractPluginUrlAware implements IAction {

    public static final String BROWSER_IN = "browserIn";

    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    protected void doEnd(IContext context, IResultSet result, WebDriver client) throws PluginException {
        Node node = context.getNode();
        String tmp = getBrowserName();
        String u = null;
        if (provider == null) {
            u = getUrl() != null ? getUrl() : node.getValue();
        } else {
            try {
                if (UtilLog.LOG.isInfoEnabled()) {
                    UtilLog.LOG.info("Trying to recover base URL from provider '" + provider + "'.");
                }
                u = getBaseFromProvider(context);
                if (UtilLog.LOG.isInfoEnabled()) {
                    UtilLog.LOG.info("Base url provided is '" + u + "'.");
                }
            } catch (Exception e) {
                if (UtilLog.LOG.isTraceEnabled()) {
                    UtilLog.LOG.trace(e.getMessage(), e);
                }
                result.addResult(Status.FAILURE, context.newBlock(node, this), e);
                return;
            }
        }
        context.saveGlobal(getBaseForBrowser(tmp), u);
        if (UtilLog.LOG.isInfoEnabled()) {
            UtilLog.LOG.info("Browser named '" + tmp + "' base url set to '" + u + "'.");
        }
        result.addResult(Status.SUCCESS, context.newBlock(node, this));
    }

    protected String getBaseFromProvider(IContext context) throws Exception {
        return ((IStringProvider) Class.forName(provider).newInstance()).newString(context);
    }

    public static String getBaseForBrowser(String browserName) {
        return browserName + "_" + BROWSER_IN;
    }
}
