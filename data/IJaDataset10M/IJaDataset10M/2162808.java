package artofillusion.ui;

import artofillusion.*;
import artofillusion.animation.*;
import artofillusion.object.*;
import java.util.*;

/** This class represents an object in the tree of objects in a scene. */
public class ObjectTreeElement extends TreeElement {

    protected ObjectInfo info;

    public ObjectTreeElement(ObjectInfo info, TreeList tree) {
        this(info, null, tree, true);
    }

    public ObjectTreeElement(ObjectInfo info, TreeElement parent, TreeList tree, boolean addChildren) {
        this.info = info;
        this.parent = parent;
        this.tree = tree;
        children = new Vector();
        if (addChildren) for (int i = 0; i < info.children.length; i++) children.addElement(new ObjectTreeElement(info.children[i], this, tree, true));
    }

    public String getLabel() {
        return info.name;
    }

    public boolean canAcceptAsParent(TreeElement el) {
        if (el == null) return true;
        if (!(el instanceof ObjectTreeElement)) return false;
        ObjectInfo i = ((ObjectTreeElement) el).info;
        while (i != null) {
            if (i == info) return false;
            i = i.parent;
        }
        return true;
    }

    public void addChild(TreeElement el, int position) {
        children.insertElementAt(el, position);
        el.parent = this;
        if (el.getObject() instanceof ObjectInfo) {
            if (tree.undo != null) tree.undo.addCommandAtBeginning(UndoRecord.REMOVE_FROM_GROUP, new Object[] { info, el.getObject() });
            info.addChild((ObjectInfo) el.getObject(), position);
        } else if (el.getObject() instanceof Track) {
            Track tr = (Track) el.getObject();
            info.addTrack(tr, position);
            tr.setParent(info);
        }
    }

    public void removeChild(Object object) {
        TreeElement el = null;
        int pos;
        for (pos = 0; pos < children.size(); pos++) {
            el = (TreeElement) children.elementAt(pos);
            if (el.getObject() == object) break;
        }
        if (pos == children.size()) {
            for (int i = 0; i < children.size(); i++) ((TreeElement) children.elementAt(i)).removeChild(object);
            return;
        }
        el.parent = null;
        children.removeElementAt(pos);
        if (object instanceof Track) {
            info.removeTrack((Track) object);
            return;
        }
        info.removeChild((ObjectInfo) object);
        if (tree.undo != null) tree.undo.addCommandAtBeginning(UndoRecord.ADD_TO_GROUP, new Object[] { info, object, new Integer(pos) });
    }

    public Object getObject() {
        return info;
    }

    public boolean isGray() {
        return !info.visible;
    }

    public void addTracks() {
        for (int i = 0; i < info.tracks.length; i++) {
            TreeElement el = new TrackTreeElement(info.tracks[i], this, tree);
            children.insertElementAt(el, i);
        }
    }
}
