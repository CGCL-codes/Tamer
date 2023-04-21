package net.simpleframework.web.page.component.ui.picshow;

import net.simpleframework.web.page.component.AbstractComponentHtmlRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;

public class PicShowRender extends AbstractComponentHtmlRender {

    public PicShowRender(final IComponentRegistry componentRegistry) {
        super(componentRegistry);
    }

    @Override
    protected String getRelativePath(ComponentParameter compParameter) {
        final StringBuilder sb = new StringBuilder();
        sb.append("/jsp/picShow.jsp?").append(PicShowUtils.BEAN_ID);
        sb.append("=").append(compParameter.componentBean.hashId());
        return sb.toString();
    }
}
