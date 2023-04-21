package org.deft.repository.repositorymanager.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import org.deft.operation.OperationChain;
import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.check.ArtifactTypeKnownCheck;
import org.deft.repository.check.ArtifactValidCheck;
import org.deft.repository.check.CheckList;
import org.deft.repository.check.DeftError;
import org.deft.repository.check.DependentArtifactsAvailableCheck;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactReference;
import org.deft.repository.datamodel.ArtifactReferenceUpdateType;
import org.deft.repository.datamodel.ArtifactRepresentation;
import org.deft.repository.datamodel.Chapter;
import org.deft.repository.datamodel.Folder;
import org.deft.repository.datamodel.Fragment;
import org.deft.repository.datamodel.FragmentFilter;
import org.deft.repository.datamodel.FragmentType;
import org.deft.repository.datamodel.Project;
import org.deft.repository.datamodel.Tutorial;
import org.deft.repository.event.ArtifactReferenceUpdateEvent;
import org.deft.repository.event.FragmentUpdateEvent;
import org.deft.repository.event.ProjectUpdateEvent;
import org.deft.repository.event.RepositoryOptionsChangeListener;
import org.deft.repository.event.RepositoryRelationUpdateEvent;
import org.deft.repository.event.RepositoryUpdateEvent;
import org.deft.repository.event.RepositoryUpdateListener;
import org.deft.repository.event.SimpleRepositoryUpdateListener;
import org.deft.repository.exception.DeftCrossProjectRelationException;
import org.deft.repository.exception.DeftErrorException;
import org.deft.repository.exception.DeftIncompatibleNameException;
import org.deft.repository.exception.DeftIncompatibleTypeException;
import org.deft.repository.exception.DeftInvalidArgumentException;
import org.deft.repository.exception.DeftNullArgumentException;
import org.deft.repository.exception.DeftProjectAlreadyExistsException;
import org.deft.repository.fragmentsource.FragmentSource;
import org.deft.repository.fragmentsource.OriginalFragmentLocation;
import org.deft.repository.integrator.FileIntegrator;
import org.deft.repository.integrator.Integrator;
import org.deft.repository.repositorymanager.ConstraintsManager;
import org.deft.repository.repositorymanager.ContentManagementService;
import org.deft.repository.repositorymanager.ContentManager;
import org.deft.repository.repositorymanager.DataStorageManager;
import org.deft.repository.repositorymanager.OptionsService;
import org.deft.repository.repositorymanager.PersistenceManager;
import org.deft.repository.repositorymanager.actions.AddChapterToTutorialAction;
import org.deft.repository.repositorymanager.actions.CreateArtifactReferenceAction;
import org.deft.repository.repositorymanager.actions.CreateChapterAction;
import org.deft.repository.repositorymanager.actions.CreateFolderAction;
import org.deft.repository.repositorymanager.actions.CreateProjectAction;
import org.deft.repository.repositorymanager.actions.CreateTutorialAction;
import org.deft.repository.repositorymanager.actions.ImportArtifactAction;
import org.deft.repository.repositorymanager.actions.MoveToFolderAction;
import org.deft.repository.repositorymanager.actions.RemoveAction;
import org.deft.repository.repositorymanager.actions.RemoveArtifactReferenceAction;
import org.deft.repository.repositorymanager.actions.RemoveChapterFromTutorialAction;
import org.deft.repository.repositorymanager.actions.RemoveProjectAction;
import org.deft.repository.repositorymanager.actions.RenameFragmentAction;
import org.deft.repository.repositorymanager.actions.RenameProjectAction;
import org.deft.repository.repositorymanager.actions.SetArtifactReferenceCheckedAction;
import org.deft.repository.repositorymanager.actions.SetArtifactReferenceUncheckedAction;
import org.deft.repository.repositorymanager.actions.SetChapterPositionAction;
import org.deft.repository.repositorymanager.actions.UpdateArtifactAction;
import org.deft.repository.repositorymanager.actions.UpdateArtifactReferenceAction;
import org.deft.repository.repositorymanager.actions.UpdateArtifactWithAction;
import org.deft.repository.service.ExtensionService;
import org.deft.repository.service.InternalStateService;
import org.deft.util.StreamUtil;
import org.w3c.dom.Document;

public class ContentManagementServiceImpl implements ContentManagementService {

    private List<RepositoryUpdateListener> updateListeners = new LinkedList<RepositoryUpdateListener>();

    private ContentManager contentManager;

    private ConstraintsManager constraintsManager;

    private PersistenceManager persistenceManager;

    private DataStorageManager dataStorageManager;

    private OptionsService options;

    private ExtensionService extensions;

    private InternalStateService internalState;

    public ContentManagementServiceImpl(ContentManager contentManager, ConstraintsManager constraintsManager, PersistenceManager persistenceManager, DataStorageManager dataStorageManager, OptionsService options, ExtensionService extensions, InternalStateService internalState) {
        this.contentManager = contentManager;
        this.constraintsManager = constraintsManager;
        this.persistenceManager = persistenceManager;
        this.dataStorageManager = dataStorageManager;
        this.options = options;
        this.options.addRepositoryOptionsChangeListener(new OptionsChangeListener());
        this.extensions = extensions;
        this.internalState = internalState;
        this.addRepositoryUpdateListener(new PersistenceListener());
        this.addRepositoryUpdateListener(new LogListener());
    }

