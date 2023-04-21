package ise.plugin.svn.pv;

import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.io.File;
import ise.plugin.svn.data.StatusData;
import ise.plugin.svn.data.SVNData;
import ise.plugin.svn.command.Status;

/**
 * This class lets ProjectViewer display an "overlay" icon in the PV tree
 * to show the status of a file like TortoiseSVN.
 */
public class SVNProvider {

    public static final int VC_STATE_NONE = 0;

    public static final int VC_STATE_LOCAL_MOD = 1;

    public static final int VC_STATE_LOCAL_ADD = 2;

    public static final int VC_STATE_LOCAL_RM = 3;

    public static final int VC_STATE_NEED_UPDATE = 4;

    public static final int VC_STATE_CONFLICT = 5;

    public static final int VC_STATE_DELETED = 6;

    public static final int VC_STATE_LOCKED = 7;

    public static final int VC_STATE_UNVERSIONED = 8;

    public static final int VC_STATE_NORMAL = 9;

    public static final Icon NORMAL_ICON = getIcon("ise/plugin/svn/gui/icons/normal.png");

    public static final Icon ADDED_ICON = getIcon("ise/plugin/svn/gui/icons/added.png");

    public static final Icon CONFLICT_ICON = getIcon("ise/plugin/svn/gui/icons/conflict.png");

    public static final Icon DELETED_ICON = getIcon("ise/plugin/svn/gui/icons/deleted.png");

    public static final Icon IGNORED_ICON = getIcon("ise/plugin/svn/gui/icons/ignored.png");

    public static final Icon LOCKED_ICON = getIcon("ise/plugin/svn/gui/icons/locked.png");

    public static final Icon MODIFIED_ICON = getIcon("ise/plugin/svn/gui/icons/modified.png");

    public static final Icon OUTOFDATE_ICON = getIcon("ise/plugin/svn/gui/icons/outofdate.png");

    public static final Icon READONLY_ICON = getIcon("ise/plugin/svn/gui/icons/readonly.png");

    public static final Icon UNVERSIONED_ICON = getIcon("ise/plugin/svn/gui/icons/unversioned.png");

    private static Icon getIcon(String name) {
        return new ImageIcon(SVNProvider.class.getClassLoader().getResource(name));
    }

    private static HashMap<String, CacheItem> cache = new HashMap<String, CacheItem>();

    class CacheItem {

        int state;

        long lastUpdate = System.currentTimeMillis();

        public CacheItem(int state) {
            this.state = state;
        }
    }

    private long refreshTime = 60 * 1000;

    public int getFileState(File f, String path) {
        CacheItem item = cache.get(path);
        if (item != null && item.lastUpdate > System.currentTimeMillis() - refreshTime) {
            return item.state;
        }
        SVNData data = new SVNData();
        List<String> paths = new ArrayList<String>();
        paths.add(path);
        data.setPaths(paths);
        data.setRecursive(false);
        data.setRemote(false);
        data.setOut(null);
        Status command = new Status();
        StatusData status = null;
        try {
            status = command.getStatus(data);
        } catch (Exception e) {
            e.printStackTrace();
            status = null;
        }
        if (status == null) {
            cache.put(path, new CacheItem(VC_STATE_NONE));
            return VC_STATE_NONE;
        }
        if (status.getAdded() != null) {
            cache.put(path, new CacheItem(VC_STATE_LOCAL_ADD));
            return VC_STATE_LOCAL_ADD;
        } else if (status.getConflicted() != null) {
            cache.put(path, new CacheItem(VC_STATE_CONFLICT));
            return VC_STATE_CONFLICT;
        } else if (status.getDeleted() != null) {
            cache.put(path, new CacheItem(VC_STATE_DELETED));
            return VC_STATE_DELETED;
        } else if (status.getLocked() != null) {
            cache.put(path, new CacheItem(VC_STATE_LOCKED));
            return VC_STATE_LOCKED;
        } else if (status.getMissing() != null) {
            cache.put(path, new CacheItem(VC_STATE_LOCAL_RM));
            return VC_STATE_LOCAL_RM;
        } else if (status.getModified() != null) {
            cache.put(path, new CacheItem(VC_STATE_LOCAL_MOD));
            return VC_STATE_LOCAL_MOD;
        } else if (status.getOutOfDate() != null) {
            cache.put(path, new CacheItem(VC_STATE_NEED_UPDATE));
            return VC_STATE_NEED_UPDATE;
        } else if (status.getUnversioned() != null) {
            cache.put(path, new CacheItem(VC_STATE_UNVERSIONED));
            return VC_STATE_UNVERSIONED;
        } else {
            cache.put(path, new CacheItem(VC_STATE_NORMAL));
            return VC_STATE_NORMAL;
        }
    }

    public Icon getIcon(int state) {
        switch(state) {
            case VC_STATE_LOCAL_MOD:
                return MODIFIED_ICON;
            case VC_STATE_LOCAL_ADD:
                return ADDED_ICON;
            case VC_STATE_LOCAL_RM:
                return DELETED_ICON;
            case VC_STATE_NEED_UPDATE:
                return OUTOFDATE_ICON;
            case VC_STATE_CONFLICT:
                return CONFLICT_ICON;
            case VC_STATE_NONE:
                return null;
            case VC_STATE_DELETED:
                return DELETED_ICON;
            case VC_STATE_LOCKED:
                return LOCKED_ICON;
            case VC_STATE_UNVERSIONED:
                return UNVERSIONED_ICON;
            case VC_STATE_NORMAL:
            default:
                return NORMAL_ICON;
        }
    }
}
