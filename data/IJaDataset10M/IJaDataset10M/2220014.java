package net.confex.views;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.confex.Constants;
import net.confex.action.AddCompositeFormViewNodeAction;
import net.confex.action.AddConfexNodeAction;
import net.confex.action.AddExecTreeNodeAction;
import net.confex.action.AddFolderNodeAction;
import net.confex.action.AddTreeNodeAction;
import net.confex.action.AddUrlTreeNodeAction;
import net.confex.action.AddVarsNodeAction;
import net.confex.action.CopyAction;
import net.confex.action.CopyFileAction;
import net.confex.action.CopyWithoutChildAction;
import net.confex.action.CutAction;
import net.confex.action.DeleteAction;
import net.confex.action.PasteAction;
import net.confex.action.PasteFileAction;
import net.confex.action.PropertyClipboardViewAction;
import net.confex.action.RunAction;
import net.confex.action.RunAsincAction;
import net.confex.action.RunAsincBatchAction;
import net.confex.action.RunBatchAction;
import net.confex.action.SaveAsConfexViewAction;
import net.confex.action.SaveConfexViewAction;
import net.confex.application.ConfexPlugin;
import net.confex.html.AddHtmlCompositeAction;
import net.confex.html.AddHtmlTextTreeNodeAction;
import net.confex.html.AddJava2HtmlNodeAction;
import net.confex.translations.Logger;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IClipboardView;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.ITreeNodeState;
import net.confex.utils.ImageResource;
import net.confex.utils.Utils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.PluginDropAdapter;
import org.eclipse.ui.part.PluginTransfer;

/**
 * ��� � ������� ������������ confex-������
 * 
 *  
 * ���� ������� �� ��������� Eclipse. ��� ����������� ����� ��
 * ��� ������ � ���� ������� ���������. 
 * 
 * ��� ����� ����� ������ ������� �� ��������.
 *  
 * 
 * @author Roman_Eremeev
 *
 */
public class NavigationView extends ConfexView implements IStateObserver, IClipboardView {

    public static final String ID = "net.confex.view.navigationView";

    public static final String DEFAULT_CONFIG_FILE = Constants.DEFAULT_CONFEX_FILE;

    private TreeViewer treeViewer;

    private ConfigTree config_tree = new ConfigTree();

    public void setConfigTree(ConfigTree config_tree) {
        this.config_tree = config_tree;
    }

    public ConfigTree getConfigTree() {
        return config_tree;
    }

    public void update(Object object) {
        if (getTreeViewer() != null) {
            getTreeViewer().refresh(object);
        }
    }

    public void setDirty(boolean dirty) {
        config_tree.setDirty(dirty);
        if (dirty && !getTitle().startsWith("*")) {
            setPartName("*" + getTitle());
            return;
        }
        if (!dirty && getTitle().startsWith("*")) {
            setPartName(getTitle().substring(1));
            return;
        }
    }

    Clipboard clipboard;

    private static NavigationView current_drag_source = null;

    private CopyAction copyAction = null;

    private CopyWithoutChildAction copyWithoutChildAction = null;

    private CutAction cutAction = null;

    private PasteAction pasteAction = null;

    private CopyFileAction copyFileAction = null;

    private PasteFileAction pasteFileAction = null;

    private PropertyClipboardViewAction propertyAction = null;

    private DeleteAction deleteAction = null;

    private SaveConfexViewAction saveAction = null;

    private SaveAsConfexViewAction saveAsAction = null;

    private static final String TAG_FILE_NAME = "file_name";

    private static final String TAG_EXPANDED = "expanded";

    private static final String TAG_ELEMENT = "element";

    private static final String TAG_PATH = "path";

    private static final String TAG_SELECTION = "selection";

    private static final String TAG_RUNNING = "running";

    private static final String TAG_RUN_OBJECT_KEY = "run_object_key";