    @Override
    public void addChapterToTutorial(Tutorial tutorial, Chapter chapter) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftCrossProjectRelationException {
        AddChapterToTutorialAction action = new AddChapterToTutorialAction(contentManager, tutorial, chapter);
        executeAddChapterToTutorialActionAndFireEvent(action, true);
    }

    private void executeAddChapterToTutorialActionAndFireEvent(AddChapterToTutorialAction action, boolean direct) {
        action.execute();
        if (action.successfullyExecuted()) {
            fireChapterAddedToTutorial(action.createEvent(true));
        }
    }

    @Override
    public void removeChapterFromTutorial(Tutorial tutorial, Chapter chapter) throws DeftNullArgumentException, DeftInvalidArgumentException {
        RemoveChapterFromTutorialAction action = new RemoveChapterFromTutorialAction(contentManager, tutorial, chapter);
        executeRemoveChapterFromTutorialActionAndFireEvent(action, true);
    }

    private void executeRemoveChapterFromTutorialActionAndFireEvent(RemoveChapterFromTutorialAction action, boolean direct) {
        action.execute();
        if (action.successfullyExecuted()) {
            fireChapterRemovedFromTutorial(action.createEvent(true));
        }
    }

    @Override
    public void setChapterPosition(Tutorial tutorial, Chapter chapter, int position) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftCrossProjectRelationException {
        SetChapterPositionAction action = new SetChapterPositionAction(contentManager, tutorial, chapter, position);
        executeSetChapterPositionActionAndFireEvent(action, true);
    }

    private void executeSetChapterPositionActionAndFireEvent(SetChapterPositionAction action, boolean direct) {
        action.execute();
        if (action.successfullyExecuted()) {
            fireChapterPositionChanged(action.createEvent(true));
        }
    }

    @Override
    public Chapter createChapter(Project project, String name, String chapterType) throws DeftNullArgumentException, DeftIncompatibleNameException, DeftInvalidArgumentException {
        CreateChapterAction action = new CreateChapterAction(contentManager, dataStorageManager, project, name, chapterType);
        Chapter chapter = executeCreateChapterActionAndFireEvent(action);
        return chapter;
    }

    @Override
    public Chapter createChapter(Folder parent, String name, String chapterType) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftIncompatibleTypeException, DeftIncompatibleNameException {
        CreateChapterAction action = new CreateChapterAction(contentManager, dataStorageManager, parent, name, chapterType);
        Chapter chapter = executeCreateChapterActionAndFireEvent(action);
        return chapter;
    }

    private Chapter executeCreateChapterActionAndFireEvent(CreateChapterAction action) {
        action.execute();
        if (action.successfullyExecuted()) {
            Chapter chapter = (Chapter) action.getCreatedObject();
            fireFragmentCreated(action.createEvent(true));
            return chapter;
        } else {
            return null;
        }
    }

    @Override
    public Folder createFolder(Project project, FragmentType fragType, String name) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftIncompatibleNameException {
        CreateFolderAction action = new CreateFolderAction(contentManager, project, fragType, name);
        Folder folder = executeCreateFolderActionAndFireEvent(action);
        return folder;
    }

    @Override
    public Folder createFolder(Folder parent, String name) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftIncompatibleNameException {
        CreateFolderAction action = new CreateFolderAction(contentManager, parent, name);
        Folder folder = executeCreateFolderActionAndFireEvent(action);
        return folder;
    }

    private Folder executeCreateFolderActionAndFireEvent(CreateFolderAction action) {
        action.execute();
        if (action.successfullyExecuted()) {
            Folder folder = (Folder) action.getCreatedObject();
            fireFragmentCreated(action.createEvent(true));
            return folder;
        } else {
            return null;
        }
    }

    @Override
    public Project createProject(String name, String chapterType) throws DeftProjectAlreadyExistsException, DeftNullArgumentException {
        CreateProjectAction action = new CreateProjectAction(contentManager, dataStorageManager, name, chapterType);
        action.execute();
        if (action.successfullyExecuted()) {
            Project project = (Project) action.getCreatedObject();
            fireProjectCreated(action.createEvent(true));
            return project;
        } else {
            return null;
        }
    }

    @Override
    public Tutorial createTutorial(Project project, String name) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftIncompatibleNameException {
        CreateTutorialAction action = new CreateTutorialAction(contentManager, project, name);
        Tutorial tutorial = executeCreateTutorialActionAndFireEvent(action);
        return tutorial;
    }

    @Override
    public Tutorial createTutorial(Folder parent, String name) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftIncompatibleTypeException, DeftIncompatibleNameException {
        CreateTutorialAction action = new CreateTutorialAction(contentManager, parent, name);
        Tutorial tutorial = executeCreateTutorialActionAndFireEvent(action);
        return tutorial;
    }

    private Tutorial executeCreateTutorialActionAndFireEvent(CreateTutorialAction action) {
        action.execute();
        if (action.successfullyExecuted()) {
            Tutorial tutorial = (Tutorial) action.getCreatedObject();
            fireFragmentCreated(action.createEvent(true));
            return tutorial;
        } else {
            return null;
        }
    }

    @Override
    public String getProjectType(Project project) throws DeftInvalidArgumentException {
        String projectType = contentManager.getProjectType(project);
        return projectType;
    }

    @Override
    public File getTempDirectory(Project project) {
        File tempDir = dataStorageManager.getTempDirectory(project);
        return tempDir;
    }

