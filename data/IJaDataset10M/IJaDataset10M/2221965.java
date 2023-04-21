package net.simpleframework.content.component.topicpager;

import java.util.Collection;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.AbstractMenuHandle;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PostsUserMenuHandle extends AbstractMenuHandle {

    @Override
    public Collection<MenuItem> getMenuItems(final ComponentParameter compParameter, final MenuItem menuItem) {
        if (menuItem != null) {
            return null;
        }
        final ComponentParameter nComponentParameter = TopicPagerUtils.getComponentParameter(compParameter);
        return ((ITopicPagerHandle) nComponentParameter.getComponentHandle()).getPostUserMenuItems(nComponentParameter, (MenuBean) compParameter.componentBean);
    }
}