    private IMemento memento;

    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;
    }

    public void saveState(IMemento memento) {
        memento.putString(TAG_FILE_NAME, config_tree.getFileName());
        Object expandedElements[] = treeViewer.getVisibleExpandedElements();
        if (expandedElements.length > 0) {
            IMemento expandedMem = memento.createChild(TAG_EXPANDED);
            for (int i = 0; i < expandedElements.length; i++) {
                if (expandedElements[i] instanceof ITreeNode) {
                    IMemento elementMem = expandedMem.createChild(TAG_ELEMENT);
                    elementMem.putString(TAG_PATH, ((ITreeNode) expandedElements[i]).getStringKey());
                }
            }
        }
        Object elements[] = ((IStructuredSelection) treeViewer.getSelection()).toArray();
        if (elements.length > 0) {
            IMemento selectionMem = memento.createChild(TAG_SELECTION);
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] instanceof ITreeNode) {
                    IMemento elementMem = selectionMem.createChild(TAG_ELEMENT);
                    elementMem.putString(TAG_PATH, ((ITreeNode) elements[i]).getStringKey());
                }
            }
        }
        getConfigTree().fillRunningStateList();
        List<ITreeNodeState> running_list = getConfigTree().getStateList();
        if (running_list.size() > 0) {
            IMemento runningMem = memento.createChild(TAG_RUNNING);
            for (Iterator<ITreeNodeState> iterator = running_list.iterator(); iterator.hasNext(); ) {
                Object object = iterator.next();
                IMemento elementMem = runningMem.createChild(TAG_ELEMENT);
                elementMem.putString(TAG_RUN_OBJECT_KEY, ((ITreeNode) object).getStringKey());
            }
        }
    }

    /**
     * Restores the state of the receiver to the state described in the specified memento.
     *
     * @param memento the memento
     * @since 2.0
     */
    protected void restoreState(IMemento memento) {
        TreeViewer viewer = getTreeViewer();
        getConfigTree().fillMomenoHashMap();
        IMemento childMem = memento.getChild(TAG_EXPANDED);
        if (childMem != null) {
            ArrayList<Object> elements = new ArrayList<Object>();
            IMemento[] elementMem = childMem.getChildren(TAG_ELEMENT);
            for (int i = 0; i < elementMem.length; i++) {
                Object element = getConfigTree().findMember(elementMem[i].getString(TAG_PATH));
                if (element != null) {
                    elements.add(element);
                }
            }
            viewer.setExpandedElements(elements.toArray());
        }
        childMem = memento.getChild(TAG_SELECTION);
        if (childMem != null) {
            ArrayList<Object> elements = new ArrayList<Object>();
            IMemento[] elementMem = childMem.getChildren(TAG_ELEMENT);
            for (int i = 0; i < elementMem.length; i++) {
                Object element = getConfigTree().findMember(elementMem[i].getString(TAG_PATH));
                if (element != null) {
                    elements.add(element);
                }
            }
            viewer.setSelection(new StructuredSelection(elements));
        }
        childMem = memento.getChild(TAG_RUNNING);
        if (childMem != null) {
            IMemento[] elementMem = childMem.getChildren(TAG_ELEMENT);
            for (int i = 0; i < elementMem.length; i++) {
                Object element = getConfigTree().findMember(elementMem[i].getString(TAG_RUN_OBJECT_KEY));
                if (element instanceof ITreeNode) {
                    ((ITreeNode) element).run(this);
                    System.out.println(" Run " + ((ITreeNode) element).getName());
                }
            }
        }
        getTreeViewer().refresh();
    }

    public void refreshNode(ITreeNode node) {
        getTreeViewer().refresh(node);
    }

    class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            if (child instanceof ITreeNode) {
                return ((ITreeNode) child).getParent();
            }
            return null;
        }

        public Object[] getChildren(Object parent) {
            if (parent instanceof ITreeNode) {
                return ((ITreeNode) parent).getChildren();
            }
            return new Object[0];
        }

        public boolean hasChildren(Object parent) {
            if (parent instanceof ITreeNode) return ((ITreeNode) parent).hasChildren();
            return false;
        }
    }

    boolean edit_mode = ConfexPlugin.getDefault().isEnableEditMode();

    /**
	 * ������� ��������� ���� 
	 * 
	 * 
	 * @author Roman_Eremeev
	 *
	 */
    class ViewLabelProvider extends LabelProvider {

        public String getText(Object obj) {
            if (obj instanceof ITreeNode) {
                return ((ITreeNode) obj).toString(edit_mode);
            }
            return obj.toString();
        }

        public Image getImage(Object obj) {
            String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
            Image image = null;
            if (obj instanceof ITreeNode) {
                imageKey = ((ITreeNode) obj).getIconFileName();
                ImageDescriptor image_descr = ImageResource.getImageDescriptor(imageKey);
                if (image_descr == ImageDescriptor.getMissingImageDescriptor()) {
                    File icon_file = new File(imageKey);
                    String url_str;
                    if (icon_file.exists()) {
                        url_str = "file:\\" + icon_file.getAbsolutePath();
                    } else {
                        url_str = "file:\\" + getConfigTree().getConfexDir();
                        if (!url_str.endsWith(File.separator)) {
                            url_str += File.separator + imageKey;
                        } else {
                            url_str += imageKey;
                        }
                    }
                    try {
                        URL iconURL = new URL(url_str);
                        ImageResource.createFromFullURL(imageKey, iconURL);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }
                image = ImageResource.getImage(imageKey);
            }
            if (image == null) image = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
            return image;
        }
    }

    /**
	 * Select confex file
	 * @return
	 */
    private String selectFile(boolean flag_new, String[] extensions, String extension) {
        String file = null;
        {
            FileDialog fileDialog = new FileDialog(this.getSite().getShell());
            if (flag_new) {
                fileDialog.setText(Translator.getString("DILAOG_TITLE_NEW_CONFEXFILE"));
            } else {
                fileDialog.setText(Translator.getString("DILAOG_TITLE_OPEN_CONFEXFILE"));
            }
            fileDialog.setFilterExtensions(extensions);
            file = fileDialog.open();
            if (file == null) {
                System.err.println(Translator.getString("MSG_ERR_FILE_NOT_SELECTED"));
                return null;
            }
            if (file.indexOf('.') == -1) {
                file += "." + extension;
            }
            if (flag_new && new File(file).exists()) {
                if (!Logger.questionDialog(getSite().getShell(), "MESSAGE_BOX_TITLE_OVERWRITE", "QUEST_OVERWRITE_FILE", "")) {
                    return null;
                }
            }
        }
        return file;
    }

    /**
	 * Select name for confex copy text file (*.copy.txt)
	 * @param flag_new
	 * @return
	 */
    protected String selectCopyFile(boolean flag_new) {
        return selectFile(flag_new, Constants.CONFEX_COPY_FILE_EXTENSIONS, Constants.DEFAULT_COPY_FILE_EXTENSION);
    }

    /**
	 * Select confex file
	 * @return
	 */
    protected String selectInputFile(boolean flag_new) {
        return selectFile(flag_new, Constants.CONFEX_EXTENSIONS, Constants.DEFAULT_CONFEX_FILE_EXTENSION);
    }

    /**
     * We will set up a dummy model to initialize tree heararchy. In real
     * code, you will connect to a real model and expose its hierarchy.
     * --- RU -----------------
	 * ��� ������� ���������� ������� ����������� ������������ �������� � �����
	 * C:\Config\confex.confex (�������� � Constants.DEFAULT_CONFEX_FILE)
	 * ��� �������� ������ ������������ ���������� ����� ����������������� �����.
     * 
     */
    private ITreeNode createDummyModel() {
        String file = null;
        if (memento != null) {
            file = memento.getString(TAG_FILE_NAME);
            if (file == null) {
                file = selectInputFile(false);
            }
        } else {
        }
        if (file == null || file.equals("")) {
            return null;
        }
        this.setPartName(file.toString());
        ITreeNode root = config_tree.loadXmlFromFile(file);
        if (config_tree.isErrorState()) {
            Logger.errorDialog(getSite().getShell(), "MSG_CONFIG_TREE_HAS_ERROR", "");
        }
        return root;
    }

    /**
     * Method disposes the Confex Navigator and clear all resources
     */
    public void dispose() {
        if (config_tree.isDirty()) {
            try {
                if (config_tree.isErrorState()) {
                    if (Logger.questionDialog(getSite().getShell(), "MESSAGEBOX_TITLE_SAVE_FILE", "QUEST_SAVE_FILE_WITH_ERROR", "\n\n" + getConfigTree().getFileName())) {
                        if (getConfigTree().saveAs(this)) {
                            Logger.informationDialog(getSite().getShell(), "MSG_FILE_SAVED", "\n\n" + getConfigTree().getFileName());
                        }
                    }
                } else {
                    if (getConfigTree().getFileName().equals("")) {
                        getConfigTree().saveAs(this);
                    } else if (Logger.questionDialog(getSite().getShell(), "MESSAGEBOX_TITLE_SAVE_FILE", "QUEST_SAVE_FILE", "\n\n" + getConfigTree().getFileName())) {
                        getConfigTree().save();
                    }
                }
            } catch (Exception e) {
                Logger.errorDialog(getSite().getShell(), "MSG_ERR_ON_SAVING_FILE", e.getMessage());
            }
        }
        getConfigTree().removeAll();
        clipboard.dispose();
        super.dispose();
    }

    private NavigationView getThis() {
        return this;
    }

    private IDoubleClickListener dbl_click_listener = new IDoubleClickListener() {

        @SuppressWarnings("unchecked")
        public void doubleClick(DoubleClickEvent event) {
            StructuredSelection ss = (StructuredSelection) event.getSelection();
            for (Iterator iter = ss.iterator(); iter.hasNext(); ) {
                Object obj = iter.next();
                if (obj instanceof ITreeNode) {
                    ((ITreeNode) obj).run(getThis());
                    getTreeViewer().refresh(obj);
                }
            }
        }
    };

    /**
     * ������ ����
     * @param file_name
     */
    public void loadFile(String file_name) {
        IWorkbenchPage[] pages = this.getSite().getWorkbenchWindow().getPages();
        for (int i = 0; i < pages.length; i++) {
            IViewReference[] views = pages[i].getViewReferences();
            for (int j = 0; j < views.length; j++) {
                IWorkbenchPart part = views[j].getPart(false);
                if (part instanceof NavigationView) {
                    if (file_name.equals(((NavigationView) part).getConfigTree().getFileName())) {
                        Logger.informationDialog(getSite().getShell(), "MSG_FILE_ALREADY_OPEN", "\n\n" + file_name);
                        pages[i].activate(part);
                        return;
                    }
                }
            }
        }
        ITreeNode root = getConfigTree().openFile(file_name);
        treeViewer.setInput(root);
        setPartName(new File(file_name).getName());
        treeViewer.refresh();
        runOnStartNode();
    }

    /**
     * Open selection dialog and load file
     */
    public void loadFile() {
        String file_name = selectInputFile(false);
        if (file_name == null) return;
        loadFile(file_name);
    }

    /**
	 *	 launch OnStartNode only if NOT in isEnableEditMode
	 *	 search config/OnStartNode if found Run Them 		
	 */
    public void runOnStartNode() {
        System.out.println("--- runOnStartNode() ");
        ITreeNode rn = getConfigTree().getRoot();
        if (rn == null) return;
        if (rn.getChildren() == null || rn.getChildren().length == 0) return;
        rn = rn.getChildren()[0];
        if (rn == null) return;
        ITreeNode start = rn.getChildWithName("OnStartNode");
        if (start != null) {
            start.run(this);
        } else {
            System.out.println("WARNING! �� ������ OnStartNode");
        }
    }

    /**
     * �������� ����� ����
     * @param file_name
     */
    public void newFile() {
        String file_name = selectInputFile(true);
        if (file_name == null) return;
        ITreeNode root = getConfigTree().createNew(file_name);
        treeViewer.setInput(root);
        setPartName(file_name);
        treeViewer.refresh();
    }

    public void openFile(String file_name) {
        ITreeNode root = getConfigTree().openFile(file_name);
        treeViewer.setInput(root);
        setPartName(file_name);
        treeViewer.refresh();
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    public void createPartControl(Composite parent) {
        treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        ITreeNode root = createDummyModel();
        if (root == null) {
            root = getConfigTree().getRoot();
        } else {
        }
        root.setObserverForAll(this);
        treeViewer.setContentProvider(new ViewContentProvider());
        ILabelProvider lp = new ViewLabelProvider();
        ILabelDecorator decorator2 = new net.confex.decorators.ConfexRunningDecorator();
        treeViewer.setLabelProvider(new DecoratingLabelProvider(lp, decorator2));
        if (root != null) {
            treeViewer.setInput(root);
        }
        treeViewer.addDoubleClickListener(dbl_click_listener);
        createActions();
        setGlobalActionHandlers();
        createContextMenu();
        contributeToActionBars();
        initDragAndDrop();
        if (ConfexPlugin.getDefault().isEnableEditMode()) {
            IContextService contextService = (IContextService) getSite().getService(IContextService.class);
            IContextActivation contextActivation = contextService.activateContext("net.confex.context.NavigationView");
        }
        if (memento != null) {
            restoreState(memento);
            memento = null;
        }
        runOnStartNode();
    }

    /**
	 * ������� ��������
	 */
    protected void createActions() {
        copyAction = new CopyAction(this);
        copyWithoutChildAction = new CopyWithoutChildAction(this);
        cutAction = new CutAction(this);
        pasteAction = new PasteAction(this);
        copyFileAction = new CopyFileAction(this);
        pasteFileAction = new PasteFileAction(this);
        deleteAction = new DeleteAction(this);
        saveAction = new SaveConfexViewAction(this);
        saveAsAction = new SaveAsConfexViewAction(this);
        propertyAction = new PropertyClipboardViewAction(this);
    }

    /**
	 *  ��������� ����������� �������� � �������� ������ ����
	 */
    private void setGlobalActionHandlers() {
        IActionBars actionBars = getViewSite().getActionBars();
        actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
        actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);
        actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
        actionBars.setGlobalActionHandler(ActionFactory.SAVE.getId(), saveAction);
        actionBars.setGlobalActionHandler(ActionFactory.SAVE_AS.getId(), saveAsAction);
        actionBars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(), propertyAction);
    }

    /**
     * ������������� drag drop ��������
     */
    private void initDragAndDrop() {
        int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { TextTransfer.getInstance(), PluginTransfer.getInstance() };
        treeViewer.addDragSupport(ops, transfers, new NavigationViewDragListener(this));
        int ops_drop = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers_drop = new Transfer[] { TextTransfer.getInstance(), PluginTransfer.getInstance() };
        treeViewer.addDropSupport(ops_drop, transfers_drop, new NavigationVeiwDropListener(this));
        clipboard = new Clipboard(((Widget) treeViewer.getControl()).getDisplay());
    }

    /**
	 * ������� ����������� ����
	 */
    private void createContextMenu() {
        MenuManager menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        Menu menu = menuManager.createContextMenu(treeViewer.getControl());
        treeViewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuManager, getSite().getSelectionProvider());
    }

    public Shell getShell() {
        return getSite().getShell();
    }

    public boolean isEditable() {
        return edit_mode;
    }

    /**
	 * 
	 * @return true if tree has selected locked element
	 */
    @SuppressWarnings("unchecked")
    protected boolean hasLockedSelected() {
        ISelection selection = getTreeViewer().getSelection();
        TreeSelection ss = (TreeSelection) selection;
        for (Iterator iter = ss.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof ITreeNode) {
                if (((ITreeNode) obj).isLocked()) return true;
            }
        }
        return false;
    }

    /**
	 * ��������� ���� ����������
	 * @param manager
	 */
    private void fillContextMenu(IMenuManager manager) {
        boolean editable = isEditable();
        MenuManager addMenu = new MenuManager(Translator.getString("POP_NEW"), "AddOther");
        if (editable) {
            manager.add(addMenu);
            manager.add(new Separator());
        }
        manager.add(new RunAction(this));
        if (editable) {
            manager.add(new RunBatchAction(this));
            manager.add(new RunAsincAction(this));
            manager.add(new RunAsincBatchAction(this));
        }
        manager.add(new Separator());
        manager.add(copyAction);
        manager.add(copyWithoutChildAction);
        manager.add(copyFileAction);
        if (editable) {
            manager.add(cutAction);
            manager.add(pasteAction);
            manager.add(pasteFileAction);
            manager.add(deleteAction);
        }
        manager.add(new Separator());
        if (editable) {
            manager.add(new Separator(org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS));
            addMenu.add(new AddTreeNodeAction(this));
            addMenu.add(new AddVarsNodeAction(this));
            addMenu.add(new Separator());
            addMenu.add(new AddUrlTreeNodeAction(this));
            addMenu.add(new AddHtmlCompositeAction(this));
            addMenu.add(new AddHtmlTextTreeNodeAction(this));
            addMenu.add(new AddJava2HtmlNodeAction(this));
            addMenu.add(new AddConfexNodeAction(this));
            addMenu.add(new Separator());
            addMenu.add(new AddExecTreeNodeAction(this));
            addMenu.add(new AddFolderNodeAction(this));
            addMenu.add(new AddCompositeFormViewNodeAction(this));
            addMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
            addMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end"));
            manager.add(new Separator());
            manager.add(propertyAction);
        }
    }

    protected void fillLocalPullDown(IMenuManager manager) {
        manager.removeAll();
        if (isEditable()) {
            manager.add(saveAction);
            manager.add(new Separator());
            manager.add(saveAsAction);
        }
        manager.add(actionShowUsersOnly);
        actionShowUsersOnly.setChecked(!edit_mode);
        actionShowUsersOnly.setEnabled(ConfexPlugin.getDefault().isEnableEditMode());
        if (edit_mode) treeViewer.removeFilter(visInEditModeFilter); else treeViewer.addFilter(visInEditModeFilter);
        manager.update(true);
    }

    final ViewerFilter visInEditModeFilter = new ViewerFilter() {

        public boolean select(Viewer viewer, Object parentElement, Object element) {
            if (element instanceof ITreeNode) return !((ITreeNode) element).isNotVisInUserMode();
            return true;
        }
    };

    Action actionShowUsersOnly = new Action(Translator.getString("NavigationView.USER_MODE")) {

        public void run() {
            if (!edit_mode) {
                edit_mode = true;
                treeViewer.removeFilter(visInEditModeFilter);
            } else {
                edit_mode = false;
                treeViewer.addFilter(visInEditModeFilter);
            }
            contributeToActionBars();
        }
    };

    protected void fillLocalToolBar(IToolBarManager manager) {
        manager.removeAll();
        if (isEditable()) {
            manager.add(saveAction);
        }
        manager.update(true);
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        treeViewer.getControl().setFocus();
    }

    /**
	 * �������� "������������� ��������"
	 */
    @SuppressWarnings("unchecked")
    public void runProperty() {
        ISelection selection = getTreeViewer().getSelection();
        TreeSelection ss = (TreeSelection) selection;
        for (Iterator iter = ss.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof ITreeNode) {
                ((ITreeNode) obj).openPropertyDialog(this, getSite().getShell(), false);
                getTreeViewer().refresh(obj);
                getTreeViewer().setExpandedState(obj, true);
            }
        }
    }

    /**
	 * Action open src editors for all selected nodes
	 */
    public void openSrcEditors() {
        ISelection selection = getTreeViewer().getSelection();
        TreeSelection ss = (TreeSelection) selection;
        for (Iterator iter = ss.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof ITreeNode) {
                ((ITreeNode) obj).openSrcEditor();
            }
        }
    }

    /**
	 * �������� "��������� ������������"
	 */
    public void runSave() {
        try {
            if (getConfigTree().getFileName() == null || getConfigTree().getFileName().equals("")) {
                runSaveAs();
                return;
            }
            getConfigTree().save();
            setDirty(false);
            Logger.informationDialog(getSite().getShell(), "MSG_FILE_SAVED", "\n\n" + getConfigTree().getFileName());
        } catch (Exception e) {
            Logger.errorDialog(getSite().getShell(), "MESSAGE_ERR_OPEN_VIEW", e.getMessage());
        }
    }

    /**
	 * �������� "��������� ���..."
	 */
    public void runSaveAs() {
        try {
            if (getConfigTree().saveAs(this)) {
                setDirty(false);
                Logger.informationDialog(getSite().getShell(), "MSG_FILE_SAVED", "\n\n" + getConfigTree().getFileName());
            }
        } catch (Exception e) {
            Logger.errorDialog(getSite().getShell(), "MESSAGE_ERR_OPEN_VIEW", e.getMessage());
        }
    }

    /**
	 * ������ ���� ������� ������ ���� �������.
	 * 
	 * @return false ���� ��-�� ���������� != 1
	 */
    public boolean cantDoWhenNotOneSelected(TreeSelection ss) {
        if (ss.size() != 1) {
            Logger.errorDialog(getSite().getShell(), "MSG_CANT_DO_SELECT_ONE", "");
            return false;
        }
        return true;
    }

    /**
	 * ������ ���� ������� ������ ���� �������.
	 * 
	 * @return false ���� ��-�� ���������� == 0
	 */
    public boolean cantDoWhenLessOneSelected(TreeSelection ss) {
        if (ss.size() == 0) {
            Logger.errorDialog(getSite().getShell(), "MSG_CANT_DO_SELECT_AT_LEAST_ONE", "");
            return false;
        }
        return true;
    }

    public void runDelete(boolean with_question) {
        if (with_question) {
            if (!Logger.questionDialog(getSite().getShell(), "MESSAGEBOX_TITLE_DELETE", "QUEST_DELETE_SELECTED", "")) {
                return;
            }
        }
        delete_selected();
        setDirty(true);
    }

    @SuppressWarnings("unchecked")
    protected void delete_selected() {
        ISelection selection = treeViewer.getSelection();
        TreeSelection ss = (TreeSelection) selection;
        for (Iterator iter = ss.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof ITreeNode) {
                ((ITreeNode) obj).getParent().removeChild((ITreeNode) obj);
                treeViewer.remove(obj);
            }
        }
    }

    public void runCut() {
        runCopy();
        runDelete(false);
    }

    public void runCopy() {
        ISelection selection = treeViewer.getSelection();
        TreeSelection ss = (TreeSelection) selection;
        if (!cantDoWhenLessOneSelected(ss)) return;
        String select_txt = "";
        select_txt += getSegmentText();
        select_txt += "";
        setClipboardContent(select_txt);
    }

    public void runCopyWithoutChild() {
        ISelection selection = treeViewer.getSelection();
        TreeSelection ss = (TreeSelection) selection;
        if (!cantDoWhenLessOneSelected(ss)) return;
        String select_txt = "";
        select_txt += getSelectedXmlWithoutChild();
        select_txt += "";
        setClipboardContent(select_txt);
    }

    public void runPaste() {
        String text = (String) getClipboardContent(DND.CLIPBOARD);
        if (text != null && text.length() > 0) {
            ISelection selection = treeViewer.getSelection();
            TreeSelection ss = (TreeSelection) selection;
            Object obj = null;
            if (ss.size() == 0) {
                obj = getConfigTree().getRoot();
            } else {
                if (!cantDoWhenNotOneSelected(ss)) return;
                obj = ss.getFirstElement();
            }
            text = "<CopyDND>" + text + "</CopyDND>";
            if (obj instanceof ITreeNode) {
                ((ITreeNode) obj).addChildXml(text, null);
                getTreeViewer().refresh(obj);
                getTreeViewer().setExpandedState(obj, true);
                setDirty(true);
            }
        }
    }

    public void runCopyFile() {
        ISelection selection = treeViewer.getSelection();
        TreeSelection ss = (TreeSelection) selection;
        if (!cantDoWhenLessOneSelected(ss)) return;
        String select_txt = "";
        select_txt += getSegmentText();
        select_txt += "";
        setClipboardContent(select_txt);
        String copy_file_name = selectCopyFile(false);
        if (copy_file_name == null) return;
        Utils.writeStringToFile(select_txt, new File(copy_file_name));
    }

    public void runPasteFile() {
        String paste_file_name = selectCopyFile(false);
        if (paste_file_name == null) return;
        String text = Utils.readTextFromFile(paste_file_name);
        if (!text.startsWith("<CopyDND>")) text = "<CopyDND>" + text + "</CopyDND>";
        if (text != null && text.length() > 0) {
            ISelection selection = treeViewer.getSelection();
            TreeSelection ss = (TreeSelection) selection;
            Object obj = null;
            if (ss.size() == 0) {
                obj = getConfigTree().getRoot();
            } else {
                if (!cantDoWhenNotOneSelected(ss)) return;
                obj = ss.getFirstElement();
            }
            if (obj instanceof ITreeNode) {
                ((ITreeNode) obj).addChildXml(text, null);
                getTreeViewer().refresh(obj);
                getTreeViewer().setExpandedState(obj, true);
                setDirty(true);
            }
        }
    }

    Object getClipboardContent(int clipboardType) {
        TextTransfer textTransfer = TextTransfer.getInstance();
        return clipboard.getContents(textTransfer, clipboardType);
    }

    private class NavigationVeiwDropListener extends PluginDropAdapter {

        private NavigationView navigation_view;

        public NavigationVeiwDropListener(NavigationView navigation_view) {
            super(navigation_view.getTreeViewer());
            this.navigation_view = navigation_view;
        }

        public boolean validateDrop(Object target, int operation, TransferData transferType) {
            if (!navigation_view.isEditable()) return false;
            TransferData currentTransfer = transferType;
            if (target instanceof ITreeNode) {
                return !((ITreeNode) target).isLocked();
            }
            if (currentTransfer != null) {
                return true;
            }
            return false;
        }

        /**
         * Returns the position of the given event's coordinates relative to its target.
         * The position is determined to be before, after, or on the item, based on
         * some threshold value.
         *
         * @param event the event
         * @return one of the <code>LOCATION_* </code>constants defined in this class
         */
        protected int determineLocation(DropTargetEvent event) {
            if (!(event.item instanceof Item)) {
                return LOCATION_NONE;
            }
            Item item = (Item) event.item;
            Point coordinates = new Point(event.x, event.y);
            coordinates = getViewer().getControl().toControl(coordinates);
            if (item != null) {
                Rectangle bounds = getBounds(item);
                if (bounds == null) {
                    return LOCATION_NONE;
                }
                if ((coordinates.y - bounds.y) < 5) {
                    return LOCATION_BEFORE;
                }
                if ((bounds.y + bounds.height - coordinates.y) < 0) {
                    return LOCATION_AFTER;
                }
            }
            return LOCATION_ON;
        }

        public void drop(DropTargetEvent event) {
            if (!isEditable()) {
                Logger.informationDialog(getSite().getShell(), "MESSAGEBOX_TITLE_DROP_DISABLED", "DISABLED_IN_USER_MODE");
                return;
            }
            int currentLocation = getCurrentLocation();
            ITreeNode selected_node = null;
            ITreeNode destination_node = null;
            ISelection selection = getViewer().getSelection();
            TreeSelection ss = (TreeSelection) selection;
            Object selected = null;
            if (current_drag_source == navigation_view) {
                selected = ss.getFirstElement();
                if (selected instanceof ITreeNode) {
                    selected_node = (ITreeNode) selected;
                } else {
                    selected_node = null;
                }
            } else {
            }
            current_drag_source = null;
            TextTransfer textTransfer = TextTransfer.getInstance();
            String data = (String) textTransfer.nativeToJava(event.currentDataType);
            if (data != null) {
                Object obj = event.item;
                if (obj instanceof TreeItem) {
                    Object obj2 = ((TreeItem) obj).getData();
                    if (obj2 instanceof ITreeNode) {
                        destination_node = (ITreeNode) obj2;
                        if (selected_node != null) {
                            if (!selected_node.isParentOf(destination_node) && selected_node != destination_node) {
                                ITreeNode parent_of_selected = selected_node.getParent();
                                if (currentLocation == ViewerDropAdapter.LOCATION_ON) {
                                    destination_node.addChildXml(data, null);
                                    delete_selected();
                                    navigation_view.setDirty(true);
                                    navigation_view.getTreeViewer().refresh(destination_node);
                                    navigation_view.getTreeViewer().setExpandedState(destination_node, true);
                                } else if (currentLocation == ViewerDropAdapter.LOCATION_AFTER) {
                                } else if (currentLocation == ViewerDropAdapter.LOCATION_BEFORE) {
                                    destination_node.getParent().addChildXml(data, destination_node);
                                    delete_selected();
                                    navigation_view.getTreeViewer().refresh(destination_node.getParent());
                                    navigation_view.getTreeViewer().refresh(parent_of_selected);
                                    navigation_view.setDirty(true);
                                } else if (currentLocation == ViewerDropAdapter.LOCATION_NONE) {
                                    System.err.println(" currentLocation --> LOCATION_NONE");
                                }
                            } else {
                                return;
                            }
                        } else {
                            if (currentLocation == ViewerDropAdapter.LOCATION_ON) {
                                destination_node.addChildXml(data, null);
                                navigation_view.setDirty(true);
                                navigation_view.getTreeViewer().refresh(destination_node);
                                navigation_view.getTreeViewer().setExpandedState(destination_node, true);
                            } else if (currentLocation == ViewerDropAdapter.LOCATION_AFTER) {
                                System.out.println(" currentLocation --> LOCATION_AFTER " + destination_node.getName());
                            } else if (currentLocation == ViewerDropAdapter.LOCATION_BEFORE) {
                                destination_node.getParent().addChildXml(data, destination_node);
                                navigation_view.getTreeViewer().refresh(destination_node.getParent());
                                navigation_view.getTreeViewer().setExpandedState(destination_node.getParent(), true);
                                navigation_view.setDirty(true);
                            } else if (currentLocation == ViewerDropAdapter.LOCATION_NONE) {
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * A drag listener for the readme editor's content outline page.
     * Allows dragging of content segments into views that support
     * the <code>TextTransfer</code> or <code>PluginTransfer</code> transfer types.
     */
    private class NavigationViewDragListener extends DragSourceAdapter {

        private NavigationView navigation_view;

        /**
         * Creates a new drag listener for the given page.
         */
        public NavigationViewDragListener(NavigationView navigation_view) {
            this.navigation_view = navigation_view;
        }

        public void dragSetData(DragSourceEvent event) {
            if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
                String text = "<CopyDND>\n";
                text += getSegmentText();
                text += "</CopyDND>\n";
                event.data = text;
                current_drag_source = navigation_view;
                return;
            }
        }
    }

    /**
     * ���������� XML ��� ������������ �����
     */
    @SuppressWarnings("unchecked")
    public String getSegmentText() {
        String ret = "";
        ISelection selection = treeViewer.getSelection();
        TreeSelection ss = (TreeSelection) selection;
        for (Iterator iter = ss.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof ITreeNode) {
                ret += ((ITreeNode) obj).getXml(true);
            }
        }
        return ret;
    }

    /**
	 * Return the exactly xml for selected without child
	 * @param read_src_text
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public String getSelectedXmlWithoutChild() {
        String ret = "";
        ISelection selection = treeViewer.getSelection();
        TreeSelection ss = (TreeSelection) selection;
        for (Iterator iter = ss.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof ITreeNode) {
                ret += ((ITreeNode) obj).getXmlTextWithoutChild(true);
            }
        }
        return ret;
    }

    public void setClipboardContent(String txt) {
        TextTransfer textTransfer = TextTransfer.getInstance();
        Object[] data;
        Transfer[] types;
        data = new Object[] { txt };
        types = new Transfer[] { textTransfer };
        clipboard.setContents(data, types, DND.CLIPBOARD);
    }

    public void setPartName(String partName) {
        super.setPartName(partName);
    }

    public TreeViewer getTreeViewer() {
        return treeViewer;
    }
}