    @Override
    public String getChapterType(Chapter chapter) throws DeftInvalidArgumentException {
        String chapterType = contentManager.getChapterType(chapter);
        return chapterType;
    }

    @Override
    public List<Chapter> getChaptersForTutorial(Tutorial tutorial) throws DeftNullArgumentException, DeftInvalidArgumentException {
        List<Chapter> chapters = contentManager.getChaptersForTutorial(tutorial);
        return chapters;
    }

    @Override
    public List<Fragment> getChildren(Folder folder) throws DeftNullArgumentException, DeftInvalidArgumentException {
        List<Fragment> children = contentManager.getChildren(folder);
        return children;
    }

    @Override
    public List<Fragment> getChildren(Project project, FragmentType fragmentType) throws DeftNullArgumentException, DeftInvalidArgumentException {
        List<Fragment> children = contentManager.getChildren(project, fragmentType);
        return children;
    }

    @Override
    public Fragment getFragment(String id) throws DeftNullArgumentException {
        Fragment fragment = contentManager.getFragment(id);
        return fragment;
    }

    @Override
    public FragmentType getFragmentTypeAffiliation(Folder folder) throws DeftNullArgumentException, DeftInvalidArgumentException {
        FragmentType fragmentType = contentManager.getFragmentTypeAffiliation(folder);
        return fragmentType;
    }

    @Override
    public List<Fragment> getFragments(Project project, FragmentFilter filter) throws DeftNullArgumentException, DeftInvalidArgumentException {
        List<Fragment> fragments = contentManager.getFragments(project, filter);
        return fragments;
    }

    @Override
    public Folder getParentFolder(Fragment fragment) throws DeftNullArgumentException {
        Folder folder = contentManager.getParentFolder(fragment);
        return folder;
    }

    @Override
    public Project getProject(String name) throws DeftNullArgumentException {
        Project project = contentManager.getProject(name);
        return project;
    }

    @Override
    public Project getProjectById(String id) throws DeftNullArgumentException {
        for (Project project : contentManager.getProjects()) {
            if (project.getId().equals(id)) {
                return project;
            }
        }
        return null;
    }

    @Override
    public Project getProjectOf(Fragment fragment) throws DeftNullArgumentException, DeftInvalidArgumentException {
        Project project = contentManager.getProjectOf(fragment);
        return project;
    }

    @Override
    public List<Project> getProjects() {
        List<Project> projects = contentManager.getProjects();
        return projects;
    }

    @Override
    public List<ArtifactRepresentation> getRepresentationsForArtifact(Artifact artifact) throws DeftNullArgumentException, DeftInvalidArgumentException {
        return null;
    }

    @Override
    public List<Tutorial> getTutorialsForChapter(Chapter chapter) throws DeftNullArgumentException, DeftInvalidArgumentException {
        List<Tutorial> tutorials = contentManager.getTutorialsForChapter(chapter);
        return tutorials;
    }

    @Override
    public Artifact importArtifact(Project project, FragmentSource fragmentSource, OriginalFragmentLocation location) throws DeftErrorException {
        String name = location.getName();
        String artifactTypeId = fragmentSource.getArtifactTypeIds(location).get(0);
        Artifact artifact = importArtifact(project, artifactTypeId, fragmentSource, location, name);
        return artifact;
    }

    @Override
    public Artifact importArtifact(Project project, String artifactTypeId, FragmentSource fragmentSource, OriginalFragmentLocation location, String name) throws DeftErrorException {
        int latestRevision = contentManager.getLatestRevision(project);
        int newRevision = latestRevision + 1;
        Artifact artifact = importArtifact(project, artifactTypeId, fragmentSource, location, name, newRevision);
        return artifact;
    }

    @Override
    public Artifact importArtifact(Project project, String artifactTypeId, FragmentSource fragmentSource, OriginalFragmentLocation location, String name, int revision) throws DeftErrorException {
        ArtifactType artifactType = extensions.getArtifactType(artifactTypeId);
        checkFragmentValidity(artifactType, fragmentSource, location);
        ImportArtifactAction action = new ImportArtifactAction(contentManager, dataStorageManager, project, artifactType, fragmentSource, location, name, revision);
        Artifact artifact = executeImportArtifactActionAndFireEvent(action);
        return artifact;
    }

    /**
	 * Checks whether the type of fragment is known and, if so, checks whether
	 * it is valid. If the fragment is unknown or contains errors it cannot be
	 * imported into the repository. A DeftErrorException is thrown, indicating
	 * what was wrong.
	 */
    private void checkFragmentValidity(ArtifactType artifactType, FragmentSource fragmentSource, OriginalFragmentLocation location) {
        CheckList list = new CheckList();
        list.add(new ArtifactTypeKnownCheck(fragmentSource, location));
        list.add(new ArtifactValidCheck(artifactType, fragmentSource, location));
        list.add(new DependentArtifactsAvailableCheck(artifactType, fragmentSource, location));
        List<DeftError> errors = list.performChecks();
        if (!errors.isEmpty()) {
            for (DeftError error : errors) {
                System.err.println(error.getErrorMessage());
            }
            throw new DeftErrorException(errors);
        }
    }

    @Override
    public Artifact importArtifact(Folder parent, FragmentSource fragmentSource, OriginalFragmentLocation location) throws DeftErrorException {
        String name = location.getName();
        String artifactTypeId = fragmentSource.getArtifactTypeIds(location).get(0);
        Artifact artifact = importArtifact(parent, artifactTypeId, fragmentSource, location, name);
        return artifact;
    }

