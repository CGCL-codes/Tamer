package gwtm.client.ui.trees.schema;

import gwtm.client.services.schema.SchemaServiceInvoker;
import gwtm.client.services.tm.Callback;
import gwtm.client.ui.trees.*;
import gwtm.client.services.tm.virtual.TopicVirtual;
import gwtm.client.services.tm.virtual.VTopicVirtuals;
import gwtm.client.ui.trees.hierarchy.ModelHieNameType;
import net.mygwt.ui.client.widget.tree.TreeItem;

/**
 *
 * @author Yorgos
 */
public class ModelSchemaNameTypes extends ModelGeneral {

    /** Creates a new instance of TreeItemTopic */
    public ModelSchemaNameTypes() {
    }

    public String toString() {
        return "Name Types (from schema)";
    }

    public void refreshFromServer(final TreeItem treeItem) {
        Callback.Ts ret = new Callback.Ts() {

            public void retTopics(VTopicVirtuals vt) {
                boolean bLeaf = treeItem.isLeaf();
                setChildren(vt);
                if (bLeaf) treeItem.setExpanded(true);
            }
        };
        SchemaServiceInvoker.getNameTypes(ret);
    }

    public void setChildren(VTopicVirtuals vt) {
        clearAll();
        for (int i = 0; i < vt.size(); i++) {
            TopicVirtual t = vt.getAt(i);
            ModelHieNameType m = new ModelHieNameType(t);
            add(m);
        }
    }
}
