package net.simpleframework.my.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultHomeApplicationModule extends AbstractWebApplicationModule implements IHomeApplicationModule {

    @Override
    protected void putTables(final Map<Class<?>, Table> tables) {
        tables.put(HomeTabsBean.class, new Table("simple_my_home_tabs"));
        tables.put(HomePortalBean.class, new Table("simple_my_home_layout", true));
    }

    @Override
    public Collection<HomeTabsBean> getHomeTabs(final PageRequestResponse requestResponse) {
        final ArrayList<HomeTabsBean> al = new ArrayList<HomeTabsBean>();
        final IAccount account = AccountSession.getLogin(requestResponse.getSession());
        if (account != null) {
            final ExpressionValue ev = new ExpressionValue("userId=? order by defaulttab desc", new Object[] { account.getId() });
            final ITableEntityManager temgr = MyHomeUtils.getTableEntityManager(HomeTabsBean.class);
            final IQueryEntitySet<HomeTabsBean> qs = temgr.query(ev, HomeTabsBean.class);
            boolean defaulttab = false;
            HomeTabsBean homeTab;
            while ((homeTab = qs.next()) != null) {
                al.add(homeTab);
                if (homeTab.isDefaulttab()) {
                    defaulttab = true;
                }
            }
            if (!defaulttab) {
                homeTab = MyHomeUtils.insertHomeTab(account.getId(), LocaleI18n.getMessage("DefaultHomeApplicationModule.0"), true, null);
                al.add(0, homeTab);
            }
        }
        return al;
    }

    @Override
    public String getTabUrl(final PageRequestResponse requestResponse, final HomeTabsBean homeTab) {
        return null;
    }

    static final String deployName = "my/home";

    @Override
    public void init(final IInitializer initializer) {
        super.init(initializer);
        doInit(MyHomeUtils.class, deployName);
        PortalModuleRegistryFactory.regist(UserPortalModule.class, "my_user", LocaleI18n.getMessage("DefaultHomeApplicationModule.1"), LocaleI18n.getMessage("DefaultHomeApplicationModule.0"), MyHomeUtils.deployPath + "images/user.png", LocaleI18n.getMessage("DefaultHomeApplicationModule.2"));
        OrgUtils.um().addListener(new TableEntityAdapter() {

            @Override
            public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
                final Object[] values = dataObjectValue.getValues();
                final ITableEntityManager tabsmgr = MyHomeUtils.getTableEntityManager(HomeTabsBean.class);
                final String userid = DbUtils.getIdsSQLParam("userid", values.length);
                final ExpressionValue ev2 = new ExpressionValue("id in (select id from " + tabsmgr.getTablename() + " where " + userid + ")", values);
                MyHomeUtils.getTableEntityManager(HomePortalBean.class).delete(ev2);
                tabsmgr.delete(new ExpressionValue(userid, values));
            }
        });
    }

    @Override
    public String getApplicationText() {
        return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultHomeApplicationModule.0"));
    }
}
