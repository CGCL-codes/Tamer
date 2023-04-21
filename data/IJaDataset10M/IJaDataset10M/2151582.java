package gwtm.client.ui.trees.explorer;

import com.google.gwt.user.client.Window;
import gwtm.client.MainEntryPoint;
import gwtm.client.services.tm.Callback;
import gwtm.client.services.tm.virtual.AssociationVirtual;
import gwtm.client.services.tm.virtual.RoleVirtual;
import gwtm.client.ui.frameworktest.GwtmStatics;
import gwtm.client.ui.trees.*;
import gwtm.client.services.tm.virtual.TopicMapVirtual;
import gwtm.client.services.tm.virtual.VRoleVirtuals;
import gwtm.client.ui.props.DlgRole;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.ShellListenerAdapter;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.tree.TreeItem;

/**
 *
 * @author Yorgos
 */
public class ModelExpAssociation extends ModelAssociation {

    /**
   * Creates a new instance of TreeItemExpTopic
   */
    public ModelExpAssociation() {
    }

    public ModelExpAssociation(AssociationVirtual a) {
        super(a);
    }

    public TopicMapVirtual getTopicMap() {
        return GwtmStatics.getInstance().getTopicMap();
    }

    private void resetData() {
        clear();
        if (getTopicMap() == null) return;
    }

    private void clear() {
    }

    public void doDefaultAction(final TreeItem treeItem) {
        refreshFromServer(treeItem);
    }

    public void refreshFromServer(final TreeItem treeItem) {
        super.refreshFromServer(treeItem);
        getAssociation().getRoles(new Callback.Rs() {

            public void retRoles(VRoleVirtuals vr) {
                boolean bLeaf = treeItem.isLeaf();
                setChildren(vr);
                if (bLeaf) treeItem.setExpanded(true);
            }
        });
    }

    public void setChildren(VRoleVirtuals vr) {
        clearAll();
        for (int i = 0; i < vr.size(); i++) {
            RoleVirtual r = vr.getAt(i);
            ModelExpRole m = new ModelExpRole(r);
            add(m);
        }
    }

    public void attachPlayer(final TreeItem item) {
        refreshFromServer(item);
        DlgRole d = new DlgRole(getAssociation());
        ShellListenerAdapter listener = new ShellListenerAdapter() {

            public void shellClosed(BaseEvent be) {
                DlgRole _d = (DlgRole) be.widget;
                Button btn = _d.getButtonPressed();
                if (btn == null) return;
                if (btn.getText().equals("OK")) {
                    _d.createGetRole(new Callback.R() {

                        public void retRole(RoleVirtual r) {
                            attachRole(r);
                            MainEntryPoint.getInstance().setDerty(true);
                        }
                    });
                }
            }
        };
        d.addShellListener(listener);
        d.open();
    }

    public void attachRole(RoleVirtual r) {
        ModelExpRole m = new ModelExpRole(r);
        add(m);
        TreeExplorer tree = MainEntryPoint.getInstance().getTabFolderCenter().getTabExplorer().getTree();
        TreeItem newItem = (TreeItem) tree.getViewer().findItem(m);
        tree.setSelection(newItem);
        m.refreshFromServer(newItem);
    }
}