    @Override
    public Artifact importArtifact(Folder parent, String artifactTypeId, FragmentSource fragmentSource, OriginalFragmentLocation location, String name) throws DeftErrorException {
        Project project = contentManager.getProjectOf(parent);
        int latestRevision = contentManager.getLatestRevision(project);
        int newRevision = latestRevision + 1;
        Artifact artifact = importArtifact(parent, artifactTypeId, fragmentSource, location, name, newRevision);
        return artifact;
    }

    @Override
    public Artifact importArtifact(Folder parent, String artifactTypeId, FragmentSource fragmentSource, OriginalFragmentLocation location, String name, int revision, String... dependentArtifactIds) {
        ArtifactType artifactType = extensions.getArtifactType(artifactTypeId);
        checkFragmentValidity(artifactType, fragmentSource, location);
        ImportArtifactAction action = new ImportArtifactAction(contentManager, dataStorageManager, parent, artifactType, fragmentSource, location, name, revision);
        Artifact artifact = executeImportArtifactActionAndFireEvent(action);
        return artifact;
    }

    private Artifact executeImportArtifactActionAndFireEvent(ImportArtifactAction action) {
        action.execute();
        if (action.successfullyExecuted()) {
            Artifact artifact = (Artifact) action.getCreatedObject();
            fireFragmentCreated(action.createEvent(true));
            return artifact;
        } else {
            return null;
        }
    }

    @Override
    public void updateArtifact(Artifact artifact, int revision) {
        ArtifactType type = extensions.getArtifactType(artifact.getArtifactTypeId());
        FragmentSource fragmentSource = extensions.getFragmentSource(artifact.getFragmentSourceId());
        int latestRevision = contentManager.getLatestRevision(artifact);
        OriginalFragmentLocation location = fragmentSource.createLocationObject(artifact.getOriginalLocation(latestRevision));
        checkFragmentValidity(type, fragmentSource, location);
        UpdateArtifactAction action = new UpdateArtifactAction(extensions, contentManager, dataStorageManager, artifact, type, fragmentSource, location, revision);
        action.execute();
        if (action.successfullyExecuted()) {
            fireArtifactUpdated(action.createEvent(true));
        }
    }

    public void updateArtifactWith(Artifact artifact, FragmentSource fragmentSource, OriginalFragmentLocation location, int revision) {
        ArtifactType type = extensions.getArtifactType(artifact.getArtifactTypeId());
        checkFragmentValidity(type, fragmentSource, location);
        UpdateArtifactWithAction action = new UpdateArtifactWithAction(extensions, contentManager, dataStorageManager, artifact, type, fragmentSource, location, revision);
        action.execute();
        if (action.successfullyExecuted()) {
            fireArtifactUpdated(action.createEvent(true));
        }
    }

    @Override
    public void moveToFolder(Fragment fragment, Folder target) throws DeftNullArgumentException, DeftCrossProjectRelationException, DeftIncompatibleTypeException, DeftIncompatibleNameException {
        MoveToFolderAction action = new MoveToFolderAction(contentManager, fragment, target);
        action.execute();
        if (action.successfullyExecuted()) {
            fireFragmentMoved(action.createEvent(true));
        }
    }

    @Override
    public boolean removeFragment(Fragment fragment) throws DeftNullArgumentException, DeftInvalidArgumentException {
        RemoveAction action = RemoveAction.createFragmentRemoveAction(fragment, contentManager, dataStorageManager);
        List<RemoveAction> priorRemoveActions = action.getPriorRemoveActions();
        boolean success = executePriorRemoveActionsAndFireEvents(priorRemoveActions);
        if (success) {
            action.execute();
            if (action.successfullyExecuted()) {
                RepositoryUpdateEvent event = action.createEvent(true);
                fireEvent(event);
                return true;
            }
        }
        return false;
    }

