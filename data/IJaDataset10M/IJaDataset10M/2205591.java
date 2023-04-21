package passreminder;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.action.Action;
import passreminder.model.Group;
import passreminder.model.GroupList;
import passreminder.model.Item;

public class ModelManager {

    public static final long USER_ID_GROUP_START = 1024;

    public static final long SYSTEM_ID_GROUP_END = 512;

    public static final Group groupData = new Group(SYSTEM_ID_GROUP_END, "Data", "");

    public static final Group groupDataSecond = new Group(SYSTEM_ID_GROUP_END, "Data", "");

    public static final Group groupTrash = new Group(1, "Trash", "");

    public static final Group groupSearch = new Group(2, "Search", "");

    private static ModelManager me;

    static {
        me = new ModelManager();
    }

    public static ModelManager getInstance() {
        return me;
    }

    public Group newGroup(String name) {
        Group newGroup = new Group();
        newGroup.id = DBManager.nextId();
        newGroup.name = name;
        newGroup.timestamp = new Date().getTime();
        return newGroup;
    }

    public void addGroup(Group parent, Group child) {
        if (parent != null) {
            child.parent = parent;
            parent.children.add(child);
        }
    }

    public boolean removeGroup(Group group) {
        if (group.isUser()) {
            Group parent = group.parent;
            parent.children.remove(group);
            for (int i = DBManager.getInstance().iListMain.size() - 1; i >= 0; i--) {
                Item item = DBManager.getInstance().iListMain.get(i);
                if (item.groupId == group.id) DBManager.getInstance().iListMain.remove(i);
            }
            group = null;
            ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(true);
            return true;
        }
        return false;
    }

    public void moveGroupToGroup(List selectedGroupList, Group group) {
        for (int i = 0; i < selectedGroupList.size(); i++) {
            Group g = (Group) (selectedGroupList.get(i));
            g.parent.children.remove(g);
            g.parent = group;
            group.children.add(g);
        }
        if (selectedGroupList.size() > 0) ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(true);
    }

    public void moveItemToGroup(List selectedItemList, Group group) {
        Iterator ite = selectedItemList.iterator();
        while (ite.hasNext()) {
            Item item = (Item) ite.next();
            item.groupId = group.id;
        }
        if (selectedItemList.size() > 0) ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(true);
    }

    public boolean renameGroup(Group g, String name) {
        boolean mod = name.equals(g.name);
        if (!mod && name.trim().length() > 0 && g.isUser()) {
            g.name = name;
            ((Action) PassReminder.getInstance().actionManager.get("save")).setEnabled(true);
            return true;
        }
        return false;
    }

    public Group toGroup(GroupList list, long id, Group defGroup) {
        Group group = list.getById(id);
        return group == null ? defGroup : group;
    }

    public Group toGroup(long id) {
        Group group = DBManager.getInstance().gListMain.getById(id);
        return group == null ? ModelManager.groupData : group;
    }

    public Group toGroup(String name) {
        Group group = DBManager.getInstance().gListMain.getByName(name);
        return group;
    }

    public Group[] flatGroupList() {
        return flatGroupList(DBManager.getInstance().gListMain);
    }

    public Group[] flatGroupList(GroupList glist) {
        Group dataGroup = null;
        for (int i = 0; i < glist.size(); i++) {
            if (glist.get(i).id == ModelManager.groupData.id) {
                dataGroup = glist.get(i);
                break;
            }
        }
        ArrayList gFinalList = new ArrayList();
        gFinalList.add(dataGroup);
        flatGroupListSub(gFinalList, dataGroup, 1);
        Group[] groupList = (Group[]) (gFinalList.toArray(new Group[gFinalList.size()]));
        return groupList;
    }

    private void flatGroupListSub(ArrayList gFinalList, Group dataGroup, int level) {
        boolean again = true;
        while (again) {
            GroupList kids = dataGroup.children;
            if (kids == null) break;
            for (int i = 0; i < kids.size(); i++) {
                kids.get(i).level = level;
                gFinalList.add(kids.get(i));
                flatGroupListSub(gFinalList, kids.get(i), kids.get(i).level + 1);
            }
            break;
        }
    }

    public String filenameFromElement(String icon) {
        try {
            File file = new File(UIManager.USER_ICON_FOLDER + "/" + icon);
            return file.exists() ? file.getName() : null;
        } catch (Exception e) {
        }
        return null;
    }

    public String filename2IconElement(String filename) {
        try {
            return filename.substring(filename.indexOf(UIManager.USER_ICON_FOLDER));
        } catch (Exception e) {
        }
        return null;
    }

    public String folderTree2String() {
        return "";
    }
}
