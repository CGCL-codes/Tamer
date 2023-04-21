package gwtm.client.ui.trees.explorer;

import gwtm.client.services.tm.virtual.AssociationVirtual;
import gwtm.client.ui.trees.*;
import gwtm.client.services.tm.virtual.TopicVirtual;
import net.mygwt.ui.client.widget.tree.TreeItem;

/**
 *
 * @author Yorgos
 */
public class ModelExpRoot extends ModelGeneral {

    /**
   * Creates a new instance of ModelExpRoot
   */
    public ModelExpRoot() {
    }

    public String toString() {
        return "Explorer";
    }

    public void refreshFromServer(final TreeItem treeItem) {
    }

    public ModelExpTopic showTopicOnRoot(TopicVirtual t) {
        ModelExpTopic m = findTopicOnRoot(t);
        if (m == null) {
            m = new ModelExpTopic(t);
            add(m);
        }
        return m;
    }

    public ModelExpAssociation showAssociationOnRoot(AssociationVirtual a) {
        ModelExpAssociation m = findAssociationOnRoot(a);
        if (m == null) {
            m = new ModelExpAssociation(a);
            add(m);
        }
        return m;
    }

    public ModelTmo getChildExpTmo(int n) {
        return (ModelTmo) getChild(n);
    }

    public ModelExpTopic findTopicOnRoot(TopicVirtual t) {
        String id = t.getID();
        for (int i = 0; i < getChildCount(); i++) {
            ModelTmo m = getChildExpTmo(i);
            if (m.getTopicMapObject().getID().equals(id)) return (ModelExpTopic) m;
        }
        return null;
    }

    public ModelExpAssociation findAssociationOnRoot(AssociationVirtual a) {
        String id = a.getID();
        for (int i = 0; i < getChildCount(); i++) {
            ModelTmo m = getChildExpTmo(i);
            if (m.getTopicMapObject().getID().equals(id)) return (ModelExpAssociation) m;
        }
        return null;
    }

    public void clear() {
        while (getChildCount() > 0) remove(0);
    }
}
