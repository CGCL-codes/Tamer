package net.simpleframework.web.page.component.base.ajaxrequest;

import javax.servlet.ServletContext;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AjaxRequestRegistry extends AbstractComponentRegistry {

    public static final String ajaxRequest = "ajaxRequest";

    public AjaxRequestRegistry(final ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public String getComponentName() {
        return ajaxRequest;
    }

    @Override
    protected Class<AjaxRequestBean> getBeanClass() {
        return AjaxRequestBean.class;
    }

    @Override
    protected Class<? extends AbstractComponentRender> getRenderClass() {
        return AjaxRequestRender.class;
    }

    @Override
    protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
        return AjaxRequestResourceProvider.class;
    }
}
