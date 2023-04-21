package ms.jasim.model.editor.views;

import java.util.List;
import ms.jasim.pddl.PddlSolution;
import ms.util.ImageCache;
import ms.util.SimulationUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */
public class SolutionListView extends ViewPart {

    /**
	 * The ID of the view as specified by the extension.
	 */
    public static final String ID = "ms.jasim.model.editor.views.SolutionListView";

    private TableViewer viewer;

    protected Action viewAction = new Action("View solution") {

        @Override
        public void run() {
            PddlSolution sol = (PddlSolution) ((StructuredSelection) viewer.getSelection()).getFirstElement();
            if (sol != null) {
                SolutionView view = (SolutionView) SimulationUtils.showView(SolutionView.ID);
                if (view != null) view.setInput(sol);
            }
        }

        ;
    };

    class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            if (parent instanceof List) return ((List) parent).toArray();
            return new Object[0];
        }
    }

    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            if (obj instanceof PddlSolution) return ((PddlSolution) obj).getName();
            return "?";
        }

        public Image getColumnImage(Object obj, int index) {
            return ImageCache.getSolutionView().createImage();
        }

        @Override
        public Image getImage(Object obj) {
            return ImageCache.getSolutionView().createImage();
        }
    }

    class NameSorter extends ViewerSorter {
    }

    /**
	 * The constructor.
	 */
    public SolutionListView() {
    }

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
    @Override
    public void createPartControl(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setSorter(new NameSorter());
        Table table = viewer.getTable();
        TableColumn column = new TableColumn(table, SWT.LEFT);
        column.setText("Solution");
        column.setWidth(200);
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                viewAction.run();
            }
        });
        getSite().setSelectionProvider(viewer);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "ms.jasim.model.editor.viewer");
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                SolutionListView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(viewAction);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(viewAction);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(viewAction);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void makeActions() {
        viewAction.setImageDescriptor(ImageCache.getImage("icons/view.png"));
    }

    private void hookDoubleClickAction() {
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void setInput(List<PddlSolution> solutions) {
        viewer.setInput(solutions);
    }
}
