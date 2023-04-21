package net.simpleframework.my.friends;

import java.util.ArrayList;
import java.util.Map;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import org.springframework.dao.DuplicateKeyException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FriendsAction extends AbstractAjaxRequestHandle {

    public IForward addFriend(final ComponentParameter compParameter) {
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) {
                try {
                    final String toid = compParameter.getRequestParameter("af_toid");
                    if (FriendsUtils.isFriend(compParameter, toid)) {
                        json.put("error", LocaleI18n.getMessage("FriendsAction.2"));
                    } else {
                        FriendsUtils.addFriendsRequest(compParameter, toid, compParameter.getRequestParameter("af_messageText"), compParameter.getRequestParameter("af_groupid"));
                        json.put("ok", LocaleI18n.getMessage("FriendsAction.1"));
                    }
                } catch (final DuplicateKeyException e) {
                    json.put("error", LocaleI18n.getMessage("FriendsAction.0"));
                }
            }
        });
    }

    public IForward deleteFriendsRequest(final ComponentParameter compParameter) {
        FriendsUtils.deleteFriendsRequest(compParameter, compParameter.getRequestParameter("rid"));
        return null;
    }

    public IForward doFriendsRequest(final ComponentParameter compParameter) {
        FriendsUtils.setFriendsRequest(compParameter, compParameter.getRequestParameter("rid"), ConvertUtils.toEnum(ERequestStatus.class, compParameter.getRequestParameter("status")));
        return null;
    }

    public IForward deleteFriend(final ComponentParameter compParameter) {
        FriendsUtils.deleteFriend(compParameter, compParameter.getRequestParameter("fid"));
        return null;
    }

    public IForward dropFriend(final ComponentParameter compParameter) {
        final String[] drags = StringUtils.split(compParameter.getRequestParameter("drag"));
        if (drags == null || drags.length == 0) {
            return null;
        }
        final ITableEntityManager fg_mgr = FriendsUtils.getTableEntityManager(FriendsGroup.class);
        final FriendsGroup fg = fg_mgr.queryForObjectById(compParameter.getRequestParameter("drop"), FriendsGroup.class);
        if (fg == null) {
            return null;
        }
        final ITableEntityManager f_mgr = FriendsUtils.getTableEntityManager(Friends.class);
        final ArrayList<Friends> f_al = new ArrayList<Friends>();
        final ArrayList<FriendsGroup> fg_al = new ArrayList<FriendsGroup>();
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) {
                for (final String drag : drags) {
                    final Friends f = f_mgr.queryForObjectById(drag, Friends.class);
                    if (f == null) {
                        continue;
                    }
                    final FriendsGroup fg2 = fg_mgr.queryForObjectById(f.getGroupId(), FriendsGroup.class);
                    if (fg2 != null) {
                        fg2.setFriends(fg2.getFriends() - 1);
                        fg_al.add(fg2);
                    }
                    f.setGroupId(fg.getId());
                    fg.setFriends(fg.getFriends() + 1);
                    fg_al.add(fg);
                    f_al.add(f);
                }
                f_mgr.update(new String[] { "groupId" }, f_al.toArray());
                fg_mgr.update(new String[] { "friends" }, fg_al.toArray());
                json.put("ok", Boolean.TRUE);
            }
        });
    }
}
