package hu.rtemplate.action;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class AbstractFileListConvertProject implements IObjectActionDelegate, IActionDelegate2 {

    IProject selectedProject;

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public static final String builderName = "hu.rtemplate.FileListBuilder";

    abstract void afterConvert() throws CoreException;

    @Override
    public void run(IAction action) {
        try {
            IProjectDescription pd = selectedProject.getDescription();
            ICommand[] newBuildSpec;
            ICommand[] oldBuildSpec = pd.getBuildSpec();
            ;
            if (toTemplate()) {
                newBuildSpec = new ICommand[oldBuildSpec.length + 1];
                for (int i = 0; i < oldBuildSpec.length; ++i) {
                    newBuildSpec[i + 1] = oldBuildSpec[i];
                }
                ICommand bc = pd.newCommand();
                bc.setBuilderName(builderName);
                newBuildSpec[0] = bc;
            } else {
                List<ICommand> newList = new ArrayList<ICommand>();
                for (int i = 0; i < oldBuildSpec.length; ++i) {
                    ICommand c = oldBuildSpec[i];
                    if (!builderName.equals(c.getBuilderName())) {
                        newList.add(c);
                    }
                }
                newBuildSpec = newList.toArray(new ICommand[] {});
            }
            pd.setBuildSpec(newBuildSpec);
            selectedProject.setDescription(pd, IResource.FORCE, null);
            afterConvert();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    abstract boolean toTemplate();

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        Object o = ((StructuredSelection) selection).getFirstElement();
        boolean enabled = false;
        if (o instanceof PlatformObject) {
            PlatformObject po = (PlatformObject) o;
            IProject project = (IProject) po.getAdapter(IProject.class);
            if (project != null) {
                this.selectedProject = project;
                enabled = toTemplate();
                try {
                    for (ICommand c : selectedProject.getDescription().getBuildSpec()) {
                        if (c.getBuilderName().equals(builderName)) {
                            enabled = !toTemplate();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        action.setEnabled(enabled);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IAction action) {
    }

    @Override
    public void runWithEvent(IAction action, Event event) {
        run(action);
    }
}
