package tr.view.projects.future;

import au.com.thinkingrock.tr.resource.Icons;
import java.awt.Image;
import org.openide.actions.PasteAction;
import org.openide.util.actions.SystemAction;
import tr.model.project.Project;
import tr.view.projects.AddProjectAction;
import tr.view.projects.CollapseAllAction;
import tr.view.projects.ExpandAllAction;
import tr.view.projects.ToggleShowDoneAction;

/**
 * The root node for future projects.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class FutureRootNode extends FutureProjectNode {

    /** Constructs a new instance. */
    public FutureRootNode(Project root, boolean showDone) {
        super(root, showDone);
    }

    @Override
    public Image getIcon(int type) {
        return Icons.ProjectsFuture.getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return Icons.ProjectsFuture.getImage();
    }

    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[] { SystemAction.get(ExpandAllAction.class), SystemAction.get(CollapseAllAction.class), null, SystemAction.get(ToggleShowDoneAction.class), null, SystemAction.get(AddProjectAction.class), null, SystemAction.get(PasteAction.class) };
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canAddAction() {
        return false;
    }
}