    private boolean executePriorRemoveActionsAndFireEvents(List<RemoveAction> actions) {
        for (RemoveAction action : actions) {
            action.execute();
            if (action.successfullyExecuted()) {
                RepositoryUpdateEvent event = action.createEvent(false);
                fireEvent(event);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeProject(Project project) throws DeftNullArgumentException, DeftInvalidArgumentException {
        RemoveAction action = new RemoveProjectAction(contentManager, dataStorageManager, project);
        List<RemoveAction> priorRemoveActions = action.getPriorRemoveActions();
        boolean success = executePriorRemoveActionsAndFireEvents(priorRemoveActions);
        if (success) {
            action.execute();
            if (action.successfullyExecuted()) {
                RepositoryUpdateEvent event = action.createEvent(true);
                fireEvent(event);
                return true;
            }
        }
        return false;
    }

    @Override
    public void rename(Project project, String newName) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftProjectAlreadyExistsException {
        RenameProjectAction action = new RenameProjectAction(contentManager, project, newName);
        action.execute();
        if (action.successfullyExecuted()) {
            fireProjectRenamed(action.createEvent(true));
        }
    }

    @Override
    public void rename(Fragment fragment, String newName) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftIncompatibleNameException {
        RenameFragmentAction action = new RenameFragmentAction(contentManager, fragment, newName);
        action.execute();
        if (action.successfullyExecuted()) {
            fireFragmentRenamed(action.createEvent(true));
        }
    }

    @Override
    public int getChapterPosition(Tutorial tutorial, Chapter chapter) throws DeftNullArgumentException, DeftInvalidArgumentException, DeftCrossProjectRelationException {
        int position = contentManager.getChapterPosition(tutorial, chapter);
        return position;
    }

    /**
	 * Removes all contents from the repository. This method is used when the
	 * repository location is changed and the repository contents of the old
	 * location have to be discarded.
	 */
    private void clearRepository() {
        contentManager.clear();
    }

    public void addRepositoryUpdateListener(RepositoryUpdateListener listener) {
        if (listener != null && !updateListeners.contains(listener)) {
            updateListeners.add(listener);
        }
    }

    public void removeRepositoryUpdateListener(RepositoryUpdateListener listener) {
        updateListeners.remove(listener);
    }

    /**
	 * Helper method that takes a generic event and dispatches it to a concrete method
	 * to really fire the event. This method is necessary because a remove action
	 * might remove other repository content recursively, which also results in events
	 * to be fired. This might result in an arbitrarily complex chain of removals and
	 * events, which is best handled in a generic way with the help of this method. 
	 */
    private void fireEvent(RepositoryUpdateEvent event) {
        if (event instanceof ProjectUpdateEvent) {
            fireEvent((ProjectUpdateEvent) event);
        } else if (event instanceof FragmentUpdateEvent) {
            fireEvent((FragmentUpdateEvent) event);
        } else if (event instanceof RepositoryRelationUpdateEvent) {
            fireEvent((RepositoryRelationUpdateEvent) event);
        } else if (event instanceof ArtifactReferenceUpdateEvent) {
            fireEvent((ArtifactReferenceUpdateEvent) event);
        } else {
            throw new IllegalStateException("Don't know how to fire unknown event type");
        }
    }

    private void fireEvent(ProjectUpdateEvent event) {
        switch(event.getUpdateType()) {
            case CREATED:
                {
                    fireProjectCreated(event);
                    return;
                }
            case REMOVED:
                {
                    fireProjectRemoved(event);
                    return;
                }
            case RENAMED:
                {
                    fireProjectRenamed(event);
                    return;
                }
        }
    }

    private void fireEvent(FragmentUpdateEvent event) {
        switch(event.getUpdateType()) {
            case CREATED:
                {
                    fireFragmentCreated(event);
                    return;
                }
            case REMOVED:
                {
                    fireFragmentRemoved(event);
                    return;
                }
            case RENAMED:
                {
                    fireFragmentRenamed(event);
                    return;
                }
            case MOVED:
                {
                    fireFragmentMoved(event);
                }
            case UPDATED:
                {
                    fireArtifactUpdated(event);
                }
        }
    }

    private void fireEvent(RepositoryRelationUpdateEvent event) {
        switch(event.getUpdateType()) {
            case CREATED:
                {
                    if (event.getRelationType() == RepositoryRelationUpdateEvent.RelationType.CHAPTER_TO_TUTORIAL) {
                        fireChapterAddedToTutorial(event);
                    }
                    return;
                }
            case REMOVED:
                {
                    if (event.getRelationType() == RepositoryRelationUpdateEvent.RelationType.CHAPTER_TO_TUTORIAL) {
                        fireChapterRemovedFromTutorial(event);
                    }
                    return;
                }
            case MOVED:
                {
                    if (event.getRelationType() == RepositoryRelationUpdateEvent.RelationType.CHAPTER_TO_TUTORIAL) {
                        fireChapterPositionChanged(event);
                    }
                    return;
                }
        }
    }

    private void fireEvent(ArtifactReferenceUpdateEvent event) {
        switch(event.getUpdateType()) {
            case CREATED:
                {
                    fireArtifactReferenceCreated(event);
                    return;
                }
            case REMOVED:
                {
                    fireArtifactReferenceRemoved(event);
                    return;
                }
        }
    }

    private void fireProjectCreated(ProjectUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.projectCreated(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireProjectRemoved(ProjectUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.projectRemoved(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireProjectRenamed(ProjectUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.projectRenamed(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireFragmentCreated(FragmentUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.fragmentCreated(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireFragmentRemoved(FragmentUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.fragmentRemoved(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireFragmentMoved(FragmentUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.fragmentMoved(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireFragmentRenamed(FragmentUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.fragmentRenamed(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireArtifactUpdated(FragmentUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.artifactUpdated(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireChapterAddedToTutorial(RepositoryRelationUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.chapterAddedToTutorial(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireChapterRemovedFromTutorial(RepositoryRelationUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.chapterRemovedFromTutorial(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireChapterPositionChanged(RepositoryRelationUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.chapterPositionChanged(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireArtifactReferenceCreated(ArtifactReferenceUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.artifactReferenceCreated(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireArtifactReferenceRemoved(ArtifactReferenceUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.artifactReferenceRemoved(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireArtifactReferenceUpdated(ArtifactReferenceUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.artifactReferenceUpdated(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireArtifactReferenceChecked(ArtifactReferenceUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.artifactReferenceChecked(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireArtifactReferenceUnchecked(ArtifactReferenceUpdateEvent event) {
        for (RepositoryUpdateListener listener : updateListeners) {
            try {
                listener.artifactReferenceUnchecked(event);
            } catch (Exception e) {
                System.err.println("Exception in listener, continuing with next one");
                e.printStackTrace();
            }
        }
    }

    private void fireRepositoryContentsLoaded() {
        for (RepositoryUpdateListener listener : updateListeners) {
            listener.contentLoaded();
        }
    }

    private class PersistenceListener extends SimpleRepositoryUpdateListener {

        public void repositoryUpdated() {
            persistenceManager.saveRepositoryContents();
        }
    }

    private class LogListener implements RepositoryUpdateListener {

        @Override
        public void fragmentCreated(FragmentUpdateEvent event) {
            System.out.println("Fragment created -" + " Name: " + event.getFragment().getName() + " Id: " + event.getFragment().getId());
        }

        @Override
        public void fragmentRemoved(FragmentUpdateEvent event) {
            System.out.println("Fragment removed -" + " Name: " + event.getFragment().getName() + " Id: " + event.getFragment().getId());
        }

        @Override
        public void projectCreated(ProjectUpdateEvent event) {
            System.out.println("Project created -" + " Name: " + event.getProject().getName() + " Id: " + event.getProject().getId());
        }

        @Override
        public void projectRemoved(ProjectUpdateEvent event) {
            System.out.println("Project removed -" + " Name: " + event.getProject().getName() + " Id: " + event.getProject().getId());
        }

        @Override
        public void chapterAddedToTutorial(RepositoryRelationUpdateEvent event) {
            System.out.println("Chapter " + event.getSecondaryFragment().getName() + " added to tutorial " + event.getPrimaryFragment().getName());
        }

        @Override
        public void chapterRemovedFromTutorial(RepositoryRelationUpdateEvent event) {
            System.out.println("Chapter " + event.getSecondaryFragment().getName() + " removed from tutorial " + event.getPrimaryFragment().getName());
        }

        @Override
        public void fragmentMoved(FragmentUpdateEvent event) {
        }

        @Override
        public void fragmentRenamed(FragmentUpdateEvent event) {
        }

        @Override
        public void artifactUpdated(FragmentUpdateEvent event) {
        }

        @Override
        public void projectRenamed(ProjectUpdateEvent event) {
        }

        @Override
        public void chapterPositionChanged(RepositoryRelationUpdateEvent event) {
        }

        @Override
        public void contentLoaded() {
        }

        @Override
        public void artifactReferenceCreated(ArtifactReferenceUpdateEvent event) {
        }

        @Override
        public void artifactReferenceRemoved(ArtifactReferenceUpdateEvent event) {
        }

        @Override
        public void artifactReferenceUpdated(ArtifactReferenceUpdateEvent event) {
        }

        @Override
        public void artifactReferenceChecked(ArtifactReferenceUpdateEvent event) {
            System.out.println("Reference " + event.getChapter().getName() + "-" + event.getArtifact().getName() + " updated.");
        }

        @Override
        public void artifactReferenceUnchecked(ArtifactReferenceUpdateEvent event) {
            System.out.println("Reference " + event.getChapter().getName() + "-" + event.getArtifact().getName() + " unchecked.");
        }
    }

    private class OptionsChangeListener implements RepositoryOptionsChangeListener {

        @Override
        public void repositoryLocationAboutToChange() {
            persistenceManager.saveRepositoryContents();
            clearRepository();
        }

        @Override
        public void repositoryLocationChanged() {
            persistenceManager.loadRepositoryContents();
            fireRepositoryContentsLoaded();
        }
    }

    @Override
    public boolean canContainChapter(Tutorial tutorial, Chapter chapter) {
        boolean result = constraintsManager.canContainChapter(tutorial, chapter);
        return result;
    }

    @Override
    public boolean canContainFragment(Folder parent, Fragment child) {
        boolean result = constraintsManager.canContainFragment(parent, child);
        return result;
    }

    @Override
    public boolean canContainFragmentAtRoot(Project project, FragmentType fragmentType, Fragment child) {
        boolean result = constraintsManager.canContainFragmentAtRoot(project, fragmentType, child);
        return result;
    }

    @Override
    public boolean canContainFragmentName(Folder parent, String childName) {
        boolean result = constraintsManager.canContainFragmentName(parent, childName);
        return result;
    }

    @Override
    public boolean canContainFragmentName(Project project, FragmentType fragmentType, String childName) {
        boolean result = constraintsManager.canContainFragmentNameAtRoot(project, fragmentType, childName);
        return result;
    }

    @Override
    public boolean canContainFragmentType(Folder parent, FragmentType fragmentType) {
        boolean result = constraintsManager.canContainFragmentType(parent, fragmentType);
        return result;
    }

    @Override
    public boolean containsChapter(Tutorial tutorial, Chapter chapter) {
        boolean result = constraintsManager.containsChapter(tutorial, chapter);
        return result;
    }

    @Override
    public boolean containsFragmentName(Folder parent, String childName) {
        boolean result = constraintsManager.containsFragmentName(parent, childName);
        return result;
    }

    @Override
    public boolean containsFragmentNameAtRoot(Project project, FragmentType fragmentType, String childName) {
        boolean result = constraintsManager.containsFragmentNameAtRoot(project, fragmentType, childName);
        return result;
    }

    @Override
    public boolean projectExists(String name) {
        boolean result = constraintsManager.projectExists(name);
        return result;
    }

    @Override
    public byte[] getChapterContent(Chapter chapter) {
        InputStream is = dataStorageManager.getInputStream(chapter);
        try {
            byte[] contents = new byte[is.available()];
            is.read(contents, 0, contents.length);
            is.close();
            return contents;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] getArtifactContent(Artifact artifact) {
        int latestRevision = contentManager.getLatestRevision(artifact);
        byte[] contents = getArtifactContent(artifact, latestRevision);
        return contents;
    }

    @Override
    public byte[] getArtifactContent(Artifact artifact, int revision) {
        List<Integer> revisions = contentManager.getRevisionIndexes(artifact);
        int closestValidRevision = getHighestNumberLessOrEqualTo(revisions, revision);
        InputStream is = dataStorageManager.getInputStream(artifact, closestValidRevision);
        byte[] contents = StreamUtil.readByteContent(is);
        return contents;
    }

    @Override
    public byte[] getContent(Artifact artifact, String dependentFileId) {
        int latestRevision = contentManager.getLatestRevision(artifact);
        byte[] contents = getContent(artifact, dependentFileId, latestRevision);
        return contents;
    }

    @Override
    public byte[] getContent(Artifact artifact, String dependentFileId, int revision) {
        List<Integer> revisions = contentManager.getRevisionIndexes(artifact);
        int closestValidRevision = getHighestNumberLessOrEqualTo(revisions, revision);
        InputStream is = dataStorageManager.getInputStream(artifact, dependentFileId, closestValidRevision);
        byte[] contents = StreamUtil.readByteContent(is);
        return contents;
    }

    @Override
    public String getOriginalLocation(Artifact artifact) {
        int latestRevision = contentManager.getLatestRevision(artifact);
        String location = getOriginalLocation(artifact, latestRevision);
        return location;
    }

    @Override
    public String getOriginalLocation(Artifact artifact, int revision) {
        List<Integer> revisions = contentManager.getRevisionIndexes(artifact);
        int closestValidRevision = getHighestNumberLessOrEqualTo(revisions, revision);
        String location = contentManager.getOriginalLocation(artifact, closestValidRevision);
        return location;
    }

    @Override
    public String getOriginalLocation(Artifact artifact, String dependentFileId) {
        int latestRevision = contentManager.getLatestRevision(artifact);
        String location = getOriginalLocation(artifact, dependentFileId, latestRevision);
        return location;
    }

    @Override
    public String getOriginalLocation(Artifact artifact, String dependentFileId, int revision) {
        List<Integer> revisions = contentManager.getRevisionIndexes(artifact);
        int closestValidRevision = getHighestNumberLessOrEqualTo(revisions, revision);
        String location = contentManager.getOriginalLocation(artifact, dependentFileId, closestValidRevision);
        return location;
    }

    @Override
    public List<String> getDependentFileIds(Artifact artifact) {
        int latestRevision = contentManager.getLatestRevision(artifact);
        List<String> ids = getDependentFileIds(artifact, latestRevision);
        return ids;
    }

    @Override
    public List<String> getDependentFileIds(Artifact artifact, int revision) {
        List<Integer> revisions = contentManager.getRevisionIndexes(artifact);
        int closestValidRevision = getHighestNumberLessOrEqualTo(revisions, revision);
        List<String> ids = contentManager.getDependentFileIds(artifact, closestValidRevision);
        return ids;
    }

    private int getHighestNumberLessOrEqualTo(List<Integer> numbers, int threshold) {
        int latestValid = -1;
        for (int number : numbers) {
            if (number > latestValid && number <= threshold) {
                latestValid = number;
            }
        }
        return latestValid;
    }

    @Override
    public void saveChapterContent(Chapter chapter, byte[] content) {
        OutputStream os = dataStorageManager.getOutputStream(chapter);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        try {
            bos.write(content);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArtifactReference createArtifactReference(Chapter chapter, Artifact artifact, OperationChain chain, Integrator integrator) {
        CreateArtifactReferenceAction action = new CreateArtifactReferenceAction(contentManager, dataStorageManager, chapter, artifact, chain, integrator);
        action.execute();
        if (action.successfullyExecuted()) {
            internalState.saveRecentlyUsedArtifact(artifact);
            ArtifactReference ref = (ArtifactReference) action.getCreatedObject();
            fireArtifactReferenceCreated(action.createEvent(true));
            return ref;
        } else {
            return null;
        }
    }

    @Override
    public void updateArtifactReference(ArtifactReference reference, OperationChain operationChain, Integrator integrator) {
        UpdateArtifactReferenceAction action = new UpdateArtifactReferenceAction(contentManager, dataStorageManager, reference, operationChain, integrator);
        action.execute();
        if (action.successfullyExecuted()) {
            fireArtifactReferenceUpdated(action.createEvent(true));
        }
    }

    @Override
    public FileIntegrator getFileIntegrator(ArtifactReference ref) {
        String integratorId = ref.getIntegratorId();
        FileIntegrator integrator = extensions.getFileIntegrator(integratorId);
        Document doc = dataStorageManager.getArtifactReferenceIntegratorConfigContent(ref);
        integrator.getIntegratorConfiguration().loadConfigurationForIntegrator(doc);
        return integrator;
    }

    @Override
    public OperationChain getOperationChain(ArtifactReference ref) {
        String chainId = ref.getOperationChainId();
        OperationChain chain = extensions.getOperationChain(chainId);
        Document doc = dataStorageManager.getArtifactReferenceOperationChainConfigContent(ref);
        chain.getChainConfigurations().loadConfigurationForOperationChain(doc);
        return chain;
    }

    @Override
    public ArtifactReference getArtifactReference(String refId) {
        ArtifactReference ref = contentManager.getArtifactReference(refId);
        return ref;
    }

    @Override
    public boolean removeArtifactReference(ArtifactReference reference) {
        RemoveArtifactReferenceAction action = new RemoveArtifactReferenceAction(contentManager, dataStorageManager, reference);
        List<RemoveAction> priorRemoveActions = action.getPriorRemoveActions();
        boolean success = executePriorRemoveActionsAndFireEvents(priorRemoveActions);
        if (success) {
            action.execute();
            if (action.successfullyExecuted()) {
                RepositoryUpdateEvent event = action.createEvent(true);
                fireEvent(event);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ArtifactReference> getArtifactReferences(Project project) {
        List<ArtifactReference> refs = contentManager.getArtifactReferences(project);
        return refs;
    }

    @Override
    public List<ArtifactReference> getArtifactReferences(Chapter chapter) {
        List<ArtifactReference> refs = contentManager.getArtifactReferences(chapter);
        return refs;
    }

    @Override
    public List<ArtifactReference> getArtifactReferences(Artifact artifact) {
        List<ArtifactReference> refs = contentManager.getArtifactReferences(artifact);
        return refs;
    }

    @Override
    public int getLatestCheckedRevision(ArtifactReference reference) {
        int latestCheckedRevision = contentManager.getLatestCheckedRevision(reference);
        return latestCheckedRevision;
    }

    @Override
    public List<ArtifactReference> getUncheckedReferences(Project project) {
        List<ArtifactReference> allReferences = getArtifactReferences(project);
        List<ArtifactReference> uncheckedReferences = getUncheckedReferences(allReferences);
        return uncheckedReferences;
    }

    private List<ArtifactReference> getUncheckedReferences(List<ArtifactReference> allReferences) {
        List<ArtifactReference> uncheckedReferences = new LinkedList<ArtifactReference>();
        for (ArtifactReference reference : allReferences) {
            if (isUnchecked(reference)) {
                uncheckedReferences.add(reference);
            }
        }
        return uncheckedReferences;
    }

    @Override
    public boolean isUnchecked(ArtifactReference reference) {
        Artifact artifact = reference.getArtifact();
        if (artifact == null) {
            return false;
        } else {
            int latestArtifactRevision = contentManager.getLatestRevision(artifact);
            int referenceRevision = contentManager.getLatestCheckedRevision(reference);
            boolean unchecked = (latestArtifactRevision != referenceRevision);
            return unchecked;
        }
    }

    @Override
    public List<ArtifactReference> getUncheckedReferences(Chapter chapter) {
        List<ArtifactReference> allReferences = getArtifactReferences(chapter);
        List<ArtifactReference> uncheckedReferences = getUncheckedReferences(allReferences);
        return uncheckedReferences;
    }

    @Override
    public void setArtifactReferenceChecked(ArtifactReference reference) {
        SetArtifactReferenceCheckedAction action = new SetArtifactReferenceCheckedAction(contentManager, reference);
        action.execute();
        if (action.successfullyExecuted()) {
            fireArtifactReferenceChecked(action.createEvent(true));
        }
    }

    @Override
    public void setArtifactReferenceUnchecked(ArtifactReference reference) {
        SetArtifactReferenceUncheckedAction action = new SetArtifactReferenceUncheckedAction(contentManager, reference);
        action.execute();
        if (action.successfullyExecuted()) {
            fireArtifactReferenceUnchecked(action.createEvent(true));
        }
    }

    @Override
    public ArtifactReferenceUpdateType getUpdateType(ArtifactReference reference) {
        ArtifactReferenceUpdateType updateType = contentManager.getUpdateType(reference);
        return updateType;
    }

    @Override
    public Chapter getChapter(ArtifactReference reference) {
        Chapter chapter = contentManager.getChapter(reference);
        return chapter;
    }

    @Override
    public Artifact getArtifact(ArtifactReference reference) {
        Artifact artifact = contentManager.getArtifact(reference);
        return artifact;
    }

    @Override
    public List<Artifact> getArtifactsWithMissingOrigin(Project project) {
        List<Artifact> originMissingArtifacts = new LinkedList<Artifact>();
        List<Fragment> allArtifacts = project.getFragments(FragmentFilter.ARTIFACTFILTER);
        for (Fragment fragment : allArtifacts) {
            Artifact artifact = (Artifact) fragment;
            boolean exists = dataStorageManager.isArtifactOriginStillExisting(artifact);
            if (!exists) {
                originMissingArtifacts.add(artifact);
            }
        }
        return originMissingArtifacts;
    }

    @Override
    public List<Artifact> getArtifactsWithChangedOrigin(Project project) {
        List<Artifact> originChangedArtifacts = new LinkedList<Artifact>();
        List<Fragment> allArtifacts = getFragments(project, FragmentFilter.ARTIFACTFILTER);
        for (Fragment fragment : allArtifacts) {
            Artifact artifact = (Artifact) fragment;
            int rev = contentManager.getLatestRevision(artifact);
            boolean changed = dataStorageManager.hasArtifactOriginChanged(artifact, rev);
            if (changed) {
                originChangedArtifacts.add(artifact);
            }
        }
        return originChangedArtifacts;
    }

    @Override
    public int getLatestRevision(Project project) {
        int latestRevision = contentManager.getLatestRevision(project);
        return latestRevision;
    }

    @Override
    public void setConfiguration(Integrator integrator, ArtifactReference reference) {
        Document config = dataStorageManager.getArtifactReferenceIntegratorConfigContent(reference);
        if (config != null) {
            integrator.getIntegratorConfiguration().loadConfigurationForIntegrator(config);
        }
    }
}
