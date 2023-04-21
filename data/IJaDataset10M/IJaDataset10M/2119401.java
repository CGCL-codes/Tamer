package org.regilo.menu.editor.page;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.regilo.core.ui.RegiloCoreImages;
import org.regilo.core.ui.editors.RegiloEditor;
import org.regilo.core.ui.editors.pages.MasterDetailsBlock;
import org.regilo.menu.model.MenuItem;

public class MenuPageMaster extends MasterDetailsBlock {

    private TreeViewer viewer;

    private RegiloEditor editor;

    private Action addAction;

    private Action removeAction;

    public MenuPageMaster(RegiloEditor editor) {
        this.editor = editor;
        makeActions();
    }

    @Override
    protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
        section.setText("Menu");
        section.setDescription("View and manage site's menu");
        createSectionToolbar(section, toolkit);
        section.marginWidth = 10;
        section.marginHeight = 5;
        Composite client = toolkit.createComposite(section, SWT.WRAP);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        client.setLayout(layout);
        PatternFilter filter = new PatternFilter();
        FilteredTree filteredTree = new FilteredTree(client, SWT.BORDER, filter, true);
        viewer = filteredTree.getViewer();
        MenuManager popupMenuManager = new MenuManager();
        IMenuListener listener = new IMenuListener() {

            public void menuAboutToShow(IMenuManager mng) {
                fillContextMenu(mng);
            }
        };
        popupMenuManager.addMenuListener(listener);
        popupMenuManager.setRemoveAllWhenShown(true);
        Control control = viewer.getControl();
        Menu menu = popupMenuManager.createContextMenu(control);
        control.setMenu(menu);
        initializeDragAndDrop();
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 20;
        gd.widthHint = 100;
        gd.verticalSpan = 1;
        viewer.getControl().setLayoutData(gd);
        Composite buttonBar = toolkit.createComposite(client, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        buttonBar.setLayout(layout);
        section.setClient(client);
        final SectionPart spart = new SectionPart(section);
        managedForm.addPart(spart);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                managedForm.fireSelectionChanged(spart, event.getSelection());
                if (editor.getSite().getSelectionProvider() instanceof MultiPageSelectionProvider) {
                    MultiPageSelectionProvider provider = (MultiPageSelectionProvider) editor.getSite().getSelectionProvider();
                    provider.setSelection(event.getSelection());
                }
            }
        });
    }

    private void createSectionToolbar(Section section, FormToolkit toolkit) {
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        ToolBar toolbar = toolBarManager.createControl(section);
        final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
        toolbar.setCursor(handCursor);
        toolbar.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                if ((handCursor != null) && (handCursor.isDisposed() == false)) {
                    handCursor.dispose();
                }
            }
        });
        CommandContributionItemParameter saveContributionParameter = new CommandContributionItemParameter(editor.getSite(), null, "it.wellnet.easysitebox.menu.commands.saveMenu", CommandContributionItem.STYLE_PUSH);
        saveContributionParameter.icon = RegiloCoreImages.getInstance().DESC_UPDATE;
        CommandContributionItem saveMenu = new CommandContributionItem(saveContributionParameter);
        toolBarManager.add(saveMenu);
        toolBarManager.update(true);
        section.setTextClient(toolbar);
    }

    protected void fillContextMenu(IMenuManager manager) {
        ISelection selection = viewer.getSelection();
        IStructuredSelection ssel = (IStructuredSelection) selection;
        MenuItem item = (MenuItem) ssel.getFirstElement();
        if (!selection.isEmpty() && item instanceof MenuItem) {
            manager.add(addAction);
            if (item.getChildren().size() == 0 && item.getParent() != null) {
                manager.add(removeAction);
            }
        }
    }

    private void makeActions() {
        addAction = new Action("Add") {

            public void run() {
                handleAdd();
            }
        };
        removeAction = new Action("Remove") {

            public void run() {
                handleRemove();
            }
        };
    }

    protected void handleRemove() {
        ISelection selection = viewer.getSelection();
        IStructuredSelection ssel = (IStructuredSelection) selection;
        MenuItem item = (MenuItem) ssel.getFirstElement();
        if (item.getChildren().size() > 0) {
            return;
        } else if (item.getParent() == null) {
            return;
        } else {
            MenuItem parent = item.getParent();
            parent.getChildren().remove(item);
            viewer.refresh();
        }
    }

    protected void handleAdd() {
        ISelection selection = viewer.getSelection();
        IStructuredSelection ssel = (IStructuredSelection) selection;
        MenuItem item = (MenuItem) ssel.getFirstElement();
        MenuItem newItem = new MenuItem();
        newItem.setName("New menu");
        newItem.setPath("node");
        newItem.setParent(item);
        newItem.setPlid(newItem.getPlid());
        newItem.setEnabled(false);
        newItem.setMenu(item.getMenu());
        viewer.refresh();
    }

    @Override
    protected void applyLayoutData(SashForm sashForm) {
        super.applyLayoutData(sashForm);
        sashForm.setWeights(new int[] { 25, 75 });
    }

    @Override
    protected void createToolBarActions(IManagedForm managedForm) {
    }

    @Override
    protected void registerPages(DetailsPart detailsPart) {
        detailsPart.setPageProvider(new IDetailsPageProvider() {

            public IDetailsPage getPage(Object key) {
                if (key instanceof MenuItem) {
                    return new MenuPageDetail((MenuItem) key, editor, getMasterPage());
                }
                return null;
            }

            public Object getPageKey(Object object) {
                if (object instanceof MenuItem) {
                    return object;
                }
                return object.getClass();
            }
        });
    }

    public TreeViewer getViewer() {
        return viewer;
    }

    private MenuPageMaster getMasterPage() {
        return this;
    }

    protected void initializeDragAndDrop() {
        if (viewer == null) {
            return;
        }
        MenuDragAdapter menuDragAdapter = new MenuDragAdapter(viewer);
        MenuDropAdapter menuDropAdapter = new MenuDropAdapter(viewer);
        int dragOperations = DND.DROP_MOVE;
        viewer.addDragSupport(dragOperations, getDragTransfers(), menuDragAdapter);
        int dropOperations = dragOperations | DND.DROP_DEFAULT;
        viewer.addDropSupport(dropOperations, getDropTransfers(), menuDropAdapter);
    }

    /**
	 * @return
	 */
    protected Transfer[] getDragTransfers() {
        return new Transfer[] { ModelDataTransfer.getInstance() };
    }

    /**
	 * @return
	 */
    protected Transfer[] getDropTransfers() {
        return getDragTransfers();
    }

    public Object getCurrentSelection() {
        return ((IStructuredSelection) viewer.getSelection()).getFirstElement();
    }
}
