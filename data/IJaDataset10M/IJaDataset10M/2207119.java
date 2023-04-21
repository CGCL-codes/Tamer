package org.deft.repository.repositorymanager.actions;

import java.util.LinkedList;
import java.util.List;
import org.deft.repository.datamodel.Folder;
import org.deft.repository.datamodel.Fragment;
import org.deft.repository.datamodel.FragmentFilter;
import org.deft.repository.datamodel.Project;
import org.deft.repository.event.ProjectUpdateEvent;
import org.deft.repository.event.RepositoryUpdateEvent.UpdateType;
import org.deft.repository.repositorymanager.ContentManager;
import org.deft.repository.repositorymanager.DataStorageManager;

public class RemoveProjectAction extends RemoveAction {

    private DataStorageManager dataStorageManager;

    public RemoveProjectAction(ContentManager cm, DataStorageManager dsm, Project project) {
        super(cm, project);
        this.dataStorageManager = dsm;
    }

    @Override
    public void execute() {
        boolean success = dataStorageManager.removeProject(getProject());
        if (success) {
            contentManager.remove(getProject());
            executeSuccessful = true;
        }
    }

    @Override
    public List<RemoveAction> getPriorRemoveActions() {
        List<RemoveAction> actions = new LinkedList<RemoveAction>();
        List<Fragment> fragments = getAllFragmentsFromProjectRoot(getProject());
        for (Fragment fragment : fragments) {
            RemoveAction action = RemoveAction.createFragmentRemoveAction(fragment, contentManager, dataStorageManager);
            List<RemoveAction> actionsPrior = action.getPriorRemoveActions();
            actions.addAll(actionsPrior);
            actions.add(action);
        }
        List<RemoveAction> copy = getCopyWithoutDuplicateActions(actions);
        return copy;
    }

    private List<Fragment> getAllFragmentsFromProjectRoot(Project project) {
        List<Fragment> rootFragments = contentManager.getFragments(project, new RootFragmentsFilter());
        return rootFragments;
    }

    private class RootFragmentsFilter implements FragmentFilter {

        @Override
        public boolean accept(Fragment fragment) {
            Folder parentFolder = contentManager.getParentFolder(fragment);
            return parentFolder == null;
        }
    }

    @Override
    public ProjectUpdateEvent createEvent(boolean direct) {
        ProjectUpdateEvent event = new ProjectUpdateEvent(getProject(), UpdateType.REMOVED, direct);
        return event;
    }
}
