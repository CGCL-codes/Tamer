package ti.plato.logcontrol.profiles;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import ti.plato.logcontrol.TreeControl;
import ti.plato.logcontrol.views.LogControlView;

public class RemoveAllProfileMenuAction extends ProfileMenuAction {

    public RemoveAllProfileMenuAction(ProfilePageController pageController, String id, String text, String tooltip, String iconName) {
        super(pageController, id, text, tooltip, iconName);
    }

    public void run() {
        LogControlView.getDefault().removeAllProfiles();
        ((TreeControl) pageController.getTabControl()).getViewer().getTree().removeAll();
        pageController.removeAll();
    }

    public void setEnabledState() {
        TreeViewer view = ((TreeControl) pageController.getTabControl()).getViewer();
        Tree tree = view.getTree();
        if (tree.getItemCount() == 0) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }
}
