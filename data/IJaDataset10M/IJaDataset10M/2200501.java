package jp.co.chojo.db.ado;

import java.util.List;
import jp.co.chojo.db.bean.GroupItem;
import jp.co.chojo.db.bean.GroupRoot;
import jp.co.chojo.db.bean.HumanBaseInfo;

public interface GroupRootDao {

    public void insertGroup(GroupRoot group);

    public void insertItem(int groupId, GroupItem item);

    public void updateGroupName(int groupId, String groupTitle);

    public void updateItemName(int itemId, String itemTitle);

    public GroupRoot getGroup(int groupid);

    public List<GroupRoot> getAllGroup();

    public void addHuman(int groupItemId, int humanId);

    public void deleteAllHuman(int groupItemId);

    public List<HumanBaseInfo> getManByGroupItemId(int itemId);
}
