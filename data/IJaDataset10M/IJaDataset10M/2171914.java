package werkzeugkasten.dirbuildpath.action;

import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import werkzeugkasten.common.resource.ResourceUtil;
import werkzeugkasten.common.runtime.AdaptableUtil;
import werkzeugkasten.dirbuildpath.Constants;
import werkzeugkasten.dirbuildpath.job.AddDirBuildpathJob;

public class AddClasspathDirAction implements IActionDelegate {

    protected ISelection selection;

    public AddClasspathDirAction() {
    }

    public void run(IAction action) {
        if (this.selection.isEmpty()) {
            return;
        }
        IStructuredSelection ss = AdaptableUtil.to(this.selection, IStructuredSelection.class);
        if (ss == null) {
            return;
        }
        Map<IProject, List<IPath>> map = ResourceUtil.toProjectPathMap(ss.iterator());
        for (IProject p : map.keySet()) {
            new AddDirBuildpathJob(JavaCore.create(p), new ScopedPreferenceStore(new ProjectScope(p), Constants.ID_PLUGIN), map.get(p)) {

                {
                    this.forceupdate = true;
                }
            }.schedule();
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }
}
