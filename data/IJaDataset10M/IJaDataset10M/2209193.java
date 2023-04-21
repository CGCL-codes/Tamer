package chanukah.ui.model;

import chanukah.pd.Entity;
import chanukah.pd.Group;
import chanukah.pd.Person;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * <p>TreeModel for the chanukah jtree. Adapter to the ProblemDomain classes</p>
 *
 * <p>$Id: ChanukahTreeModel.java,v 1.19 2004/02/26 22:39:09 phuber Exp $</p>
 *
 * @author Patrick Huber  | <a href="mailto:phuber@users.sf.net">phuber@users.sf.net</a>
 * @version $Revision: 1.19 $
 */
public class ChanukahTreeModel implements TreeModel, Observer {

    /** List of attached listeners */
    EventListenerList eventListenerList = new EventListenerList();

    /** Root Group */
    Group root = null;

    /**
	 * Create TreeModel with a given root. The root node must not contain any real data.
	 * @param root Root node.
	 */
    public ChanukahTreeModel(Group root) {
        this();
        this.root = root;
        this.root.addChangedObserver(this);
    }

    /**
	 * Default Constructor.
	 */
    private ChanukahTreeModel() {
        super();
    }

    /**
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
    public Object getChild(Object parent, int index) {
        if (parent.getClass() != Group.class) {
            return null;
        }
        return ((Group) parent).getEntities().get(index);
    }

    /**
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
    public int getChildCount(Object parent) {
        int count = 0;
        if (parent.getClass() == Group.class) {
            count = ((Group) parent).getEntities().size();
        } else if (parent.getClass() == Person.class) {
            count = 0;
        } else {
            count = 0;
        }
        return count;
    }

    /**
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
    public int getIndexOfChild(Object parent, Object child) {
        if ((parent == null) || (parent.getClass() != Group.class)) {
            return -1;
        }
        return ((Group) parent).getEntities().indexOf(child);
    }

    /**
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
    public boolean isLeaf(Object node) {
        return (node.getClass() == Person.class);
    }

    /**
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
    public Object getRoot() {
        return this.root;
    }

    /**
	 *
	 * @param parent
	 * @param child
	 * @return
	 */
    public ArrayList addEntity(Group parent, Entity child) {
        if (child.getClass() == Person.class) {
            return addPerson(parent, (Person) child);
        } else if (child.getClass() == Group.class) {
            return addGroup(parent, (Group) child);
        }
        return null;
    }

    /**
	 * @param parent
	 * @param child
	 */
    public ArrayList addGroup(Group parent, Group child) {
        parent.addEntity(child);
        int[] childIndices = new int[] { getIndexOfChild(parent, child) };
        Object[] children = new Object[] { child };
        TreeModelEvent e = new TreeModelEvent(this, parent.getPath().toArray(), childIndices, children);
        fireTreeNodesInserted(e);
        return child.getPath();
    }

    /**
	 * @param parent
	 * @param child
	 */
    public ArrayList addPerson(Group parent, Person child) {
        parent.addEntity(child);
        int[] childIndices = new int[] { getIndexOfChild(parent, child) };
        Object[] children = new Object[] { child };
        TreeModelEvent e = new TreeModelEvent(this, parent.getPath().toArray(), childIndices, children);
        fireTreeNodesInserted(e);
        return child.getPath();
    }

    /**
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
    public void addTreeModelListener(TreeModelListener l) {
        eventListenerList.add(l.getClass(), l);
    }

    /**
	 * @param parent
	 * @param child
	 */
    public void removeEntity(Group parent, Entity child) {
        if (child.getClass() == Person.class) {
            this.removePerson(parent, (Person) child);
        } else if (child.getClass() == Group.class) {
            this.removeGroup(parent, (Group) child);
        } else {
        }
    }

    /**
	 * @param parent
	 * @param child
	 */
    public void removeGroup(Group parent, Group child) {
        int childIndex = getIndexOfChild(parent, child);
        if (childIndex < 0) {
            System.err.println("Cannot remove group - seems to be the root");
            return;
        }
        int[] childIndices = new int[] { childIndex };
        Object[] children = new Object[] { child };
        TreeModelEvent e = new TreeModelEvent(this, parent.getPath().toArray(), childIndices, children);
        fireTreeNodesRemoved(e);
        parent.removeEntity(child);
    }

    /**
	 * @param parent
	 * @param child
	 */
    public void removePerson(Group parent, Person child) {
        int childIndex = getIndexOfChild(parent, child);
        if (childIndex < 0) {
            System.err.println("Cannot remove person - seems to be the root");
            return;
        }
        int[] childIndices = new int[] { childIndex };
        Object[] children = new Object[] { child };
        TreeModelEvent e = new TreeModelEvent(this, parent.getPath().toArray(), childIndices, children);
        fireTreeNodesRemoved(e);
        parent.removeEntity(child);
    }

    /**
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
    public void removeTreeModelListener(TreeModelListener l) {
        eventListenerList.remove(l.getClass(), l);
    }

    /**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
    public void update(Observable o, Object arg) {
        Entity changedEntity = (Entity) arg;
        TreeModelEvent event = new TreeModelEvent(changedEntity, changedEntity.getPath().toArray());
        fireTreeNodesChanged(event);
    }

    /**
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("ChanukahTreeModel::valueForPathChanged " + path.toString() + " " + newValue.toString());
    }

    /**
	 * @param e
	 */
    private void fireTreeNodesChanged(TreeModelEvent e) {
        Object[] listeners = eventListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
        }
    }

    /**
	 * @param e
	 */
    private void fireTreeNodesInserted(TreeModelEvent e) {
        Object[] listeners = eventListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
        }
    }

    /**
	 * @param e
	 */
    private void fireTreeNodesRemoved(TreeModelEvent e) {
        Object[] listeners = eventListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
        }
    }

    /**
	 * @param e
	 */
    private void fireTreeStructureChanged(TreeModelEvent e) {
        Object[] listeners = eventListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
        }
    }
}
