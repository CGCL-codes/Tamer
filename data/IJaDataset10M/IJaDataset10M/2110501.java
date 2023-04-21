package net.simpleframework.organization.component.register;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserRegisterBean extends AbstractContainerBean {

    private String closeAction;

    private String termsUrl;

    public UserRegisterBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
        super(componentRegistry, pageDocument, element);
    }

    @Override
    protected Class<? extends IComponentHandle> getDefaultHandleClass() {
        return DefaultUserRegisterHandle.class;
    }

    public String getCloseAction() {
        return closeAction;
    }

    public void setCloseAction(final String closeAction) {
        this.closeAction = closeAction;
    }

    public String getTermsUrl() {
        return termsUrl;
    }

    public void setTermsUrl(final String termsUrl) {
        this.termsUrl = termsUrl;
    }
}
