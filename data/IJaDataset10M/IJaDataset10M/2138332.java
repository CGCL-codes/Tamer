package projecttree;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public abstract class NodeModel implements INodeModel {

    protected NodeModel parent;

    protected String name;

    protected int type;

    protected IProjectTreeListener listener = ProjectTreeListener.getInstance();

    protected List items;

    public static final int TYPE_ROOT = 0;

    public static final int TYPE_FOLDER = 1;

    public static final int TYPE_LUA = 2;

    public static final int TYPE_XML = 3;

    public static final int TYPE_LIBRARY = 4;

    public static final int TYPE_LIBRARY_SUBNODE = 5;

    public static final int TYPE_OTHER = 5;

    public static final int TYPE_PROJECT = 6;

    protected NodeModel(String name, int type) {
        this.name = name;
        this.type = type;
        items = new ArrayList();
    }

    protected void fireAdd(Object added) {
        listener.add(new ProjectTreeEvent(added));
    }

    protected void fireRemove(Object removed) {
        listener.remove(new ProjectTreeEvent(removed));
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeModel getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void addListener(IProjectTreeListener listener) {
        this.listener = listener;
    }

    public NodeModel() {
    }

    public void removeListener(IProjectTreeListener listener) {
        if (this.listener.equals(listener)) {
            this.listener = ProjectTreeListener.getInstance();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void add(NodeModel model) {
        model.parent = this;
        items.add(model);
        fireAdd(model);
    }

    public void remove(NodeModel model) {
        if (model.hasChildren()) {
            Vector children = new Vector(model.getChildren());
            for (int i = 0; i < children.size(); i++) {
                NodeModel child = (NodeModel) children.get(i);
                model.remove(child);
            }
        }
        items.remove(model);
        fireRemove(model);
    }

    public List getChildren() {
        return items;
    }

    public int getItemCount() {
        return items.size();
    }

    public boolean hasChildren() {
        return (items.size() > 0);
    }
}
