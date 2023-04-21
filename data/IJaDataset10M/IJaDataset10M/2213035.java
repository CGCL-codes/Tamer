package aga.jitracker.client.views.dir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import aga.jitracker.client.ObjectFactory;
import aga.jitracker.client.services.CallbackAdapter;
import aga.jitracker.client.services.Action;
import aga.jitracker.client.services.CrudService.*;
import aga.jitracker.client.services.CrudServiceAsync;
import aga.jitracker.client.ui.CompositeTree;
import aga.jitracker.client.ui.Node;
import aga.jitracker.client.ui.NodeTextCell;
import aga.jitracker.client.ui.SelectionHelper;
import aga.jitracker.client.views.CrudView;
import aga.jitracker.shared.domain.Directory;
import aga.jitracker.shared.domain.DirectoryEntry;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

public class DirEntriesTreeView extends CrudView<Node<DirectoryEntry>> {

    private static final ProvidesKey<Node<DirectoryEntry>> NodeKeyProvider = new ProvidesKey<Node<DirectoryEntry>>() {

        @Override
        public Object getKey(Node<DirectoryEntry> item) {
            return item.getData() != null ? item.getData().getId() : null;
        }
    };

    private static final NodeTextCell Cell = new NodeTextCell<DirectoryEntry>() {

        @Override
        public String getNodeText(Node<DirectoryEntry> node) {
            return node.getData().getName();
        }
    };

    private final CrudServiceAsync svc = ObjectFactory.INSTANCE.getCrudService();

    private final Directory dir;

    private CompositeTree<DirectoryEntry> tree;

    public DirEntriesTreeView(Directory dir) {
        this.dir = dir;
        SingleSelectionModel sel = new SingleSelectionModel(NodeKeyProvider);
        tree = new CompositeTree(Cell, NodeKeyProvider);
        tree.getTree().setSelectionModel(sel);
        setSelectionHelper(new SelectionHelper(sel));
        setDataWidget(tree);
        addButtons();
    }

    private void addButtons() {
        addButton("Add", new ClickHandler() {

            public void onClick(ClickEvent event) {
                doAdd();
            }
        });
        Button addc = addButton("Add Child", new ClickHandler() {

            public void onClick(ClickEvent event) {
                doAddChild();
            }
        });
        Button ed = addButton("Edit", new ClickHandler() {

            public void onClick(ClickEvent event) {
                Node<DirectoryEntry> node = getSelectedNode();
                doEdit(node, node.getData());
            }
        });
        addDeleteButton();
        addRefreshButton();
        enableOnSelection(addc, ed);
    }

    private Node<DirectoryEntry> getSelectedNode() {
        return getSelectionHelper().getSelectedObject();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        doRefresh();
    }

    void doAdd() {
        DirectoryEntry de = new DirectoryEntry();
        de.setDirectoryId(dir.getId());
        Node<DirectoryEntry> sel = getSelectedNode();
        if (sel != null) {
            de.setParentId(sel.getData().getParentId());
            doEdit(sel.getParent(), de);
        } else doEdit(tree.getTree().getRoot(), de);
    }

    void doAddChild() {
        Node<DirectoryEntry> sel = getSelectedNode();
        DirectoryEntry de = new DirectoryEntry();
        de.setDirectoryId(dir.getId());
        de.setParentId(sel.getData().getId());
        doEdit(sel, de);
    }

    protected void doEdit(final Node<DirectoryEntry> parentNode, final DirectoryEntry object) {
        final DirEntryEditDialog dlg = new DirEntryEditDialog(object);
        dlg.center();
        dlg.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                if (dlg.isDialogResultOK()) doRefresh(parentNode);
            }
        });
    }

    @Override
    public void doRefresh() {
        doRefresh(null);
    }

    public void doRefresh(final Node<DirectoryEntry> node) {
        svc.execute(new GetDirEntries(dir.getId()), new CallbackAdapter<Action<List<DirectoryEntry>>>() {

            @Override
            public void processResponce(ServiceResponce responce) {
                super.processResponce(responce);
                if (responce.result != null) {
                    updateTree(tree.getTree().getRoot(), responce.result.getResult());
                }
            }
        });
    }

    @Override
    public void doDelete() {
        final Node<DirectoryEntry> sel = getSelectedNode();
        Long[] keys = new Long[] { sel.getData().getId() };
        svc.execute(new DeleteDBObjects(DirectoryEntry.CLAZZ, keys), new CallbackAdapter() {

            @Override
            public void processResponce(ServiceResponce responce) {
                super.processResponce(responce);
                if (responce.isOK()) doRefresh(sel.getParent());
            }
        });
    }

    private void updateTree(Node<DirectoryEntry> parent, List<DirectoryEntry> items) {
        Long parentId = parent.getData() != null ? parent.getData().getId() : null;
        HashMap<Long, List<DirectoryEntry>> map = buildMap(items);
        HashSet expanedKeys = tree.getTree().saveExpandedState(parent);
        addNodes(parent, parentId, map);
        tree.getTree().restoreExpandedState(parent, expanedKeys);
        tree.getTree().refresh(parent);
    }

    private void addNodes(Node<DirectoryEntry> parent, Long parentId, HashMap<Long, List<DirectoryEntry>> map) {
        parent.clear();
        List<DirectoryEntry> list = map.get(parentId);
        if (list != null) {
            for (DirectoryEntry de : list) {
                Node n = tree.getTree().createNode(parent, de);
                addNodes(n, de.getId(), map);
            }
        }
    }

    private HashMap<Long, List<DirectoryEntry>> buildMap(List<DirectoryEntry> items) {
        HashMap<Long, List<DirectoryEntry>> map = new HashMap<Long, List<DirectoryEntry>>();
        for (DirectoryEntry e : items) {
            List<DirectoryEntry> list = map.get(e.getParentId());
            if (list == null) {
                list = new ArrayList<DirectoryEntry>();
                map.put(e.getParentId(), list);
            }
            list.add(e);
        }
        return map;
    }
}
