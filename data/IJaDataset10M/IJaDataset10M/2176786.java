package net.simpleframework.web.page.component.ui.tabs;

import java.util.Collection;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTabsHandle extends AbstractComponentHandle implements ITabsHandle {

    @Override
    public Collection<TabItem> getTabItems(final ComponentParameter compParameter) {
        return null;
    }
}
