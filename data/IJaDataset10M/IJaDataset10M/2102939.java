package org.hibnet.lune.ui.view;

import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.hibnet.lune.core.Index;
import org.hibnet.lune.core.http.HTTPIndex;
import org.hibnet.lune.core.local.LocalFSIndex;
import org.hibnet.lune.ui.IndexInput;
import org.hibnet.lune.ui.LuneUIPlugin;
import org.hibnet.lune.ui.action.DeleteAction;
import org.hibnet.lune.ui.action.EditIndexInputAction;
import org.hibnet.lune.ui.action.GenericNewIndexInputAction;
import org.hibnet.lune.ui.action.NewIndexInputAction;
import org.hibnet.lune.ui.action.OpenIndexInputEditorAction;
import org.hibnet.lune.ui.editor.DocumentEditorPart;
import org.hibnet.lune.ui.editor.IndexEditorPart;
import org.hibnet.lune.ui.editor.QueryEditorPart;
import org.hibnet.lune.ui.editor.TermEditorPart;
import org.hibnet.lune.ui.plugin.ConfigurableBeanFactory;
import org.hibnet.lune.ui.plugin.ConfigurableBeanFactoryList;
import org.hibnet.lune.ui.plugin.index.IndexManager;

/**
 * View to manage the indexes and choose the index to use.
 */
public class IndexesView extends ViewPart {

    /** the index managers ID */
    public static final String ID = "org.hibnet.lune.ui.view.indexes";

    TreeViewer viewer;

    Action deleteAction;

    Action openQueryEditorAction;

    Action openDocumentBrowserAction;

    Action editIndex;

    Action openTermBrowserAction;

    Action openIndexEditorAction;

    private ArrayList<GenericNewIndexInputAction> newIndexActions;

    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.NONE | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        deleteAction = new DeleteAction(this);
        openQueryEditorAction = new OpenIndexInputEditorAction(this, QueryEditorPart.ID);
        openQueryEditorAction.setText("Search");
        openQueryEditorAction.setToolTipText("Open the query editor");
        openQueryEditorAction.setImageDescriptor(LuneUIPlugin.getDescriptor("search"));
        openDocumentBrowserAction = new OpenIndexInputEditorAction(this, DocumentEditorPart.ID);
        openDocumentBrowserAction.setText("Browse by doc");
        openDocumentBrowserAction.setToolTipText("Open the document bowser");
        openDocumentBrowserAction.setImageDescriptor(LuneUIPlugin.getDescriptor("document"));
        openTermBrowserAction = new OpenIndexInputEditorAction(this, TermEditorPart.ID);
        openTermBrowserAction.setText("Browse by term");
        openTermBrowserAction.setToolTipText("Open the term bowser");
        openTermBrowserAction.setImageDescriptor(LuneUIPlugin.getDescriptor("searchTerm"));
        openIndexEditorAction = new OpenIndexInputEditorAction(this, IndexEditorPart.ID);
        openIndexEditorAction.setText("Open");
        openIndexEditorAction.setToolTipText("Open the index");
        openIndexEditorAction.setImageDescriptor(LuneUIPlugin.getDescriptor("localIndex"));
        editIndex = new EditIndexInputAction();
        deleteAction.setEnabled(false);
        openQueryEditorAction.setEnabled(false);
        openDocumentBrowserAction.setEnabled(false);
        openTermBrowserAction.setEnabled(false);
        openIndexEditorAction.setEnabled(false);
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                if (openIndexEditorAction.isEnabled()) {
                    openIndexEditorAction.run();
                }
            }
        });
        viewer.setLabelProvider(new LabelProvider() {

            @Override
            public Image getImage(Object element) {
                Index index = ((IndexInput) element).getIndex();
                Image base = null;
                ImageDescriptor overlay = null;
                if (index.isInitialized()) {
                    overlay = LuneUIPlugin.getDescriptor("inUseOverlay");
                }
                if (index instanceof LocalFSIndex) {
                    base = LuneUIPlugin.getImage("localIndex");
                }
                if (index instanceof HTTPIndex) {
                    base = LuneUIPlugin.getImage("remoteIndex");
                }
                if (base == null) {
                    return null;
                }
                if (overlay != null) {
                    return new DecorationOverlayIcon(base, overlay, IDecoration.TOP_LEFT).createImage();
                }
                return base;
            }
        });
        viewer.setContentProvider(new ITreeContentProvider() {

            public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            }

            public void dispose() {
            }

            public Object[] getElements(Object inputElement) {
                return getChildren(inputElement);
            }

            public boolean hasChildren(Object element) {
                Object[] tmp = getChildren(element);
                return tmp != null && tmp.length != 0;
            }

            public Object getParent(Object element) {
                IndexManager indexManager = LuneUIPlugin.getIndexManager();
                if (element == indexManager) {
                    return null;
                }
                return indexManager;
            }

            public Object[] getChildren(Object parentElement) {
                IndexManager indexManager = LuneUIPlugin.getIndexManager();
                if (parentElement == indexManager) {
                    return indexManager.getIndexes().values().toArray();
                }
                return null;
            }
        });
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                refreshButtons(getSelection());
            }
        });
        IActionBars bars = getViewSite().getActionBars();
        bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
        viewer.getControl().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.character == SWT.DEL && event.stateMask == 0 && deleteAction.isEnabled()) {
                    deleteAction.run();
                }
            }
        });
        ConfigurableBeanFactoryList<IndexInput> factories = LuneUIPlugin.getIndexInputFactories();
        newIndexActions = new ArrayList<GenericNewIndexInputAction>();
        for (ConfigurableBeanFactory<IndexInput> factory : factories) {
            newIndexActions.add(new GenericNewIndexInputAction(factory));
        }
        IToolBarManager toolBarMgr = bars.getToolBarManager();
        IMenuManager barMenuManager = bars.getMenuManager();
        for (GenericNewIndexInputAction action : newIndexActions) {
            toolBarMgr.add(action);
            barMenuManager.add(action);
        }
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                Object elem = getSelection();
                if (elem instanceof IndexInput) {
                    mgr.add(openIndexEditorAction);
                    mgr.add(deleteAction);
                    mgr.add(editIndex);
                }
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        viewer.setInput(LuneUIPlugin.getIndexManager());
    }

    @Override
    public void setFocus() {
        viewer.getTree().setFocus();
    }

    public IndexInput getSelection() {
        return (IndexInput) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
    }

    public void refresh() {
        viewer.refresh();
        refreshButtons(getSelection());
    }

    void refreshButtons(IndexInput index) {
        deleteAction.setEnabled(index != null);
        openQueryEditorAction.setEnabled(index != null);
        openDocumentBrowserAction.setEnabled(index != null);
        openTermBrowserAction.setEnabled(index != null);
        openIndexEditorAction.setEnabled(index != null);
    }

    public void select(final IndexInput indexInput) {
        viewer.setSelection(new StructuredSelection(indexInput));
        refresh();
    }
}
