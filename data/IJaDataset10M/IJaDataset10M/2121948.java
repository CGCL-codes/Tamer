package net.simpleframework.organization.component.userpager;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerBean;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserPagerBean extends AbstractTablePagerBean {

    public UserPagerBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
        super(componentRegistry, pageDocument, element);
    }

    @Override
    public String getDataPath() {
        return getResourceHomePath() + "/jsp/userpager.jsp";
    }

    @Override
    protected Class<? extends IComponentHandle> getDefaultHandleClass() {
        return DefaultUserPagerHandle.class;
    }
}
