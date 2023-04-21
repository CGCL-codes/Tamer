package org.jnormalform.ui;

import java.util.List;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jnormalform.builders.RefactoringSuggestionBuilder;

/**
 * Action to add <code>public</code> modifier to <code>IMember</code>.
 */
public class NormaliseAction implements IObjectActionDelegate {

    private ISelection selection;

    private IUser user = null;

    /**
	 * Need 0 arg public constructor as am an Action.
	 */
    public NormaliseAction() {
        this.user = new InteractiveUser();
    }

    public NormaliseAction(final IUser user) {
        this.user = user;
    }

    public NormaliseAction(final List factories) {
    }

    /**
	 * @see org.jnormalform.ChangeIMemberFlagAction#isChecked(IAction, IMember)
	 */
    protected boolean isChecked() {
        if (selection != null && selection instanceof ITreeSelection) {
            Object firstElement = ((ITreeSelection) selection).getFirstElement();
            if (firstElement != null && firstElement instanceof IJavaProject) {
                try {
                    IProject project = ((IJavaProject) firstElement).getProject();
                    ICommand[] buildSpec = project.getDescription().getBuildSpec();
                    return exists(buildSpec, RefactoringSuggestionBuilder.BUILDER_NAME);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
        action.setChecked(isChecked());
        System.out.println("Set checked.");
    }

    public void run(final IAction action) {
        try {
            IProject project = ((IJavaProject) ((ITreeSelection) selection).getFirstElement()).getProject();
            IProjectDescription description = project.getDescription();
            ICommand[] buildSpec = description.getBuildSpec();
            ICommand[] newBuildSpec;
            int buildspecLength = buildSpec.length;
            if (exists(buildSpec, RefactoringSuggestionBuilder.BUILDER_NAME)) {
                int free = 0;
                newBuildSpec = new ICommand[buildspecLength - 1];
                for (int i = 0; i < buildspecLength; i++) {
                    ICommand command = buildSpec[i];
                    if (command.getBuilderName().equals(RefactoringSuggestionBuilder.BUILDER_NAME)) {
                        continue;
                    }
                    newBuildSpec[free++] = command;
                }
                MarkProposalsAction.deleteMarketsFromProject(project);
            } else {
                ICommand newCommand = description.newCommand();
                newCommand.setBuilderName(RefactoringSuggestionBuilder.BUILDER_NAME);
                newBuildSpec = new ICommand[buildspecLength + 1];
                System.arraycopy(buildSpec, 0, newBuildSpec, 0, buildspecLength);
                newBuildSpec[buildspecLength] = newCommand;
            }
            description.setBuildSpec(newBuildSpec);
            project.setDescription(description, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private boolean exists(ICommand[] buildSpec, String id) {
        for (int i = 0; i < buildSpec.length; i++) {
            if (buildSpec[i].getBuilderName().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
        this.selection = selection;
        action.setEnabled(true);
        action.setChecked(isChecked());
    }

    public IUser getUser() {
        return user;
    }
}
