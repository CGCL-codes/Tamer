package org.specrunner.webdriver.assertions;

import org.specrunner.context.IContext;
import org.specrunner.plugins.PluginException;

/**
 * Check if body or a given element does not contains a text.
 * 
 * @author Thiago Santos
 * 
 */
public class PluginNotContains extends PluginContains {

    @Override
    protected boolean test(String content, String value) {
        return !content.contains(value);
    }

    @Override
    protected String getMessage(IContext context, String value) throws PluginException {
        return getFinder().resume(context) + " contains '" + value + "'.";
    }
}
