package hu.cubussapiens.modembed.ui.perspective;

import hu.cubussapiens.modembed.ui.MODembedUI;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * @author balazs.grill
 *
 */
public class MODembedPerspectiveFactory implements IPerspectiveFactory {

    public static final String LEFT = MODembedUI.PLUGIN_ID + ".left";

    public static final String UTILS = MODembedUI.PLUGIN_ID + ".left.utils";

    @Override
    public void createInitialLayout(IPageLayout layout) {
        IFolderLayout navFolder = layout.createFolder(LEFT, IPageLayout.LEFT, 0.25f, layout.getEditorArea());
        navFolder.addView(IPageLayout.ID_PROJECT_EXPLORER);
        IFolderLayout utils = layout.createFolder(UTILS, IPageLayout.BOTTOM, 0.45f, LEFT);
        utils.addView(IDebugUIConstants.ID_DEBUG_VIEW);
        utils.addView(IPageLayout.ID_PROBLEM_VIEW);
        layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
    }
}
