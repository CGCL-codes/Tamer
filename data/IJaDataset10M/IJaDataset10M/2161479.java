package org.skyfree.ghyll.tcard.repository;

import java.util.ArrayList;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.skyfree.ghyll.tcard.core.IResourceChangeEvent;
import org.skyfree.ghyll.tcard.core.IResourceChangedListener;
import org.skyfree.ghyll.tcard.core.IStoragePath;
import org.skyfree.ghyll.tcard.core.ITRepository;
import org.skyfree.ghyll.tcard.core.ITWorkpiece;
import org.skyfree.ghyll.tcard.core.TCardCore;
import org.skyfree.ghyll.tcard.repository.action.AddCategoryMenuItemAction;
import org.skyfree.ghyll.tcard.repository.action.DeleteMenuItemAction;
import org.skyfree.ghyll.tcard.repository.action.MenuItemAction;
import org.skyfree.ghyll.tcard.repository.action.SeparatorMenuItemAction;
import org.skyfree.ghyll.ui.part.SelectionEnableViewPart;

public class RepoExplorer extends SelectionEnableViewPart implements IDoubleClickListener, ISelectionChangedListener, MouseListener, IResourceChangedListener, ModifyListener {

    class TreeLabelProvider extends LabelProvider {

        public String getText(Object element) {
            if (element instanceof ITRepository) {
                return "�豸��";
            } else if (element instanceof String) {
                return (String) element;
            } else if (element instanceof ITWorkpiece) {
                return ((ITWorkpiece) element).getName();
            }
            return null;
        }

        public Image getImage(Object element) {
            if (element instanceof ITRepository) {
                return IM_RE;
            } else if (element instanceof String) {
                return IM_CA;
            } else if (element instanceof ITWorkpiece) {
                return IM_WP;
            }
            return null;
        }
    }

    class TreeContentProvider implements IStructuredContentProvider, ITreeContentProvider {

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object inputElement) {
            String searchString = searchInput.getText().trim();
            if (searchString.length() != 0) {
                return repository.searchWorkPiece(searchString).toArray();
            }
            return new Object[] { repository };
        }

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof ITRepository) {
                return repository.getAllCategory();
            } else if (parentElement instanceof String) {
                Object[] array = repository.getWorkPiece((String) parentElement).toArray();
                return array;
            } else return new Object[0];
        }

        public Object getParent(Object element) {
            return null;
        }

        public boolean hasChildren(Object element) {
            return getChildren(element).length > 0;
        }
    }

    public static final String ID = "org.skyfree.ghyll.tcard.repository.RepoExplorer";

    static final String WEID = "org.skyfree.ghyll.tcard.repository.WorkpieceEditor";

    static Image IM_RE = Activator.getImageDescriptor("icons/repo_rep.gif").createImage();

    static Image IM_WP = Activator.getImageDescriptor("icons/workpiece.png").createImage();

    static Image IM_CA = Activator.getImageDescriptor("icons/category.gif").createImage();

    ITRepository repository;

    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        this.repository = TCardCore.getDefault().getRepository();
    }

    TreeViewer treeViewer;

    private Text searchInput;

    /**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.numColumns = 1;
        gl.verticalSpacing = 2;
        gl.marginWidth = 1;
        gl.marginHeight = 1;
        container.setLayout(gl);
        final CLabel label = new CLabel(container, SWT.NONE);
        label.setText("�����豸��ƿ��ٶ�λ");
        label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
        this.searchInput = new org.eclipse.swt.widgets.Text(container, SWT.NONE);
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gd.minimumWidth = 50;
        this.searchInput.setLayoutData(gd);
        Composite treeContainer = new Composite(container, SWT.NONE);
        GridData gd2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
        treeContainer.setLayoutData(gd2);
        treeContainer.setLayout(new FillLayout());
        this.treeViewer = new TreeViewer(treeContainer, SWT.BORDER);
        treeViewer.setLabelProvider(new TreeLabelProvider());
        treeViewer.setContentProvider(new TreeContentProvider());
        treeViewer.setInput(new Object());
        treeViewer.addDoubleClickListener(this);
        treeViewer.addSelectionChangedListener(this);
        this.treeViewer.getTree().addMouseListener(this);
        registerMenuItemActions();
        this.searchInput.addModifyListener(this);
        TCardCore.getDefault().addResourceChangedListener(this);
    }

    @Override
    public void setFocus() {
    }

    @Override
    public void doubleClick(DoubleClickEvent event) {
        Object selection = ((StructuredSelection) event.getSelection()).getFirstElement();
        System.out.println(selection.getClass());
        if (selection instanceof ITWorkpiece) {
            ITWorkpiece wp = (ITWorkpiece) selection;
            try {
                boolean opened = false;
                IEditorReference[] allEditor = getViewSite().getWorkbenchWindow().getActivePage().getEditorReferences();
                for (int i = 0; i < allEditor.length; i++) {
                    IEditorInput input = allEditor[i].getEditorInput();
                    Object re = input.getAdapter(IStoragePath.class);
                    if (re != null) {
                        IStoragePath path = (IStoragePath) re;
                        if (wp.getPath().equals(path)) {
                            opened = true;
                            getViewSite().getWorkbenchWindow().getActivePage().activate(allEditor[i].getEditor(true));
                        }
                    }
                }
                if (!opened) {
                    getViewSite().getWorkbenchWindow().getActivePage().openEditor(new WorkpieceInput(wp), WEID);
                }
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        setSelection(event.getSelection());
        this.fireSelectionEvent(event);
    }

    private ArrayList<MenuItemAction> menuList = new ArrayList<MenuItemAction>();

    private Menu createDynamicMenu(Shell shell, TreeItem item) {
        Menu popUpMenu = new Menu(shell, SWT.POP_UP);
        for (int i = 0; i < this.menuList.size(); i++) {
            MenuItemAction menuItem = (MenuItemAction) this.menuList.get(i);
            menuItem.createItem(popUpMenu, item);
        }
        return popUpMenu;
    }

    private void registerMenuItemActions() {
        this.register(new AddCategoryMenuItemAction(SWT.PUSH, "���ӷ���", SWT.MOD1 + SWT.MOD2 + '2', IM_CA, this.treeViewer));
        this.register(new SeparatorMenuItemAction());
        Image image = ActionFactory.DELETE.create(this.getViewSite().getWorkbenchWindow()).getImageDescriptor().createImage();
        this.register(new DeleteMenuItemAction(SWT.PUSH, "ɾ��", SWT.MOD1 + SWT.MOD2 + '2', image, this.treeViewer));
    }

    public void register(MenuItemAction item) {
        this.menuList.add(item);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        Tree tree = this.treeViewer.getTree();
        TreeItem item = tree.getSelection()[0];
        if (item.getBounds().contains(e.x, e.y)) {
            if (e.button == 3) {
                tree.setMenu(createDynamicMenu(tree.getShell(), item));
            }
        } else {
            tree.setMenu(null);
        }
    }

    @Override
    public void onResourceChanged(IResourceChangeEvent event) {
        if (event.getType().equals(IResourceChangeEvent.Type_Repository)) {
            try {
                this.repository.reload((String) event.getResource());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.treeViewer.refresh();
        }
    }

    @Override
    public void modifyText(ModifyEvent e) {
        this.treeViewer.refresh();
    }
}
