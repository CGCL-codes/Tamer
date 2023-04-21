package net.simpleframework.my.friends;

import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.userpager.UserPagerBean;
import net.simpleframework.organization.component.userselect.DefaultUserSelectHandle;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyFriendsSelectedHandle extends DefaultUserSelectHandle {

    @Override
    public IDataObjectQuery<? extends IUser> getUsers(final ComponentParameter compParameter, final UserPagerBean userPager) {
        final IAccount login = AccountSession.getLogin(compParameter.getSession());
        if (login == null) {
            return null;
        }
        final StringBuilder sql = new StringBuilder();
        sql.append("select t.* from ").append(OrgUtils.um().tblname());
        sql.append(" t right join ").append(FriendsUtils.getTableEntityManager(Friends.class).getTablename());
        sql.append(" a on a.friendid=t.id where a.userid=?");
        final String user_name = compParameter.getRequestParameter(OrgUtils.um().getUserTextParameterName());
        if (StringUtils.hasText(user_name) && !user_name.equals(LocaleI18n.getMessage("user_select.3"))) {
            sql.append(" and (t.name like '%").append(user_name).append("%' or t.text like '%").append(user_name).append("%')");
        }
        return OrgUtils.um().query(new SQLValue(sql.toString(), new Object[] { login.getId() }));
    }
}
