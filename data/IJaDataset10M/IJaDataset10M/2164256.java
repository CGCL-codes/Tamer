package mou.admin.dbbrowser;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * @author pb
 */
public class IntrospectorTreeNode extends DefaultMutableTreeNode {

    private Map map;

    private Collection collection;

    /**
	 * @param userObject
	 */
    public IntrospectorTreeNode(Object userObject) {
        super(userObject);
        if (userObject instanceof Map.Entry) {
            userObject = ((Map.Entry) userObject).getValue();
        }
        if (userObject instanceof Map) {
            map = (Map) userObject;
            for (Iterator iter = ((Map) userObject).entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                this.add(new IntrospectorTreeNode(entry));
            }
        } else if (userObject instanceof Collection) {
            collection = (Collection) userObject;
            for (Iterator iter = ((Collection) userObject).iterator(); iter.hasNext(); ) {
                add(new IntrospectorTreeNode(iter.next()));
            }
        }
    }

    public void remove(MutableTreeNode aChild) {
        super.remove(aChild);
        if (map != null) map.remove(((IntrospectorTreeNode) aChild).getEntryKey()); else if (collection != null) collection.remove(((IntrospectorTreeNode) aChild).getUserObject());
    }

    public void remove(int childIndex) {
        MutableTreeNode child = (MutableTreeNode) getChildAt(childIndex);
        super.remove(childIndex);
        if (map != null) map.remove(((IntrospectorTreeNode) child).getEntryKey()); else if (collection != null) collection.remove(((IntrospectorTreeNode) child).getUserObject());
    }

    /**
	 * Liefert Key aus der unterliegenden Map, mit dem diese Node addressiert wird
	 * 
	 * @return
	 */
    public Object getEntryKey() {
        if (getUserObject() instanceof Map.Entry) return ((Map.Entry) getUserObject()).getKey();
        return null;
    }

    /**
	 * Liefert Value aus der unterliegenden Map, mit dem diese Node verkn�ft ist
	 * 
	 * @return
	 */
    public Object getEntryValue() {
        if (getUserObject() instanceof Map.Entry) return ((Map.Entry) getUserObject()).getValue();
        return getUserObject();
    }
}
