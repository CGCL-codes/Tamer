package j3dworkbench.app.commands.window;

import j3dworkbench.app.views.scenegraph.SceneGraphView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class WindowCommandsDelegate extends AbstractHandler {

    private static final AbstractHandler instance = new WindowCommandsDelegate();

    private WindowCommandsDelegate() {
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            switch(WindowCommandsEnum.valueOf(event.getCommand().getId())) {
                case SHOW_PROPERTIES_VIEW:
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.PropertySheet");
                    break;
                case SHOW_SCENEGRAPH_VIEW:
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SceneGraphView.ID);
                    break;
            }
        } catch (PartInitException pie) {
            pie.printStackTrace();
        }
        return null;
    }

    public static AbstractHandler getInstance() {
        return instance;
    }
}
