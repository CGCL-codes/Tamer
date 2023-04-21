package org.specrunner.webdriver.actions.window;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.specrunner.context.IContext;
import org.specrunner.plugins.PluginException;
import org.specrunner.result.IResultSet;
import org.specrunner.result.Status;
import org.specrunner.util.UtilLog;
import org.specrunner.webdriver.AbstractPluginWindow;

public class PluginSize extends AbstractPluginWindow {

    @Override
    protected void doEnd(IContext context, IResultSet result, WebDriver client, Options options, Window window) throws PluginException {
        Dimension d = null;
        try {
            d = window.getSize();
        } catch (Exception e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        if (getWidth() == null && getHeight() == null) {
            throw new PluginException("PluginSize action requires at least one of attributes 'width' and/or 'height'.");
        }
        if (d != null) {
            window.setSize(new Dimension(getWidth() != null ? getWidth() : d.getWidth(), getHeight() != null ? getHeight() : d.getHeight()));
            result.addResult(Status.SUCCESS, context.peek());
        }
    }
}
