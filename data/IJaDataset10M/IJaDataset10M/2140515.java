package net.simpleframework.applets.invite;

import net.simpleframework.core.IInitializer;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractWebApplicationModule;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultInviteApplicationModule extends AbstractWebApplicationModule implements IInviteApplicationModule {

    static final String deployName = "applets/invite";

    @Override
    public void init(final IInitializer initializer) {
        super.init(initializer);
        doInit(InviteUtils.class, deployName);
    }

    @Override
    public String getApplicationText() {
        return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultInviteApplicationModule.0"));
    }
}
