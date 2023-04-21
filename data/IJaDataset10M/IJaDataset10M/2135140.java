package ti.plato.logcontrol;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import ti.mcore.u.log.PlatoLogger;
import ti.plato.logcontrol.constants.Constants;

/**
 * 
 * A wrapper class for all the elements to be displayed in a page/tab in the log control gui.
 *
 */
public class PageModelItem {

    private static final PlatoLogger LOGGER = PlatoLogger.getLogger(PageModelItem.class);

    protected IPageModelItemState state = null;

    protected String treePath;

    private String pageName;

    protected String name;

    private final PageModelItem parent;

    private List<PageModelItem> children;

    public PageModelItem(String pageName, String name, PageModelItem parent) {
        this.pageName = pageName;
        this.name = name;
        this.parent = parent;
        if (parent != null) children = Collections.synchronizedList(new LinkedList<PageModelItem>());
        setTreePath(Constants.pathSeparator + name);
    }

    public IPageModelItemState getState() {
        return state;
    }

    public void setState(IPageModelItemState state) {
        this.state = state;
    }

    public String getTreePath() {
        return treePath;
    }

    private void setTreePath(String path) {
        treePath = path;
        Map<String, Map<String, IPageModelItemState>> checkedStateMap = LogControlPlugin.getWorkspaceSaveContainer().getCheckedStateMap();
        Map<String, IPageModelItemState> pageState = checkedStateMap.get(pageName);
        IPageModelItemState state = pageState.get(treePath);
        LOGGER.dbg("pageName=%s, state=%s", pageName, state);
        setState(state);
    }

    public final PageModelItem getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void addChild(PageModelItem child) {
        if (children == null) children = Collections.synchronizedList(new LinkedList<PageModelItem>());
        synchronized (children) {
            children.add(child);
            updateChildPath(child);
        }
    }

    public void updateChildPath(PageModelItem child) {
        child.setTreePath(getTreePath() + Constants.pathSeparator + child.getName());
        if (child.getChildren().length > 0) {
            PageModelItem[] children = child.getChildren();
            for (int i = 0; i < children.length; i++) child.updateChildPath(children[i]);
        }
    }

    public void removeChild(PageModelItem child) {
        if (children == null) return;
        synchronized (children) {
            children.remove(child);
        }
    }

    public void removeAll() {
        PageModelItem[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            removeChild(children[i]);
        }
    }

    public PageModelItem[] getChildren() {
        if (children == null) return new PageModelItem[0];
        synchronized (children) {
            return children.toArray(new PageModelItem[children.size()]);
        }
    }

    public PageModelItem[] getSortedChildren() {
        if (children == null) return new PageModelItem[0];
        synchronized (children) {
            Vector<PageModelItem> v = new Vector<PageModelItem>();
            PageModelItem[] elements = children.toArray(new PageModelItem[children.size()]);
            for (int i = 0; i < elements.length; i++) v.add(elements[i]);
            Collections.sort(v, new LogControlComparator());
            return v.toArray(new PageModelItem[v.size()]);
        }
    }

    public boolean hasChildren() {
        if (children == null) return false;
        synchronized (children) {
            return children.size() > 0;
        }
    }

    public PageModelItem contains(String treePath) {
        PageModelItem ret = null;
        if (getTreePath().equals(treePath)) {
            ret = this;
        } else {
            if (getChildren().length > 0) {
                PageModelItem[] kids = getChildren();
                for (int i = 0; i < kids.length; i++) {
                    ret = kids[i].contains(treePath);
                    if (ret != null) break;
                }
            }
        }
        return ret;
    }

    public String toString() {
        return "name= " + name + " treePath= " + treePath;
    }
}
