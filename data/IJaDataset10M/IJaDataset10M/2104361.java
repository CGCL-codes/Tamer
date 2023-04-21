package org.jmesa.view.html.toolbar;

import org.jmesa.core.CoreContext;
import org.jmesa.limit.Limit;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class FilterItemRenderer extends AbstractItemRenderer {

    public FilterItemRenderer(ToolbarItem item, CoreContext coreContext) {
        setToolbarItem(item);
        setCoreContext(coreContext);
    }

    public String render() {
        Limit limit = getCoreContext().getLimit();
        ToolbarItem item = getToolbarItem();
        StringBuilder action = new StringBuilder("javascript:" + getOnInvokeActionJavaScript(limit, item));
        item.setAction(action.toString());
        return item.enabled();
    }
}
