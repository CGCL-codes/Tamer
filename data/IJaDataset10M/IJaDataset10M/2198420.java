package org.specrunner.browser.assertions;

import java.util.List;
import org.specrunner.browser.AbstractPluginFind;
import org.specrunner.browser.IFinder;
import org.specrunner.context.IContext;
import org.specrunner.plugins.PluginException;
import org.specrunner.result.IResultSet;
import org.specrunner.result.Status;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Check if an id, name, value, xpath, etc is present.
 * 
 * @author Thiago Santos
 * 
 */
public class PluginPresent extends AbstractPluginFind implements IAssertion {

    private Integer count;

    private Integer min;

    private Integer max;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    @Override
    protected void doEnd(IContext context, IResultSet result, WebClient client, SgmlPage page) throws PluginException {
        IFinder finder = getFinderInstance(context);
        List<?> list = finder.find(context, result, client, page);
        int failure = 0;
        if (getCount() != null) {
            if (list.size() != getCount()) {
                result.addResult(Status.FAILURE, context.peek(), new PluginException("The expected count of elements was '" + count + "', but '" + list.size() + "' was received."));
                failure++;
            }
        } else {
            if (list.isEmpty()) {
                result.addResult(Status.FAILURE, context.peek(), new PluginException("Element not found."));
                failure++;
            }
        }
        if (getMin() != null) {
            if (list.size() < getMin()) {
                result.addResult(Status.FAILURE, context.peek(), new PluginException("The expected minimum of elements was '" + getMin() + "', but '" + list.size() + "' was received."));
                failure++;
            }
        }
        if (getMax() != null) {
            if (list.size() > getMax()) {
                result.addResult(Status.FAILURE, context.peek(), new PluginException("The expected maximum of elements was '" + getMax() + "', but '" + list.size() + "' was received."));
                failure++;
            }
        }
        if (failure == 0) {
            result.addResult(Status.SUCCESS, context.peek());
        }
    }

    @Override
    protected void process(IContext context, IResultSet result, WebClient client, SgmlPage page, HtmlElement[] elements) throws PluginException {
    }
}
