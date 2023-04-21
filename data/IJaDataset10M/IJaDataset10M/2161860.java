package net.simpleframework.applets.notification.component.window;

import java.util.Collection;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.window.WindowRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageWindowResourceProvider extends AbstractComponentResourceProvider {

    public MessageWindowResourceProvider(final IComponentRegistry componentRegistry) {
        super(componentRegistry);
    }

    @Override
    public String[] getDependentComponents(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
        return new String[] { WindowRegistry.window };
    }

    @Override
    public String[] getCssPath(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
        return new String[] { getCssSkin(requestResponse, "message_window.css") };
    }

    private static final String[] JAVASCRIPT_PATH = new String[] { "/js/message_window.js" };

    @Override
    public String[] getJavascriptPath(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
        return JAVASCRIPT_PATH;
    }
}
