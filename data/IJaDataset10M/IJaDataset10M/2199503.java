package i18ntool.utils;

import iceworld.fernado.entity.INode;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

public final class Refresh {

    private static final Refresh instance = new Refresh();

    private Refresh() {
    }

    public static Refresh getInstance() {
        return instance;
    }

    /**
	 * refresh the whole Explorer view
	 */
    public void doRefresh() {
        final ExplorerView ev = (ExplorerView) ViewAssistant.getInstance().findView(i18ntool.view.ExplorerView.ID);
        final INode treeNode = NodeAssistant.getInstance().getData();
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                ev.getViewer().setInput(treeNode);
            }
        });
        ViewAssistant.getInstance().closeAllEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());
    }

    /**
	 * refresh the node in Explorer view
	 * @param node
	 */
    public void doRefreshAfterTreeNodeChanged(final INode node) {
        IViewPart view = ViewAssistant.getInstance().findView(ExplorerView.ID);
        ExplorerView ev = null;
        if (null != view) {
            ev = (ExplorerView) view;
            ev.getViewer().refresh(node, true);
        }
    }
}
