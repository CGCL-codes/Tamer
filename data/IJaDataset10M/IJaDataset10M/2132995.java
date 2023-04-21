package net.sourceforge.taggerplugin.action;

import java.util.LinkedList;
import java.util.List;
import net.sourceforge.taggerplugin.dialog.TagDialog;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.view.TagSetView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class AddTagAction implements IViewActionDelegate {

    private static final String CONTAINERID_ALL = "All Selected Containers";

    private IViewPart view;

    /**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
    public void init(IViewPart view) {
        this.view = view;
    }

    /**
	 * @see IViewActionDelegate#run(IAction)
	 */
    public void run(IAction action) {
        final TagSetView tagSetView = (TagSetView) view;
        final ITreeSelection selection = (ITreeSelection) tagSetView.getSelection();
        if (!selection.isEmpty()) {
            final List<ITagSetContainer> tscs = new LinkedList<ITagSetContainer>();
            final TreePath[] paths = selection.getPaths();
            for (TreePath path : paths) {
                final Object lastSeg = path.getLastSegment();
                if (lastSeg instanceof ITagSetContainer) {
                    tscs.add((ITagSetContainer) lastSeg);
                } else {
                    return;
                }
            }
            final TagDialog dialog = new TagDialog(view.getSite().getShell());
            dialog.setTagContainerEditable(true);
            dialog.setTagContainers(listContainerNames(tscs));
            dialog.setTagContainerId(CONTAINERID_ALL);
            if (dialog.showCreate() == TagDialog.OK) {
                final String containerId = dialog.getTagContainerId();
                if (containerId.equals(CONTAINERID_ALL)) {
                    for (ITagSetContainer tsc : tscs) {
                        tsc.addTag(dialog.getTagName(), dialog.getTagDescription());
                    }
                } else {
                    for (ITagSetContainer tsc : tscs) {
                        if (tsc.getName().equals(containerId)) {
                            tsc.addTag(dialog.getTagName(), dialog.getTagDescription());
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    private String[] listContainerNames(final List<ITagSetContainer> tscs) {
        final String[] names = new String[tscs.size() + 1];
        names[0] = CONTAINERID_ALL;
        for (int i = 0; i < tscs.size(); i++) {
            names[i + 1] = tscs.get(i).getName();
        }
        return names;
    }
}
