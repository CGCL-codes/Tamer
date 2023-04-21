package au.com.kelpie.fgfp.action;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import au.com.kelpie.fgfp.Messages;
import au.com.kelpie.fgfp.util.ImageProvider;
import au.com.kelpie.fgfp.views.MapView;
import au.com.kelpie.fgfp.views.PlanEditor;

/**
 * Add To Plan Action
 */
public class ViewMap extends PlannerAction {

    /**
	 * 
	 */
    public ViewMap() {
        super(Messages.getString("MapView.0"), ImageProvider.ICON_VIEW_MAP);
    }

    @Override
    public void run() {
        final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        MapView view = (MapView) page.findView(MapView.ID_VIEW);
        if (view == null) {
            final IWorkbenchPart activePart = page.getActivePart();
            try {
                view = (MapView) page.showView(MapView.ID_VIEW);
            } catch (final PartInitException e) {
                e.printStackTrace();
                return;
            }
            final IEditorPart editor = page.getActiveEditor();
            if (editor == null) {
                return;
            }
            if (editor instanceof PlanEditor) {
                final PlanEditor pe = (PlanEditor) editor;
                pe.planChanged();
            }
            page.activate(activePart);
            page.bringToTop(view);
        } else {
            page.bringToTop(view);
        }
    }
}
