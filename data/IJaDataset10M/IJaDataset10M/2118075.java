package es.ehrflex.client.main.archetype;

import java.util.List;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import es.ehrflex.client.main.archetype.model.ArchetypeDataModel;
import es.ehrflex.client.main.event.EHRflexEvent;
import es.ehrflex.client.main.event.EHRflexEventType;
import es.ehrflex.client.main.model.FileNode;
import es.ehrflex.client.main.model.FolderNode;
import es.ehrflex.client.main.system.EHRflexTreeStore;
import es.ehrflex.client.service.EHRflexSecuredService;
import es.ehrflex.client.service.GWTServiceFactory;

/**
 * Tree that contains all available archetypes for a user.
 * 
 * @author Anton Brass
 * @version 1.0, 12.05.2009
 */
public class ArchetypeTree extends Tree {

    private static ArchetypeTree instance;

    public TreeLoader loader;

    public static ArchetypeTree getInstance() {
        if (ArchetypeTree.instance == null) {
            ArchetypeTree.instance = new ArchetypeTree();
        }
        return ArchetypeTree.instance;
    }

    private ArchetypeTree() {
        RpcProxy<List<FileNode>> proxy = new RpcProxy<List<FileNode>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<List<FileNode>> callback) {
                String sharedSecret = Cookies.getCookie(EHRflexSecuredService.SHARED_SECRET_KEY);
                GWTServiceFactory.getArchetypeGWTService().getFolderChildren((FileNode) loadConfig, sharedSecret, callback);
            }
        };
        this.loader = new BaseTreeLoader(proxy) {

            @Override
            public boolean hasChildren(ModelData parent) {
                return parent instanceof FolderNode;
            }
        };
        TreeStore<FileNode> store = new EHRflexTreeStore<FileNode>(loader);
        TreeBinder<FileNode> binder = new TreeBinder<FileNode>(this, store);
        binder.setDisplayProperty("name");
        binder.setIconProvider(new ModelStringProvider<FileNode>() {

            public String getStringValue(FileNode model, String property) {
                return model.getNodeType().getIcon();
            }
        });
        loader.load(null);
        Listener<TreeEvent> treeListener = new Listener<TreeEvent>() {

            public void handleEvent(TreeEvent event) {
                TreeItem selectedNode = ArchetypeTree.this.getSelectedItem();
                FileNode selectedNodeData = null;
                FolderNode selectedNodeParentData = null;
                if (selectedNode != null) {
                    selectedNodeData = (FileNode) ArchetypeTree.this.getSelectedItem().getModel();
                }
                ArchetypeDataModel archetype = new ArchetypeDataModel();
                archetype.setArchetypeId(selectedNodeData.getName());
                Dispatcher.get().dispatch(new EHRflexEvent(EHRflexEventType.ARCHETYPE_VIEW, archetype));
            }
        };
        this.addListener(Events.OnDoubleClick, treeListener);
    }
}
