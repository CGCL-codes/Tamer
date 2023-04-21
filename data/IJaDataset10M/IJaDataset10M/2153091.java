package com.jiexplorer.rcp.ui.navigator.action;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import com.jiexplorer.rcp.IJIExplorerK;
import com.jiexplorer.rcp.actions.CollapseAction;
import com.jiexplorer.rcp.actions.CollapseAllAction;
import com.jiexplorer.rcp.actions.ExpandAction;
import com.jiexplorer.rcp.actions.ExpandAllAction;
import com.jiexplorer.rcp.actions.NewCategoryAction;
import com.jiexplorer.rcp.actions.RefreshTreeNodeAction;
import com.jiexplorer.rcp.actions.RemoveCategoryAction;
import com.jiexplorer.rcp.actions.RenameCategoryAction;
import com.jiexplorer.rcp.srv.ImageSrv;
import com.jiexplorer.rcp.ui.category.CategoryView;
import com.jiexplorer.rcp.util.Messages;

public class CategoryActionGroup extends NavigatorActionGroup {

    public static final String GROUP_FILTERS = "group.filters";

    private RemoveCategoryAction removeCategoryAction;

    private RenameCategoryAction renameCategoryAction;

    private NewCategoryAction newCategoryAction;

    private RefreshTreeNodeAction refreshAction;

    private ExpandAllAction expandAllAction;

    private CollapseAllAction collapseAllAction;

    private ExpandAction expandAction;

    private CollapseAction collapseAction;

    private CategoryView view;

    /**
	 * Constructs a new navigator action group and creates its actions and
	 * action groups.
	 * 
	 * @param navigator
	 *            the resource navigator
	 */
    public CategoryActionGroup(final CategoryView view) {
        super();
        this.view = view;
    }

    @Override
    public void dispose() {
        view = null;
        disposeActionGroups();
        super.dispose();
    }

    protected void handleKeyReleased(final KeyEvent event) {
    }

    /**
	 * Creates the actions contained in this action group.
	 */
    @Override
    protected void createActions() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final ISharedImages platformImages = workbench.getSharedImages();
        removeCategoryAction = new RemoveCategoryAction(Messages.menu_Delete_text);
        removeCategoryAction.setImageDescriptor(platformImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        removeCategoryAction.setDisabledImageDescriptor(platformImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        renameCategoryAction = new RenameCategoryAction(IJIExplorerK.RENAME);
        newCategoryAction = new NewCategoryAction(IJIExplorerK.NEW);
        refreshAction = new RefreshTreeNodeAction(Messages.menu_Refresh_text);
        refreshAction.setImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_REFRESH));
        refreshAction.setDisabledImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_REFRESH_DISABLED));
        refreshAction.setToolTipText(Messages.menu_Refresh_tooltip);
        expandAllAction = new ExpandAllAction(Messages.menu_ExpandAll_text);
        expandAllAction.setImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_EXPANDALL));
        expandAllAction.setDisabledImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_EXPANDALL_DISABLED));
        expandAllAction.setToolTipText(Messages.menu_ExpandAll_tooltip);
        collapseAllAction = new CollapseAllAction(Messages.menu_CollapseAll_text);
        collapseAllAction.setImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_COLLAPSEALL));
        collapseAllAction.setDisabledImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_COLLAPSEALL_DISABLED));
        collapseAllAction.setToolTipText(Messages.menu_CollapseAll_tooltip);
        expandAction = new ExpandAction(Messages.menu_Expand_text);
        expandAction.setImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_EXPANDALL));
        expandAction.setDisabledImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_EXPANDALL_DISABLED));
        expandAction.setToolTipText(Messages.menu_Expand_tooltip);
        collapseAction = new CollapseAction(Messages.menu_Collapse_text);
        collapseAction.setImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_COLLAPSEALL));
        collapseAction.setDisabledImageDescriptor(ImageSrv.getInstance().getImageDescriptor(ImageSrv.IMAGE_COLLAPSEALL_DISABLED));
        collapseAction.setToolTipText(Messages.menu_Collapse_tooltip);
    }

    @Override
    public void fillContextMenu(final IMenuManager menuMgr) {
        final boolean isEmpty = view.getSelection().isEmpty();
        removeCategoryAction.setEnabled(!isEmpty);
        removeCategoryAction.init(view);
        menuMgr.add(removeCategoryAction);
        renameCategoryAction.setEnabled(!isEmpty);
        renameCategoryAction.init(view);
        menuMgr.add(renameCategoryAction);
        newCategoryAction.setEnabled(!isEmpty);
        newCategoryAction.init(view);
        menuMgr.add(newCategoryAction);
        menuMgr.add(new Separator());
        expandAction.setEnabled(!isEmpty);
        expandAction.init(view);
        menuMgr.add(expandAction);
        collapseAction.setEnabled(!isEmpty);
        collapseAction.init(view);
        menuMgr.add(collapseAction);
        menuMgr.add(new Separator());
        refreshAction.setEnabled(!isEmpty);
        refreshAction.init(view);
        menuMgr.add(refreshAction);
        expandAllAction.init(view);
        menuMgr.add(expandAllAction);
        collapseAllAction.init(view);
        menuMgr.add(collapseAllAction);
        super.fillContextMenu(menuMgr);
    }
}
